package org.terasology.combatSystem.systems;

import org.terasology.combatSystem.FactionList;
import org.terasology.combatSystem.components.FactionComponent;
import org.terasology.combatSystem.event.CombatEnteredEvent;
import org.terasology.combatSystem.hurting.HurtEvent;
import org.terasology.combatSystem.hurting.HurtingComponent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;

/**
 * 
 */
@RegisterSystem
public class FactionSystem extends BaseComponentSystem{
    
    @ReceiveEvent(components = {FactionComponent.class, HurtingComponent.class})
    public void combatEntered(HurtEvent event, EntityRef entity){
        EntityRef otherEntity = event.getTarget();
        
        if(!otherEntity.hasComponent(FactionComponent.class)){
            return;
        }
        
        FactionList entityFaction = entity.getComponent(FactionComponent.class).faction;
        FactionList otherEntityFaction = entity.getComponent(FactionComponent.class).faction;
        if(entityFaction != otherEntityFaction){
            entity.send(new CombatEnteredEvent());
            otherEntity.send(new CombatEnteredEvent());
        }
    }

}
