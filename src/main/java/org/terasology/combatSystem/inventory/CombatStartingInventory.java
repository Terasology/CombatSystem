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
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.block.family.BlockFamily;
import org.terasology.world.block.items.BlockItemFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private final int NUM_ARROWS = 16;
    private Map<String, Integer> blocks = new HashMap<>();
    private Map<String, Integer> items = new HashMap<>();

    @Override
    public void initialise() {
        populateDefaultInventory();
    }

    @ReceiveEvent(components = {InventoryComponent.class})
    public void onPlayerSpawn(OnPlayerSpawnedEvent event, EntityRef player) {
        List<EntityRef> entities = createItemEntities();
        for (EntityRef item : entities) {
            item.send(new GiveItemEvent(player));
        }
    }

    public void setItems(Map<String, Integer> items, Map<String, Integer> blocks) {
        this.blocks = blocks;
        this.items = items;
    }

    private void populateDefaultInventory() {
        items.put("CombatSystem:bow", 1);
        items.put("CombatSystem:stickArrowItem", NUM_ARROWS);
        items.put("CombatSystem:bounceArrowItem", NUM_ARROWS);
        items.put("CombatSystem:explodeArrowItem", NUM_ARROWS);
        items.put("CombatSystem:sword", 1);
        items.put("CombatSystem:waraxe", 1);
        items.put("CombatSystem:staff", 1);
        items.put("CombatSystem:spearItem", 1);

        blocks.put("fireBallMine", 1);
        blocks.put("explodeMine", 1);
    }

    private List<EntityRef> createItemEntities() {
        List<EntityRef> entities = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                entities.add(entityManager.create(entry.getKey()));
            }
        }

        BlockItemFactory factory = new BlockItemFactory(entityManager);
        for (Map.Entry<String, Integer> entry : blocks.entrySet()) {
            BlockFamily family = blockManager.getBlockFamily(entry.getKey());
            if (family != null) {
                entities.add(factory.newInstance(family, entry.getValue()));
            }
        }

        return entities;
    }
}
