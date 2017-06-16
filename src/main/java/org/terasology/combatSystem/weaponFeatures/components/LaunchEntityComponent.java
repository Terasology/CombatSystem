package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.prefab.Prefab;

/**
 * makes the entity launch another entity.
 * <p>
 * the other entity may be defined in <b>launchEntityPrefab</b> or <b>launchEntity</b>.
 * <b>launchEntity</b> is given precedence over <b>launchEntityPrefab</b> if both are present.
 */
public class LaunchEntityComponent extends AttackComponent{
    public Prefab launchEntityPrefab;
    public EntityRef launchEntity;
    public float impulse = 300.0f;
}
