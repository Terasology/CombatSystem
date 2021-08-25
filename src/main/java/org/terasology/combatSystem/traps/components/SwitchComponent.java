// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.combatSystem.traps.components;

import com.google.common.collect.Lists;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.world.block.ForceBlockActive;
import org.terasology.gestalt.entitysystem.component.Component;

import java.util.List;

@ForceBlockActive
public class SwitchComponent implements Component<SwitchComponent> {
    public List<EntityRef> doors = Lists.newArrayList();

    @Override
    public void copyFrom(SwitchComponent other) {
        this.doors = Lists.newArrayList(other.doors);
    }
}
