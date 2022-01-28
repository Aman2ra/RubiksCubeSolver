package Solver.BasicBuilders;

import Solver.CanvasWindow;
import java.awt.*;

// Class for transforming a point and getting values
public class PointConverter {
    private static final double ZoomFactor = 1.5;
    private static final int PerspectiveFactor = 1400;
    private static double scale = 4;

    // Zooms in
    public static void zoomIn() {
        scale *= ZoomFactor;
    }

    // Zooms out
    public static void zoomOut() {
        scale /= ZoomFactor;
    }

    // Converts the point from a (x,y,z) position to a (x,y) position for the 2D screen
    public static Point convertPoint(MyPoint point3D) {
        double x3D = point3D.getAdjustedX() * scale;
        double y3D = point3D.getAdjustedY() * scale;
        double z3D = point3D.getAdjustedZ() * scale;
        double[] newVal = scale(x3D, y3D, z3D);
        int x2D = (int) (CanvasWindow.WIDTH / 2 + newVal[0]);
        int y2D = (int) (CanvasWindow.HEIGHT / 2 - newVal[1]);
        Point point2D = new Point(x2D, y2D);
        return point2D;
    }

    // Scales the point
    private static double[] scale(double x3D, double y3D, double z3D) {
        double distance = Math.sqrt(Math.pow(x3D, 2) + Math.pow(y3D, 2));
        double theta = Math.atan2(y3D, x3D);
        double depth = 15 - z3D;
        double localScale = Math.abs(PerspectiveFactor / (depth + PerspectiveFactor));
        distance *= localScale;
        double[] newVal = new double[2];
        newVal[0] = distance * Math.cos(theta);
        newVal[1] = distance * Math.sin(theta);
        return newVal;
    }

    // Translates the point
    public static void translate(MyPoint p, double x, double y, double z) {
        p.xOffset += x;
        p.yOffset += y;
        p.zOffset += z;
    }

    // Rotates the point around current basis
    public static void rotateAxis(MyPoint p, boolean CW, double xDegrees, double yDegrees, double zDegrees) {
        rotateAxisX(p, CW, xDegrees);
        rotateAxisY(p, CW, yDegrees);
        rotateAxisZ(p, CW, zDegrees);
        return;
    }

    // Rotates the point around the "standard" basis given the axis object is the standard axis transformed
    public static void rotateAxis(MyPoint p, Axis axis, boolean CW, double xDegrees, double yDegrees, double zDegrees) {
        MyPoint standard = new MyPoint(p.xStandard, p.yStandard, p.zStandard);
        rotateAxisX(standard, CW, xDegrees);
        rotateAxisY(standard, CW, yDegrees);
        rotateAxisZ(standard, CW, zDegrees);
        p.xStandard = standard.x;
        p.yStandard = standard.y;
        p.zStandard = standard.z;
        p.x = p.xStandard * axis.xAxis.getXComponent() + p.yStandard * axis.yAxis.getXComponent() + p.zStandard * axis.zAxis.getXComponent();
        p.y = p.xStandard * axis.xAxis.getYComponent() + p.yStandard * axis.yAxis.getYComponent() + p.zStandard * axis.zAxis.getYComponent();
        p.z = p.xStandard * axis.xAxis.getZComponent() + p.yStandard * axis.yAxis.getZComponent() + p.zStandard * axis.zAxis.getZComponent();
        return;
    }

    // Rotates the point around the x axis
    private static void rotateAxisX(MyPoint p, boolean CW, double degrees) {
        double radius = Math.sqrt(Math.pow(p.y, 2) + Math.pow(p.z, 2));
        double theta = Math.atan2(p.z, p.y);
        theta += 2 * Math.PI / 360 * degrees * (CW ? -1 : 1);
        p.y = radius * Math.cos(theta);
        p.z = radius * Math.sin(theta);
    }

    // Rotates the point around the y axis
    private static void rotateAxisY(MyPoint p, boolean CW, double degrees) {
        double radius = Math.sqrt(Math.pow(p.x, 2) + Math.pow(p.z, 2));
        double theta = Math.atan2(p.z, p.x);
        theta += 2 * Math.PI / 360 * degrees * (CW ? 1 : -1);
        p.x = radius * Math.cos(theta);
        p.z = radius * Math.sin(theta);
    }

    // Rotates the point around the z axis
    private static void rotateAxisZ(MyPoint p, boolean CW, double degrees) {
        double radius = Math.sqrt(Math.pow(p.x, 2) + Math.pow(p.y, 2));
        double theta = Math.atan2(p.y, p.x);
        theta += 2 * Math.PI / 360 * degrees * (CW ? -1 : 1);
        p.x = radius * Math.cos(theta);
        p.y = radius * Math.sin(theta);
    }
}