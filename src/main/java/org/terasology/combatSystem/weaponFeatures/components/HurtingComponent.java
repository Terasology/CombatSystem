package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.entitySystem.Component;
import org.terasology.network.Replicate;

public class HurtingComponent implements Component{
    @Replicate
    public boolean canHurt = true;

}
