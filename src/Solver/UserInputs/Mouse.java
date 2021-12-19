package Solver.UserInputs;

import java.awt.event.*;

public class Mouse implements MouseListener, MouseMotionListener, MouseWheelListener {

    private int mouseX = -1;
    private int mouseY = -1;
    private int mouseB = -1;
    private int scroll = 0;

    public int getX(){
        return this.mouseX;
    }

    public int getY(){
        return this.mouseY;
    }

    public boolean isScrollingUp(){
        return this.scroll == -1;
    }
    public boolean isScrollingDown(){
        return this.scroll == 1;
    }
    public void resetScroll(){
        this.scroll = 0;
    }

    public ClickType getButton(){
        switch (this.mouseB){
            case 1:
                return ClickType.LeftClick;
            case 2:
                return ClickType.MiddleClick;
            case 3:
                return ClickType.RightClick;
            case 4:
                return ClickType.BackThumb;
            case 5:
                return ClickType.ForwardThumb;
            default:
                return ClickType.Unknown;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.mouseB = e.getButton();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.mouseB = -1;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        this.mouseX = e.getX();
        this.mouseY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.mouseX = e.getX();
        this.mouseY = e.getY();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        this.scroll = e.getWheelRotation();
    }
}
