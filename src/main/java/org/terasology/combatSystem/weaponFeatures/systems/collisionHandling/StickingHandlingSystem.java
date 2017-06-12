package org.terasology.combatSystem.weaponFeatures.systems.collisionHandling;

import java.util.Iterator;

import org.terasology.combatSystem.physics.components.GravityComponent;
import org.terasology.combatSystem.physics.components.MassComponent;
import org.terasology.combatSystem.weaponFeatures.components.ParentComponent;
import org.terasology.combatSystem.weaponFeatures.components.features.FeatureComponent;
import org.terasology.combatSystem.weaponFeatures.components.hurting.StickComponent;
import org.terasology.combatSystem.weaponFeatures.events.AddFeaturesEvent;
import org.terasology.combatSystem.weaponFeatures.events.StickEvent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.health.BeforeDestroyEvent;
import org.terasology.logic.location.Location;
import org.terasology.logic.location.LocationComponent;
import org.terasology.math.geom.Quat4f;
import org.terasology.math.geom.Vector3f;
import org.terasology.physics.components.TriggerComponent;
import org.terasology.physics.events.CollideEvent;

@RegisterSystem(RegisterMode.AUTHORITY)
public class StickingHandlingSystem extends CollisionHandlingSystem{
    
    @ReceiveEvent(components = {StickComponent.class})
    public void sticking(CollideEvent event, EntityRef entity){
        EntityRef otherEntity = event.getOtherEntity();
        LocationComponent location = entity.getComponent(LocationComponent.class);
        LocationComponent otherEntityLocation = otherEntity.getComponent(LocationComponent.class);
        if(location == null || otherEntityLocation == null){
            return;
        }
        
        // offset of the point of collision on the colliding entity to the
        // location of the colliding entity
        Vector3f offset = new Vector3f(0.0f, 0.0f, 0.0f);
        
        // to find quaternion rotation that if applied to quaternion 1 (the one which is
        // inversed) would result in quaternion 2 (the one which is multiplied)
        Quat4f relativeRot = new Quat4f(otherEntityLocation.getWorldRotation());
        relativeRot.inverse();
        relativeRot.mul(location.getWorldRotation());
        
        // world scale of the entity.
        float scale = location.getWorldScale();
        
        entity.send(new StickEvent(otherEntity, offset, relativeRot, scale));
        
        event.consume();
    }
    
    @ReceiveEvent(components = StickComponent.class)
    public void stick(StickEvent event, EntityRef entity){
        EntityRef target = event.getTarget();
        
        // resting all the movements of the entity
        MassComponent body = entity.getComponent(MassComponent.class);
        if(body != null){
            body.acceleration.set(0, 0, 0);
            body.velocity.set(0, 0, 0);
            body.force.set(0, 0, 0);
            entity.saveComponent(body);
        }
        
        if(entity.hasComponent(TriggerComponent.class)){
            entity.removeComponent(TriggerComponent.class);
        }
        
        // attaching entity to otherEntity so that it always follows otherEntity
        Location.attachChild(target, entity, event.getOffset(), event.getRelativeRotation(), event.getRelativeScale());
        
        StickComponent stick = entity.getComponent(StickComponent.class);
        stick.setTarget(target);
        entity.saveComponent(stick);
        
        ParentComponent parent = target.getComponent(ParentComponent.class);
        if(parent == null){
            parent = new ParentComponent();
        }
        
        parent.children.add(entity);
        
        target.addOrSaveComponent(parent);
        
        if(entity.hasComponent(GravityComponent.class)){
            entity.removeComponent(GravityComponent.class);
        }
        
        //-------------------------repetitive code for every HurtingComponent-----------
        
        // damage the other entity
        damageOtherEntity(target, entity, stick);
        
        //----------------repetitive code for every component that triggers action-------
        
        //send a new AddFeaturesEvent with the collide event info as parameters
        if(entity.hasComponent(FeatureComponent.class)){
            entity.send(new AddFeaturesEvent());
        }
    }
    
    @ReceiveEvent(components = ParentComponent.class)
    public void removeChildEntities(BeforeDestroyEvent event, EntityRef entity){
        ParentComponent parent = entity.getComponent(ParentComponent.class);
        if(parent == null){
            return;
        }
        if(parent.children == null){
            return;
        }
        
        Iterator<EntityRef> childrenList = parent.children.iterator();
        while(childrenList.hasNext()){
            EntityRef children = childrenList.next();
            children.destroy();
        }
    }

}
