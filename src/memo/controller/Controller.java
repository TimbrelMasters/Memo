package memo.controller;

import javafx.application.Platform;
import memo.utils.platform.PlatformUtils;
import memo.utils.platform.WindowsUtils;



public class Controller extends AbstractController {
    
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
        if (!registeredViews.get(0).isShowing()) {
            registeredViews.get(0).hideTrayIcon();
        }
        Platform.exit();
    }
    
}
