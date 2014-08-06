

package memo.utils.platform;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;


public class WindowsUtils implements PlatformUtils{

    @Override
    public void addToStartUp() {
        try {
        String path = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getAbsolutePath();
        
        WinRegistry.writeStringValue(WinRegistry.HKEY_LOCAL_MACHINE, 
                "SOFTWARE\\MICROSOFT\\Windows\\CurrentVersion\\Run", 
                "Memo", path);
        }
        catch(IllegalArgumentException | IllegalAccessException | InvocationTargetException |
                URISyntaxException e) {
            System.err.println("Wrong path to exe file or problems with WinRegistry");
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

    @Override
    public boolean isAddedToStartUp() {
        try{
            String value = WinRegistry.readString(WinRegistry.HKEY_LOCAL_MACHINE, 
                "SOFTWARE\\MICROSOFT\\Windows\\CurrentVersion\\Run", "Memo");
            return value != null;
        }
        catch(IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            System.err.println("Wrong path to exe file or problems with WinRegistry");
            throw new RuntimeException(e);
        }
    }
    
}
