// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.widgets;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.logic.characters.CharacterHeldItemComponent;
import org.terasology.engine.logic.players.LocalPlayer;
import org.terasology.engine.registry.In;
import org.terasology.engine.rendering.nui.layers.hud.CoreHudWidget;
import org.terasology.module.inventory.ui.InventoryCell;

public class QuiverHud extends CoreHudWidget {
    @In
    private LocalPlayer localPlayer;

    @Override
    public void initialise() {
        EntityRef character = localPlayer.getCharacterEntity();
        CharacterHeldItemComponent component = character.getComponent(CharacterHeldItemComponent.class);
        if (component == null) {
            return;
        }
        EntityRef item = component.selectedItem;

        for (InventoryCell cell : findAll(InventoryCell.class)) {
            cell.setTargetInventory(item);
        }
    }

    @Override
    public void onOpened() {
        initialise();
    }

}
