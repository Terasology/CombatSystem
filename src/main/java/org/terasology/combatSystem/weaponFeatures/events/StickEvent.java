package org.terasology.combatSystem.weaponFeatures.events;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.Event;
import org.terasology.math.geom.Quat4f;
import org.terasology.math.geom.Vector3f;

public class StickEvent implements Event{
    EntityRef target;
    Vector3f offset;
    Quat4f relativeRotation;
    float relativeScale;
    
    public StickEvent(EntityRef target){
        this.target = target;
        offset = new Vector3f(0.0f, 0.0f, 0.0f);
        relativeRotation = new Quat4f();
        relativeScale = 1.0f;
    }
    
    public StickEvent(EntityRef target, Vector3f offset, Quat4f relativeRot, float scale){
        this.target = target;
        this.offset = offset;
        relativeRotation = relativeRot;
        relativeScale = scale;
    }
    
    public EntityRef getTarget(){
        return target;
    }
    
    public Vector3f getOffset(){
        return offset;
    }
    
    public Quat4f getRelativeRotation(){
        return relativeRotation;
    }
    
    public float getRelativeScale(){
        return relativeScale;
    }

}
