package org.terasology.combatSystem.traps.systems;

import org.terasology.combatSystem.traps.components.SwitchComponent;
import org.terasology.combatSystem.weaponFeatures.components.ParentComponent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.entity.lifecycleEvents.OnActivatedComponent;
import org.terasology.entitySystem.entity.lifecycleEvents.OnChangedComponent;
import org.terasology.entitySystem.event.EventPriority;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.health.DestroyEvent;
import org.terasology.logic.health.EngineDamageTypes;

@RegisterSystem
public class SwitchDoorHandlingSystem extends BaseComponentSystem{
    
    @ReceiveEvent(components = ParentComponent.class)
    public void openDoor(OnActivatedComponent event, EntityRef entity){
        SwitchComponent switchComp = entity.getComponent(SwitchComponent.class);
        if(switchComp == null){
            return;
        }
        
        for(EntityRef door : switchComp.doors){
            if(door == null || door == EntityRef.NULL || !door.exists()){
                continue;
            }
            door.send(new DestroyEvent(entity, entity, EngineDamageTypes.DIRECT.get()));
        }
    }
    
    @ReceiveEvent(components = ParentComponent.class)
    public void openDoor(OnChangedComponent event, EntityRef entity){
        SwitchComponent switchComp = entity.getComponent(SwitchComponent.class);
        if(switchComp == null){
            return;
        }
        
        for(EntityRef door : switchComp.doors){
            if(door == null || door == EntityRef.NULL || !door.exists()){
                continue;
            }
            door.send(new DestroyEvent(entity, entity, EngineDamageTypes.DIRECT.get()));
        }
    }
    
    @ReceiveEvent(components = SwitchComponent.class, priority = EventPriority.PRIORITY_HIGH)
    public void openDoor(DestroyEvent event, EntityRef entity){
        SwitchComponent switchComp = entity.getComponent(SwitchComponent.class);
        
        for(EntityRef door : switchComp.doors){
            if(door == null || door == EntityRef.NULL || !door.exists()){
                continue;
            }
            door.send(new DestroyEvent(entity, entity, EngineDamageTypes.DIRECT.get()));
        }
    }
}
