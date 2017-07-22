package org.terasology.combatSystem.weaponFeatures.systems;

import org.terasology.combatSystem.weaponFeatures.components.LaunchEntityComponent;
import org.terasology.combatSystem.weaponFeatures.components.WorldAvatarComponent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.entity.lifecycleEvents.OnChangedComponent;
import org.terasology.entitySystem.event.EventPriority;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.inventory.InventoryComponent;
import org.terasology.logic.inventory.InventoryUtils;
import org.terasology.logic.inventory.events.MoveItemAmountRequest;
import org.terasology.logic.inventory.events.MoveItemRequest;

@RegisterSystem
public class LoadingEntityForLaunchHandlingSystem extends BaseComponentSystem{
    
    @ReceiveEvent(priority = EventPriority.PRIORITY_HIGH)
    public void avoidPuttingNonThrowableItemsInQuiver(MoveItemAmountRequest event, EntityRef entity){
        EntityRef quiver = event.getToInventory();
        if(!quiver.hasComponent(LaunchEntityComponent.class)){
            return;
        }
        
        EntityRef item = InventoryUtils.getItemAt(event.getFromInventory(), event.getFromSlot());
        
        if(!item.hasComponent(WorldAvatarComponent.class)){
            event.consume();
        }
    }
    
    @ReceiveEvent(priority = EventPriority.PRIORITY_HIGH)
    public void avoidPuttingNonThrowableItemsInQuiver(MoveItemRequest event, EntityRef entity){
        EntityRef quiver = event.getToInventory();
        if(!quiver.hasComponent(LaunchEntityComponent.class)){
            return;
        }
        
        EntityRef item = InventoryUtils.getItemAt(event.getFromInventory(), event.getFromSlot());
        
        if(!item.hasComponent(WorldAvatarComponent.class)){
            event.consume();
        }
    }
    
    @ReceiveEvent( components = {InventoryComponent.class})
    public void makePrefabForLaunchAvailable(OnChangedComponent event, EntityRef entity){
        LaunchEntityComponent launchEntity = entity.getComponent(LaunchEntityComponent.class);
        if(launchEntity == null){
            return;
        }
        EntityRef entityToLaunch = InventoryUtils.getItemAt(entity, 0);
        
        if(entityToLaunch == EntityRef.NULL){
            launchEntity.launchEntityPrefab = null;
            entity.saveComponent(launchEntity);
            return;
        }
        
        WorldAvatarComponent avatar = entityToLaunch.getComponent(WorldAvatarComponent.class);
        if(avatar == null){
            launchEntity.launchEntityPrefab = null;
            entity.saveComponent(launchEntity);
            return;
        }
        
        launchEntity.launchEntityPrefab = avatar.worldAvatarPrefab;
        entity.saveComponent(launchEntity);
    }

}
