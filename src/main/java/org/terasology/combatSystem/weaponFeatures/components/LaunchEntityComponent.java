package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.prefab.Prefab;

public class LaunchEntityComponent implements Component{
    public Prefab launchEntityPrefab;
    public EntityRef launchEntity;
    public float impulse = 300.0f;
    public boolean primaryAttack = true;
}
