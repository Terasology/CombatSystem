// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.physics.systems;

import org.terasology.combatSystem.physics.components.GravityComponent;
import org.terasology.combatSystem.physics.components.MassComponent;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.engine.registry.In;

import java.util.Iterator;

/**
 * Handles the gravity applied to an entity with {@link MassComponent}
 */
@RegisterSystem
public class GravitySystem extends BaseComponentSystem implements UpdateSubscriberSystem {

    @In
    EntityManager entityManager;

    /**
     * Adds the acceleration due to gravity into total acceleration of entity.
     *
     * @param delta Not used.
     */
    @Override
    public void update(float delta) {
        // TODO Auto-generated method stub
        Iterable<EntityRef> entitiesWith = entityManager.getEntitiesWith(MassComponent.class);
        Iterator<EntityRef> entities = entitiesWith.iterator();
        while (entities.hasNext()) {
            EntityRef entity = entities.next();
            GravityComponent gravity = entity.getComponent(GravityComponent.class);
            MassComponent body = entity.getComponent(MassComponent.class);

            // Adds gravity into acceleration

            if (gravity != null) {
                body.acceleration.add(gravity.gravityAccel);
            }
        }
    }

}
