package org.terasology.combatSystem.weaponFeatures.systems;

import org.terasology.combatSystem.hurting.HurtEvent;
import org.terasology.combatSystem.weaponFeatures.components.MeleeComponent;
import org.terasology.combatSystem.weaponFeatures.events.MeleeEvent;
import org.terasology.combatSystem.weaponFeatures.events.PrimaryAttackEvent;
import org.terasology.combatSystem.weaponFeatures.events.SecondaryAttackEvent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.math.geom.Vector3f;

@RegisterSystem
public class MeleeAttackSystem extends BaseComponentSystem{
    
    @ReceiveEvent(components = MeleeComponent.class)
    public void meleePrimaryAttack(PrimaryAttackEvent event, EntityRef entity, MeleeComponent melee){
        if(melee.primaryAttack){
            entity.send(new MeleeEvent(event.getTarget(), event.getOrigin(), event.getHitPosition()));
        }
    }
    
    @ReceiveEvent(components = MeleeComponent.class)
    public void meleeSecondaryAttack(SecondaryAttackEvent event, EntityRef entity, MeleeComponent melee){
        if(!melee.primaryAttack){
            entity.send(new MeleeEvent(event.getTarget(), event.getOrigin(), event.getHitPosition()));
        }
    }
    
    public void meleeing(MeleeEvent event, EntityRef entity){
        melee(entity, event.getTarget(), event.getWeaponLoc(), event.getTargetHitLoc());
    }
    
    //------------------------------private methods----------------------------
    private void melee(EntityRef entity, EntityRef target, Vector3f weaponLoc, Vector3f targetHitLoc){
        MeleeComponent melee = entity.getComponent(MeleeComponent.class);
        
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
