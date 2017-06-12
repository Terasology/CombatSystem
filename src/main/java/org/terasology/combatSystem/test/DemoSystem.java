package org.terasology.combatSystem.test;

import org.terasology.combatSystem.weaponFeatures.components.LaunchEntityComponent;
import org.terasology.combatSystem.weaponFeatures.components.ShooterComponent;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.prefab.PrefabManager;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.inventory.InventoryComponent;
import org.terasology.logic.inventory.InventoryManager;
import org.terasology.logic.players.event.OnPlayerSpawnedEvent;
import org.terasology.registry.In;

@RegisterSystem(RegisterMode.AUTHORITY)
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
        
        ShooterComponent stickShooter = stickBow.getComponent(ShooterComponent.class);
        if(stickShooter == null){
            stickShooter = new ShooterComponent(player);
        }
        stickShooter.shooter = player;
        
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
        
        ShooterComponent shooter2 = bounceBow.getComponent(ShooterComponent.class);
        if(shooter2 == null){
            shooter2 = new ShooterComponent(player);
        }
        shooter2.shooter = player;
        
        bounceBow.addOrSaveComponent(shooter2);
        
        inventoryManager.giveItem(player, EntityRef.NULL, bounceBow);
        
        // 3. Stick After Bounce Arrows
        EntityRef stickAfterBounceBow = entityManager.create("CombatSystem:bow");
        
        LaunchEntityComponent launchEntity3 = stickAfterBounceBow.getComponent(LaunchEntityComponent.class);
        if(launchEntity3 == null){
            launchEntity3 = new LaunchEntityComponent();
        }
        launchEntity3.launchEntityPrefab = prefabManager.getPrefab("CombatSystem:stickAfterBounceArrow");
        stickAfterBounceBow.addOrSaveComponent(launchEntity3);
        
        ShooterComponent shooter3 = stickAfterBounceBow.getComponent(ShooterComponent.class);
        if(shooter3 == null){
            shooter3 = new ShooterComponent(player);
        }
        shooter3.shooter = player;
        
        stickAfterBounceBow.addOrSaveComponent(shooter3);
        
        inventoryManager.giveItem(player, EntityRef.NULL, stickAfterBounceBow);
        
        // 4. Explode Arrows
        EntityRef explodeBow = entityManager.create("CombatSystem:bow");
        
        LaunchEntityComponent launchEntity4 = explodeBow.getComponent(LaunchEntityComponent.class);
        if(launchEntity4 == null){
            launchEntity4 = new LaunchEntityComponent();
        }
        launchEntity4.launchEntityPrefab = prefabManager.getPrefab("CombatSystem:explodeArrow");
        explodeBow.addOrSaveComponent(launchEntity4);
        
        ShooterComponent shooter4 = explodeBow.getComponent(ShooterComponent.class);
        if(shooter4 == null){
            shooter4 = new ShooterComponent(player);
        }
        shooter4.shooter = player;
        
        explodeBow.addOrSaveComponent(shooter4);
        
        inventoryManager.giveItem(player, EntityRef.NULL, explodeBow);
    }
}
