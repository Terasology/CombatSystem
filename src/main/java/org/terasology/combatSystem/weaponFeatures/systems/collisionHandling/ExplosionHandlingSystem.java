// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.weaponFeatures.systems.collisionHandling;

import org.joml.Vector3f;
import org.terasology.combatSystem.hurting.HurtEvent;
import org.terasology.combatSystem.hurting.HurtingComponent;
import org.terasology.combatSystem.weaponFeatures.components.ExplosionComponent;
import org.terasology.combatSystem.weaponFeatures.events.ExplosionEvent;
import org.terasology.engine.core.Time;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.EventPriority;
import org.terasology.engine.entitySystem.event.Priority;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.engine.logic.health.DestroyEvent;
import org.terasology.engine.logic.health.EngineDamageTypes;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.engine.physics.Physics;
import org.terasology.engine.registry.In;
import org.terasology.joml.geom.AABBf;
import org.terasology.module.health.components.HealthComponent;

import java.util.List;

@RegisterSystem
public class ExplosionHandlingSystem extends BaseComponentSystem implements UpdateSubscriberSystem {
    @In
    private Time time;
    @In
    private EntityManager entityManager;
    @In
    private Physics physics;

    @ReceiveEvent(components = ExplosionComponent.class)
    public void explosion(ExplosionEvent event, EntityRef entity) {
        ExplosionComponent explosion = entity.getComponent(ExplosionComponent.class);
        if (explosion == null) {
            return;
        }

        explosion.explosionStartTime = time.getGameTime();

        if (explosion.explosionDelayTime > 0.0f) {
            return;
        }

        explosion.explosionStarted = true;

        entity.saveComponent(explosion);

        doExplosion(entity);
    }

    @Priority(EventPriority.PRIORITY_HIGH)
    @ReceiveEvent(components = ExplosionComponent.class)
    public void explosionOnDestroy(DestroyEvent event, EntityRef entity) {
        ExplosionComponent explosion = entity.getComponent(ExplosionComponent.class);
        if (explosion.explosionStarted) {
            return;
        }

        explosion.explosionStartTime = time.getGameTime();

        explosion.explosionStarted = true;

        entity.saveComponent(explosion);

        doExplosion(entity);
    }

    @Override
    public void update(float delta) {
        // TODO Auto-generated method stub
        Iterable<EntityRef> entityList = entityManager.getEntitiesWith(ExplosionComponent.class);
        for (EntityRef entity : entityList) {
            ExplosionComponent explosion = entity.getComponent(ExplosionComponent.class);
            if (explosion.explosionStartTime < 0.0f) {
                continue;
            }
            float currentGameTime = time.getGameTime();

            // starts a delayed explosion;
            if (!explosion.explosionStarted) {
                if (currentGameTime >= (explosion.explosionStartTime + explosion.explosionDelayTime)) {
                    explosion.explosionStarted = true;

                    entity.saveComponent(explosion);

                    doExplosion(entity);
                }
            }
        }
    }

    //--------------------private methods-------------------------

    //creates a Trigger and collider for exlosion entity.
    private void doExplosion(EntityRef entity) {
        ExplosionComponent explosion = entity.getComponent(ExplosionComponent.class);
        LocationComponent location = entity.getComponent(LocationComponent.class);
        if (explosion == null || location == null) {
            return;
        }

        if (entity.hasComponent(HealthComponent.class)) {
            entity.removeComponent(HealthComponent.class);
        }

        Vector3f pos = location.getWorldPosition(new Vector3f());
        List<EntityRef> broadPhaseEntities = physics.scanArea(new AABBf(
            pos.x - explosion.radius,
            pos.y - explosion.radius,
            pos.z - explosion.radius,
            pos.x + explosion.radius,
            pos.y + explosion.radius,
            pos.z + explosion.radius), explosion.collidesWith);

        for (EntityRef otherEntity : broadPhaseEntities) {
            if (otherEntity.getId() == entity.getId()) {
                continue;
            }
            LocationComponent otherEntityLocation = otherEntity.getComponent(LocationComponent.class);
            if (otherEntityLocation == null) {
                continue;
            }

            float distanceSq = pos.distanceSquared(otherEntityLocation.getWorldPosition(new Vector3f()));
            float radiusSquared = explosion.radius * explosion.radius;
            float explosionFactor = 1 - (distanceSq / radiusSquared);
            if (explosionFactor < 0) {
                explosionFactor = 0;
            }

            if (distanceSq <= radiusSquared) {
                HurtingComponent hurting = entity.getComponent(HurtingComponent.class);
                if (hurting == null) {
                    return;
                }
                hurting.amount = (int) (hurting.amount * explosionFactor);
                hurting.damageType = EngineDamageTypes.EXPLOSIVE.get();
                entity.saveComponent(hurting);
                entity.send(new HurtEvent(otherEntity));
            }
        }
    }

}
