
package memo.controller;

import javafx.application.Platform;
import memo.controller.utils.PlatformUtils;
import memo.controller.utils.WindowsUtils;


public class Cotroller extends AbstractController {

    private PlatformUtils platformUtils;

    public Cotroller() {
        platformUtils = new WindowsUtils();
    }

    public Cotroller(PlatformUtils platformUtils) {
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
