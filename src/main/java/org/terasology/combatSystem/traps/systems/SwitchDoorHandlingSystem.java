package org.terasology.combatSystem.traps.systems;

import org.terasology.combatSystem.traps.components.SwitchComponent;
import org.terasology.combatSystem.weaponFeatures.components.ParentComponent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.entity.lifecycleEvents.OnChangedComponent;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.health.DestroyEvent;
import org.terasology.logic.health.EngineDamageTypes;
import org.terasology.world.block.BlockComponent;

@RegisterSystem
public class SwitchDoorHandlingSystem {
    
    @ReceiveEvent(components = ParentComponent.class)
    public void openDoor(OnChangedComponent event, EntityRef entity){
        ParentComponent parent = entity.getComponent(ParentComponent.class);
        if(!entity.hasComponent(SwitchComponent.class)){
            return;
        }
        boolean openDoors = false;
        for(EntityRef child : parent.children){
            if(!child.hasComponent(BlockComponent.class)){
                openDoors = true;
            }
        }
        if(openDoors){
            for(EntityRef child : parent.children){
                if(child.hasComponent(BlockComponent.class)){
                    child.send(new DestroyEvent(entity, entity, EngineDamageTypes.DIRECT.get()));
                }
            }
        }
    }

}
