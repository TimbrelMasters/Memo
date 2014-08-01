
package memo.controller;

import javafx.application.Platform;
import memo.utils.platform.PlatformUtils;
import memo.utils.platform.WindowsUtils;
import memo.view.ViewInterface;



public class Controller extends AbstractController {

    private ViewInterface view;
    
    private PlatformUtils platformUtils;

    public Controller() {
        platformUtils = new WindowsUtils();
    }

    public Controller(PlatformUtils platformUtils) {
        this.platformUtils = platformUtils;
    }

    public void setPlatformUtils(PlatformUtils platformUtils) {
        this.platformUtils = platformUtils;
    }

    public void setView(ViewInterface view) {
        this.view = view;
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
    public void showStage() {
        view.showStage();
    }
    
    

    @Override
    public void exit() {
        Platform.exit();
    }
    
}
