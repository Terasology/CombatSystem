package org.terasology.combatSystem.weaponFeatures.systems.collisionHandling;

import org.terasology.combatSystem.weaponFeatures.components.features.FeatureComponent;
import org.terasology.combatSystem.weaponFeatures.components.hurting.ExplosionComponent;
import org.terasology.combatSystem.weaponFeatures.events.AddExceptionEvent;
import org.terasology.combatSystem.weaponFeatures.events.AddFeaturesEvent;
import org.terasology.combatSystem.weaponFeatures.events.ExplosionEvent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.entity.lifecycleEvents.OnActivatedComponent;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.location.LocationComponent;
import org.terasology.physics.CollisionGroup;
import org.terasology.physics.StandardCollisionGroup;
import org.terasology.physics.components.TriggerComponent;
import org.terasology.physics.events.CollideEvent;
import org.terasology.physics.shapes.SphereShapeComponent;

import com.google.common.collect.Lists;

@RegisterSystem(RegisterMode.AUTHORITY)
public class ExplosionHandlingSystem extends CollisionHandlingSystem{
    
    @ReceiveEvent(components = ExplosionComponent.class)
    public void createExplosionShape(OnActivatedComponent event, EntityRef entity){
        ExplosionComponent explosion = entity.getComponent(ExplosionComponent.class);
        if(explosion == null){
            return;
        }
        
        SphereShapeComponent outerSphere = new SphereShapeComponent();
        outerSphere.radius = explosion.outerRadius;
        entity.addOrSaveComponent(outerSphere);
        
        LocationComponent location = entity.getComponent(LocationComponent.class);
        if(location == null){
            location = new LocationComponent();
        }
        entity.addOrSaveComponent(location);
        
        TriggerComponent trigger = entity.getComponent(TriggerComponent.class);
        if(trigger == null){
            trigger = new TriggerComponent();
            trigger.detectGroups = Lists.<CollisionGroup>newArrayList(StandardCollisionGroup.DEFAULT, StandardCollisionGroup.WORLD, StandardCollisionGroup.CHARACTER);
        }
        entity.addOrSaveComponent(trigger);
    }
    
    @ReceiveEvent(components = ExplosionComponent.class)
    public void explosionOnCollision(CollideEvent event, EntityRef entity){
        entity.send(new ExplosionEvent(event.getOtherEntity()));
        
        event.consume();
    }
    
    @ReceiveEvent(components = ExplosionComponent.class)
    public void explosion(ExplosionEvent event, EntityRef entity){
        EntityRef otherEntity = event.getOtherEntity();
        LocationComponent otherEntityLocation = otherEntity.getComponent(LocationComponent.class);
        LocationComponent location = entity.getComponent(LocationComponent.class);
        ExplosionComponent explosion = entity.getComponent(ExplosionComponent.class);
        
        if(otherEntityLocation == null || location == null){
            return;
        }
        
        float distanceSq = location.getWorldPosition().distanceSquared(otherEntityLocation.getWorldPosition());
        if(distanceSq <= (explosion.innerRadius*explosion.innerRadius)){
            damageOtherEntity(otherEntity, entity, explosion);
            entity.send(new AddExceptionEvent(otherEntity));
        }
        else{
            explosion.amount *= explosion.outerDamageFactor;
            damageOtherEntity(otherEntity, entity, explosion);
            entity.send(new AddExceptionEvent(otherEntity));
            explosion.amount /= explosion.outerDamageFactor;
        }
        
        //----------------repetitive code for every component that triggers action-------
        
        //send a new AddFeaturesEvent with the collide event info as parameters
        if(entity.hasComponent(FeatureComponent.class)){
            entity.send(new AddFeaturesEvent());
        }
    }

}
