package Solver.Temp;

import Solver.BasicBuilders.Axis;
import Solver.BasicBuilders.MyPoint;
import jdk.swing.interop.SwingInterOpUtils;

public class Solver {
    private RubiksCube rubiksCube;
    private Axis axis;
    private EntityManager em;
    private double rSpeed = 90.0;

    public Solver(RubiksCube cube, Axis axis) {
        this.rubiksCube = cube;
        this.axis = axis;
    }

    //Delete after
    public Solver(RubiksCube cube, Axis axis, EntityManager em) {
        this.rubiksCube = cube;
        this.axis = axis;
        this.em = em;
    }

    public void solveCube(){
        // Stage One: Bottom [White] Cross
        solveStageOne();
        // Stage Two: Bottom [White] Corners
        solveStageTwo();
        // Stage Three: Middle Layer Edges
        solveStageThree();
        // Stage Four: Top [Yellow] Cross (Edge Orientation)
        solveStageFour();
        // Stage Five: Top [Yellow] Corner Orientation
        // Stage Six:  Top [Yellow] Corner Position
        // Stage Seven: Top [Yellow] Edge Position
        // Stage Eight: Top [Yellow] Fix Position
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void solveStageOne(){
        System.out.println("Solver - Stage One: Bottom Cross");
        for (IEntity cube : this.rubiksCube.getAll()) {
            String algString = "";
            String colours = ((Entity) cube).getColours();
            if (colours.contains("W") && colours.length() == 2) {
                int[] orientation = ((Entity) cube).getOrientation();
                MyPoint originalPos = ((Entity) cube).getOriginalSPosition();
                MyPoint currentPos = ((Entity) cube).getCurrentSPosition();
//                System.out.printf("%s\n" , colours);
//                System.out.printf("     Current Position: (%.2f, %.2f, %.2f)\n", currentPos.x, currentPos.y, currentPos.z);
                // If cube is in the middle layer, put it in the top layer
                if (currentPos.y > -0.5 && currentPos.y < 0.5) {
                    if (currentPos.x > 0.5 && currentPos.z > 0.5) {
                        algString = " RUR' ";
                    } else if (currentPos.x > 0.5 && currentPos.z < -0.5) {
                        algString = " R'UR ";
                    } else if (currentPos.x < -0.5 && currentPos.z > 0.5) {
                        algString = " L'UL ";
                    } else if (currentPos.x < -0.5 && currentPos.z < -0.5) {
                        algString = " LUL' ";
                    }
                    algString = executeAlg(algString);
                }
                // If cube is in bottom layer but wrong position
                orientation = ((Entity) cube).getOrientation();
                currentPos = ((Entity) cube).getCurrentSPosition();
                if (currentPos.y < -0.5 && (Math.abs(currentPos.x - originalPos.x) > 0.5 || Math.abs(currentPos.z - originalPos.z) > 0.5 || orientation[5] != 5)) {
                    if (currentPos.x > 0.5) {
                        algString = " RR ";
                    } else if (currentPos.x < -0.5) {
                        algString = " LL ";
                    } else if (currentPos.z > 0.5) {
                        algString = " FF ";
                    } else if (currentPos.z < -0.5) {
                        algString = " BB ";
                    }
                    algString = executeAlg(algString);
                }
                // cube is in top layer
                orientation = ((Entity) cube).getOrientation();
                currentPos = ((Entity) cube).getCurrentSPosition();
                if (currentPos.y >= 0.5) {
                    // Putting the cube over the correct position
                    int angleDif = getAngle(currentPos.x, currentPos.z) - getAngle(originalPos.x, originalPos.z);
                    if (angleDif == 180 || angleDif == -180) {
                        algString = " UU ";
                    } else if (angleDif == 90 || angleDif == -270) {
                        algString = " U' ";
                    } else if (angleDif == 270 || angleDif == -90) {
                        algString = " U ";
                    }
                    algString = executeAlg(algString);
                    orientation = ((Entity) cube).getOrientation();
                    currentPos = ((Entity) cube).getCurrentSPosition();
                    // Cube is in correct position in top layer, Algorithms based which way the side with bottom colour is facing
                    if (originalPos.x > 0.5) {
                        algString = orientation[5] == 4 ? " RR " : " UFR'F' ";
                    } else if (originalPos.x < -0.5) {
                        algString = orientation[5] == 4 ? " LL " : " UBL'B' ";
                    } else if (originalPos.z > 0.5) {
                        algString = orientation[5] == 4 ? " FF " : " ULF'L' ";
                    } else if (originalPos.z < -0.5) {
                        algString = orientation[5] == 4 ? " BB " : " URB'R' ";
                    }
                    algString = executeAlg(algString);
                }
            }
        }
        System.out.println("Solver - Stage One: Bottom Cross Finished");
        return;
    }

    public void solveStageTwo() {
        System.out.println("Solver - Stage Two: Bottom Corners");
        for (IEntity cube : this.rubiksCube.getAll()) {
            String algString = "";
            String colours = ((Entity) cube).getColours();
            if (colours.contains("W") && colours.length() == 3) {
                int[] orientation = ((Entity) cube).getOrientation();
                MyPoint originalPos = ((Entity) cube).getOriginalSPosition();
                MyPoint currentPos = ((Entity) cube).getCurrentSPosition();
//                System.out.printf("%s\n" , colours);
//                System.out.printf("     Current Position: (%.2f, %.2f, %.2f)\n", currentPos.x, currentPos.y, currentPos.z);
                // If cube is in the bottom layer, put it in the top layer if position and/or orientation is wrong
                if (currentPos.y < -0.5 && (Math.abs(currentPos.x - originalPos.x) > 0.5 || Math.abs(currentPos.z - originalPos.z) > 0.5 ||
                        (orientation[0] != 0 || orientation[1] != 1 || orientation[2] != 2 || orientation[3] != 3 || orientation[4] != 4 || orientation[5] != 5))) {
                    if (currentPos.x > 0.5 && currentPos.z > 0.5) {
                        algString = " RUR' ";
                    } else if (currentPos.x > 0.5 && currentPos.z < -0.5) {
                        algString = " R'U'R ";
                    } else if (currentPos.x < -0.5 && currentPos.z > 0.5) {
                        algString = " L'U'L ";
                    } else if (currentPos.x < -0.5 && currentPos.z < -0.5) {
                        algString = " LUL' ";
                    }
                    algString = executeAlg(algString);
                }
                // If cube is in top layer but wrong position
                orientation = ((Entity) cube).getOrientation();
                currentPos = ((Entity) cube).getCurrentSPosition();
                if (currentPos.y >= 0.5) {
                    double xC = Math.round(currentPos.x);
                    double zC = Math.round(currentPos.z);
                    double xO = Math.round(originalPos.x);
                    double zO = Math.round(originalPos.z);
                    // Putting the cube over the correct position
                    if (xC == -xO && zC == -zO) {
                        algString = " UU ";
                    } else if (!(xC == xO && zC == zO)) {
                        if (xC == 1 && zC == 1){
                            algString = (xO == -1 && zO == 1) ? " U " : " U' ";
                        } else if (xC == 1 && zC == -1) {
                            algString = (xO == 1 && zO == 1) ? " U " : " U' ";
                        } else if (xC == -1 && zC == -1) {
                            algString = (xO == 1 && zO == -1) ? " U " : " U' ";
                        } else if (xC == -1 && zC == 1) {
                            algString = (xO == -1 && zO == -1) ? " U " : " U' ";
                        }
                    }
                    algString = executeAlg(algString);
                    orientation = ((Entity) cube).getOrientation();
                    currentPos = ((Entity) cube).getCurrentSPosition();

                    // Cube is in correct position in top layer, Algorithms based which way the side with bottom colour and others are facing
                    if (currentPos.x > 0.5 && currentPos.z > 0.5) {
                        if (orientation[5] == 0) {
                            algString = " R'FRF' ";
                        } else if (orientation[5] == 4) {
                            algString = " RUUR'U'FR'F'R ";
                        } else if (orientation[5] == 3) {
                            algString = " FR'F'R ";
                        }
                    } else if (currentPos.x > 0.5 && currentPos.z < -0.5) {
                        if (orientation[5] == 1) {
                            algString = " RB'R'B ";
                        } else if (orientation[5] == 4) {
                            algString = " R'UURUB'RBR' ";
                        } else if (orientation[5] == 3) {
                            algString = " B'RBR' ";
                        }
                    } else if (currentPos.x < -0.5 && currentPos.z > 0.5) {
                        if (orientation[5] == 0) {
                            algString = " LF'L'F ";
                        } else if (orientation[5] == 4) {
                            algString = " L'UULUF'LFL' ";
                        } else if (orientation[5] == 2) {
                            algString = " F'LFL' ";
                        }
                    } else if (currentPos.x < -0.5 && currentPos.z < -0.5) {
                        if (orientation[5] == 1) {
                            algString = " L'BLB' ";
                        } else if (orientation[5] == 4) {
                            algString = " LUUL'U'BL'B'L ";
                        } else if (orientation[5] == 2) {
                            algString = " BL'B'L ";
                        }
                    }
                    algString = executeAlg(algString);
                }
//                printInfo((Entity) cube);
            }
        }
        System.out.println("Solver - Stage Two: Bottom Corners Finished");
        return;
    }

    public void solveStageThree() {
        System.out.println("Solver - Stage Three: Middle Layer");
        for (IEntity cube : this.rubiksCube.getAll()) {
            String algString = "";
            String colours = ((Entity) cube).getColours();
            if (!(colours.contains("W") || colours.contains("Y")) && colours.length() == 2) {
                int[] orientation = ((Entity) cube).getOrientation();
                MyPoint originalPos = ((Entity) cube).getOriginalSPosition();
                MyPoint currentPos = ((Entity) cube).getCurrentSPosition();
                // If cube is in the middle layer and has the wrong position or orientation, put it in the top layer
                if (currentPos.y > -0.5 && currentPos.y < 0.5 && (orientation[0] != 0 || orientation[1] != 1 || orientation[2] != 2 || orientation[3] != 3 || orientation[4] != 4 || orientation[5] != 5)) {
                    if (currentPos.x > 0.5 && currentPos.z > 0.5) {
                        algString = " R'U'R'U'R'URUR ";
                    } else if (currentPos.x > 0.5 && currentPos.z < -0.5) {
                        algString = " RURURU'R'U'R' ";
                    } else if (currentPos.x < -0.5 && currentPos.z > 0.5) {
                        algString = " LULULU'L'U'L' ";
                    } else if (currentPos.x < -0.5 && currentPos.z < -0.5) {
                        algString = " L'U'L'U'L'ULUL ";
                    }
                    algString = executeAlg(algString);
                }
                // cube is in top layer
                orientation = ((Entity) cube).getOrientation();
                currentPos = ((Entity) cube).getCurrentSPosition();
                if (currentPos.y >= 0.5) {
                    printInfo((Entity) cube);
                    // Cube on the opposite side
                    if (Math.abs(currentPos.x - originalPos.x) > 0.5 && Math.abs(currentPos.z - originalPos.z) > 0.5) {
                        algString = " UU ";
                    }
                    algString = executeAlg(algString);
                    orientation = ((Entity) cube).getOrientation();
                    currentPos = ((Entity) cube).getCurrentSPosition();
                    System.out.println(orientation[0] != 0 && orientation[1] != 1 && orientation[2] != 2 && orientation[3] != 3 && orientation[4] != 4 && orientation[5] != 5);;
                    if ((orientation[0] != 0) && (orientation[1] != 1) && (orientation[2] != 2) && (orientation[3] != 3) && (orientation[4] != 4) && (orientation[5] != 5)) {
                        double xC = Math.round(currentPos.x);
                        double zC = Math.round(currentPos.z);
                        double xO = Math.round(originalPos.x);
                        double zO = Math.round(originalPos.z);
                        int angleDiff = getAngle(xO - xC,zO - zC) - getAngle(xC,zC);
                        algString = (angleDiff == 90 || angleDiff == -270) ? " U " : " U' ";
                    }
                    algString = executeAlg(algString);
                    orientation = ((Entity) cube).getOrientation();
                    currentPos = ((Entity) cube).getCurrentSPosition();
                    // Cube is in correct position in top layer, Algorithms based on which face is pointing up
                    if (currentPos.x > 0.5) {
                        algString = orientation[1] == 4 ? " RURURU'R'U'R' " : " R'U'R'U'R'URUR ";
                    } else if (currentPos.x < -0.5) {
                        algString = orientation[1] == 4 ? " L'U'L'U'L'ULUL " : " LULULU'L'U'L' ";
                    } else if (currentPos.z > 0.5) {
                        algString = orientation[2] == 4 ? " F'U'F'U'F'UFUF " : " FUFUFU'F'U'F' ";
                    } else if (currentPos.z < -0.5) {
                        algString = orientation[2] == 4 ? " BUBUBU'B'U'B' " : " B'U'B'U'B'UBUB ";
                    }
                    algString = executeAlg(algString);
                }
            }
        }
        System.out.println("Solver - Stage Three: Middle Layer Finished");
        return;
    }

    public void solveStageFour() {
        System.out.println("Solver - Stage Four: Yellow Cross");
        int pointingUp = 0;
        Entity correctCube1 = null;
        Entity correctCube2 = null;
        for (IEntity cube : this.rubiksCube.getTop()) {
            String colours = ((Entity) cube).getColours();
            int[] orientation = ((Entity) cube).getOrientation();
            MyPoint currentPos = ((Entity) cube).getCurrentSPosition();
            // If we are looking at the center cube, pass
            if (Math.round(currentPos.x) == 0 && Math.round(currentPos.z) == 0) {
                continue;
            }
            // save the cubes to recognise the pattern with
            if (orientation[4] == 4 && colours.length() == 2) {
                pointingUp += 1;
                if (correctCube1 == null) {
                    correctCube1 = (Entity) cube;
                } else {
                    correctCube2 = (Entity) cube;
                }
            }
        }
        // if all cubes are already pointing up, no need to do any algorithms
        if (pointingUp == 4) {
            return;
        }
        int pattern = getCrossPattern(correctCube1, correctCube2);
        String algString = "";
        // Algorithms based on the patterns
        if (pattern == 1) {
            algString = " FRUR'U'F'BULU'L'B' ";
        } else if (pattern == 2) {
            MyPoint currentPos1 = ((Entity) correctCube1).getCurrentSPosition();
            algString = (Math.round(currentPos1.x) == 0) ? " UFRUR'U'F' " : " FRUR'U'F' ";
        } else if (pattern == 3) {
            MyPoint currentPos1 = ((Entity) correctCube1).getCurrentSPosition();
            MyPoint currentPos2 = ((Entity) correctCube2).getCurrentSPosition();
            double x1 = Math.round(currentPos1.x);
            double z1 = Math.round(currentPos1.z);
            double x2 = Math.round(currentPos2.x);
            double z2 = Math.round(currentPos2.z);
            if ((x1 == 0 && z1 == 1 && x2 == -1 && z2 == 0) || (x1 == -1 && z1 == 0 && x2 == 0 && z2 == 1)) {
                algString = " U'";
            } else if ((x1 == -1 && z1 == 0 && x2 == 0 && z2 == -1) || (x1 == 0 && z1 == -1 && x2 == -1 && z2 == 0)){
                algString = " UU";
            } else if ((x1 == 0 && z1 == -1 && x2 == 1 && z2 == 0) || (x1 == 1 && z1 == 0 && x2 == 0 && z2 == -1)){
                algString = " U";
            }
            algString += "BULU'L'B' ";
        }
        algString = executeAlg(algString);
        System.out.println("Solver - Stage Four: Yellow Cross Finished");
        return;
    }

    public void printInfo(Entity cube) {
        double cubeSize = cube.getSize();
        int[] orientation = cube.getOrientation();
        MyPoint cubeOriginalPos = cube.getOriginalSPosition();
        MyPoint cubeCurrentPos = cube.getCurrentSPosition();
        System.out.printf("%s : %s\n" , cube.getID(), cube.getColours());
        System.out.printf("     Original Position: (%.2f, %.2f, %.2f)\n", cubeOriginalPos.x, cubeOriginalPos.y, cubeOriginalPos.z);
        System.out.printf("     Current Position: (%.2f, %.2f, %.2f)\n", cubeCurrentPos.x, cubeCurrentPos.y, cubeCurrentPos.z);
        System.out.printf("     Size: %.2f\n", cubeSize);
        System.out.printf("             | %6s %6s %6s %6s %6s %6s\n", "Front", "Back", "Left", "Right", "Top", "Bottom");
        System.out.printf("     Pointing| %6d %6d %6d %6d %6d %6d\n", orientation[0], orientation[1], orientation[2], orientation[3], orientation[4], orientation[5]);
        System.out.printf("     Pointing|");
        for (int i = 0; i < orientation.length; i++) {
            String temp = "";
            if (orientation[i] == 0) {
                temp = "Front";
            } else if (orientation[i] == 1) {
                temp = "Back";
            } else if (orientation[i] == 2) {
                temp = "Left";
            } else if (orientation[i] == 3) {
                temp = "Right";
            } else if (orientation[i] == 4) {
                temp = "Up";
            } else if (orientation[i] == 5) {
                temp = "Down";
            }
            System.out.printf(" %6s", temp);
        }
        System.out.println("");
    }

    public String executeAlg(String algString) {
        System.out.printf("          Current Algo: %s\n", algString);
        for (int i = 0; i < algString.length(); i++) {
            Character curr = algString.charAt(i);
            if (curr.equals('\'') || curr.equals(' ')){
                continue;
            }
            boolean dir = ((Character) algString.charAt(i+1)).equals('\'') ? false : true;
            if (curr.equals('F')) {
                this.rubiksCube.forward(this.axis, dir, rSpeed);
            } else if (curr.equals('B')) {
                this.rubiksCube.backward(this.axis, dir, rSpeed);
            } else if (curr.equals('L')) {
                this.rubiksCube.left(this.axis, dir, rSpeed);
            } else if (curr.equals('R')) {
                this.rubiksCube.right(this.axis, dir, rSpeed);
            } else if (curr.equals('U')) {
                this.rubiksCube.up(this.axis, dir, rSpeed);
            } else if (curr.equals('D')) {
                this.rubiksCube.down(this.axis, dir, rSpeed);
            }
        }
        return "";
    }

    public void sleep() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int getAngle(double x, double y){
        int angle = 0;
        if (Math.round(x) == 0 && Math.round(y) == 1){
            angle = 90;
        } else if (Math.round(x) == -1 && Math.round(y) == 0){
            angle = 180;
        } else if (Math.round(x) == 0 && Math.round(y) == -1){
            angle = 270;
        }
        return angle;
    }

    private int getCrossPattern(Entity cube1, Entity cube2) {
        // no cube provided (no cubes were pointing up other than the middle cube)
        if (cube1 == null) {
            return 1;
        }
        MyPoint currentPos1 = ((Entity) cube1).getCurrentSPosition();
        double x1 = Math.round(currentPos1.x);
        double z1 = Math.round(currentPos1.z);
        MyPoint currentPos2 = ((Entity) cube2).getCurrentSPosition();
        double x2 = Math.round(currentPos2.x);
        double z2 = Math.round(currentPos2.z);
        // Straight line pattern
        if (x1 == x2 || z1 == z2) {
            return 2;
        } // "r" pattern
        else {
            return 3;
        }
    }

}
