// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.combatSystem.weaponFeatures.systems;

import org.terasology.combatSystem.weaponFeatures.components.LegacyShootComponent;
import org.terasology.engine.core.Time;
import org.terasology.engine.entitySystem.entity.EntityBuilder;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.common.ActivateEvent;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.engine.physics.CollisionGroup;
import org.terasology.engine.physics.HitResult;
import org.terasology.engine.physics.Physics;
import org.terasology.engine.physics.StandardCollisionGroup;
import org.terasology.engine.registry.In;
import org.terasology.engine.world.BlockEntityRegistry;
import org.terasology.engine.world.WorldProvider;
import org.terasology.engine.world.block.Block;
import org.terasology.health.logic.event.DoDamageEvent;
import org.terasology.math.geom.Vector3f;
import org.terasology.math.geom.Vector3i;

@RegisterSystem(RegisterMode.AUTHORITY)
public class LegacyShootSystem extends BaseComponentSystem {
    private final CollisionGroup filter = StandardCollisionGroup.ALL;
    @In
    private WorldProvider worldProvider;
    @In
    private Physics physicsRenderer;
    @In
    private BlockEntityRegistry blockEntityRegistry;
    @In
    private EntityManager entityManager;
    @In
    private Time time;
    private float lastTime;

    @Override
    public void initialise() {
    }

    @Override
    public void shutdown() {
    }

    @ReceiveEvent
    public void onActivate(ActivateEvent event, EntityRef entity, LegacyShootComponent arrowActionComponent) {

        if (time.getGameTime() > lastTime + 1.0f / arrowActionComponent.arrowsPerSecond) {
            Vector3f target = event.getHitNormal();
            Vector3i blockPos = new Vector3i(target);

            Vector3f position = new Vector3f(event.getOrigin());
            Vector3f dir = new Vector3f(event.getDirection());

            HitResult result;
            result = physicsRenderer.rayTrace(position, dir, arrowActionComponent.maxDistance, filter);

            Block currentBlock = worldProvider.getBlock(blockPos);

            if (currentBlock.isDestructible()) {
                EntityBuilder builder = entityManager.newBuilder("CoreAssets:defaultBlockParticles");
                builder.getComponent(LocationComponent.class).setWorldPosition(target);
                builder.build();
            }
            EntityRef blockEntity = result.getEntity();
            blockEntity.send(new DoDamageEvent(arrowActionComponent.damageAmount, arrowActionComponent.damageType));
            lastTime = time.getGameTime();
        }
    }
}
