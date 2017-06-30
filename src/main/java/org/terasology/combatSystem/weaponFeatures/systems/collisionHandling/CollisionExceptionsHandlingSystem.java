package org.terasology.combatSystem.weaponFeatures.systems.collisionHandling;

import java.util.Iterator;
import java.util.List;

import org.terasology.combatSystem.physics.components.CollisionExceptionsComponent;
import org.terasology.combatSystem.weaponFeatures.events.AddCollisionExceptionEvent;
import org.terasology.combatSystem.weaponFeatures.events.RemoveCollisionExceptionEvent;
import org.terasology.combatSystem.weaponFeatures.events.ReplaceCollisionExceptionEvent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.EventPriority;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.physics.events.CollideEvent;

import com.google.common.collect.Lists;

@RegisterSystem
public class CollisionExceptionsHandlingSystem extends BaseComponentSystem{
    
    @ReceiveEvent(components = CollisionExceptionsComponent.class, priority = EventPriority.PRIORITY_CRITICAL)
    public void avoidCollisionWithExceptions(CollideEvent event, EntityRef entity){
        EntityRef otherEntity = event.getOtherEntity();
        
        if(checkCollisionWithAllExceptions(otherEntity.getId(), entity)){
            event.consume();
        }
    }
    
    @ReceiveEvent
    public void addException(AddCollisionExceptionEvent event, EntityRef entity){
        CollisionExceptionsComponent exceptions = entity.getComponent(CollisionExceptionsComponent.class);
        if(exceptions == null){
            exceptions = new CollisionExceptionsComponent();
        }
        
        List<EntityRef> exceptionsList = event.getCollisionExceptionsList();
        
        if(exceptionsList != null){
            for(EntityRef exceptionEntity : exceptionsList){
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
    public void removeException(RemoveCollisionExceptionEvent event, EntityRef entity){
        CollisionExceptionsComponent exceptions = entity.getComponent(CollisionExceptionsComponent.class);
        if(exceptions == null){
            exceptions = new CollisionExceptionsComponent();
        }
        
        List<EntityRef> exceptionsList = event.getCollisionExceptionsList();
        
        if(exceptionsList != null){
            for(EntityRef exceptionEntity : exceptionsList){
                if(exceptionEntity != null){
                    if(exceptions.exceptions.contains(exceptionEntity)){
                        exceptions.exceptions.remove(exceptionEntity);
                    }
                }
            }
        }

        entity.addOrSaveComponent(exceptions);
    }
    
    @ReceiveEvent
    public void replaceException(ReplaceCollisionExceptionEvent event, EntityRef entity){
        CollisionExceptionsComponent exceptions = entity.getComponent(CollisionExceptionsComponent.class);
        if(exceptions == null){
            exceptions = new CollisionExceptionsComponent();
        }
        
        List<EntityRef> exceptionsList = event.getCollisionExceptionsList();
        
        if(exceptionsList == null){
            exceptionsList = Lists.<EntityRef>newArrayList();
        }
        
        exceptions.exceptions.clear();
        exceptions.exceptions.addAll(exceptionsList);

        entity.addOrSaveComponent(exceptions);
    }
    
    //-----------------------private methods-----------------------------
    
    private boolean checkCollisionWithAllExceptions(long otherEntityId, EntityRef entity){
        CollisionExceptionsComponent exceptions = entity.getComponent(CollisionExceptionsComponent.class);
        if(exceptions == null){
            return false;
        }
        
        Iterator<EntityRef> entities = exceptions.exceptions.iterator();
        while(entities.hasNext()){
            EntityRef exception = entities.next();
            if(exception.getId() == otherEntityId){
                return true;
            }
        }
        
        return false;
    }

}
