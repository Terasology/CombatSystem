package org.terasology.combatSystem.weaponFeatures.systems.collisionHandling;

import java.util.Iterator;
import java.util.List;

import org.terasology.combatSystem.physics.components.CollisionExceptionsComponent;
import org.terasology.combatSystem.weaponFeatures.events.AddExceptionEvent;
import org.terasology.combatSystem.weaponFeatures.events.RemoveExceptionEvent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.EventPriority;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.physics.events.CollideEvent;

@RegisterSystem(RegisterMode.AUTHORITY)
public class CollisionExceptionsHandlingSystem extends CollisionHandlingSystem{
    
    @ReceiveEvent(components = CollisionExceptionsComponent.class, priority = EventPriority.PRIORITY_CRITICAL)
    public void avoidCollisionWithExceptions(CollideEvent event, EntityRef entity){
        EntityRef otherEntity = event.getOtherEntity();
        
        if(checkCollisionWithAllExceptions(otherEntity.getId(), entity)){
            event.consume();
        }
    }
    
    @ReceiveEvent
    public void addException(AddExceptionEvent event, EntityRef entity){
        CollisionExceptionsComponent exceptions = entity.getComponent(CollisionExceptionsComponent.class);
        if(exceptions == null){
            exceptions = new CollisionExceptionsComponent();
        }
        
        EntityRef exception = event.getException();
        List<EntityRef> exceptionsList = event.getExceptionsList();
        if(exception != null){
            if(!exceptions.exceptions.contains(exception)){
                exceptions.exceptions.add(exception);
            }
        }
        
        if(exceptionsList != null){
            Iterator<EntityRef> entities = exceptionsList.iterator();
            while(entities.hasNext()){
                EntityRef exceptionEntity = entities.next();
                if(exceptionEntity != null){
                    if(!exceptions.exceptions.contains(exceptionEntity)){
                        exceptions.exceptions.add(exceptionEntity);
                    }
                }
            }
        }
        
        entity.addOrSaveComponent(exceptions);
    }
    
    @ReceiveEvent
    public void removeException(RemoveExceptionEvent event, EntityRef entity){
        CollisionExceptionsComponent exceptions = entity.getComponent(CollisionExceptionsComponent.class);
        if(exceptions == null){
            exceptions = new CollisionExceptionsComponent();
        }
        
        EntityRef exception = event.getException();
        List<EntityRef> exceptionsList = event.getExceptionsList();
        if(exception != null){
            if(exceptions.exceptions.contains(exception)){
                exceptions.exceptions.remove(exception);
            }
        }
        
        if(exceptionsList != null){
            Iterator<EntityRef> entities = exceptionsList.iterator();
            while(entities.hasNext()){
                EntityRef exceptionEntity = entities.next();
                if(exceptionEntity != null){
                    if(exceptions.exceptions.contains(exceptionEntity)){
                        exceptions.exceptions.remove(exceptionEntity);
                    }
                }
            }
        }

        entity.addOrSaveComponent(exceptions);
    }

}
