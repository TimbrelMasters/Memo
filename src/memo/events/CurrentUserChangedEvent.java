
package memo.events;

import memo.model.User;


public class CurrentUserChangedEvent extends ModelChangedEvent {

    private User user;
        
    public CurrentUserChangedEvent(User user) {
        super();
        this.user = user;
    }

    @Override
    public void perform() {
        view.showUserCards(user);
    }
    
}
