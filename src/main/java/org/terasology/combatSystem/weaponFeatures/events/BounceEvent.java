package org.terasology.combatSystem.weaponFeatures.events;

import org.joml.Vector3f;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.Event;

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
