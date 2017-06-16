package org.terasology.combatSystem.weaponFeatures;

import java.util.List;

import org.terasology.combatSystem.weaponFeatures.components.ShooterComponent;
import org.terasology.entitySystem.entity.EntityRef;

import com.google.common.collect.Lists;

public class OwnerSpecific {
    // get the ultimate owner of the entity. The owner of the entity may have an owner of its
    // own, that ultimate owner is returned from this method through recursion
    public static EntityRef getUltimateOwner(EntityRef entity){
        ShooterComponent shooter = entity.getComponent(ShooterComponent.class);
        
        if(shooter == null){
            return null;
        }
        if(shooter.shooter == EntityRef.NULL || shooter.shooter == null){
            return null;
        }
        else{
            return recursiveOwner(shooter.shooter);
        }
    }
    
    private static EntityRef recursiveOwner(EntityRef entity){
        ShooterComponent shooter = entity.getComponent(ShooterComponent.class);
        
        if(shooter == null){
            return entity;
        }
        if(shooter.shooter == EntityRef.NULL || shooter.shooter == null){
            return entity;
        }
        else{
            return recursiveOwner(shooter.shooter);
        }
    }
    
    public static EntityRef getFirstOwner(EntityRef entity){
        ShooterComponent shooter = entity.getComponent(ShooterComponent.class);
        
        if(shooter == null){
            return null;
        }
        if(shooter.shooter == EntityRef.NULL || shooter.shooter == null){
            return null;
        }
        else{
            return shooter.shooter;
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
