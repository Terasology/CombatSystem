package org.terasology.combatSystem.weaponFeatures.systems.collisionHandling;

import org.terasology.combatSystem.OwnerCollisionState;
import org.terasology.combatSystem.weaponFeatures.OwnerSpecific;
import org.terasology.combatSystem.weaponFeatures.components.AttackerComponent;
import org.terasology.combatSystem.weaponFeatures.events.AddCollisionExceptionEvent;
import org.terasology.combatSystem.weaponFeatures.events.RemoveCollisionExceptionEvent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.entity.lifecycleEvents.OnActivatedComponent;
import org.terasology.entitySystem.entity.lifecycleEvents.OnChangedComponent;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;

@RegisterSystem
public class AttackerCollisionHandlingSystem extends BaseComponentSystem{
    @ReceiveEvent(components = AttackerComponent.class)
    public void resolveShooterState(OnActivatedComponent event, EntityRef entity){
        updateExceptionListForShooterComponent(entity);
    }
    
    @ReceiveEvent(components = AttackerComponent.class)
    public void updateShooterState(OnChangedComponent event, EntityRef entity){
        updateExceptionListForShooterComponent(entity);
    }
    
    //--------------------private methods--------------------------------
    private void updateExceptionListForShooterComponent(EntityRef entity){
        AttackerComponent attacker = entity.getComponent(AttackerComponent.class);
        if(attacker == null){
            return;
        }
        
        if(attacker.state == OwnerCollisionState.DISABLED){
            entity.send(new AddCollisionExceptionEvent(OwnerSpecific.getAllOwners(entity)));
        }
        else if(attacker.state == OwnerCollisionState.DISABLED_WITH_DIRECT_OWNER){
            entity.send(new RemoveCollisionExceptionEvent(OwnerSpecific.getAllOwners(entity)));
            entity.send(new AddCollisionExceptionEvent(OwnerSpecific.getFirstOwner(entity)));
        }
        else if(attacker.state == OwnerCollisionState.DISABLED_WITH_ULTIMATE_OWNER){
            entity.send(new RemoveCollisionExceptionEvent(OwnerSpecific.getAllOwners(entity)));
            entity.send(new AddCollisionExceptionEvent(OwnerSpecific.getUltimateOwner(entity)));
        }
        else if(attacker.state == OwnerCollisionState.ENABLED){
            entity.send(new RemoveCollisionExceptionEvent(OwnerSpecific.getAllOwners(entity)));
        }
    }
}
