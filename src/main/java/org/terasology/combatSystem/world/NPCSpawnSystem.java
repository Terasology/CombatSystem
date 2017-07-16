package org.terasology.combatSystem.world;

import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.math.geom.Vector3f;
import org.terasology.physics.CollisionGroup;
import org.terasology.physics.StandardCollisionGroup;
import org.terasology.physics.components.RigidBodyComponent;
import org.terasology.physics.shapes.BoxShapeComponent;
import org.terasology.registry.In;
import org.terasology.rendering.logic.SkeletalMeshComponent;
import org.terasology.utilities.Assets;

import com.google.common.collect.Lists;

@RegisterSystem
public class NPCSpawnSystem extends BaseComponentSystem{
    
    @In
    private EntityManager entityManager;
    
    private EntityRef npc1 = EntityRef.NULL;
    
    private Prefab staticCharacterPrefab;
    private Prefab trapTest;
    
    @Override
    public void initialise(){
        staticCharacterPrefab = Assets.getPrefab("CombatSystem:staticCharacter").get();
        trapTest = Assets.getPrefab("CombatSystem:explodeMineTrap").get();
    }
    
    @Override
    public void postBegin() {
        npc1 = entityManager.create(staticCharacterPrefab, new Vector3f(0,1.5f,5));
        
        BoxShapeComponent boxShape = new BoxShapeComponent();
        SkeletalMeshComponent skeletalMesh = npc1.getComponent(SkeletalMeshComponent.class);
        boxShape.extents = skeletalMesh.mesh.getStaticAabb().getExtents().scale(2.0f);
        
        RigidBodyComponent rigidBody = npc1.getComponent(RigidBodyComponent.class);
        rigidBody.collidesWith = Lists.<CollisionGroup>newArrayList(StandardCollisionGroup.DEFAULT, StandardCollisionGroup.CHARACTER, StandardCollisionGroup.WORLD);
        
        npc1.addOrSaveComponent(boxShape);
        npc1.saveComponent(rigidBody);
        
        entityManager.create(trapTest, new Vector3f(10, 1.5f, 5));
    }

}
