package org.terasology.combatSystem.traps.components;

import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.network.Replicate;

public class HomingComponent implements Component{
    @Replicate
    public EntityRef target = EntityRef.NULL;
    
    @Replicate
    public int initialSleepTime = 500;
    
    @Replicate
    public int updationTime = 200;
    
    @Replicate
    public float lastUpdateTime = -1.0f;
    
    @Replicate
    public float launchedTime = -1.0f;
}
