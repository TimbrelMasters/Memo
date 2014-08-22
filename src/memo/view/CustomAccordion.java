
package memo.view;

import java.util.ArrayList;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Accordion;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import memo.controller.AbstractController;
import memo.model.Card;
import memo.model.CardSet;
import memo.model.Section;
import memo.model.User;


public class CustomAccordion {

    private static final double INNER_ACCORDION_PADDING = 24;
    private static final double CARD_HEIGHT = 24;
    private static final double CARD_SET_HEIGHT = 26;
    private static final double CARD_SET_MAX_HEIGHT = 120;
    private static final double SECTION_MAX_HEIGHT = 180;

    private AbstractController controller;
    private Accordion accordion;
    private EventHandler<MouseEvent> onOpenTheme;
    private User user;
    private ArrayList<CheckBox> sectionCheckBoxes;
    private ArrayList<ArrayList<CheckBox>> cardSetCheckBoxes;
    private ArrayList<ArrayList<ArrayList<CheckBox>>> cardCheckBoxes;
    private int currentSection;
    private int currentSet;
    private int currentCard;

    public CustomAccordion(User user, Accordion accordion, AbstractController controller, EventHandler<MouseEvent> onOpenTheme) {
        this.accordion = accordion;
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
        ScrollPane innerSroll = (ScrollPane) accordion.getPanes().get(i).getContent();
        Accordion inner = (Accordion) innerSroll.getContent();
        ListView listView = (ListView)inner.getPanes().get(j).getContent();
        listView.getItems().add(listView.getItems().size()-1, card);
        listView.setPrefHeight(CARD_HEIGHT*listView.getItems().size());
    }

    public void removeCard(int i, int j, int k) {
        ScrollPane innerSroll = (ScrollPane) accordion.getPanes().get(i).getContent();
        Accordion inner = (Accordion) innerSroll.getContent();
        ListView listView = (ListView)inner.getPanes().get(j).getContent();
        listView.getItems().remove(k);
        listView.setPrefHeight(CARD_HEIGHT*listView.getItems().size());
    }

    public void addCardSetButton(int i) {
        ScrollPane innerSroll = (ScrollPane) accordion.getPanes().get(i).getContent();
        Accordion inner = (Accordion) innerSroll.getContent();
        inner.expandedPaneProperty().addListener(new ChangeListener<TitledPane>() {
            @Override
            public void changed(ObservableValue<? extends TitledPane> observable, TitledPane oldValue, TitledPane newValue) {
                currentSet = inner.getPanes().indexOf(newValue);
            }
        });
        inner.setPadding(new Insets(0, 0, 0, INNER_ACCORDION_PADDING));

        //Customized titledPane
        TitledPane cardSetButton = new TitledPane("+ Add new CardSet", null);
        cardSetButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                controller.addCardSet(i, new CardSet("Empty"));
                inner.getPanes().get(inner.getPanes().size()-2).setExpanded(true);
            }
        });
        inner.getPanes().add(cardSetButton);
    }

    public void addCardSet(int i, CardSet cardSet) {
        ScrollPane innerSroll = (ScrollPane) accordion.getPanes().get(i).getContent();
        Accordion inner = (Accordion) innerSroll.getContent();
        inner.setPadding(new Insets(0, 0, 0, INNER_ACCORDION_PADDING));
        ObservableList<Card> items = FXCollections.observableArrayList(cardSet.getCards());
        items.add(new FakeCard());
        ListView listView = new ListView(items);
        listView.setCellFactory(new Callback<ListView<Card>, ListCell<Card>>() {
            @Override
            public ListCell<Card> call(ListView<Card> param) {
                return new CustomListCell();
            }
        });
        listView.getSelectionModel().getSelectedIndices().addListener(new ListChangeListener() {
            @Override
            public void onChanged(ListChangeListener.Change c) {
                currentCard = (Integer)c.getList().get(0);
                System.out.println(currentCard);
            }
        });

        listView.setMaxHeight(CARD_SET_MAX_HEIGHT);
        listView.setPrefHeight(CARD_HEIGHT*listView.getItems().size());

        TitledPane titledPane = new TitledPane(cardSet.getName(), listView);
        CheckBox checkBox = addCheckBox(titledPane, inner);
        cardSetCheckBoxes.get(i).add(checkBox);
        cardCheckBoxes.get(i).add(new ArrayList<>());
        inner.getPanes().add(inner.getPanes().size()-1, titledPane);
    }

    public void removeCardSet(int i, int j) {
        ScrollPane innerSroll = (ScrollPane)accordion.getPanes().get(i).getContent();
        Accordion inner = (Accordion)innerSroll.getContent();
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

        //titled pane customized by css
        TitledPane addSectionButton = new TitledPane("Add new Section", null);
        addSectionButton.getStylesheets().add("memo/view/styles/ThemeAccordionStyle.css");
        addSectionButton.getStyleClass().add("addThemeButton");

        accordion.getPanes().add(addSectionButton);
        addSectionButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
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
        ScrollPane innerScroll = new ScrollPane(inner);
        innerScroll.setFitToWidth(true);
        innerScroll.setMinHeight(CARD_SET_HEIGHT);
        innerScroll.prefHeightProperty().bind(inner.heightProperty());
        innerScroll.setMaxHeight(SECTION_MAX_HEIGHT);
        TitledPane titledPane = new TitledPane(section.getName(), innerScroll);
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

    public int getCurrentCard() {
        return currentCard;
    }

}
