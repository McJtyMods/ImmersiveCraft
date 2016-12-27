package mcjty.immcraft.blocks.shelf;

import mcjty.immcraft.api.handles.HandleSelector;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BookshelfBlock extends GenericShelfBlock<BookshelfTE> {

    public BookshelfBlock() {
        super("bookshelf", BookshelfTE.class);
    }

    @Override
    protected void createSelectors() {
        float boundsdx = .05f;
        float boundsdy = .2f;
        int i = 0;

        for (int y = 0 ; y < 4 ; y++) {
            for (int x = 0 ; x < 16 ; x++) {
                addSelector(new HandleSelector("i" + i, new AxisAlignedBB(boundsdx * x + .1f, boundsdy * y + .1f, 0.2f, boundsdx * ((float) x + 1) + .1f, boundsdy * ((float) y + 1) + .1f, 0.5)));
                i++;
            }
        }
    }


    @SideOnly(Side.CLIENT)
    @Override
    public void initModel() {
        super.initModel();
        ClientRegistry.bindTileEntitySpecialRenderer(BookshelfTE.class, new BookshelfTESR());
    }
}
