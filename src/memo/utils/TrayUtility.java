package memo.utils;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * This class simplifies work with SystemTray
 * @author Pisarik
 */
public class TrayUtility {
    private SystemTray tray;
    private final TrayIcon icon;
    private static final boolean isTraySupported;

    static{
        isTraySupported = SystemTray.isSupported();
    }

    public TrayUtility(){
        icon = new TrayIcon(new BufferedImage(1, 1, 1), null, new PopupMenu());
        icon.setImageAutoSize(true);

        if (isTraySupported){
            tray = SystemTray.getSystemTray();
        }
    }

    /**
     * if tray is supported, shows it on the task bar
     * else does nothing
     */
    public void showIcon() {
        try {
            if (isTraySupported){
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
     * if tray is supported, hides it from task bar
     * else does nothing
     */
    public void hideIcon() {
        if (isTraySupported){
            tray.remove(icon);
        }
        else{
            //do nothing
        }
    }

    public void setOnDoubleClick(ActionListener e){
        icon.addActionListener(e);
    }

    /**
     *
     * @param filepath path to image JPEG, GIF, PNG
     */
    public void setIcon(String filepath){
        Image image = Toolkit.getDefaultToolkit().getImage(filepath);
        icon.setImage(image);
    }

    /**
     *
     * @param url url to image JPEG, GIF, PNG
     */
    public void setIcon(URL url){
        Image image = Toolkit.getDefaultToolkit().getImage(url);
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

    public static boolean isTraySupported(){
        return isTraySupported;
    }

}
