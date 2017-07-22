package org.terasology.combatSystem.widgets;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.logic.characters.CharacterHeldItemComponent;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.registry.In;
import org.terasology.rendering.nui.layers.hud.CoreHudWidget;
import org.terasology.rendering.nui.layers.ingame.inventory.InventoryCell;

public class QuiverHud extends CoreHudWidget{
    @In
    private LocalPlayer localPlayer;
    
    @Override
    public void initialise() {
        EntityRef character = localPlayer.getCharacterEntity();
        CharacterHeldItemComponent component = character.getComponent(CharacterHeldItemComponent.class);
        if(component == null){
            return;
        }
        EntityRef item = component.selectedItem;
        
        for (InventoryCell cell : findAll(InventoryCell.class)) {
            cell.setTargetInventory(item);
        }
    }
    
    @Override
    public void onOpened(){
        initialise();
    }

}
