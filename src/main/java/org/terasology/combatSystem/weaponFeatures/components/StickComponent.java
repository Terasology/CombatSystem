// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;

public class StickComponent implements Component<StickComponent> {
    @Replicate
    public float stickTime = -1;

    @Replicate
    public float totalStickingTime = -1;

    @Replicate
    public float pierceAmount = 1.0f;

    @Replicate
    public EntityRef target = EntityRef.NULL;

    public void setTarget(EntityRef entity) {
        target = entity;
    }

    public EntityRef getTarget() {
        return target;
    }

    @Override
    public void copyFrom(StickComponent other) {
        this.stickTime = other.stickTime;
        this.totalStickingTime = other.totalStickingTime;
        this.pierceAmount = other.pierceAmount;
        this.target = other.target;
    }
}
