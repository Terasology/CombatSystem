package org.terasology.combatSystem.physics.components;

import org.joml.Vector3f;
import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.network.Replicate;
import org.terasology.engine.physics.components.RigidBodyComponent;
import org.terasology.engine.physics.components.TriggerComponent;

/**
 * Adds <b>translational motion</b> feature to an entity.
 * <p>
 * <b>NOTE</b>: It does not add <b>collider</b> like {@link RigidBodyComponent}.
 * {@link TriggerComponent} is used for collisions.
 */
public class MassComponent implements Component{
    @Replicate(initialOnly = true)
    public float mass = 10.0f;

    @Replicate
    public Vector3f velocity = new Vector3f();
    @Replicate
    public Vector3f acceleration = new Vector3f();
    @Replicate
    public Vector3f force = new Vector3f();

}
