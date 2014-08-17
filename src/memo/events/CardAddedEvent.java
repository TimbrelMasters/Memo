
package memo.events;

import memo.model.Card;



public class CardAddedEvent extends ModelChangedEvent {

    private int sectionNumber;
    private int cardSetNumber;
    private Card card;
    
    public CardAddedEvent() {
        super();
    }

    public CardAddedEvent(int i, int j, Card card) {
        super();
        this.sectionNumber = i;
        this.cardSetNumber = j;
        this.card = card;
    }
    
    @Override
    public void perform() {
        view.addCard(sectionNumber, cardSetNumber, card);
    }
    
}
