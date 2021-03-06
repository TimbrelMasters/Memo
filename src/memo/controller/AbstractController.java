package memo.controller;

import java.util.ArrayList;
import java.util.Locale;
import memo.events.ModelChangedEvent;
import memo.model.AbstractModel;
import memo.model.Card;
import memo.model.CardSet;
import memo.model.Section;
import memo.model.User;
import memo.utils.singleinstance.NewInstanceListener;
import memo.view.RootViewInterface;


public abstract class AbstractController implements ModelChangedListener, NewInstanceListener {

    public enum ControlPaneType {
        MAIN,
        USER_EDIT,
        THEME_EDIT
    };

    protected ArrayList<AbstractModel> registeredModels;
    protected ArrayList<RootViewInterface> registeredViews;

    public AbstractController() {
        registeredModels = new ArrayList<>();
        registeredViews = new ArrayList<>();
    }

    public void addModel(AbstractModel model) {
        registeredModels.add(model);
        model.addModelChangedistener(this);
    }

    public void removeModel(AbstractModel model) {
        registeredModels.remove(model);
        model.removeModelChangedListener(this);
    }

    public void addView(RootViewInterface view) {
        registeredViews.add(view);
    }

    public void removeView(RootViewInterface view) {
        registeredViews.remove(view);
    }

    @Override
    public void modelChanged(ModelChangedEvent event) {
        for(int i = 0; i < registeredViews.size(); i++) {
            event.setView(registeredViews.get(i));
            event.perform();
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
    public abstract void setCurrentUser(User user);
    
    //public abstract int getCurrentUserIndex();

    public abstract void addUser(User user);
    public abstract void addCard(int i, int j, Card card);
    public abstract void addCardSet(int i, CardSet cardSet);
    public abstract void addSection(Section section);

    public abstract void removeUser();
    public abstract void removeCard(int i, int j, int k);
    public abstract void removeCardSet(int i, int j);
    public abstract void removeSection(int i);

    public abstract void changeSectionName(int sectionIndex, String newName);
    public abstract void changeUserName(String newName);

    public abstract void setLanguage(Locale locale);

    /**
     * Replaces controlPane that provide other functionality
     * @param controlPane - pane with other functions
     * @param type - type of panel for specified information
     */
    public abstract void changeControlPane(ControlPaneType type,
             int currentSection, int currentCardSet, int currentCard); //maybe will be wrapped in class-wrapper);

    public abstract void updateView(int currentSection, int currentCardSet, int currentCard);




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
