package org.terasology.combatSystem.weaponFeatures.events;

import java.util.List;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.Event;

public class RemoveExceptionEvent implements Event{
    EntityRef exception;
    List<EntityRef> exceptions;
    
    public RemoveExceptionEvent(){
        
    }
    
    public RemoveExceptionEvent(List<EntityRef> exceptions){
        this.exceptions = exceptions;
    }
    
    public RemoveExceptionEvent(EntityRef entity){
        exception = entity;
    }
    
    public EntityRef getException(){
        return exception;
    }
    
    public List<EntityRef> getExceptionsList(){
        return exceptions;
    }

}
