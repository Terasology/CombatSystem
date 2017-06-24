package org.terasology.combatSystem.weaponFeatures.events;

import java.util.List;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.Event;

import com.google.api.client.util.Lists;

public class RemoveCollisionExceptionEvent implements Event{
    List<EntityRef> exceptions;
    
    public RemoveCollisionExceptionEvent(){
        
    }
    
    public RemoveCollisionExceptionEvent(List<EntityRef> exceptions){
        this.exceptions = exceptions;
    }
    
    public RemoveCollisionExceptionEvent(EntityRef entity){
        exceptions = Lists.<EntityRef>newArrayList();
        exceptions.add(entity);
    }
    
    public List<EntityRef> getCollisionExceptionsList(){
        return exceptions;
    }

}
