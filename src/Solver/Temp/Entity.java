package Solver.Temp;

import Solver.BasicBuilders.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Entity implements IEntity {

    protected List<Polyhedron> polyhedron;
    private MyPolygon[] polygons;
    private MyPoint centerPosition;
    private String id;
    private String colours;
    private double size;
    private MyPoint originalPosition;
    private int[] orientation = {0, 1, 2, 3, 4, 5};

    public Entity(List<Polyhedron> polyhedron){
        this.polyhedron = polyhedron;
        List<MyPolygon> tempList = new ArrayList<MyPolygon>();
        for (Polyhedron poly : this.polyhedron){
            tempList.addAll(Arrays.asList(poly.getPolygons()));
        }
        this.polygons = new MyPolygon[tempList.size()];
        this.polygons = tempList.toArray(this.polygons);
        this.sortPolygons();
    }

    @Override
    public void render(Graphics g) {
        for (MyPolygon poly : this.polygons){
            poly.render(g);
        }
    }

    @Override
    public void translate(double x, double y, double z) {
        for (Polyhedron polyhedron : this.polyhedron){
            polyhedron.translate(x,y,z);
        }
        this.sortPolygons();
    }

    @Override
    public void rotate(boolean CW, double xDegrees, double yDegrees, double zDegrees) {
        for (Polyhedron polyhedron : this.polyhedron){
            polyhedron.rotate(CW, xDegrees, yDegrees, zDegrees);
        }
        if (this.centerPosition != null) {
            PointConverter.rotateAxis(this.centerPosition, CW, xDegrees, yDegrees,zDegrees);
        }
        this.sortPolygons();

    }

    private int xCounter = 0;
    private int yCounter = 0;
    private int zCounter = 0;
    @Override
    public void rotate(Axis axis, boolean CW, double xDegrees, double yDegrees, double zDegrees) {
        for (Polyhedron polyhedron : this.polyhedron){
            polyhedron.rotate(axis, CW, xDegrees, yDegrees, zDegrees);
        }
        if (this.centerPosition != null) {
            PointConverter.rotateAxis(this.centerPosition, axis, CW, xDegrees, yDegrees, zDegrees);
        }
        if (xDegrees > 0) {
            this.xCounter += xDegrees;
        }
        if (yDegrees > 0) {
            this.yCounter += yDegrees;
        }
        if (zDegrees > 0) {
            this.zCounter += zDegrees;
        }
        if (this.xCounter == 90 || this.yCounter == 90 || this.zCounter == 90) {
            correctOrientation(CW,xDegrees,yDegrees,zDegrees);
            this.xCounter = 0;
            this.yCounter = 0;
            this.zCounter = 0;
        }
        this.sortPolygons();
    }

    private void correctOrientation(boolean CW, double xDegrees, double yDegrees, double zDegrees){
        // Cube Face:
        //      orientation[0] = Front
        //      orientation[1] = Back
        //      orientation[2] = Left
        //      orientation[3] = Right
        //      orientation[4] = Top
        //      orientation[5] = Bottom
        // Points in Direction:
        //      0 - Pointing Towards Front (Z+)
        //      1 - Pointing Towards Back (Z-)
        //      2 - Pointing To Left (X-)
        //      3 - Pointing To Right (X+)
        //      4 - Pointing Up (Y+)
        //      5 - Pointing Down (Y-)
        if (xDegrees > 0) {
            for (int i = 0; i < orientation.length; i++) {
                switch (orientation[i]) {
                    case 5:
                        orientation[i] = CW == true ? 0 : 1;
                        break;
                    case 0:
                        orientation[i] = CW == true ? 4 : 5;
                        break;
                    case 4:
                        orientation[i] = CW == true ? 1 : 0;
                        break;
                    case 1:
                        orientation[i] = CW == true ? 5 : 4;
                        break;
                    default:
                        break;
                }
            }
        }
        if (yDegrees > 0) {
            for (int i = 0; i < orientation.length; i++) {
                switch (orientation[i]) {
                    case 0:
                        orientation[i] = CW == true ? 2 : 3;
                        break;
                    case 2:
                        orientation[i] = CW == true ? 1 : 0;
                        break;
                    case 1:
                        orientation[i] = CW == true ? 3 : 2;
                        break;
                    case 3:
                        orientation[i] = CW == true ? 0 : 1;
                        break;
                    default:
                        break;
                }
            }
        }
        if (zDegrees > 0) {
            for (int i = 0; i < orientation.length; i++) {
                switch (orientation[i]) {
                    case 5:
                        orientation[i] = CW == true ? 2 : 3;
                        break;
                    case 2:
                        orientation[i] = CW == true ? 4 : 5;
                        break;
                    case 4:
                        orientation[i] = CW == true ? 3 : 2;
                        break;
                    case 3:
                        orientation[i] = CW == true ? 5 : 4;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public static void sortEntities(List<IEntity> entities) {
        Collections.sort(entities, new Comparator<IEntity>(){
            @Override
            public int compare(IEntity e1, IEntity e2) {
                MyPoint p1Average = e1.getAveragePoint();
                MyPoint p2Average = e2.getAveragePoint();
                double p1Dist = MyPoint.distBetween(p1Average, MyPoint.origin);
                double p2Dist = MyPoint.distBetween(p2Average, MyPoint.origin);
                double diff = p1Dist - p2Dist;
                if (diff == 0) {
                    return 0;
                }
                return diff < 0 ? 1 : -1;
            }
        });
    }

    private void sortPolygons(){
        MyPolygon.sortPolygons(this.polygons);
    }

    public MyPoint getAveragePoint(){
        this.sortPolygons();
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

    public static IEntity axis(double size, int direction, double centerX, double centerY, double centerZ) {
        List<Polyhedron> axis = new ArrayList<Polyhedron>();

        double arrowSide = 0.5*size;
        double arrowHead = 4*size;
        double sizeAdjust = size/2;
        double sizeAdjustLong = 100*size;
        Color colour;
        if (direction == 0) {
            colour = Color.RED;
            MyPoint p1 = new MyPoint(centerX + -sizeAdjust, centerY + -sizeAdjust, centerZ + -sizeAdjustLong);
            MyPoint p2 = new MyPoint(centerX + sizeAdjust, centerY + -sizeAdjust, centerZ + -sizeAdjustLong);
            MyPoint p3 = new MyPoint(centerX + sizeAdjust, centerY + sizeAdjust, centerZ + -sizeAdjustLong);
            MyPoint p4 = new MyPoint(centerX + -sizeAdjust, centerY + sizeAdjust, centerZ + -sizeAdjustLong);
            MyPoint p5 = new MyPoint(centerX + -sizeAdjust, centerY + -sizeAdjust, centerZ + sizeAdjustLong);
            MyPoint p6 = new MyPoint(centerX + sizeAdjust, centerY + -sizeAdjust, centerZ + sizeAdjustLong);
            MyPoint p7 = new MyPoint(centerX + sizeAdjust, centerY + sizeAdjust, centerZ + sizeAdjustLong);
            MyPoint p8 = new MyPoint(centerX + -sizeAdjust, centerY + sizeAdjust, centerZ + sizeAdjustLong);
            MyPoint p9 = new MyPoint(centerX + -sizeAdjust - arrowSide, centerY + -sizeAdjust - arrowSide, centerZ + sizeAdjustLong);
            MyPoint p10 = new MyPoint(centerX + sizeAdjust + arrowSide, centerY + -sizeAdjust - arrowSide, centerZ + sizeAdjustLong);
            MyPoint p11 = new MyPoint(centerX + sizeAdjust + arrowSide, centerY + sizeAdjust + arrowSide, centerZ + sizeAdjustLong);
            MyPoint p12 = new MyPoint(centerX + -sizeAdjust - arrowSide, centerY + sizeAdjust + arrowSide, centerZ + sizeAdjustLong);
            MyPoint p13 = new MyPoint(centerX, centerY, centerZ + sizeAdjustLong + arrowHead);

            axis.add(createArrow(colour, p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13));
        } else if (direction == 1) {
            colour = Color.BLUE;
            MyPoint p1 = new MyPoint(centerX + -sizeAdjust, centerY + -sizeAdjustLong, centerZ + sizeAdjust);
            MyPoint p2 = new MyPoint(centerX + sizeAdjust, centerY + -sizeAdjustLong, centerZ + sizeAdjust);
            MyPoint p3 = new MyPoint(centerX + sizeAdjust, centerY + -sizeAdjustLong, centerZ + -sizeAdjust);
            MyPoint p4 = new MyPoint(centerX + -sizeAdjust, centerY + -sizeAdjustLong, centerZ + -sizeAdjust);
            MyPoint p5 = new MyPoint(centerX + -sizeAdjust, centerY + sizeAdjustLong, centerZ + sizeAdjust);
            MyPoint p6 = new MyPoint(centerX + sizeAdjust, centerY + sizeAdjustLong, centerZ + sizeAdjust);
            MyPoint p7 = new MyPoint(centerX + sizeAdjust, centerY + sizeAdjustLong, centerZ + -sizeAdjust);
            MyPoint p8 = new MyPoint(centerX + -sizeAdjust, centerY + sizeAdjustLong, centerZ + -sizeAdjust);
            MyPoint p9 = new MyPoint(centerX + -sizeAdjust - arrowSide, centerY + sizeAdjustLong, centerZ + sizeAdjust + arrowSide);
            MyPoint p10 = new MyPoint(centerX + sizeAdjust + arrowSide, centerY + sizeAdjustLong, centerZ + sizeAdjust + arrowSide);
            MyPoint p11 = new MyPoint(centerX + sizeAdjust + arrowSide, centerY + sizeAdjustLong, centerZ + -sizeAdjust - arrowSide);
            MyPoint p12 = new MyPoint(centerX + -sizeAdjust - arrowSide, centerY + sizeAdjustLong, centerZ + -sizeAdjust - arrowSide);
            MyPoint p13 = new MyPoint(centerX, centerY + sizeAdjustLong + arrowHead, centerZ);

            axis.add(createArrow(colour, p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13));
        } else if (direction == 2) {
            colour = Color.GREEN;
            MyPoint p1 = new MyPoint(centerX + -sizeAdjustLong, centerY + -sizeAdjust, centerZ + sizeAdjust);
            MyPoint p2 = new MyPoint(centerX + -sizeAdjustLong, centerY + -sizeAdjust, centerZ + -sizeAdjust);
            MyPoint p3 = new MyPoint(centerX + -sizeAdjustLong, centerY + sizeAdjust, centerZ + -sizeAdjust);
            MyPoint p4 = new MyPoint(centerX + -sizeAdjustLong, centerY + sizeAdjust, centerZ + sizeAdjust);
            MyPoint p5 = new MyPoint(centerX + sizeAdjustLong, centerY + -sizeAdjust, centerZ + sizeAdjust);
            MyPoint p6 = new MyPoint(centerX + sizeAdjustLong, centerY + -sizeAdjust, centerZ + -sizeAdjust);
            MyPoint p7 = new MyPoint(centerX + sizeAdjustLong, centerY + sizeAdjust, centerZ + -sizeAdjust);
            MyPoint p8 = new MyPoint(centerX + sizeAdjustLong, centerY + sizeAdjust, centerZ + sizeAdjust);
            MyPoint p9 = new MyPoint(centerX + sizeAdjustLong, centerY + -sizeAdjust - arrowSide, centerZ + sizeAdjust + arrowSide);
            MyPoint p10 = new MyPoint(centerX + sizeAdjustLong, centerY + -sizeAdjust - arrowSide, centerZ + -sizeAdjust - arrowSide);
            MyPoint p11 = new MyPoint(centerX + sizeAdjustLong, centerY + sizeAdjust + arrowSide, centerZ + -sizeAdjust - arrowSide);
            MyPoint p12 = new MyPoint(centerX + sizeAdjustLong, centerY + sizeAdjust + arrowSide, centerZ + sizeAdjust + arrowSide);
            MyPoint p13 = new MyPoint(centerX + sizeAdjustLong + arrowHead, centerY, centerZ);

            axis.add(createArrow(colour, p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13));
        }

        return new Entity(axis);
    }

    private static Polyhedron createArrow(Color colour, MyPoint p1, MyPoint p2, MyPoint p3, MyPoint p4, MyPoint p5, MyPoint p6, MyPoint p7, MyPoint p8, MyPoint p9, MyPoint p10, MyPoint p11, MyPoint p12, MyPoint p13){
        Polyhedron arrow = new Polyhedron(colour,
                new MyPolygon(p1, p2, p3, p4),
                new MyPolygon(p1, p5, p8, p4),
                new MyPolygon(p4, p8, p7, p3),
                new MyPolygon(p3, p7, p6, p2),
                new MyPolygon(p2, p6, p5, p1),
                new MyPolygon(p5, p6, p7, p8),
                new MyPolygon(p9, p10, p11, p12),
                new MyPolygon(p9, p13, p12),
                new MyPolygon(p12, p13, p11),
                new MyPolygon(p11, p13, p10),
                new MyPolygon(p10, p13, p9)
        );
        return arrow;
    }

    public static IEntity plane(double offset, int direction) {
        List<Polyhedron> axis = new ArrayList<Polyhedron>();
        MyPoint p1 = new MyPoint(offset+5, -1000, 1000);
        MyPoint p2 = new MyPoint(offset+5, -1000, -1000);
        MyPoint p3 = new MyPoint(offset+5, 1000, -1000);
        MyPoint p4 = new MyPoint(offset+5, 1000, 1000);
        MyPoint p5 = new MyPoint(offset-5, -1000, 1000);
        MyPoint p6 = new MyPoint(offset-5, -1000, -1000);
        MyPoint p7 = new MyPoint(offset-5, 1000, -1000);
        MyPoint p8 = new MyPoint(offset-5, 1000, 1000);
        axis.add(new Polyhedron(new Color(255,155,255),
                new MyPolygon(p1, p2, p3, p4),
                new MyPolygon(p1, p4, p8, p5),
                new MyPolygon(p2, p3, p7, p6),
                new MyPolygon(p4, p3, p7, p8),
                new MyPolygon(p1, p2, p6, p5),
                new MyPolygon(p5, p6, p7, p8)));
        return new Entity(axis);
    }

    public void printCoord() {
        System.out.printf("%s : (%4.2f, %4.2f, %4.2f)\n", this.id, this.centerPosition.xStandard, this.centerPosition.yStandard, this.centerPosition.zStandard);
    }

    public MyPoint getCurrentPosition() {
        return new MyPoint(this.centerPosition.xStandard, this.centerPosition.yStandard, this.centerPosition.zStandard);
    }

    public MyPoint getCurrentSPosition() {
        return new MyPoint(this.centerPosition.xStandard/this.size, this.centerPosition.yStandard/this.size, this.centerPosition.zStandard/this.size);
    }

    public void setCenter(double x, double y, double z){
        this.centerPosition = new MyPoint(x,y,z);
    }

    public MyPoint getCenter() {
        return new MyPoint(this.centerPosition.xStandard, this.centerPosition.yStandard, this.centerPosition.zStandard);
    }

    public void setOriginalPosition(double x, double y, double z) {
        this.originalPosition = new MyPoint(x, y, z);
    }

    public MyPoint getOriginalPosition(){
        return new MyPoint(this.originalPosition.xStandard, this.originalPosition.yStandard, this.originalPosition.zStandard);
    }

    public MyPoint getOriginalSPosition(){
        return new MyPoint(this.originalPosition.xStandard/this.size, this.originalPosition.yStandard/this.size, this.originalPosition.zStandard/this.size);
    }

    public void setID(String id){
        this.id = id;
    }

    public String getID(){
        return this.id;
    }

    public void setColours(String colours){
        this.colours = colours;
    }

    public String getColours(){
        return this.colours;
    }

    public void setSize(double size){
        this.size = size;
    }

    public double getSize(){
        return this.size;
    }

    public int[] getOrientation(){
        return this.orientation;
    }

    public MyPoint faceDirection(Entity cube, int face) {
        for (Polyhedron polyhedron : this.polyhedron) {
            polyhedron.getPolygons();
        }
        return null;
    }

}
