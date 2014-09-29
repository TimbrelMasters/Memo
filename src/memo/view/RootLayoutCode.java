package memo.view;

import java.io.IOException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import memo.controller.AbstractController;
import memo.controller.AbstractController.ControlPaneType;
import memo.model.Card;
import memo.model.CardSet;
import memo.model.Section;
import memo.model.User;
import memo.utils.TrayUtility;
import memo.utils.internationalization.Internationalizator;
import memo.utils.internationalization.InternationalizedAWTMenuItem;
import memo.utils.internationalization.InternationalizedLabeledComponent;
import memo.utils.internationalization.InternationalizedMenuItem;

/**
 *
 * @author Pisarik
 */
public class RootLayoutCode extends AbstractView implements RootViewInterface {

    private final URL TRAY_ICON_URL;

    private ControlPaneType currentControlPaneType;
    private MainViewCode mainView;
    private EditThemeCode editThemeView;
    private EditUserCode editUserView;

    private TrayUtility trayUtility;
    private java.awt.PopupMenu trayMenu;
    private java.awt.MenuItem trayExitItem;
    private java.awt.MenuItem trayOpenItem;

    private Internationalizator internationalizator;

    @FXML private BorderPane thisPane;

    @FXML private ComboBox<Map.Entry<Image, Locale>> languageComboBox;
    @FXML private Menu fileMenu;
    @FXML private RadioMenuItem addToStratUpItem;
    @FXML private MenuItem exitItem;

    @FXML private Label userLabel;
    @FXML private Button addUserButton;
    @FXML private Button editUserButton;
    @FXML private ComboBox<User> userComboBox;

    @FXML private Accordion themeAccordion;
    @FXML private ScrollPane themeScroll;
    @FXML private Button addThemeButton;

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
        internationalizator = Internationalizator.newInstance();

        /*-----MAIN MENU-------*/
        handleAddToStartUpClick();
        handleExitClick();
        handleCloseRequest();

        /*-----TRAY MENU-------*/
        handleTrayOpenClick();
        handleTrayExitClick();
        handleTrayDoubleClick();

        /*------CONTROLS-------*/
        initLanguageComboBox();
        initUserComboBox();

        handleSelectLanguage();
        handleSelectUser();
        handleChangeUser();

        initCustomAccordion();
        internationalizeComponents();

        /*---AFTER ROOT INIT---*/
        initInnerViews();
        setControlPaneType(ControlPaneType.Main);

        /*------INIT DATA-----*/
        if (users.size() != 0){
            controller.setCurrentUser(users.get(0));
        }
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

            FXMLLoader editUserViewLoader = new FXMLLoader(this.getClass().getResource("EditUserDesign.fxml"));
            editUserViewLoader.load();
            editUserView = editUserViewLoader.getController();
            editUserView.setController(controller);
            editUserView.setPrimaryStage(primaryStage);
            editUserView.setCustomAccordion(customAccordion);
            editUserView.manualInitialize();
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private void initLanguageComboBox() {
        Map.Entry russianEntry = new AbstractMap.SimpleEntry(
                new Image(this.getClass().getResourceAsStream("resources/russia.png")),
                new Locale("ru", "RU"));
        Map.Entry britainEntry = new AbstractMap.SimpleEntry(
                new Image(this.getClass().getResourceAsStream("resources/britain.png")),
                new Locale("en", "EN"));
        languageComboBox.getItems().addAll(russianEntry, britainEntry);

        Callback<ListView<Map.Entry<Image, Locale>>, ListCell<Map.Entry<Image, Locale>>> cellfactory =
                               (ListView<Map.Entry<Image, Locale>> p) -> new LanguageComboBoxListCell();
        languageComboBox.setCellFactory(cellfactory);
        languageComboBox.setButtonCell(cellfactory.call(null));
        List<Map.Entry<Image, Locale>> languageEntries = languageComboBox.getItems();
        Locale localeToSelect = internationalizator.getLocale();
        for(int i = 0; i < languageEntries.size(); i++) {
            if(languageEntries.get(i).getValue().equals(localeToSelect)) {
                languageComboBox.getSelectionModel().select(i);
                break;
            }
        }
    }

    private void initCustomAccordion(){
        themeScroll.prefHeightProperty().bind(themeAccordion.heightProperty());
        Platform.runLater(() -> {
            themeScroll.autosize();
        });

        EventHandler<MouseEvent> onOpenTheme = (MouseEvent event) -> {
            if (customAccordion.getCurrentSection() != -1) {
                 controller.changeControlPane(ControlPaneType.ThemeEdit,
                        customAccordion.getCurrentSection(), customAccordion.getCurrentSet(), customAccordion.getCurrentCard());
            }
            else{
                controller.changeControlPane(ControlPaneType.Main, -1, -1, -1);
            }
        };

        customAccordion = new CustomAccordion(themeAccordion, controller, onOpenTheme, addThemeButton);
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

    private void internationalizeComponents() {
        internationalizator.addObserver(new InternationalizedMenuItem(addToStratUpItem, "key.addToStartUp"));
        internationalizator.addObserver(new InternationalizedMenuItem(exitItem, "key.exit"));
        internationalizator.addObserver(new InternationalizedMenuItem(fileMenu, "key.file"));
        internationalizator.addObserver(new InternationalizedLabeledComponent(userLabel, "key.user"));
        internationalizator.addObserver(new InternationalizedLabeledComponent(addThemeButton, "key.addNewTheme"));
        internationalizator.addObserver(new InternationalizedAWTMenuItem(trayOpenItem, "key.open"));
        internationalizator.addObserver(new InternationalizedAWTMenuItem(trayExitItem, "key.exit"));
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

    private void handleExitClick() {
        exitItem.setOnAction((ActionEvent e) -> {
            controller.exit();
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

    @FXML
    private void OnUserAdd(ActionEvent event){
        controller.addUser(new User("Unknown"));
        userComboBox.getSelectionModel().selectLast();

        controller.changeControlPane(ControlPaneType.UserEdit,
                    customAccordion.getCurrentSection(), customAccordion.getCurrentSet(), customAccordion.getCurrentCard());
    }

    @FXML
    private void OnUserEdit(){
        if (!userComboBox.getSelectionModel().isEmpty()){
            controller.changeControlPane(ControlPaneType.UserEdit,
                    customAccordion.getCurrentSection(), customAccordion.getCurrentSet(), customAccordion.getCurrentCard());
        }
    }

    private void handleSelectUser(){
        userComboBox.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends User> observable, User oldValue, User newValue) -> {
            controller.setCurrentUser(newValue);
        });
    }

    private void handleChangeUser(){

    }

    private void handleSelectLanguage() {
        languageComboBox.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends Map.Entry<Image, Locale>> observable, Map.Entry<Image, Locale> oldValue, Map.Entry<Image, Locale> newValue) -> {
                    controller.setLanguage(newValue.getValue());
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

        if (type == ControlPaneType.UserEdit){
            thisPane.setCenter(editUserView.getRootPane());
        }
        else if (type == ControlPaneType.ThemeEdit){
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

    @Override
    public void setUserName(String name){
        editUserView.setUserName(name);
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
        customAccordion.showUserCards(user);
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

    @Override
    public void changeUserName(String newName){
        users.add(new User("systemUser"));
        users.remove(users.size() - 1);
    }

}
