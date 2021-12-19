package Solver.BasicBuilders;

import java.awt.*;
import java.util.*;
import java.util.List;

public class MyPolygon {
    protected MyPoint[] points;
    private Color baseColour;
    private boolean visible;

    public MyPolygon(MyPoint... points){
        this.createPointsArray(points);
        this.updateVisibitily();
    }

    public MyPolygon(Color colour, MyPoint... points){
        this.baseColour = colour;
        this.createPointsArray(points);
        this.updateVisibitily();
    }

    public void render(Graphics g) {
        if (this.visible) return;

        Polygon poly = new Polygon();
        for (int i = 0; i < this.points.length; i++){
            Point p = PointConverter.convertPoint(this.points[i]);
            poly.addPoint(p.x, p.y);
        }

        g.setColor(this.baseColour);
        g.fillPolygon(poly);
    }

    public void translate(double x, double y, double z){
        for (MyPoint p: points){
            PointConverter.translate(p, x, y, z);
        }
        this.updateVisibitily();
    }

    public void rotate(boolean CW, double xDegrees, double yDegrees, double zDegrees){
        for (MyPoint p: points){
            PointConverter.rotateAxis(p,CW,xDegrees,yDegrees,zDegrees);
        }
        this.updateVisibitily();
    }

    public void rotate(Axis axis, boolean CW, double xDegrees, double yDegrees, double zDegrees){
        for (MyPoint p: points){
            PointConverter.rotateAxis(p, axis, CW, xDegrees, yDegrees, zDegrees);
        }
        this.updateVisibitily();
    }

    public void updateVisibitily() {
        this.visible = this.getAverageZ() > 0;
    }

    public double getAverageZ(){
        double sum = 0;
        for (MyPoint p : this.points){
            sum += p.getAdjustedZ();
        }
        return sum/this.points.length;
    }

    protected void createPointsArray(MyPoint[] points) {
        this.points = new MyPoint[points.length];
        for (int i = 0; i < points.length; i++){
            MyPoint p = points[i];
            this.points[i] = new MyPoint(p.x, p.y, p.z);
        }
    }

    public MyPoint getAveragePoint() {
        double x = 0;
        double y = 0;
        double z = 0;
        for (MyPoint p : this.points){
            x += p.getAdjustedX();
            y += p.getAdjustedY();
            z += p.getAdjustedZ();
        }
        x /= this.points.length;
        y /= this.points.length;
        z /= this.points.length;

        return new MyPoint(x, y, z);
    }

    public int getNumPoints(){
        return this.points.length;
    }

    public void setColor(Color colour) {
        this.baseColour = colour;
    }

    public static MyPolygon[] sortPolygons(MyPolygon [] polys) {
        List<MyPolygon> polygonsList = new ArrayList<MyPolygon>(Arrays.asList(polys));

        Collections.sort(polygonsList, new Comparator<MyPolygon>(){
            @Override
            public int compare(MyPolygon p1, MyPolygon p2) {
                MyPoint p1Average = p1.getAveragePoint();
                MyPoint p2Average = p2.getAveragePoint();
                double p1Dist = MyPoint.distBetween(p1Average, MyPoint.origin);
                double p2Dist = MyPoint.distBetween(p2Average, MyPoint.origin);
                double diff = p1Dist - p2Dist;
                if (diff == 0) {
                    return 0;
                }
                return diff < 0 ? 1 : -1;
            }
        });
        for (int i = 0; i < polys.length;  i++){
            polys[i] = polygonsList.get(i);
        }
        return polys;
    }
}
