// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem;

import org.terasology.engine.entitySystem.entity.EntityRef;

/**
 * Specifies whether the owner of an entity can be collided with or not.
 */
public enum OwnerCollisionState {
    /**
     * Does not collide with any owner.
     */
    DISABLED,
    /**
     * Collides with every owner.
     */
    ENABLED,
    /**
     * Collides with just the direct owner.
     * <p>
     * <b>Direct Owner</b> is the {@link EntityRef} in {@link ShooterComponent} of the entity.
     */
    DISABLED_WITH_DIRECT_OWNER,
    /**
     * Collides with just the ultimate owner.
     * <p>
     * <b>Ultimate Owner</b> is the owner of the owner until there are no owners.
     * Simply put it may be the owner of {@code EntityRef} in {@code ShooterComponent}.
     */
    DISABLED_WITH_ULTIMATE_OWNER

}
