package mcjty.immcraft.blocks.foliage;

import net.minecraft.util.IStringSerializable;

public enum EnumAmount implements IStringSerializable {
    SINGLE("single"),
    DOUBLE("double"),
    TRIPPLE("tripple"),
    ALL("all");

    private final String name;

    EnumAmount(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
