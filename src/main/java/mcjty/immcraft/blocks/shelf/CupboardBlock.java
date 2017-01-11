package mcjty.immcraft.blocks.shelf;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CupboardBlock extends GenericShelfBlock<CupboardTE> {

    public CupboardBlock() {
        super("cupboard", CupboardTE.class);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initModel() {
        super.initModel();
        ClientRegistry.bindTileEntitySpecialRenderer(CupboardTE.class, new CupboardTESR());
    }
}
