package org.terasology.combatSystem.weaponFeatures.systems;

import java.util.Iterator;

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
import org.terasology.math.geom.Quat4f;
import org.terasology.math.geom.Vector3f;
import org.terasology.physics.events.ChangeVelocityEvent;
import org.terasology.physics.events.ImpulseEvent;
import org.terasology.registry.In;

@RegisterSystem(RegisterMode.AUTHORITY)
public class LaunchEntitySystem extends BaseComponentSystem implements UpdateSubscriberSystem{
    @In
    private EntityManager entityManager;
    
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
                location.setWorldPosition(entity.getComponent(LocationComponent.class).getWorldPosition());
                
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
//        Iterable<EntityRef> entitiesWith = entityManager.getEntitiesWith(ArrowComponent.class);
//        Iterator<EntityRef> entities = entitiesWith.iterator();
//        while(entities.hasNext()){
//            EntityRef arrow = entities.next();
//            RigidBodyComponent rigidBody = arrow.getComponent(RigidBodyComponent.class);
//            LocationComponent location = arrow.getComponent(LocationComponent.class);
//            
//            Vector3f finalDir = new Vector3f(rigidBody.velocity);
//            if(finalDir.length() != 0.0f){
//                Vector3f initialDir = location.getWorldDirection().invert();
//                finalDir.normalize();
//                location.setWorldRotation(Quat4f.shortestArcQuat(initialDir, finalDir));
//            }
//            
//            arrow.saveComponent(location);
//        }
    }
}
