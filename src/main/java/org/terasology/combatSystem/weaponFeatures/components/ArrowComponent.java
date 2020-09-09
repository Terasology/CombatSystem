// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.network.Replicate;
import org.terasology.math.geom.Vector3f;

/**
 * the entity would traverse like an arrow.
 * <p>
 * The entity would always be facing tangent to its path.
 */
public class ArrowComponent implements Component {
    @Replicate
    public Vector3f previousDir = new Vector3f();

}
