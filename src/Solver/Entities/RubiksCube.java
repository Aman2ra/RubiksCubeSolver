package Solver.Entities;

import Solver.BasicBuilders.Axis;
import Solver.BasicBuilders.MyPoint;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RubiksCube implements IEntity{

    private List<IEntity> cubes = new ArrayList<IEntity>();
    private List<IEntity> left = new ArrayList<IEntity>();
    private List<IEntity> right = new ArrayList<IEntity>();
    private List<IEntity> front = new ArrayList<IEntity>();
    private List<IEntity> back = new ArrayList<IEntity>();
    private List<IEntity> top = new ArrayList<IEntity>();
    private List<IEntity> bottom = new ArrayList<IEntity>();
    private double rubiksCubeSize;
    private double distance;
    private MyPoint center;
    private boolean visible = true;

    public RubiksCube(double size, double centerX, double centerY, double centerZ, double distance) {
        this.rubiksCubeSize = size;
        this.distance = distance;
        this.center = new MyPoint(centerX,centerY,centerZ);
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) continue;
                    double cX = x * (size + distance) + centerX;
                    double cY = y * (size + distance) + centerY;
                    double cZ = z * (size + distance) + centerZ;
                    IEntity cube = CubeBuilder.createCube(size,cX,cY,cZ);
                    ((Entity) cube).setID(""+x+y+z);
                    this.cubes.add(cube);
                    if (x == -1) {
                        this.left.add(cube);
                    } else if (x == 1) {
                        this.right.add(cube);
                    }
                    if (y == -1) {
                        this.bottom.add(cube);
                    } else if (y == 1) {
                        this.top.add(cube);
                    }
                    if (z == -1) {
                        this.back.add(cube);
                    } else if (z == 1) {
                        this.front.add(cube);
                    }
                }
            }
        }
    }

    public RubiksCube(double size, double centerX, double centerY, double centerZ, double distance, Color[] face_colours) {
        this.rubiksCubeSize = size;
        this.center = new MyPoint(centerX,centerY,centerZ);
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) continue;
                    String colours = "";
                    boolean[] faces = {false, false, false, false, false, false};
                    if (z == 1) {
                        faces[0] = true;
                        colours += "R";
                    } else if (z == -1) {
                        faces[1] = true;
                        colours += "O";
                    }
                    if (x == 1) {
                        faces[3] = true;
                        colours += "G";
                    } else if (x == -1) {
                        faces[2] = true;
                        colours += "B";
                    }
                    if (y == 1) {
                        faces[4] = true;
                        colours += "Y";
                    } else if (y == -1) {
                        faces[5] = true;
                        colours += "W";
                    }
                    double cX = x * (size + distance) + centerX;
                    double cY = y * (size + distance) + centerY;
                    double cZ = z * (size + distance) + centerZ;
                    IEntity cube = CubeBuilder.createCube2(size, cX, cY, cZ, faces, face_colours);
                    ((Entity) cube).setID(""+x+y+z);
                    ((Entity) cube).setColours(colours);
                    this.cubes.add(cube);
                    if (x == -1) {
                        this.left.add(cube);
                    } else if (x == 1) {
                        this.right.add(cube);
                    }
                    if (y == -1) {
                        this.bottom.add(cube);
                    } else if (y == 1) {
                        this.top.add(cube);
                    }
                    if (z == -1) {
                        this.back.add(cube);
                    } else if (z == 1) {
                        this.front.add(cube);
                    }
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        if (this.visible) {
            for (IEntity cube : this.cubes) {
                cube.render(g);
            }
        }
    }

    @Override
    public void translate(double x, double y, double z) {
        for (IEntity cube : this.cubes){
            cube.translate(x,y,z);
        }
    }

    @Override
    public void rotate(boolean CW, double xDegrees, double yDegrees, double zDegrees) {
        for (IEntity cube : this.cubes){
            cube.rotate(CW, xDegrees, yDegrees, zDegrees);
        }
    }

    @Override
    public void rotate(Axis axis, boolean CW, double xDegrees, double yDegrees, double zDegrees) {
        for (IEntity cube : this.cubes){
            cube.rotate(axis, CW, xDegrees, yDegrees, zDegrees);
        }
    }

    @Override
    public MyPoint getAveragePoint() {
        double x = 0;
        double y = 0;
        double z = 0;
        MyPoint point;
        for (IEntity cube : this.cubes){
            point = cube.getAveragePoint();
            x += point.x;
            y += point.y;
            z += point.z;
        }
        x /= this.cubes.size();
        y /= this.cubes.size();
        z /= this.cubes.size();
        return new MyPoint(x,y,z);
    }

    @Override
    public void printCoord() {
        System.out.printf("Rubiks cube : (%4.2f,%4.2f,%4.2f)\n", this.center.getAdjustedX(), this.center.getAdjustedY(), this.center.getAdjustedZ());
    }

    @Override
    public MyPoint getCenter() {
        return new MyPoint(this.center.x, this.center.y, this.center.z);
    }

    public void setVisible(boolean visible) {
        for (IEntity entity : this.cubes) {
            ((Entity) entity).setVisible(visible);
        }
        this.visible = visible;
    }

    @Override
    public void replaceColor(Color newColor, Color prevColor) {
        for (IEntity entity : this.cubes) {
            entity.replaceColor(newColor, prevColor);
        }
    }

    public void sortEntities() {
        Entity.sortEntities(this.cubes);
    }

    public void left(Axis axis, boolean direction, double degrees){
        for (IEntity cube : this.left){
            cube.rotate(axis, !direction,degrees,0,0);
        }
        this.fixLists();
    }

    public void right(Axis axis, boolean direction, double degrees){
        for (IEntity cube : this.right){
            cube.rotate(axis, direction, degrees, 0, 0);
        }
        this.fixLists();
    }

    public void up(Axis axis, boolean direction, double degrees) {
        for (IEntity cube : this.top){
            cube.rotate(axis, direction,0,degrees,0);
        }
        this.fixLists();
    }

    public void down(Axis axis, boolean direction, double degrees) {
        for (IEntity cube : this.bottom){
            cube.rotate(axis, !direction,0,degrees,0);
        }
        this.fixLists();
    }

    public void forward(Axis axis, boolean direction, double degrees) {
        for (IEntity cube : this.front){
            cube.rotate(axis, direction,0,0,degrees);
        }
        this.fixLists();
    }

    public void backward(Axis axis, boolean direction, double degrees) {
        for (IEntity cube : this.back){
            cube.rotate(axis, !direction,0,0,degrees);
        }
        this.fixLists();
    }

    private void fixLists(){
        this.left.clear();
        this.right.clear();
        this.top.clear();
        this.bottom.clear();
        this.front.clear();
        this.back.clear();
        for (IEntity cube : this.cubes){
            MyPoint center = cube.getCenter();
            if (center.xStandard < -this.rubiksCubeSize/2) {
                this.left.add(cube);
            } else if (center.xStandard > this.rubiksCubeSize/2) {
                this.right.add(cube);
            }
            if (center.yStandard < -this.rubiksCubeSize/2) {
                this.bottom.add(cube);
            } else if (center.yStandard > this.rubiksCubeSize/2) {
                this.top.add(cube);
            }
            if (center.zStandard < -this.rubiksCubeSize/2) {
                this.back.add(cube);
            } else if (center.zStandard > this.rubiksCubeSize/2) {
                this.front.add(cube);
            }
        }
    }

    public List<IEntity> getLeft() {
        return this.left;
    }

    public List<IEntity> getRight() {
        return this.right;
    }

    public List<IEntity> getFront() {
        return this.front;
    }

    public List<IEntity> getBack() {
        return this.back;
    }

    public List<IEntity> getTop() {
        return this.top;
    }

    public List<IEntity> getBottom() {
        return this.bottom;
    }

    public List<IEntity> getAll() {
        return this.cubes;
    }

    public void changeColors(Color left, Color right, Color top, Color bottom, Color front, Color back) {
        for (IEntity cube : this.cubes){

        }
    }
}
