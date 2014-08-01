package memo.view;

import java.awt.PopupMenu;
import java.beans.PropertyChangeEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.stage.Stage;
import memo.controller.AbstractController;
import memo.utils.TrayUtility;

/**
 *
 * @author Pisarik
 */
public class ViewCode implements ViewInterface{
    
    private AbstractController controller;
    
    private Stage primaryStage;
    private TrayUtility trayUtility;
    private java.awt.MenuItem trayCloseItem;
    private java.awt.MenuItem trayOpenItem;
    

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
        trayUtility = new TrayUtility();
        //trayUtility.setIcon(path);
        trayUtility.setToolTip("tooltip"); //don't know what tooltip to show
        PopupMenu menu = new PopupMenu();
        trayOpenItem = new java.awt.MenuItem("Open");
        trayCloseItem = new java.awt.MenuItem("Close");
        menu.add(trayOpenItem);
        menu.add(trayCloseItem);
        trayUtility.setMenu(menu);
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
        
    private void handleTrayExitClick() {
        trayCloseItem.addActionListener((java.awt.event.ActionEvent e) -> {
            trayUtility.hideIcon();
            controller.exit();
        });
    }

    private void handleTrayOpenClick() {
        trayOpenItem.addActionListener((java.awt.event.ActionEvent e) -> {
            trayUtility.hideIcon();
            controller.showStage();
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
    
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    
  /*
   ***
   *** Some action methods
   ***
    */
    
    @Override
    public void showStage() {
        primaryStage.show();
    }
    
}
