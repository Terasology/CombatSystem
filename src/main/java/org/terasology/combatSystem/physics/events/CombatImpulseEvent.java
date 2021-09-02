// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.physics.events;

import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.terasology.combatSystem.physics.components.MassComponent;
import org.terasology.engine.entitySystem.event.Event;

/**
 * Add <>bImpulse</b> to entities with {@code MassComponent}
 * <b>
 * this changes {@code MassComponent#velocity} variable in {@link MassComponent}
 */
public class CombatImpulseEvent implements Event {
    private Vector3f impulse = new Vector3f();

    public CombatImpulseEvent(Vector3fc impulse) {
        this.impulse.set(impulse);
    }

    public Vector3fc getImpulse() {
        return impulse;
    }
}
