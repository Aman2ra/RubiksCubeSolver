package Solver;

import Solver.BasicBuilders.*;
import Solver.Entities.Entity;
import Solver.Entities.IEntity;
import Solver.Entities.RubiksCube;

import javax.swing.*;


public class Solver {
    private JTextArea outputPane;

    private RubiksCube testRubiksCube;
    private Axis axis;
    private double testRSpeed = 90.0;
    private String currentAlg;
    private int algIndex;
    private int animationTime;
    private int currentAnimationTime;
    private boolean executingAlg;
    private boolean animationLocked;
    private int[] currentMove;
    private int stage;
    private String message;
    private String currOutputText;

    public Solver(RubiksCube hiddenCube, Axis axis, int animationTime) {
        this.testRubiksCube = hiddenCube;
        this.axis = axis;
        this.animationTime = animationTime;
        this.currentAnimationTime = 0;
        this.algIndex = 0;
        this.currentAlg = "";
        this.stage = 0;
        this.executingAlg = false;
        this.animationLocked = false;
        this.currentMove = new int[2];
    }

    public void solveCube() {
        int success = 0;
        switch (this.stage){
            case 1:
                // Stage One: Bottom [White] Cross
                this.outputPane.append("Solver - Stage One: Bottom Cross\n");
                success = solveStageOne();
                if (success == 1) {
                    this.message = "Solver - Stage One: Bottom Cross Finished\n";
                }
                break;
            case 2:
                // Stage Two: Bottom [White] Corners
                this.outputPane.append("Solver - Stage Two: Bottom Corners\n");
                success = solveStageTwo();
                if (success == 1) {
                    this.message = "Solver - Stage Two: Bottom Corners Finished\n";
                }
                break;
            case 3:
                // Stage Three: Middle Layer Edges
                this.outputPane.append("Solver - Stage Three: Middle Layer\n");
                success = solveStageThree();
                if (success == 1) {
                    this.message = "Solver - Stage Three: Middle Layer Finished\n";
                }
                break;
            case 4:
                // Stage Four: Top [Yellow] Cross (Edge Orientation)
                this.outputPane.append("Solver - Stage Four: Yellow Cross\n");
                success = solveStageFour();
                if (success == 1) {
                    this.message = "Solver - Stage Four: Yellow Cross Finished\n";
                }
                break;
            case 5:
                // Stage Five: Top [Yellow] Corner Orientation
                this.outputPane.append("Solver - Stage Five: Yellow Corners\n");
                success = solveStageFive();
                if (success == 1) {
                    this.message = "Solver - Stage Five: Yellow Corners Finished\n";
                } else if (success == -1) {
                    this.currentAlg = "";
                    this.currentMove[0] = -1;
                    this.currentMove[1] = -1;
                    this.outputPane.append("Solver - Stage Five: ERROR CUBE UNSOLVABLE\n");
                }
                break;
            case 6:
                // Stage Six:  Top [Yellow] Corner Position
                this.outputPane.append("Solver - Stage Six: Top Corner Position\n");
                success = solveStageSix();
                if (success == 1) {
                    this.message = "Solver - Stage Six: Top Corner Position Finished\n";
                }
                break;
            case 7:
                // Stage Seven: Top [Yellow] Edge Position
                this.outputPane.append("Solver - Stage Seven: Top Edge Position\n");
                success = solveStageSeven();
                if (success == 1) {
                    this.message = "Solver - Stage Seven: Top Edge Position Finished\n";
                }
                break;
            default:
                this.currentMove[0] = -1;
                this.currentMove[1] = -1;
                break;
        }
        return;
    }

