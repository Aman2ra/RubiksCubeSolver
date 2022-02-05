package Solver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;

public class ControlsWindow {
    private JPanel window;
    private JTextPane cubeControlsTextPane;
    private JTextPane cameraControlsTextPane;
    private JTextPane specialTextPane;
    private JButton cubeTopButton;
    private JButton cubeBottomButton;
    private JButton cubeLeftButton;
    private JButton cubeFrontButton;
    private JButton cubeBackButton;
    private JButton cubeRightButton;
    private JButton camForwardButton;
    private JButton camLeftButton;
    private JButton camUpButton;
    private JButton camBackwardButton;
    private JButton camDownButton;
    private JButton camRightButton;
    private JButton scrambleButton;
    private JButton solveButton;
    private JButton showKeyboardControlsButton;
    private JSplitPane cameraSpecialSplit;
    private JSplitPane cubeCameraSpecialSplit;
    private JPanel cubeControlsButton;
    private JPanel cubePanel;
    private JPanel cameraButtons;
    private JPanel cameraPanel;
    private JPanel specialPanel;
    public JFrame frame;

    private EntityManager entityManager;

    public ControlsWindow(EntityManager entityManager){
        this.frame = new JFrame();
        this.frame.setTitle("Controls");
        this.frame.add(this.window);
        this.frame.pack();
        //this.frame.setLocationRelativeTo(null);
        this.frame.setResizable(false);
        this.frame.setVisible(true);
        this.frame.toFront();

        this.entityManager = entityManager;

        this.createListeners();
    }

    private void createListeners(){
        cubeLeftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotateCube(1);
            }
        });
        cubeRightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotateCube(2);
            }
        });
        cubeFrontButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotateCube(3);
            }
        });
        cubeBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotateCube(4);
            }
        });
        cubeTopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotateCube(5);
            }
        });
        cubeBottomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotateCube(6);
            }
        });


        camForwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveCamera(0, 0, -1);
            }
        });
        camLeftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveCamera(-1, 0, 0);
            }
        });
        camUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveCamera(0, 1, 0);
            }
        });
        camBackwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveCamera(0, 0, 1);
            }
        });
        camDownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveCamera(0, -1, 0);
            }
        });
        camRightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveCamera(1, 0, 0);
            }
        });


        scrambleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startScramble();
            }
        });
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSolve();
            }
        });
        showKeyboardControlsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openControlsFile();
            }
        });
    }

    private void rotateCube(int move) {
        this.entityManager.rotateCube2(move);
    }

    private void moveCamera(int x, int y, int z) {
        this.entityManager.moveCamera(x, y, z);
    }

    private void startScramble() {
        this.entityManager.startScramble();
    }

    private void startSolve() {
        this.entityManager.startSolve();
    }

    private void openControlsFile() {
        String text = "";
        try {
            File file = new File(System.getProperty("user.dir") + "\\src\\Controls.txt");
            FileInputStream fis = new FileInputStream(file);
            int r = 0;
            while((r=fis.read())!=-1){
                text += ((char)r);
            }
        } catch (Exception e) {
            text = "Could not open file";
        }
        JOptionPane.showMessageDialog(new JFrame("Controls"), text);
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
