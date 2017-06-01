package org.terasology.combatSystem.weaponFeatures.systems;

import org.terasology.combatSystem.physics.components.GravityComponent;
import org.terasology.combatSystem.physics.components.MassComponent;
import org.terasology.combatSystem.weaponFeatures.components.StickComponent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.location.LocationComponent;
import org.terasology.math.geom.Vector3f;
import org.terasology.physics.components.TriggerComponent;
import org.terasology.physics.events.CollideEvent;

@RegisterSystem
public class CollisionHandlingSystem extends BaseComponentSystem{
    
    @ReceiveEvent(components = {StickComponent.class})
    public void sticking(CollideEvent event, EntityRef entity){
        EntityRef otherEntity = event.getOtherEntity();
        LocationComponent location = entity.getComponent(LocationComponent.class);
        LocationComponent otherEntityLocation = otherEntity.getComponent(LocationComponent.class);
        if(location != null && otherEntityLocation != null){
            Vector3f finalLoc = event.getOtherEntityContactPoint();
            location.setWorldPosition(finalLoc);
            
            MassComponent body = entity.getComponent(MassComponent.class);
            body.acceleration.set(0, 0, 0);
            body.velocity.set(0, 0, 0);
            body.force.set(0, 0, 0);
            entity.removeComponent(TriggerComponent.class);
            entity.saveComponent(body);
            entity.saveComponent(location);
        }
        
        entity.removeComponent(GravityComponent.class);
        
    }

}
