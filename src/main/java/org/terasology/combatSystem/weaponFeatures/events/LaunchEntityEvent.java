package org.terasology.combatSystem.weaponFeatures.events;

import org.terasology.entitySystem.event.Event;
import org.terasology.math.geom.Vector3f;

public class LaunchEntityEvent implements Event{
    private Vector3f direction;
    
    public LaunchEntityEvent(Vector3f dir){
        direction = dir;
    }
    
    public Vector3f getDirection(){
        return direction;
    }
}
