
package memo.view;

import java.util.ArrayList;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.util.Callback;
import memo.model.Card;
import memo.model.CardSet;
import memo.model.Section;
import memo.model.User;


public class CustomAccordion {

    private static final double LIST_VIEW_HEIGHT = 23.1;
    private static final double INNER_ACCORDION_PADDING = 24;
    private Accordion accordion;
    private User user;


    public CustomAccordion(User user, Accordion accordion) {
        this.accordion = accordion;
        this.accordion.setMinWidth(50);
        this.user = user;
        showUserCards();
    }

    private void addComboBox(TitledPane titledPane, Accordion acco) {
        Label label = new Label();
        CheckBox checkBox = new CheckBox();
        AnchorPane title = new AnchorPane();
        AnchorPane.setLeftAnchor(label, 0.0);
        AnchorPane.setRightAnchor(checkBox, 0.0);
        title.getChildren().addAll(label, checkBox);
        titledPane.setGraphic(title);
        titledPane.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        Region arrow = (Region) titledPane.lookup(".arrow-button");
        label.textProperty().bind(titledPane.textProperty());
        title.prefWidthProperty().bind(new DoubleBinding() {
            {
                super.bind(acco.widthProperty());
            }

            @Override
            protected double computeValue() {
                double breathingSpace = 15;
                double arrowSpace = 24;
                double insets = acco.getInsets().getLeft();
                if(insets != 0)
                    insets--;
                double value = acco.getWidth() - arrowSpace - breathingSpace - insets;
                return value;
            }
        });

    }
    
    public void showUserCards() {
        this.user = user;
        this.accordion.getPanes().clear();
        ArrayList<Section> sections = user.getSections();
        for(int i = 0; i < sections.size(); i++) {
            addSection(sections.get(i));
            ArrayList<CardSet> cardSets = sections.get(i).getCardSets();
            for(int j = 0; j < cardSets.size(); j++) {
                addCardSet(i, cardSets.get(j));
            }
        }
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addCard(int i, int j, Card card) {
        Accordion inner = (Accordion)accordion.getPanes().get(i).getContent();
        ListView listView = (ListView)inner.getPanes().get(j).getContent();
        listView.getItems().add(card);
        listView.setPrefHeight(LIST_VIEW_HEIGHT*listView.getItems().size());
    }
    
    public void removeCard(int i, int j, int k) {
        Accordion inner = (Accordion)accordion.getPanes().get(i).getContent();
        ListView listView = (ListView)inner.getPanes().get(j).getContent();
        listView.getItems().remove(k);
        listView.setPrefHeight(LIST_VIEW_HEIGHT*listView.getItems().size());
    }
    
    public void addCardSet(int i, CardSet cardSet) {
        Accordion inner = (Accordion)accordion.getPanes().get(i).getContent();
        inner.setMinWidth(50);
        inner.setPadding(new Insets(0, 0, 0, INNER_ACCORDION_PADDING));
        ListView listView = new ListView(FXCollections.observableArrayList(cardSet.getCardSet()));
        listView.setCellFactory(null);
        listView.setCellFactory(new Callback<ListView<Card>, ListCell<Card>>() {
            @Override
            public ListCell<Card> call(ListView<Card> param) {
                return new CustomListCell();
            }
        });
        listView.setPrefHeight(LIST_VIEW_HEIGHT*listView.getItems().size());
        TitledPane titledPane = new TitledPane(cardSet.getName(), listView);
        addComboBox(titledPane, inner);
        inner.getPanes().add(titledPane);
    }
    
    public void removeCardSet(int i, int j) {
        Accordion inner = (Accordion)accordion.getPanes().get(i).getContent();
        inner.getPanes().remove(j);
    }
    
    public void addSection(Section section) {
        Accordion inner = new Accordion();
        inner.setMinWidth(50);
        TitledPane titledPane = new TitledPane(section.getName(), inner);
        addComboBox(titledPane, accordion);
        accordion.getPanes().add(titledPane);
    }
    
    public void removeSection(int i) {
        accordion.getPanes().remove(i);
    }
    
    public Accordion getRoot() {
        return accordion;
    }

}