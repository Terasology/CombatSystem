// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.hurting;

import org.terasology.combatSystem.weaponFeatures.OwnerSpecific;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.EventPriority;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.notifications.NotificationMessageEvent;
import org.terasology.logic.health.HealthComponent;
import org.terasology.logic.health.event.DoDamageEvent;

import java.util.Random;

/**
 * This system handles all the tasks related to hurting an entity in <b>CombatSystem</b> module.
 */
@RegisterSystem
public class HurtingHandlingSystem extends BaseComponentSystem {

    /**
     * This event handler handles the hurting of target entity by the amount and damage types
     * given in {@link HurtingComponent}.
     *
     * @param event
     * @param entity
     */
    @ReceiveEvent(components = HurtingComponent.class, priority = EventPriority.PRIORITY_TRIVIAL)
    public void hurting(HurtEvent event, EntityRef entity) {
        HurtingComponent hurting = entity.getComponent(HurtingComponent.class);
        EntityRef otherEntity = event.getTarget();
        if (otherEntity == null || otherEntity == EntityRef.NULL) {
            return;
        }

        if (otherEntity.hasComponent(HealthComponent.class)) {
            EntityRef instigator = OwnerSpecific.getUltimateOwner(entity);
            otherEntity.send(new DoDamageEvent(hurting.amount, hurting.damageType, instigator, entity));
            otherEntity.send(new NotificationMessageEvent(new String(hurting.amount + " damage dealt.."), entity));
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
