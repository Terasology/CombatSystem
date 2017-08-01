package org.terasology.combatSystem.test;

import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.inventory.InventoryComponent;
import org.terasology.logic.inventory.events.GiveItemEvent;
import org.terasology.logic.players.event.OnPlayerSpawnedEvent;
import org.terasology.registry.In;
import org.terasology.world.block.BlockManager;
import org.terasology.world.block.family.BlockFamily;
import org.terasology.world.block.items.BlockItemFactory;

/**
 * implements a basic inventory at start of game for testing weapons.
 */
@RegisterSystem
public class DemoSystem extends BaseComponentSystem{
    
    @In
    EntityManager entityManager;
    @In
    BlockManager blockManager;
    
    @ReceiveEvent( components = {InventoryComponent.class})
    public void givingWeaponsToPlayers(OnPlayerSpawnedEvent event, EntityRef player){
        // 0. bow
        EntityRef bow = entityManager.create("CombatSystem:bow");
        
        bow.send(new GiveItemEvent(player));
        
        for(int i=0; i<16; i++){
         // 1. Stick Arrow
            EntityRef stickArrow = entityManager.create("CombatSystem:stickArrowItem");
            
            stickArrow.send(new GiveItemEvent(player));
            
            // 2. Bounce Arrow
            EntityRef bounceArrow = entityManager.create("CombatSystem:bounceArrowItem");
            
            bounceArrow.send(new GiveItemEvent(player));
            
            // 3. Explode Arrow
            EntityRef explodeArrow = entityManager.create("CombatSystem:explodeArrowItem");
            
            explodeArrow.send(new GiveItemEvent(player));
        }
        
        // 4. Sword
        EntityRef sword = entityManager.create("CombatSystem:sword");
        
        sword.send(new GiveItemEvent(player));
        
        // 5. war axe
        EntityRef warAxe = entityManager.create("CombatSystem:waraxe");
        
        warAxe.send(new GiveItemEvent(player));
        
        // 6. Staff
        EntityRef staff = entityManager.create("CombatSystem:staff");
        
        staff.send(new GiveItemEvent(player));
        
        // 7. Spear
        EntityRef spear = entityManager.create("CombatSystem:spearItem");
        
        spear.send(new GiveItemEvent(player));
        
        // 8. Fire ball Launcher
        BlockFamily fireBallLauncherItem = blockManager.getBlockFamily("fireBallMine");
        if (fireBallLauncherItem != null) {
            BlockItemFactory blockItemFactory = new BlockItemFactory(entityManager);
            EntityRef blockItem = blockItemFactory.newInstance(fireBallLauncherItem);
            
            blockItem.send(new GiveItemEvent(player));
        }
        
        // 9. Exploding mine
        BlockFamily explodingMineItem = blockManager.getBlockFamily("explodeMine");
        if (explodingMineItem != null) {
            BlockItemFactory blockItemFactory = new BlockItemFactory(entityManager);
            EntityRef blockItem = blockItemFactory.newInstance(explodingMineItem);
            
            blockItem.send(new GiveItemEvent(player));
        }
    }
}
