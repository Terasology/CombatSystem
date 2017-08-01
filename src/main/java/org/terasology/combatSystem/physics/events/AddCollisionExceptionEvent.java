package org.terasology.combatSystem.physics.events;

import java.util.List;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.Event;

import com.google.common.collect.Lists;

/**
 * Used to add entities as exceptions to avoid collision with them.
 */
public class AddCollisionExceptionEvent implements Event{
    List<EntityRef> exceptions;
    
    public AddCollisionExceptionEvent(){
        
    }
    
    public AddCollisionExceptionEvent(List<EntityRef> exceptions){
        this.exceptions = exceptions;
    }
    
    public AddCollisionExceptionEvent(EntityRef entity){
        exceptions = Lists.<EntityRef>newArrayList();
        this.exceptions.add(entity);
    }
    
    public List<EntityRef> getCollisionExceptionsList(){
        return exceptions;
    }

}
