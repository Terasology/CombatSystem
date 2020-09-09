// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.weaponFeatures.events;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.Event;
import org.terasology.math.geom.Vector3f;

public class BounceEvent implements Event {
    EntityRef target = EntityRef.NULL;
    Vector3f normal;

    public BounceEvent(EntityRef entity, Vector3f normal) {
        target = entity;
        this.normal = normal;
    }

    public EntityRef getTarget() {
        return target;
    }

    public Vector3f getNormal() {
        return normal;
    }

}
