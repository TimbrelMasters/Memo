package memo.model;

import java.util.ArrayList;
import memo.events.AddUserEvent;
import memo.events.CardAddedEvent;
import memo.events.CardSetAddedEvent;
import memo.events.CurrentUserChangedEvent;
import memo.events.SectionAddedEvent;

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

    @Override
    public void addCard(int i, int j, Card card) {
        currentUser.getSections().get(i).getCardSets().get(j).addCard(card);
        fireModelChanged(new CardAddedEvent(i, j, card));
    }

    @Override
    public void addCardSet(int i, CardSet cardSet) {
        currentUser.getSections().get(i).addCardSet(cardSet);
        fireModelChanged(new CardSetAddedEvent(i, cardSet));
    }

    @Override
    public void addSection(Section section) {
        currentUser.addSection(section);
        fireModelChanged(new SectionAddedEvent(section));
    }
    
    
    
}
