package memo.view;

import java.beans.PropertyChangeEvent;
import javafx.stage.Stage;
import memo.controller.AbstractController;
import memo.model.User;


public interface ViewInterface {
    /**
     * <p> Initializes control's handlers with current controller </p>
     */
    public void manualInitialize();

    public void setController(AbstractController controller);
    public void setPrimaryStage(Stage primaryStage);

    /**
     * <p> Controller notifies this method about model changing
     * @param evt .getPropertyName() is name of variable which has been changed
     */
    public void modelPropertyChange(final PropertyChangeEvent evt);

    //public boolean isHideableToTray();
    public void hideStage();
    public void showStage();

    public void showTrayIcon();
    public void hideTrayIcon();

    public boolean isShowing();

    public void addUser(User user);

    /**
     * Bring the Window to foreground
     */
    public void showToFront();
}
