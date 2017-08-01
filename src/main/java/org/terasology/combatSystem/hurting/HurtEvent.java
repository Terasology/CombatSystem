package org.terasology.combatSystem.hurting;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.AbstractConsumableEvent;

/**
 * Used to hurt the <b>target</b> entity by the amount specified in {@code HurtingComponent}
 */
public class HurtEvent extends AbstractConsumableEvent{
    
    /**
     * the target entity which is to be hurt.
     */
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
