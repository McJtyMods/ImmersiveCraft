package mcjty.immcraft.varia;

import mcjty.immcraft.blocks.generic.GenericBlock;
import mcjty.immcraft.blocks.generic.GenericInventoryTE;
import mcjty.immcraft.blocks.generic.GenericTE;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.Random;

import static net.minecraft.util.EnumFacing.*;

public class BlockTools {
    private static final Random random = new Random();

    // Use these flags if you want to support a single redstone signal and 3 bits for orientation.
    public static final int MASK_ORIENTATION = 0x7;
    public static final int MASK_REDSTONE = 0x8;

    // Use these flags if you want to support both redstone in and output and only 2 bits for orientation.
    public static final int MASK_ORIENTATION_HORIZONTAL = 0x3;          // Only two bits for orientation
    public static final int MASK_REDSTONE_IN = 0x8;                     // Redstone in
    public static final int MASK_REDSTONE_OUT = 0x4;                    // Redstone out
    public static final int MASK_STATE = 0xc;                           // If redstone is not used: state

    public static EnumFacing getOrientation(IBlockState state) {
        return ((GenericBlock)state.getBlock()).getFrontDirection(state);
    }

    // Given the metavalue of a block, reorient the world direction to the internal block direction
    // so that the front side will be SOUTH.
    public static EnumFacing worldToBlockSpace(EnumFacing side, IBlockState state) {
        return worldToBlockSpace(side, getOrientation(state));
    }

    // Given the metavalue of a block, reorient the world direction to the internal block direction
    // so that the front side will be SOUTH.
    public static EnumFacing worldToBlockSpaceHoriz(EnumFacing side, IBlockState state) {
        return worldToBlockSpace(side, getOrientationHoriz(state));
    }

    public static EnumFacing worldToBlockSpace(EnumFacing worldSide, EnumFacing blockDirection) {
        switch (blockDirection) {
            case DOWN:
                switch (worldSide) {
                    case DOWN: return SOUTH;
                    case UP: return NORTH;
                    case NORTH: return UP;
                    case SOUTH: return DOWN;
                    case WEST: return EAST;
                    case EAST: return WEST;
                    default: return worldSide;
                }
            case UP:
                switch (worldSide) {
                    case DOWN: return NORTH;
                    case UP: return SOUTH;
                    case NORTH: return UP;
                    case SOUTH: return DOWN;
                    case WEST: return WEST;
                    case EAST: return EAST;
                    default: return worldSide;
                }
            case NORTH:
                if (worldSide == DOWN || worldSide == UP) {
                    return worldSide;
                }
                return worldSide.getOpposite();
            case SOUTH:
                return worldSide;
            case WEST:
                if (worldSide == DOWN || worldSide == UP) {
                    return worldSide;
                } else if (worldSide == WEST) {
                    return SOUTH;
                } else if (worldSide == NORTH) {
                    return WEST;
                } else if (worldSide == EAST) {
                    return NORTH;
                } else {
                    return EAST;
                }
            case EAST:
                if (worldSide == DOWN || worldSide == UP) {
                    return worldSide;
                } else if (worldSide == WEST) {
                    return NORTH;
                } else if (worldSide == NORTH) {
                    return EAST;
                } else if (worldSide == EAST) {
                    return SOUTH;
                } else {
                    return WEST;
                }
            default:
                return worldSide;
        }
    }

    public static EnumFacing blockToWorldSpace(EnumFacing blockSide, EnumFacing blockDirection) {
        switch (blockDirection) {
            case DOWN:
                switch (blockSide) {
                    case SOUTH: return DOWN;
                    case NORTH: return UP;
                    case UP: return NORTH;
                    case DOWN: return SOUTH;
                    case EAST: return WEST;
                    case WEST: return EAST;
                    default: return blockSide;
                }
            case UP:
                switch (blockSide) {
                    case NORTH: return DOWN;
                    case SOUTH: return UP;
                    case UP: return NORTH;
                    case DOWN: return SOUTH;
                    case WEST: return WEST;
                    case EAST: return EAST;
                    default: return blockSide;
                }
            case NORTH:
                if (blockSide == DOWN || blockSide == UP) {
                    return blockSide;
                }
                return blockSide.getOpposite();
            case SOUTH:
                return blockSide;
            case WEST:
                if (blockSide == DOWN || blockSide == UP) {
                    return blockSide;
                } else if (blockSide == SOUTH) {
                    return WEST;
                } else if (blockSide == WEST) {
                    return NORTH;
                } else if (blockSide == NORTH) {
                    return EAST;
                } else {
                    return SOUTH;
                }
            case EAST:
                if (blockSide == DOWN || blockSide == UP) {
                    return blockSide;
                } else if (blockSide == NORTH) {
                    return WEST;
                } else if (blockSide == EAST) {
                    return NORTH;
                } else if (blockSide == SOUTH) {
                    return EAST;
                } else {
                    return SOUTH;
                }
            default:
                return blockSide;
        }
    }


