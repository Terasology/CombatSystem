// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.weaponFeatures.systems;

import org.joml.Vector3f;
import org.terasology.combatSystem.OwnerCollisionState;
import org.terasology.combatSystem.weaponFeatures.components.AttackerComponent;
import org.terasology.combatSystem.weaponFeatures.components.PrimaryAttackComponent;
import org.terasology.combatSystem.weaponFeatures.events.PrimaryAttackEvent;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.OnChangedComponent;
import org.terasology.engine.entitySystem.event.EventPriority;
import org.terasology.engine.entitySystem.event.Priority;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.characters.CharacterHeldItemComponent;
import org.terasology.engine.logic.characters.CharacterImpulseEvent;
import org.terasology.engine.logic.common.ActivateEvent;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.engine.world.block.items.BlockItemComponent;
import org.terasology.engine.world.block.items.OnBlockItemPlaced;
import org.terasology.gestalt.entitysystem.event.ReceiveEvent;

@RegisterSystem
public class AttackSystem extends BaseComponentSystem {

    @ReceiveEvent(components = PrimaryAttackComponent.class)
    public void primaryAttack(ActivateEvent event, EntityRef entity) {
        entity.send(new PrimaryAttackEvent(event));
    }

    @ReceiveEvent
    public void giveImpulse(PrimaryAttackEvent event, EntityRef entity) {
        EntityRef instigator = event.getInstigator();
        EntityRef target = event.getTarget();
        if (instigator.exists() && target.exists()) {
            LocationComponent locI = instigator.getComponent(LocationComponent.class);
            LocationComponent locT = target.getComponent(LocationComponent.class);
            Vector3f impulse = new Vector3f(locT.getWorldPosition(new Vector3f())).sub(locI.getWorldPosition(new Vector3f()));
            impulse.normalize();
            impulse.mul(5);
            target.send(new CharacterImpulseEvent(impulse));
        }
    }

    @Priority(EventPriority.PRIORITY_HIGH)
    @ReceiveEvent(components = CharacterHeldItemComponent.class)
    public void addAttacker(OnChangedComponent event, EntityRef character) {
        CharacterHeldItemComponent heldItem = character.getComponent(CharacterHeldItemComponent.class);
        EntityRef item = heldItem.selectedItem;

        AttackerComponent attacker = item.getComponent(AttackerComponent.class);
        if (attacker == null) {
            if (item.hasComponent(BlockItemComponent.class)) {
                attacker = new AttackerComponent(OwnerCollisionState.DISABLED);
            } else {
                return;
            }
        }

        attacker.attacker = character;
        item.addOrSaveComponent(attacker);
    }

    @Priority(EventPriority.PRIORITY_HIGH)
    @ReceiveEvent
    public void addAttacker(OnBlockItemPlaced event, EntityRef item) {
        EntityRef block = event.getPlacedBlock();

        AttackerComponent attacker = block.getComponent(AttackerComponent.class);
        if (attacker == null) {
            return;
        }

        AttackerComponent itemAttacker = item.getComponent(AttackerComponent.class);
        if (itemAttacker == null) {
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
