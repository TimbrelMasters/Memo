package memo.model;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "cardSet")
@XmlAccessorType(XmlAccessType.FIELD)
public class CardSet {
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "card")
    private ArrayList<Card> cards;

    public CardSet() {
        cards = new ArrayList<>();
    }

    public CardSet(String name) {
        this.name = name;
        cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public int getCardsCount() {
        return cards.size();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cardSet) {
        this.cards = cardSet;
    }

}
