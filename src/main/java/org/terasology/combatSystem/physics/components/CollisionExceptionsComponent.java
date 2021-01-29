// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.physics.components;

import com.google.common.collect.Lists;
import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.network.Replicate;

import java.util.List;

/**
 * specifies the <b>Entities</b> that will not be collided with.
 * <p>
 * An {@link EntityRef} with this {@link Component} will not collide with <b>exceptions</b>. 
 */
public class CollisionExceptionsComponent implements Component{
    @Replicate
    public List<EntityRef> exceptions = Lists.<EntityRef>newArrayList();

}
