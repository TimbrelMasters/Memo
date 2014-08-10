
package memo.model;

import java.util.ArrayList;

public class CardSet {

    private ArrayList<Card> cardSet;

    public CardSet() {
        cardSet = new ArrayList<Card>();
    }

    public CardSet(ArrayList<Card> cardSet) {
        this.cardSet = cardSet;
    }

    public void addCard(Card card) {
        cardSet.add(card);
    }

    public int getCardsCount() {
        return cardSet.size();
    }

}
