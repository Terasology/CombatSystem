package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.entitySystem.Component;
import org.terasology.network.Replicate;

public class MeleeComponent implements Component{
    
    //inclusive
    @Replicate
    public int minDamage = 2;
    
    //inclusive
    @Replicate
    public int maxDamage = 4;
    
    @Replicate
    public float range = 2.0f;
    
    @Replicate(initialOnly = true)
    public boolean primaryAttack = false;
}
