package org.terasology.combatSystem.world;

import java.util.List;
import java.util.Map;

import org.terasology.combatSystem.weaponFeatures.components.ExplodeComponent;
import org.terasology.combatSystem.weaponFeatures.components.LaunchEntityComponent;
import org.terasology.combatSystem.weaponFeatures.components.ParentComponent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.EventPriority;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.In;
import org.terasology.sensors.volumeSensing.VolumeSensorComponent;
import org.terasology.structureTemplates.events.GetStructureTemplateBlocksEvent;
import org.terasology.structureTemplates.events.StructureBlocksSpawnedEvent;
import org.terasology.world.BlockEntityRegistry;
import org.terasology.world.block.Block;
import org.terasology.world.block.family.BlockFamily;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@RegisterSystem
public class StructuresHandlingSystem extends BaseComponentSystem{
    @In
    BlockEntityRegistry registry;
    
    Map<Vector3i, Block> switchBlocks = Maps.newHashMap();
    Map<Vector3i, Block> doorBlocks = Maps.newHashMap();
    
    @ReceiveEvent
    public void activateBlocksOnStructureSpawn(StructureBlocksSpawnedEvent event, EntityRef entity){
        GetStructureTemplateBlocksEvent getBlocksEvent =  new GetStructureTemplateBlocksEvent(event.getTransformation());
        entity.send(getBlocksEvent);
        Map<Vector3i, Block> blocksPlaced = getBlocksEvent.getBlocksToPlace();
        for (Map.Entry<Vector3i, Block> entry: blocksPlaced.entrySet()) {
            Block block = entry.getValue();
            BlockFamily family = block.getBlockFamily();
            if(family.hasCategory("switch")){
                switchBlocks.put(entry.getKey(), block);
            }
            if(family.hasCategory("door")){
                doorBlocks.put(entry.getKey(), block);
            }
            if(family.hasCategory("trap")){
                EntityRef trapEntity = registry.getBlockEntityAt(entry.getKey());
                VolumeSensorComponent component = trapEntity.getComponent(VolumeSensorComponent.class);
                if(component == null){
                    continue;
                }
                if(trapEntity.hasComponent(ExplodeComponent.class)){
                    component.range = 1.0f;
                    trapEntity.saveComponent(component);
                }
                if(trapEntity.hasComponent(LaunchEntityComponent.class)){
                    
                }
            }
        }
        
        for (Map.Entry<Vector3i, Block> entry: switchBlocks.entrySet()) {
            Vector3i switchPos = entry.getKey();
            Vector3i switchFloorPos = new Vector3i(switchPos);
            switchFloorPos.y = 0;
            float distanceSq = 0;
            List<Vector3i> bestDoorPos = Lists.newArrayList();
            for (Map.Entry<Vector3i, Block> doorEntry: doorBlocks.entrySet()) {
                Vector3i doorPos = doorEntry.getKey();
                Vector3i doorFloorPos = new Vector3i(doorPos);
                doorFloorPos.y = 0;
                
                float disSq = doorFloorPos.distanceSquared(switchFloorPos);
                if(distanceSq == 0){
                    distanceSq = disSq;
                    bestDoorPos.add(doorPos);
                }
                else if(disSq < distanceSq){
                    distanceSq = disSq;
                    bestDoorPos.clear();
                    bestDoorPos.add(doorPos);
                }
                else if(disSq == distanceSq){
                    bestDoorPos.add(doorPos);
                }
            }
            List<EntityRef> doorEntityList = Lists.newArrayList();
            for(Vector3i bestDoor : bestDoorPos){
                doorEntityList.add(registry.getBlockEntityAt(bestDoor));
            }
            EntityRef switchEntity = registry.getBlockEntityAt(switchPos);
            ParentComponent parent = switchEntity.getComponent(ParentComponent.class);
            parent.children.addAll(doorEntityList);
            switchEntity.saveComponent(parent);
        }
    }

}
