package mcjty.immcraft.worldgen;


import mcjty.immcraft.blocks.ModBlocks;
import mcjty.immcraft.blocks.foliage.SticksTE;
import mcjty.immcraft.config.GeneralConfiguration;
import mcjty.immcraft.varia.BlockTools;
import mcjty.lib.blocks.BaseBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
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

    private void spawnRubble(Random random, int chunkX, int chunkZ, World world) {

        // Spawn above ground
        if (GeneralConfiguration.worldgenRockAttemptsPerChunk > 0) {
            for (int i = 0; i < random.nextInt(GeneralConfiguration.worldgenRockAttemptsPerChunk); i++) {
                int x = chunkX * 16 + random.nextInt(16) + 8;
                int z = chunkZ * 16 + random.nextInt(16) + 8;
                BlockPos pos = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).down();
                IBlockState state = world.getBlockState(pos);
                if (isRockSpawnable(state)) {
                    if (world.isAirBlock(pos.up())) {
                        world.setBlockState(pos.up(), ModBlocks.rockBlock.getDefaultState().withProperty(BaseBlock.FACING_HORIZ, EnumFacing.getHorizontal(random.nextInt(4))), 3);
                    }
                }
            }
        }
        if (GeneralConfiguration.worldgenStickAttemptsPerChunk > 0) {
            for (int i = 0; i < random.nextInt(GeneralConfiguration.worldgenStickAttemptsPerChunk); i++) {
                int x = chunkX * 16 + random.nextInt(16) + 8;
                int z = chunkZ * 16 + random.nextInt(16) + 8;
                BlockPos pos = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).down();
                IBlockState state = world.getBlockState(pos);
                if (isStickSpawnable(state)) {
                    trySpawnSticks(world, pos, random);
                }
            }
        }

        // Spawn in caves
        if (GeneralConfiguration.worldgenRockAttemptsPerChunk > 0) {
            for (int i = 0; i < random.nextInt(GeneralConfiguration.worldgenRockAttemptsPerChunk); i++) {
                int x = chunkX * 16 + random.nextInt(16) + 8;
                int z = chunkZ * 16 + random.nextInt(16) + 8;
                BlockPos pos = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).down();
                int y = findCaveSpot(world, pos);
                pos = new BlockPos(x, y, z);
                IBlockState state = world.getBlockState(pos);
                if (y != -1 && isRockSpawnable(state)) {
                    if (world.isAirBlock(pos.up())) {
                        world.setBlockState(pos.up(), ModBlocks.rockBlock.getDefaultState().withProperty(BaseBlock.FACING_HORIZ, EnumFacing.getHorizontal(random.nextInt(4))), 3);
                    }
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
                    world.setBlockState(pos.up(), ModBlocks.sticksBlock.getDefaultState().withProperty(BaseBlock.FACING_HORIZ, EnumFacing.getHorizontal(random.nextInt(4))), 3);
                    TileEntity te = world.getTileEntity(pos.up());
                    if (te instanceof SticksTE) {
                        SticksTE sticksTE = (SticksTE) te;
                        sticksTE.setSticks(random.nextInt(6) + 1);
                    }
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

    private boolean isStickSpawnable(IBlockState state) {
        return GeneralConfiguration.getValidBlocksForSticks().contains(state);
    }

    private boolean isLeafBlock(Block block, IBlockState state) {
        return block.getMaterial(state) == Material.LEAVES;
    }

    private boolean isRockSpawnable(IBlockState state) {
        return GeneralConfiguration.getValidBlocksForRocks().contains(state);
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
