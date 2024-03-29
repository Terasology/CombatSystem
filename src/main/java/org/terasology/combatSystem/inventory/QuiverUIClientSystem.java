// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.inventory;

import org.terasology.combatSystem.weaponFeatures.components.LaunchEntityComponent;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.OnAddedComponent;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.OnChangedComponent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.characters.CharacterHeldItemComponent;
import org.terasology.engine.logic.players.LocalPlayer;
import org.terasology.engine.registry.In;
import org.terasology.engine.rendering.nui.NUIManager;
import org.terasology.gestalt.assets.management.AssetManager;
import org.terasology.gestalt.entitysystem.event.ReceiveEvent;
import org.terasology.module.inventory.components.InventoryComponent;
import org.terasology.nui.asset.UIElement;

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

    // case: start up -> CharacterHeldItemComponent being added
    @ReceiveEvent
    public void reactTo(OnAddedComponent event, EntityRef character, CharacterHeldItemComponent heldItem) {
        onAmmoBearingItemSelected(heldItem);
    }

    // case: in game after start up -> CharacterHeldItemComponent being changed
    @ReceiveEvent
    public void reactTo(OnChangedComponent event, EntityRef character, CharacterHeldItemComponent heldItem) {
        onAmmoBearingItemSelected(heldItem);
    }

    public void onAmmoBearingItemSelected(CharacterHeldItemComponent heldItem) {
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
