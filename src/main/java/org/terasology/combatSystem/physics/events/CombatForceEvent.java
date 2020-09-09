// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.physics.events;

import org.terasology.combatSystem.physics.components.MassComponent;
import org.terasology.engine.entitySystem.event.Event;
import org.terasology.math.geom.Vector3f;

/**
 * Add <b>Force</b> to entities with {@code MassComponent}
 * <p>
 * this changes {@code MassComponent#force} variable in {@link MassComponent}
 */
public class CombatForceEvent implements Event {
    Vector3f force;

    public CombatForceEvent(Vector3f force) {
        this.force = force;
    }

    public Vector3f getForce() {
        return force;
    }

}
