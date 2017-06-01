package org.terasology.combatSystem.weaponFeatures.components;

import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.entity.EntityRef;

public class ShooterComponent implements Component{
    public EntityRef shooter = EntityRef.NULL;
    
    public ShooterComponent(){
        
    }
    
    public ShooterComponent(EntityRef shooter){
        this.shooter = shooter;
    }

}
