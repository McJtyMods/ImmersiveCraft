package mcjty.immcraft.api.multiblock;

public interface IMultiBlockFactory<T extends IMultiBlock> {

    T create();

    /**
     * Create a new client info object for this multiblock
     * @return
     */
    IMultiBlockClientInfo createClientInfo();

    boolean isSameType(IMultiBlock mb);
}
