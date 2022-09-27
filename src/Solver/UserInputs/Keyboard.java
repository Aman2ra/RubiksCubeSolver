package Solver.UserInputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// Keyboard class for key press events
public class Keyboard implements KeyListener {

    private boolean[] keys = new boolean[66568];
    private boolean left, right, up, down, forward, backward, reverse, alternate, scramble, solve;

    public void update(){
        this.left = this.keys[KeyEvent.VK_LEFT] || this.keys[KeyEvent.VK_A];
        this.right = this.keys[KeyEvent.VK_RIGHT] || this.keys[KeyEvent.VK_D];
        this.forward = this.keys[KeyEvent.VK_UP] || this.keys[KeyEvent.VK_W];
        this.backward = this.keys[KeyEvent.VK_DOWN] || this.keys[KeyEvent.VK_S];
        this.up = this.keys[KeyEvent.VK_SPACE];
        this.down = this.keys[KeyEvent.VK_C];
        this.reverse = this.keys[KeyEvent.VK_SHIFT];
        this.alternate = this.keys[KeyEvent.VK_CONTROL];
        this.scramble = this.keys[KeyEvent.VK_1];
        this.solve = this.keys[KeyEvent.VK_2];
    }

    public boolean getLeft() {
        return this.left;
    }

    public boolean getRight() {
        return this.right;
    }

    public boolean getUp() {
        return this.up;
    }

    public boolean getDown() {
        return this.down;
    }

    public boolean getForward() {
        return this.forward;
    }

    public boolean getBackward() {
        return this.backward;
    }

    public boolean getReverse() {
        return this.reverse;
    }

    public boolean getAlt() {
        return this.alternate;
    }

    public boolean getScramble() {
        return this.scramble;
    }

    public boolean getSolve() {
        return this.solve;
    }

    @Override
    public void keyTyped(KeyEvent e) {    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }
}
