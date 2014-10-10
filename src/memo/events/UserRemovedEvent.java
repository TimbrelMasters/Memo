
package memo.events;


public class UserRemovedEvent extends ModelChangedEvent {

    private final int userIndex;

    public UserRemovedEvent(int userIndex) {
        this.userIndex = userIndex;
    }
    
    @Override
    public void perform() {
        view.removeUser(userIndex);
    }
    
}
