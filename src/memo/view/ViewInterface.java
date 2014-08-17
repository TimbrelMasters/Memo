package memo.view;

import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import memo.controller.AbstractController;
import memo.model.Card;
import memo.model.User;


public interface ViewInterface {
    /**
     * <p> Initializes control's handlers with current controller </p>
     */
    public void manualInitialize();

    public void setController(AbstractController controller);
    public void setPrimaryStage(Stage primaryStage);
    public void setRootLayout(BorderPane rootLayout);

    //public boolean isHideableToTray();
    public void hideStage();
    public void showStage();

    public void showTrayIcon();
    public void hideTrayIcon();

    public boolean isShowing();

    public void addUser(User user);
    public void showUserCards(User user);
    public void addCard(int i, int j, Card card);

    /**
     * Bring the Window to foreground
     */
    public void showToFront();
}
