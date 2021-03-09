// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.weaponFeatures.systems;

import org.joml.Vector3fc;
import org.terasology.combatSystem.hurting.HurtEvent;
import org.terasology.combatSystem.weaponFeatures.components.MeleeComponent;
import org.terasology.combatSystem.weaponFeatures.events.MeleeEvent;
import org.terasology.combatSystem.weaponFeatures.events.PrimaryAttackEvent;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;

/**
 * Manages meelee attacks
 */
@RegisterSystem
public class MeleeAttackSystem extends BaseComponentSystem {

    /**
     * Triggers when a primaryAttack is made with a melee weapon, and converts it to a melee attack
     *
     * @param event primary attack event
     * @param entity entity attacking
     */
    @ReceiveEvent(components = MeleeComponent.class)
    public void meleePrimaryAttack(PrimaryAttackEvent event, EntityRef entity) {
        melee(entity, event.getTarget(), event.getOrigin(), event.getHitPosition());

    }

    /**
     * Launch a melee attack
     *
     * @param event melee event
     * @param entity entity attacking
     */
    public void meleeing(MeleeEvent event, EntityRef entity) {
        melee(entity, event.getTarget(), event.getWeaponLoc(), event.getTargetHitLoc());
    }

    //------------------------------private methods----------------------------

    /**
     * Damages the target if the target is in range
     *
     * @param entity The entity attacking
     * @param target The target of the attack
     * @param weaponLoc The location the attack is coming from in the world
     * @param targetHitLoc The location the attack is hitting in the world
     */
    private void melee(EntityRef entity, EntityRef target, Vector3fc weaponLoc, Vector3fc targetHitLoc) {
        MeleeComponent melee = entity.getComponent(MeleeComponent.class);
        if (!melee.primaryAttack) {
            return;
        }

        if (weaponLoc == null || targetHitLoc == null) {
            return;
        }

        float distanceSq = weaponLoc.distanceSquared(targetHitLoc);

        if (distanceSq <= (melee.range * melee.range)) {
            // damage the other entity
            entity.send(new HurtEvent(target));
        }
    }
}
