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
        tenses.addCard(presentSimple);
        english.addCardSet(tenses);

        sections.add(english);
    }

    public User(){
        this("Unknown");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Section> getSections() {
        return sections;
    }

    public void setSections(ArrayList<Section> sections) {
        this.sections = sections;
    }



    @Override
    public String toString() {
        return name;
    }

}
