// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.combatSystem.weaponFeatures.components;

import com.google.common.collect.Lists;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;

import java.util.List;

public class ParentComponent implements Component<ParentComponent> {
    @Replicate
    public List<EntityRef> children;

    public ParentComponent(){
        children = Lists.<EntityRef>newArrayList();
    }

    @Override
    public void copyFrom(ParentComponent other) {
        this.children = Lists.newArrayList(other.children);
    }
}
