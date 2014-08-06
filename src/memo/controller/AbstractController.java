package memo.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import memo.model.AbstractModel;
import memo.view.ViewInterface;


public abstract class AbstractController implements PropertyChangeListener {
    
    protected ArrayList<AbstractModel> registeredModels;
    protected ArrayList<ViewInterface> registeredViews;

    public AbstractController() {
        registeredModels = new ArrayList<>();
        registeredViews = new ArrayList<>();
    }
    
    public void addModel(AbstractModel model) {
        registeredModels.add(model);
        model.addPropertyChangeListener(this);
    }
    
    public void removeModel(AbstractModel model) {
        registeredModels.remove(model);
        model.removePropertyChangeListener(this);
    }
    
    public void addView(ViewInterface view) {
        registeredViews.add(view);
    }
    
    public void removeView(ViewInterface view) {
        registeredViews.remove(view);
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        for(int i = 0; i < registeredViews.size(); i++) {
            registeredViews.get(i).modelPropertyChange(evt);
        }
    }
    
    /* These methods are connected to the logic of program */
    
    public abstract void addToStartUp();
    public abstract void removeFromStartUp();
    public abstract boolean isAddedToStartUp(); 
    
    public abstract void exit();
    
    public abstract void showStageFromTray();
    public abstract void hideStageToTray();
    
}
