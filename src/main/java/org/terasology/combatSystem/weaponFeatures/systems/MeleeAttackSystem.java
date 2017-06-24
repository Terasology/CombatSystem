package org.terasology.combatSystem.weaponFeatures.systems;

import java.util.Random;

import org.terasology.combatSystem.weaponFeatures.components.HurtingComponent;
import org.terasology.combatSystem.weaponFeatures.components.MeleeComponent;
import org.terasology.combatSystem.weaponFeatures.events.CritHurtEvent;
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
        MeleeComponent melee = entity.getComponent(MeleeComponent.class);
        if(!melee.primaryAttack){
            return;
        }
        
        Vector3f weaponLoc = event.getOrigin();
        Vector3f targetHitLoc = event.getHitPosition();
        
        if(weaponLoc == null || targetHitLoc == null){
            return;
        }
        
        float distanceSq = weaponLoc.distanceSquared(targetHitLoc);
        
        if(distanceSq <= (melee.range*melee.range)){
            EntityRef otherEntity = event.getTarget();
            
            // damage the other entity
            HurtingComponent hurting = entity.getComponent(HurtingComponent.class);
            if(hurting != null){
                Random rand = new Random();
                hurting.amount = rand.nextInt(melee.maxDamage - melee.minDamage + 1) + melee.minDamage;
                hurting.damageType = EngineDamageTypes.DIRECT.get();
                entity.saveComponent(hurting);
                entity.send(new CritHurtEvent(otherEntity));
            }
        }
    }

}
