package mcjty.immcraft.api.util;

import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.tuple.Pair;

public class Vector {
    // Given two vectors, calculate a vector that is perpendicular to this while keeping on an axis
    public static Pair<Vec3d,Vec3d> calculatePerpendicularVector(Vec3d v1, Vec3d v2) {
        Vec3d d = v2.subtract(v1);
        if (d.x >= d.y && d.x >= d.z) {
            // X is the biggest distance.
            Vec3d vectorZ = d.crossProduct(new Vec3d(0, 1, 0));
            Vec3d vectorY = d.crossProduct(vectorZ);
            return Pair.of(vectorY, vectorZ);
        } else if (d.z >= d.y) {
            // Z is the biggest distance.
            Vec3d vectorX = d.crossProduct(new Vec3d(0, 1, 0));
            Vec3d vectorY = d.crossProduct(vectorX);
            return Pair.of(vectorY, vectorX);
        } else {
            // Y is the biggest distance.
            Vec3d vectorX = d.crossProduct(new Vec3d(0, 0, 1));
            Vec3d vectorZ = d.crossProduct(vectorX);
            return Pair.of(vectorX, vectorZ);
        }
    }
}
