// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.weaponFeatures.events;

import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.Event;

/**
 * Triggered in case of a melee attack
 */
public class MeleeEvent implements Event {
    EntityRef target = EntityRef.NULL;
    Vector3f weaponLoc;
    Vector3f targetHitLoc;

    /**
     * Creates a new instance of the event
     *
     * @param target the attack target
     * @param weaponLoc location of weapon
     * @param targetHitLoc location of target
     */
    public MeleeEvent(EntityRef target, Vector3f weaponLoc, Vector3f targetHitLoc) {
        this.target = target;
        this.weaponLoc = weaponLoc;
        this.targetHitLoc = targetHitLoc;
    }

    /**
     * Get the target entity
     *
     * @return target
     */
    public EntityRef getTarget() {
        return target;
    }

    /**
     * Get location of weapon in the world
     *
     * @return location of weapon
     */
    public Vector3fc getWeaponLoc() {
        return weaponLoc;
    }

    /**
     * Get target hit location in the world
     *
     * @return location of target hit
     */
    public Vector3fc getTargetHitLoc() {
        return targetHitLoc;
    }
}
