// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.physics.events;

import com.google.common.collect.Lists;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.Event;

import java.util.List;

/**
 * Used to remove entities that are added as exceptions to re-enable collisions with them.
 */
public class RemoveCollisionExceptionEvent implements Event {
    List<EntityRef> exceptions;

    public RemoveCollisionExceptionEvent() {

    }

    public RemoveCollisionExceptionEvent(List<EntityRef> exceptions) {
        this.exceptions = exceptions;
    }

    public RemoveCollisionExceptionEvent(EntityRef entity) {
        exceptions = Lists.newArrayList();
        exceptions.add(entity);
    }

    public List<EntityRef> getCollisionExceptionsList() {
        return exceptions;
    }

}
