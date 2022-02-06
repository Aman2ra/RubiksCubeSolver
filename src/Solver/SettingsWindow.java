package Solver;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsWindow {
    private JFrame frame;
    private JPanel settingsPanel;
    private JTextPane tpAnimationSpeed;
    private JTextArea settingsTextArea;
    private JButton changeColourBack;
    private JTextPane backTextPane;
    private JButton changeColourFront;
    private JTextPane frontTextPane;
    private JButton changeColourRight;
    private JTextPane rightTextPane;
    private JButton changeColourLeft;
    private JTextPane leftTextPane;
    private JButton changeColourBottom;
    private JTextPane BottomTextPane;
    private JButton changeColourTop;
    private JTextPane topTextPane;
    private JTextPane cubeColorsTextPane;
    private JPanel cubeColorsPanel;
    private JTextPane numberOfRandomMovesTextPane;
    private JSplitPane randomMoveSplit;
    private JSlider sliderAnimationSpeed;
    private JPanel window;
    private JPanel topColorPanel;
    private JPanel bottomColorPanel;
    private JPanel leftColorPanel;
    private JPanel rightColorPanel;
    private JPanel frontColorPanel;
    private JPanel backColorPanel;
    private JSplitPane movementSpeedSplit;
    private JTextPane movementSpeedTextPane;
    private JTextField moveSpeedTextField;
    private JTextField randomMovesTextField;

    private EntityManager entityManager;

    int prevRandomMoves = 50;
    int prevMoveSpeed = 10;

    public SettingsWindow(EntityManager entityManager){
        this.frame = new JFrame();
        this.frame.setTitle("Settings");
        this.frame.add(this.window);
        this.frame.pack();
        //this.frame.setLocationRelativeTo(null);
        this.frame.setResizable(false);
        this.frame.setVisible(true);
        this.frame.toFront();

        this.entityManager = entityManager;


        createListeners();
    }

    private void createListeners() {
        changeColourTop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color baseColor = Color.YELLOW;
                Color prevColor = getTopColor();
                Color newColor = JColorChooser.showDialog(null, "Choose Top Color", prevColor);
                if (newColor != null) {
                    setTopColor(newColor, baseColor);
                }
            }
        });
        changeColourBottom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color baseColor = Color.WHITE;
                Color prevColor = getBottomColor();
                Color newColor = JColorChooser.showDialog(null, "Choose Bottom Color", prevColor);
                if (newColor != null) {
                    setBottomColor(newColor, baseColor);
                }
            }
        });
        changeColourLeft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color baseColor = Color.BLUE;
                Color prevColor = getLeftColor();
                Color newColor = JColorChooser.showDialog(null, "Choose Left Color", prevColor);
                if (newColor != null) {
                    setLeftColor(newColor, baseColor);
                }
            }
        });
        changeColourRight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color baseColor = Color.GREEN;
                Color prevColor = getRightColor();
                Color newColor = JColorChooser.showDialog(null, "Choose Right Color", prevColor);
                if (newColor != null) {
                    setRightColor(newColor, baseColor);
                }
            }
        });
        changeColourFront.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color baseColor = Color.RED;
                Color prevColor = getFrontColor();
                Color newColor = JColorChooser.showDialog(null, "Choose Front Color", prevColor);
                if (newColor != null) {
                    setFrontColor(newColor, baseColor);
                }
            }
        });
        changeColourBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color baseColor = new Color(255,120,30);
                Color prevColor = getBackColor();
                Color newColor = JColorChooser.showDialog(null, "Choose Back Color", prevColor);
                if (newColor != null) {
                    setBackColor(newColor, baseColor);
                }
            }
        });


        sliderAnimationSpeed.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int animationSpeed = sliderAnimationSpeed.getValue();
                if (animationSpeed == 0) {
                    animationSpeed = 1;
                } else if (animationSpeed == 90) {
                    animationSpeed = 89;
                }
                sliderAnimationSpeed.setValue(animationSpeed);
                setAnimationSpeed(90 - animationSpeed);
            }
        });
        moveSpeedTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int moveSpeed = Integer.parseInt(moveSpeedTextField.getText());
                    prevMoveSpeed = moveSpeed;
                    setMoveSpeed(moveSpeed);
                } catch (NumberFormatException exc) {
                    JOptionPane.showMessageDialog(new JFrame("Invalid Operation"), "Invalid Input. Please enter an integer value");
                    moveSpeedTextField.setText(Integer.toString(prevMoveSpeed));
                }
            }
        });
        randomMovesTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int randomMoves = Integer.parseInt(randomMovesTextField.getText());
                    prevRandomMoves = randomMoves;
                    setRandomMoves(randomMoves);
                } catch (NumberFormatException exc) {
                    JOptionPane.showMessageDialog(new JFrame("Invalid Operation"), "Invalid Input. Please enter an integer value");
                    randomMovesTextField.setText(Integer.toString(prevRandomMoves));
                }
            }
        });

    }

    private void replaceColor(Color newColor, Color prevColor) {
        this.entityManager.replaceColor(newColor, prevColor);
    }

    private void setTopColor(Color newColor, Color prevColor) {
        this.topColorPanel.setBackground(newColor);
        replaceColor(newColor, prevColor);
    }

    private Color getTopColor() {
        return this.topColorPanel.getBackground();
    }

    private void setBottomColor(Color newColor, Color prevColor) {
        this.bottomColorPanel.setBackground(newColor);
        replaceColor(newColor, prevColor);
    }

    private Color getBottomColor() {
        return this.bottomColorPanel.getBackground();
    }

    private void setLeftColor(Color newColor, Color prevColor) {
        this.leftColorPanel.setBackground(newColor);
        replaceColor(newColor, prevColor);
    }

    private Color getLeftColor() {
        return this.leftColorPanel.getBackground();
    }

    private void setRightColor(Color newColor, Color prevColor) {
        this.rightColorPanel.setBackground(newColor);
        replaceColor(newColor, prevColor);
    }

    private Color getRightColor() {
        return this.rightColorPanel.getBackground();
    }

    private void setFrontColor(Color newColor, Color prevColor) {
        this.frontColorPanel.setBackground(newColor);
        replaceColor(newColor, prevColor);
    }

    private Color getFrontColor() {
        return this.frontColorPanel.getBackground();
    }

    private void setBackColor(Color newColor, Color prevColor) {
        this.backColorPanel.setBackground(newColor);
        replaceColor(newColor, prevColor);
    }

    private Color getBackColor() {
        return this.backColorPanel.getBackground();
    }

    private void setAnimationSpeed(int animationSpeed) {
        this.entityManager.setAnimationSpeed(animationSpeed);
    }

    private void setRandomMoves(int randomMoves) {
        this.entityManager.setRandomMoves(randomMoves);
    }

    private void setMoveSpeed(int moveSpeed) {
        this.entityManager.setMoveSpeed(moveSpeed);
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
