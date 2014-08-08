package memo.view;

import java.beans.PropertyChangeEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import memo.controller.AbstractController;
import memo.model.User;

/**
 *
 * @author Pisarik
 */
public class MainViewCode implements ViewInterface{

    @FXML
    private ComboBox<User> userComboBox;

    @FXML
    private VBox menuVBox;

    private AbstractController controller;
    private Stage primaryStage;

    public MainViewCode(){

    }

    @Override
    public void manualInitialize() {
        ObservableList<User> users = FXCollections.observableArrayList();
        userComboBox.setItems(controller.getUserList());

    }

    @Override
    public void setController(AbstractController controller) {
        this.controller = controller;
    }

    @Override
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        //it's empty because Model is empty
    }

    @Override
    public void hideStage() {
        //???
    }

    @Override
    public void showStage() {
        //???
    }

    @Override
    public void showTrayIcon() {
        //???
    }

    @Override
    public void hideTrayIcon() {
        //???
    }

    @Override
    public boolean isShowing() {
        return primaryStage.isShowing();
    }

}
