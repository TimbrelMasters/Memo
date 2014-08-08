package memo.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Pisarik
 */
public class Model extends AbstractModel{
    ObservableList<User> users;
    
    public Model(){
        users = FXCollections.observableArrayList();
        users.add(new User());
    }

    @Override
    public ObservableList<User> getUserList() {
        return users;
    }
}
