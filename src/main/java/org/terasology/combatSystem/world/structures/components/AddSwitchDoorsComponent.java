// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.world.structures.components;

import com.google.common.collect.Lists;
import org.joml.Vector3i;
import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;
import org.terasology.reflection.MappedContainer;

import java.util.List;

public class AddSwitchDoorsComponent implements Component<AddSwitchDoorsComponent> {
    @Replicate
    public List<DoorsToSpawn> doorsToSpawn = Lists.newArrayList();

    @Override
    public void copy(AddSwitchDoorsComponent other) {
        this.doorsToSpawn = Lists.newArrayList(other.doorsToSpawn);
    }

    @MappedContainer
    public static class DoorsToSpawn {
        public Vector3i switchPos;
        public List<Vector3i> doorsPos;
    }

}
