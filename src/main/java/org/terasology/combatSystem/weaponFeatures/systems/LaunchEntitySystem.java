package org.terasology.combatSystem.weaponFeatures.systems;

import com.google.common.collect.Lists;
import org.terasology.combatSystem.physics.components.MassComponent;
import org.terasology.combatSystem.physics.events.CombatImpulseEvent;
import org.terasology.combatSystem.weaponFeatures.OwnerSpecific;
import org.terasology.combatSystem.weaponFeatures.components.ArrowComponent;
import org.terasology.combatSystem.weaponFeatures.components.AttackerComponent;
import org.terasology.combatSystem.weaponFeatures.components.LaunchEntityComponent;
import org.terasology.combatSystem.weaponFeatures.events.LaunchEntityEvent;
import org.terasology.combatSystem.weaponFeatures.events.PrimaryAttackEvent;
import org.terasology.combatSystem.weaponFeatures.events.ReduceAmmoEvent;
import org.terasology.engine.Time;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.logic.characters.GazeMountPointComponent;
import org.terasology.logic.inventory.ItemComponent;
import org.terasology.logic.location.LocationComponent;
import org.terasology.math.Direction;
import org.terasology.math.geom.Quat4f;
import org.terasology.math.geom.Vector3f;
import org.terasology.physics.CollisionGroup;
import org.terasology.physics.StandardCollisionGroup;
import org.terasology.physics.components.TriggerComponent;
import org.terasology.physics.components.shapes.BoxShapeComponent;
import org.terasology.registry.In;
import org.terasology.rendering.logic.MeshComponent;

/**
 * Launch an entity at a given impulse from the intended entity
 */
@RegisterSystem
public class LaunchEntitySystem extends BaseComponentSystem implements UpdateSubscriberSystem{
    @In
    private EntityManager entityManager;
    
    @In
    private Time time;
    
    @ReceiveEvent(components = {LaunchEntityComponent.class})
    public void onFire(PrimaryAttackEvent event, EntityRef entity){
        entity.send(new LaunchEntityEvent(event.getDirection()));
    }
    
    @ReceiveEvent(components = {LaunchEntityComponent.class})
    public void launchingEntity(LaunchEntityEvent event, EntityRef entity){
        launchEntity(event.getDirection(), entity);
    }

    @Override
    public void update(float delta) {
        // TODO Auto-generated method stub
        Iterable<EntityRef> entitiesWith = entityManager.getEntitiesWith(ArrowComponent.class);
        for(EntityRef arrow : entitiesWith){
            MassComponent body = arrow.getComponent(MassComponent.class);
            LocationComponent location = arrow.getComponent(LocationComponent.class);
            
            // change rotation of arrow to always be tangent to path of trajectory
            if(body != null && location != null){
                Vector3f finalDir = new Vector3f(body.velocity);
                if(finalDir.length() != 0.0f){
                    Vector3f initialDir = Direction.FORWARD.getVector3f();
                    finalDir.normalize();
                    location.setWorldRotation(Quat4f.shortestArcQuat(initialDir, finalDir));
                }
            }
            
            arrow.saveComponent(location);
        }
    }
    
    //---------------------------------private methods--------------------------
    
    private void launchEntity(Vector3f direction, EntityRef entity){
        
        LaunchEntityComponent launchEntity = entity.getComponent(LaunchEntityComponent.class);
        
        // checking if its alright to fire.
        if(launchEntity.launchTime >= 0.0f){
            float currentTime = time.getGameTimeInMs();
            if(currentTime - launchEntity.launchTime < launchEntity.cooldownTime){
                return;
            }
        }
        
        launchEntity.launchTime = time.getGameTimeInMs();
        entity.saveComponent(launchEntity);
        
        EntityRef player = EntityRef.NULL;
        
        if(entity.hasComponent(ItemComponent.class)){
            player = OwnerSpecific.getUltimateOwner(entity);
        }
        
        // if no owner of "entity" is present than "entity" becomes "player". e.g. world generated 
        // launchers or player implemented traps that don't have ItemComponent that shot the 
        // projectile.
        
        if(player == EntityRef.NULL || player == null){
            player = entity;
        }
        
        if(launchEntity.primaryAttack){
            EntityRef entityToLaunch = EntityRef.NULL;
            // creates an entity with specified prefab for eg. an arrow prefab
            if(launchEntity.launchEntityPrefab != null){
                entityToLaunch = entityManager.create(launchEntity.launchEntityPrefab);
            }
            
            if(entityToLaunch != EntityRef.NULL){
                LocationComponent location = entityToLaunch.getComponent(LocationComponent.class);
                
                // adds the entity as the shooter for the arrow. It will be the launcher itself.
                entityToLaunch.addOrSaveComponent(new AttackerComponent(entity));
                
                LocationComponent shooterLoc = player.getComponent(LocationComponent.class);
                
                if(shooterLoc == null){
                    return;
                }
                
                if(entityToLaunch.hasComponent(MeshComponent.class)){
                    MeshComponent mesh = entityToLaunch.getComponent(MeshComponent.class);
                    BoxShapeComponent box = new BoxShapeComponent();
                    box.extents = mesh.mesh.getAABB().getExtents().scale(2.0f);
                    entityToLaunch.addOrSaveComponent(box);
                }
                
                // rotates the entity to face in the direction of pointer
                Vector3f initialDir = location.getWorldDirection();
                Vector3f finalDir = new Vector3f(direction);
                finalDir.normalize();
                location.setWorldRotation(Quat4f.shortestArcQuat(initialDir, finalDir));
                
                // sets the scale of the entity
                location.setWorldScale(0.5f);
                
                // sets the location of entity to current player's location with an offset
                GazeMountPointComponent gaze = player.getComponent(GazeMountPointComponent.class);
                if(gaze != null){
                    location.setWorldPosition(shooterLoc.getWorldPosition().add(gaze.translate).add(finalDir.scale(0.3f)));
                }
                else{
                    location.setWorldPosition(shooterLoc.getWorldPosition());
                }
                
                entityToLaunch.saveComponent(location);
                
                if(!entityToLaunch.hasComponent(TriggerComponent.class)){
                    TriggerComponent trigger = new TriggerComponent();
                    trigger.collisionGroup = StandardCollisionGroup.ALL;
                    trigger.detectGroups = Lists.<CollisionGroup>newArrayList(StandardCollisionGroup.DEFAULT, StandardCollisionGroup.WORLD, StandardCollisionGroup.CHARACTER, StandardCollisionGroup.SENSOR);
                    entityToLaunch.addOrSaveComponent(trigger);
                }
                
                // applies impulse to the entity
                Vector3f impulse = finalDir;
                impulse.normalize();
                impulse.mul(launchEntity.impulse);
                
                entityToLaunch.send(new CombatImpulseEvent(impulse));
                entity.send(new ReduceAmmoEvent());
            }
            else{
                // dispatch no ammo event
            }
        }
    }
}
