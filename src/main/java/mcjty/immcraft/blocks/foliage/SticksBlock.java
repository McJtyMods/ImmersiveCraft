package mcjty.immcraft.blocks.foliage;


import mcjty.immcraft.api.helpers.InventoryHelper;
import mcjty.immcraft.blocks.generic.GenericBlockWithTE;
import mcjty.immcraft.varia.BlockTools;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static mcjty.immcraft.blocks.foliage.EnumAmount.*;
import static net.minecraft.util.EnumFacing.*;

public class SticksBlock extends GenericBlockWithTE<SticksTE> {

    public static final PropertyAmount AMOUNT = PropertyAmount.create("amount", (Collection<EnumAmount>) Arrays.stream(EnumAmount.values()).collect(Collectors.toList()));
    public static final PropertyBool BURNING = PropertyBool.create("burning");

    public static final AxisAlignedBB AABB = new AxisAlignedBB(.1f, 0, .1f, .9f, .4f, .9f);

    public SticksBlock() {
        super(Material.CIRCUITS, "sticks", SticksTE.class, true);
        setHardness(0.0f);
        setSoundType(SoundType.WOOD);
        setTickRandomly(true);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        super.addProbeInfo(mode, probeInfo, player, world, blockState, data);
        TileEntity te = world.getTileEntity(data.getPos());
        if (te instanceof SticksTE) {
            SticksTE sticksTE = (SticksTE) te;
            probeInfo.text(TextFormatting.GREEN + "Sticks: " + sticksTE.getSticks());
        }
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        SticksTE sticksTE = (SticksTE) accessor.getTileEntity();
        currenttip.add(TextFormatting.GREEN + "Sticks: " + sticksTE.getSticks());
        return currenttip;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }


    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
    }

    @Override
    protected void clOnNeighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
        super.clOnNeighborChanged(state, worldIn, pos, blockIn);
        if (!canBlockStay(worldIn, pos)) {
            dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        }
    }

    private boolean canBlockStay(World worldIn, BlockPos pos) {
        return BlockTools.isTopValidAndSolid(worldIn, pos.down());
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof SticksTE) {
            SticksTE sticksTE = (SticksTE) te;
            Boolean burning = sticksTE.getBurnTime() > 0;
            state = state.withProperty(BURNING, burning);
            int cnt = sticksTE.getSticks();
            if (cnt <= 3) {
                return state.withProperty(AMOUNT, SINGLE);
            } else if (cnt <= 6) {
                return state.withProperty(AMOUNT, DOUBLE);
            } else if (cnt <= 9) {
                return state.withProperty(AMOUNT, TRIPPLE);
            } else {
                return state.withProperty(AMOUNT, ALL);
            }
        } else {
            return state;
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING_HORIZ, AMOUNT, BURNING);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof SticksTE) {
            InventoryHelper.spawnItemStack(world, pos, new ItemStack(Items.STICK, ((SticksTE) te).getSticks()));
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (isBurning(world, pos)) {
            return 14;
        } else {
            return 0;
        }
    }

    @Override
    public boolean isBurning(IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof SticksTE) {
            return ((SticksTE) te).getBurnTime() > 0;
        }
        return false;
    }

    /**
     * How many world ticks before ticking
     */
    @Override
    public int tickRate(World world) {
        return 30;
    }


    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        if (!isBurning(world, pos)) {
            return;
        }
        if (world.getGameRules().getBoolean("doFireTick")) {
            world.scheduleUpdate(pos, this, this.tickRate(world) + random.nextInt(10));

            boolean flag1 = world.isBlockinHighHumidity(pos);
            byte b0 = 0;

            if (flag1) {
                b0 = -50;
            }

//            this.tryCatchFire(world, pos.east(), 600 + b0, random, WEST);
//            this.tryCatchFire(world, pos.west(), 600 + b0, random, EAST);
//            this.tryCatchFire(world, pos.down(), 250 + b0, random, UP);
//            this.tryCatchFire(world, pos.up(), 250 + b0, random, DOWN);
//            this.tryCatchFire(world, pos.north(), 600 + b0, random, SOUTH);
//            this.tryCatchFire(world, pos.south(), 600 + b0, random, NORTH);
//
//            int x = pos.getX();
//            int y = pos.getY();
//            int z = pos.getZ();
//            for (int i1 = x - 1; i1 <= x + 1; ++i1) {
//                for (int j1 = z - 1; j1 <= z + 1; ++j1) {
//                    for (int k1 = y - 1; k1 <= y + 4; ++k1) {
//                        if (i1 != x || k1 != y || j1 != z) {
//                            int l1 = 100;
//
//                            if (k1 > y + 1) {
//                                l1 += (k1 - (y + 1)) * 100;
//                            }
//
//                            int i2 = this.getChanceOfNeighborsEncouragingFire(world, new BlockPos(i1, k1, j1));
//
//                            if (i2 > 0) {
//                                int j2 = (i2 + 40 + world.getDifficulty().getDifficultyId() * 7) / (10 + 30);
//
//                                if (flag1) {
//                                    j2 /= 2;
//                                }
//
//
//                                if (j2 > 0 && random.nextInt(l1) <= j2 && (!world.isRaining() ||
//                                        !world.isRainingAt(new BlockPos(i1, k1, j1))) &&
//                                        !world.isRainingAt(new BlockPos(i1 - 1, k1, z)) &&
//                                        !world.isRainingAt(new BlockPos(i1 + 1, k1, j1)) &&
//                                        !world.isRainingAt(new BlockPos(i1, k1, j1 - 1)) &&
//                                        !world.isRainingAt(new BlockPos(i1, k1, j1 + 1))) {
//                                    int k2 = 10 + random.nextInt(5) / 4;
//
//                                    if (k2 > 15) {
//                                        k2 = 15;
//                                    }
//
//                                    world.setBlockState(new BlockPos(i1, k1, j1), Blocks.FIRE.getStateFromMeta(k2), 3);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
        }
    }

    private int getChanceOfNeighborsEncouragingFire(World world, BlockPos pos) {
        if (!world.isAirBlock(pos)) {
            return 0;
        } else {
            int i = 0;

            for (EnumFacing enumfacing : EnumFacing.values()) {
                i = Math.max(world.getBlockState(pos.offset(enumfacing)).getBlock().getFlammability(world, pos.offset(enumfacing), enumfacing.getOpposite()), i);
            }

            return i;
        }
    }


    private void tryCatchFire(World world, BlockPos pos, int chance, Random random, EnumFacing face) {
        int j1 = world.getBlockState(pos).getBlock().getFlammability(world, pos, face);

        if (random.nextInt(chance) < j1) {
            boolean flag = world.getBlockState(pos).getBlock() == Blocks.TNT;

            if (random.nextInt(10 + 10) < 5 && !world.isRainingAt(pos)) {
                int k1 = 10 + random.nextInt(5) / 4;

                if (k1 > 15) {
                    k1 = 15;
                }

                world.setBlockState(pos, Blocks.FIRE.getStateFromMeta(k1), 3);
            } else {
                world.setBlockToAir(pos);
            }

            if (flag) {
                Blocks.TNT.onBlockDestroyedByPlayer(world, pos, world.getBlockState(pos));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random random) {
        if (!isBurning(world, pos)) {
            return;
        }

        if (random.nextInt(24) == 0) {
            world.playSound((pos.getX() + 0.5F), (pos.getY() + 0.5F), (pos.getZ() + 0.5F), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
        }

        int l;
        float f;
        float f1;
        float f2;

        if (!state.isSideSolid(world, pos.down(), EnumFacing.UP) && !Blocks.FIRE.canCatchFire(world, pos.down(), UP)) {
            if (Blocks.FIRE.canCatchFire(world, pos.west(), EAST)) {
                for (l = 0; l < 2; ++l) {
                    f = pos.getX() + random.nextFloat() * 0.1F;
                    f1 = pos.getY() + random.nextFloat();
                    f2 = pos.getZ() + random.nextFloat();
                    world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, f, f1, f2, 0.0D, 0.0D, 0.0D);
                }
            }

            if (Blocks.FIRE.canCatchFire(world, pos.east(), WEST)) {
                for (l = 0; l < 2; ++l) {
                    f = (pos.getX() + 1) - random.nextFloat() * 0.1F;
                    f1 = pos.getY() + random.nextFloat();
                    f2 = pos.getZ() + random.nextFloat();
                    world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, f, f1, f2, 0.0D, 0.0D, 0.0D);
                }
            }

            if (Blocks.FIRE.canCatchFire(world, pos.north(), SOUTH)) {
                for (l = 0; l < 2; ++l) {
                    f = pos.getX() + random.nextFloat();
                    f1 = pos.getY() + random.nextFloat();
                    f2 = pos.getZ() + random.nextFloat() * 0.1F;
                    world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, f, f1, f2, 0.0D, 0.0D, 0.0D);
                }
            }

            if (Blocks.FIRE.canCatchFire(world, pos.south(), NORTH)) {
                for (l = 0; l < 2; ++l) {
                    f = pos.getX() + random.nextFloat();
                    f1 = pos.getY() + random.nextFloat();
                    f2 = (pos.getZ() + 1) - random.nextFloat() * 0.1F;
                    world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, f, f1, f2, 0.0D, 0.0D, 0.0D);
                }
            }

            if (Blocks.FIRE.canCatchFire(world, pos.up(), DOWN)) {
                for (l = 0; l < 2; ++l) {
                    f = pos.getX() + random.nextFloat();
                    f1 = (pos.getY() + 1) - random.nextFloat() * 0.1F;
                    f2 = pos.getZ() + random.nextFloat();
                    world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, f, f1, f2, 0.0D, 0.0D, 0.0D);
                }
            }
        } else {
            for (l = 0; l < 3; ++l) {
                f = pos.getX() + random.nextFloat();
                f1 = pos.getY() + random.nextFloat() * 0.5F + 0.5F;
                f2 = pos.getZ() + random.nextFloat();
                world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, f, f1, f2, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
