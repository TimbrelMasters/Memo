package memo.controller;

import java.util.ArrayList;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import memo.model.Card;
import memo.model.CardSet;
import memo.model.Section;
import memo.model.User;
import memo.utils.platform.PlatformUtils;
import memo.utils.platform.WindowsUtils;
import memo.utils.singleinstance.SingleInstanceUtility;



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
    public void changeControlPane(Pane controlPane) {
        registeredViews.get(0).setControlPane(controlPane);
    }

}
