
package memo.view;

import java.util.ArrayList;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Accordion;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.util.Callback;
import memo.controller.AbstractController;
import memo.model.Card;
import memo.model.CardSet;
import memo.model.Section;
import memo.model.User;


public class CustomAccordion {

    private static final double LIST_VIEW_HEIGHT = 23.1;
    private static final double INNER_ACCORDION_PADDING = 24;
    private static final double ACCORDION_MIN_WIDTH = 50;
    private AbstractController controller;
    private Accordion accordion;
    private EventHandler<MouseEvent> onOpenTheme;
    private User user;
    private ArrayList<CheckBox> sectionCheckBoxes;
    private ArrayList<ArrayList<CheckBox>> cardSetCheckBoxes;
    private ArrayList<ArrayList<ArrayList<CheckBox>>> cardCheckBoxes;
    private int currentSection;
    private int currentSet;

    public CustomAccordion(User user, Accordion accordion, AbstractController controller, EventHandler<MouseEvent> onOpenTheme) {
        this.accordion = accordion;
        this.accordion.setMinWidth(ACCORDION_MIN_WIDTH);
        this.user = user;
        this.controller = controller;
        sectionCheckBoxes = new ArrayList<>();
        cardSetCheckBoxes = new ArrayList<>();
        cardCheckBoxes = new ArrayList<>();
        CustomListCell.setController(controller);
        CustomListCell.setCustomAccordion(CustomAccordion.this);
        this.onOpenTheme = onOpenTheme;
        showUserCards();
    }

    private CheckBox addCheckBox(TitledPane titledPane, Accordion acco) {
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
        return checkBox;
    }

    public void showUserCards() {
        this.user = user;
        this.accordion.getPanes().clear();
        addSectionButton();
        ArrayList<Section> sections = user.getSections();
        for(int i = 0; i < sections.size(); i++) {
            addSection(sections.get(i));
            addCardSetButton(i);
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
        listView.getItems().add(listView.getItems().size()-1, card);
        listView.setPrefHeight(LIST_VIEW_HEIGHT*listView.getItems().size());
    }

    public void removeCard(int i, int j, int k) {
        Accordion inner = (Accordion)accordion.getPanes().get(i).getContent();
        ListView listView = (ListView)inner.getPanes().get(j).getContent();
        listView.getItems().remove(k);
        listView.setPrefHeight(LIST_VIEW_HEIGHT*listView.getItems().size());
    }
    
    public void addCardSetButton(int i) {
        Accordion inner = (Accordion) accordion.getPanes().get(i).getContent();
        inner.expandedPaneProperty().addListener(new ChangeListener<TitledPane>() {
            @Override
            public void changed(ObservableValue<? extends TitledPane> observable, TitledPane oldValue, TitledPane newValue) {
                currentSet = inner.getPanes().indexOf(newValue);
            }
        });
        inner.setMinWidth(ACCORDION_MIN_WIDTH);
        inner.setPadding(new Insets(0, 0, 0, INNER_ACCORDION_PADDING));
        TitledPane titledPane = new TitledPane("+ Add new CardSet", null);
        titledPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                controller.addCardSet(i, new CardSet("Empty"));
                inner.getPanes().get(inner.getPanes().size()-2).setExpanded(true);
            }
        });
        inner.getPanes().add(titledPane);
    }
    
    public void addCardSet(int i, CardSet cardSet) {
        Accordion inner = (Accordion)accordion.getPanes().get(i).getContent();
        inner.setMinWidth(ACCORDION_MIN_WIDTH);
        inner.setPadding(new Insets(0, 0, 0, INNER_ACCORDION_PADDING));
        ObservableList<Card> items = FXCollections.observableArrayList(cardSet.getCardSet());
        items.add(new FakeCard());
        ListView listView = new ListView(items);
        listView.setCellFactory(new Callback<ListView<Card>, ListCell<Card>>() {
            @Override
            public ListCell<Card> call(ListView<Card> param) {
                return new CustomListCell();
            }
        });
        listView.setPrefHeight(LIST_VIEW_HEIGHT*listView.getItems().size());
        TitledPane titledPane = new TitledPane(cardSet.getName(), listView);
        CheckBox checkBox = addCheckBox(titledPane, inner);
        cardSetCheckBoxes.get(i).add(checkBox);
        cardCheckBoxes.get(i).add(new ArrayList<>());
        inner.getPanes().add(inner.getPanes().size()-1, titledPane);
    }

    public void removeCardSet(int i, int j) {
        Accordion inner = (Accordion)accordion.getPanes().get(i).getContent();
        inner.getPanes().remove(j);
        cardSetCheckBoxes.get(i).remove(j);
        cardCheckBoxes.get(i).remove(j);
    }
    
    public void addSectionButton() {
        this.accordion.expandedPaneProperty().addListener(new ChangeListener<TitledPane>(){
            @Override
            public void changed(ObservableValue<? extends TitledPane> observable, TitledPane oldValue, TitledPane newValue) {
                currentSection = accordion.getPanes().indexOf(newValue);
            }
        });
        TitledPane titledPane = new TitledPane("+ Add new Section", null);
        accordion.getPanes().add(titledPane);
        titledPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                controller.addSection(new Section("Empty"));
                addCardSetButton(currentSection);
                accordion.getPanes().get(accordion.getPanes().size()-2).setExpanded(true);
            }
        }); 
    }
    
    public void addSection(Section section) {
        Accordion inner = new Accordion();
        inner.setMinWidth(ACCORDION_MIN_WIDTH);
        TitledPane titledPane = new TitledPane(section.getName(), inner);
        CheckBox checkBox = addCheckBox(titledPane, accordion);
        sectionCheckBoxes.add(checkBox);
        cardSetCheckBoxes.add(new ArrayList<>());
        cardCheckBoxes.add(new ArrayList<>());
        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    int i = sectionCheckBoxes.indexOf(checkBox);
                    ArrayList<CheckBox> innerCheckBoxes = cardSetCheckBoxes.get(i);
                    for (int j = 0; j < innerCheckBoxes.size(); j++) {
                        cardSetCheckBoxes.get(i).get(j).setSelected(newValue);
                    }
            }
        });
        titledPane.setOnMouseClicked(onOpenTheme);
        addCheckBox(titledPane, accordion);
        accordion.getPanes().add(accordion.getPanes().size()-1, titledPane);
    }

    public void removeSection(int i) {
        accordion.getPanes().remove(i);
        sectionCheckBoxes.remove(i);
        cardSetCheckBoxes.remove(i);
        cardCheckBoxes.remove(i);
    }

    public Accordion getRoot() {
        return accordion;
    }

    public int getCurrentSection() {
        return currentSection;
    }

    public int getCurrentSet() {
        return currentSet;
    }
    
    
    
}
