package mcjty.immcraft.blocks.chest;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BookShelfBlock extends GenericShelfBlock<ShelfTE> {

    public BookShelfBlock() {
        super("shelf", ShelfTE.class);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initModel() {
        super.initModel();
        ClientRegistry.bindTileEntitySpecialRenderer(ShelfTE.class, new ShelfTESR());
    }
}
