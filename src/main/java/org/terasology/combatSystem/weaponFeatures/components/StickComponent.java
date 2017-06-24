package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.entity.EntityRef;

public class StickComponent implements Component{
    EntityRef target = EntityRef.NULL;
    public int amount = 3;
    
    public void setTarget(EntityRef entity){
        target = entity;
    }
    
    public EntityRef getTarget(){
        return target;
    }

}
