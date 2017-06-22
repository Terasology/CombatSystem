package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.entitySystem.Component;
import org.terasology.math.geom.Vector3f;

public class BounceComponent implements Component{
    public float bounceFactor = 0.5f;
    public int maxPierceAngle = 1;                        // in degrees 
    public Vector3f minVelocity = new Vector3f();
    public int amount = 3;

}
