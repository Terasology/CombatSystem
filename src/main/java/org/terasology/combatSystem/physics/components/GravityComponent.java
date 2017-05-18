package org.terasology.combatSystem.physics.components;

import org.terasology.entitySystem.Component;
import org.terasology.math.geom.Vector3f;

public class GravityComponent implements Component{
    public Vector3f gravityAccel = new Vector3f(0, -10.0f, 0);

}
