

package memo.utils.platform;

import java.lang.reflect.InvocationTargetException;


public class WindowsUtils implements PlatformUtils{

    @Override
    public void addToStartup() {
        try {
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath(); // path to Memo.exe file. Will be corrected later.
        WinRegistry.writeStringValue(WinRegistry.HKEY_LOCAL_MACHINE, 
                "SOFTWARE\\MICROSOFT\\Windows\\CurrentVersion\\Run", 
                "Memo", path);
        }
        catch(IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            System.err.println("Wrong path to exe file or problems wih WinRegistry");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeFromStartUp() {
        try {
        WinRegistry.deleteValue(WinRegistry.HKEY_LOCAL_MACHINE, 
                "SOFTWARE\\MICROSOFT\\Windows\\CurrentVersion\\Run", "Memo");
        }
        catch(IllegalArgumentException e) {
            System.err.println("The program wasn't in the startup"); // Should be kind of MessageBox here 
                                                                     // or just empy catch
        } 
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
    
}
