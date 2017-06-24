package org.terasology.combatSystem.weaponFeatures.systems.collisionHandling;

import org.terasology.combatSystem.weaponFeatures.components.DestroyOnCollisionComponent;
import org.terasology.combatSystem.weaponFeatures.components.HurtingComponent;
import org.terasology.combatSystem.weaponFeatures.events.HurtEvent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.health.EngineDamageTypes;
import org.terasology.physics.events.CollideEvent;

@RegisterSystem
public class DestroyOnCollisionHandlingSystem extends BaseComponentSystem{
    
    @ReceiveEvent(components = DestroyOnCollisionComponent.class)
    public void destroyOnCollision(CollideEvent event, EntityRef entity){
        // damage the other entity
        HurtingComponent hurting = entity.getComponent(HurtingComponent.class);
        if(hurting != null){
            DestroyOnCollisionComponent destroy = entity.getComponent(DestroyOnCollisionComponent.class);
            hurting.amount = destroy.amount;
            hurting.damageType = EngineDamageTypes.DIRECT.get();
            entity.saveComponent(hurting);
            entity.send(new HurtEvent(event.getOtherEntity()));
        }
        
        entity.destroy();
    }

}
