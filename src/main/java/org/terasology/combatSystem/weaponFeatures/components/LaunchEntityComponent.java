// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.engine.entitySystem.prefab.Prefab;
import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;

/**
 * makes the entity launch another entity.
 * <p>
 * the other entity may be defined in <b>launchEntityPrefab</b> or <b>launchEntity</b>.
 * <b>launchEntity</b> is given precedence over <b>launchEntityPrefab</b> if both are present.
 */
public class LaunchEntityComponent implements Component<LaunchEntityComponent> {
    @Replicate
    public Prefab launchEntityPrefab;

    @Replicate
    public int cooldownTime = 200;

    @Replicate
    public float launchTime = -1.0f;

    @Replicate
    public float impulse = 300.0f;

    @Replicate(initialOnly = true)
    public boolean primaryAttack = true;

    @Override
    public void copyFrom(LaunchEntityComponent other) {
        this.launchEntityPrefab = other.launchEntityPrefab;
        this.cooldownTime = other.cooldownTime;
        this.launchTime = other.launchTime;
        this.impulse = other.impulse;
        this.primaryAttack = other.primaryAttack;
    }
}
