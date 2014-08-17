package memo.model;

import java.util.ArrayList;
import memo.controller.ModelChangedListener;
import memo.events.ModelChangedEvent;


public abstract class AbstractModel {

    private final ArrayList<ModelChangedListener> listeners;

    public AbstractModel() {
        this.listeners = new ArrayList<>();
    }
    
    public void addModelChangedistener(ModelChangedListener listener) {
        listeners.add(listener);
    }
    
    public void removeModelChangedListener(ModelChangedListener listener) {
        listeners.remove(listener);
    }
    
    public void fireModelChanged(ModelChangedEvent event) {
        for(int i = 0; i < listeners.size(); i++) {
            listeners.get(i).modelChanged(event);
        }
    }
    
    public abstract ArrayList<User> getUserList();
    public abstract void addUser(User user);
    public abstract void setCurrentUser(User user);
    public abstract void addCard(int i, int j, Card card);
}
