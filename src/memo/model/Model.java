package memo.model;

import java.util.ArrayList;
import memo.events.AddUserEvent;
import memo.events.CurrentUserChangedEvent;

/**
 *
 * @author Pisarik
 */
public class Model extends AbstractModel{

    private ArrayList<User> users;
    private User currentUser;

    public Model(){
        super();
        users = new ArrayList<>();
        users.add(new User());
        currentUser = users.get(0);
    }

    @Override
    public ArrayList<User> getUserList() {
        return users;
    }

    @Override
    public void addUser(User user) {
        users.add(user);
        fireModelChanged(new AddUserEvent(user));
    }

    @Override
    public void setCurrentUser(User user) {
        this.currentUser = user;
        fireModelChanged(new CurrentUserChangedEvent(user));
    }
    
}
