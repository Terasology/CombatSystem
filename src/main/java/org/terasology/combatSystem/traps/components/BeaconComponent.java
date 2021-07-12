// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.combatSystem.traps.components;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;

public class BeaconComponent implements Component<BeaconComponent> {
    @Replicate
    public EntityRef base = EntityRef.NULL;

    @Override
    public void copy(BeaconComponent other) {
        this.base = other.base;
    }
}