    public static Vector blockToWorldSpace(Vector v, IBlockState state) {
        return blockToWorldSpace(v, getOrientation(state));
    }

    // Given the metavalue of a block, reorient the world direction to the internal block direction
    // so that the front side will be SOUTH.
    public static Vector blockToWorldSpaceHoriz(Vector v, IBlockState state) {
        return blockToWorldSpace(v, getOrientationHoriz(state));
    }

    public static Vector blockToWorldSpace(Vector v, EnumFacing side) {
        switch (side) {
            case DOWN: return new Vector(v.x, v.z, v.y);        // @todo check: most likely wrong
            case UP:  return new Vector(v.x, v.z, v.y);         // @todo check: most likely wrong
            case NORTH: return new Vector(1-v.x, v.y, 1-v.z);
            case SOUTH: return v;
            case WEST: return new Vector(1-v.z, v.y, v.x);
            case EAST: return new Vector(v.z, v.y, 1-v.x);
            default: return v;
        }
    }

    public static EnumFacing getTopDirection(EnumFacing rotation) {
        switch(rotation) {
            case DOWN:
                return SOUTH;
            case UP:
                return EnumFacing.NORTH;
            default:
                return EnumFacing.UP;
        }
    }

    public static EnumFacing getBottomDirection(EnumFacing rotation) {
        switch(rotation) {
            case DOWN:
                return EnumFacing.NORTH;
            case UP:
                return SOUTH;
            default:
                return DOWN;
        }
    }

    public static int setOrientation(int metadata, EnumFacing orientation) {
        return (metadata & ~MASK_ORIENTATION) | orientation.ordinal();
    }

    public static EnumFacing getOrientationHoriz(IBlockState state) {
        return state.getValue(GenericBlock.FACING_HORIZ);
    }

    public static int setOrientationHoriz(int metadata, EnumFacing orientation) {
        return (metadata & ~MASK_ORIENTATION_HORIZONTAL) | getHorizOrientationMeta(orientation);
    }

    public static int getHorizOrientationMeta(EnumFacing orientation) {
        return orientation.ordinal()-2;
    }

    public static boolean getRedstoneSignal(int metadata) {
        return (metadata & MASK_REDSTONE) != 0;
    }

    public static int setRedstoneSignal(int metadata, boolean signal) {
        if (signal) {
            return metadata | MASK_REDSTONE;
        } else {
            return metadata & ~MASK_REDSTONE;
        }
    }

    public static boolean getRedstoneSignalIn(int metadata) {
        return (metadata & MASK_REDSTONE_IN) != 0;
    }

    public static int setRedstoneSignalIn(int metadata, boolean signal) {
        if (signal) {
            return metadata | MASK_REDSTONE_IN;
        } else {
            return metadata & ~MASK_REDSTONE_IN;
        }
    }

    public static boolean getRedstoneSignalOut(int metadata) {
        return (metadata & MASK_REDSTONE_OUT) != 0;
    }

    public static int setRedstoneSignalOut(int metadata, boolean signal) {
        if (signal) {
            return metadata | MASK_REDSTONE_OUT;
        } else {
            return metadata & ~MASK_REDSTONE_OUT;
        }
    }

    public static int setState(int metadata, int value) {
        return (metadata & ~MASK_STATE) | (value << 2);
    }

    public static int getState(int metadata) {
        return (metadata & MASK_STATE) >> 2;
    }

