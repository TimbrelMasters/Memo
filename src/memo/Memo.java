package memo;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import memo.controller.AbstractController;
import memo.controller.Controller;
import memo.model.AbstractModel;
import memo.model.Model;
import memo.utils.singleinstance.SingleInstanceUtility;
import memo.view.AbstractView;
import memo.view.RootViewInterface;

/**
 *
 * @author TimbrelMasters
 */
public class Memo extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private Pane mainLayout;

    private AbstractModel model;
    private AbstractController controller;
    private AbstractView view;
    private RootViewInterface viewInterface;

    @Override
    public void start(Stage primaryStage) {
        Platform.setImplicitExit(false);
        this.primaryStage = primaryStage;

        try {
            initMVC();
        } catch (IOException ex) {
            Logger.getLogger(Memo.class.getName()).log(Level.SEVERE, null, ex);
        }

        Scene scene = new Scene(rootLayout);
        primaryStage.setTitle("Memo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initMVC() throws IOException{
        FXMLLoader rootLoader = new FXMLLoader(Memo.this.getClass().getResource("view/RootLayoutDesign.fxml"));
        this.rootLayout = (BorderPane)rootLoader.load();

        this.model = new Model();
        this.controller = new Controller();
        this.view = rootLoader.getController();
        this.viewInterface = rootLoader.getController();

        view.setController(controller);
        view.setPrimaryStage(primaryStage);
        controller.addModel(model);
        controller.addView(viewInterface);

        view.manualInitialize();
        SingleInstanceUtility.setNewInstanceListener(controller);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            SingleInstanceUtility.setPort(43456);
            SingleInstanceUtility.setSharedKey("&&MemoInstance&&\n");

            if (!SingleInstanceUtility.registerInstance()){
                SingleInstanceUtility.closeInstance();
                Platform.exit();
            }
            else{
                launch(args);
            }
        }
        catch (RuntimeException e){
            Logger.getLogger(Memo.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            SingleInstanceUtility.closeInstance();
        }
    }

}
