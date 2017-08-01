package org.terasology.combatSystem.traps.components;

import java.util.List;

import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.world.block.ForceBlockActive;

import com.google.common.collect.Lists;

@ForceBlockActive
public class SwitchComponent implements Component{
    public List<EntityRef> doors = Lists.newArrayList();
}
