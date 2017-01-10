package mcjty.immcraft.blocks.book;

import mcjty.immcraft.blocks.generic.GenericBlockWithTE;
import net.minecraft.block.material.Material;

public class BookBlock extends GenericBlockWithTE<BookTE> {

    public BookBlock() {
        super(Material.WOOD, "book", BookTE.class, false);
    }


}
