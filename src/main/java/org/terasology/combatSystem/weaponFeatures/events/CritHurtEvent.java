package org.terasology.combatSystem.weaponFeatures.events;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.Event;

public class CritHurtEvent implements Event{
    EntityRef target = EntityRef.NULL;
    
    public CritHurtEvent(){
        
    }
    
    public CritHurtEvent(EntityRef entity){
        target = entity;
    }
    
    public EntityRef getTarget(){
        return target;
    }

}
