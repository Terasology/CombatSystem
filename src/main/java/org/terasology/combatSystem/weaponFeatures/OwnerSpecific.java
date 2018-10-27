package org.terasology.combatSystem.weaponFeatures;

import com.google.common.collect.Lists;
import org.terasology.combatSystem.weaponFeatures.components.AttackerComponent;
import org.terasology.entitySystem.entity.EntityRef;

import java.util.List;

public class OwnerSpecific {

    /**
     * 
     * get the ultimate owner of the entity. 
     * 
     * @param entity  the entity of the ultimate owner
     * @return  the ultimate owner
     */
    public static EntityRef getUltimateOwner(EntityRef entity){
        /**
         * create a new instance of attacker
         */
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
    
    /**
     * Get first owner
     * @param entity  the entity of the first owner
     * @return  the first owner
     */
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
    
    /**
     * get all owners
     * @param entity  the entity of all owners
     * @return  all owners
     */
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
