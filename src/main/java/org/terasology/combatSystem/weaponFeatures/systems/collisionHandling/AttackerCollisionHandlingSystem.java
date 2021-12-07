// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.weaponFeatures.systems.collisionHandling;

import org.terasology.combatSystem.OwnerCollisionState;
import org.terasology.combatSystem.physics.events.AddCollisionExceptionEvent;
import org.terasology.combatSystem.physics.events.RemoveCollisionExceptionEvent;
import org.terasology.combatSystem.weaponFeatures.OwnerSpecific;
import org.terasology.combatSystem.weaponFeatures.components.AttackerComponent;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.OnActivatedComponent;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.OnChangedComponent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.inventory.events.GiveItemEvent;
import org.terasology.gestalt.entitysystem.event.ReceiveEvent;

@RegisterSystem
public class AttackerCollisionHandlingSystem extends BaseComponentSystem {
    @ReceiveEvent(components = AttackerComponent.class)
    public void resolveShooterState(OnActivatedComponent event, EntityRef entity) {
        updateExceptionListForShooterComponent(entity);
    }
    
    @ReceiveEvent(components = AttackerComponent.class)
    public void updateShooterState(OnChangedComponent event, EntityRef entity) {
        updateExceptionListForShooterComponent(entity);
    }
    
    @ReceiveEvent(components = AttackerComponent.class)
    public void makeEntityAsWeaponOwner(GiveItemEvent event, EntityRef entity) {
        AttackerComponent attacker = entity.getComponent(AttackerComponent.class);
        attacker.attacker = event.getTargetEntity();
        
        entity.saveComponent(attacker);
    }
    
    //--------------------private methods--------------------------------
    private void updateExceptionListForShooterComponent(EntityRef entity) {
        AttackerComponent attacker = entity.getComponent(AttackerComponent.class);
        if (attacker == null) {
            return;
        }
        
        if (attacker.state == OwnerCollisionState.DISABLED) {
            entity.send(new AddCollisionExceptionEvent(OwnerSpecific.getAllOwners(entity)));
        } else if (attacker.state == OwnerCollisionState.DISABLED_WITH_DIRECT_OWNER) {
            entity.send(new RemoveCollisionExceptionEvent(OwnerSpecific.getAllOwners(entity)));
            entity.send(new AddCollisionExceptionEvent(OwnerSpecific.getFirstOwner(entity)));
        } else if (attacker.state == OwnerCollisionState.DISABLED_WITH_ULTIMATE_OWNER) {
            entity.send(new RemoveCollisionExceptionEvent(OwnerSpecific.getAllOwners(entity)));
            entity.send(new AddCollisionExceptionEvent(OwnerSpecific.getUltimateOwner(entity)));
        } else if (attacker.state == OwnerCollisionState.ENABLED) {
            entity.send(new RemoveCollisionExceptionEvent(OwnerSpecific.getAllOwners(entity)));
        }
    }
}
