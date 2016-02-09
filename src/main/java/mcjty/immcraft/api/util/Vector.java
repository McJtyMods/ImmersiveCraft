package mcjty.immcraft.api.util;

import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import org.apache.commons.lang3.tuple.Pair;

public class Vector {
    public final float x;
    public final float y;
    public final float z;

    public Vector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector(double x, double y, double z) {
        this.x = (float) x;
        this.y = (float) y;
        this.z = (float) z;
    }

    public Vec3 getVec3() {
        return new Vec3(x, y, z);
    }

    public double length() {
        return (float) Math.sqrt(Vector.dot(this, this));
    }

    public Vector mul(float f) {
        return Vector.mul(this, f);
    }

    public static Vector mul(Vector v, float f) {
        return new Vector(v.getX() * f, v.getY() * f, v.getZ() * f);
    }

    public Vector subtract(Vector v2) {
        return Vector.subtract(this, v2);
    }

    public Vector subtract(float x, float y, float z) {
        return Vector.subtract(this, x, y, z);
    }

    public Vector subtract(BlockPos pos) {
        return subtract(pos.getX(), pos.getY(), pos.getZ());
    }

    public static Vector subtract(Vector v1, Vector v2) {
        return new Vector(v1.getX() - v2.getX(), v1.getY() - v2.getY(), v1.getZ() - v2.getZ());
    }

    public static Vector subtract(Vector v1, float x, float y, float z) {
        return new Vector(v1.getX() - x, v1.getY() - y, v1.getZ() - z);
    }

    public Vector add(Vector v2) {
        return Vector.add(this, v2);
    }

    public Vector add(float x, float y, float z) {
        return Vector.add(this, x, y, z);
    }

    public Vector add(BlockPos pos) {
        return add(pos.getX(), pos.getY(), pos.getZ());
    }

    public static Vector add(Vector v1, Vector v2) {
        return new Vector(v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ());
    }

    public static Vector add(Vector v1, float x, float y, float z) {
        return new Vector(v1.getX() + x, v1.getY() + y, v1.getZ() + z);
    }

    public float dot(Vector v2) {
        return Vector.dot(this, v2);
    }

    public static float dot(Vector v1, Vector v2) {
        return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ();
    }

    public Vector cross(Vector b) {
        return Vector.cross(this, b);
    }

    public static Vector cross(Vector a, Vector b) {
        float x = a.y*b.z - a.z*b.y;
        float y = a.z*b.x - a.x*b.z;
        float z = a.x*b.y - a.y*b.x;
        return new Vector(x, y, z);
    }

    // Given two vectors, calculate a vector that is perpendicular to this while keeping on an axis
    public static Pair<Vector,Vector> calculatePerpendicularVector(Vector v1, Vector v2) {
        Vector d = v2.subtract(v1);
        if (d.getX() >= d.getY() && d.getX() >= d.getZ()) {
            // X is the biggest distance.
            Vector vectorZ = d.cross(new Vector(0, 1, 0));
            Vector vectorY = d.cross(vectorZ);
            return Pair.of(vectorY, vectorZ);
        } else if (d.getZ() >= d.getY()) {
            // Z is the biggest distance.
            Vector vectorX = d.cross(new Vector(0, 1, 0));
            Vector vectorY = d.cross(vectorX);
            return Pair.of(vectorY, vectorX);
        } else {
            // Y is the biggest distance.
            Vector vectorX = d.cross(new Vector(0, 0, 1));
            Vector vectorZ = d.cross(vectorX);
            return Pair.of(vectorX, vectorZ);
        }
    }


    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float norm() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public Vector normalize() {
        float n = norm();
        return new Vector(x / n, y / n, z / n);
    }

    @Override
    public String toString() {
        return "Vector{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
