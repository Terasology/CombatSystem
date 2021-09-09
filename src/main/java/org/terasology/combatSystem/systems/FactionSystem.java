// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.systems;

import org.terasology.combatSystem.Faction;
import org.terasology.combatSystem.components.FactionComponent;
import org.terasology.combatSystem.event.CombatEnteredEvent;
import org.terasology.combatSystem.hurting.HurtEvent;
import org.terasology.combatSystem.hurting.HurtingComponent;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.gestalt.entitysystem.event.ReceiveEvent;

@RegisterSystem
public class FactionSystem extends BaseComponentSystem {
    
    @ReceiveEvent(components = {FactionComponent.class, HurtingComponent.class})
    public void combatEntered(HurtEvent event, EntityRef entity) {
        EntityRef otherEntity = event.getTarget();
        
        if (!otherEntity.hasComponent(FactionComponent.class)) {
            return;
        }
        
        Faction entityFaction = entity.getComponent(FactionComponent.class).faction;
        Faction otherEntityFaction = entity.getComponent(FactionComponent.class).faction;
        if (entityFaction != otherEntityFaction) {
            entity.send(new CombatEnteredEvent());
            otherEntity.send(new CombatEnteredEvent());
        }
    }

}
