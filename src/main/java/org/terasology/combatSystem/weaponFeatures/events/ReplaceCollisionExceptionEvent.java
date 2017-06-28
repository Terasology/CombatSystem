package org.terasology.combatSystem.weaponFeatures.events;

import java.util.List;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.Event;

import com.google.api.client.util.Lists;

public class ReplaceCollisionExceptionEvent implements Event{
    List<EntityRef> exceptions;
    
    public ReplaceCollisionExceptionEvent(){
        
    }
    
    public ReplaceCollisionExceptionEvent(List<EntityRef> exceptions){
        this.exceptions = exceptions;
    }
    
    public ReplaceCollisionExceptionEvent(EntityRef entity){
        exceptions = Lists.<EntityRef>newArrayList();
        exceptions.add(entity);
    }
    
    public List<EntityRef> getCollisionExceptionsList(){
        return exceptions;
    }
}
