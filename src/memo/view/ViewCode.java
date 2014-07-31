package memo.view;

import java.beans.PropertyChangeEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import memo.controller.AbstractController;

/**
 *
 * @author Pisarik
 */
public class ViewCode implements ViewInterface{
    
    private AbstractController controller;
    
    @FXML
    private RadioMenuItem addToStratUpItem;
    
    @FXML
    private MenuItem exitItem;
    
    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        //it's empty bacause model empty
    }
    
 /*
   ***
   *** Initialize methods
   ***
    */ 
    
    @Override
    public void initialize() {        
        initSystemTray();
        handleAddToStartUpClick();
        handleExitClick();
    }
    
    /**
     * set icon, popupmenu, tooltip for tray
     */
    private void initSystemTray() {
        
    }

    private void handleAddToStartUpClick(){
        addToStratUpItem.setOnAction((ActionEvent e) -> {
            if (addToStratUpItem.isSelected()){
                controller.addToStartUp();
            }
            else {
                controller.removeFromStartUp();
            }
        });
    }

    private void handleExitClick() {
        exitItem.setOnAction((ActionEvent e) -> {
            controller.exit();
        });
    }
    
 /*
   ***
   *** Constructors and Destructors
   ***
    */ 
    
    public ViewCode(){
        this.controller = null;
    }
    
    public ViewCode(AbstractController controller){
        this.controller = controller;
    }

 /*
   ***
   *** Setters and Getters 
   ***
    */   
    
    @Override
    public void setController(AbstractController controller) {
        this.controller = controller;
    }
    
}
