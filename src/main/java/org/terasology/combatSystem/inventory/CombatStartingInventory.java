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
import org.terasology.entitySystem.event.EventPriority;
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

import java.util.HashMap;
import java.util.Map;

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

    private Map<EntityRef, Integer> items = new HashMap<>();

    public void setItems(Map<EntityRef, Integer> map) {
        items = map;
    }

    private void populateDefaultInventory() {
        items.put(entityManager.create("CombatSystem:bow"), 1);
        items.put(entityManager.create("CombatSystem:stickArrowItem"), 16);
        items.put(entityManager.create("CombatSystem:bounceArrowItem"), 16);
        items.put(entityManager.create("CombatSystem:explodeArrowItem"), 16);
        items.put(entityManager.create("CombatSystem:sword"), 1);
        items.put(entityManager.create("CombatSystem:waraxe"), 1);
        items.put(entityManager.create("CombatSystem:staff"), 1);
        items.put(entityManager.create("CombatSystem:spearItem"), 1);

        BlockFamily fireBallLauncherItem = blockManager.getBlockFamily("fireBallMine");
        if (fireBallLauncherItem != null) {
            BlockItemFactory blockItemFactory = new BlockItemFactory(entityManager);
            items.put(blockItemFactory.newInstance(fireBallLauncherItem), 1);
        }

        BlockFamily explodingMineItem = blockManager.getBlockFamily("explodeMine");
        if (explodingMineItem != null) {
            BlockItemFactory blockItemFactory = new BlockItemFactory(entityManager);
            items.put(blockItemFactory.newInstance(explodingMineItem), 1);
        }
    }

    @ReceiveEvent(components = {InventoryComponent.class})
    public void givingWeaponsToPlayers(OnPlayerSpawnedEvent event, EntityRef player) {
        if (items.isEmpty()) {
            populateDefaultInventory();
        }

        for (EntityRef item : items.keySet()) {
            for (int i = 0; i < items.get(item); i++) {
                item.send(new GiveItemEvent(player));
            }
        }
    }
}