    public int[] getNextMove() {
        if (!this.executingAlg) {
            this.outputPane.append("--------------------------------------------------------\n");
            this.stage++;
            System.out.printf("getNextMove: Solving next stage: %d\n", this.stage);
            solveCube();
            this.executingAlg = true;
            this.animationLocked = false;
            this.currentAnimationTime = 0;
            this.currOutputText = this.outputPane.getText();
        }
        if (this.executingAlg) {
            executeAlgMain();
            this.animationLocked = true;
            this.currentAnimationTime++;

            // output the algorithm
            String algStr = "";
            //System.out.printf("        getNextMove: Executed move: %d %d\n", this.currentMove[0], this.currentMove[1]);
            //System.out.printf("        getNextMove: ");
            if (this.algIndex != 0) {
                for (int i = 0; i < this.currentAlg.length(); i++) {
                    if (i == this.algIndex) {
                        algStr += "|";
                    }
                    algStr += this.currentAlg.charAt(i);
                }
            }
            algStr += "\n";
            this.outputPane.setText(currOutputText + algStr);
        }
        if (this.currentAnimationTime % this.animationTime == 0) {
            this.animationLocked = false;
            this.currentAnimationTime = 0;
            if (this.currentMove[0] == -1 && this.currentMove[1] == -1){
                algIndex = 0;
                currentAlg = "";
                stage = 0;
                executingAlg = false;
                animationLocked = false;
            }
        }
        return this.currentMove;
    }

    public void executeAlgMain() {
        if (!this.animationLocked) {
            Character curr = 'N';
            if (!this.currentAlg.equals("") && this.algIndex < this.currentAlg.length()) {
                curr = this.currentAlg.charAt(this.algIndex);
            }
            int dir = 1;
            if (this.algIndex + 1 < this.currentAlg.length() && ((Character) this.currentAlg.charAt(algIndex + 1)).equals('\'')) {
                dir = 0;
                this.algIndex++;
            }
            this.algIndex++;
            if (curr.equals('L')) {
                this.currentMove[0] = 1;
                this.currentMove[1] = dir;
            } else if (curr.equals('R')) {
                this.currentMove[0] = 2;
                this.currentMove[1] = dir;
            } else if (curr.equals('F')) {
                this.currentMove[0] = 3;
                this.currentMove[1] = dir;
            } else if (curr.equals('B')) {
                this.currentMove[0] = 4;
                this.currentMove[1] = dir;
            } else if (curr.equals('U')) {
                this.currentMove[0] = 5;
                this.currentMove[1] = dir;
            } else if (curr.equals('D')) {
                this.currentMove[0] = 6;
                this.currentMove[1] = dir;
            } else if (this.currentMove[0] != -1 && this.currentMove[1] != -1){
                this.currOutputText += this.currentAlg + "\n" + this.message;
                this.currentAlg = "";
                this.algIndex = 0;
                this.executingAlg = false;
                this.currentMove[0] = -2;
                this.currentMove[1] = -2;
            }
        }
        return;
    }

    public String executeAlgTest(String algString) {
        this.currentAlg += algString;
        for (int i = 0; i < algString.length(); i++) {
            Character curr = algString.charAt(i);
            if (curr.equals('\'')){
                continue;
            }
            boolean dir = true;
            if (i+1 < algString.length() && ((Character) algString.charAt(i+1)).equals('\'')) {
                dir = false;
            }
            if (curr.equals('F')) {
                this.testRubiksCube.forward(this.axis, dir, testRSpeed);
            } else if (curr.equals('B')) {
                this.testRubiksCube.backward(this.axis, dir, testRSpeed);
            } else if (curr.equals('L')) {
                this.testRubiksCube.left(this.axis, dir, testRSpeed);
            } else if (curr.equals('R')) {
                this.testRubiksCube.right(this.axis, dir, testRSpeed);
            } else if (curr.equals('U')) {
                this.testRubiksCube.up(this.axis, dir, testRSpeed);
            } else if (curr.equals('D')) {
                this.testRubiksCube.down(this.axis, dir, testRSpeed);
            }
        }
        return "";
    }

