// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.combatSystem.world;

import org.terasology.engine.core.SimpleUri;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.registry.In;
import org.terasology.engine.world.generation.BaseFacetedWorldGenerator;
import org.terasology.engine.world.generation.WorldBuilder;
import org.terasology.engine.world.generator.RegisterWorldGenerator;
import org.terasology.engine.world.generator.plugin.WorldGeneratorPluginLibrary;
import org.terasology.math.geom.Vector3f;

@RegisterWorldGenerator(id = "PhysicsRoomWorldGenerator", displayName = "Physics Room World Generator")
public class PhysicsRoomWorldGenerator extends BaseFacetedWorldGenerator {

    @In
    private WorldGeneratorPluginLibrary worldGeneratorPluginLibrary;

    public PhysicsRoomWorldGenerator(SimpleUri uri) {
        super(uri);
    }

    @Override
    protected WorldBuilder createWorld() {
        return new WorldBuilder(worldGeneratorPluginLibrary).setSeaLevel(0).addRasterizer(new PhysicsRoomRasterizer());
    }

    @Override
    public Vector3f getSpawnPosition(EntityRef entity) {
        return new Vector3f(0, PhysicsRoomRasterizer.FLOOR_HEIGHT + 1, 0);
    }
}
