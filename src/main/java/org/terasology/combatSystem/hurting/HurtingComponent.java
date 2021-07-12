package org.terasology.combatSystem.hurting;

import org.terasology.engine.entitySystem.prefab.Prefab;
import org.terasology.engine.logic.health.EngineDamageTypes;
import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;

/**
 * Specifies the amount and damage type that an entity will inflict on other entities.
 * <p>
 * {@link HurtEvent} is used to hurt other entity/entities.
 */
public class HurtingComponent implements Component<HurtingComponent> {
    /**
     * The amount of damage the entity will inflict.
     */
    @Replicate
    public int amount = 3;

    /**
     * The type of damage.
     */
    @Replicate
    public Prefab damageType = EngineDamageTypes.DIRECT.get();

    @Override
    public void copy(HurtingComponent other) {
        this.amount = other.amount;
        this.damageType = other.damageType;
    }
}
