

package memo.events;


public class CardRemovedEvent extends ModelChangedEvent {

    private int sectionNumber;
    private int cardSetNumber;
    private int cardNumber;
    
    public CardRemovedEvent() {
        super();
    }

    public CardRemovedEvent(int i, int j, int k) {
        super();
        this.sectionNumber = i;
        this.cardSetNumber = j;
        this.cardNumber = k;
    }
    
    @Override
    public void perform() {
        view.removeCard(sectionNumber, cardSetNumber, cardNumber);
    }
    
}
