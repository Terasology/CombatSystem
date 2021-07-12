// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.engine.entitySystem.prefab.Prefab;
import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;

/**
 *  to be used specifically for items
 */
public class WorldAvatarComponent implements Component<WorldAvatarComponent> {
    @Replicate
    public Prefab worldAvatarPrefab;

    @Override
    public void copy(WorldAvatarComponent other) {
        this.worldAvatarPrefab = other.worldAvatarPrefab;
    }
}
