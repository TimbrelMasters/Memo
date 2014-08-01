package memo.view;

import java.beans.PropertyChangeEvent;
import memo.controller.AbstractController;


public interface ViewInterface {
    /**
     * <p> Initializes control's handlers with current controller </p>
     */
    public void initialize();
    
    public void setController(AbstractController controller);
    
    /**
     * <p> Controller notifies this method about model changing
     * @param evt .getPropertyName() is name of variable which has been changed
     */
    public void modelPropertyChange(final PropertyChangeEvent evt);
    
    //public boolean isHideableToTray();
    
    
    /*
    public void handleAddToStartUpClick();
    public void handleExitClick();
    */
    public void showStage();
}
