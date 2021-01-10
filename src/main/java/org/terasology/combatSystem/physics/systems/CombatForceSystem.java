package org.terasology.combatSystem.physics.systems;

import org.joml.Vector3f;
import org.terasology.combatSystem.physics.components.MassComponent;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.registry.In;

/**
 * handles the forces applied to an entity with {@code MassComponent}
 * <p>
 * It does not handle <b>gravity</b> and <b>friction</b>
 */
@RegisterSystem
public class CombatForceSystem extends BaseComponentSystem implements UpdateSubscriberSystem {

    @In
    EntityManager entityManager;

    /**
     * Converts the total force applied to an entity into acceleration. Adds the acceleration into total acceleration.
     */
    @Override
    public void update(float delta) {
        // TODO Auto-generated method stub
        Iterable<EntityRef> entitiesWith = entityManager.getEntitiesWith(MassComponent.class);
        for (EntityRef entity : entitiesWith) {
            MassComponent body = entity.getComponent(MassComponent.class);
            Vector3f force = new Vector3f(body.force);
            force.div(body.mass);

            body.acceleration.add(force);

            entity.saveComponent(body);
        }
    }
}
