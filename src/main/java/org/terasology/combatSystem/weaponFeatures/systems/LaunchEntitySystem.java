package org.terasology.combatSystem.weaponFeatures.systems;

import org.terasology.combatSystem.weaponFeatures.components.LaunchEntityComponent;
import org.terasology.combatSystem.weaponFeatures.events.LaunchEntityEvent;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.location.LocationComponent;
import org.terasology.math.geom.Vector3f;
import org.terasology.physics.engine.PhysicsEngine;
import org.terasology.registry.In;

@RegisterSystem(RegisterMode.AUTHORITY)
public class LaunchEntitySystem extends BaseComponentSystem{
    @In
    EntityManager entityManager;
    @In
    private PhysicsEngine physics;
    
    @ReceiveEvent(components = {LaunchEntityComponent.class})
    public void onFire(LaunchEntityEvent event, EntityRef entity){
        LaunchEntityComponent launchEntity = entity.getComponent(LaunchEntityComponent.class);
        EntityRef entityToLaunch = entityManager.create(launchEntity.launchEntityPrefab);
        
        if(entityToLaunch != EntityRef.NULL){
            Vector3f impulse = entity.getComponent(LocationComponent.class).getWorldDirection();
            impulse.normalize();
            impulse.mul(launchEntity.impulse);
            physics.getRigidBody(entityToLaunch).applyImpulse(impulse);
        }
        else{
            // dispatch no ammo event
        }
    }
}
