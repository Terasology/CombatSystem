// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.engine.entitySystem.prefab.Prefab;
import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;

/**
 * makes the entity explode into another entity.
 * <p>
 * the other entity may be defined in <b>explosionPrefab</b> or <b>explosionEntity</b>.
 * <b>explosionEntity</b> is given precedence over <b>explosionPrefab</b> if both are present.
 */
public class ExplodeComponent implements Component<ExplodeComponent> {
    @Replicate
    public Prefab explosionPrefab;

    @Override
    public void copy(ExplodeComponent other) {
        this.explosionPrefab = other.explosionPrefab;
    }
}
