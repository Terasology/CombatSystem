package org.terasology.combatSystem.weaponFeatures.systems;

import org.terasology.combatSystem.weaponFeatures.OwnerSpecific;
import org.terasology.combatSystem.weaponFeatures.components.HurtingComponent;
import org.terasology.combatSystem.weaponFeatures.events.HurtEvent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.health.DoDamageEvent;
import org.terasology.logic.health.HealthComponent;

@RegisterSystem(RegisterMode.AUTHORITY)
public class HurtingHandlingSystem extends BaseComponentSystem{
    
    @ReceiveEvent(components = HurtingComponent.class)
    public void hurting(HurtEvent event, EntityRef entity){
        HurtingComponent hurting = entity.getComponent(HurtingComponent.class);
        if(!hurting.canHurt){
            return;
        }
        
        EntityRef otherEntity = event.getTarget();
        if(otherEntity == null || otherEntity == EntityRef.NULL){
            return;
        }
        
        if(otherEntity.hasComponent(HealthComponent.class)){
            EntityRef instigator = OwnerSpecific.getUltimateOwner(entity);
            
            otherEntity.send(new DoDamageEvent(hurting.amount, hurting.damageType, instigator, entity));
        }
    }

}
