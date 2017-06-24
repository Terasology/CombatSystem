package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.logic.health.EngineDamageTypes;

public class HurtingComponent implements Component{
    public Prefab damageType = EngineDamageTypes.DIRECT.get();
    public int amount;
    public boolean canHurt = true;

}
