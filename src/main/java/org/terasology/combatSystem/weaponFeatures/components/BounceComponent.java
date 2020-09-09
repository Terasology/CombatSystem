// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.network.Replicate;

public class BounceComponent implements Component {
    @Replicate
    public float bounceFactor = 0.5f;

    @Replicate
    public int maxPierceAngle = 10;                        // in degrees 

    @Replicate
    public float minPierceVelocity = 0.0f;

    @Replicate
    public float minBounceVelocity = 3.0f;

}
