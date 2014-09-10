package memo.controller;

import java.util.ArrayList;
import java.util.Locale;
import javafx.application.Platform;
import memo.model.AbstractModel;
import memo.model.Card;
import memo.model.CardSet;
import memo.model.Section;
import memo.model.User;
import memo.utils.internationalization.Internationalizator;
import memo.utils.platform.PlatformUtils;
import memo.utils.platform.WindowsUtils;
import memo.utils.singleinstance.SingleInstanceUtility;
import memo.view.RootViewInterface;



public class Controller extends AbstractController{

    private PlatformUtils platformUtils;

    public Controller() {
        this(new WindowsUtils());
    }

    public Controller(PlatformUtils platformUtils) {
        this.platformUtils = platformUtils;
    }

    public void setPlatformUtils(PlatformUtils platformUtils) {
        this.platformUtils = platformUtils;
    }

    @Override
    public void addToStartUp() {
        platformUtils.addToStartUp();
    }

    @Override
    public void removeFromStartUp() {
        platformUtils.removeFromStartUp();
    }

    @Override
    public boolean isAddedToStartUp() {
        return platformUtils.isAddedToStartUp();
    }

    @Override
    public void showStageFromTray() {
        for (int i = 0; i < registeredViews.size(); i++) {
            registeredViews.get(i).hideTrayIcon();
            registeredViews.get(i).showStage();
        }
    }

    @Override
    public void hideStageToTray() {
        for (int i = 0; i < registeredViews.size(); i++) {
            registeredViews.get(i).showTrayIcon();
            registeredViews.get(i).hideStage();
        }
    }

    @Override
    public void exit() {
        for (int i = 0; i < registeredViews.size(); i++){
            if (!registeredViews.get(i).isShowing()) {
                registeredViews.get(i).hideTrayIcon();
            }
        }
        SingleInstanceUtility.closeInstance();
        Platform.exit();
    }

    @Override
    public ArrayList<User> getUserList(){
        return registeredModels.get(0).getUserList();
    }

    @Override
    public void addUser(User user) {
        registeredModels.get(0).addUser(user);
    }

    @Override
    public void setCurrentUser(User user) {
        registeredModels.get(0).setCurrentUser(user);
    }

    @Override
    public void addCard(int i, int j, Card card) {
        registeredModels.get(0).addCard(i, j, card);
    }

    @Override
    public void addCardSet(int i, CardSet cardSet) {
        registeredModels.get(0).addCardSet(i, cardSet);
    }

    @Override
    public void addSection(Section section) {
        registeredModels.get(0).addSection(section);
    }

    @Override
    public void removeCard(int i, int j, int k) {
        registeredModels.get(0).removeCard(i, j, k);
    }

    @Override
    public void removeCardSet(int i, int j) {
        registeredModels.get(0).removeCardSet(i, j);
    }

    @Override
    public void removeSection(int i) {
        registeredModels.get(0).removeSection(i);
    }

    @Override
    public void changeSectionName(int i, String newName){
        //some checks..
        registeredModels.get(0).changeSectionName(i, newName);
    }

    @Override
    public void setLanguage(Locale locale) {
        Internationalizator.newInstance().setLocale(locale);
        registeredModels.get(0).setLocale(locale);
    }
    
    

    @Override
    public void changeControlPane(ControlPaneType type,
            int currentSection, int currentCardSet, int currentCard) { //maybe will be wrapped in class-wrapper
        registeredViews.get(0).setControlPaneType(type);
        updateView(currentSection, currentCardSet, currentCard);
    }

    @Override
    public void updateView(int currentSection, int currentCardSet, int currentCard){
        AbstractModel model = registeredModels.get(0);
        RootViewInterface view = registeredViews.get(0);
        ControlPaneType type = view.getControlPaneType();

        if (type == ControlPaneType.ThemeEdit){
            if (currentSection != -1){
                view.setThemeName(model.getSection(currentSection).getName());
            }
        }
        else if (type == ControlPaneType.Main){
            //empty yet
        }
        else{
            throw new RuntimeException("Type of Control Pane not recognized");
        }
    }

}
