package org.terasology.combatSystem.weaponFeatures.systems.collisionHandling;

import java.util.Iterator;

import org.terasology.combatSystem.physics.components.GravityComponent;
import org.terasology.combatSystem.physics.components.MassComponent;
import org.terasology.combatSystem.weaponFeatures.components.HurtingComponent;
import org.terasology.combatSystem.weaponFeatures.components.ParentComponent;
import org.terasology.combatSystem.weaponFeatures.components.StickComponent;
import org.terasology.combatSystem.weaponFeatures.events.HurtEvent;
import org.terasology.combatSystem.weaponFeatures.events.StickEvent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.health.BeforeDestroyEvent;
import org.terasology.logic.health.DestroyEvent;
import org.terasology.logic.location.Location;
import org.terasology.logic.location.LocationComponent;
import org.terasology.math.geom.Quat4f;
import org.terasology.math.geom.Vector3f;
import org.terasology.physics.components.TriggerComponent;
import org.terasology.physics.events.CollideEvent;

@RegisterSystem(RegisterMode.AUTHORITY)
public class StickingHandlingSystem extends BaseComponentSystem{
    
    @ReceiveEvent(components = {StickComponent.class})
    public void stickingCollision(CollideEvent event, EntityRef entity){
        sticking(entity, event.getOtherEntity());
        
        event.consume();
    }
    
    @ReceiveEvent( components = StickComponent.class)
    public void stick(StickEvent event, EntityRef entity){
        sticking(entity, event.getTarget());
    }
    
    @ReceiveEvent(components = ParentComponent.class)
    public void removeChildEntities(DestroyEvent event, EntityRef entity){
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
    
    //------------------------------private methods-----------------------
    
    private void sticking(EntityRef entity, EntityRef target){
        if(target == EntityRef.NULL || target == null){
            return;
        }
        LocationComponent location = entity.getComponent(LocationComponent.class);
        LocationComponent otherEntityLocation = target.getComponent(LocationComponent.class);
        if(location == null || otherEntityLocation == null){
            return;
        }
        
        // offset of the point of collision on the colliding entity to the
        // location of the colliding entity
        Vector3f offset = new Vector3f(location.getWorldPosition());
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
        Location.attachChild(target, entity, offset, relativeRot, scale);
        
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
        if(entity.hasComponent(HurtingComponent.class)){
            entity.send(new HurtEvent(target));
        }
    }

}
