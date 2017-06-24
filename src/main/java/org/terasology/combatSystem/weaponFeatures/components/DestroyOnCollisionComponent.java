package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.entitySystem.Component;
import org.terasology.network.Replicate;

public class DestroyOnCollisionComponent implements Component{
    @Replicate
    public int amount = 3;

}
