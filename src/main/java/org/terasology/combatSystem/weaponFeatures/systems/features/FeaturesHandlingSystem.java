package org.terasology.combatSystem.weaponFeatures.systems.features;

import org.terasology.combatSystem.ComponentUsageType;
import org.terasology.combatSystem.weaponFeatures.components.features.FeatureComponent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.BaseComponentSystem;

public class FeaturesHandlingSystem extends BaseComponentSystem{
    
    
    
    //-----------------protected methods----------------
    
    protected void resolveFeatureUsage(FeatureComponent feature, EntityRef entity){
        if(feature.usageType == ComponentUsageType.ONCE){
            entity.removeComponent(feature.getClass());
        }
        else if(feature.usageType == ComponentUsageType.UNLIMITED){
            
        }
    }

}
