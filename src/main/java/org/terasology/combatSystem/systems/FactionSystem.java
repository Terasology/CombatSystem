package org.terasology.combatSystem.systems;

import org.terasology.combatSystem.FactionList;
import org.terasology.combatSystem.components.FactionComponent;
import org.terasology.combatSystem.event.CombatEnteredEvent;
import org.terasology.combatSystem.weaponFeatures.components.HurtingComponent;
import org.terasology.combatSystem.weaponFeatures.events.HurtEvent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;

@RegisterSystem
public class FactionSystem extends BaseComponentSystem{
    
    @ReceiveEvent(components = {FactionComponent.class, HurtingComponent.class})
    public void combatEntered(HurtEvent event, EntityRef entity){
        HurtingComponent hurting = entity.getComponent(HurtingComponent.class);
        if(!hurting.canHurt){
            return;
        }
        
        EntityRef otherEntity = event.getTarget();
        
        if(!otherEntity.hasComponent(FactionComponent.class)){
            return;
        }
        
        FactionList entityFaction = entity.getComponent(FactionComponent.class).faction;
        FactionList otherEntityFaction = entity.getComponent(FactionComponent.class).faction;
        if(entityFaction == otherEntityFaction){
            entity.send(new CombatEnteredEvent());
            otherEntity.send(new CombatEnteredEvent());
        }
    }

}
