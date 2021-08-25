// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.combatSystem.traps.components;

import org.terasology.engine.entitySystem.prefab.Prefab;
import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;

public class BaseLauncherComponent implements Component<BaseLauncherComponent> {
    @Replicate
    public Prefab entityInBase;

    @Replicate
    public int amountOfEntityInBase;

    @Replicate
    public int coolDownTime = 1000;

    @Replicate
    public float lastLaunchTime = -1.0f;

    @Override
    public void copyFrom(BaseLauncherComponent other) {
        this.entityInBase = other.entityInBase;
        this.amountOfEntityInBase = other.amountOfEntityInBase;
        this.coolDownTime = other.coolDownTime;
        this.lastLaunchTime = other.lastLaunchTime;
    }
}
