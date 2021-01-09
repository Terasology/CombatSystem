package org.terasology.combatSystem.world;

import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.terasology.engine.SimpleUri;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.registry.In;
import org.terasology.world.generation.BaseFacetedWorldGenerator;
import org.terasology.world.generation.WorldBuilder;
import org.terasology.world.generator.RegisterWorldGenerator;
import org.terasology.world.generator.plugin.WorldGeneratorPluginLibrary;

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
    public Vector3fc getSpawnPosition(EntityRef entity) {
        return new Vector3f(0, PhysicsRoomRasterizer.FLOOR_HEIGHT + 1, 0);
    }
}
