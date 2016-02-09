package mcjty.immcraft.cables;

import net.minecraft.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum CableSubType {
    LIQUID("liq"),
    GROUND("gro"),
    PLUS("plu");

    private static final Map<String,CableSubType> TYPE_MAP = new HashMap<>();

    static {
        for (CableSubType type : values()) {
            TYPE_MAP.put(type.getTypeId(), type);
        }
    }

    private final String typeId;

    CableSubType(String typeId) {
        this.typeId = typeId;
    }

    public Optional<Block> getBlock() {
        switch (this) {
            case LIQUID:
//                return Optional.of(ModBlocks.hoseBlock);
//            case GROUND:
//                return Optional.of(ModBlocks.blackElectricCableBlock);
//            case PLUS:
//                return Optional.of(ModBlocks.electricCableBlock);
        }
        return Optional.empty();
    }

    public String getTypeId() {
        return typeId;
    }

    public static CableSubType getTypeById(String id) {
        return TYPE_MAP.get(id);
    }
}
