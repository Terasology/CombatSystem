package org.terasology.combatSystem.weaponFeatures.events;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.Event;

/**
 * Can be used to stick to a target specified in the event instead of only during collision
 */
public class StickEvent implements Event{
    EntityRef target = EntityRef.NULL;
    
    public StickEvent(EntityRef otherEntity){
        target = otherEntity;
    }
    
    public EntityRef getTarget(){
        return target;
    }
}
