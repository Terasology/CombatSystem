/*
 * Copyright 2019 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.combatSystem.hurting;

import java.util.Random;

import org.terasology.alterationEffects.damageOverTime.DamageOverTimeAlterationEffect;
import org.terasology.alterationEffects.speed.StunAlterationEffect;
import org.terasology.combatSystem.weaponFeatures.OwnerSpecific;
import org.terasology.context.Context;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.EventPriority;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.characters.CharacterComponent;
import org.terasology.logic.characters.CharacterImpulseEvent;
import org.terasology.logic.health.event.DoDamageEvent;
import org.terasology.logic.health.HealthComponent;
import org.terasology.logic.notifications.NotificationMessageEvent;
import org.terasology.registry.In;

/**
 * This system handles all the tasks related to hurting an entity in <b>CombatSystem</b> module.
 */
@RegisterSystem
public class HurtingHandlingSystem extends BaseComponentSystem {

    @In
    private Context context;

    /**
     * This event handler handles the hurting of target entity by the amount and damage types
     * given in {@link HurtingComponent}.
     *
     * @param event
     * @param entity
     */
    @ReceiveEvent(priority = EventPriority.PRIORITY_TRIVIAL)
    public void hurting(HurtEvent event, EntityRef entity, HurtingComponent hurtingComponent) {

        EntityRef otherEntity = event.getTarget();
        if (otherEntity == null || otherEntity == EntityRef.NULL) {
            return;
        }

        EntityRef instigator = OwnerSpecific.getUltimateOwner(entity);

        switch (hurtingComponent.hurtingType) {
            case "attract":
                if (otherEntity.hasComponent(CharacterComponent.class)) {
                    otherEntity.send(new CharacterImpulseEvent(event.getImpulseDirection().negate().mul(hurtingComponent.amount)));
                }
                break;
            case "repulse":
                if (otherEntity.hasComponent(CharacterComponent.class)) {
                    otherEntity.send(new CharacterImpulseEvent(event.getImpulseDirection().mul(hurtingComponent.amount)));
                }
                break;
            case "stun":
                if (otherEntity.hasComponent(CharacterComponent.class)) {
                    StunAlterationEffect stunAlterationEffect = new StunAlterationEffect(context);
                    stunAlterationEffect.applyEffect(instigator, otherEntity, hurtingComponent.amount, hurtingComponent.duration);
                }
                break;
            case "dot":
                if (otherEntity.hasComponent(CharacterComponent.class)) {
                    DamageOverTimeAlterationEffect dotAlterationEffect = new DamageOverTimeAlterationEffect(context);
                    dotAlterationEffect.applyEffect(instigator, otherEntity, hurtingComponent.amount, hurtingComponent.duration);
                }
                break;
            default:
                if (otherEntity.hasComponent(HealthComponent.class)) {
                    otherEntity.send(new DoDamageEvent(hurtingComponent.amount, hurtingComponent.damageType, instigator, entity));
                    otherEntity.send(new NotificationMessageEvent(new String(hurtingComponent.amount + " damage dealt.."), entity));
                }
        }
    }

    /**
     * This event handler is responsible for doing a critical damage if the attacking entity has
     * a {@link CritDamageComponent} and than saving the new amount in {@code HurtingComponent}.
     *
     * @param event
     * @param entity
     */
    @ReceiveEvent(components = {HurtingComponent.class, CritDamageComponent.class}, priority = EventPriority.PRIORITY_LOW)
    public void critHurting(HurtEvent event, EntityRef entity) {
        HurtingComponent hurting = entity.getComponent(HurtingComponent.class);
        CritDamageComponent critDamage = entity.getComponent(CritDamageComponent.class);

        EntityRef otherEntity = event.getTarget();
        if (otherEntity == null || otherEntity == EntityRef.NULL) {
            return;
        }

        if (otherEntity.hasComponent(HealthComponent.class)) {
            EntityRef instigator = OwnerSpecific.getUltimateOwner(entity);

            Random rand = new Random();
            int value = rand.nextInt(100);

            // We take the crit chances to be 10% approx and damage doubles when crit
            if (value < critDamage.critChance) {
                int totalAmount = (int) (hurting.amount * critDamage.critFactor);
                otherEntity.send(new DoDamageEvent(totalAmount, hurting.damageType, instigator, entity));
                otherEntity.send(new NotificationMessageEvent(new String(totalAmount + "crit! damage dealt.."), entity));
            } else {
                otherEntity.send(new DoDamageEvent(hurting.amount, hurting.damageType, instigator, entity));
                otherEntity.send(new NotificationMessageEvent(new String(hurting.amount + " damage dealt.."), entity));
            }
        }
    }

    /**
     * This event handler is responsible for dealing random damage between the specified range
     * if {@link RandomDamageComponent} is present in attacking entity and saving the amount in
     * {@code HurtingComponent}.
     *
     * @param event
     * @param entity
     */
    @ReceiveEvent(components = {HurtingComponent.class, RandomDamageComponent.class})
    public void randomDamage(HurtEvent event, EntityRef entity) {
        RandomDamageComponent randomDamage = entity.getComponent(RandomDamageComponent.class);
        HurtingComponent hurting = entity.getComponent(HurtingComponent.class);

        Random rand = new Random();

        hurting.amount = rand.nextInt(randomDamage.maxDamage - randomDamage.minDamage + 1) + randomDamage.minDamage;

        entity.saveComponent(hurting);
    }

}
