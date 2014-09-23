package memo.view;

import memo.controller.AbstractController.ControlPaneType;
import memo.model.Card;
import memo.model.CardSet;
import memo.model.Section;
import memo.model.User;


public interface RootViewInterface {

    //public AbstractView getAbstractView();

    public void hideStage();
    public void showStage();

    public void showTrayIcon();
    public void hideTrayIcon();

    public boolean isShowing();

    public void addUser(User user);
    public void showUserCards(User user);
    public void addCard(int i, int j, Card card);
    public void addCardSet(int i, CardSet cardSet);
    public void addSection(Section section);

    public abstract void removeCard(int i, int j, int k);
    public abstract void removeCardSet(int i, int j);
    public abstract void removeSection(int i);

    public abstract void changeSectionName(int sectionIndex, String newName);
    public abstract void changeUserName(String newName);

    public abstract void setControlPaneType(ControlPaneType type);
    public abstract ControlPaneType getControlPaneType();
    public abstract void setThemeName(String themeName);
    public abstract void setUserName(String userName);

    /**
     * Bring the Window to foreground
     */
    public void showToFront();

}
