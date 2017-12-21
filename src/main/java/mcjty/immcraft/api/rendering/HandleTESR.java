package mcjty.immcraft.api.rendering;


import mcjty.immcraft.api.IImmersiveCraft;
import mcjty.immcraft.api.generic.GenericBlock;
import mcjty.immcraft.api.generic.GenericTE;
import mcjty.immcraft.api.handles.IInterfaceHandle;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public abstract class HandleTESR<T extends GenericTE> extends TileEntitySpecialRenderer<T> {

    protected final GenericBlock block;

    protected Vec3d textOffset = new Vec3d(0, 0, 0);

    public HandleTESR(GenericBlock block) {
        this.block = block;
    }

    @Nonnull
    protected abstract IImmersiveCraft getApi();

    @Override
    public void render(T tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

        IBlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
        if (!(state.getBlock() instanceof GenericBlock)) {
            // Safety. In some situations (like with shaders mod installed) this gets called
            // when the block is already removed
            return;
        }

        GlStateManager.pushMatrix();

        GlStateManager.translate(x + .5, y, z + .5);
        GlStateManager.disableRescaleNormal();

        BlockRenderHelper.rotateFacing(tileEntity, block.getRotationType());
        renderExtra(tileEntity);
        renderHandles(tileEntity);

        GlStateManager.popMatrix();
    }

    protected void renderHandles(T tileEntity) {
        double distanceSq = Minecraft.getMinecraft().player.getDistanceSq(tileEntity.getPos());
        if (distanceSq > getApi().getMaxHandleRenderDistanceSquared()) {
            return;
        }

        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        IInterfaceHandle selectedHandle = BlockRenderHelper.getFacingInterfaceHandle(tileEntity, block);
        BlockRenderHelper.renderInterfaceHandles(getApi(), tileEntity, selectedHandle, textOffset);
    }

    protected void renderExtra(T tileEntity) {

    }
}
