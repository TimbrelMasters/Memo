package memo.view;

import java.beans.PropertyChangeEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
    private VBox vBoxForAccordion;

    CustomAccordion mainAccordion;


    private AbstractController controller;
    private Stage primaryStage;

    ObservableList<User> users;

    public MainViewCode(){
    }

    @FXML
    public void OnUserAdd(ActionEvent event){
        controller.addUser(new User("Pisarik"));
    }

    @Override
    public void manualInitialize() {
        users = FXCollections.observableArrayList(controller.getUserList());
        userComboBox.setItems(users);

        mainAccordion = new CustomAccordion(new User("Lol"));
        vBoxForAccordion.getChildren().add(mainAccordion.getRoot());

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

    @Override
    public void showToFront() {
        //???
    }

    @Override
    public void addUser(User user) {
        users.add(user);
    }

}
