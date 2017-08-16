package org.terasology.combatSystem.weaponFeatures.systems;

import org.terasology.combatSystem.weaponFeatures.components.SniperScopeComponent;
import org.terasology.combatSystem.weaponFeatures.events.PrimaryAttackEvent;
import org.terasology.combatSystem.weaponFeatures.events.SecondaryAttackEvent;
import org.terasology.combatSystem.weaponFeatures.events.ToggleScopeEvent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.logic.players.LocalPlayerSystem;
import org.terasology.registry.In;

@RegisterSystem
public class SniperScopeSystem extends BaseComponentSystem{
    @In
    private LocalPlayer localPlayer;
    @In
    private LocalPlayerSystem localPlayerSystem;
    
    @ReceiveEvent(components = {SniperScopeComponent.class})
    public void toggleScopePrimary(PrimaryAttackEvent event, EntityRef entity, SniperScopeComponent sniperScope){
        if(sniperScope.primaryAttack){
            entity.send(new ToggleScopeEvent());
        }
    }
    
    @ReceiveEvent(components = {SniperScopeComponent.class})
    public void toggleScopeSecondary(SecondaryAttackEvent event, EntityRef entity, SniperScopeComponent sniperScope){
        if(!sniperScope.primaryAttack){
            entity.send(new ToggleScopeEvent());
        }
    }
    
    @ReceiveEvent(components = {SniperScopeComponent.class})
    public void togglingScope(ToggleScopeEvent event, EntityRef entity, SniperScopeComponent sniperScope){
        toggleScope(entity, sniperScope);
    }
    
    //-------------------------------private methods---------------------------------
    private void toggleScope(EntityRef entity, SniperScopeComponent sniperScope){
        if(!sniperScope.scoped){
            localPlayerSystem.playerCamera.extendFov(sniperScope.zoom);
            sniperScope.scoped = true;
        }
        else{
            localPlayerSystem.playerCamera.resetFov();
            sniperScope.scoped = false;
        }
        
        entity.saveComponent(sniperScope);
    }

}
