package org.terasology.combatSystem.world;

import static org.terasology.world.chunks.ChunkConstants.CHUNK_REGION;

import org.terasology.registry.CoreRegistry;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.WorldRasterizer;

public class PhysicsRoomRasterizer implements WorldRasterizer{
    
    private static final int ROOM_CHUNK_SIZE = 1;
    public static final int FLOOR_HEIGHT = 1;
    
    private Block grass;

    @Override
    public void initialize() {
        BlockManager manager = CoreRegistry.get(BlockManager.class);
        grass = manager.getBlock("Core:Grass");
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        if (ROOM_CHUNK_SIZE >= chunk.getPosition().getX() && chunk.getPosition().getX() >= -ROOM_CHUNK_SIZE) {
            if (ROOM_CHUNK_SIZE >= chunk.getPosition().getZ() && chunk.getPosition().getZ() >= -ROOM_CHUNK_SIZE) {
                if (chunk.getPosition().getY() == 0) {
                    for (int x = CHUNK_REGION.minX(); x <= CHUNK_REGION.maxX(); x++) {
                        for (int z = CHUNK_REGION.minZ(); z <= CHUNK_REGION.maxZ(); z++) {
                            chunk.setBlock(x, FLOOR_HEIGHT, z, grass);
                            if (isBorder(chunk, x, z)) {
                                for (int y = 1; y < 5; y++) {
                                    chunk.setBlock(x, FLOOR_HEIGHT + y, z, grass);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private boolean isBorder(CoreChunk chunk, int x, int z) {
        boolean xPos = chunk.getPosition().getX() == ROOM_CHUNK_SIZE && x == CHUNK_REGION.maxX();
        boolean xNeg = chunk.getPosition().getX() == -ROOM_CHUNK_SIZE && x == CHUNK_REGION.minX();
        boolean zPos = chunk.getPosition().getZ() == ROOM_CHUNK_SIZE && z == CHUNK_REGION.maxZ();
        boolean zNeg = chunk.getPosition().getZ() == -ROOM_CHUNK_SIZE && z == CHUNK_REGION.minZ();
        return xPos || xNeg || zPos || zNeg;
    }
}
