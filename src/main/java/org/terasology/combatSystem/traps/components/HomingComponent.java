// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.combatSystem.traps.components;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;

public class HomingComponent implements Component<HomingComponent> {
    @Replicate
    public EntityRef target = EntityRef.NULL;

    @Replicate
    public int initialSleepTime = 500;

    @Replicate
    public int updationTime = 200;

    @Replicate
    public float lastUpdateTime = -1.0f;

    @Replicate
    public float launchedTime = -1.0f;

    @Override
    public void copyFrom(HomingComponent other) {
        this.target = other.target;
        this.initialSleepTime = other.initialSleepTime;
        this.updationTime = other.updationTime;
        this.lastUpdateTime = other.lastUpdateTime;
        this.launchedTime = other.launchedTime;
    }
}
