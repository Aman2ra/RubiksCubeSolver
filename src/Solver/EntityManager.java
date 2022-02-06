package Solver;

import Solver.BasicBuilders.*;
import Solver.Entities.Entity;
import Solver.Entities.IEntity;
import Solver.Entities.RubiksCube;
import Solver.UserInputs.ClickType;
import Solver.UserInputs.Keyboard;
import Solver.UserInputs.Mouse;
import Solver.UserInputs.UserInput;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class EntityManager {
    private List<IEntity> entities;

    private int initialX, initialY;
    private double rSpeedX = 1/2.5;
    private double rSpeedY = 1/2.5;
    private double rSpeedZ = 1/2.5;
    private double moveSpeed = 10;

    private boolean direction = true;
    private int animationTime = 20;
    private int animationTimeLeft = animationTime;
    private double rotateSpeed = 90.0/ ((double) animationTime);
    private boolean locked = false;
    private boolean autoModeLock = false;
    private boolean scramble = false;
    private boolean solve = false;
    private int numMoves = 0;
    private int currentMove;
    private int randomMoves = 100;

    private Mouse mouse;
    private Keyboard keyboard;
    protected Camera camera;
    private RubiksCube rubiksCube;
    private RubiksCube hiddenRubiksCube;
    private Axis axis;
    private Solver solver;

    private boolean changeAnimationSpeed = false;
    private int newAnimationTime;

    private JTextArea outputPane;

    public EntityManager() {
        this.entities = new ArrayList<IEntity>();
        this.camera = new Camera(0,0,750);
    }

    public void init(UserInput userInput) {
        int dist = 0;
        int size = 100;
        this.mouse = userInput.mouse;
        this.keyboard = userInput.keyboard;
        this.axis = new Axis();

        Color[] face_colours = {Color.RED, new Color(255,120,30), Color.BLUE, Color.GREEN, Color.YELLOW, Color.WHITE, Color.BLACK};
        this.rubiksCube = new RubiksCube(size,0,0,0,dist, face_colours);
        this.hiddenRubiksCube = new RubiksCube(size,0,0,0,dist, face_colours);
        this.rubiksCube.setVisible(true);
        this.hiddenRubiksCube.setVisible(false);
        this.entities.add(this.rubiksCube);
        this.entities.add(this.hiddenRubiksCube);

        this.solver = new Solver(this.hiddenRubiksCube, this.axis, this.animationTime);
        this.solver.setOutputPane(this.outputPane);

        //this.entities.add(Entity.axis(5,2,0,0,0));
        //this.entities.add(Entity.axis(5,1,0,0,0));
        //this.entities.add(Entity.axis(5,0,0,0,0));
        for (IEntity entity : this.entities) {
            entity.translate(-this.camera.getX(), -this.camera.getY(), -this.camera.getZ());
        }
        sortEntities();

    }

    public void update() {
        int x = this.mouse.getX();
        int y = this.mouse.getY();
        if (this.mouse.getButton() == ClickType.LeftClick){
            double yRotation = -(x - initialX)*rSpeedX;
            double xRotation = -(y - initialY)*rSpeedY;
            this.rotate(true, xRotation, yRotation, 0);
        } else if (this.mouse.getButton() == ClickType.RightClick){
            double zRotation = (x - initialX)*rSpeedZ;
            this.rotate(false, 0, 0, zRotation);
        }

        if (this.mouse.isScrollingUp()) {
            PointConverter.zoomIn();
        } else if (this.mouse.isScrollingDown()) {
            PointConverter.zoomOut();
        }

        if (this.camButton) {
            this.translate(this.camMoveX, this.camMoveY, this.camMoveZ);
            this.camButton = false;
            this.camMoveX = 0;
            this.camMoveY = 0;
            this.camMoveZ = 0;
        }

        if (!this.keyboard.getAlt()) {
            if (this.keyboard.getLeft()) {
                this.translate(-moveSpeed,0,0);
            } else if (this.keyboard.getRight()) {
                this.translate(moveSpeed,0,0);
            } else if (this.keyboard.getForward()) {
                this.translate(0,0,-moveSpeed);
            } else if (this.keyboard.getBackward()) {
                this.translate(0,0,moveSpeed);
            } else if (this.keyboard.getUp()) {
                this.translate(0,moveSpeed,0);
            } else if (this.keyboard.getDown()) {
                this.translate(0,-moveSpeed,0);
            } else if (this.keyboard.getScramble() && !this.autoModeLock) {
                this.startScramble();
            } else if (this.keyboard.getSolve() && !this.autoModeLock) {
                this.startSolve();
            }
        }

        if (this.autoModeLock && this.scramble) {
            this.randomMove();
            this.numMoves--;
            if (this.numMoves == 0){
                this.autoModeLock = false;
                this.scramble = false;
                this.outputScrambleStats();
            }
        } else if (this.autoModeLock && this.solve) {
            int[] solverMove = this.solver.getNextMove();
            if (solverMove[0] == -1 && solverMove[1] == -1) {
                this.outputPane.append("----------Finished solving----------\n");
                this.outputPane.append("____________________________________\n");
                this.autoModeLock = false;
                this.solve = false;
            } else {
                this.currentMove = solverMove[0];
                this.direction = solverMove[1] == 1 ? true : false;
                this.rotateCube(solverMove[0], direction);
            }
        } else if (this.locked) {
            this.rotateCube(currentMove, direction);
            this.animationTimeLeft--;
            if (this.animationTimeLeft == 0) {
                this.locked = false;
                this.animationTimeLeft = animationTime;
            }
        } else if (this.keyboard.getAlt()) {
            this.direction = true;
            if (this.keyboard.getReverse()) {
                this.direction = false;
            }

            if (this.keyboard.getLeft()) {
                this.currentMove = 1;
                this.locked = true;
            } else if (this.keyboard.getRight()) {
                this.currentMove = 2;
                this.locked = true;
            } else if (this.keyboard.getUp()) {
                this.currentMove = 3;
                this.locked = true;
            } else if (this.keyboard.getDown()) {
                this.currentMove = 4;
                this.locked = true;
            } else if (this.keyboard.getForward()) {
                this.currentMove = 5;
                this.locked = true;
            } else if (this.keyboard.getBackward()) {
                this.currentMove = 6;
                this.locked = true;
            }
        }

        if (this.changeAnimationSpeed && ((!this.locked && !this.autoModeLock)||(this.scramble && this.numMoves % this.animationTime == 0)||(this.solve && !this.solver.isAnimationLocked()))) {
            updateAnimationTime();
        }

        this.mouse.resetScroll();
        this.keyboard.update();

        this.initialX = x;
        this.initialY = y;

    }

    public void rotateCube(int move, boolean direction) {
        switch (move) {
            case 1:
                this.rubiksCube.left(this.axis, direction, this.rotateSpeed);
                break;
            case 2:
                this.rubiksCube.right(this.axis, direction, this.rotateSpeed);
                break;
            case 3:
                this.rubiksCube.forward(this.axis, direction, this.rotateSpeed);
                break;
            case 4:
                this.rubiksCube.backward(this.axis, direction, this.rotateSpeed);
                break;
            case 5:
                this.rubiksCube.up(this.axis, direction, this.rotateSpeed);
                break;
            case 6:
                this.rubiksCube.down(this.axis, direction, this.rotateSpeed);
                break;
            default:
                System.out.printf("No Move: %d %b\n", move, direction);
                return;
        }
        if (!this.solve) {
            switch (move) {
                case 1:
                    this.hiddenRubiksCube.left(this.axis, direction, this.rotateSpeed);
                    break;
                case 2:
                    this.hiddenRubiksCube.right(this.axis, direction, this.rotateSpeed);
                    break;
                case 3:
                    this.hiddenRubiksCube.forward(this.axis, direction, this.rotateSpeed);
                    break;
                case 4:
                    this.hiddenRubiksCube.backward(this.axis, direction, this.rotateSpeed);
                    break;
                case 5:
                    this.hiddenRubiksCube.up(this.axis, direction, this.rotateSpeed);
                    break;
                case 6:
                    this.hiddenRubiksCube.down(this.axis, direction, this.rotateSpeed);
                    break;
                default:
                    System.out.printf("No Move (Solver): %d %b\n", move, direction);
                    return;
            }
        }
        sortEntities();
        return;
    }

    private void sleep() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void render(Graphics g) {
        for(IEntity entity : this.entities){
            entity.render(g);
        }
    }

    public void sortEntities() {
        this.rubiksCube.sortEntities();
        this.hiddenRubiksCube.sortEntities();
        Entity.sortEntities(this.entities);
    }

    private void translate(double x, double y, double z){
        this.camera.translate(x, y, z);
        this.axis.translate(-x,-y,-z);
        for (IEntity entity : this.entities) {
            entity.translate(-x, -y, -z);
        }
        sortEntities();
    }

    private void rotate(boolean CW, double xDegrees, double yDegrees, double zDegrees) {
        this.camera.rotate(!CW, xDegrees, yDegrees,zDegrees);
        this.axis.rotate(CW, xDegrees, yDegrees, zDegrees);
        for(IEntity entity : this.entities){
            entity.rotate(CW, xDegrees, yDegrees, zDegrees);
        }
        sortEntities();
    }

    public MyPoint getCameraPos() {
        return this.camera.getPosition();
    }
    public MyPoint getAxisPos() {
        return this.axis.getPosition();
    }
    public MyPoint getXAxis() {
        return this.axis.getXAxis();
    }
    public MyPoint getYAxis() {
        return this.axis.getYAxis();
    }
    public MyPoint getZAxis() {
        return this.axis.getZAxis();
    }


    private int currentRandMove;
    private boolean currentRandDir;

    public void randomMove(){
        Random random = new Random();
        if (this.numMoves % this.animationTime == 0) {
            this.currentRandMove = random.nextInt(1, 7);
            int dir = random.nextInt(0, 2);
            this.currentRandDir = dir == 1 ? true:false;
            this.moveCounter[this.currentRandMove-1]++;
            this.dirCounter[dir]++;
        }
        this.rotateCube(this.currentRandMove, this.currentRandDir);
    }

    public void rotateCube2(int dir){
        if (!this.locked) {
            this.currentMove = dir;
            this.locked = true;
        }
    }

    boolean camButton = false;
    double camMoveX = 0;
    double camMoveY = 0;
    double camMoveZ = 0;
    public void moveCamera(int x, int y, int z) {
        this.camButton = true;
        this.camMoveX = x * this.moveSpeed;
        this.camMoveY = y * this.moveSpeed;
        this.camMoveZ = z * this.moveSpeed;
    }

    private int[] moveCounter = {0, 0, 0, 0, 0, 0};
    private int[] dirCounter = {0, 0};

    public void startScramble(){
        this.outputPane.append("----------STARTING SCRAMBLE----------\n");
        this.moveCounter = new int[]{0,0,0,0,0,0};
        this.dirCounter = new int[]{0,0};
        this.autoModeLock = true;
        this.scramble = true;
        this.numMoves = randomMoves*animationTime;
    }

    public void startSolve(){
        this.outputPane.append("------------STARTING SOLVE-----------\n");
        this.autoModeLock = true;
        this.solve = true;
    }

    public void replaceColor(Color newColor, Color prevColor) {
        this.rubiksCube.replaceColor(newColor, prevColor);
        //this.hiddenRubiksCube.replaceColor(newColor, prevColor);
    }

    public void setAnimationSpeed(int animationSpeed) {
        this.changeAnimationSpeed = true;
        this.newAnimationTime = animationSpeed;
    }

    public void setRandomMoves(int numMoves) {
        this.randomMoves = numMoves;
    }

    public void setMoveSpeed(int moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    private void updateAnimationTime(){
        if (this.scramble) {
            this.numMoves = (this.numMoves/this.animationTime)*this.newAnimationTime;
        }
        this.animationTime = newAnimationTime;
        this.animationTimeLeft = this.animationTime;
        this.rotateSpeed = 90.0 / ((double) this.animationTime);
        this.changeAnimationSpeed = false;
        this.newAnimationTime = 0;
        this.solver.setAnimationTime(this.animationTime);
    }

    public void setOutputPane(JTextArea textPane) {
        this.outputPane = textPane;
    }

    public void outputScrambleStats() {
        this.outputPane.append("----------SCRAMBLE STATISTICS----------\n");
        this.outputPane.append("Total Moves = " + this.randomMoves + "\n");
        this.outputPane.append("Moves: \n");
        this.outputPane.append("    Left = " + this.moveCounter[0] + "\n");
        this.outputPane.append("    Right = " + this.moveCounter[1] + "\n");
        this.outputPane.append("    Front = " + this.moveCounter[2] + "\n");
        this.outputPane.append("    Back = " + this.moveCounter[3] + "\n");
        this.outputPane.append("    Top = " + this.moveCounter[4] + "\n");
        this.outputPane.append("    Bottom = " + this.moveCounter[5] + "\n");

        this.outputPane.append("\nDirection: \n");
        this.outputPane.append("    Clockwise (Normal) = " + this.dirCounter[1] + "\n");
        this.outputPane.append("    Counter-Clockise (Prime) = " + this.dirCounter[0] + "\n");
        this.outputPane.append("_______________________________________" + "\n");
    }
}
