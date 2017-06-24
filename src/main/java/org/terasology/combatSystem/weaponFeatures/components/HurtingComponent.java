package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.logic.health.EngineDamageTypes;
import org.terasology.network.Replicate;

public class HurtingComponent implements Component{
    @Replicate
    public Prefab damageType = EngineDamageTypes.DIRECT.get();
    
    @Replicate
    public int amount = 3;
    
    @Replicate
    public boolean canHurt = true;

}
