package org.terasology.combatSystem.weaponFeatures.systems;

import org.terasology.combatSystem.weaponFeatures.components.PrimaryAttackComponent;
import org.terasology.combatSystem.weaponFeatures.events.PrimaryAttackEvent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.common.ActivateEvent;

@RegisterSystem(RegisterMode.AUTHORITY)
public class AttackSystem extends BaseComponentSystem{
    
    @ReceiveEvent( components = {PrimaryAttackComponent.class})
    public void primaryAttack(ActivateEvent event, EntityRef entity){
        entity.send(new PrimaryAttackEvent(event));
    }
}
        
        
//            AttackEvent primary;
//            ActivateEvent secondary;
            /*
             * ActivateEvent
               to elaborate, ActivateEvent is called on the item when the player right
               clicks while holding the item
               it's not the actual right click event
               is that what you are looking for?
               AttackEvent is also called on the entity that the player left clicks on
               LeftMouseDownButtonEvent and RightMouseDownButtonEvent seem to be the 
               events that correspond to the actual input, although I have not worked 
               with those events before
             */
//            LeftMouseDownButtonEvent
    
//    @RecieveEvent( components = {SecondaryAttackComponent.class})
//    public void secondaryAttackEvent(Attack)
