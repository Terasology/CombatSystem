package org.terasology.combatSystem.weaponFeatures.systems;

import org.terasology.combatSystem.weaponFeatures.components.LaunchEntityComponent;
import org.terasology.combatSystem.weaponFeatures.events.PrimaryAttackEvent;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.location.LocationComponent;
import org.terasology.math.geom.Quat4f;
import org.terasology.math.geom.Vector3f;
import org.terasology.physics.events.ImpulseEvent;
import org.terasology.registry.In;

@RegisterSystem(RegisterMode.AUTHORITY)
public class LaunchEntitySystem extends BaseComponentSystem{
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
                Quat4f rotation = new Quat4f(0, 0, 0, 1);
                Vector3f initialDir = new Vector3f(0, 0, -1);
                Vector3f finalDir = new Vector3f(event.info.getDirection());
                Vector3f crossProduct = new Vector3f();
                crossProduct.cross(initialDir, finalDir);
                rotation.x = crossProduct.x;
                rotation.y = crossProduct.y;
                rotation.z = crossProduct.z;
                rotation.w = (float) (Math.sqrt((initialDir.lengthSquared())*(finalDir.lengthSquared())) +
                        initialDir.dot(finalDir)); 
                rotation.normalize();
                location.setWorldRotation(rotation);
                
                entityToLaunch.addOrSaveComponent(location);
                
                // applies impulse to the entity
                Vector3f impulse = finalDir;
                impulse.normalize();
                impulse.mul(launchEntity.impulse);
                entityToLaunch.send(new ImpulseEvent(impulse));
            }
            else{
                // dispatch no ammo event
            }
        }
    }
}
