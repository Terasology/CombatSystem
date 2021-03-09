// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.world.structures.components;

import org.joml.Vector3i;
import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.network.Replicate;
import org.terasology.reflection.MappedContainer;

import java.util.List;

public class AddSwitchDoorsComponent implements Component{
    @Replicate
    public List<DoorsToSpawn> doorsToSpawn;

    @MappedContainer
    public static class DoorsToSpawn {
        public Vector3i switchPos;
        public List<Vector3i> doorsPos;
    }

}
