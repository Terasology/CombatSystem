// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.traps.systems;

import org.terasology.combatSystem.physics.components.GravityComponent;
import org.terasology.combatSystem.physics.components.MassComponent;
import org.terasology.combatSystem.weaponFeatures.components.LaunchEntityComponent;
import org.terasology.combatSystem.weaponFeatures.events.LaunchEntityEvent;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.math.geom.Vector3f;
import org.terasology.sensors.EntitySensedEvent;

@RegisterSystem
public class LaunchEntityTrapHandlingSystem extends BaseComponentSystem {

    @ReceiveEvent(components = LaunchEntityComponent.class)
    public void launchEntity(EntitySensedEvent event, EntityRef entity) {
        EntityRef target = event.getEntity();
        LocationComponent loc = entity.getComponent(LocationComponent.class);
        if (loc == null) {
            return;
        }

        Vector3f pos = loc.getWorldPosition();
        Vector3f targetPos = predictMotion(target);
        if (targetPos == null) {
            return;
        }

        Vector3f dir = targetPos.sub(pos);
        dir.normalize();

        entity.send(new LaunchEntityEvent(dir));
    }

    //--------------------------------------private methods---------------------

    private Vector3f predictMotion(EntityRef target) {
        LocationComponent loc = target.getComponent(LocationComponent.class);
        if (loc == null) {
            return null;
        }
        Vector3f pos = loc.getWorldPosition();

        MassComponent mass = target.getComponent(MassComponent.class);
        if (mass == null) {
            return pos;
        }

        Vector3f vel = new Vector3f(mass.velocity);
        Vector3f accel = new Vector3f(mass.acceleration);

        GravityComponent gravity = target.getComponent(GravityComponent.class);
        if (gravity != null) {
            accel.add(gravity.gravityAccel);
        }

        vel.add(accel.scale(1.0f / 60.0f));
        pos.add(vel.scale(1.0f / 60.0f));

        return pos;
    }
}
