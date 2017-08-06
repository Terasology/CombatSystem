package org.terasology.combatSystem.components;

import java.util.List;

import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.math.geom.Vector3f;
import org.terasology.reflection.MappedContainer;
import org.terasology.world.block.ForceBlockActive;

import com.google.common.collect.Lists;

@ForceBlockActive
public class EntitiesConnectorComponent implements Component{
    public List<ConnectingInfo> childEntities = Lists.newArrayList();
    
    @MappedContainer
    public static class ConnectingInfo{
        public Prefab prefab;
        public Vector3f offset;
        public boolean addParentAsChild = false;
    }

}
