package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.network.Replicate;

/**
 * makes the entity launch another entity.
 * <p>
 * the other entity may be defined in <b>launchEntityPrefab</b> or <b>launchEntity</b>.
 * <b>launchEntity</b> is given precedence over <b>launchEntityPrefab</b> if both are present.
 */
public class LaunchEntityComponent implements Component{
    @Replicate
    public Prefab launchEntityPrefab;
    
    @Replicate
    public int cooldownTime = 200;
    
    @Replicate
    public float impulse = 300.0f;
    
    @Replicate(initialOnly = true)
    public boolean primaryAttack = true;
}
