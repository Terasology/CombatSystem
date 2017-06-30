package org.terasology.combatSystem.weaponFeatures.systems;

import java.util.Random;

import org.terasology.combatSystem.weaponFeatures.OwnerSpecific;
import org.terasology.combatSystem.weaponFeatures.components.CritDamageComponent;
import org.terasology.combatSystem.weaponFeatures.components.HurtingComponent;
import org.terasology.combatSystem.weaponFeatures.events.HurtEvent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.health.DoDamageEvent;
import org.terasology.logic.health.HealthComponent;

@RegisterSystem
public class HurtingHandlingSystem extends BaseComponentSystem{
    
    @ReceiveEvent(components = HurtingComponent.class)
    public void hurting(HurtEvent event, EntityRef entity){
        HurtingComponent hurting = entity.getComponent(HurtingComponent.class);
        if(!hurting.canHurt){
            return;
        }
        
        // checking if the damage can crit. If yes than letting the critHurting function to handle it.
        if(entity.hasComponent(CritDamageComponent.class)){
            return;
        }
        
        EntityRef otherEntity = event.getTarget();
        if(otherEntity == null || otherEntity == EntityRef.NULL){
            return;
        }
        
        if(otherEntity.hasComponent(HealthComponent.class)){
            EntityRef instigator = OwnerSpecific.getUltimateOwner(entity);
            
            otherEntity.send(new DoDamageEvent(event.getAmount(), event.getDamageType(), instigator, entity));
        }
    }
    
    @ReceiveEvent(components = {HurtingComponent.class, CritDamageComponent.class})
    public void critHurting(HurtEvent event, EntityRef entity){
        HurtingComponent hurting = entity.getComponent(HurtingComponent.class);
        CritDamageComponent critDamage = entity.getComponent(CritDamageComponent.class);
        if(!hurting.canHurt){
            return;
        }
        
        EntityRef otherEntity = event.getTarget();
        if(otherEntity == null || otherEntity == EntityRef.NULL){
            return;
        }
        
        if(otherEntity.hasComponent(HealthComponent.class)){
            EntityRef instigator = OwnerSpecific.getUltimateOwner(entity);
            
            Random rand = new Random();
            int value = rand.nextInt(100);
            
            // We take the crit chances to be 10% approx and damage doubles when crit
            if(value < critDamage.critChance){
                otherEntity.send(new DoDamageEvent((int)(event.getAmount()*critDamage.critFactor), event.getDamageType(), instigator, entity));
            }
            else{
                otherEntity.send(new DoDamageEvent(event.getAmount(), event.getDamageType(), instigator, entity));
            }
        }
    }

}
