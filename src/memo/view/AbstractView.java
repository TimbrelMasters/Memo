package memo.view;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import memo.controller.AbstractController;

/**
 * m=Must be the root element for any hierarchy of view
 * @author Pisarik
 */
public abstract class AbstractView {

    protected AbstractController controller;
    protected Stage primaryStage;

    /**
     * Must be set in manualInitialize()
     */
    @FXML
    private Pane rootPane;

    /**
     * <p> Initializes control's handlers with current controller. In some a point of view replaces constructor </p>
     * rootPane must be set here.
     */
    public abstract void manualInitialize();

    public void setController(AbstractController controller){
        this.controller = controller;
    }

    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    protected void setRootPane(Pane rootPane){
        this.rootPane = rootPane;
    }

    public Pane getRootPane(){
        return rootPane;
    }

}
