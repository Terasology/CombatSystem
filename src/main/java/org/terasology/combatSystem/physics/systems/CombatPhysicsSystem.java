package org.terasology.combatSystem.physics.systems;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.terasology.combatSystem.physics.components.MassComponent;
import org.terasology.combatSystem.physics.events.CombatForceEvent;
import org.terasology.combatSystem.physics.events.CombatImpulseEvent;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.entity.lifecycleEvents.BeforeDeactivateComponent;
import org.terasology.entitySystem.entity.lifecycleEvents.OnActivatedComponent;
import org.terasology.entitySystem.entity.lifecycleEvents.OnChangedComponent;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.logic.location.LocationComponent;
import org.terasology.math.geom.Vector3f;
import org.terasology.physics.CollisionGroup;
import org.terasology.physics.HitResult;
import org.terasology.physics.Physics;
import org.terasology.physics.StandardCollisionGroup;
import org.terasology.physics.components.TriggerComponent;
import org.terasology.physics.events.CollideEvent;
import org.terasology.physics.shapes.BoxShapeComponent;
import org.terasology.physics.shapes.CapsuleShapeComponent;
import org.terasology.physics.shapes.CylinderShapeComponent;
import org.terasology.physics.shapes.SphereShapeComponent;
import org.terasology.registry.In;
import com.google.common.collect.Maps;

/**
 * handles various physical operations applied to an entity with {@code MassComponent}
 */
@RegisterSystem
public class CombatPhysicsSystem extends BaseComponentSystem implements UpdateSubscriberSystem{
    private static final float TUNNELING_MIN_VELOCITY_SQ = 400.0f;
    
    private Map<EntityRef, CollisionGroup[]> entityCollidesWithGroup = Maps.newHashMap();
    
    @In
    private EntityManager entityManager;
    @In
    private Physics physics;
//    @In
//    private NetworkSystem network;
    
    @ReceiveEvent(components = {TriggerComponent.class})
    public void addCollisionGroupOnMap(OnActivatedComponent event, EntityRef entity) {
        entityCollidesWithGroup.put(entity, collisionGroupListToArray(entity));
    }
    
    @ReceiveEvent(components = {TriggerComponent.class})
    public void updateCollisionGroupOnMap(OnChangedComponent event, EntityRef entity) {
        entityCollidesWithGroup.put(entity, collisionGroupListToArray(entity));
    }
    
    @ReceiveEvent(components = {TriggerComponent.class})
    public void removeCollisionGroupOnMap(BeforeDeactivateComponent event, EntityRef entity) {
        entityCollidesWithGroup.remove(entity);
    }
    
    /**
     * applies impulse to specified entity.
     * 
     * @param event  stores the value of impulse to be applied.
     * @param entity  the entity on which the impulse is applied.
     */
    @ReceiveEvent(components = {MassComponent.class})
    public void onImpulse(CombatImpulseEvent event, EntityRef entity) {
        MassComponent body = entity.getComponent(MassComponent.class);
        
        if(body == null){
            return;
        }
        Vector3f impulse = event.getImpulse();
        impulse.div(body.mass);
        if(impulse == null || impulse.length() == 0){
            return;
        }
        body.velocity.add(impulse);
        
        entity.saveComponent(body);
    }
    
    /**
     * applies force to specified entity.
     * <p>
     * the force is added to total force on entity. 
     * Therefore, force is resolved in this system.
     * 
     * @param event  stores the value of force to be applied.
     * @param entity  the entity on which the impulse is applied.
     */
    @ReceiveEvent(components = {MassComponent.class})
    public void onApplyForce(CombatForceEvent event, EntityRef entity) {
        MassComponent body = entity.getComponent(MassComponent.class);
        
        body.force.add(event.getForce());
        
        entity.saveComponent(body);
    }
    
