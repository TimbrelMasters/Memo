package memo.model;

import java.util.ArrayList;
import memo.events.AddUserEvent;

/**
 *
 * @author Pisarik
 */
public class Model extends AbstractModel{

    ArrayList<User> users;

    public Model(){
        users = new ArrayList<>();
        users.add(new User());
    }

    @Override
    public ArrayList<User> getUserList() {
        return users;
    }

    @Override
    public void addUser(User user) {
        users.add(user);
        firePropertyChange(new AddUserEvent(user));
    }
}
