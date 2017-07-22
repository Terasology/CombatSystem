package org.terasology.combatSystem.hurting;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.AbstractConsumableEvent;

public class HurtEvent extends AbstractConsumableEvent{
    
    
    EntityRef target = EntityRef.NULL;
    
    public HurtEvent(){
        
    }
    
    public HurtEvent(EntityRef entity){
        this.target = entity;
    }
    
    public EntityRef getTarget(){
        return target;
    }

}
