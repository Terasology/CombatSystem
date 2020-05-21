package org.terasology.combatSystem.weaponFeatures.events;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.Event;

/**
 * Can be used to stick to a target specified
 */
public class StickEvent implements Event{
    EntityRef target = EntityRef.NULL;
    
    /**
     * Creates a new event
     * @param otherEntity The entity to stick to
     */
    public StickEvent(EntityRef otherEntity){
        target = otherEntity;
    }
    
    /**
     * Gets the entity to stick to.
     * @return the entity to stick to.
     */
    public EntityRef getTarget(){
        return target;
    }
}
