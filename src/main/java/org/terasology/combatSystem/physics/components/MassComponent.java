package org.terasology.combatSystem.physics.components;

import org.terasology.entitySystem.Component;
import org.terasology.math.geom.Vector3f;

public class MassComponent implements Component{
    public float mass = 10.0f;
    
    public Vector3f velocity = new Vector3f();
    public Vector3f acceleration = new Vector3f();
    public Vector3f force = new Vector3f();
    
    public Vector3f friction = new Vector3f();

}
