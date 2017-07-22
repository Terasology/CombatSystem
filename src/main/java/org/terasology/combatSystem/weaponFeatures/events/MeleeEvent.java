package org.terasology.combatSystem.weaponFeatures.events;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.Event;
import org.terasology.math.geom.Vector3f;

public class MeleeEvent implements Event{
    EntityRef target = EntityRef.NULL;
    Vector3f weaponLoc;
    Vector3f targetHitLoc;
    
    public MeleeEvent(EntityRef target, Vector3f weaponLoc, Vector3f targetHitLoc){
        this.target = target;
        this.weaponLoc = weaponLoc;
        this.targetHitLoc = targetHitLoc;
    }
    
    public EntityRef getTarget(){
        return target;
    }
    
    public Vector3f getWeaponLoc(){
        return weaponLoc;
    }
    
    public Vector3f getTargetHitLoc(){
        return targetHitLoc;
    }

}
