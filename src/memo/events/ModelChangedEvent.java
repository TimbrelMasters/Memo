package memo.events;

import java.beans.PropertyChangeEvent;
import memo.view.ViewInterface;


public abstract class ModelChangedEvent {

    protected ViewInterface view;

    public abstract void perform();

    public void setView(ViewInterface view) {
        this.view = view;
    }

}
