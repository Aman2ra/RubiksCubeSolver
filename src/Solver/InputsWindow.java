package Solver;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class InputsWindow {
    // Animation Speed Slider
    static final int speedMin = 1;
    static final int speedMax = 90;
    static final int speedInit = 20;
    JSlider animationSpeed = new JSlider(JSlider.HORIZONTAL, speedMin, speedMax, speedInit);


    public InputsWindow(){
        animationSpeed.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

            }
        });
        animationSpeed.setMajorTickSpacing(10);
        animationSpeed.setMajorTickSpacing(1);
        animationSpeed.setPaintTicks(true);
        animationSpeed.setPaintLabels(true);
    }

}
