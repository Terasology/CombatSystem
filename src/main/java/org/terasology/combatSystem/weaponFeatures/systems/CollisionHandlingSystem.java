package org.terasology.combatSystem.weaponFeatures.systems;

import org.terasology.combatSystem.physics.components.GravityComponent;
import org.terasology.combatSystem.physics.components.MassComponent;
import org.terasology.combatSystem.weaponFeatures.components.HurtingComponent;
import org.terasology.combatSystem.weaponFeatures.components.ShooterComponent;
import org.terasology.combatSystem.weaponFeatures.components.StickComponent;
import org.terasology.entitySystem.entity.EntityRef;
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

@RegisterSystem(RegisterMode.AUTHORITY)
public class CollisionHandlingSystem extends BaseComponentSystem{
    
    @ReceiveEvent(priority = EventPriority.PRIORITY_HIGH, components = {ShooterComponent.class})
    public void avoidCollisionWithOwner(CollideEvent event, EntityRef entity){
        EntityRef otherEntity = event.getOtherEntity();
        
        if(checkCollisionWithAllOwners(otherEntity.getId(), entity)){
            event.consume();
        }
//        if(checkCollisionWithFirstOwner(otherEntity.getId(), entity)){
//            event.consume();
//        }
        
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
            
            // attaching entity to otherEntity so that it always follows otherEntity
            Location.attachChild(otherEntity, entity, offset, relativeRot, scale);
            
            entity.removeComponent(TriggerComponent.class);
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
    
    //-------------private methods used in this system---------------
    
    // checks if the id is equal to any owners of the entity. For eg. the owner of the owner
    // of the entity
    private boolean checkCollisionWithAllOwners(long otherEntityId, EntityRef entity){
        ShooterComponent shooter = entity.getComponent(ShooterComponent.class);
        if(shooter == null){
            return false;
        }
        if(shooter.shooter == EntityRef.NULL || shooter.shooter == null){
            return false;
        }
        
        if(otherEntityId == shooter.shooter.getId()){
            return true;
        }
        else{
            return checkCollisionWithAllOwners(otherEntityId, shooter.shooter);
        }
    }
    
    // checks if the Id is equal to the owner of the given entity
    private boolean checkCollisionWithFirstOwner(long otherEntityId, EntityRef entity){
        ShooterComponent shooter = entity.getComponent(ShooterComponent.class);
        if(shooter == null){
            return false;
        }
        if(shooter.shooter == EntityRef.NULL || shooter.shooter == null){
            return false;
        }
        
        if(otherEntityId == shooter.shooter.getId()){
            return true;
        }
        else{
            return false;
        }
    }
    
    // get the ultimate owner of the entity. The owner of the entity may have an owner of its
    // own, that ultimate owner is returned from this method through recursion
    private EntityRef getUltimateOwner(EntityRef entity){
        ShooterComponent shooter = entity.getComponent(ShooterComponent.class);
        
        if(shooter == null){
            return entity;
        }
        if(shooter.shooter == EntityRef.NULL || shooter.shooter == null){
            return entity;
        }
        else{
            return getUltimateOwner(shooter.shooter);
        }
    }

}
