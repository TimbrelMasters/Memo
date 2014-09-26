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

    public Section(String name){
        this.name = name;
        cardSets = new ArrayList<CardSet>();
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    void addCardSet(CardSet cardSet) {
        cardSets.add(cardSet);
    }

    public ArrayList<CardSet> getCardSets() {
        return cardSets;
    }

    public int getCardSetsCount() {
        return cardSets.size();
    }

}
