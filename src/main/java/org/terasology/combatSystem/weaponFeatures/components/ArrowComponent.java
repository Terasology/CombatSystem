// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.combatSystem.weaponFeatures.components;

import org.joml.Vector3f;
import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;

/**
 * the entity would traverse like an arrow.
 * <p>
 * The entity would always be facing tangent to its path.
 */
public class ArrowComponent implements Component<ArrowComponent> {
    @Replicate
    public Vector3f previousDir = new Vector3f();

    @Override
    public void copy(ArrowComponent other) {
        this.previousDir.set(other.previousDir);
    }
}
