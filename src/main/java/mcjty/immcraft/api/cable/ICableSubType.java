package mcjty.immcraft.api.cable;

import net.minecraft.block.Block;

import java.util.Optional;

/**
 * Implement this to support your own sub type of cable
 */
public interface ICableSubType {

    String getTypeID();

    String getReadableName();

    /**
     * The block that implements this sub type
     * @return
     */
    Optional<Block> getBlock();

    /**
     * The texture name (MODID:name) for this subtype
     */
    String getTextureName();
}
