package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.entitySystem.Component;
import org.terasology.network.Replicate;

public class SniperScopeComponent implements Component{
    @Replicate
    public float zoom = -85;
    @Replicate
    public float radius = 5;
    @Replicate
    public boolean scoped = false;
    
    @Replicate(initialOnly = true)
    public boolean primaryAttack = true;
}
