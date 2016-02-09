package mcjty.immcraft.api.cable;

/**
 * Represents a section of a cable
 */
public interface ICableSection {

    /**
     * Return the id of the multiblock network for this cable.
     * @return
     */
    int getId();

    ICableType getType();

    ICableSubType getSubType();
}