    public int solveStageOne() {
        for (IEntity cube : this.testRubiksCube.getAll()) {
            String algString = "";
            String colours = ((Entity) cube).getColours();
            if (colours.contains("W") && colours.length() == 2) {
                int[] orientation = ((Entity) cube).getOrientation();
                MyPoint originalPos = ((Entity) cube).getOriginalSPosition();
                MyPoint currentPos = ((Entity) cube).getCurrentSPosition();
                // If cube is in the middle layer, put it in the top layer
                if (currentPos.y > -0.5 && currentPos.y < 0.5) {
                    if (currentPos.x > 0.5 && currentPos.z > 0.5) {
                        algString = "RUR'";
                    } else if (currentPos.x > 0.5 && currentPos.z < -0.5) {
                        algString = "R'UR";
                    } else if (currentPos.x < -0.5 && currentPos.z > 0.5) {
                        algString = "L'UL";
                    } else if (currentPos.x < -0.5 && currentPos.z < -0.5) {
                        algString = "LUL'";
                    }
                    algString = executeAlgTest(algString);
                }
                // If cube is in bottom layer but wrong position
                orientation = ((Entity) cube).getOrientation();
                currentPos = ((Entity) cube).getCurrentSPosition();
                if (currentPos.y < -0.5 && (Math.abs(currentPos.x - originalPos.x) > 0.5 || Math.abs(currentPos.z - originalPos.z) > 0.5 || orientation[5] != 5)) {
                    if (currentPos.x > 0.5) {
                        algString = "RR";
                    } else if (currentPos.x < -0.5) {
                        algString = "LL";
                    } else if (currentPos.z > 0.5) {
                        algString = "FF";
                    } else if (currentPos.z < -0.5) {
                        algString = "BB";
                    }
                    algString = executeAlgTest(algString);
                }
                // cube is in top layer
                orientation = ((Entity) cube).getOrientation();
                currentPos = ((Entity) cube).getCurrentSPosition();
                if (currentPos.y >= 0.5) {
                    // Putting the cube over the correct position
                    int angleDif = getAngle(currentPos.x, currentPos.z) - getAngle(originalPos.x, originalPos.z);
                    if (angleDif == 180 || angleDif == -180) {
                        algString = "UU";
                    } else if (angleDif == 90 || angleDif == -270) {
                        algString = "U'";
                    } else if (angleDif == 270 || angleDif == -90) {
                        algString = "U";
                    }
                    algString = executeAlgTest(algString);
                    orientation = ((Entity) cube).getOrientation();
                    currentPos = ((Entity) cube).getCurrentSPosition();
                    // Cube is in correct position in top layer, Algorithms based which way the side with bottom colour is facing
                    if (originalPos.x > 0.5) {
                        algString = orientation[5] == 4 ? "RR" : "UFR'F'";
                    } else if (originalPos.x < -0.5) {
                        algString = orientation[5] == 4 ? "LL" : "UBL'B'";
                    } else if (originalPos.z > 0.5) {
                        algString = orientation[5] == 4 ? "FF" : "ULF'L'";
                    } else if (originalPos.z < -0.5) {
                        algString = orientation[5] == 4 ? "BB" : "URB'R'";
                    }
                    algString = executeAlgTest(algString);
                }
            }
        }
        return 1;
    }

