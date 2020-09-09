// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.weaponFeatures.events;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.Event;
import org.terasology.math.geom.Vector3f;

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
    public Vector3f getWeaponLoc() {
        return weaponLoc;
    }

    /**
     * Get target hit location in the world
     *
     * @return location of target hit
     */
    public Vector3f getTargetHitLoc() {
        return targetHitLoc;
    }

}
