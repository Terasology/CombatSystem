package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.combatSystem.OwnerCollisionState;
import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.network.Replicate;

public class AttackerComponent implements Component{
    @Replicate
    public EntityRef attacker = EntityRef.NULL;
    
    @Replicate
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
