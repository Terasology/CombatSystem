// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.traps.systems;

import org.terasology.combatSystem.traps.components.SwitchComponent;
import org.terasology.combatSystem.weaponFeatures.components.ParentComponent;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.OnActivatedComponent;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.OnChangedComponent;
import org.terasology.engine.entitySystem.event.EventPriority;
import org.terasology.engine.entitySystem.event.Priority;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.health.DestroyEvent;
import org.terasology.engine.logic.health.EngineDamageTypes;
import org.terasology.gestalt.entitysystem.event.ReceiveEvent;

@RegisterSystem
public class SwitchDoorHandlingSystem extends BaseComponentSystem {

    @ReceiveEvent(components = ParentComponent.class)
    public void openDoor(OnActivatedComponent event, EntityRef entity) {
        SwitchComponent switchComp = entity.getComponent(SwitchComponent.class);
        if (switchComp == null) {
            return;
        }

        for (EntityRef door : switchComp.doors) {
            if (door == null || door == EntityRef.NULL || !door.exists()) {
                continue;
            }
            door.send(new DestroyEvent(entity, entity, EngineDamageTypes.DIRECT.get()));
        }
    }

    @ReceiveEvent(components = ParentComponent.class)
    public void openDoor(OnChangedComponent event, EntityRef entity) {
        SwitchComponent switchComp = entity.getComponent(SwitchComponent.class);
        if (switchComp == null) {
            return;
        }

        for (EntityRef door : switchComp.doors) {
            if (door == null || door == EntityRef.NULL || !door.exists()) {
                continue;
            }
            door.send(new DestroyEvent(entity, entity, EngineDamageTypes.DIRECT.get()));
        }
    }

    @Priority(EventPriority.PRIORITY_HIGH)
    @ReceiveEvent(components = SwitchComponent.class)
    public void openDoor(DestroyEvent event, EntityRef entity) {
        SwitchComponent switchComp = entity.getComponent(SwitchComponent.class);

        for (EntityRef door : switchComp.doors) {
            if (door == null || door == EntityRef.NULL || !door.exists()) {
                continue;
            }
            door.send(new DestroyEvent(entity, entity, EngineDamageTypes.DIRECT.get()));
        }
    }
}
