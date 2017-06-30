package mcjty.immcraft.blocks.shelf;


import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.api.IImmersiveCraft;
import mcjty.immcraft.api.rendering.HandleTESR;
import mcjty.immcraft.blocks.ModBlocks;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class ShelfTESR extends HandleTESR<ShelfTE> {

    public ShelfTESR() {
        super(ModBlocks.shelfBlock);
        textOffset = new Vec3d(0, 0, -.2);
    }

    @Nonnull
    @Override
    protected IImmersiveCraft getApi() {
        return ImmersiveCraft.api;
    }

    @Override
    protected void renderHandles(ShelfTE tileEntity) {
        super.renderHandles(tileEntity);
    }
}
