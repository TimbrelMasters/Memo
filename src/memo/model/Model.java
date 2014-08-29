package memo.model;

import java.util.ArrayList;
import memo.events.AddUserEvent;
import memo.events.CardAddedEvent;
import memo.events.CardRemovedEvent;
import memo.events.CardSetAddedEvent;
import memo.events.CardSetRemovedEvent;
import memo.events.CurrentUserChangedEvent;
import memo.events.SectionAddedEvent;
import memo.events.SectionChangedEvent;
import memo.events.SectionRemovedEvent;

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
        currentUser.getSections().get(i).getCardSets().get(j).getCards().add(card);
        fireModelChanged(new CardAddedEvent(i, j, card));
    }

    @Override
    public void addCardSet(int i, CardSet cardSet) {
        currentUser.getSections().get(i).getCardSets().add(cardSet);
        fireModelChanged(new CardSetAddedEvent(i, cardSet));
    }

    @Override
    public void addSection(Section section) {
        currentUser.getSections().add(section);
        fireModelChanged(new SectionAddedEvent(section));
    }

    @Override
    public void removeCard(int i, int j, int k) {
        currentUser.getSections().get(i).getCardSets().get(j).getCards().remove(k);
        fireModelChanged(new CardRemovedEvent(i, j, k));
    }

    @Override
    public void removeCardSet(int i, int j) {
        currentUser.getSections().get(i).getCardSets().remove(j);
        fireModelChanged(new CardSetRemovedEvent(i, j));
    }

    @Override
    public void removeSection(int i) {
        currentUser.getSections().remove(i);
        fireModelChanged(new SectionRemovedEvent(i));
    }

    @Override
    public void changeSectionName(int i, String newName){
        currentUser.getSections().get(i).setName(newName);
        fireModelChanged(new SectionChangedEvent(i, currentUser.getSections().get(i)));
    }

    @Override
    public Section getSection(int i){
        return currentUser.getSections().get(i);
    }

    @Override
    public User getCurrentUser() {
        return currentUser;
    }




}
