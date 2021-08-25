// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;

public class MeleeComponent implements Component<MeleeComponent> {

    @Replicate
    public float range = 2.0f;

    @Replicate(initialOnly = true)
    public boolean primaryAttack = false;

    @Override
    public void copyFrom(MeleeComponent other) {
        this.range = other.range;
        this.primaryAttack = other.primaryAttack;
    }
}