    /**
     * changes velocity and location of an entity based on delta as well as checks for world 
     * collisions. This method is also responsible for raycasting to prevent tunneling for 
     * small fast objects.
     */
    @Override
    public void update(float delta){
        
        Iterable<EntityRef> entitiesWith = entityManager.getEntitiesWith(MassComponent.class);
        Iterator<EntityRef> entities = entitiesWith.iterator();
        while(entities.hasNext()){
            EntityRef entity = entities.next();
            MassComponent body = entity.getComponent(MassComponent.class);
            LocationComponent location = entity.getComponent(LocationComponent.class);
            
            if(location != null){
                // change velocity based on acceleration
                body.acceleration.mul(delta);
                body.velocity.add(body.acceleration);
                body.acceleration.set(0, 0, 0);
                
                // raycasting for world collisions
                float velocityMagSq = body.velocity.lengthSquared();
                
                CollisionGroup[] group = entityCollidesWithGroup.get(entity);
                short combinedGroup = combineGroups(group);
                if((combinedGroup & StandardCollisionGroup.WORLD.getFlag()) != 0){
                    Vector3f direction = new Vector3f(body.velocity);
                    float distance = ((float)Math.sqrt(velocityMagSq)) * delta;
                    Vector3f from = calculateStartingPoint(location.getWorldPosition(), direction, entity);
                    direction.normalize();
                    
                    HitResult result = physics.rayTrace(from, direction, distance, group);
                    
                    if(result.isHit() && result.isWorldHit()){
                        EntityRef otherEntity = result.getEntity();
                        Vector3f hitPoint = result.getHitPoint();
                        Vector3f normal = result.getHitNormal();
                        
                        // updating position so that tip of entity is at point of contact
                        Vector3f newPos = new Vector3f(hitPoint);
                        from.sub(location.getWorldPosition());
                        newPos.sub(from);
                        location.setWorldPosition(newPos);
                        
                        entity.saveComponent(location);
                        
                        entity.send(new CollideEvent(otherEntity, hitPoint,
                                hitPoint, 0.0f, normal));
                        
                        continue;
                    }
                }
                
                // raycasting to resolve tunneling in fast moving small objects
                if(velocityMagSq >= TUNNELING_MIN_VELOCITY_SQ 
                        && group != null){
                    Vector3f direction = new Vector3f(body.velocity);
                    float distance = ((float)Math.sqrt(velocityMagSq)) * delta;
                    Vector3f from = calculateStartingPoint(location.getWorldPosition(), direction, entity);
                    direction.normalize();
                    
                    HitResult result = physics.rayTrace(from, direction, distance, group);
                    
                    if(result.isHit()){
                        EntityRef otherEntity = result.getEntity();
                        Vector3f hitPoint = result.getHitPoint();
                        Vector3f normal = result.getHitNormal();
                        
                        // updating position so that tip of entity is at point of contact
                        Vector3f newPos = new Vector3f(hitPoint);
                        from.sub(location.getWorldPosition());
                        newPos.sub(from);
                        location.setWorldPosition(newPos);
                        
                        entity.saveComponent(location);
                        
                        entity.send(new CollideEvent(otherEntity, hitPoint,
                                hitPoint, 0.0f, result.getHitNormal()));
                        
                        if(otherEntity.hasComponent(TriggerComponent.class)){
                            otherEntity.send(new CollideEvent(entity, hitPoint, hitPoint, 0.0f, normal));
                        }
                        
                        continue;
                    }
                }
                
                // change location based on velocity
                Vector3f velocity = new Vector3f(body.velocity);
                velocity.mul(delta);
                Vector3f initialLoc = location.getWorldPosition();
                initialLoc.add(velocity);
                location.setWorldPosition(initialLoc);
                
                entity.saveComponent(body);
                entity.saveComponent(location);
                
                
            }
          
        }
    }
    
    //-----------------------------private methods----------------------
    
    /**
     * converts {@link List} data structure of type {@link CollisionGroup} into array
     * @param entity
     * @return group Array with all elements of CollisionGroup List.
     */
    private CollisionGroup[] collisionGroupListToArray(EntityRef entity){
        TriggerComponent trigger = entity.getComponent(TriggerComponent.class);
        if(trigger == null){
            return null;
        }
        List<CollisionGroup> collidesWith = trigger.detectGroups;
        
        CollisionGroup[] group = collidesWith.toArray(new CollisionGroup[collidesWith.size()]);
        
        return group;
    }
    
    private short combineGroups(CollisionGroup... collisionGroups){
        if(collisionGroups == null){
            return 0;
        }
        return combineGroups(Arrays.asList(collisionGroups));
    }
    
    /**
     * combines the {@code CollisionGroup} in filter mask through & bit operation and returns 
     * the value
     * @param collisionGroup
     * @return flag
     */
    private short combineGroups(List<CollisionGroup> collisionGroup){
        short flag = 0;
        if(collisionGroup == null){
            return flag;
        }
        for(CollisionGroup group : collisionGroup){
            flag |= group.getFlag();
        }
        return flag;
    }
    
    /**
     * calculates the tip of collision shape in its direction of motion.
     * Right now it only works for entities whose +ve z-axis always faces the direction of 
     * movement.
     * @param origin the location of entity
     * @param direction the direction of motion of entity
     * @param entity the entity whose tip point is to be calculated
     * @return
     */
    private Vector3f calculateStartingPoint(Vector3f origin, Vector3f direction, EntityRef entity){
        direction.normalize();
        BoxShapeComponent box = entity.getComponent(BoxShapeComponent.class);
        if (box != null) {
            direction.scale(box.extents.z/2.0f);
            origin.add(direction);
        }
        SphereShapeComponent sphere = entity.getComponent(SphereShapeComponent.class);
        if (sphere != null) {
            direction.scale(sphere.radius);
            origin.add(direction);
        }
        CapsuleShapeComponent capsule = entity.getComponent(CapsuleShapeComponent.class);
        if (capsule != null) {
            direction.scale((capsule.radius + (capsule.height/2.0f)));
            origin.add(direction);
        }
        CylinderShapeComponent cylinder = entity.getComponent(CylinderShapeComponent.class);
        if (cylinder != null) {
            direction.scale(cylinder.height/2.0f);
            origin.add(direction);
        }
        
        return origin;
    }

}
