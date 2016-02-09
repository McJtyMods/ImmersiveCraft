package mcjty.immcraft.cables;

import mcjty.immcraft.api.cable.ICableType;

import java.util.HashMap;
import java.util.Map;

public class CableRegistry {
    private static Map<String, ICableType> cableTypes = new HashMap<>();

    public static void registerCableType(ICableType type) {
        cableTypes.put(type.getTypeID(), type);
    }

    public static ICableType getTypeByID(String id) {
        return cableTypes.get(id);
    }
}
