package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.entitySystem.Component;
import org.terasology.math.geom.Vector3f;
import org.terasology.network.Replicate;

public class BounceComponent implements Component{
    @Replicate
    public float bounceFactor = 0.5f;
    
    @Replicate
    public int maxPierceAngle = 1;                        // in degrees 
    
    @Replicate
    public Vector3f minVelocity = new Vector3f();
    
    @Replicate
    public int amount = 3;

}
