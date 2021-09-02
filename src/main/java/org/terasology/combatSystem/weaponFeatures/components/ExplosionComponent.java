// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.combatSystem.weaponFeatures.components;

import com.google.common.collect.Lists;
import org.terasology.engine.network.Replicate;
import org.terasology.engine.physics.CollisionGroup;
import org.terasology.engine.physics.StandardCollisionGroup;
import org.terasology.gestalt.entitysystem.component.Component;

import java.util.List;

public class ExplosionComponent implements Component<ExplosionComponent> {
    @Replicate
    public List<CollisionGroup> collidesWith = Lists.<CollisionGroup>newArrayList(
        StandardCollisionGroup.DEFAULT, 
        StandardCollisionGroup.WORLD, 
        StandardCollisionGroup.CHARACTER);

    @Replicate
    public float radius = 2.0f;

    @Replicate
    public float explosionStartTime = -1.0f;                 //sec
    @Replicate
    public float explosionDelayTime = 0.0f;                 //sec

    @Replicate
    public boolean explosionStarted = false;

    @Override
    public void copyFrom(ExplosionComponent other) {
        this.collidesWith = Lists.newArrayList(other.collidesWith);
        this.radius = other.radius;
        this.explosionStartTime = other.explosionStartTime;
        this.explosionDelayTime = other.explosionDelayTime;
        this.explosionStarted = other.explosionStarted;
    }
}
