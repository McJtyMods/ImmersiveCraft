package mcjty.immcraft.blocks.book;

import net.minecraft.util.IStringSerializable;

public enum EnumStandState implements IStringSerializable {
    EMPTY("empty"),
    CLOSED("closed"),
    OPEN("open");

    private final String name;

    EnumStandState(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
