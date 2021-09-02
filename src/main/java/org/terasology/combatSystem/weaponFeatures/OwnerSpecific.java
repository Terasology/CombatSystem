// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.weaponFeatures;

import com.google.common.collect.Lists;
import org.terasology.combatSystem.weaponFeatures.components.AttackerComponent;
import org.terasology.engine.entitySystem.entity.EntityRef;

import java.util.List;

public class OwnerSpecific {

    /**
     * 
     * Get the ultimate owner of the entity. 
     * 
     * @param entity  the entity to find the ultimate owner
     * @return  the entity of ultimate owner
     */
    public static EntityRef getUltimateOwner(EntityRef entity) {

        //create a new instance of attacker

        AttackerComponent attacker = entity.getComponent(AttackerComponent.class);
        
        if (attacker == null) {
            return null;
        }
        if (attacker.attacker == EntityRef.NULL || attacker.attacker == null) {
            return null;
        } else {
            return recursiveOwner(attacker.attacker);
        }
    }
    
    /**
     * Recursively gets the owner (attacker) at the top of the chain, who started the attack.
     * @param entity The entity to find the owner of.
     * @return The owner at the top of the chain.
     */
    private static EntityRef recursiveOwner(EntityRef entity) {
        AttackerComponent attacker = entity.getComponent(AttackerComponent.class);
        
        if (attacker == null){
            return entity;
        }
        if (attacker.attacker == EntityRef.NULL || attacker.attacker == null) {
            return entity;
        } else {
            return recursiveOwner(attacker.attacker);
        }
    }
    
    /**
     * Gets the first owner (attacker) of an entity
     * @param entity the entity to find the owner of
     * @return the first owner of that entity
     */
    public static EntityRef getFirstOwner(EntityRef entity) {
        AttackerComponent attacker = entity.getComponent(AttackerComponent.class);
        
        if (attacker == null){
            return null;
        }
        if (attacker.attacker == EntityRef.NULL || attacker.attacker == null) {
            return null;
        } else {
            return attacker.attacker;
        }
    }
    
    /**
     * Get all owners of a certain entity
     * @param entity the entity to check
     * @return all owners of that entity
     */
    public static List<EntityRef> getAllOwners(EntityRef entity) {
        List<EntityRef> entityList = Lists.<EntityRef>newArrayList();
        
        EntityRef temp = getFirstOwner(entity);
        while (temp != null) {
            entityList.add(temp);
            temp = getFirstOwner(temp);
        }
        
        if (entityList.isEmpty()) {
            return null;
        } else {
            return entityList;
        }
    }

}
