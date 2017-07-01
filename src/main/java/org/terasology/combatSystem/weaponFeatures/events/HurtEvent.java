package org.terasology.combatSystem.weaponFeatures.events;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.Event;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.logic.health.EngineDamageTypes;

public class HurtEvent implements Event{
    int amount = 3;
    Prefab damageType = EngineDamageTypes.DIRECT.get();
    
    EntityRef target = EntityRef.NULL;
    
    public HurtEvent(){
        
    }
    
    public HurtEvent(EntityRef entity){
        this(entity, 3, EngineDamageTypes.DIRECT.get());
    }
    
    public HurtEvent(EntityRef entity, int amount){
        this(entity, amount, EngineDamageTypes.DIRECT.get());
    }
    
    public HurtEvent(EntityRef entity, int amount, Prefab damageType){
        target = entity;
        this.amount = amount;
        this.damageType = damageType;
    }
    
    public EntityRef getTarget(){
        return target;
    }
    
    public int getAmount(){
        return amount;
    }
    
    public Prefab getDamageType(){
        return damageType;
    }

}
