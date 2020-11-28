package org.terasology.combatSystem.world.structures.components;

import java.util.List;

import org.joml.Vector3i;
import org.terasology.entitySystem.Component;
import org.terasology.network.Replicate;
import org.terasology.reflection.MappedContainer;

public class AddSwitchDoorsComponent implements Component{
    @Replicate
    public List<DoorsToSpawn> doorsToSpawn;

    @MappedContainer
    public static class DoorsToSpawn {
        public Vector3i switchPos;
        public List<Vector3i> doorsPos;
    }

}
