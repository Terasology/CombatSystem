package org.terasology.combatSystem.weaponFeatures.systems.collisionHandling;

import org.terasology.combatSystem.weaponFeatures.components.ExplodeComponent;
import org.terasology.combatSystem.weaponFeatures.components.features.FeatureComponent;
import org.terasology.combatSystem.weaponFeatures.components.hurting.ExplosionComponent;
import org.terasology.combatSystem.weaponFeatures.events.AddFeaturesEvent;
import org.terasology.combatSystem.weaponFeatures.events.ExplodeEvent;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.location.LocationComponent;
import org.terasology.physics.events.CollideEvent;
import org.terasology.registry.In;

@RegisterSystem(RegisterMode.AUTHORITY)
public class ExplodeHandlingSystem extends CollisionHandlingSystem{
    @In
    EntityManager entityManager;
    
    @ReceiveEvent(components = ExplodeComponent.class)
    public void explosionOnContact(CollideEvent event, EntityRef entity){
        entity.send(new ExplodeEvent());
        
        event.consume();
    }
    
    @ReceiveEvent(components = ExplodeComponent.class)
    public void explode(ExplodeEvent event, EntityRef entity){
        LocationComponent location = entity.getComponent(LocationComponent.class);
        if(location == null){
            return;
        }
        ExplodeComponent explode = entity.getComponent(ExplodeComponent.class);
        EntityRef explosion = EntityRef.NULL;
        if(explode.explosionEntity != null){
            explosion = explode.explosionEntity.copy();
        }
        else if(explode.explosionPrefab != null){
            explosion = entityManager.create(explode.explosionPrefab);
        }
        
        if(explosion == null || explosion == EntityRef.NULL){
            explosion = entityManager.create();
        }
        
        explosion.addOrSaveComponent(new ExplosionComponent());
        
        LocationComponent explosionLoc = explosion.getComponent(LocationComponent.class);
        if(explosionLoc == null){
            explosionLoc = new LocationComponent();
        }
        explosionLoc.setWorldPosition(location.getWorldPosition());
        explosionLoc.setWorldRotation(location.getWorldRotation());
        explosionLoc.setWorldScale(location.getWorldScale());
        
        explosion.addOrSaveComponent(explosionLoc);
        
        //----------------repetitive code for every component that triggers action-------
        
        //send a new AddFeaturesEvent with the collide event info as parameters
        if(entity.hasComponent(FeatureComponent.class)){
            entity.send(new AddFeaturesEvent());
        }
    }

}
