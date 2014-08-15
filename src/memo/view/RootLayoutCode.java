package memo.view;

import java.io.IOException;
import java.net.URL;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
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

    private MainViewCode mainView;

    private BorderPane rootLayout;
    private Pane mainViewPane;

    private Stage primaryStage;
    private TrayUtility trayUtility;
    private java.awt.PopupMenu trayMenu;
    private java.awt.MenuItem trayExitItem;
    private java.awt.MenuItem trayOpenItem;

    @FXML
    private RadioMenuItem addToStratUpItem;

    @FXML
    private MenuItem exitItem;

    @FXML
    private ComboBox<User> userComboBox;

    @FXML
    private Accordion themeAccordion;

    private CustomAccordion customAccordion;

    private ObservableList<User> users;

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
        initInnerViews();
        initStartView();

        /*-----MAIN MENU-------*/
        handleAddToStartUpClick();
        handleExitClick();
        handleCloseRequest();

        /*-----TRAY MENU-------*/
        handleTrayOpenClick();
        handleTrayExitClick();
        handleTrayDoubleClick();

        /*------CONTROLS-------*/
        initUserComboBox();

        customAccordion = new CustomAccordion(users.get(0), themeAccordion);
        
    }

    private void initInnerViews(){
        try{
            FXMLLoader mainViewLoader = new FXMLLoader(this.getClass().getResource("MainViewDesign.fxml"));
            mainViewPane = mainViewLoader.load();
            mainView = mainViewLoader.getController();
            mainView.manualInitialize();
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private void initStartView(){ //may be setInnerView
        rootLayout.setRight(mainViewPane);
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
    
    private void initUserComboBox() {
        users = FXCollections.observableArrayList(controller.getUserList());
        userComboBox.setItems(users);
        userComboBox.getSelectionModel().selectFirst();
        userComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
            
            @Override
            public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
                controller.setCurrentUser(newValue);
            } 
            
        });
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
   *** Controls handle
   ***
    */

    @FXML
    public void OnUserAdd(ActionEvent event){
        controller.addUser(new User("Pisarik"));
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

    @Override
    public void setRootLayout(BorderPane rootLayout) {
        this.rootLayout = rootLayout;
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
        users.add(user);
    }
    
    @Override
    public void showUserCards(User user) {
        customAccordion.setUser(user);
        customAccordion.showUserCards();
    }
    
}