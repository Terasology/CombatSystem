package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.entitySystem.Component;
import org.terasology.network.Replicate;

public class RecoilComponent implements Component{
    @Replicate
    public float amount = -1;             

}
