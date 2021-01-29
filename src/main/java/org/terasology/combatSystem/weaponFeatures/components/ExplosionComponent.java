// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.weaponFeatures.components;

import com.google.common.collect.Lists;
import org.terasology.entitySystem.Component;
import org.terasology.network.Replicate;
import org.terasology.physics.CollisionGroup;
import org.terasology.physics.StandardCollisionGroup;

import java.util.List;

public class ExplosionComponent implements Component{
    @Replicate
    public List<CollisionGroup> collidesWith = Lists.<CollisionGroup>newArrayList(StandardCollisionGroup.DEFAULT, StandardCollisionGroup.WORLD, StandardCollisionGroup.CHARACTER);
    
    @Replicate
    public float radius = 2.0f;
    
    @Replicate
    public float explosionStartTime = -1.0f;                 //sec
    @Replicate
    public float explosionDelayTime = 0.0f;                 //sec
    
    @Replicate
    public boolean explosionStarted = false;

}
