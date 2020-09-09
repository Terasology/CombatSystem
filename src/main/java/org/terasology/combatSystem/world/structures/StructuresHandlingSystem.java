// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.world.structures;

import com.google.common.collect.Maps;
import org.terasology.combatSystem.traps.components.ActivateOnPlaceComponent;
import org.terasology.combatSystem.traps.components.SwitchComponent;
import org.terasology.combatSystem.world.structures.components.AddSwitchDoorsComponent;
import org.terasology.combatSystem.world.structures.components.AddSwitchDoorsComponent.DoorsToSpawn;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.math.Region3i;
import org.terasology.engine.registry.In;
import org.terasology.engine.world.BlockEntityRegistry;
import org.terasology.engine.world.block.Block;
import org.terasology.engine.world.block.family.BlockFamily;
import org.terasology.math.geom.Vector3i;
import org.terasology.sensors.PhysicalSensorComponent;
import org.terasology.structureTemplates.components.SpawnBlockRegionsComponent;
import org.terasology.structureTemplates.components.SpawnBlockRegionsComponent.RegionToFill;
import org.terasology.structureTemplates.events.StructureBlocksSpawnedEvent;
import org.terasology.structureTemplates.util.BlockRegionTransform;

import java.util.Map;

@RegisterSystem
public class StructuresHandlingSystem extends BaseComponentSystem {
    @In
    BlockEntityRegistry registry;

    Map<Vector3i, Block> switchBlocks = Maps.newHashMap();
    Map<Vector3i, Block> doorBlocks = Maps.newHashMap();

    @ReceiveEvent
    public void activateBlocksOnStructureSpawn(StructureBlocksSpawnedEvent event, EntityRef entity,
                                               SpawnBlockRegionsComponent spawnBlockRegions) {
        BlockRegionTransform transformation = event.getTransformation();

        for (RegionToFill regionToFill : spawnBlockRegions.regionsToFill) {
            Block block = regionToFill.blockType;
            BlockFamily family = block.getBlockFamily();
            if (family.hasCategory("trap")) {
                Region3i region = regionToFill.region;
                region = transformation.transformRegion(region);
                block = transformation.transformBlock(block);

                for (Vector3i blockPos : region) {
                    awakenAndModifyTrap(block, blockPos);
                }
            }
        }

        AddSwitchDoorsComponent switchDoors = entity.getComponent(AddSwitchDoorsComponent.class);
        if (switchDoors == null) {
            return;
        }

        for (DoorsToSpawn doors : switchDoors.doorsToSpawn) {
            Vector3i switchPos = doors.switchPos;
            switchPos = transformation.transformVector3i(switchPos);
            EntityRef switchEntity = registry.getBlockEntityAt(switchPos);

            SwitchComponent switchComp = switchEntity.getComponent(SwitchComponent.class);
            if (switchComp == null) {
                continue;
            }

            for (Vector3i doorPos : doors.doorsPos) {
                doorPos = transformation.transformVector3i(doorPos);
                EntityRef doorEntity = registry.getBlockEntityAt(doorPos);

                switchComp.doors.add(doorEntity);
            }
            switchEntity.saveComponent(switchComp);
        }
    }

    //-------------------------------private methods-------------------------

    private void awakenAndModifyTrap(Block block, Vector3i blockPos) {
        EntityRef trapEntity = registry.getBlockEntityAt(blockPos);
        PhysicalSensorComponent component = trapEntity.getComponent(PhysicalSensorComponent.class);
        if (component == null) {
            return;
        }
        trapEntity.addComponent(new ActivateOnPlaceComponent());
    }

}
