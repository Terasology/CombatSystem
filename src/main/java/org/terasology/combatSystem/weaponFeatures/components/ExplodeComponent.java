package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.network.Replicate;

/**
 * makes the entity explode into another entity.
 * <p>
 * the other entity may be defined in <b>explosionPrefab</b> or <b>explosionEntity</b>.
 * <b>explosionEntity</b> is given precedence over <b>explosionPrefab</b> if both are present.
 */
public class ExplodeComponent implements Component{
    @Replicate
    public Prefab explosionPrefab;
    
    @Replicate
    public int amount = 3;
}
