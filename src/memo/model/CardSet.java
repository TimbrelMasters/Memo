
package memo.model;

import java.util.ArrayList;

public class CardSet {

    private String name;
    private ArrayList<Card> cardSet;

    public CardSet() {
        cardSet = new ArrayList<Card>();
    }

    public CardSet(String name) {
        this.name = name;
    }

    public void addCard(Card card) {
        cardSet.add(card);
    }

    public int getCardsCount() {
        return cardSet.size();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Card> getCardSet() {
        return cardSet;
    }

    public void setCardSet(ArrayList<Card> cardSet) {
        this.cardSet = cardSet;
    }
    
}
