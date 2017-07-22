package org.terasology.combatSystem.hurting;

import org.terasology.entitySystem.Component;
import org.terasology.network.Replicate;

public class CritDamageComponent implements Component{
    
    @Replicate
    public int critChance = 10;                 // in percentage out of 100
    
    @Replicate
    public float critFactor = 2;                // factor to multiply damage with

}
