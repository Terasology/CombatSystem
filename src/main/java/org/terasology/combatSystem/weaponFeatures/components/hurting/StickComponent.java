package org.terasology.combatSystem.weaponFeatures.components.hurting;

import org.terasology.entitySystem.entity.EntityRef;

public class StickComponent extends HurtingComponent{
    EntityRef target = EntityRef.NULL;
    
    public void setTarget(EntityRef entity){
        target = entity;
    }
    
    public EntityRef getTarget(){
        return target;
    }

}
