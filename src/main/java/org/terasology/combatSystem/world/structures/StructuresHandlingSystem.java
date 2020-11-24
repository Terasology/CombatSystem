package org.terasology.combatSystem.world.structures;

import java.util.Map;

import org.joml.Vector3ic;
import org.terasology.combatSystem.traps.components.ActivateOnPlaceComponent;
import org.terasology.combatSystem.traps.components.SwitchComponent;
import org.terasology.combatSystem.world.structures.components.AddSwitchDoorsComponent;
import org.terasology.combatSystem.world.structures.components.AddSwitchDoorsComponent.DoorsToSpawn;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.math.JomlUtil;
import org.terasology.math.Region3i;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.In;
import org.terasology.sensors.PhysicalSensorComponent;
import org.terasology.structureTemplates.components.SpawnBlockRegionsComponent;
import org.terasology.structureTemplates.components.SpawnBlockRegionsComponent.RegionToFill;
import org.terasology.structureTemplates.events.StructureBlocksSpawnedEvent;
import org.terasology.structureTemplates.util.BlockRegionTransform;
import org.terasology.world.BlockEntityRegistry;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockRegion;
import org.terasology.world.block.BlockRegionIterable;
import org.terasology.world.block.family.BlockFamily;

import com.google.common.collect.Maps;

@RegisterSystem
public class StructuresHandlingSystem extends BaseComponentSystem{
    @In
    BlockEntityRegistry registry;

    Map<Vector3i, Block> switchBlocks = Maps.newHashMap();
    Map<Vector3i, Block> doorBlocks = Maps.newHashMap();

    @ReceiveEvent
    public void activateBlocksOnStructureSpawn(StructureBlocksSpawnedEvent event, EntityRef entity, SpawnBlockRegionsComponent spawnBlockRegions){
        BlockRegionTransform transformation = event.getTransformation();

        for (RegionToFill regionToFill : spawnBlockRegions.regionsToFill) {
            Block block = regionToFill.blockType;
            BlockFamily family = block.getBlockFamily();
            if(family.hasCategory("trap")){
                BlockRegion region = regionToFill.region;
                region = transformation.transformRegion(region);
                block = transformation.transformBlock(block);

                for(Vector3ic blockPos : BlockRegionIterable.region(region).build()){
                    awakenAndModifyTrap(block, JomlUtil.from(blockPos));
                }
            }
        }

        AddSwitchDoorsComponent switchDoors = entity.getComponent(AddSwitchDoorsComponent.class);
        if(switchDoors == null){
            return;
        }

        for(DoorsToSpawn doors : switchDoors.doorsToSpawn){
            org.joml.Vector3i switchPos = doors.switchPos;
            switchPos = transformation.transformVector3i(switchPos);
            EntityRef switchEntity = registry.getBlockEntityAt(switchPos);

            SwitchComponent switchComp = switchEntity.getComponent(SwitchComponent.class);
            if(switchComp == null){
                continue;
            }

            for(org.joml.Vector3i doorPos : doors.doorsPos){
                doorPos = transformation.transformVector3i(doorPos);
                EntityRef doorEntity = registry.getBlockEntityAt(doorPos);

                switchComp.doors.add(doorEntity);
            }
            switchEntity.saveComponent(switchComp);
        }
    }

    //-------------------------------private methods-------------------------

    private void awakenAndModifyTrap(Block block, Vector3i blockPos){
        EntityRef trapEntity = registry.getBlockEntityAt(blockPos);
        PhysicalSensorComponent component = trapEntity.getComponent(PhysicalSensorComponent.class);
        if(component == null){
            return;
        }
        trapEntity.addComponent(new ActivateOnPlaceComponent());
    }

}
