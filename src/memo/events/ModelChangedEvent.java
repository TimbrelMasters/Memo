package memo.events;

import java.beans.PropertyChangeEvent;
import memo.view.RootViewInterface;


public abstract class ModelChangedEvent {

    protected RootViewInterface view;

    public abstract void perform();

    public void setView(RootViewInterface view) {
        this.view = view;
    }

}
