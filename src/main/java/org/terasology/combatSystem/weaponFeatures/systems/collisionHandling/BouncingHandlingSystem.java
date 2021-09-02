// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.weaponFeatures.systems.collisionHandling;

import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.terasology.combatSystem.hurting.HurtEvent;
import org.terasology.combatSystem.physics.components.MassComponent;
import org.terasology.combatSystem.physics.events.ReplaceCollisionExceptionEvent;
import org.terasology.combatSystem.weaponFeatures.components.BounceComponent;
import org.terasology.combatSystem.weaponFeatures.components.StickComponent;
import org.terasology.combatSystem.weaponFeatures.events.BounceEvent;
import org.terasology.combatSystem.weaponFeatures.events.StickEvent;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.health.DestroyEvent;
import org.terasology.engine.logic.health.EngineDamageTypes;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.engine.physics.events.CollideEvent;

@RegisterSystem
public class BouncingHandlingSystem extends BaseComponentSystem {

    @ReceiveEvent(components = BounceComponent.class)
    public void bouncingCollision(CollideEvent event, EntityRef entity) {
        bounce(entity, event.getOtherEntity(), event.getNormal());

        event.consume();
    }

    @ReceiveEvent(components = BounceComponent.class)
    public void bouncing(BounceEvent event, EntityRef entity) {
        bounce(entity, event.getTarget(), event.getNormal());
    }

    //----------------------------private methods----------------------------------

    // bounce entity
    private void bounce(EntityRef entity, EntityRef target, Vector3fc normal) {
        MassComponent mass = entity.getComponent(MassComponent.class);
        BounceComponent bounce = entity.getComponent(BounceComponent.class);
        LocationComponent location = entity.getComponent(LocationComponent.class);
        if (mass == null || location == null || bounce == null) {
            return;
        }

        // check peircing
        if (checkPiercing(normal, mass.velocity, bounce.maxPierceAngle, bounce.minPierceVelocity)) {
            entity.addOrSaveComponent(new StickComponent());
            entity.removeComponent(BounceComponent.class);
            entity.send(new StickEvent(target));
            return;
        }

        // check if the velocity is not enough for another bounce. Destroy if true else
        // bounce the arrow.
        if (mass.velocity.lengthSquared() <= (bounce.minBounceVelocity * bounce.minBounceVelocity)) {
            entity.send(new DestroyEvent(EntityRef.NULL, EntityRef.NULL, EngineDamageTypes.DIRECT.get()));
        } else {
            Vector3f bounceDir = new Vector3f(normal);
            bounceDir.normalize();
            bounceDir.negate();
            bounceDir.mul(2 * bounceDir.dot(mass.velocity));
            bounceDir.sub(mass.velocity);
            bounceDir.mul(bounce.bounceFactor);
            bounceDir.negate();

            mass.velocity.set(bounceDir);
            entity.saveComponent(mass);

            entity.send(new ReplaceCollisionExceptionEvent(target));
        }

        //-------------------------repetitive code for every HurtingComponent-----------

        // damage the other entity
        entity.send(new HurtEvent(target));
    }

    private boolean checkPiercing(Vector3fc normal, Vector3f velocity, int maxAngle, float minVelocity) {
        Vector3f vel = new Vector3f(velocity);

        int angle = (int) Math.toDegrees(vel.angle(normal));
        if (angle > 90) {
            angle = 180 - angle;
        }
        if (angle <= maxAngle && velocity.lengthSquared() >= (minVelocity * minVelocity)) {
            return true;
        }
        return false;
    }
}
