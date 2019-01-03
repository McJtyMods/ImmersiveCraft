package mcjty.immcraft.blocks.shelf;

public class CupboardBlock extends GenericShelfBlock<CupboardTE> {

    public CupboardBlock() {
        super("cupboard", CupboardTE.class);
    }

    @Override
    public void initModel() {
        super.initModel();
        CupboardTESR.register();
    }
}
