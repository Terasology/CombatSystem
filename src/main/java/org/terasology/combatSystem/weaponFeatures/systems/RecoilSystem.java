package org.terasology.combatSystem.weaponFeatures.systems;

import org.terasology.combatSystem.weaponFeatures.OwnerSpecific;
import org.terasology.combatSystem.weaponFeatures.components.RecoilComponent;
import org.terasology.combatSystem.weaponFeatures.events.LaunchEntityEvent;
import org.terasology.engine.Time;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.EventPriority;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.input.events.MouseYAxisEvent;
import org.terasology.logic.inventory.ItemComponent;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.registry.In;

@RegisterSystem
public class RecoilSystem extends BaseComponentSystem{
    @In
    private LocalPlayer localPlayer;
    @In
    private Time time;
    
    @ReceiveEvent(components = {RecoilComponent.class}, priority = EventPriority.PRIORITY_LOW)
    public void recoil(LaunchEntityEvent event, EntityRef entity, RecoilComponent recoil){
        EntityRef recoilEntity = entity;
        if(entity.hasComponent(ItemComponent.class)){
            recoilEntity = OwnerSpecific.getUltimateOwner(entity);
        }
        recoilEntity(recoilEntity, recoil.amount);
    }
    
    //-------------------------private methods---------------------------
    private void recoilEntity(EntityRef entity, float recoilAmount){
        
        MouseYAxisEvent requestedEvent = new MouseYAxisEvent(recoilAmount, time.getGameDelta());
        entity.send(requestedEvent);
    }
}
