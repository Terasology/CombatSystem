package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.combatSystem.OwnerCollisionState;
import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.entity.EntityRef;

public class AttackerComponent implements Component{
    public EntityRef attacker = EntityRef.NULL;
    public OwnerCollisionState state = OwnerCollisionState.DISABLED;
    
    public AttackerComponent(){
        
    }
    
    public AttackerComponent(EntityRef shooter){
        this(shooter, null);
    }
    
    public AttackerComponent(OwnerCollisionState state){
        this(null, state);
    }
    
    public AttackerComponent(EntityRef shooter, OwnerCollisionState state){
        if(shooter != null){
            this.attacker = shooter;
        }
        if(state != null){
            this.state = state;
        }
    }

}
