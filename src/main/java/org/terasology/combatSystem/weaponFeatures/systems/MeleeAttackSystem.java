package org.terasology.combatSystem.weaponFeatures.systems;

import org.terasology.combatSystem.hurting.HurtEvent;
import org.terasology.combatSystem.weaponFeatures.components.MeleeComponent;
import org.terasology.combatSystem.weaponFeatures.events.MeleeEvent;
import org.terasology.combatSystem.weaponFeatures.events.PrimaryAttackEvent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.math.geom.Vector3f;

/**
 * Manages meelee attacks
 */
@RegisterSystem
public class MeleeAttackSystem extends BaseComponentSystem{
    
    /**
     * Launch a melee primary attack
     * @param event primary attack event
     * @param entity entity attacking
     */
    @ReceiveEvent(components = MeleeComponent.class)
    public void meleePrimaryAttack(PrimaryAttackEvent event, EntityRef entity){
        melee(entity, event.getTarget(), event.getOrigin(), event.getHitPosition());
        
    }
    
    /**
     * Launch a melee attack
     * @param event melee event
     * @param entity entity attacking
     */
    public void meleeing(MeleeEvent event, EntityRef entity){
        melee(entity, event.getTarget(), event.getWeaponLoc(), event.getTargetHitLoc());
    }
    
    //------------------------------private methods----------------------------
    /**
     * Damages the target if the target is in range
     * @param entity The entity attacking
     * @param target The target of the attack
     * @param weaponLoc The location the attack is coming from in the world
     * @param targetHitLoc The location the attack is hitting in the world
     */
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
            entity.send(new HurtEvent(target));
        }
    }

}
