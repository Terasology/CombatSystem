package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.prefab.Prefab;

public class LaunchEntityComponent implements Component{
    public Prefab launchEntityPrefab;
    public float impulse = 10.0f;
}
