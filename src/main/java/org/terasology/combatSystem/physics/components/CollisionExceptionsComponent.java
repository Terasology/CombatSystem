// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.physics.components;

import com.google.common.collect.Lists;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;

import java.util.List;

/**
 * specifies the <b>Entities</b> that will not be collided with.
 * <p>
 * An {@link EntityRef} with this {@link Component} will not collide with <b>exceptions</b>.
 */
public class CollisionExceptionsComponent implements Component<CollisionExceptionsComponent> {
    @Replicate
    public List<EntityRef> exceptions = Lists.<EntityRef>newArrayList();

    @Override
    public void copyFrom(CollisionExceptionsComponent other) {
        this.exceptions = Lists.newArrayList(other.exceptions);
    }
}
