package mcjty.immcraft.blocks.book;

import mcjty.immcraft.api.handles.HandleSelector;
import mcjty.immcraft.blocks.foliage.SticksTE;
import mcjty.immcraft.blocks.generic.GenericBlockWithTE;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static mcjty.immcraft.blocks.foliage.EnumAmount.*;

public class BookStandBlock extends GenericBlockWithTE<BookStandTE> {

    public static final PropertyStandState STATE = PropertyStandState.create("state", (Collection<EnumStandState>) Arrays.stream(EnumStandState.values()).collect(Collectors.toList()));

    public static final AxisAlignedBB EMPTY = new AxisAlignedBB(0, 0, 0, 0, 0, 0);

    public BookStandBlock() {
        super(Material.WOOD, "bookstand", BookStandTE.class, false);
        addSelector(new HandleSelector("l", new AxisAlignedBB(0.15f, 0.23f, 0.8f, 0.25f, 0.33f, 0.9f)));
        addSelector(new HandleSelector("r", new AxisAlignedBB(0.75f, 0.23f, 0.8f, 0.85f, 0.33f, 0.9f)));
    }


    @SideOnly(Side.CLIENT)
    @Override
    public void initModel() {
        super.initModel();
        ClientRegistry.bindTileEntitySpecialRenderer(BookStandTE.class, new BookStandTESR());
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        super.addProbeInfo(mode, probeInfo, player, world, blockState, data);
        TileEntity te = world.getTileEntity(data.getPos());
        if (te instanceof BookStandTE) {
            if (((BookStandTE) te).hasBook()) {
                probeInfo.text(TextFormatting.BLUE + "Use sneak-right click to remove book");
            }
        }
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        TileEntity te = accessor.getTileEntity();
        if (te instanceof BookStandTE) {
            if (((BookStandTE) te).hasBook()) {
                currenttip.add(TextFormatting.BLUE + "Use sneak-right click to remove book");
            }
        }
        return currenttip;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        return EMPTY;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
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
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntity te = worldIn instanceof ChunkCache ? ((ChunkCache)worldIn).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : worldIn.getTileEntity(pos);
        if (te instanceof BookStandTE) {
            BookStandTE bookStandTE = (BookStandTE) te;
            EnumStandState bookState = bookStandTE.getState();
            return state.withProperty(STATE, bookState);
        } else {
            return state;
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING_HORIZ, STATE);
    }

}
