package org.terasology.combatSystem.weaponFeatures.systems;

import org.terasology.combatSystem.weaponFeatures.components.AttackerComponent;
import org.terasology.combatSystem.weaponFeatures.components.PrimaryAttackComponent;
import org.terasology.combatSystem.weaponFeatures.components.SecondaryAttackComponent;
import org.terasology.combatSystem.weaponFeatures.events.PrimaryAttackEvent;
import org.terasology.combatSystem.weaponFeatures.events.SecondaryAttackEvent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.entity.lifecycleEvents.OnChangedComponent;
import org.terasology.entitySystem.event.EventPriority;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.characters.CharacterHeldItemComponent;
import org.terasology.logic.characters.GazeAuthoritySystem;
import org.terasology.logic.characters.events.AttackEvent;
import org.terasology.logic.characters.events.AttackRequest;
import org.terasology.logic.characters.events.OnItemUseEvent;
import org.terasology.logic.common.ActivateEvent;
import org.terasology.logic.location.LocationComponent;
import org.terasology.math.geom.Vector3f;
import org.terasology.world.block.items.BlockItemComponent;
import org.terasology.world.block.items.OnBlockItemPlaced;

@RegisterSystem
public class AttackSystem extends BaseComponentSystem{
    
    @ReceiveEvent( components = {PrimaryAttackComponent.class})
    public void primaryAttack(ActivateEvent event, EntityRef entity){
        entity.send(new PrimaryAttackEvent(event));
    }
    
    // TODO: intercept Attack Request Event and trigger a secondary attack event
    //       and avoid AttackEvent triggering
    @ReceiveEvent(components = LocationComponent.class, netFilter = RegisterMode.AUTHORITY, 
            priority = EventPriority.PRIORITY_HIGH)
    public void secondaryAttack(AttackRequest event, EntityRef character){
        EntityRef item = event.getItem();
        
        if (item.exists()) {
            if (!character.equals(item.getOwner())) {
                return;
            }
        }
        
        if(!item.hasComponent(SecondaryAttackComponent.class)){
            return;
        }
        
        OnItemUseEvent onItemUseEvent = new OnItemUseEvent();
        character.send(onItemUseEvent);
        if (!onItemUseEvent.isConsumed()) {
            EntityRef gazeEntity = GazeAuthoritySystem.getGazeEntityForCharacter(character);
            LocationComponent gazeLocation = gazeEntity.getComponent(LocationComponent.class);
            Vector3f direction = gazeLocation.getWorldDirection();
            
            item.send(new SecondaryAttackEvent(character, null, null, direction, null, null, -1));
        }
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
        
        
             /*  LeftMouseDownButtonEvent and RightMouseDownButtonEvent seem to be the 
               events that correspond to the actual input, although I have not worked 
               with those events before
             */
//            LeftMouseDownButtonEvent
