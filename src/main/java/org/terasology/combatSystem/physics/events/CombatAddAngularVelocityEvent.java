package org.terasology.combatSystem.physics.events;

import org.terasology.entitySystem.event.Event;
import org.terasology.math.geom.Quat4f;

public class CombatAddAngularVelocityEvent implements Event{
    private Quat4f angularVelocity = null;
    
    public CombatAddAngularVelocityEvent(Quat4f angularVelocity){
        this.angularVelocity = angularVelocity;
    }
    
    public Quat4f getAngularVelocity(){
        return angularVelocity;
    }

}
