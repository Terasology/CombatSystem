package org.terasology.combatSystem.weaponFeatures.systems;

import org.terasology.combatSystem.physics.components.MassComponent;
import org.terasology.combatSystem.physics.events.CombatImpulseEvent;
import org.terasology.combatSystem.weaponFeatures.OwnerSpecific;
import org.terasology.combatSystem.weaponFeatures.components.ArrowComponent;
import org.terasology.combatSystem.weaponFeatures.components.AttackerComponent;
import org.terasology.combatSystem.weaponFeatures.components.LaunchEntityComponent;
import org.terasology.combatSystem.weaponFeatures.events.PrimaryAttackEvent;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.logic.characters.GazeMountPointComponent;
import org.terasology.logic.location.LocationComponent;
import org.terasology.math.Direction;
import org.terasology.math.geom.Quat4f;
import org.terasology.math.geom.Vector3f;
import org.terasology.physics.CollisionGroup;
import org.terasology.physics.StandardCollisionGroup;
import org.terasology.physics.components.TriggerComponent;
import org.terasology.physics.shapes.BoxShapeComponent;
import org.terasology.registry.In;
import org.terasology.rendering.logic.MeshComponent;

import com.google.common.collect.Lists;

@RegisterSystem
public class LaunchEntitySystem extends BaseComponentSystem implements UpdateSubscriberSystem{
    @In
    private EntityManager entityManager;
    
    @ReceiveEvent(components = {LaunchEntityComponent.class})
    public void onFire(PrimaryAttackEvent event, EntityRef entity){
        LaunchEntityComponent launchEntity = entity.getComponent(LaunchEntityComponent.class);
        EntityRef player = OwnerSpecific.getUltimateOwner(entity);;
        
        // if no owner of "entity" is present than "entity" becomes "player". e.g. world generated 
        // launcher that shot the projectile.
        
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
                
                // adds the "player" as the shooter for the arrow. It may be the character or 
                // the launcher itself.
                entityToLaunch.addOrSaveComponent(new AttackerComponent(player));
                
                LocationComponent shooterLoc = player.getComponent(LocationComponent.class);
                
                if(shooterLoc == null){
                    return;
                }
                
                // rotates the entity to face in the direction of pointer
                Vector3f initialDir = location.getWorldDirection().invert();
                Vector3f finalDir = new Vector3f(event.getDirection());
                finalDir.normalize();
                location.setWorldRotation(Quat4f.shortestArcQuat(initialDir, finalDir));
                
                // sets the location of entity to current player's location with an offset
                location.setWorldScale(0.5f);
                
                
                location.setWorldPosition(shooterLoc.getWorldPosition().addY(0.5f).add(finalDir.scale(0.5f)));
                
                
                
                entityToLaunch.saveComponent(location);
                
                if(!entityToLaunch.hasComponent(TriggerComponent.class)){
                    TriggerComponent trigger = new TriggerComponent();
                    trigger.detectGroups = Lists.<CollisionGroup>newArrayList(StandardCollisionGroup.DEFAULT, StandardCollisionGroup.WORLD, StandardCollisionGroup.CHARACTER);
                    entityToLaunch.addOrSaveComponent(trigger);
                }
                
                if(entityToLaunch.hasComponent(MeshComponent.class)){
                    MeshComponent mesh = entityToLaunch.getComponent(MeshComponent.class);
                    BoxShapeComponent box = new BoxShapeComponent();
                    box.extents = mesh.mesh.getAABB().getExtents().scale(2.0f);
                    entityToLaunch.addOrSaveComponent(box);
                }
                
                // applies impulse to the entity
                Vector3f impulse = finalDir;
                impulse.normalize();
                impulse.mul(launchEntity.impulse);
                entityToLaunch.send(new CombatImpulseEvent(impulse));
            }
            else{
                // dispatch no ammo event
            }
        }
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
                    Vector3f initialDir = Direction.FORWARD.getVector3f().invert();
                    finalDir.normalize();
                    location.setWorldRotation(Quat4f.shortestArcQuat(initialDir, finalDir));
                }
            }
            
            arrow.saveComponent(location);
        }
    }
}
