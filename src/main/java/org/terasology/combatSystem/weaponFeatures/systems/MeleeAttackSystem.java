package org.terasology.combatSystem.weaponFeatures.systems;

import java.util.Random;

import org.terasology.combatSystem.weaponFeatures.components.MeleeComponent;
import org.terasology.combatSystem.weaponFeatures.events.CritHurtEvent;
import org.terasology.combatSystem.weaponFeatures.events.MeleeEvent;
import org.terasology.combatSystem.weaponFeatures.events.PrimaryAttackEvent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.health.EngineDamageTypes;
import org.terasology.math.geom.Vector3f;

@RegisterSystem
public class MeleeAttackSystem extends BaseComponentSystem{
    
    @ReceiveEvent(components = MeleeComponent.class)
    public void meleePrimaryAttack(PrimaryAttackEvent event, EntityRef entity){
        melee(entity, event.getTarget(), event.getOrigin(), event.getHitPosition());
        
    }
    
    public void meleeing(MeleeEvent event, EntityRef entity){
        melee(entity, event.getTarget(), event.getWeaponLoc(), event.getTargetHitLoc());
    }
    
    //------------------------------private methods----------------------------
    private void melee(EntityRef entity, EntityRef target, Vector3f weaponLoc, Vector3f targetHitLoc){
        MeleeComponent melee = entity.getComponent(MeleeComponent.class);
        if(!melee.primaryAttack){
            return;
        }
        
        if(weaponLoc == null || targetHitLoc == null){
            return;
        }
        
        float distanceSq = weaponLoc.distanceSquared(targetHitLoc);
        
        if(distanceSq <= (melee.range*melee.range)){
            // damage the other entity
            Random rand = new Random();
            int amount = rand.nextInt(melee.maxDamage - melee.minDamage + 1) + melee.minDamage;
            entity.send(new CritHurtEvent(target, amount, EngineDamageTypes.DIRECT.get(), melee.critChance));
        }
    }

}
