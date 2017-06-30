package mcjty.immcraft.api.util;

import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import java.text.DecimalFormat;

public class Plane {

    private final Vec3d s1;
    private final Vec3d s2;
    private final Vec3d s3;
    private final Vec3d s4;

    // The plane is:
    //         S1 S2
    //         S3 S4
    public Plane(Vec3d s1, Vec3d s2, Vec3d s3, Vec3d s4) {
        this.s1 = s1;
        this.s2 = s2;
        this.s3 = s3;
        this.s4 = s4;
    }

    public Plane offset(Vec3d o) {
        return new Plane(s1.add(o), s2.add(o), s3.add(o), s4.add(o));
    }

    public Vec3d getS1() {
        return s1;
    }

    public Vec3d getS2() {
        return s2;
    }

    public Vec3d getS3() {
        return s3;
    }

    public Vec3d getS4() {
        return s4;
    }

    private static DecimalFormat dfCommas = new DecimalFormat("###.##");

    public static String v(Vec3d v) {
        return dfCommas.format(v.x) + " " + dfCommas.format(v.y) + " " + dfCommas.format(v.z);
    }

    public Vec2f intersect(Vec3d r1, Vec3d r2) {
//        System.out.println("Plane[" + v(s1) + " | " + v(s2) + " | " + v(s3) + " | " + v(s4) + "]");
//        System.out.println("    Ray[" + v(r1) + " | " + v(r2) + "]");

        Vec3d dS21 = s2.subtract(s1);
        Vec3d dS31 = s3.subtract(s1);
        Vec3d n = dS21.crossProduct(dS31);

        // 2.
        Vec3d dR = r1.subtract(r2);

        double ndotdR = n.dotProduct(dR);

        if (Math.abs(ndotdR) < 1e-6f) { // Choose your tolerance
            return null;
        }

        double t = -n.dotProduct(r1.subtract(s1)) / ndotdR;
        Vec3d M = r1.add(dR.scale(t));

        // 3.
        Vec3d dMS1 = M.subtract(s1);
        double u = dMS1.dotProduct(dS21);
        double v = dMS1.dotProduct(dS31);

//        System.out.println("    u = " + u + "," + v);
//        System.out.println("    M = " + v(M));

        // 4.
        if (u >= 0.0 && u <= dS21.dotProduct(dS21) &&
                v >= 0.0 && v <= dS31.dotProduct(dS31)) {
            return new Vec2f((float)u, (float)v);
        } else {
            return null;
        }
    }
}
