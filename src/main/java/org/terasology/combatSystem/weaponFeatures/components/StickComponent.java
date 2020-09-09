// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.network.Replicate;

public class StickComponent implements Component {
    @Replicate
    public float stickTime = -1;
    @Replicate
    public float totalStickingTime = -1;
    @Replicate
    public float pierceAmount = 1.0f;
    @Replicate
    EntityRef target = EntityRef.NULL;

    public EntityRef getTarget() {
        return target;
    }

    public void setTarget(EntityRef entity) {
        target = entity;
    }

}
