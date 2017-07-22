package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.network.Replicate;

public class WorldAvatarComponent implements Component{
    @Replicate
    public Prefab worldAvatarPrefab;

}
