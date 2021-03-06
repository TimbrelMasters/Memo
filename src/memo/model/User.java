package memo.model;

import java.util.ArrayList;

/**
 *
 * @author Pisarik
 */
public class User {
    private String name;
    private ArrayList<Section> sections;

    public User(String name){
        this.name = name;
        sections = new ArrayList<>();

        Section english = new Section("English");
        CardSet tenses = new CardSet("Tenses");
        Card presentSimple = new Card("Present Simple");
        Card pastSimple = new Card("Past Simple");
        tenses.addCard(presentSimple);
        tenses.addCard(pastSimple);
        CardSet tenses1 = new CardSet("Tenses");
        tenses1.addCard(presentSimple);
        tenses1.addCard(pastSimple);
        english.addCardSet(tenses);
        english.addCardSet(tenses1);

        sections.add(english);
    }

    public User(){
        this("Unknown");
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public ArrayList<Section> getSections() {
        return sections;
    }

    void setSections(ArrayList<Section> sections) {
        this.sections = sections;
    }

    void addSection(Section section) {
        this.sections.add(section);
    }

    @Override
    public String toString() {
        return name;
    }

}
