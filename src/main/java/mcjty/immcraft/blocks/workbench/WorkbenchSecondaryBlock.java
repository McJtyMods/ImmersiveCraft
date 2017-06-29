package mcjty.immcraft.blocks.workbench;

import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.api.IImmersiveCraft;
import mcjty.immcraft.api.rendering.HandleTESR;
import mcjty.immcraft.blocks.generic.GenericBlockWithTE;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class WorkbenchSecondaryBlock extends GenericBlockWithTE<WorkbenchSecondaryTE> {

    public WorkbenchSecondaryBlock() {
        super(Material.WOOD, "workbench_sec", WorkbenchSecondaryTE.class, false);
        setHardness(1.0f);
        setSoundType(SoundType.WOOD);
        setHarvestLevel("axe", 0);

        // @todo define handles
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initModel() {
        super.initModel();
        ClientRegistry.bindTileEntitySpecialRenderer(WorkbenchSecondaryTE.class, new HandleTESR<WorkbenchSecondaryTE>(this) {
            @Nonnull
            @Override
            protected IImmersiveCraft getApi() {
                return ImmersiveCraft.api;
            }
        });
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return false;
    }

    // @todo
//    @Override
//    public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
//        return false;
//    }

    @Override
    public boolean isBlockNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (!world.isRemote) {
            EnumFacing left = getLeftDirection(state);
            ((EntityPlayerMP) player).interactionManager.tryHarvestBlock(pos.offset(left));
        }
    }
}
