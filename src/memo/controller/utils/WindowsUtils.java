

package memo.controller.utils;

import java.lang.reflect.InvocationTargetException;


public class WindowsUtils implements PlatformUtils{

    @Override
    public void addToStartup() {
        try {
        String path = ""; // path to Memo.exe file. Will be corrected later.
        WinRegistry.writeStringValue(WinRegistry.HKEY_LOCAL_MACHINE, 
                "SOFTWARE\\MICROSOFT\\Windows\\CurrentVersion\\Run", 
                "Memo", path);
        }
        catch(IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            System.err.println("Wrong path to exe file or problems wih WinRegistry");
            throw new RuntimeException(e);
        }
    }
    
}
