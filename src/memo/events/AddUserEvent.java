package memo.events;

import memo.model.User;

/**
 *
 * @author Pisarik
 */
public class AddUserEvent extends AbstractProperrtyChangeEvent{
    User user;

    public AddUserEvent(User user){
        this.user = user;
    }

    @Override
    public void perform() {
        if (view != null){
            view.addUser(user);
        }
        else{
            throw new RuntimeException("View isn't set at " + this.getClass().getName());
        }
    }

}
