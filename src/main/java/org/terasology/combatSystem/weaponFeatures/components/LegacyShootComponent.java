// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.engine.entitySystem.prefab.Prefab;
import org.terasology.engine.logic.health.EngineDamageTypes;
import org.terasology.gestalt.entitysystem.component.Component;

public class LegacyShootComponent implements Component<LegacyShootComponent> {

    /**
     * The max distance the arrow will fly.
     */
    public int maxDistance = 24;

    /**
     * The damage the arrow does
     */
    public int damageAmount = 3;

    /**
     * How many arrows can be fired per second
     */
    public float arrowsPerSecond = 1.0f;

    public Prefab damageType = EngineDamageTypes.PHYSICAL.get();

    @Override
    public void copyFrom(LegacyShootComponent other) {
        this.maxDistance = other.maxDistance;
        this.damageAmount = other.damageAmount;
        this.arrowsPerSecond = other.arrowsPerSecond;
        this.damageType = other.damageType;
    }
}
