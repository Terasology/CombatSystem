/*
 * Copyright 2019 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.terasology.combatSystem.inventory;

import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.inventory.InventoryComponent;
import org.terasology.logic.inventory.events.GiveItemEvent;
import org.terasology.logic.players.event.OnPlayerSpawnedEvent;
import org.terasology.registry.In;
import org.terasology.registry.Share;
import org.terasology.world.block.BlockManager;
import org.terasology.world.block.family.BlockFamily;
import org.terasology.world.block.items.BlockItemFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * implements a basic inventory at start of game for testing weapons.
 */
@Share(CombatStartingInventory.class)
@RegisterSystem
public class CombatStartingInventory extends BaseComponentSystem {

    @In
    EntityManager entityManager;
    @In
    BlockManager blockManager;

    private final int numArrows = 16;
    
    private Set<EntityRef> items = new HashSet<>();

    public void setItems(Set<EntityRef> set) {
        items = set;
    }

    private void populateDefaultInventory() {

        items.add(entityManager.create("CombatSystem:bow"));

        for (int i = 0; i < numArrows; i++) {
            items.add(entityManager.create("CombatSystem:stickArrowItem"));
            items.add(entityManager.create("CombatSystem:bounceArrowItem"));
            items.add(entityManager.create("CombatSystem:explodeArrowItem"));
        }

        items.add(entityManager.create("CombatSystem:sword"));
        items.add(entityManager.create("CombatSystem:waraxe"));
        items.add(entityManager.create("CombatSystem:staff"));
        items.add(entityManager.create("CombatSystem:spearItem"));

        BlockFamily fireBallLauncherItem = blockManager.getBlockFamily("fireBallMine");
        if (fireBallLauncherItem != null) {
            BlockItemFactory blockItemFactory = new BlockItemFactory(entityManager);
            items.add(blockItemFactory.newInstance(fireBallLauncherItem));
        }

        BlockFamily explodingMineItem = blockManager.getBlockFamily("explodeMine");
        if (explodingMineItem != null) {
            BlockItemFactory blockItemFactory = new BlockItemFactory(entityManager);
            items.add(blockItemFactory.newInstance(explodingMineItem));
        }
    }

    @ReceiveEvent(components = {InventoryComponent.class})
    public void givingWeaponsToPlayers(OnPlayerSpawnedEvent event, EntityRef player) {
        if (items.isEmpty()) {
            populateDefaultInventory();
        }

        for (EntityRef item : items) {
            item.send(new GiveItemEvent(player));
        }
    }
}