    public int solveStageTwo() {
        for (IEntity cube : this.testRubiksCube.getAll()) {
            String algString = "";
            String colours = ((Entity) cube).getColours();
            if (colours.contains("W") && colours.length() == 3) {
                int[] orientation = ((Entity) cube).getOrientation();
                MyPoint originalPos = ((Entity) cube).getOriginalSPosition();
                MyPoint currentPos = ((Entity) cube).getCurrentSPosition();
                // If cube is in the bottom layer, put it in the top layer if position and/or orientation is wrong
                if (currentPos.y < -0.5 && (Math.abs(currentPos.x - originalPos.x) > 0.5 || Math.abs(currentPos.z - originalPos.z) > 0.5 ||
                        (orientation[0] != 0 || orientation[1] != 1 || orientation[2] != 2 || orientation[3] != 3 || orientation[4] != 4 || orientation[5] != 5))) {
                    if (currentPos.x > 0.5 && currentPos.z > 0.5) {
                        algString = "RUR'";
                    } else if (currentPos.x > 0.5 && currentPos.z < -0.5) {
                        algString = "R'U'R";
                    } else if (currentPos.x < -0.5 && currentPos.z > 0.5) {
                        algString = "L'U'L";
                    } else if (currentPos.x < -0.5 && currentPos.z < -0.5) {
                        algString = "LUL'";
                    }
                    algString = executeAlgTest(algString);
                }
                // If cube is in top layer but wrong position
                orientation = ((Entity) cube).getOrientation();
                currentPos = ((Entity) cube).getCurrentSPosition();
                if (currentPos.y >= 0.5) {
                    double xC = currentPos.x;
                    double zC = currentPos.z;
                    double xO = originalPos.x;
                    double zO = originalPos.z;
                    // Putting the cube over the correct position
                    if (xC == -xO && zC == -zO) {
                        algString = "UU";
                    } else if (!(xC == xO && zC == zO)) {
                        if (xC == 1 && zC == 1) {
                            algString = (xO == -1 && zO == 1) ? "U" : "U'";
                        } else if (xC == 1 && zC == -1) {
                            algString = (xO == 1 && zO == 1) ? "U" : "U'";
                        } else if (xC == -1 && zC == -1) {
                            algString = (xO == 1 && zO == -1) ? "U" : "U'";
                        } else if (xC == -1 && zC == 1) {
                            algString = (xO == -1 && zO == -1) ? "U" : "U'";
                        }
                    }
                    algString = executeAlgTest(algString);
                    orientation = ((Entity) cube).getOrientation();
                    currentPos = ((Entity) cube).getCurrentSPosition();

                    // Cube is in correct position in top layer, Algorithms based which way the side with bottom colour and others are facing
                    if (currentPos.x > 0.5 && currentPos.z > 0.5) {
                        if (orientation[5] == 0) {
                            algString = "R'FRF'";
                        } else if (orientation[5] == 4) {
                            algString = "RUUR'U'FR'F'R";
                        } else if (orientation[5] == 3) {
                            algString = "FR'F'R";
                        }
                    } else if (currentPos.x > 0.5 && currentPos.z < -0.5) {
                        if (orientation[5] == 1) {
                            algString = "RB'R'B";
                        } else if (orientation[5] == 4) {
                            algString = "R'UURUB'RBR'";
                        } else if (orientation[5] == 3) {
                            algString = "B'RBR'";
                        }
                    } else if (currentPos.x < -0.5 && currentPos.z > 0.5) {
                        if (orientation[5] == 0) {
                            algString = "LF'L'F";
                        } else if (orientation[5] == 4) {
                            algString = "L'UULUF'LFL'";
                        } else if (orientation[5] == 2) {
                            algString = "F'LFL'";
                        }
                    } else if (currentPos.x < -0.5 && currentPos.z < -0.5) {
                        if (orientation[5] == 1) {
                            algString = "L'BLB'";
                        } else if (orientation[5] == 4) {
                            algString = "LUUL'U'BL'B'L";
                        } else if (orientation[5] == 2) {
                            algString = "BL'B'L";
                        }
                    }
                    algString = executeAlgTest(algString);
                }
            }
        }
        return 1;
    }

