package org.terasology.combatSystem.traps.components;

import java.util.List;

import org.terasology.entitySystem.Component;
import org.terasology.math.geom.Vector3i;
import org.terasology.reflection.MappedContainer;

public class AddSwitchDoorsComponent implements Component{
    public List<DoorsToSpawn> doorsToSpawn;
    
    @MappedContainer
    public static class DoorsToSpawn {
        public Vector3i switchPos;
        public List<Vector3i> doorsPos;
    }

}
