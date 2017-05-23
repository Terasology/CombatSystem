package org.terasology.combatSystem.weaponFeatures.systems;

import java.util.Iterator;
import java.util.List;

import org.terasology.combatSystem.physics.components.MassComponent;
import org.terasology.combatSystem.physics.events.CombatImpulseEvent;
import org.terasology.combatSystem.weaponFeatures.components.ArrowComponent;
import org.terasology.combatSystem.weaponFeatures.components.LaunchEntityComponent;
import org.terasology.combatSystem.weaponFeatures.events.PrimaryAttackEvent;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.logic.location.LocationComponent;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.math.Direction;
import org.terasology.math.geom.Quat4f;
import org.terasology.math.geom.Vector3f;
import org.terasology.physics.CollisionGroup;
import org.terasology.physics.HitResult;
import org.terasology.physics.StandardCollisionGroup;
import org.terasology.physics.engine.PhysicsEngine;
import org.terasology.physics.events.CollideEvent;
import org.terasology.registry.In;

import com.google.common.collect.Lists;

@RegisterSystem(RegisterMode.AUTHORITY)
public class LaunchEntitySystem extends BaseComponentSystem implements UpdateSubscriberSystem{
    @In
    private EntityManager entityManager;
    @In
    private LocalPlayer localPlayer;
//    @In
//    private PhysicsEngine physics;
    
    @ReceiveEvent(components = {LaunchEntityComponent.class})
    public void onFire(PrimaryAttackEvent event, EntityRef entity){
        LaunchEntityComponent launchEntity = entity.getComponent(LaunchEntityComponent.class);
        if(launchEntity.primaryAttack){
            // creates an entity with specified prefab for eg. an arrow prefab
            EntityRef entityToLaunch = entityManager.create(launchEntity.launchEntityPrefab);
            
            if(entityToLaunch != EntityRef.NULL){
                // sets the location of entity to current player's bow location where it is spawned
                LocationComponent location = entityToLaunch.getComponent(LocationComponent.class);
                location.setWorldScale(0.5f);
                location.setWorldPosition(localPlayer.getPosition().addY(0.5f));
                
                // rotates the entity to face in the direction of pointer
                Vector3f initialDir = location.getWorldDirection().invert();
                Vector3f finalDir = new Vector3f(event.info.getDirection());
                finalDir.normalize();
                location.setWorldRotation(Quat4f.shortestArcQuat(initialDir, finalDir));
                
                entityToLaunch.saveComponent(location);
                
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
        Iterator<EntityRef> entities = entitiesWith.iterator();
        while(entities.hasNext()){
            EntityRef arrow = entities.next();
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
                
                // raytrace the next step for collisions
//                HitResult rayTraceResult;
//                float displacement = body.velocity.length()*(1.0f/60.0f);
//                CollisionGroup filter = StandardCollisionGroup.DEFAULT;
//                rayTraceResult = physics.rayTrace(location.getWorldPosition(), finalDir, displacement, filter);
//                if(rayTraceResult.isHit()){
//                    arrow.send(new CollideEvent(rayTraceResult.getEntity(), Vector3f.north(), rayTraceResult.getHitPoint(), 0.0f, rayTraceResult.getHitNormal()));
//                }
            }
            
            arrow.saveComponent(location);
        }
    }
}
