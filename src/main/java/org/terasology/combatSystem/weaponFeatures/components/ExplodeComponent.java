package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.prefab.Prefab;

public class ExplodeComponent implements Component{
    public Prefab explosionPrefab;
    public EntityRef explosionEntity;
}
