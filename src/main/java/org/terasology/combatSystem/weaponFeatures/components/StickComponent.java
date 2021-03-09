package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.network.Replicate;

public class StickComponent implements Component{
    @Replicate
    EntityRef target = EntityRef.NULL;
    
    @Replicate
    public float stickTime = -1;
    
    @Replicate
    public float totalStickingTime = -1;
    
    @Replicate
    public float pierceAmount = 1.0f;
    
    public void setTarget(EntityRef entity){
        target = entity;
    }
    
    public EntityRef getTarget(){
        return target;
    }

}
