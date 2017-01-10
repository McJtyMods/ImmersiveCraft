package mcjty.immcraft.api.cable;

import net.minecraft.util.math.BlockPos;

import java.util.List;

/**
 * Representation of a cable.
 */
public interface ICable {
    List<BlockPos> getPath();
}
