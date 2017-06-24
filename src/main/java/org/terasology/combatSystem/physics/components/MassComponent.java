package org.terasology.combatSystem.physics.components;

import org.terasology.entitySystem.Component;
import org.terasology.math.geom.Vector3f;

/**
 * Adds <b>translational motion</b> feature to an entity.
 * <p>
 * <b>NOTE</b>: It does not add <b>collider</b> like {@link RigidBodyComponent}.
 * {@link TriggerComponent} is used for collisions.
 */
public class MassComponent implements Component{
    public float mass = 10.0f;
    
    public Vector3f velocity = new Vector3f();
    public Vector3f acceleration = new Vector3f();
    public Vector3f force = new Vector3f();
    
    //TODO Implement the usage of this feature.
    public Vector3f friction = new Vector3f();

}
