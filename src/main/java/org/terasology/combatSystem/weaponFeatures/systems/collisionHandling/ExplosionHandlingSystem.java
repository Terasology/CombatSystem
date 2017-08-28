package org.terasology.combatSystem.weaponFeatures.systems.collisionHandling;

import java.util.List;

import org.terasology.combatSystem.hurting.HurtEvent;
import org.terasology.combatSystem.hurting.HurtingComponent;
import org.terasology.combatSystem.physics.events.CombatImpulseEvent;
import org.terasology.combatSystem.weaponFeatures.components.ExplosionComponent;
import org.terasology.combatSystem.weaponFeatures.events.ExplosionEvent;
import org.terasology.engine.Time;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.EventPriority;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.logic.health.DestroyEvent;
import org.terasology.logic.health.EngineDamageTypes;
import org.terasology.logic.health.HealthComponent;
import org.terasology.logic.location.LocationComponent;
import org.terasology.math.AABB;
import org.terasology.math.geom.Vector3f;
import org.terasology.physics.Physics;
import org.terasology.physics.events.ImpulseEvent;
import org.terasology.registry.In;

@RegisterSystem
public class ExplosionHandlingSystem extends BaseComponentSystem implements UpdateSubscriberSystem{
    @In
    private Time time;
    @In
    private EntityManager entityManager;
    @In
    private Physics physics;
    
    @ReceiveEvent(components = ExplosionComponent.class)
    public void explosion(ExplosionEvent event, EntityRef entity){
        ExplosionComponent explosion = entity.getComponent(ExplosionComponent.class);
        if(explosion == null){
            return;
        }
        
        explosion.explosionStartTime = time.getGameTime();
        
        if(explosion.explosionDelayTime > 0.0f){
            return;
        }
        
        explosion.explosionStarted = true;
        
        entity.saveComponent(explosion);
        
        doExplosion(entity);
    }
    
    @ReceiveEvent(components = ExplosionComponent.class, priority = EventPriority.PRIORITY_HIGH)
    public void explosionOnDestroy(DestroyEvent event, EntityRef entity){
        ExplosionComponent explosion = entity.getComponent(ExplosionComponent.class);
        if(explosion.explosionStarted){
            return;
        }
        
        explosion.explosionStartTime = time.getGameTime();
        
        explosion.explosionStarted = true;
        
        entity.saveComponent(explosion);
        
        doExplosion(entity);
    }

    @Override
    public void update(float delta) {
        // TODO Auto-generated method stub
        Iterable<EntityRef> entityList = entityManager.getEntitiesWith(ExplosionComponent.class);
        for(EntityRef entity : entityList){
            ExplosionComponent explosion = entity.getComponent(ExplosionComponent.class);
            if(explosion.explosionStartTime < 0.0f){
                continue;
            }
            float currentGameTime = time.getGameTime();
            
            // starts a delayed explosion;
            if(!explosion.explosionStarted){
                if(currentGameTime >= (explosion.explosionStartTime + explosion.explosionDelayTime)){
                    explosion.explosionStarted = true;
                    
                    entity.saveComponent(explosion);
                    
                    doExplosion(entity);
                }
            }
        }
    }
    
    //--------------------private methods-------------------------
    
    //creates a Trigger and collider for exlosion entity.
    private void doExplosion(EntityRef entity){
        ExplosionComponent explosion = entity.getComponent(ExplosionComponent.class);
        LocationComponent location = entity.getComponent(LocationComponent.class);
        if(explosion == null || location == null){
            return;
        }
        
        if(entity.hasComponent(HealthComponent.class)){
            entity.removeComponent(HealthComponent.class);
        }
        
        List<EntityRef> broadPhaseEntities = physics.scanArea(AABB.createCenterExtent(location.getWorldPosition(), 
                new Vector3f(explosion.radius, explosion.radius, explosion.radius)), explosion.collidesWith);
        
        for(EntityRef otherEntity : broadPhaseEntities){
            if(otherEntity.getId() == entity.getId()){
                continue;
            }
            LocationComponent otherEntityLocation = otherEntity.getComponent(LocationComponent.class);
            if(otherEntityLocation == null){
                continue;
            }
            
            Vector3f entityPos = location.getWorldPosition();
            Vector3f otherEntityPos = otherEntityLocation.getWorldPosition();
            
            Vector3f direction = new Vector3f(otherEntityPos);
            direction.sub(entityPos);
            direction.normalize();
            
            float distanceSq = entityPos.distanceSquared(otherEntityPos);
            float radiusSquared = explosion.radius*explosion.radius;
            float explosionFactor = 1 - (distanceSq/radiusSquared);
            if(explosionFactor < 0){
                explosionFactor = 0;
            }
            
            if(distanceSq <= radiusSquared){
                HurtingComponent hurting = entity.getComponent(HurtingComponent.class);
                if(hurting == null){
                    return;
                }
                hurting.amount = (int) (hurting.amount*explosionFactor);
                hurting.damageType = EngineDamageTypes.EXPLOSIVE.get();
                entity.saveComponent(hurting);
                entity.send(new HurtEvent(otherEntity));
                
                Vector3f impulse = direction.scale((explosion.impulse*explosionFactor));
                otherEntity.send(new CombatImpulseEvent(impulse));
                otherEntity.send(new ImpulseEvent(impulse));
            }
        }
    }

}
