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
import org.terasology.physics.events.ChangeVelocityEvent;
import org.terasology.physics.events.ImpulseEvent;
import org.terasology.registry.In;

@RegisterSystem(RegisterMode.AUTHORITY)
public class LaunchEntitySystem extends BaseComponentSystem{
    @In
    private EntityManager entityManager;
    
    @ReceiveEvent(components = {LaunchEntityComponent.class})
    public void onFire(LaunchEntityEvent event, EntityRef entity){
        LaunchEntityComponent launchEntity = entity.getComponent(LaunchEntityComponent.class);
        EntityRef entityToLaunch = entityManager.create(launchEntity.launchEntityPrefab);
        
        if(entityToLaunch != EntityRef.NULL){
            LocationComponent location = entityToLaunch.getComponent(LocationComponent.class);
            location.setWorldPosition(entity.getComponent(LocationComponent.class).getWorldPosition());
            entityToLaunch.addOrSaveComponent(location);
            Vector3f impulse = event.getDirection();
            impulse.normalize();
            impulse.mul(launchEntity.impulse);
            entityToLaunch.send(new ImpulseEvent(impulse));
        }
        else{
            // dispatch no ammo event
        }
    }
}
