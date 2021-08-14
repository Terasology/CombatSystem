// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.hurting;

import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;

/**
 * To generate a random damage amount between <b>minDamage</b> and <b>maxDamage</b> (both inclusive).
 */
public class RandomDamageComponent implements Component<RandomDamageComponent> {
    public RandomDamageComponent() {

    }

    //inclusive
    @Replicate
    public int minDamage = 2;

    //inclusive
    @Replicate
    public int maxDamage = 4;

    @Override
    public void copyFrom(RandomDamageComponent other) {
        this.minDamage = other.minDamage;
        this.maxDamage = other.maxDamage;
    }
}
