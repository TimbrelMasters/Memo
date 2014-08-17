package memo.view;

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

    /**
     * Bring the Window to foreground
     */
    public void showToFront();
}
