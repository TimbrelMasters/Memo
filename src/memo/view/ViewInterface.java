package memo.view;

import memo.controller.AbstractController;


public interface ViewInterface {
    /**
     * <p> Initializes control's handlers with current controller </p>
     */
    public void initialize();
    
    public void setController(AbstractController controller);
    
    
    /*
    public void handleAddToStartUpClick();
    public void handleExitClick();
    */
}
