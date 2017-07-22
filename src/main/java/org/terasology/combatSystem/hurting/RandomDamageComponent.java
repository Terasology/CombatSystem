package org.terasology.combatSystem.hurting;

import org.terasology.entitySystem.Component;
import org.terasology.network.Replicate;

public class RandomDamageComponent implements Component{
    //inclusive
    @Replicate
    public int minDamage = 2;
    
    //inclusive
    @Replicate
    public int maxDamage = 4;

}
