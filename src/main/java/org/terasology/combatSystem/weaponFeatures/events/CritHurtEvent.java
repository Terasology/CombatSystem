package org.terasology.combatSystem.weaponFeatures.events;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.Event;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.logic.health.EngineDamageTypes;

public class CritHurtEvent implements Event{
    int amount = 3;
    Prefab damageType = EngineDamageTypes.DIRECT.get();
    int critChance = 10;                        //in percentage out of 100
    
    EntityRef target = EntityRef.NULL;
    
    public CritHurtEvent(){
        
    }
    
    public CritHurtEvent(EntityRef entity){
        this(entity, 3, EngineDamageTypes.DIRECT.get(), 10);
    }
    
    public CritHurtEvent(EntityRef entity, int amount){
        this(entity, amount, EngineDamageTypes.DIRECT.get(), 10);
    }
    
    public CritHurtEvent(EntityRef entity, int amount, Prefab damageType){
        this(entity, amount, damageType, 10);
    }
    
    public CritHurtEvent(EntityRef entity, int amount, Prefab damageType, int critChance){
        target = entity;
        this.amount = amount;
        this.damageType = damageType;
        this.critChance = critChance;
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
    
    public int getCritChance(){
        return critChance;
    }

}
