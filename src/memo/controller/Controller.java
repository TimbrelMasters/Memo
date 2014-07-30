
package memo.controller;

import javafx.application.Platform;
import memo.controller.utils.PlatformUtils;
import memo.controller.utils.WindowsUtils;


public class Controller extends AbstractController {

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
    
    @Override
    public void addToStartUp() {
        platformUtils.addToStartup();    
    }

    @Override
    public void exit() {
        Platform.exit();
    }
    
}
