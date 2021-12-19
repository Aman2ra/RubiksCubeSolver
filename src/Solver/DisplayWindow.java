package Solver;


import Solver.BasicBuilders.MyPoint;
import Solver.Temp.EntityManager;
import Solver.UserInputs.UserInput;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class DisplayWindow extends Canvas implements Runnable {
    private Thread thread;
    private JFrame frame;
    private static String title = "Rubiks Cube Solver";
    private static boolean running = false;
    public static final int WIDTH = 720;
    public static final int HEIGHT = 1080;
    private Color skybox_colour = new Color(110,60,110);

    private EntityManager entityManager;

    private UserInput userInput;

    public DisplayWindow(){
        this.frame = new JFrame();
        Dimension size = new Dimension(WIDTH, HEIGHT);
        this.setPreferredSize(size);

        this.entityManager = new EntityManager();

        this.userInput = new UserInput();
        this.addMouseListener(this.userInput.mouse);
        this.addMouseMotionListener(this.userInput.mouse);
        this.addMouseWheelListener(this.userInput.mouse);
        this.addKeyListener(this.userInput.keyboard);
    }

    public static void main(String[] args){
        DisplayWindow window = new DisplayWindow();
        window.frame.setTitle(title);
        window.frame.add(window);
        window.frame.pack();
        window.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.frame.setLocationRelativeTo(null);
        window.frame.setResizable(false);
        window.frame.setVisible(true);
        window.start();
    }

    public synchronized void start(){
        running = true;
        this.thread = new Thread(this, "Renderer.Window");
        this.thread.start();
    }

    public synchronized void stop(){
        running = false;
        try {
            this.thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        int desiredFramerate = 60;
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ns = 1000000000.0 / desiredFramerate;
        double delta = 0;
        int frames = 0;

        this.entityManager.init(this.userInput);

        while (running){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1){
                update();
                render();
                delta--;
                frames++;
            }
            if (System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                MyPoint c = entityManager.getCameraPos();
                MyPoint a = entityManager.getAxisPos();
                MyPoint x = entityManager.getXAxis();
                MyPoint y = entityManager.getYAxis();
                MyPoint z = entityManager.getZAxis();
                this.frame.setTitle(title + " | " + frames + " fps");
                frames = 0;
            }
        }

        stop();
    }

    private void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null){
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        g.setColor(skybox_colour);
        g.fillRect(0,0,WIDTH,HEIGHT);

        this.entityManager.render(g);

        g.dispose();
        bs.show();
    }

    private void update(){
        this.entityManager.update();
    }

}