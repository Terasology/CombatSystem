// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.network.Replicate;

public class MeleeComponent implements Component {

    @Replicate
    public float range = 2.0f;

    @Replicate(initialOnly = true)
    public boolean primaryAttack = false;
}
