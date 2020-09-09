// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.weaponFeatures.events;

import org.terasology.engine.entitySystem.event.Event;
import org.terasology.math.geom.Vector3f;

public class LaunchEntityEvent implements Event {
    Vector3f direction;

    public LaunchEntityEvent(Vector3f dir) {
        direction = dir;
    }

    public Vector3f getDirection() {
        return direction;
    }

}
