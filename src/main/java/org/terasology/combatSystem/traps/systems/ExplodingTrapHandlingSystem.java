package org.terasology.combatSystem.traps.systems;

import org.terasology.combatSystem.weaponFeatures.components.ExplodeComponent;
import org.terasology.combatSystem.weaponFeatures.events.ExplodeEvent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.sensors.DeactivateSensorEvent;
import org.terasology.sensors.EntitySensedEvent;

@RegisterSystem
public class ExplodingTrapHandlingSystem extends BaseComponentSystem{
    
    @ReceiveEvent(components = ExplodeComponent.class)
    public void explodeOnSense(EntitySensedEvent event, EntityRef entity){
        entity.send(new DeactivateSensorEvent());
        entity.send(new ExplodeEvent());
    }

}
