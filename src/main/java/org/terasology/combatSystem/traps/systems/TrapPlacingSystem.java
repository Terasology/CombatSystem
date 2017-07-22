package org.terasology.combatSystem.traps.systems;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.common.ActivateEvent;
import org.terasology.logic.location.LocationComponent;
import org.terasology.sensors.ActivateSensorEvent;
import org.terasology.sensors.DeactivateSensorEvent;
import org.terasology.sensors.volumeSensing.VolumeSensorComponent;
import org.terasology.world.block.items.OnBlockItemPlaced;
import org.terasology.world.block.items.OnBlockToItem;

@RegisterSystem
public class TrapPlacingSystem extends BaseComponentSystem{
    
    @ReceiveEvent(components = {VolumeSensorComponent.class, LocationComponent.class})
    public void activateOrDeactivateTrap(ActivateEvent event, EntityRef entity, VolumeSensorComponent volumeSensor){
        if(volumeSensor.sensor == null || volumeSensor.sensor == EntityRef.NULL || !volumeSensor.sensor.exists()){
            entity.send(new ActivateSensorEvent());
        }
        else{
            entity.send(new DeactivateSensorEvent());
        }
    }
    
    @ReceiveEvent(components = {VolumeSensorComponent.class})
    public void deactivateOnItemConversion(OnBlockToItem event, EntityRef entity){
        entity.send(new DeactivateSensorEvent());
    }
    
//    @ReceiveEvent(components = {VolumeSensorComponent.class})
//    public void activateOnBlockConversion(OnBlockItemPlaced event, EntityRef entity){
//        entity.send(new ActivateSensorEvent());
//    }

}