    public int solveStageThree() {
        for (IEntity cube : this.testRubiksCube.getAll()) {
            String algString = "";
            String colours = ((Entity) cube).getColours();
            if (!(colours.contains("W") || colours.contains("Y")) && colours.length() == 2) {
                int[] orientation = ((Entity) cube).getOrientation();
                MyPoint originalPos = ((Entity) cube).getOriginalSPosition();
                MyPoint currentPos = ((Entity) cube).getCurrentSPosition();
                // If cube is in the middle layer and has the wrong position or orientation, put it in the top layer
                if (currentPos.y > -0.5 && currentPos.y < 0.5 && (orientation[0] != 0 || orientation[1] != 1 || orientation[2] != 2 || orientation[3] != 3 || orientation[4] != 4 || orientation[5] != 5)) {
                    if (currentPos.x > 0.5 && currentPos.z > 0.5) {
                        algString = "R'U'R'U'R'URUR";
                    } else if (currentPos.x > 0.5 && currentPos.z < -0.5) {
                        algString = "RURURU'R'U'R'";
                    } else if (currentPos.x < -0.5 && currentPos.z > 0.5) {
                        algString = "LULULU'L'U'L'";
                    } else if (currentPos.x < -0.5 && currentPos.z < -0.5) {
                        algString = "L'U'L'U'L'ULUL";
                    }
                    algString = executeAlgTest(algString);
                }
                // cube is in top layer
                orientation = ((Entity) cube).getOrientation();
                currentPos = ((Entity) cube).getCurrentSPosition();
                if (currentPos.y >= 0.5) {
                    // Cube on the opposite side
                    if (Math.abs(currentPos.x - originalPos.x) > 0.5 && Math.abs(currentPos.z - originalPos.z) > 0.5) {
                        algString = "UU";
                    }
                    algString = executeAlgTest(algString);
                    orientation = ((Entity) cube).getOrientation();
                    currentPos = ((Entity) cube).getCurrentSPosition();
                    if ((orientation[0] != 0) && (orientation[1] != 1) && (orientation[2] != 2) && (orientation[3] != 3) && (orientation[4] != 4) && (orientation[5] != 5)) {
                        double xC = currentPos.x;
                        double zC = currentPos.z;
                        double xO = originalPos.x;
                        double zO = originalPos.z;
                        int angleDiff = getAngle(xO - xC, zO - zC) - getAngle(xC, zC);
                        algString = (angleDiff == 90 || angleDiff == -270) ? "U" : "U'";
                    }
                    algString = executeAlgTest(algString);
                    orientation = ((Entity) cube).getOrientation();
                    currentPos = ((Entity) cube).getCurrentSPosition();
                    // Cube is in correct position in top layer, Algorithms based on which face is pointing up
                    if (currentPos.x > 0.5) {
                        algString = orientation[1] == 4 ? "RURURU'R'U'R'" : "R'U'R'U'R'URUR";
                    } else if (currentPos.x < -0.5) {
                        algString = orientation[1] == 4 ? "L'U'L'U'L'ULUL" : "LULULU'L'U'L'";
                    } else if (currentPos.z > 0.5) {
                        algString = orientation[2] == 4 ? "F'U'F'U'F'UFUF" : "FUFUFU'F'U'F'";
                    } else if (currentPos.z < -0.5) {
                        algString = orientation[2] == 4 ? "BUBUBU'B'U'B'" : "B'U'B'U'B'UBUB";
                    }
                    algString = executeAlgTest(algString);
                }
            }
        }
        return 1;
    }

    public int solveStageFour() {
        int pointingUp = 0;
        Entity correctCube1 = null;
        Entity correctCube2 = null;
        for (IEntity cube : this.testRubiksCube.getTop()) {
            String colours = ((Entity) cube).getColours();
            int[] orientation = ((Entity) cube).getOrientation();
            MyPoint currentPos = ((Entity) cube).getCurrentSPosition();
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
            return 1;
        }
        int pattern = getCrossPattern(correctCube1, correctCube2);
        String algString = "";
        // Algorithms based on the patterns
        if (pattern == 1) {
            algString = "FRUR'U'F'BULU'L'B'";
        } else if (pattern == 2) {
            MyPoint currentPos1 = ((Entity) correctCube1).getCurrentSPosition();
            algString = currentPos1.x == 0 ? "UFRUR'U'F'" : "FRUR'U'F'";
        } else if (pattern == 3) {
            MyPoint currentPos1 = ((Entity) correctCube1).getCurrentSPosition();
            MyPoint currentPos2 = ((Entity) correctCube2).getCurrentSPosition();
            double x1 = currentPos1.x;
            double z1 = currentPos1.z;
            double x2 = currentPos2.x;
            double z2 = currentPos2.z;
            if ((x1 == 0 && z1 == 1 && x2 == -1 && z2 == 0) || (x1 == -1 && z1 == 0 && x2 == 0 && z2 == 1)) {
                algString = "U'";
            } else if ((x1 == -1 && z1 == 0 && x2 == 0 && z2 == -1) || (x1 == 0 && z1 == -1 && x2 == -1 && z2 == 0)) {
                algString = "UU";
            } else if ((x1 == 0 && z1 == -1 && x2 == 1 && z2 == 0) || (x1 == 1 && z1 == 0 && x2 == 0 && z2 == -1)) {
                algString = "U";
            }
            algString += "BULU'L'B'";
        }
        algString = executeAlgTest(algString);
        return 1;
    }

