package org.terasology.combatSystem.systems;

import org.terasology.combatSystem.event.CombatEnteredEvent;
import org.terasology.combatSystem.event.CombatLeftEvent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;

/**
 * handles the features to be triggered when Combat State is entered or exited
 */
@RegisterSystem
public class CombatStateSystem extends BaseComponentSystem{
    
    // code for combat entered features comes here. Like change in music
    @ReceiveEvent()
    public void combatStateEntered(CombatEnteredEvent event, EntityRef entity){
        
    }
    
    // code for combat left features comes here. Like change in theme or music
    @ReceiveEvent()
    public void combatLefteEntered(CombatLeftEvent event, EntityRef entity){
        
    }

}
