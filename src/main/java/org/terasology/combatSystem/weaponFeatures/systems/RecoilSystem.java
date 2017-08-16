package org.terasology.combatSystem.weaponFeatures.systems;

import org.terasology.combatSystem.weaponFeatures.OwnerSpecific;
import org.terasology.combatSystem.weaponFeatures.components.RecoilComponent;
import org.terasology.combatSystem.weaponFeatures.events.LaunchEntityEvent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.EventPriority;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.characters.GazeAuthoritySystem;
import org.terasology.logic.inventory.ItemComponent;
import org.terasology.logic.location.LocationComponent;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.math.geom.Quat4f;
import org.terasology.math.geom.Vector3f;
import org.terasology.registry.In;

@RegisterSystem
public class RecoilSystem extends BaseComponentSystem{
    @In
    private LocalPlayer localPlayer;
    
    private float COS_PIE_BY_4 = (float) Math.cos(Math.PI/4);
    
    @ReceiveEvent(components = {RecoilComponent.class}, priority = EventPriority.PRIORITY_LOW)
    public void recoil(LaunchEntityEvent event, EntityRef entity, RecoilComponent recoil){
        EntityRef recoilEntity = entity;
        if(entity.hasComponent(ItemComponent.class)){
            recoilEntity = GazeAuthoritySystem.getGazeEntityForCharacter(OwnerSpecific.getUltimateOwner(entity));
        }
        recoilEntity(recoilEntity, recoil.amount);
    }
    
    //-------------------------private methods---------------------------
    private void recoilEntity(EntityRef entity, float recoilAmount){
        LocationComponent location = entity.getComponent(LocationComponent.class);
        if(location == null){
            return;
        }
        
        Vector3f currentDir = location.getWorldDirection();
        currentDir.normalize();
        Quat4f xQuat = Quat4f.shortestArcQuat(currentDir, Vector3f.west());
        Quat4f yQuat = Quat4f.shortestArcQuat(currentDir, Vector3f.up());
        Quat4f zQuat = Quat4f.shortestArcQuat(currentDir, Vector3f.north());
        
        float xDiff = Math.abs(xQuat.w - COS_PIE_BY_4);
        float yDiff = Math.abs(yQuat.w - COS_PIE_BY_4);
        float zDiff = Math.abs(zQuat.w - COS_PIE_BY_4);
        
        Vector3f axis;
        
        if(xDiff <= yDiff){
            if(xDiff <= zDiff){
                axis = Vector3f.west();
            }
            else{
                axis = Vector3f.north();
            }
        }
        else if(yDiff <= zDiff){
            axis = Vector3f.up();
        }
        else{
            axis = Vector3f.north();
        }
        
        Quat4f currentRot = location.getWorldRotation();
        Quat4f recoilRot = new Quat4f(axis, recoilAmount);
        
        currentRot.mul(recoilRot);
        
        location.setWorldRotation(currentRot);
        entity.saveComponent(location);
    }
}