    public int solveStageFive() {
        while (true) {
            int pointingUp = 0;
            int pointingSide = 0;
            Entity[] yellowCorners = new Entity[4];
            for (IEntity cube : this.testRubiksCube.getTop()) {
                String colours = ((Entity) cube).getColours();
                int[] orientation = ((Entity) cube).getOrientation();
                // save the corner cubes to recognise the pattern with
                if (orientation[4] == 4 && colours.length() == 3) {
                    yellowCorners[pointingUp] = (Entity) cube;
                    pointingUp++;
                } else if (colours.length() == 3) {
                    yellowCorners[3 - pointingSide] = (Entity) cube;
                    pointingSide++;
                }
            }
            // All corners already pointing up
            if (pointingUp == 4) {
                return 1;
            } else if (pointingUp == 3) {
                return -1;
            }
            // Best case, orient that cube to the bottom left then do the algorithm
            String algString = "";
            if (pointingUp == 1) {
                MyPoint currentPos = yellowCorners[0].getCurrentSPosition();
                double xC = currentPos.x;
                double zC = currentPos.z;
                if (xC == -1 && zC == -1) {
                    algString = "U'";
                } else if (xC == 1 && zC == -1) {
                    algString = "UU";
                } else if (xC == 1 && zC == 1) {
                    algString = "U";
                }
            }
            if (pointingUp == 2) {
                MyPoint currentPos1 = yellowCorners[0].getCurrentSPosition();
                double xC1 = currentPos1.x;
                double zC1 = currentPos1.z;
                MyPoint currentPos2 = yellowCorners[1].getCurrentSPosition();
                double xC2 = currentPos2.x;
                double zC2 = currentPos2.z;
                // Two opposite corners are pointing up
                if (xC1 == -xC2 && zC1 == -zC2) {
                    if (xC1 == 1 && zC1 == -1 || xC2 == 1 && zC2 == -1) {
                        algString += "U";
                    }
                } // Corners are on the same side
                else {
                    MyPoint currentPos3 = yellowCorners[2].getCurrentSPosition();
                    int[] orientation3 = yellowCorners[2].getOrientation();
                    double xC3 = currentPos3.x;
                    double zC3 = currentPos3.z;
                    MyPoint currentPos4 = yellowCorners[3].getCurrentSPosition();
                    int[] orientation4 = yellowCorners[3].getOrientation();
                    double xC4 = currentPos4.x;
                    double zC4 = currentPos4.z;
                    // Orientation of yellow are facing same side, put them at bottom, otherwise put them on left
                    if (zC3 == -1 && zC4 == -1) {
                        algString += (orientation3[0] == orientation4[0] || orientation3[1] == orientation4[1] || orientation3[2] == orientation4[2] || orientation3[3] == orientation4[3] || orientation3[4] == orientation4[4] || orientation3[5] == orientation4[5]) ? "UU" : "U'";
                    } else if (xC3 == -1 && xC4 == -1) {
                        algString += (orientation3[0] == orientation4[0] || orientation3[1] == orientation4[1] || orientation3[2] == orientation4[2] || orientation3[3] == orientation4[3] || orientation3[4] == orientation4[4] || orientation3[5] == orientation4[5]) ? "U'" : "";
                    } else if (xC3 == 1 && xC4 == 1) {
                        algString += (orientation3[0] == orientation4[0] || orientation3[1] == orientation4[1] || orientation3[2] == orientation4[2] || orientation3[3] == orientation4[3] || orientation3[4] == orientation4[4] || orientation3[5] == orientation4[5]) ? "U" : "UU";
                    } else {
                        algString += (orientation3[0] == orientation4[0] || orientation3[1] == orientation4[1] || orientation3[2] == orientation4[2] || orientation3[3] == orientation4[3] || orientation3[4] == orientation4[4] || orientation3[5] == orientation4[5]) ? "" : "U";
                    }
                }
            }
            algString += "RUR'URUUR'";
            algString = executeAlgTest(algString);
        }
    }

