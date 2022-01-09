package Solver.BasicBuilders;

// Point class
public class MyPoint {
    public final static MyPoint origin = new MyPoint(0,0,0);

    // Current x y z
    public double x, y, z;
    // x y z in the standard basis (without any rotations)
    public double xStandard,yStandard,zStandard;
    // Translated x y z (relative to the camera)
    public double xOffset, yOffset, zOffset;

    public MyPoint(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
        this.xStandard = x;
        this.yStandard = y;
        this.zStandard = z;
        this.xOffset = 0;
        this.yOffset = 0;
        this.zOffset = 0;
    }

    // Gets the distance between two points
    public static double distBetween(MyPoint p1, MyPoint p2){
        // Distance between points in 3D space
        return Math.sqrt(Math.pow(p1.x-p2.x, 2) + Math.pow(p1.y-p2.y, 2) + Math.pow(p1.z-p2.z, 2));
    }

    // Get the current adjusted x
    public double getAdjustedX() {
        return this.x + this.xOffset;
    }

    // Get the current adjusted y
    public double getAdjustedY() {
        return this.y + this.yOffset;
    }

    // Get the current adjusted z
    public double getAdjustedZ() {
        return this.z + this.zOffset;
    }

}

