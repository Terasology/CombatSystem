package org.terasology.combatSystem.weaponFeatures.systems.features;

import org.terasology.combatSystem.weaponFeatures.components.features.StickAfterBounceComponent;
import org.terasology.combatSystem.weaponFeatures.components.hurting.BounceComponent;
import org.terasology.combatSystem.weaponFeatures.components.hurting.StickComponent;
import org.terasology.combatSystem.weaponFeatures.events.AddFeaturesEvent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;

@RegisterSystem(RegisterMode.AUTHORITY)
public class StickAfterBounceHandlingSystem extends FeaturesHandlingSystem{
    @ReceiveEvent(components = {StickAfterBounceComponent.class})
    public void stickAfterBouncing(AddFeaturesEvent event, EntityRef entity){
        if(entity.hasComponent(BounceComponent.class)){
            entity.removeComponent(BounceComponent.class);
        }
        StickComponent stick = new StickComponent();
        
        entity.addOrSaveComponent(stick);
        
        //-----------------repetitive code for every feature event handler------------
        StickAfterBounceComponent stickAfterBounce = entity.getComponent(StickAfterBounceComponent.class);
        resolveFeatureUsage(stickAfterBounce, entity);
    }

}
