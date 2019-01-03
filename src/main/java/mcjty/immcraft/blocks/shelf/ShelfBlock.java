package mcjty.immcraft.blocks.shelf;

public class ShelfBlock extends GenericShelfBlock<ShelfTE> {

    public ShelfBlock() {
        super("shelf", ShelfTE.class);
    }

    @Override
    public void initModel() {
        super.initModel();
        ShelfTESR.register();
    }
}
