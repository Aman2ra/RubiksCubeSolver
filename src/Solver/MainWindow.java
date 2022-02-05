package Solver;

public class MainWindow {
    private ControlsWindow controlsWindow;
    private SettingsWindow settingsWindow;
    private CanvasWindow canvasWindow;
    private EntityManager entityManager;

    public MainWindow() {
        this.entityManager = new EntityManager();
        this.canvasWindow = new CanvasWindow(this.entityManager);
        this.controlsWindow = new ControlsWindow(this.entityManager);
        this.settingsWindow = new SettingsWindow(this.entityManager);

        int canvasX = this.canvasWindow.getLocationX();
        int canvasY = this.canvasWindow.getLocationY();
        int canvasWidth = this.canvasWindow.getWidth();
        int canvasHeight = this.canvasWindow.getHeight();

        int controlsWidth = this.controlsWindow.getWidth();
        int controlsHeight = this.controlsWindow.getHeight();
        int settingsWidth = this.settingsWindow.getWidth();
        int settingsHeight = this.settingsWindow.getHeight();

        this.controlsWindow.setLocation(canvasX-controlsWidth+10,canvasY);
        this.settingsWindow.setLocation(canvasX+canvasWidth-10,canvasY);
    }

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
    }
}
