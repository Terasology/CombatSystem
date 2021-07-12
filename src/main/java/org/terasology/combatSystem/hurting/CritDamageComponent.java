// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.combatSystem.hurting;

import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;

/**
 * Damages an entity critically.
 * <p>
 * Damaging critically means multiplying the original damage by <b>critFactor</b>.
 * The critical damage has <b>critChance</b>/100 probability of occurring.
 */
public class CritDamageComponent implements Component<CritDamageComponent> {

    @Replicate
    public int critChance = 10;                 // in percentage out of 100

    @Replicate
    public float critFactor = 2;                // factor to multiply damage with

    @Override
    public void copy(CritDamageComponent other) {
        this.critChance = other.critChance;
        this.critFactor = other.critFactor;
    }
}
