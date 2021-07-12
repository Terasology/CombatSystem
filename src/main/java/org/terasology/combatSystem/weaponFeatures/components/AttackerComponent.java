// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.combatSystem.OwnerCollisionState;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;

public class AttackerComponent implements Component<AttackerComponent> {
    @Replicate
    public EntityRef attacker = EntityRef.NULL;

    @Replicate
    public OwnerCollisionState state = OwnerCollisionState.DISABLED;

    public AttackerComponent() {

    }

    public AttackerComponent(EntityRef shooter) {
        this(shooter, null);
    }

    public AttackerComponent(OwnerCollisionState state) {
        this(null, state);
    }

    public AttackerComponent(EntityRef shooter, OwnerCollisionState state) {
        if (shooter != null) {
            this.attacker = shooter;
        }
        if (state != null) {
            this.state = state;
        }
    }

    @Override
    public void copy(AttackerComponent other) {
        this.attacker = other.attacker;
        this.state = other.state;
    }
}
