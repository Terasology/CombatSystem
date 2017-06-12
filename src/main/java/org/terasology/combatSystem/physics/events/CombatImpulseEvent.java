package org.terasology.combatSystem.physics.events;

import org.terasology.entitySystem.event.Event;
import org.terasology.math.geom.Vector3f;

public class CombatImpulseEvent implements Event{
    Vector3f impulse;
    
    public CombatImpulseEvent(Vector3f impulse){
        this.impulse = impulse;
    }
    
    public Vector3f getImpulse(){
        return impulse;
    }

}
