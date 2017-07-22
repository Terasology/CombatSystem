package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.entitySystem.Component;
import org.terasology.network.Replicate;

public class MeleeComponent implements Component{
    
    @Replicate
    public float range = 2.0f;
    
    @Replicate(initialOnly = true)
    public boolean primaryAttack = false;
}
