// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.physics.events;

import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.terasology.combatSystem.physics.components.MassComponent;
import org.terasology.gestalt.entitysystem.event.Event;

/**
 * Add <b>Force</b> to entities with {@code MassComponent}
 * <p>
 * this changes {@code MassComponent#force} variable in {@link MassComponent}
 */
public class CombatForceEvent implements Event {
    private Vector3f force = new Vector3f();

    public CombatForceEvent(Vector3f force) {
        this.force.set(force);
    }

    public Vector3fc getForce() {
        return force;
    }
}
