package org.terasology.combatSystem.physics.events;

import org.terasology.entitySystem.Component;
import org.terasology.math.geom.Vector3f;

public class CombatForceEvent implements Component{
    Vector3f force;
    
    public CombatForceEvent(Vector3f force){
        this.force = force;
    }
    
    public Vector3f getForce(){
        return force;
    }

}
