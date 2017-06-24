package org.terasology.combatSystem.test;

import org.terasology.combatSystem.weaponFeatures.components.AttackerComponent;
import org.terasology.combatSystem.weaponFeatures.components.LaunchEntityComponent;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.prefab.PrefabManager;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.inventory.InventoryComponent;
import org.terasology.logic.inventory.InventoryManager;
import org.terasology.logic.players.event.OnPlayerSpawnedEvent;
import org.terasology.registry.In;

/**
 * implements a basic inventory at start of game for testing weapons.
 */
@RegisterSystem
public class DemoSystem extends BaseComponentSystem{
    
    @In
    EntityManager entityManager;
    @In
    InventoryManager inventoryManager;
    
    @ReceiveEvent( components = {InventoryComponent.class})
    public void givingWeaponsToPlayers(OnPlayerSpawnedEvent event, EntityRef player){
        PrefabManager prefabManager = entityManager.getPrefabManager();
        
        // 1. Stick Arrows
        EntityRef stickBow = entityManager.create("CombatSystem:bow");
        
        LaunchEntityComponent launchEntity = stickBow.getComponent(LaunchEntityComponent.class);
        if(launchEntity == null){
            launchEntity = new LaunchEntityComponent();
        }
        launchEntity.launchEntityPrefab = prefabManager.getPrefab("CombatSystem:stickArrow");
        stickBow.addOrSaveComponent(launchEntity);
        
        AttackerComponent stickShooter = stickBow.getComponent(AttackerComponent.class);
        if(stickShooter == null){
            stickShooter = new AttackerComponent(player);
        }
        stickShooter.attacker = player;
        
        stickBow.addOrSaveComponent(stickShooter);
        
        inventoryManager.giveItem(player, EntityRef.NULL, stickBow);
        
        // 2. Bounce Arrows
        EntityRef bounceBow = entityManager.create("CombatSystem:bow");
        
        LaunchEntityComponent launchEntity2 = bounceBow.getComponent(LaunchEntityComponent.class);
        if(launchEntity2 == null){
            launchEntity2 = new LaunchEntityComponent();
        }
        launchEntity2.launchEntityPrefab = prefabManager.getPrefab("CombatSystem:bounceArrow");
        bounceBow.addOrSaveComponent(launchEntity2);
        
        AttackerComponent shooter2 = bounceBow.getComponent(AttackerComponent.class);
        if(shooter2 == null){
            shooter2 = new AttackerComponent(player);
        }
        shooter2.attacker = player;
        
        bounceBow.addOrSaveComponent(shooter2);
        
        inventoryManager.giveItem(player, EntityRef.NULL, bounceBow);
        
        // 3. Explode Arrows
        EntityRef explodeBow = entityManager.create("CombatSystem:bow");
        
        LaunchEntityComponent launchEntity4 = explodeBow.getComponent(LaunchEntityComponent.class);
        if(launchEntity4 == null){
            launchEntity4 = new LaunchEntityComponent();
        }
        launchEntity4.launchEntityPrefab = prefabManager.getPrefab("CombatSystem:explodeArrow");
        explodeBow.addOrSaveComponent(launchEntity4);
        
        AttackerComponent shooter4 = explodeBow.getComponent(AttackerComponent.class);
        if(shooter4 == null){
            shooter4 = new AttackerComponent(player);
        }
        shooter4.attacker = player;
        
        explodeBow.addOrSaveComponent(shooter4);
        
        inventoryManager.giveItem(player, EntityRef.NULL, explodeBow);
        
        // 4. Sword
        EntityRef sword = entityManager.create("CombatSystem:sword");
        
        inventoryManager.giveItem(player, EntityRef.NULL, sword);
        
        // 4. war axe
        EntityRef warAxe = entityManager.create("CombatSystem:waraxe");
        
        inventoryManager.giveItem(player, EntityRef.NULL, warAxe);
        
        // 4. Staff
        EntityRef staff = entityManager.create("CombatSystem:staff");
        
        AttackerComponent shooter5 = explodeBow.getComponent(AttackerComponent.class);
        if(shooter5 == null){
            shooter5 = new AttackerComponent(player);
        }
        shooter5.attacker = player;
        
        staff.addOrSaveComponent(shooter5);
        
        inventoryManager.giveItem(player, EntityRef.NULL, staff);
    }
}
