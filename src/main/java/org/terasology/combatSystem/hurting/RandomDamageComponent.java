package org.terasology.combatSystem.hurting;

import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.network.Replicate;

/**
 * To generate a random damage amount between <b>minDamage</b> and <b>maxDamage</b> (both inclusive).
 */
public class RandomDamageComponent implements Component{
    //inclusive
    @Replicate
    public int minDamage = 2;
    
    //inclusive
    @Replicate
    public int maxDamage = 4;

}
