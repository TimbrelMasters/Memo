package memo.events;

import java.beans.PropertyChangeEvent;
import memo.view.ViewInterface;

/**
 *
 * @author Pisarik
 */
public abstract class AbstractProperrtyChangeEvent extends PropertyChangeEvent{

    protected ViewInterface view;

    public AbstractProperrtyChangeEvent() {
        super(new Object(), "", "3", "4");
    }

    public abstract void perform();

    public void setView(ViewInterface view) {
        this.view = view;
    }

}
