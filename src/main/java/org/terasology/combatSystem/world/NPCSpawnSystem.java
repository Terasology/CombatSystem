package org.terasology.combatSystem.world;

import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.location.LocationComponent;
import org.terasology.math.geom.Vector3f;
import org.terasology.registry.In;
import org.terasology.utilities.Assets;
import org.terasology.world.RelevanceRegionComponent;

@RegisterSystem(RegisterMode.AUTHORITY)
public class NPCSpawnSystem extends BaseComponentSystem{
    
    @In
    private EntityManager entityManager;
    
    private EntityRef npc1 = EntityRef.NULL;
    
    private Prefab staticCharacterPrefab;
    
    @Override
    public void initialise(){
        staticCharacterPrefab = Assets.getPrefab("CombatSystem:staticCharacter").get();
    }
    
    @Override
    public void postBegin() {
        npc1 = entityManager.create(staticCharacterPrefab, Vector3f.zero());
    }

}
