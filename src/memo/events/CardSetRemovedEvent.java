
package memo.events;


public class CardSetRemovedEvent extends ModelChangedEvent {

    private int sectionNumber;
    private int cardSetNumber;
    
    public CardSetRemovedEvent() {
        super();
    }

    public CardSetRemovedEvent(int i, int j) {
        super();
        this.sectionNumber = i;
        this.cardSetNumber = j;
    }
    
    @Override
    public void perform() {
        view.removeCardSet(sectionNumber, cardSetNumber);
    }
    
}
