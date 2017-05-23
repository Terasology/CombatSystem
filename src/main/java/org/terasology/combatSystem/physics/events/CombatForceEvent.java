package org.terasology.combatSystem.physics.events;

import org.terasology.entitySystem.event.Event;
import org.terasology.math.geom.Vector3f;

public class CombatForceEvent implements Event{
    Vector3f force;
    
    public CombatForceEvent(Vector3f force){
        this.force = force;
    }
    
    public Vector3f getForce(){
        return force;
    }

}
