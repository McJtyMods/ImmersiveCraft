package mcjty.immcraft.api.cable;

/**
 * Implement this to support your own type of cable
 */
public interface ICableType {

    String getTypeID();

    String getReadableName();

    ICableHandler getCableHandler();

    ICableSubType getSubTypeByID(String id);
}
