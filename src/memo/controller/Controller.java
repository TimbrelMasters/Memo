
package memo.controller;

import javafx.application.Platform;
import memo.utils.platform.PlatformUtils;
import memo.utils.platform.WindowsUtils;
//import memo.view.ViewInterface;



public class Controller extends AbstractController {

    //private ViewInterface mainView;
    
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
        platformUtils.addToStartup();    
    }

    @Override
    public void removeFromStartUp() {
        platformUtils.removeFromStartUp();
    }

    @Override
    public void showStageFromTray() {
        /*mainView.hideTrayIcon();
        mainView.showStage();*/
        registeredViews.get(0).hideTrayIcon();
        registeredViews.get(0).showStage();
    }
    
    @Override
    public void hideStageToTray(){
        /*mainView.showTrayIcon();
        mainView.hideStage();*/
        registeredViews.get(0).showTrayIcon();
        registeredViews.get(0).hideStage();
    }
    
    

    @Override
    public void exit() {
        Platform.exit();
    }
    
}
