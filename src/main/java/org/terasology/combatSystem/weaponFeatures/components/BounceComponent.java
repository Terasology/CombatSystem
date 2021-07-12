// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;

public class BounceComponent implements Component<BounceComponent> {
    @Replicate
    public float bounceFactor = 0.5f;

    @Replicate
    public int maxPierceAngle = 10;                        // in degrees

    @Replicate
    public float minPierceVelocity = 0.0f;

    @Replicate
    public float minBounceVelocity = 3.0f;

    @Override
    public void copy(BounceComponent other) {
        this.bounceFactor = other.bounceFactor;
        this.maxPierceAngle = other.maxPierceAngle;
        this.minPierceVelocity = other.minPierceVelocity;
        this.minBounceVelocity = other.minBounceVelocity;
    }
}
