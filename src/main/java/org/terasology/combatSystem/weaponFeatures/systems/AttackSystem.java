package org.terasology.combatSystem.weaponFeatures.systems;

import org.terasology.combatSystem.weaponFeatures.components.AttackerComponent;
import org.terasology.combatSystem.weaponFeatures.components.PrimaryAttackComponent;
import org.terasology.combatSystem.weaponFeatures.events.PrimaryAttackEvent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.entity.lifecycleEvents.OnChangedComponent;
import org.terasology.entitySystem.event.EventPriority;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.characters.CharacterHeldItemComponent;
import org.terasology.logic.common.ActivateEvent;
import org.terasology.world.block.items.BlockItemComponent;
import org.terasology.world.block.items.OnBlockItemPlaced;

@RegisterSystem
public class AttackSystem extends BaseComponentSystem{
    
    @ReceiveEvent( components = {PrimaryAttackComponent.class})
    public void primaryAttack(ActivateEvent event, EntityRef entity){
        entity.send(new PrimaryAttackEvent(event));
    }
    
    @ReceiveEvent( components = {CharacterHeldItemComponent.class}, priority = EventPriority.PRIORITY_HIGH)
    public void addAttacker(OnChangedComponent event, EntityRef character){
        CharacterHeldItemComponent heldItem = character.getComponent(CharacterHeldItemComponent.class);
        EntityRef item = heldItem.selectedItem;
        
        AttackerComponent attacker = item.getComponent(AttackerComponent.class);
        if(attacker == null){
            if(item.hasComponent(BlockItemComponent.class)){
                attacker = new AttackerComponent();
            }
            else{
                return;
            }
        }
        
        attacker.attacker = character;
        item.addOrSaveComponent(attacker);
    }
    
    @ReceiveEvent(priority = EventPriority.PRIORITY_HIGH)
    public void addAttacker(OnBlockItemPlaced event, EntityRef item){
        EntityRef block = event.getPlacedBlock();
        
        AttackerComponent attacker = block.getComponent(AttackerComponent.class);
        if(attacker == null){
            return;
        }
        
        AttackerComponent itemAttacker = item.getComponent(AttackerComponent.class);
        if(itemAttacker == null){
            return;
        }
        
        attacker.attacker = itemAttacker.attacker;
        block.saveComponent(attacker);
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
