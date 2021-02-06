// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.traps.components;

import com.google.common.collect.Lists;
import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.world.block.ForceBlockActive;

import java.util.List;

@ForceBlockActive
public class SwitchComponent implements Component{
    public List<EntityRef> doors = Lists.newArrayList();
}
