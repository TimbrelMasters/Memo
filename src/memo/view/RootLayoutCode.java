package memo.view;

import java.io.IOException;
import java.net.URL;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.WindowEvent;
import memo.controller.AbstractController;
import memo.controller.AbstractController.ControlPaneType;
import memo.model.Card;
import memo.model.CardSet;
import memo.model.Section;
import memo.model.User;
import memo.utils.TrayUtility;

/**
 *
 * @author Pisarik
 */
public class RootLayoutCode extends AbstractView implements RootViewInterface{

    private final URL TRAY_ICON_URL;

    private ControlPaneType currentControlPaneType;
    private MainViewCode mainView;
    private EditThemeCode editThemeView;

    private TrayUtility trayUtility;
    private java.awt.PopupMenu trayMenu;
    private java.awt.MenuItem trayExitItem;
    private java.awt.MenuItem trayOpenItem;

    @FXML
    private BorderPane thisPane;

    @FXML
    private RadioMenuItem addToStratUpItem;

    @FXML
    private MenuItem exitItem;

    @FXML
    private ComboBox<User> userComboBox;

    @FXML
    private Accordion themeAccordion;

    @FXML
    private ScrollPane themeScroll;

    private CustomAccordion customAccordion;

    private ObservableList<User> users;

 /*
   ***
   *** Initialize methods
   ***
    */

    @Override
    public void manualInitialize() {
        /*------CONSTRUCTOR----*/
        setRootPane(thisPane);

        /*-------SYSTEM--------*/
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

        /*------CONTROLS-------*/
        initUserComboBox();
        handleSelectUser();

        EventHandler<MouseEvent> onOpenTheme = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (currentControlPaneType != ControlPaneType.ThemeEdit &&
                        customAccordion.getCurrentSection() != -1){
                    controller.changeControlPane(ControlPaneType.ThemeEdit,
                        customAccordion.getCurrentSection(), customAccordion.getCurrentSet(), customAccordion.getCurrentCard());
                }
            }
        };
        themeScroll.prefHeightProperty().bind(themeAccordion.heightProperty());
        themeScroll.maxHeightProperty().bind(thisPane.heightProperty());
        customAccordion = new CustomAccordion(users.get(0), themeAccordion, controller, onOpenTheme);

        /*---AFTER ROOT INIT---*/
        initInnerViews();
        setControlPaneType(ControlPaneType.Main);
    }

    private void initInnerViews(){
        try{
            FXMLLoader mainViewLoader = new FXMLLoader(this.getClass().getResource("MainViewDesign.fxml"));
            mainViewLoader.load();
            mainView = mainViewLoader.getController();
            mainView.setController(controller);
            mainView.setPrimaryStage(primaryStage);
            mainView.manualInitialize();

            FXMLLoader editThemeViewLoader = new FXMLLoader(this.getClass().getResource("EditThemeDesign.fxml"));
            editThemeViewLoader.load();
            editThemeView = editThemeViewLoader.getController();
            editThemeView.setController(controller);
            editThemeView.setPrimaryStage(primaryStage);
            editThemeView.setCustomAccordion(customAccordion);
            editThemeView.manualInitialize();
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
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
    private void OnUserAdd(ActionEvent event){
        controller.addUser(new User("Pisarik"));
    }


    private void handleSelectUser(){
        userComboBox.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends User> observable, User oldValue, User newValue) -> {
            controller.setCurrentUser(newValue);
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
    public void setControlPaneType(ControlPaneType type){
        currentControlPaneType = type;

        if (type == ControlPaneType.ThemeEdit){
            thisPane.setCenter(editThemeView.getRootPane());
        }
        else if (type == ControlPaneType.Main){
            thisPane.setCenter(mainView.getRootPane());
        }
        else{
            throw new RuntimeException("Type of Control Pane not recognized");
        }
    }

    @Override
    public ControlPaneType getControlPaneType() {
        return currentControlPaneType;
    }

    @Override
    public void setThemeName(String name){
        editThemeView.setThemeName(name);
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

    @Override
    public void addCard(int i, int j, Card card) {
        customAccordion.addCard(i, j, card);
    }

    @Override
    public void addCardSet(int i, CardSet cardSet) {
        customAccordion.addCardSet(i, cardSet);
    }

    @Override
    public void addSection(Section section) {
        customAccordion.addSection(section);
    }

    @Override
    public void removeCard(int i, int j, int k) {
        customAccordion.removeCard(i, j, k);
    }

    @Override
    public void removeCardSet(int i, int j) {
        customAccordion.removeCardSet(i, j);
    }

    @Override
    public void removeSection(int i) {
        customAccordion.removeSection(i);
    }

    @Override
    public void changeSectionName(int i, String newName){
        customAccordion.changeSectionName(i, newName);
    }

}
