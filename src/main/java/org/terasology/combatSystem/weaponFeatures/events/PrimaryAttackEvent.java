package org.terasology.combatSystem.weaponFeatures.events;

import org.terasology.entitySystem.event.Event;
import org.terasology.logic.common.ActivateEvent;

public class PrimaryAttackEvent implements Event{
    public ActivateEvent info;
    
    public PrimaryAttackEvent(ActivateEvent info){
        this.info = info;
    }

}
