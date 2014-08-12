package memo.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import memo.events.AbstractProperrtyChangeEvent;
import memo.model.AbstractModel;
import memo.model.User;
import memo.utils.singleinstance.NewInstanceListener;
import memo.view.ViewInterface;


public abstract class AbstractController implements PropertyChangeListener, NewInstanceListener {

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
        AbstractProperrtyChangeEvent aEvt = (AbstractProperrtyChangeEvent)evt;
        for(int i = 0; i < registeredViews.size(); i++) {
            aEvt.setView(registeredViews.get(i));
            aEvt.perform();
        }
    }

    /* These methods are connected to the logic of program */

    public abstract void addToStartUp();
    public abstract void removeFromStartUp();
    public abstract boolean isAddedToStartUp();

    public abstract void exit();

    public abstract void showStageFromTray();
    public abstract void hideStageToTray();

    public abstract ArrayList<User> getUserList();
    public abstract void addUser(User user);

    @Override
    public void onNewInstance() {
        for (int i = 0; i < registeredViews.size(); i++){
            if (registeredViews.get(i).isShowing()){
                registeredViews.get(i).showToFront();
            }
            else{
                showStageFromTray();
                break;
            }
        }
    }

}
