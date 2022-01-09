package Solver.Entities;

import Solver.BasicBuilders.MyPoint;
import Solver.BasicBuilders.MyPolygon;
import Solver.BasicBuilders.Polyhedron;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// Builds the Cubes for the Rubik's cube
public class CubeBuilder {
    // Builds a standard cube (Colors: Red, Orange, Blue, Green, White, Yellow)
    public static IEntity createCube(double size, double centerX, double centerY, double centerZ) {
        List<Polyhedron> cubeFaces = new ArrayList<Polyhedron>();
        double border_size = 10;
        Color border_colour = Color.BLACK;

        // Creates the 8 corner points
        MyPoint p1 = new MyPoint(centerX + -size / 2, centerY + -size / 2, centerZ + size / 2);
        MyPoint p2 = new MyPoint(centerX + size / 2, centerY + -size / 2, centerZ + size / 2);
        MyPoint p3 = new MyPoint(centerX + size / 2, centerY + size / 2, centerZ + size / 2);
        MyPoint p4 = new MyPoint(centerX + -size / 2, centerY + size / 2, centerZ + size / 2);
        MyPoint p5 = new MyPoint(centerX + -size / 2, centerY + -size / 2, centerZ + -size / 2);
        MyPoint p6 = new MyPoint(centerX + size / 2, centerY + -size / 2, centerZ + -size / 2);
        MyPoint p7 = new MyPoint(centerX + size / 2, centerY + size / 2, centerZ + -size / 2);
        MyPoint p8 = new MyPoint(centerX + -size / 2, centerY + size / 2, centerZ + -size / 2);

        // Front Face
        cubeFaces.add(createSquare(p1, p2, p3, p4, 1, border_size, Color.RED, border_colour));
        // Back Face
        cubeFaces.add(createSquare(p5, p6, p7, p8, 1, border_size, new Color(255,120,30), border_colour));

        // Left Face
        cubeFaces.add(createSquare(p1, p5, p8, p4, 2, border_size, Color.BLUE, border_colour));
        // Right Face
        cubeFaces.add(createSquare(p2, p6, p7, p3, 2, border_size, Color.GREEN, border_colour));

        // Top Face
        cubeFaces.add(createSquare(p4, p3, p7, p8, 3, border_size, Color.YELLOW, border_colour));
        // Bottom Face
        cubeFaces.add(createSquare(p1, p2, p6, p5, 3, border_size, Color.WHITE, border_colour));

        Entity cube = new Entity(cubeFaces);
        cube.setCenter(centerX, centerY, centerZ);
        cube.setOriginalPosition(centerX, centerY, centerZ);
        cube.setSize(size);
        return cube;
    }

    // Builds a custom cube (custom colours)
    public static IEntity createCube2(double size, double centerX, double centerY, double centerZ, boolean[] faces, Color[] face_colours) {
        List<Polyhedron> cubeFaces = new ArrayList<Polyhedron>();
        double border_size = 10;
        Color[] cube_colours = new Color[7];
        for (int i = 0; i < faces.length; i++) {
            cube_colours[i] = faces[i] == true ? face_colours[i]:face_colours[6];
        }
        cube_colours[6] = face_colours[6];

        MyPoint p1 = new MyPoint(centerX + -size / 2, centerY + -size / 2, centerZ + size / 2);
        MyPoint p2 = new MyPoint(centerX + size / 2, centerY + -size / 2, centerZ + size / 2);
        MyPoint p3 = new MyPoint(centerX + size / 2, centerY + size / 2, centerZ + size / 2);
        MyPoint p4 = new MyPoint(centerX + -size / 2, centerY + size / 2, centerZ + size / 2);
        MyPoint p5 = new MyPoint(centerX + -size / 2, centerY + -size / 2, centerZ + -size / 2);
        MyPoint p6 = new MyPoint(centerX + size / 2, centerY + -size / 2, centerZ + -size / 2);
        MyPoint p7 = new MyPoint(centerX + size / 2, centerY + size / 2, centerZ + -size / 2);
        MyPoint p8 = new MyPoint(centerX + -size / 2, centerY + size / 2, centerZ + -size / 2);

        // Front
        cubeFaces.add(CubeBuilder.createSquare(p1, p2, p3, p4, 1, border_size, cube_colours[0], cube_colours[6]));
        // Back
        cubeFaces.add(CubeBuilder.createSquare(p5, p6, p7, p8, 1, border_size, cube_colours[1], cube_colours[6]));
        // Left
        cubeFaces.add(CubeBuilder.createSquare(p1, p5, p8, p4, 2, border_size, cube_colours[2], cube_colours[6]));
        // Right
        cubeFaces.add(CubeBuilder.createSquare(p2, p6, p7, p3, 2, border_size, cube_colours[3], cube_colours[6]));
        // Top
        cubeFaces.add(CubeBuilder.createSquare(p4, p3, p7, p8, 3, border_size, cube_colours[4], cube_colours[6]));
        // Bottom
        cubeFaces.add(CubeBuilder.createSquare(p1, p2, p6, p5, 3, border_size, cube_colours[5], cube_colours[6]));

        Entity cube = new Entity(cubeFaces);
        cube.setCenter(centerX, centerY, centerZ);
        cube.setOriginalPosition(centerX, centerY, centerZ);
        cube.setSize(size);
        return cube;
    }

    // Creates the square for each face of the cube
    private static Polyhedron createSquare(MyPoint p1, MyPoint p2, MyPoint p3, MyPoint p4, int orientation, double border_size, Color face_colour, Color border_colour){
//        Color temp = border_colour;
//        border_colour = face_colour;
//        face_colour = temp;

        MyPoint innerP1 = p1;
        MyPoint innerP2 = p2;
        MyPoint innerP3 = p3;
        MyPoint innerP4 = p4;
        switch (orientation){
            case 1:
                // Vertical X
                innerP1 = new MyPoint(p1.x+border_size, p1.y+border_size, p1.z);
                innerP2 = new MyPoint(p2.x-border_size, p2.y+border_size, p2.z);
                innerP3 = new MyPoint(p3.x-border_size, p3.y-border_size, p3.z);
                innerP4 = new MyPoint(p4.x+border_size, p4.y-border_size, p4.z);
                break;
            case 2:
                // Vertical Z
                innerP1 = new MyPoint(p1.x, p1.y+border_size, p1.z-border_size);
                innerP2 = new MyPoint(p2.x, p2.y+border_size, p2.z+border_size);
                innerP3 = new MyPoint(p3.x, p3.y-border_size, p3.z+border_size);
                innerP4 = new MyPoint(p4.x, p4.y-border_size, p4.z-border_size);
                break;
            case 3:
                // Horizontal
                innerP1 = new MyPoint(p1.x+border_size, p1.y, p1.z-border_size);
                innerP2 = new MyPoint(p2.x-border_size, p2.y, p2.z-border_size);
                innerP3 = new MyPoint(p3.x-border_size, p3.y, p3.z+border_size);
                innerP4 = new MyPoint(p4.x+border_size, p4.y, p4.z+border_size);
                break;
            default:
                System.out.println("Error: No Orientation");
                break;
        }

        Polyhedron cubeFace = new Polyhedron(
                new MyPolygon(border_colour, p1, p2, innerP2, innerP1),
                new MyPolygon(border_colour, p2, p3, innerP3, innerP2),
                new MyPolygon(border_colour, p3, p4, innerP4, innerP3),
                new MyPolygon(border_colour, p4, p1, innerP1, innerP4),
                new MyPolygon(face_colour, innerP1, innerP2, innerP3, innerP4)
        );
        return cubeFace;
    }
}
