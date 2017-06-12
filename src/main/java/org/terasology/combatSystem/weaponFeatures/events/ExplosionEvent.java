package org.terasology.combatSystem.weaponFeatures.events;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.Event;

public class ExplosionEvent implements Event{
    EntityRef otherEntity;
    
    public ExplosionEvent(){
        otherEntity = EntityRef.NULL;
    }
    
    public ExplosionEvent(EntityRef target){
        otherEntity = target;
    }
    
    public EntityRef getOtherEntity(){
        return otherEntity;
    }

}
