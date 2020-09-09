// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.world;

import com.google.common.collect.Lists;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.prefab.Prefab;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.physics.StandardCollisionGroup;
import org.terasology.engine.physics.components.RigidBodyComponent;
import org.terasology.engine.physics.components.shapes.BoxShapeComponent;
import org.terasology.engine.registry.In;
import org.terasology.engine.rendering.logic.SkeletalMeshComponent;
import org.terasology.engine.utilities.Assets;
import org.terasology.math.geom.Vector3f;

@RegisterSystem
public class NPCSpawnSystem extends BaseComponentSystem {

    @In
    private EntityManager entityManager;

    private EntityRef npc1 = EntityRef.NULL;

    private Prefab staticCharacterPrefab;

    @Override
    public void initialise() {
        staticCharacterPrefab = Assets.getPrefab("CombatSystem:staticCharacter").get();
    }

    @Override
    public void postBegin() {
        npc1 = entityManager.create(staticCharacterPrefab, new Vector3f(0, 1.5f, 5));

        BoxShapeComponent boxShape = new BoxShapeComponent();
        SkeletalMeshComponent skeletalMesh = npc1.getComponent(SkeletalMeshComponent.class);
        boxShape.extents = skeletalMesh.mesh.getStaticAabb().getExtents().scale(2.0f);

        RigidBodyComponent rigidBody = npc1.getComponent(RigidBodyComponent.class);
        rigidBody.collidesWith = Lists.newArrayList(StandardCollisionGroup.DEFAULT, StandardCollisionGroup.CHARACTER,
                StandardCollisionGroup.WORLD);

        npc1.addOrSaveComponent(boxShape);
        npc1.saveComponent(rigidBody);
    }

}
