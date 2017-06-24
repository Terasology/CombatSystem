package org.terasology.combatSystem.weaponFeatures.events;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.Event;

public class HurtEvent implements Event{
    EntityRef target = EntityRef.NULL;
    
    public HurtEvent(){
        
    }
    
    public HurtEvent(EntityRef entity){
        target = entity;
    }
    
    public EntityRef getTarget(){
        return target;
    }

}
