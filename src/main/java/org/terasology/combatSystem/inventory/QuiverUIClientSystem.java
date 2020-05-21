package org.terasology.combatSystem.inventory;

import org.terasology.assets.management.AssetManager;
import org.terasology.combatSystem.weaponFeatures.components.LaunchEntityComponent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.entity.lifecycleEvents.OnChangedComponent;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.characters.CharacterHeldItemComponent;
import org.terasology.logic.inventory.InventoryComponent;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.registry.In;
import org.terasology.rendering.nui.NUIManager;
import org.terasology.rendering.nui.asset.UIElement;

import java.util.Optional;

/**
 * This system runs only on client and handles the removal or addition of quiver inventory slot on screen.
 */
@RegisterSystem(RegisterMode.CLIENT)
public class QuiverUIClientSystem extends BaseComponentSystem {

    @In
    private NUIManager nuiManager;
    
    @In
    private AssetManager assetManager;

    @In
    private LocalPlayer player;

    @ReceiveEvent(components = {CharacterHeldItemComponent.class})
    public void onLauncherSelected(OnChangedComponent event, EntityRef character) {
        if (character != null && character.equals(player.getCharacterEntity())) {
            CharacterHeldItemComponent heldItem = character.getComponent(CharacterHeldItemComponent.class);
            EntityRef item = heldItem.selectedItem;

            if (item == null || item == EntityRef.NULL || !item.exists()) {
                Optional<? extends UIElement> data = assetManager.getAsset("quiverHud", UIElement.class);
                nuiManager.getHUD().removeHUDElement(data.get().getUrn());
            }

            if (item.hasComponent(InventoryComponent.class) && item.hasComponent(LaunchEntityComponent.class)) {
                nuiManager.getHUD().addHUDElement("quiverHud");
            } else {
                Optional<? extends UIElement> data = assetManager.getAsset("quiverHud", UIElement.class);
                nuiManager.getHUD().removeHUDElement(data.get().getUrn());
            }
        }
    }
}
