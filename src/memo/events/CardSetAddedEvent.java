
package memo.events;

import memo.model.CardSet;


public class CardSetAddedEvent extends ModelChangedEvent {

    private int sectionNumber;
    private CardSet cardSet;
    
    public CardSetAddedEvent() {
        super();
    }
    
    public CardSetAddedEvent(int i, CardSet cardSet) {
        super();
        this.sectionNumber = i;
        this.cardSet = cardSet;
    }
    
    @Override
    public void perform() {
        view.addCardSet(sectionNumber, cardSet);
    }
    
}