    public int solveStageSix() {
        Entity[] yellowCorners = new Entity[4];
        int found = 0;
        for (IEntity cube : this.testRubiksCube.getTop()) {
            String colours = ((Entity) cube).getColours();
            // save the corner cubes to recognise the pattern with
            if (colours.length() == 3) {
                yellowCorners[found] = (Entity) cube;
                found++;
            }
        }
        while (true) {
            Entity[] cubePair1 = {null, null};
            Entity[] cubePair2 = {null, null};
            int[] pair1Index = {-1, -1};
            for (int i = 0; i < yellowCorners.length; i++) {
                for (int j = 0; j < yellowCorners.length; j++) {
                    int[] orientation1 = yellowCorners[i].getOrientation();
                    int[] orientation2 = yellowCorners[j].getOrientation();
                    MyPoint currentPos1 = yellowCorners[i].getCurrentSPosition();
                    MyPoint currentPos2 = yellowCorners[j].getCurrentSPosition();
                    // If theyre not the same cube and they are not diagonally opposite to each other
                    if ((i != j) && (orientation1[0] == orientation2[0] || orientation1[1] == orientation2[1] || orientation1[2] == orientation2[2] || orientation1[3] == orientation2[3]) && !(currentPos1.x == currentPos2.z && currentPos2.x == currentPos1.z)) {
                        // Pair one has not been assigned
                        if ((cubePair1[0] == null) && (cubePair1[1] == null)) {
                            cubePair1[0] = yellowCorners[i];
                            cubePair1[1] = yellowCorners[j];
                            pair1Index[0] = i;
                            pair1Index[1] = j;
                        } // Found another pair, check if that isnt already in pair 1
                        else if ((i != pair1Index[0] && j != pair1Index[1]) && (j != pair1Index[0] && i != pair1Index[1])) {
                            cubePair2[0] = yellowCorners[i];
                            cubePair2[1] = yellowCorners[j];
                        }
                    }
                }
            }
            // All corners are oriented properly (relative to each other)
            if ((cubePair1[0] != null) && (cubePair1[1] != null) && (cubePair2[0] != null) && (cubePair2[1] != null)) {
                return 1;
            }

            String algString = "";
            if ((cubePair1[0] != null) && (cubePair1[1] != null)) {
                MyPoint currentPos1 = cubePair1[0].getCurrentSPosition();
                double xC1 = currentPos1.x;
                double zC1 = currentPos1.z;
                MyPoint currentPos2 = cubePair1[1].getCurrentSPosition();
                double xC2 = currentPos2.x;
                double zC2 = currentPos2.z;
                if (xC1 == -1 && xC2 == -1) {
                    algString += "U";
                } else if (xC1 == 1 && xC2 == 1) {
                    algString += "U'";
                } else if (zC1 == 1 && zC2 == 1) {
                    algString += "UU";
                }
            }

            algString += "R'FR'BBRF'R'BBRR";
            algString = executeAlgTest(algString);
        }
    }

