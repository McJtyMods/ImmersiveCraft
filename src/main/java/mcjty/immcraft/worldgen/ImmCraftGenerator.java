package mcjty.immcraft.worldgen;


import mcjty.immcraft.blocks.ModBlocks;
import mcjty.immcraft.blocks.foliage.SticksTE;
import mcjty.immcraft.blocks.generic.GenericBlock;
import mcjty.immcraft.varia.BlockTools;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class ImmCraftGenerator implements IWorldGenerator {
    public static ImmCraftGenerator instance = new ImmCraftGenerator();

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        generateWorld(random, chunkX, chunkZ, world);
    }

    public void generateWorld(Random random, int chunkX, int chunkZ, World world) {
//        addOreSpawn(ModBlocks.resonatingOreBlock, (byte) 0, Blocks.stone, world, random, chunkX * 16, chunkZ * 16,
//                WorldGenConfiguration.minVeinSize, WorldGenConfiguration.maxVeinSize, WorldGenConfiguration.chancesToSpawn, WorldGenConfiguration.minY, WorldGenConfiguration.maxY);
        spawnRubble(random, chunkX, chunkZ, world);
    }

    public static final int ROCK_TRIES = 10;
    public static final int STICK_TRIES = 30;

    private void spawnRubble(Random random, int chunkX, int chunkZ, World world) {

        // Spawn above ground
        for (int i = 0 ; i < random.nextInt(ROCK_TRIES) ; i++) {
            int x = chunkX * 16 + random.nextInt(16);
            int z = chunkZ * 16 + random.nextInt(16);
            BlockPos pos = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).down();
            Block block = world.getBlockState(pos).getBlock();
            if (isRockSpawnable(block)) {
                if (world.isAirBlock(pos.up())) {
                    world.setBlockState(pos.up(), ModBlocks.rockBlock.getDefaultState().withProperty(GenericBlock.FACING_HORIZ, EnumFacing.getHorizontal(random.nextInt(4))), 3);
                }
            }
        }
        for (int i = 0 ; i < random.nextInt(STICK_TRIES) ; i++) {
            int x = chunkX * 16 + random.nextInt(16);
            int z = chunkZ * 16 + random.nextInt(16);
            BlockPos pos = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).down();
            Block block = world.getBlockState(pos).getBlock();
            if (isStickSpawnable(block)) {
                trySpawnSticks(world, pos, random);
            }
        }

        // Spawn in caves
        for (int i = 0 ; i < random.nextInt(ROCK_TRIES) ; i++) {
            int x = chunkX * 16 + random.nextInt(16);
            int z = chunkZ * 16 + random.nextInt(16);
            BlockPos pos = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).down();
            int y = findCaveSpot(world, pos);
            pos = new BlockPos(x, y, z);
            Block block = world.getBlockState(pos).getBlock();
            if (y != -1 && isRockSpawnable(block)) {
                if (world.isAirBlock(pos.up())) {
                    world.setBlockState(pos.up(), ModBlocks.rockBlock.getDefaultState().withProperty(GenericBlock.FACING_HORIZ, EnumFacing.getHorizontal(random.nextInt(4))), 3);
                }
            }
        }
    }

    private void trySpawnSticks(World world, BlockPos pos, Random random) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        y++;
        if (!world.isAirBlock(new BlockPos(x, y, z))) {
            return;
        }
        y++;
        if (!BlockTools.isTopValidAndSolid(world, pos)) {
            return;
        }
        for (int i = 0 ; i < 15 ; i++) {
            if (!world.isAirBlock(new BlockPos(x, y, z))) {
                IBlockState state = world.getBlockState(new BlockPos(x, y, z));
                if (isLeafBlock(state.getBlock(), state)) {
                    world.setBlockState(pos.up(), ModBlocks.sticksBlock.getDefaultState().withProperty(GenericBlock.FACING_HORIZ, EnumFacing.getHorizontal(random.nextInt(4))), 3);
                    SticksTE sticksTE = (SticksTE) world.getTileEntity(pos.up());
                    sticksTE.setSticks(random.nextInt(6)+1);
                }
                return;
            }
            y++;
        }
    }

    private int findCaveSpot(World world, BlockPos pos) {
        boolean air = false;
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        while (y > 1 && !air) {
            if (world.isAirBlock(new BlockPos(x, y, z))) {
                air = true;
            }
            y--;
        }
        if (air) {
            while (y > 1 && air) {
                if (!world.isAirBlock(new BlockPos(x, y, z))) {
                    air = false;
                } else {
                    y--;
                }
            }
            if (!air) {
                return y;
            }
        }
        return -1;
    }

    private boolean isStickSpawnable(Block block) {
        return block == Blocks.DIRT || block == Blocks.GRASS;
    }

    private boolean isLeafBlock(Block block, IBlockState state) {
        return block.getMaterial(state) == Material.LEAVES;
    }

    private boolean isRockSpawnable(Block block) {
        return block == Blocks.DIRT || block == Blocks.GRASS || block == Blocks.STONE;
    }

//    public void addOreSpawn(Block block, byte blockMeta, Block targetBlock,
//                            World world, Random random, int blockXPos, int blockZPos, int minVeinSize, int maxVeinSize, int chancesToSpawn, int minY, int maxY) {
//        WorldGenMinable minable = new WorldGenMinable(block.getStateFromMeta(blockMeta), (minVeinSize - random.nextInt(maxVeinSize - minVeinSize)), targetBlock);
//        for (int i = 0 ; i < chancesToSpawn ; i++) {
//            int posX = blockXPos + random.nextInt(16);
//            int posY = minY + random.nextInt(maxY - minY);
//            int posZ = blockZPos + random.nextInt(16);
//            minable.generate(world, random, posX, posY, posZ);
//        }
//    }
}
