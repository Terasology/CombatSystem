package org.terasology.combatSystem.weaponFeatures.systems.collisionHandling;

import org.terasology.combatSystem.hurting.HurtEvent;
import org.terasology.combatSystem.weaponFeatures.components.ExplodeComponent;
import org.terasology.combatSystem.weaponFeatures.components.ExplosionComponent;
import org.terasology.combatSystem.weaponFeatures.events.ExplodeEvent;
import org.terasology.combatSystem.weaponFeatures.events.ExplosionEvent;
import org.terasology.entitySystem.entity.EntityBuilder;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.health.DestroyEvent;
import org.terasology.logic.health.EngineDamageTypes;
import org.terasology.logic.location.LocationComponent;
import org.terasology.physics.events.CollideEvent;
import org.terasology.registry.In;

@RegisterSystem
public class ExplodeHandlingSystem extends BaseComponentSystem{
    @In
    EntityManager entityManager;
    
    @ReceiveEvent(components = ExplodeComponent.class)
    public void explodeOnContact(CollideEvent event, EntityRef entity){
        ExplodeComponent destroy = entity.getComponent(ExplodeComponent.class);
        
        // damage the other entity
        entity.send(new HurtEvent(event.getOtherEntity()));
        
        explode(entity);
        
        event.consume();
    }
    
    @ReceiveEvent(components = ExplodeComponent.class)
    public void exploding(ExplodeEvent event, EntityRef entity){
        explode(entity);
    }
    
    @ReceiveEvent(components = ExplodeComponent.class)
    public void explodeOnDestroy(DestroyEvent event, EntityRef entity){
        explode(entity);
    }
    
    //--------------------------------private methods--------------------------------
    
    private void explode(EntityRef entity){
        LocationComponent location = entity.getComponent(LocationComponent.class);
        if(location == null){
            return;
        }
        ExplodeComponent explode = entity.getComponent(ExplodeComponent.class);
        EntityBuilder explosion = entityManager.newBuilder(explode.explosionPrefab);
        
        if(explosion != null){
            LocationComponent explosionLoc = explosion.getComponent(LocationComponent.class);
            if(explosionLoc != null){
                explosionLoc.setWorldPosition(location.getWorldPosition());
                explosionLoc.setWorldRotation(location.getWorldRotation());
                explosionLoc.setWorldScale(location.getWorldScale());
                
                explosion.addOrSaveComponent(explosionLoc);
            }
        }
        entity.removeComponent(ExplodeComponent.class);
        
        entity.destroy();
        
        if(explosion != null){
            EntityRef explosionEntity = explosion.build();
            
            if(explosionEntity.hasComponent(ExplosionComponent.class)){
                explosionEntity.send(new ExplosionEvent());
            }
        }
    }

}
