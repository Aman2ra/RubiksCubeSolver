package Solver.Temp;

import Solver.BasicBuilders.Axis;
import Solver.BasicBuilders.MyPoint;
import Solver.BasicBuilders.PointConverter;
import Solver.Camera;
import Solver.UserInputs.ClickType;
import Solver.UserInputs.Keyboard;
import Solver.UserInputs.Mouse;
import Solver.UserInputs.UserInput;

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
//    private int animationTime = 90;
    private int animationTime = 1;
    private int animationTimeLeft = animationTime;
    private double rotateSpeed = 90.0/animationTime;
    private boolean locked = false;
    private boolean autoModeLock = false;
    private boolean scramble = false;
    private boolean solve = false;
    private int numMoves = 0;
    private int currentMove;
    private int counter;

    private Mouse mouse;
    private Keyboard keyboard;
    protected Camera camera;
    private RubiksCube rubiksCube;
    private Axis axis;
    private Solver solver;


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
        this.entities.add(this.rubiksCube);

        this.solver = new Solver(this.rubiksCube, this.axis);

        this.entities.add(Entity.axis(5,2,0,0,0));
        this.entities.add(Entity.axis(5,1,0,0,0));
        this.entities.add(Entity.axis(5,0,0,0,0));
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

        if (!this.keyboard.getAlt()) {
            if (this.keyboard.getLeft()) {
                this.translate(-moveSpeed,0,0);
            } else if (this.keyboard.getRight()) {
                this.translate(moveSpeed,0,0);
            } else if (this.keyboard.getForward()) {
                this.translate(0,moveSpeed,0);
            } else if (this.keyboard.getBackward()) {
                this.translate(0,-moveSpeed,0);
            } else if (this.keyboard.getUp()) {
                this.translate(0,0,-moveSpeed);
            } else if (this.keyboard.getDown()) {
                this.translate(0,0,moveSpeed);
            } else if (this.keyboard.getScramble() && !autoModeLock) {
                autoModeLock = true;
                scramble = true;
                numMoves = 100*animationTime;
            } else if (this.keyboard.getSolve() && !autoModeLock) {
                autoModeLock = true;
                solve = true;
            }
        }

        if (autoModeLock && scramble) {
            this.randomMove();
            numMoves--;
            if (numMoves == 0){
                autoModeLock = false;
                scramble = false;
                System.out.println("Left = " + moveCounter[0]/animationTime);
                System.out.println("Right = " + moveCounter[1]/animationTime);
                System.out.println("Front = " + moveCounter[2]/animationTime);
                System.out.println("Back = " + moveCounter[3]/animationTime);
                System.out.println("Top = " + moveCounter[4]/animationTime);
                System.out.println("Bottom = " + moveCounter[5]/animationTime);

                System.out.println("Move = " + dirCounter[1]/animationTime);
                System.out.println("Move' = " + dirCounter[0]/animationTime);
                System.out.println("---------------------------------------");
            }
        } else if (autoModeLock && solve) {
            this.solver.solveCube();
            autoModeLock = false;
            solve = false;
        } else if (locked) {
            rotateSpeed = 90.0/90.0;
            rotateCube(currentMove, direction);
            animationTimeLeft--;
            rotateSpeed = 90.0/animationTime;
            if (animationTimeLeft == 0) {
                locked = false;
                animationTimeLeft = animationTime;
                counter = 0;
            }
        } else if (this.keyboard.getAlt()) {
            direction = true;
            if (this.keyboard.getReverse()) {
                direction = false;
            }

            if (this.keyboard.getLeft()) {
                animationTimeLeft = 90;
                currentMove = 1;
                locked = true;
            } else if (this.keyboard.getRight()) {
                animationTimeLeft = 90;
                currentMove = 2;
                locked = true;
            } else if (this.keyboard.getUp()) {
                animationTimeLeft = 90;
                currentMove = 3;
                locked = true;
            } else if (this.keyboard.getDown()) {
                animationTimeLeft = 90;
                currentMove = 4;
                locked = true;
            } else if (this.keyboard.getForward()) {
                animationTimeLeft = 90;
                currentMove = 5;
                locked = true;
            } else if (this.keyboard.getBackward()) {
                animationTimeLeft = 90;
                currentMove = 6;
                locked = true;
            }
        }

        this.mouse.resetScroll();
        this.keyboard.update();

        initialX = x;
        initialY = y;

    }

    private int[] moveCounter = {0, 0, 0, 0, 0, 0};
    private int[] dirCounter = {0, 0};

    private void rotateCube(int move, boolean direction) {
        if (direction) {
            dirCounter[1]++;
        } else {
            dirCounter[0]++;
        }
        switch (move) {
            case 1:
                this.rubiksCube.left(this.axis, direction, this.rotateSpeed);
                moveCounter[0]++;
                break;
            case 2:
                this.rubiksCube.right(this.axis, direction, this.rotateSpeed);
                moveCounter[1]++;
                break;
            case 3:
                this.rubiksCube.forward(this.axis, direction, this.rotateSpeed);
                moveCounter[2]++;
                break;
            case 4:
                this.rubiksCube.backward(this.axis, direction, this.rotateSpeed);
                moveCounter[3]++;
                break;
            case 5:
                this.rubiksCube.up(this.axis, direction, this.rotateSpeed);
                moveCounter[4]++;
                break;
            case 6:
                this.rubiksCube.down(this.axis, direction, this.rotateSpeed);
                moveCounter[5]++;
                break;
            default:
                System.out.printf("No Move: %d %b\n", move, direction);
                return;
        }
        sortEntities();
        return;
    }

    private void sleep() {
        try {
            Thread.sleep(200);
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
        if (this.numMoves % animationTime == 0) {
            currentRandMove = random.nextInt(1, 7);
            currentRandDir = random.nextInt(0, 2) == 1 ? true:false;
        }
        this.rotateCube(currentRandMove, currentRandDir);
    }
}
