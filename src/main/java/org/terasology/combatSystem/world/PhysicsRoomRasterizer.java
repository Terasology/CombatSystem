package org.terasology.combatSystem.world;

import static org.terasology.world.chunks.ChunkConstants.CHUNK_REGION;

import java.util.List;
import java.util.Map;

import org.terasology.core.world.generator.rasterizers.FloraType;
import org.terasology.core.world.generator.trees.TreeGenerator;
import org.terasology.core.world.generator.trees.Trees;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.utilities.procedural.WhiteNoise;
import org.terasology.utilities.random.FastRandom;
import org.terasology.utilities.random.Random;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.WorldRasterizer;
import org.terasology.world.liquid.LiquidData;
import org.terasology.world.liquid.LiquidType;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

public class PhysicsRoomRasterizer implements WorldRasterizer{
    
    private static final int ROOM_CHUNK_SIZE = 1;
    public static final int FLOOR_HEIGHT = 1;
    
    private Block grass;
    private Block water;
    
    BlockManager manager;
    
    private final Map<FloraType, List<Block>> flora = Maps.newEnumMap(FloraType.class);

    @Override
    public void initialize() {
        manager = CoreRegistry.get(BlockManager.class);
        grass = manager.getBlock("Core:Grass");
        water = manager.getBlock("Core:water");
        
        flora.put(FloraType.GRASS, ImmutableList.<Block>of(
                manager.getBlock("core:TallGrass1"),
                manager.getBlock("core:TallGrass2"),
                manager.getBlock("core:TallGrass3")));
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        LiquidData waterLiquid = new LiquidData(LiquidType.WATER, LiquidData.MAX_LIQUID_DEPTH);
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
                            if(x == 10 && z == 10){
                                if(chunk.getPosition().getX() == ROOM_CHUNK_SIZE 
                                    && chunk.getPosition().getZ() == ROOM_CHUNK_SIZE){
                                    for(int width = 0; width < 10; width++){
                                        for(int y = 1; y < 15; y++) {
                                            chunk.setBlock(x + width, FLOOR_HEIGHT + y, z, grass);
                                        }
                                    }
                                }
                                if(chunk.getPosition().getX() == ROOM_CHUNK_SIZE 
                                        && chunk.getPosition().getZ() == 0){
                                    for(int width = 0; width < 10; width++){
                                        for(int y = 1; y < 15; y++) {
                                            chunk.setBlock(x + width, FLOOR_HEIGHT + y, z + width, grass);
                                        }
                                    }
                                }
                                if(chunk.getPosition().getX() == ROOM_CHUNK_SIZE 
                                        && chunk.getPosition().getZ() == -ROOM_CHUNK_SIZE){
                                    int maxHeight = 11;
                                    for(int length = 0; length < 10; length++){
                                        for(int width = 0; width < 10; width++){
                                            chunk.setBlock(x + width, FLOOR_HEIGHT + maxHeight, z + length, grass);
                                        }
                                        maxHeight--;
                                    }
                                }
                                if(chunk.getPosition().getX() == 0 
                                        && chunk.getPosition().getZ() == ROOM_CHUNK_SIZE){
                                    for(int length = 0; length < 10; length++){
                                        for(int width = 0; width < 10; width++){
                                            for(int height =1; height < 11; height++){
                                                chunk.setBlock(x + width, FLOOR_HEIGHT + height, z + length, water);
                                                chunk.setLiquid(x + width, FLOOR_HEIGHT + height, z + length, waterLiquid);
                                            }
                                        }
                                    }
                                }
                                if(chunk.getPosition().getX() == 0 
                                        && chunk.getPosition().getZ() == 0){
                                    
                                }
                                if(chunk.getPosition().getX() == 0 
                                        && chunk.getPosition().getZ() == -ROOM_CHUNK_SIZE){
                                    TreeGenerator treeGen = Trees.oakTree();
                                    Vector3i pos = new Vector3i(x, FLOOR_HEIGHT, z);
                                    int seed = pos.hashCode();
                                    Random random = new FastRandom(seed);
                                    treeGen.generate(manager, chunk, random, x, FLOOR_HEIGHT + 1, z);
                                }
                                if(chunk.getPosition().getX() == -ROOM_CHUNK_SIZE 
                                        && chunk.getPosition().getZ() == ROOM_CHUNK_SIZE){
                                    List<Block> list = flora.get(FloraType.GRASS);
                                    WhiteNoise noise = new WhiteNoise(chunk.getPosition().hashCode());
                                    for(int length = 0; length < 10; length++){
                                        for(int width = 0; width < 10; width++){
                                            int blockIdx = Math.abs(noise.intNoise(x + width, FLOOR_HEIGHT + 1, z + length)) % list.size();
                                            Block block = list.get(blockIdx);
                                            chunk.setBlock(x + width, FLOOR_HEIGHT + 1, z + length, block);
                                        }
                                    }
                                }
                                if(chunk.getPosition().getX() == -ROOM_CHUNK_SIZE 
                                        && chunk.getPosition().getZ() == 0){
                                    
                                }
                                if(chunk.getPosition().getX() == -ROOM_CHUNK_SIZE 
                                        && chunk.getPosition().getZ() == -ROOM_CHUNK_SIZE){
                                    
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
