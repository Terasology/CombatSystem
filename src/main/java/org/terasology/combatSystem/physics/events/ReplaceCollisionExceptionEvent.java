// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.physics.events;

import com.google.common.collect.Lists;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.gestalt.entitysystem.event.Event;

import java.util.List;

/**
 * used to replace all the exceptions with ones in the event's list.
 * <p>
 * You can also say that it removes all previous exceptions and adds these as the new ones.
 */
public class ReplaceCollisionExceptionEvent implements Event {
    List<EntityRef> exceptions;
    
    public ReplaceCollisionExceptionEvent() {
        
    }
    
    public ReplaceCollisionExceptionEvent(List<EntityRef> exceptions) {
        this.exceptions = exceptions;
    }
    
    public ReplaceCollisionExceptionEvent(EntityRef entity) {
        exceptions = Lists.<EntityRef>newArrayList();
        exceptions.add(entity);
    }
    
    public List<EntityRef> getCollisionExceptionsList() {
        return exceptions;
    }
}
