package mcjty.immcraft.cables;


import mcjty.immcraft.api.cable.ICableSubType;
import net.minecraft.util.math.Vec3d;

/**
 * Cable section for the renderer (BundleISBM)
 */
public class CableSectionRender {
    private final ICableSubType subType;
    private final Vec3d vector;
    private final Vec3d vector1;
    private final Vec3d vector2;

    public CableSectionRender(ICableSubType subType, Vec3d vector, Vec3d vector1, Vec3d vector2) {
        this.subType = subType;
        this.vector = vector;
        this.vector1 = vector1;
        this.vector2 = vector2;
    }

    public ICableSubType getSubType() {
        return subType;
    }

    public Vec3d getVector() {
        return vector;
    }

    public Vec3d getVector1() {
        return vector1;
    }

    public Vec3d getVector2() {
        return vector2;
    }
}
