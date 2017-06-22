package org.terasology.combatSystem.weaponFeatures;

import java.util.List;

import org.terasology.combatSystem.weaponFeatures.components.AttackerComponent;
import org.terasology.entitySystem.entity.EntityRef;

import com.google.common.collect.Lists;

public class OwnerSpecific {
    // get the ultimate owner of the entity. The owner of the entity may have an owner of its
    // own, that ultimate owner is returned from this method through recursion
    public static EntityRef getUltimateOwner(EntityRef entity){
        AttackerComponent attacker = entity.getComponent(AttackerComponent.class);
        
        if(attacker == null){
            return null;
        }
        if(attacker.attacker == EntityRef.NULL || attacker.attacker == null){
            return null;
        }
        else{
            return recursiveOwner(attacker.attacker);
        }
    }
    
    private static EntityRef recursiveOwner(EntityRef entity){
        AttackerComponent attacker = entity.getComponent(AttackerComponent.class);
        
        if(attacker == null){
            return entity;
        }
        if(attacker.attacker == EntityRef.NULL || attacker.attacker == null){
            return entity;
        }
        else{
            return recursiveOwner(attacker.attacker);
        }
    }
    
    public static EntityRef getFirstOwner(EntityRef entity){
        AttackerComponent attacker = entity.getComponent(AttackerComponent.class);
        
        if(attacker == null){
            return null;
        }
        if(attacker.attacker == EntityRef.NULL || attacker.attacker == null){
            return null;
        }
        else{
            return attacker.attacker;
        }
    }
    
    public static List<EntityRef> getAllOwners(EntityRef entity){
        List<EntityRef> entityList = Lists.<EntityRef>newArrayList();
        
        EntityRef temp = getFirstOwner(entity);
        while(temp != null){
            entityList.add(temp);
            temp = getFirstOwner(temp);
        }
        
        if(entityList.isEmpty()){
            return null;
        }
        else{
            return entityList;
        }
    }

}
