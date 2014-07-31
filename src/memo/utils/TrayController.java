package memo.utils;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;

/**
 * This class simplify working with SystemTray
 * @author Pisarik
 */
public class TrayController {
    private SystemTray tray = null;
    private TrayIcon icon = null;
    private static boolean isSupportTray = false;
    
    public TrayController(){
        isSupportTray = SystemTray.isSupported();
        icon = new TrayIcon(null, null, new PopupMenu());
        if (isSupportTray){
            tray = SystemTray.getSystemTray();
        }
    }
    
    /**
     * if tray supported shows it into task bar
     * else do nothing
     */
    public void show() {
        try {
            if (isSupportTray){
                tray.add(icon);
            }
            else {
                //do nothing
            }
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * if tray supported hides it from task bar
     * else do nothing 
     */
    public void hide() throws NullPointerException{
        if (isSupportTray){
            tray.remove(icon);
        }
        else{
            //do nothing
        }
    }
    
    /**
     *  
     * @param filepath path to image JPEG, GIF, PNG
     */
    public void setIcon(String filepath){
        Image image = Toolkit.getDefaultToolkit().getImage(filepath);
        icon.setImage(image);
    }
    
    public void setMenu(PopupMenu menu){
        icon.setPopupMenu(menu);
    }
    
    public void setToolTip(String tip){
        icon.setToolTip(tip);
    }
    
    public Image getImage(){
        return icon.getImage();
    }
    
    public PopupMenu getMenu(){
        return icon.getPopupMenu();
    }
    
    public String getToolTip(){
        return icon.getToolTip();
    }
    
    public static boolean isSupportTray(){
        return isSupportTray;
    }

}
