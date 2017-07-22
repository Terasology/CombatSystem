package org.terasology.combatSystem.hurting;

import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.logic.health.EngineDamageTypes;
import org.terasology.network.Replicate;

public class HurtingComponent implements Component{
    
    @Replicate
    public int amount = 3;
    
    @Replicate
    public Prefab damageType = EngineDamageTypes.DIRECT.get();

}
