package org.terasology.combatSystem.weaponFeatures.systems.collisionHandling;

import java.util.Iterator;

import org.terasology.combatSystem.physics.components.GravityComponent;
import org.terasology.combatSystem.physics.components.MassComponent;
import org.terasology.combatSystem.weaponFeatures.components.ParentComponent;
import org.terasology.combatSystem.weaponFeatures.components.StickComponent;
import org.terasology.combatSystem.weaponFeatures.events.HurtEvent;
import org.terasology.combatSystem.weaponFeatures.events.StickEvent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.health.DestroyEvent;
import org.terasology.logic.health.EngineDamageTypes;
import org.terasology.logic.location.Location;
import org.terasology.logic.location.LocationComponent;
import org.terasology.math.geom.Quat4f;
import org.terasology.math.geom.Vector3f;
import org.terasology.physics.components.TriggerComponent;
import org.terasology.physics.events.CollideEvent;
import org.terasology.world.block.BlockComponent;

@RegisterSystem
public class StickingHandlingSystem extends BaseComponentSystem{
    
    @ReceiveEvent(components = {StickComponent.class})
    public void stickingCollision(CollideEvent event, EntityRef entity){
        EntityRef target = event.getOtherEntity();
        if(target.hasComponent(BlockComponent.class)){
            blockSticking(entity, target);
        }
        else{
            sticking(entity, target);
        }
        
        event.consume();
    }
    
    @ReceiveEvent( components = StickComponent.class)
    public void stick(StickEvent event, EntityRef entity){
        EntityRef target = event.getTarget();
        if(target.hasComponent(BlockComponent.class)){
            blockSticking(entity, target);
        }
        else{
            sticking(entity, target);
        }
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
        entity.send(new HurtEvent(target, stick.amount, EngineDamageTypes.DIRECT.get()));
    }
    
    // if the entity wants to stick to a block
    private void blockSticking(EntityRef entity, EntityRef target){
        if(target == EntityRef.NULL || target == null){
            return;
        }
        
        BlockComponent block = target.getComponent(BlockComponent.class);
        if(block == null){
            return;
        }
        if(block.getBlock().isPenetrable()){
            return;
        }
        LocationComponent location = entity.getComponent(LocationComponent.class);
        if(location == null){
            return;
        }
        
        MassComponent body = entity.getComponent(MassComponent.class);
        if(body != null){
//            Vector3f pos = location.getWorldPosition();
//            Vector3f dir = new Vector3f(body.velocity);
//            dir.normalize();
//            //we assume the peircing to be 0.1 units deep
//            dir.scale(0.1f);
//            pos.add(dir);
//            
//            location.setWorldPosition(pos);
//            entity.saveComponent(location);
            
            // resting all the movements of the entity
            body.acceleration.set(0, 0, 0);
            body.velocity.set(0, 0, 0);
            body.force.set(0, 0, 0);
            entity.saveComponent(body);
        }
        
        if(entity.hasComponent(GravityComponent.class)){
            entity.removeComponent(GravityComponent.class);
        }
        
        if(entity.hasComponent(TriggerComponent.class)){
            entity.removeComponent(TriggerComponent.class);
        }
    }

}
