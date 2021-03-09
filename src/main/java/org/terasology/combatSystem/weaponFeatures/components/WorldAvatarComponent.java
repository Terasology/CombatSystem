package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.entitySystem.prefab.Prefab;
import org.terasology.engine.network.Replicate;

/**
 *  to be used specifically for items
 */
public class WorldAvatarComponent implements Component{
    @Replicate
    public Prefab worldAvatarPrefab;

}
