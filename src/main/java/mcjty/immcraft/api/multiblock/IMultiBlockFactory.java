package mcjty.immcraft.api.multiblock;

public interface IMultiBlockFactory<T extends IMultiBlock> {

    T create();

    boolean isSameType(IMultiBlock mb);
}
