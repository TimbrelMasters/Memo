package memo.model;

public class Card {

    private String name;
    private boolean isSelected;

    public Card() {

    }

    public Card(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }



}
