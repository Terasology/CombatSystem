// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.traps.systems;

import org.terasology.combatSystem.weaponFeatures.components.ExplodeComponent;
import org.terasology.combatSystem.weaponFeatures.events.ExplodeEvent;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.sensors.DeactivateSensorEvent;
import org.terasology.sensors.EntitySensedEvent;

@RegisterSystem
public class ExplodingTrapHandlingSystem extends BaseComponentSystem {
    
    @ReceiveEvent(components = ExplodeComponent.class)
    public void explodeOnSense(EntitySensedEvent event, EntityRef entity) {
        entity.send(new DeactivateSensorEvent());
        entity.send(new ExplodeEvent());
    }

}
