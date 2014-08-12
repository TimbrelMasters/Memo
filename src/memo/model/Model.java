package memo.model;

import java.util.ArrayList;

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
        System.out.println("in model");
        users.add(user);
        //firePropertyChange(new AddUserEvent(user));
        propertyChangeSupport.firePropertyChange("name", "1", "2");
    }
}
