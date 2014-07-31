
package memo.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import memo.model.AbstractModel;
import memo.view.ViewInterface;


public abstract class AbstractController implements PropertyChangeListener {
    
    private ArrayList<AbstractModel> registeredModels;
    private ArrayList<ViewInterface> registeredViews;

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
    
    protected void setModelProperty(String propertyName, Object newValue) {
        for (AbstractModel model: registeredModels) {
            try {
                Method method = model.getClass().
                    getMethod("set"+propertyName, new Class[] {newValue.getClass()});
                method.invoke(model, newValue);
            } 
            catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                System.err.println("Exception caused by Reflection API");
                throw new RuntimeException(e);
            }
        }
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        for(int i = 0; i < registeredViews.size(); i++) {
            //registeredViews[i].modelPropertyChange(evt);
        }
    }
    
    /* These methods are connected to the logic of program */
    
    public abstract void addToStartUp();
    
    public abstract void removeFromStartUp();
    
    public abstract void exit();
    
}
