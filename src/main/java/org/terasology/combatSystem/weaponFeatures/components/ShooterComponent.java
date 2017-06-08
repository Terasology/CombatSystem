package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.combatSystem.OwnerCollisionState;
import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.entity.EntityRef;

public class ShooterComponent implements Component{
    public EntityRef shooter = EntityRef.NULL;
    public OwnerCollisionState state = OwnerCollisionState.DISABLED;
    
    public ShooterComponent(){
        
    }
    
    public ShooterComponent(EntityRef shooter){
        this(shooter, null);
    }
    
    public ShooterComponent(OwnerCollisionState state){
        this(null, state);
    }
    
    public ShooterComponent(EntityRef shooter, OwnerCollisionState state){
        if(shooter != null){
            this.shooter = shooter;
        }
        if(state != null){
            this.state = state;
        }
    }

}
