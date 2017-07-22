package org.terasology.combatSystem.traps.systems;

import org.terasology.combatSystem.traps.components.BeaconComponent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.sensors.EntitySensedEvent;

@RegisterSystem
public class BeaconTrapHandlingSystem extends BaseComponentSystem{
    
    @ReceiveEvent(components = BeaconComponent.class)
    public void sendEntityInfoOnSense(EntitySensedEvent event, EntityRef entity, BeaconComponent beacon){
        if(beacon.base != null && beacon.base != EntityRef.NULL && beacon.base.exists()){
            beacon.base.send(event);
        }
    }
    
    

}
