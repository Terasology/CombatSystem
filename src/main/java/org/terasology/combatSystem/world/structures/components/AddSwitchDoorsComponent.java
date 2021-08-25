// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.world.structures.components;

import com.google.common.collect.Lists;
import org.joml.Vector3i;
import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;
import org.terasology.reflection.MappedContainer;

import java.util.List;
import java.util.stream.Collectors;

public class AddSwitchDoorsComponent implements Component<AddSwitchDoorsComponent> {
    @Replicate
    public List<DoorsToSpawn> doorsToSpawn = Lists.newArrayList();

    @Override
    public void copyFrom(AddSwitchDoorsComponent other) {
        this.doorsToSpawn = other.doorsToSpawn.stream().map(DoorsToSpawn::copy).collect(Collectors.toList());
    }

    @MappedContainer
    public static class DoorsToSpawn {
        public Vector3i switchPos;
        public List<Vector3i> doorsPos;

        /**
         * Create a <i>deep copy</i> of this data class.
         */
        DoorsToSpawn copy() {
            DoorsToSpawn copy = new DoorsToSpawn();
            copy.switchPos = new Vector3i(this.switchPos);
            copy.doorsPos = this.doorsPos.stream().map(Vector3i::new).collect(Collectors.toList());
            return copy;
        }
    }

}
