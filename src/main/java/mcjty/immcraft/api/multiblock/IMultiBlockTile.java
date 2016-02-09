package mcjty.immcraft.api.multiblock;

/**
 * This interface can be implemented by tile entities that
 * want to be part of a given multiblock. Multiblocks that
 * work through the cable system do not use this interface.
 */
public interface IMultiBlockTile<T extends IMultiBlock> {

    T getMultiBlock();

    Integer getID();

    void setID(Integer id);
}
