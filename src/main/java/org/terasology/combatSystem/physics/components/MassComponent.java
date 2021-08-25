// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.combatSystem.physics.components;

import org.joml.Vector3f;
import org.terasology.engine.network.Replicate;
import org.terasology.engine.physics.components.RigidBodyComponent;
import org.terasology.engine.physics.components.TriggerComponent;
import org.terasology.gestalt.entitysystem.component.Component;

/**
 * Adds <b>translational motion</b> feature to an entity.
 * <p>
 * <b>NOTE</b>: It does not add <b>collider</b> like {@link RigidBodyComponent}.
 * {@link TriggerComponent} is used for collisions.
 */
public class MassComponent implements Component<MassComponent> {
    @Replicate(initialOnly = true)
    public float mass = 10.0f;

    @Replicate
    public Vector3f velocity = new Vector3f();
    @Replicate
    public Vector3f acceleration = new Vector3f();
    @Replicate
    public Vector3f force = new Vector3f();

    @Override
    public void copyFrom(MassComponent other) {
        this.velocity.set(other.velocity);
        this.acceleration.set(other.acceleration);
        this.force.set(other.force);
        this.mass = other.mass;
    }
}
