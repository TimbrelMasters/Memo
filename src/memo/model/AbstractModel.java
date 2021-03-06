package memo.model;

import java.util.ArrayList;
import java.util.Locale;
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
    public abstract void removeUser(int index);
    public abstract void setCurrentUser(User user);
    public abstract User getCurrentUser();

    public abstract void addCard(int i, int j, Card card);
    public abstract void addCardSet(int i, CardSet cardSet);
    public abstract void addSection(Section section);

    public abstract void removeCard(int i, int j, int k);
    public abstract void removeCardSet(int i, int j);
    public abstract void removeSection(int i);

    public abstract void changeSectionName(int sectionIndex, String newName);
    public abstract void changeCurrentUserName(String newName);

    public abstract Section getSection(int i);

    public abstract void setLocale(Locale locale);

}
