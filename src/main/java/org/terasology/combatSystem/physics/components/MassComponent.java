package org.terasology.combatSystem.physics.components;

import org.terasology.entitySystem.Component;
import org.terasology.math.geom.Quat4f;
import org.terasology.math.geom.Vector3f;
import org.terasology.network.Replicate;
import org.terasology.physics.components.RigidBodyComponent;
import org.terasology.physics.components.TriggerComponent;

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
    
//    @Replicate
//    public Quat4f angularVelocity = new Quat4f(0, 0, 0, 1);
//    @Replicate
//    public Quat4f angularAccel = new Quat4f(0, 0, 0, 1);
//    @Replicate
//    public Quat4f angularForce = new Quat4f(0, 0, 0, 1);

}
