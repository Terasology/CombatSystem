package org.terasology.combatSystem.weaponFeatures.events;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.Event;
import org.terasology.math.geom.Vector3f;

public class BounceEvent implements Event{
    EntityRef target;
    Vector3f normal;
    
    public BounceEvent(Vector3f normal){
        this.normal = normal;
    }
    
    public BounceEvent(EntityRef target, Vector3f normal){
        this.target = target;
        this.normal = normal;
    }
    
    public Vector3f getNormal(){
        return normal;
    }
    
    public EntityRef getTarget(){
        return target;
    }

}
