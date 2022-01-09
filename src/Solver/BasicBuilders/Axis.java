package Solver.BasicBuilders;

// Axis class that keeps track of the original axis and its rotations
public class Axis {
    Vector xAxis,yAxis,zAxis;
    MyPoint origin;

    // Defines the base axis
    public Axis() {
        this.origin = new MyPoint(0,0,0);
        this.xAxis = new Vector(1,0,0);
        this.yAxis = new Vector(0,1,0);
        this.zAxis = new Vector(0,0,1);
    }

    // Rotates the axis object
    public void rotate(boolean CW, double xDegrees, double yDegrees, double zDegrees){
        MyPoint x = new MyPoint(this.xAxis.getXComponent(),this.xAxis.getYComponent(),this.xAxis.getZComponent());
        MyPoint y = new MyPoint(this.yAxis.getXComponent(),this.yAxis.getYComponent(),this.yAxis.getZComponent());
        MyPoint z = new MyPoint(this.zAxis.getXComponent(),this.zAxis.getYComponent(),this.zAxis.getZComponent());

        PointConverter.rotateAxis(x, CW, xDegrees, yDegrees, zDegrees);
        PointConverter.rotateAxis(y, CW, xDegrees, yDegrees, zDegrees);
        PointConverter.rotateAxis(z, CW, xDegrees, yDegrees, zDegrees);

        this.xAxis = Vector.normalize(new Vector(origin, x));
        this.yAxis = Vector.normalize(new Vector(origin, y));
        this.zAxis = Vector.normalize(new Vector(origin, z));
    }

    // Translates the axis object
    public void translate(double x, double y, double z){
        PointConverter.translate(this.origin,x,y,z);
    }

    // Gets the position
    public MyPoint getPosition(){
        return new MyPoint(this.origin.x, this.origin.y, this.origin.z);
    }

    // gets the current X axis
    public MyPoint getXAxis(){
        return new MyPoint(this.xAxis.getXComponent(), this.xAxis.getYComponent(), this.xAxis.getZComponent());
    }

    // gets the current Y axis
    public MyPoint getYAxis(){
        return new MyPoint(this.yAxis.getXComponent(), this.yAxis.getYComponent(), this.yAxis.getZComponent());
    }

    // gets the current Z axis
    public MyPoint getZAxis(){
        return new MyPoint(this.zAxis.getXComponent(), this.zAxis.getYComponent(), this.zAxis.getZComponent());
    }

    // Helper function - gets the current prints the axis
    public void print() {
        System.out.println("----------------------------------------");
        System.out.println("Axis Origin: (" + this.origin.x + ", " + this.origin.y + ", " + this.origin.z + ")");
        System.out.println("Axis Origin (Adjusted): (" + this.origin.getAdjustedX() + ", " + this.origin.getAdjustedY() + ", " + this.origin.getAdjustedZ() + ")");
        System.out.println("Axis X Vector: [" + this.xAxis.getXComponent() + ", " + this.xAxis.getYComponent() + ", " + this.xAxis.getZComponent() + "]");
        System.out.println("Axis Y Vector: [" + this.yAxis.getXComponent() + ", " + this.yAxis.getYComponent() + ", " + this.yAxis.getZComponent() + "]");
        System.out.println("Axis Z Vector: [" + this.zAxis.getXComponent() + ", " + this.zAxis.getYComponent() + ", " + this.zAxis.getZComponent() + "]");
        System.out.println("----------------------------------------");
    }

}
