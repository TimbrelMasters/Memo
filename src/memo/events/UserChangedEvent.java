package memo.events;

import memo.model.User;

/**
 *
 * @author Pisarik
 */
public class UserChangedEvent extends ModelChangedEvent{

    private User user;

    public UserChangedEvent(User user){
        this.user = user;
    }

    @Override
    public void perform() {
        if (view != null){
            view.changeUserName("");
        }
        else{
            throw new RuntimeException("View isn't set at " + this.getClass().getName());
        }
    }
}