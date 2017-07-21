package org.terasology.combatSystem.test;

import org.terasology.combatSystem.weaponFeatures.components.LaunchEntityComponent;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.prefab.PrefabManager;
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
        PrefabManager prefabManager = entityManager.getPrefabManager();
        
        // 1. Stick Arrows
        EntityRef stickBow = entityManager.create("CombatSystem:bow");
        
        stickBow.send(new GiveItemEvent(player));
        
        // 2. Bounce Arrows
        EntityRef bounceBow = entityManager.create("CombatSystem:bow");
        
        LaunchEntityComponent launchEntity2 = bounceBow.getComponent(LaunchEntityComponent.class);
        if(launchEntity2 == null){
            launchEntity2 = new LaunchEntityComponent();
        }
        launchEntity2.launchEntityPrefab = prefabManager.getPrefab("CombatSystem:bounceArrow");
        bounceBow.addOrSaveComponent(launchEntity2);
        
        EntityRef testItem = entityManager.create("CombatSystem:sword");
        
        testItem.send(new GiveItemEvent(bounceBow));
        
        bounceBow.send(new GiveItemEvent(player));
        
        // 3. Explode Arrows
        EntityRef explodeBow = entityManager.create("CombatSystem:bow");
        
        LaunchEntityComponent launchEntity4 = explodeBow.getComponent(LaunchEntityComponent.class);
        if(launchEntity4 == null){
            launchEntity4 = new LaunchEntityComponent();
        }
        launchEntity4.launchEntityPrefab = prefabManager.getPrefab("CombatSystem:explodeArrow");
        explodeBow.addOrSaveComponent(launchEntity4);
        
        explodeBow.send(new GiveItemEvent(player));
        
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
