package org.terasology.combatSystem.hurting;

import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.logic.health.EngineDamageTypes;
import org.terasology.network.Replicate;

/**
 * Specifies the amount and damage type that an entity will inflict on other entities.
 * <p>
 * {@link HurtEvent} is used to hurt other entity/entities.
 */
public class HurtingComponent implements Component{
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

}
