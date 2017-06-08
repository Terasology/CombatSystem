package org.terasology.combatSystem.weaponFeatures.systems;

import java.util.Iterator;
import java.util.List;

import org.terasology.combatSystem.OwnerCollisionState;
import org.terasology.combatSystem.physics.components.CollisionExceptionsComponent;
import org.terasology.combatSystem.physics.components.GravityComponent;
import org.terasology.combatSystem.physics.components.MassComponent;
import org.terasology.combatSystem.weaponFeatures.components.BounceComponent;
import org.terasology.combatSystem.weaponFeatures.components.HurtingComponent;
import org.terasology.combatSystem.weaponFeatures.components.ShooterComponent;
import org.terasology.combatSystem.weaponFeatures.components.StickComponent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.entity.lifecycleEvents.OnActivatedComponent;
import org.terasology.entitySystem.entity.lifecycleEvents.OnChangedComponent;
import org.terasology.entitySystem.event.EventPriority;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.health.DoDamageEvent;
import org.terasology.logic.health.HealthComponent;
import org.terasology.logic.location.Location;
import org.terasology.logic.location.LocationComponent;
import org.terasology.math.geom.Quat4f;
import org.terasology.math.geom.Vector3f;
import org.terasology.physics.components.TriggerComponent;
import org.terasology.physics.events.CollideEvent;

import com.google.common.collect.Lists;

@RegisterSystem(RegisterMode.AUTHORITY)
public class CollisionHandlingSystem extends BaseComponentSystem{
    
    @ReceiveEvent(components = ShooterComponent.class)
    public void resolveShooterState(OnActivatedComponent event, EntityRef entity){
        updateExceptionListForShooterComponent(entity);
    }
    
    @ReceiveEvent(components = ShooterComponent.class)
    public void updateShooterState(OnChangedComponent event, EntityRef entity){
        updateExceptionListForShooterComponent(entity);
    }
    
    @ReceiveEvent(priority = EventPriority.PRIORITY_HIGH, components = {CollisionExceptionsComponent.class})
    public void avoidCollisionWithExceptions(CollideEvent event, EntityRef entity){
        EntityRef otherEntity = event.getOtherEntity();
        
        if(checkCollisionWithAllExceptions(otherEntity.getId(), entity)){
            event.consume();
        }
    }
    
    @ReceiveEvent(components = {StickComponent.class})
    public void sticking(CollideEvent event, EntityRef entity){
        EntityRef otherEntity = event.getOtherEntity();
        LocationComponent location = entity.getComponent(LocationComponent.class);
        LocationComponent otherEntityLocation = otherEntity.getComponent(LocationComponent.class);
        if(location != null && otherEntityLocation != null){
            // offset of the point of collision on the colliding entity to the
            // location of the colliding entity
            Vector3f offset = event.getOtherEntityContactPoint();
            offset.sub(otherEntityLocation.getWorldPosition());
            
            // to find quaternion rotation that if applied to quaternion 1 (the one which is
            // inversed) would result in quaternion 2 (the one which is multiplied)
            Quat4f relativeRot = new Quat4f(otherEntityLocation.getWorldRotation());
            relativeRot.inverse();
            relativeRot.mul(location.getWorldRotation());
            
            // world scale of the entity.
            float scale = location.getWorldScale();
            
            // resting all the movements of the entity
            MassComponent body = entity.getComponent(MassComponent.class);
            body.acceleration.set(0, 0, 0);
            body.velocity.set(0, 0, 0);
            body.force.set(0, 0, 0);
            
            entity.removeComponent(TriggerComponent.class);
            
            // attaching entity to otherEntity so that it always follows otherEntity
            Location.attachChild(otherEntity, entity, offset, relativeRot, scale);
            
            entity.saveComponent(body);
            entity.saveComponent(location);
            
            // damage the other entity
            if(otherEntity.hasComponent(HealthComponent.class)){
                HurtingComponent hurting = entity.getComponent(HurtingComponent.class);
                EntityRef instigator = getUltimateOwner(entity);
                
                
                if(hurting != null){
                    otherEntity.send(new DoDamageEvent(hurting.amount, hurting.damageType,
                            instigator, entity));
                }
            }
        }
        
        entity.removeComponent(GravityComponent.class);
        
    }
    
    @ReceiveEvent(components = {BounceComponent.class})
    public void bouncing(CollideEvent event, EntityRef entity){
        MassComponent mass = entity.getComponent(MassComponent.class);
        LocationComponent location = entity.getComponent(LocationComponent.class);
        if(mass == null || location == null){
            return;
        }
        
        Vector3f bounceDir = event.getNormal();
        bounceDir.normalize();
        bounceDir.negate();
        bounceDir.scale(2*bounceDir.dot(mass.velocity));
        bounceDir.negate();
        
        mass.velocity.add(bounceDir);
        
        CollisionExceptionsComponent exceptions = entity.getComponent(CollisionExceptionsComponent.class);
        if(exceptions == null){
            exceptions = new CollisionExceptionsComponent();
        }
        exceptions.exceptions.add(event.getOtherEntity());
        
        location.setWorldPosition(event.getEntityContactPoint());
        
        entity.saveComponent(mass);
        entity.addOrSaveComponent(exceptions);
    }
    
    //-------------private methods used in this system---------------
    
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
    
    // get the ultimate owner of the entity. The owner of the entity may have an owner of its
    // own, that ultimate owner is returned from this method through recursion
    private EntityRef getUltimateOwner(EntityRef entity){
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
    
    private EntityRef recursiveOwner(EntityRef entity){
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
    
    private EntityRef getFirstOwner(EntityRef entity){
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
    
    private List<EntityRef> getAllOwners(EntityRef entity){
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
    
    private void updateExceptionListForShooterComponent(EntityRef entity){
        ShooterComponent shooter = entity.getComponent(ShooterComponent.class);
        if(shooter == null){
            return;
        }
        
        CollisionExceptionsComponent exceptions = entity.getComponent(CollisionExceptionsComponent.class);
        if(exceptions == null){
            exceptions = new CollisionExceptionsComponent();
        }
        
        if(shooter.state == OwnerCollisionState.DISABLED){
            List<EntityRef> entityList = getAllOwners(entity);
            exceptions.exceptions.removeAll(entityList);
            exceptions.exceptions.addAll(entityList);
        }
        else if(shooter.state == OwnerCollisionState.DISABLED_WITH_DIRECT_OWNER){
            exceptions.exceptions.removeAll(getAllOwners(entity));
            exceptions.exceptions.add(getFirstOwner(entity));
        }
        else if(shooter.state == OwnerCollisionState.DISABLED_WITH_ULTIMATE_OWNER){
            exceptions.exceptions.removeAll(getAllOwners(entity));
            exceptions.exceptions.add(getUltimateOwner(entity));
        }
        else if(shooter.state == OwnerCollisionState.ENABLED){
            exceptions.exceptions.removeAll(getAllOwners(entity));
        }
    }

}
