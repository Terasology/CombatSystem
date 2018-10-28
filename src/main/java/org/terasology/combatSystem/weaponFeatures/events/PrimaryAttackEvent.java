package org.terasology.combatSystem.weaponFeatures.events;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.Event;
import org.terasology.logic.common.ActivateEvent;
import org.terasology.logic.location.LocationComponent;
import org.terasology.math.geom.Vector3f;

public class PrimaryAttackEvent implements Event{
    private EntityRef instigator;
    private EntityRef target;
    private Vector3f origin;
    private Vector3f direction;
    private Vector3f hitPosition;
    private Vector3f hitNormal;
    private int activationId;
    
    public PrimaryAttackEvent(){
        
    }
    
    public PrimaryAttackEvent(ActivateEvent info){
        instigator = info.getInstigator();
        target = info.getTarget();
        origin = info.getOrigin();
        direction = info.getDirection();
        hitPosition = info.getHitPosition();
        hitNormal = info.getHitNormal();
        activationId = info.getActivationId();
    }
    
    public EntityRef getInstigator() {
        return instigator;
    }

    /**
     * 
     * @return the target of the primary attack
     */
    public EntityRef getTarget() {
        return target;
    }

    /**
     * 
     * @return  the origin where the attack is from
     */
    public Vector3f getOrigin() {
        return origin;
    }

    /**
     * 
     * @return  the direction of the attack
     */
    public Vector3f getDirection() {
        return direction;
    }

    public Vector3f getHitPosition() {
        return hitPosition;
    }

    public Vector3f getHitNormal() {
        return hitNormal;
    }

    public int getActivationId() {
        return activationId;
    }

    public Vector3f getTargetLocation() {
        LocationComponent loc = target.getComponent(LocationComponent.class);
        if (loc != null) {
            return loc.getWorldPosition();
        }
        return null;
    }

    public Vector3f getInstigatorLocation() {
        LocationComponent loc = instigator.getComponent(LocationComponent.class);
        if (loc != null) {
            return loc.getWorldPosition();
        }
        return new Vector3f();
    }

}
