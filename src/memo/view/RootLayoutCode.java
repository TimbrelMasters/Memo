package memo.view;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import memo.controller.AbstractController;
import memo.model.User;
import memo.utils.TrayUtility;

/**
 *
 * @author Pisarik
 */
public class RootLayoutCode implements ViewInterface{

    private final URL TRAY_ICON_URL;

    private AbstractController controller;

    private Stage primaryStage;
    private TrayUtility trayUtility;
    private java.awt.PopupMenu trayMenu;
    private java.awt.MenuItem trayExitItem;
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
    public void manualInitialize() {
        initStartUpItem();
        initSystemTray();
        initIcons();

        /*-----MAIN MENU-------*/
        handleAddToStartUpClick();
        handleExitClick();
        handleCloseRequest();

        /*-----TRAY MENU-------*/
        handleTrayOpenClick();
        handleTrayExitClick();
        handleTrayDoubleClick();
    }

    private void initTrayMenu(){
        trayMenu = new java.awt.PopupMenu();

        trayOpenItem = new java.awt.MenuItem("Open");
        trayExitItem = new java.awt.MenuItem("Exit");

        trayMenu.add(trayOpenItem);
        trayMenu.add(new java.awt.MenuItem("-"));
        trayMenu.add(trayExitItem);
    }

    private void initIcons(){
        try {
            primaryStage.getIcons().add(
                    new Image(TRAY_ICON_URL.openStream()));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * set icon, popupmenu, tooltip for tray
     */
    private void initSystemTray() {
        trayUtility = new TrayUtility();

        if (TrayUtility.isTraySupported()){
            initTrayMenu();

            trayUtility.setIcon(TRAY_ICON_URL);
            trayUtility.setToolTip("Memo");
            trayUtility.setMenu(trayMenu);
        }
    }

    private void initStartUpItem(){
        addToStratUpItem.setSelected(controller.isAddedToStartUp());
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
        trayExitItem.addActionListener((java.awt.event.ActionEvent e) -> {
            controller.exit();
        });
    }

    private void handleTrayOpenClick() {
        trayOpenItem.addActionListener((java.awt.event.ActionEvent e) -> {
            controller.showStageFromTray();
        });
    }

    private void handleTrayDoubleClick(){
        trayUtility.setOnDoubleClick((java.awt.event.ActionEvent e) -> {
            controller.showStageFromTray();
        });
    }

    private void handleCloseRequest(){
        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            if (TrayUtility.isTraySupported()){
                controller.hideStageToTray();
            }
            else{
                throw new UnsupportedOperationException("Must be dialog about exit");
            }
            event.consume();
        });
    }

 /*
   ***
   *** Constructors and Destructors
   ***
    */

    public RootLayoutCode(){
        this(null);
    }

    public RootLayoutCode(AbstractController controller){
        this.controller = controller;
        TRAY_ICON_URL = this.getClass().getResource("resources/trayIcon.png");
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

    @Override
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
        Platform.runLater(() -> {
            primaryStage.show();
        });
    }

    @Override
    public void hideStage() {
        primaryStage.hide();
    }

    @Override
    public void showTrayIcon() {
        trayUtility.showIcon();
    }

    @Override
    public void hideTrayIcon() {
        trayUtility.hideIcon();
    }

    @Override
    public boolean isShowing() {
        return primaryStage.isShowing();
    }

    @Override
    public void showToFront() {
        Platform.runLater(() ->{
           if (primaryStage.isIconified())
               primaryStage.setIconified(false);
           else{
            primaryStage.toFront();
           }
        });
    }

    @Override
    public void addUser(User user) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

}
