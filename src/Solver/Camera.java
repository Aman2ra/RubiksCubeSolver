package Solver;

import Solver.BasicBuilders.MyPoint;
import Solver.BasicBuilders.PointConverter;

public class Camera {
    private MyPoint pos;

    public Camera(double x, double y, double z){
        pos = new MyPoint(x,y,z);
    }

    public void translate(double x, double y, double z){
        PointConverter.translate(pos,x,y,z);
    }

    public void rotate(boolean CW, double xDegrees, double yDegrees, double zDegrees){
        PointConverter.rotateAxis(pos, CW, xDegrees, yDegrees, zDegrees);
    }

    public double getX() {
        return this.pos.x;
    }

    public double getY() {
        return this.pos.y;
    }

    public double getZ() {
        return this.pos.z;
    }

    public MyPoint getPosition(){
        return new MyPoint(this.pos.x, this.pos.y, this.pos.z);
    }
}
