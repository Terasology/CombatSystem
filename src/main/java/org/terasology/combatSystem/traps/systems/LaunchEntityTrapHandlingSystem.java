package org.terasology.combatSystem.traps.systems;

import org.terasology.combatSystem.weaponFeatures.components.LaunchEntityComponent;
import org.terasology.combatSystem.weaponFeatures.events.LaunchEntityEvent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.location.LocationComponent;
import org.terasology.math.geom.Vector3f;
import org.terasology.sensors.EntitySensedEvent;
import org.terasology.world.block.BlockComponent;

@RegisterSystem
public class LaunchEntityTrapHandlingSystem extends BaseComponentSystem{
    
    @ReceiveEvent(components = LaunchEntityComponent.class)
    public void launchEntity(EntitySensedEvent event, EntityRef entity){
        EntityRef target = event.getEntity();
        LocationComponent targetLoc = target.getComponent(LocationComponent.class);
        if(targetLoc == null){
            return;
        }
        Vector3f targetPos = targetLoc.getWorldPosition();
        
        Vector3f pos;
        LocationComponent loc = entity.getComponent(LocationComponent.class);
        if(loc == null){
            BlockComponent block = entity.getComponent(BlockComponent.class);
            if(block == null){
                return;
            }
            pos = block.getPosition().toVector3f();
        }
        else{
            pos = loc.getWorldPosition();
        }
        
        Vector3f dir = targetPos.sub(pos);
        dir.normalize();
        
        entity.send(new LaunchEntityEvent(dir));
    }

}
