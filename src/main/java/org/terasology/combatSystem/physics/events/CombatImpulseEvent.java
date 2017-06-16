package org.terasology.combatSystem.physics.events;

import org.terasology.entitySystem.event.Event;
import org.terasology.math.geom.Vector3f;

/**
 * Add <>bImpulse</b> to entities with {@code MassComponent}
 * <b>
 * this changes {@code MassComponent#velocity} variable in {@link MassComponent}
 */
public class CombatImpulseEvent implements Event{
    Vector3f impulse;
    
    public CombatImpulseEvent(Vector3f impulse){
        this.impulse = impulse;
    }
    
    public Vector3f getImpulse(){
        return impulse;
    }

}
