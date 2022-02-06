package Solver;


import Solver.BasicBuilders.MyPoint;
import Solver.UserInputs.UserInput;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class CanvasWindow extends Canvas implements Runnable {
    private Thread thread;
    public JFrame frame;
    public JFrame outputFrame;
    public JScrollPane outputPane;
    public JTextArea outputTextArea;
    private static String title = "Rubiks Cube Solver";
    private static boolean running = false;
    public static final int WIDTH = 900;
    public static final int HEIGHT = 506;
    private Color skybox_colour = Color.BLACK;

    private EntityManager entityManager;
    private UserInput userInput;

    public CanvasWindow(){
        this.frame = new JFrame();
        Dimension size = new Dimension(WIDTH, HEIGHT);
        this.setPreferredSize(size);

        this.entityManager = new EntityManager();
        this.userInput = new UserInput();

        this.addMouseListener(this.userInput.mouse);
        this.addMouseMotionListener(this.userInput.mouse);
        this.addMouseWheelListener(this.userInput.mouse);
        this.addKeyListener(this.userInput.keyboard);

        this.startCanvas();
    }

    public CanvasWindow(EntityManager entityManager){
        this.frame = new JFrame();
        Dimension size = new Dimension(WIDTH, HEIGHT);
        this.setPreferredSize(size);

        this.entityManager = entityManager;
        this.userInput = new UserInput();

        this.addMouseListener(this.userInput.mouse);
        this.addMouseMotionListener(this.userInput.mouse);
        this.addMouseWheelListener(this.userInput.mouse);
        this.addKeyListener(this.userInput.keyboard);

        this.startCanvas();
    }

    public void startCanvas() {
        this.frame.setTitle(title);
        this.frame.add(this);
        this.frame.pack();
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLocationRelativeTo(null);
        this.frame.setResizable(false);
        this.frame.setVisible(true);
        this.setLocation(this.getLocationX(), 0);
        this.startOutputPane();
        this.start();
    }

    private void startOutputPane() {
        this.outputTextArea = new JTextArea();
        this.outputTextArea.setBackground(new Color(41,41,45));
        this.outputTextArea.setForeground(new Color(241,241,255));
        this.outputTextArea.setFont(new Font("Arial", Font.PLAIN, 16));
        this.outputTextArea.setEditable(false);
        this.outputTextArea.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                goToBottom();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                goToBottom();
            }

            @Override
            public void changedUpdate(DocumentEvent arg0) {
                goToBottom();
            }
        });

        this.outputPane = new JScrollPane(outputTextArea);
        this.outputPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.outputPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.outputPane.setVisible(true);
        this.outputPane.setPreferredSize(new Dimension(WIDTH - 10,HEIGHT/2));


        this.outputFrame = new JFrame("Output");
        this.outputFrame.setVisible(true);
        this.outputFrame.setLocation(this.frame.getX(), this.frame.getY() + this.frame.getHeight());
        this.outputFrame.setResizable(false);
        this.outputFrame.getContentPane().add(this.outputPane);
        this.outputFrame.getContentPane().setBackground(new Color(41,41,45));
        this.outputFrame.getContentPane().setLayout(new FlowLayout());
        this.outputFrame.pack();


        this.outputTextArea.append("                    Output Console\n__________________________________\n");
        this.entityManager.setOutputPane(this.outputTextArea);
    }

    private void goToBottom() {
        this.outputTextArea.setCaretPosition(this.outputTextArea.getDocument().getLength());
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

    public void toFront() {
        this.frame.toFront();
    }

    public int getLocationX() {
        return this.frame.getX();
    }

    public int getLocationY() {
        return this.frame.getY();
    }

    public int getWidth() {
        return this.frame.getWidth();
    }

    public int getHeight() {
        return this.frame.getHeight();
    }

    public void setLocation(int x, int y){
        this.frame.setLocation(x, y);
    }

}