    public int solveStageSeven() {
        Entity[][] edgePairs = new Entity[4][3];
        for (IEntity cube : this.testRubiksCube.getTop()) {
            String colours = ((Entity) cube).getColours();
            MyPoint pos = ((Entity) cube).getCurrentSPosition();
            // save the corner cubes to recognise the pattern with
            if (colours.length() == 3) {
                if (pos.x == 1 && pos.z == 1) {
                    edgePairs[0][0] = (Entity) cube;
                    edgePairs[3][2] = (Entity) cube;
                } else if (pos.x == -1 && pos.z == 1) {
                    edgePairs[1][0] = (Entity) cube;
                    edgePairs[0][2] = (Entity) cube;
                } else if (pos.x == -1 && pos.z == -1) {
                    edgePairs[2][0] = (Entity) cube;
                    edgePairs[1][2] = (Entity) cube;
                } else {
                    edgePairs[3][0] = (Entity) cube;
                    edgePairs[2][2] = (Entity) cube;
                }
            } else if (colours.length() == 2) {
                if (pos.x == 0 && pos.z == 1) {
                    edgePairs[0][1] = (Entity) cube;
                } else if (pos.x == -1 && pos.z == 0) {
                    edgePairs[1][1] = (Entity) cube;
                } else if (pos.x == 0 && pos.z == -1) {
                    edgePairs[2][1] = (Entity) cube;
                } else {
                    edgePairs[3][1] = (Entity) cube;
                }
            }
        }

        while (true) {
            int correctOrientations = 0;
            String algString = "";
            for (Entity[] edge : edgePairs) {
                int[] corner1Orientation = edge[0].getOrientation();
                int[] edgeOrientation = edge[1].getOrientation();
                int[] corner2Orientation = edge[2].getOrientation();
                // if all their colours are pointing the same way
                if (((corner1Orientation[0] == edgeOrientation[0]) && (corner2Orientation[0] == edgeOrientation[0])) &&
                        ((corner1Orientation[1] == edgeOrientation[1]) && (corner2Orientation[1] == edgeOrientation[1])) &&
                        ((corner1Orientation[2] == edgeOrientation[2]) && (corner2Orientation[2] == edgeOrientation[2])) &&
                        ((corner1Orientation[3] == edgeOrientation[3]) && (corner2Orientation[3] == edgeOrientation[3]))) {
                    MyPoint edgePosition = edge[1].getCurrentSPosition();
                    if (edgePosition.x == 1 && edgePosition.z == 0) {
                        algString = "U'";
                    } else if (edgePosition.x == 0 && edgePosition.z == 1) {
                        algString = "UU";
                    } else if (edgePosition.x == -1 && edgePosition.z == 0) {
                        algString = "U";
                    }
                    correctOrientations++;
                }
            }
            algString = executeAlgTest(algString);
            // All edges are alligned
            if (correctOrientations == 4) {
                MyPoint currentPos = edgePairs[0][1].getCurrentSPosition();
                MyPoint originalPos = edgePairs[0][1].getOriginalSPosition();
                int angleDif = getAngle(currentPos.x, currentPos.z) - getAngle(originalPos.x, originalPos.z);
                if (angleDif == 180 || angleDif == -180) {
                    this.outputPane.append("UU");
                    algString = "UU";
                } else if (angleDif == 90 || angleDif == -270) {
                    this.outputPane.append("U'");
                    algString = "U'";
                } else if (angleDif == 270 || angleDif == -90) {
                    this.outputPane.append("U");
                    algString = "U";
                }
                System.out.printf("\n");
                algString = executeAlgTest(algString);
                return 1;
            }
            algString = "RU'RURURU'R'U'RR";
            algString = executeAlgTest(algString);
        }
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
        this.outputPane.append("");
    }

    private int getAngle(double x, double y){
        int angle = 0;
        if (x == 0 && y == 1){
            angle = 90;
        } else if (x == -1 && y == 0){
            angle = 180;
        } else if (x == 0 && y == -1){
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
        double x1 = currentPos1.x;
        double z1 = currentPos1.z;
        MyPoint currentPos2 = ((Entity) cube2).getCurrentSPosition();
        double x2 = currentPos2.x;
        double z2 = currentPos2.z;
        // Straight line pattern
        if (x1 == x2 || z1 == z2) {
            return 2;
        } // "r" pattern
        else {
            return 3;
        }
    }

    public boolean isAnimationLocked() {
        return this.animationLocked;
    }

    public void setAnimationTime(int animationTime) {
        this.animationTime = animationTime;
        this.currentAnimationTime = 0;
    }

    public void setOutputPane(JTextArea textPane) {
        this.outputPane = textPane;
    }
    

}
