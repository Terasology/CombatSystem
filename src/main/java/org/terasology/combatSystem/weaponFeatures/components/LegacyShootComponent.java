// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.entitySystem.prefab.Prefab;
import org.terasology.engine.logic.destruction.EngineDamageTypes;

public class LegacyShootComponent implements Component {

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

}
