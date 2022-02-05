package Solver.Entities;

import Solver.BasicBuilders.Axis;
import Solver.BasicBuilders.MyPoint;
import Solver.BasicBuilders.MyPolygon;

import java.awt.*;

public interface IEntity {

    void render(Graphics g);

    void translate(double x, double y, double z);

    void rotate(boolean CW, double xDegrees, double yDegrees, double zDegrees);

    void rotate(Axis axis, boolean CW, double xDegrees, double yDegrees, double zDegrees);

    MyPoint getAveragePoint();

    void printCoord();

    MyPoint getCenter();

    void setVisible(boolean visible);

    void replaceColor(Color newColor, Color prevColor);
}