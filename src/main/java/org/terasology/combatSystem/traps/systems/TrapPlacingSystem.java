package org.terasology.combatSystem.traps.systems;

import java.util.List;

import org.terasology.combatSystem.weaponFeatures.OwnerSpecific;
import org.terasology.combatSystem.weaponFeatures.components.AttackerComponent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.entity.lifecycleEvents.OnActivatedComponent;
import org.terasology.entitySystem.event.EventPriority;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.common.ActivateEvent;
import org.terasology.logic.location.LocationComponent;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.registry.In;
import org.terasology.rendering.logic.MeshComponent;
import org.terasology.sensors.ActivateSensorEvent;
import org.terasology.sensors.DeactivateSensorEvent;
import org.terasology.sensors.volumeSensing.VolumeSensorComponent;
import org.terasology.utilities.Assets;
import org.terasology.world.block.BlockComponent;
import org.terasology.world.block.items.OnBlockToItem;

@RegisterSystem
public class TrapPlacingSystem extends BaseComponentSystem{
    @In
    LocalPlayer player;
    
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
    
    @ReceiveEvent(components = {VolumeSensorComponent.class, BlockComponent.class})
    public void activateOnBlockConversion(OnActivatedComponent event, EntityRef entity){
        entity.send(new ActivateSensorEvent());
    }
    
    @ReceiveEvent(components = {VolumeSensorComponent.class, LocationComponent.class}, netFilter = RegisterMode.CLIENT, priority = EventPriority.PRIORITY_LOW)
    public void addMeshForClient(ActivateSensorEvent event, EntityRef entity, VolumeSensorComponent volumeSensor){
        EntityRef sensor = volumeSensor.sensor;
        
        if(!entity.hasComponent(AttackerComponent.class)){
            return;
        }
        
        EntityRef character = player.getCharacterEntity();
        List<EntityRef> allOwners = OwnerSpecific.getAllOwners(entity);
        if(allOwners == null){
            return;
        }
                
        for(EntityRef attackerEntity : allOwners){
            if(attackerEntity.equals(character)){
                if(!sensor.hasComponent(MeshComponent.class)){
                    MeshComponent mesh = new MeshComponent();
                    mesh.translucent = true;
                    mesh.material = Assets.getMaterial("CombatSystem:forceField").get();
                    mesh.mesh = Assets.getMesh("CombatSystem:zesphere").get();
                    sensor.addComponent(mesh);
                }
            }
        }
    }

}
