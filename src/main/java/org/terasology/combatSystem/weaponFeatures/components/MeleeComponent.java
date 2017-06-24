package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.entitySystem.Component;

public class MeleeComponent implements Component{
    public int amount = 3;
    public float range = 2.0f;
    
    public boolean primaryAttack = false;
}
