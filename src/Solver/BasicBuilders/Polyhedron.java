package Solver.BasicBuilders;

import java.awt.*;

public class Polyhedron {
    protected MyPolygon[] polygons;
    private Color colour;

    public Polyhedron(MyPolygon... polys) {
        this.polygons = polys;
        this.sortPolys();
    }

    public Polyhedron(Color colour, MyPolygon... polys) {
        this.colour = colour;
        this.polygons = polys;
        this.setPolyColour();
        this.sortPolys();
    }

    public void render(Graphics g) {
        for (MyPolygon poly : this.polygons){
            poly.render(g);
        }
    }

    public void translate(double x, double y, double z){
        for (MyPolygon poly : this.polygons){
            poly.translate(x, y, z);
        }
    }

    public void rotate(boolean CW, double xDegrees, double yDegrees, double zDegrees){
        for (MyPolygon poly : this.polygons){
            poly.rotate(CW, xDegrees, yDegrees, zDegrees);
        }
    }

    public void rotate(Axis axis, boolean CW, double xDegrees, double yDegrees, double zDegrees){
        for (MyPolygon poly : this.polygons){
            poly.rotate(axis, CW, xDegrees, yDegrees, zDegrees);
        }
    }

    public MyPolygon[] getPolygons(){
        return this.polygons;
    }


    private void sortPolys(){
        MyPolygon.sortPolygons(this.polygons);
    }

    public MyPoint getAveragePoint(){
        double x = 0;
        double y = 0;
        double z = 0;
        double total = 0;
        for (MyPolygon poly : this.polygons){
            MyPoint temp = poly.getAveragePoint();
            total += poly.getNumPoints();
            x += temp.x;
            y += temp.y;
            z += temp.z;
        }
        x /= total;
        y /= total;
        z /= total;

        return new MyPoint(x, y, z);
    }

    private void setPolyColour(){
        for (MyPolygon poly : this.polygons) {
            poly.setColor(this.colour);
        }
    }
}
