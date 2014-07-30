package memo;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import memo.controller.AbstractController;
import memo.controller.Controller;
import memo.model.AbstractModel;
import memo.model.Model;
import memo.view.ViewInterface;

/**
 *
 * @author TimbrelMasters
 */
public class Memo extends Application {
    
    private Stage primaryStage;
    private Pane rootLayout;
    
    private AbstractModel model;
    private AbstractController controller;
    private ViewInterface view;
    
    @Override
    public void start(Stage primaryStage) {
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
        FXMLLoader loader = new FXMLLoader(Memo.this.getClass().getResource("view/ViewDesign.fxml"));
        this.rootLayout = loader.load();
        
        this.model = new Model();
        this.controller = new Controller();
        this.view = loader.getController();
        
        view.setController(controller);
        controller.addModel(model);
        controller.addView(view);        
        
        view.initialize();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
