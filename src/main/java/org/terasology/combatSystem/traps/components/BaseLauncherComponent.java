package org.terasology.combatSystem.traps.components;

import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.entitySystem.prefab.Prefab;
import org.terasology.engine.network.Replicate;

public class BaseLauncherComponent implements Component{
    @Replicate
    public Prefab entityInBase;
    
    @Replicate
    public int amountOfEntityInBase;
    
    @Replicate
    public int coolDownTime = 1000;
    
    @Replicate
    public float lastLaunchTime = -1.0f;

}
