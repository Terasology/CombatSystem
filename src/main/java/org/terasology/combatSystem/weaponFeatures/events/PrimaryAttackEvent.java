package org.terasology.combatSystem.weaponFeatures.events;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.Event;
import org.terasology.logic.common.ActivateEvent;
import org.terasology.logic.location.LocationComponent;
import org.terasology.math.geom.Vector3f;

/**
 * Triggered in case of a primary attack
 */
public class PrimaryAttackEvent implements Event{
    private EntityRef instigator;
    private EntityRef target;
    private Vector3f origin;
    private Vector3f direction;
    private Vector3f hitPosition;
    private Vector3f hitNormal;
    private int activationId;
    
    /**
     * Default constructor required for persistence
     */
    public PrimaryAttackEvent(){
        
    }
    
    /**
     * Creates an event based on an ActivateEvent.
     * @param info The ActivateEvent to base this event off of.
     */
    public PrimaryAttackEvent(ActivateEvent info){
        instigator = info.getInstigator();
        target = info.getTarget();
        origin = info.getOrigin();
        direction = info.getDirection();
        hitPosition = info.getHitPosition();
        hitNormal = info.getHitNormal();
        activationId = info.getActivationId();
    }
    
    /**
     * Gets the instigator of this event.
     * @return the instigator of this event.
     */
    public EntityRef getInstigator() {
        return instigator;
    }

    /**
     * Gets the target of the attack.
     * @return the target of the attack.
     */
    public EntityRef getTarget() {
        return target;
    }

    /**
     * Gets the origin position of the attack.
     * @return the origin position of the attack.
     */
    public Vector3f getOrigin() {
        return origin;
    }

    /**
     * Gets the direction position of the attack
     * @return the direction position of the attack.
     */
    public Vector3f getDirection() {
        return direction;
    }

    /**
     * Gets position where target is hit
     * @return position where target is hit
     */
    public Vector3f getHitPosition() {
        return hitPosition;
    }

    /**
     * Gets position of normal force perpendicular to hit position
     * @return position of normal force perpendicular to hit position
     */
    public Vector3f getHitNormal() {
        return hitNormal;
    }

    /**
     * Gets activation ID
     * @return activation ID
     */
    public int getActivationId() {
        return activationId;
    }

    /**
     * Check if target location is null and gets target position
     * @return target location
     */
    public Vector3f getTargetLocation() {
        LocationComponent loc = target.getComponent(LocationComponent.class);
        if (loc != null) {
            return loc.getWorldPosition();
        }
        return null;
    }

    /**
     * Check if instigator location is null and gets instigator position
     * @return instigator position
     */
    public Vector3f getInstigatorLocation() {
        LocationComponent loc = instigator.getComponent(LocationComponent.class);
        if (loc != null) {
            return loc.getWorldPosition();
        }
        return new Vector3f();
    }

}