    public static EnumFacing determineOrientation(int x, int y, int z, EntityLivingBase entityLivingBase) {
        if (MathHelper.abs((float) entityLivingBase.posX - x) < 2.0F && MathHelper.abs((float) entityLivingBase.posZ - z) < 2.0F) {
            double d0 = entityLivingBase.posY + 1.82D - entityLivingBase.getYOffset();

            if (d0 - y > 2.0D) {
                return EnumFacing.UP;
            }

            if (y - d0 > 0.0D) {
                return DOWN;
            }
        }
        int l = MathHelper.floor_double((entityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        return l == 0 ? EnumFacing.NORTH : (l == 1 ? EnumFacing.EAST : (l == 2 ? SOUTH : (l == 3 ? EnumFacing.WEST : DOWN)));
    }

    public static EnumFacing determineOrientationHoriz(EntityLivingBase entityLivingBase) {
        int l = MathHelper.floor_double((entityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        return l == 0 ? EnumFacing.NORTH : (l == 1 ? EnumFacing.EAST : (l == 2 ? SOUTH : (l == 3 ? EnumFacing.WEST : DOWN)));
    }


    public static void emptyInventoryInWorld(World world, BlockPos pos, Block block, IInventory inventory) {
        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            ItemStack itemstack = inventory.getStackInSlot(i);
            spawnItemStack(world, pos, itemstack);
            inventory.setInventorySlotContents(i, null);
        }

        world.updateComparatorOutputLevel(pos, block);
//        world.func_147453_f(x, y, z, block);
    }

    public static void spawnItemStack(World world, BlockPos c, ItemStack itemStack) {
        spawnItemStack(world, c.getX(), c.getY(), c.getZ(), itemStack);
    }

    public static void spawnItemStack(World world, int x, int y, int z, ItemStack itemstack) {
        if (itemstack != null) {
            float f = random.nextFloat() * 0.8F + 0.1F;
            float f1 = random.nextFloat() * 0.8F + 0.1F;
            EntityItem entityitem;

            float f2 = random.nextFloat() * 0.8F + 0.1F;
            while (itemstack.stackSize > 0) {
                int j = random.nextInt(21) + 10;

                if (j > itemstack.stackSize) {
                    j = itemstack.stackSize;
                }

                itemstack.stackSize -= j;
                entityitem = new EntityItem(world, (x + f), (y + f1), (z + f2), new ItemStack(itemstack.getItem(), j, itemstack.getItemDamage()));
                float f3 = 0.05F;
                entityitem.motionX = ((float)random.nextGaussian() * f3);
                entityitem.motionY = ((float)random.nextGaussian() * f3 + 0.2F);
                entityitem.motionZ = ((float)random.nextGaussian() * f3);

                if (itemstack.hasTagCompound()) {
                    entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                }
                world.spawnEntityInWorld(entityitem);
            }
        }
    }


    public static void placeBlock(World world, BlockPos pos, GenericBlock block, EntityPlayer player) {
        IBlockState state = block.getDefaultState().withProperty(GenericBlock.FACING_HORIZ, player.getHorizontalFacing().getOpposite());
        world.setBlockState(pos, state, 2);
    }

    public static Block getBlock(ItemStack stack) {
        if (stack.getItem() instanceof ItemBlock) {
            return ((ItemBlock) stack.getItem()).getBlock();
        } else {
            return null;
        }
    }

    public static <T extends GenericTE> Optional<T> getTE(Class<T> clazz, IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof GenericTE && (clazz == null || clazz.isInstance(te))) {
            return Optional.of((T) te);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<GenericInventoryTE> getInventoryTE(World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof GenericInventoryTE) {
            return Optional.of((GenericInventoryTE) te);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<IInventory> getInventory(World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof IInventory) {
            return Optional.of((IInventory) te);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<GenericTE> castGenericTE(TileEntity te) {
        return (te instanceof GenericTE) ? Optional.of((GenericTE) te) : Optional.empty();
    }

    public static <T extends GenericTE> T castTE(TileEntity te) {
        return (T) te;
    }

    public static Optional<GenericBlock> getBlock(World world, BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();
        if (block instanceof GenericBlock) {
            return Optional.of((GenericBlock) block);
        } else {
            return Optional.empty();
        }
    }
}
