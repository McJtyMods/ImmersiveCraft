package mcjty.immcraft.api.input;

import java.util.HashMap;
import java.util.Map;

public enum KeyType {
    KEY_NEXTITEM("nextItem"),
    KEY_PREVIOUSITEM("prevItem"),
    KEY_PLACEITEM("placeItem");

    private final String name;

    private static final Map<String, KeyType> TYPE_MAP = new HashMap<>();

    static {
        for (KeyType type : values()) {
            TYPE_MAP.put(type.getName(), type);
        }
    }

    KeyType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static KeyType getKeyType(String name) {
        return TYPE_MAP.get(name);
    }
}
