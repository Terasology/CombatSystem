package org.terasology.combatSystem.physics.systems;

import java.util.Iterator;
import java.util.List;

import org.terasology.combatSystem.physics.components.CollisionExceptionsComponent;
import org.terasology.combatSystem.physics.events.AddCollisionExceptionEvent;
import org.terasology.combatSystem.physics.events.RemoveCollisionExceptionEvent;
import org.terasology.combatSystem.physics.events.ReplaceCollisionExceptionEvent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.EventPriority;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.physics.events.CollideEvent;
import org.terasology.sensors.EntitySensedEvent;

import com.google.common.collect.Lists;

/**
 * handles the addition or removal of collision exceptions as well as takes care of collisions
 * with exceptions.
 */
@RegisterSystem
public class CollisionExceptionsHandlingSystem extends BaseComponentSystem{
    /**
     * this handles the consuming of {@code CollideEvent} in case the entity collided with an exception.
     * @param event
     * @param entity
     */
    @ReceiveEvent(components = CollisionExceptionsComponent.class, priority = EventPriority.PRIORITY_CRITICAL)
    public void avoidCollisionWithExceptions(CollideEvent event, EntityRef entity){
        EntityRef otherEntity = event.getOtherEntity();
        
        if(checkCollisionWithAllExceptions(otherEntity.getId(), entity)){
            event.consume();
        }
        else if(checkCollisionWithAllExceptions(entity.getId(), otherEntity)){
            event.consume();
        }
    }
    
    /**
     * this handles consuming of {@code EntitySensedEvent} if the entity sensed is an exception.
     * @param event
     * @param entity
     */
    @ReceiveEvent(components = CollisionExceptionsComponent.class, priority = EventPriority.PRIORITY_CRITICAL)
    public void avoidSensingExceptions(EntitySensedEvent event, EntityRef entity){
        EntityRef otherEntity = event.getEntity();
        
        if(checkCollisionWithAllExceptions(otherEntity.getId(), entity)){
            event.consume();
        }
        else if(checkCollisionWithAllExceptions(entity.getId(), otherEntity)){
            event.consume();
        }
    }
    
    /**
     * handles the addition of an entity as collision exception for given entity.
     * @param event
     * @param entity
     */
    @ReceiveEvent
    public void addException(AddCollisionExceptionEvent event, EntityRef entity){
        CollisionExceptionsComponent exceptions = entity.getComponent(CollisionExceptionsComponent.class);
        if(exceptions == null){
            exceptions = new CollisionExceptionsComponent();
        }
        
        List<EntityRef> exceptionsList = event.getCollisionExceptionsList();
        
        if(exceptionsList != null){
            for(EntityRef exceptionEntity : exceptionsList){
                if(exceptionEntity != null && !exceptionEntity.equals(entity)){
                    if(!exceptions.exceptions.contains(exceptionEntity)){
                        exceptions.exceptions.add(exceptionEntity);
                    }
                }
            }
        }
        
        entity.addOrSaveComponent(exceptions);
    }
    
    /**
     * handles removal of an entity as collision exception of given entity.
     * @param event
     * @param entity
     */
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
    
    /**
     * handles replacing of current collision exceptions with the new ones for a given entity.
     * @param event
     * @param entity
     */
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
    
    /**
     * checks if the entity that was collided with is an exception or not.
     * @param otherEntityId
     * @param entity
     * @return
     */
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
