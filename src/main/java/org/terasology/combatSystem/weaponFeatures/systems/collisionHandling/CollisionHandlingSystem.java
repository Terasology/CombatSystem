package org.terasology.combatSystem.weaponFeatures.systems.collisionHandling;

import java.util.Iterator;
import java.util.List;

import org.terasology.combatSystem.OwnerCollisionState;
import org.terasology.combatSystem.physics.components.CollisionExceptionsComponent;
import org.terasology.combatSystem.weaponFeatures.components.ShooterComponent;
import org.terasology.combatSystem.weaponFeatures.components.hurting.HurtingComponent;
import org.terasology.combatSystem.weaponFeatures.events.AddExceptionEvent;
import org.terasology.combatSystem.weaponFeatures.events.RemoveExceptionEvent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.logic.health.DoDamageEvent;
import org.terasology.logic.health.HealthComponent;

import com.google.common.collect.Lists;

public class CollisionHandlingSystem extends BaseComponentSystem{
    
    //-------------protected methods used in this system---------------
    
    protected boolean checkCollisionWithAllExceptions(long otherEntityId, EntityRef entity){
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
    
    // get the ultimate owner of the entity. The owner of the entity may have an owner of its
    // own, that ultimate owner is returned from this method through recursion
    protected EntityRef getUltimateOwner(EntityRef entity){
        ShooterComponent shooter = entity.getComponent(ShooterComponent.class);
        
        if(shooter == null){
            return null;
        }
        if(shooter.shooter == EntityRef.NULL || shooter.shooter == null){
            return null;
        }
        else{
            return recursiveOwner(shooter.shooter);
        }
    }
    
    protected EntityRef recursiveOwner(EntityRef entity){
        ShooterComponent shooter = entity.getComponent(ShooterComponent.class);
        
        if(shooter == null){
            return entity;
        }
        if(shooter.shooter == EntityRef.NULL || shooter.shooter == null){
            return entity;
        }
        else{
            return recursiveOwner(shooter.shooter);
        }
    }
    
    protected void updateExceptionListForShooterComponent(EntityRef entity){
        ShooterComponent shooter = entity.getComponent(ShooterComponent.class);
        if(shooter == null){
            return;
        }
        
        if(shooter.state == OwnerCollisionState.DISABLED){
            entity.send(new AddExceptionEvent(getAllOwners(entity)));
        }
        else if(shooter.state == OwnerCollisionState.DISABLED_WITH_DIRECT_OWNER){
            entity.send(new RemoveExceptionEvent(getAllOwners(entity)));
            entity.send(new AddExceptionEvent(getFirstOwner(entity)));
        }
        else if(shooter.state == OwnerCollisionState.DISABLED_WITH_ULTIMATE_OWNER){
            entity.send(new RemoveExceptionEvent(getAllOwners(entity)));
            entity.send(new AddExceptionEvent(getUltimateOwner(entity)));
        }
        else if(shooter.state == OwnerCollisionState.ENABLED){
            entity.send(new RemoveExceptionEvent(getAllOwners(entity)));
        }
    }
    
    protected EntityRef getFirstOwner(EntityRef entity){
        ShooterComponent shooter = entity.getComponent(ShooterComponent.class);
        
        if(shooter == null){
            return null;
        }
        if(shooter.shooter == EntityRef.NULL || shooter.shooter == null){
            return null;
        }
        else{
            return shooter.shooter;
        }
    }
    
    protected List<EntityRef> getAllOwners(EntityRef entity){
        List<EntityRef> entityList = Lists.<EntityRef>newArrayList();
        
        EntityRef temp = getFirstOwner(entity);
        while(temp != null){
            entityList.add(temp);
            temp = getFirstOwner(temp);
        }
        
        if(entityList.isEmpty()){
            return null;
        }
        else{
            return entityList;
        }
    }
    
    // damage other entity
    protected void damageOtherEntity(EntityRef otherEntity, EntityRef entity, HurtingComponent hurting){
        if(otherEntity.hasComponent(HealthComponent.class)){
            EntityRef instigator = getUltimateOwner(entity);
            
            
            if(hurting != null){
                if(hurting.canHurt){
                    otherEntity.send(new DoDamageEvent(hurting.amount, hurting.damageType,
                            instigator, entity));
                }
            }
        }
    }

}
