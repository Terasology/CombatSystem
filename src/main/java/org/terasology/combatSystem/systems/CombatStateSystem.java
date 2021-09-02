// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.systems;

import org.terasology.combatSystem.event.CombatEnteredEvent;
import org.terasology.combatSystem.event.CombatLeftEvent;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;

/**
 * handles the features to be triggered when Combat State is entered or exited
 */
@RegisterSystem
public class CombatStateSystem extends BaseComponentSystem {
    
    /**
     * code for combat entered features comes here. Like change in music
     * @param event
     * @param entity
     */
    @ReceiveEvent
    public void combatStateEntered(CombatEnteredEvent event, EntityRef entity) {
        
    }
    
    /**
     * code for combat left features comes here. Like change in theme or music
     * @param event
     * @param entity
     */
    @ReceiveEvent
    public void combatStateLeft(CombatLeftEvent event, EntityRef entity) {
        
    }

}
