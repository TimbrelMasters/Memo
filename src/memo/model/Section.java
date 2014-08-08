
package memo.model;

import java.util.ArrayList;

public class Section {
    
    private String name;
    private ArrayList<CardSet> cardSets;
    
    public Section() {
        cardSets = new ArrayList<CardSet>();
    }
    
    public Section(String name, ArrayList<CardSet> cardSets) {
        this.name = name;
        this.cardSets = cardSets;
    }
    
    public Section(ArrayList<CardSet> cardSets) {
        this.cardSets = cardSets;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void addCardSet(CardSet cardSet) {
        cardSets.add(cardSet);
    }
    
    public int getCardSetsCount() {
        return cardSets.size();
    }
    
}
