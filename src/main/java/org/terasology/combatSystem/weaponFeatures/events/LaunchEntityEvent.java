package org.terasology.combatSystem.weaponFeatures.events;

import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.terasology.engine.entitySystem.event.Event;

public class LaunchEntityEvent implements Event {
    private Vector3f direction = new Vector3f();

    public LaunchEntityEvent(Vector3fc dir) {
        direction.set(dir);
    }

    public Vector3fc getDirection() {
        return direction;
    }
}
