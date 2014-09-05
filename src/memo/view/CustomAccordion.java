package memo.view;

import java.util.ArrayList;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
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

    private final AbstractController controller;
    private final Accordion mainAccordion;
    private final EventHandler<MouseEvent> onOpenTheme;
    private User user;

    private final ArrayList<CheckBox> sectionCheckBoxes;
    private final ArrayList<ArrayList<CheckBox>> cardSetCheckBoxes;
    private final ArrayList<ArrayList<ArrayList<Selectable<Card>>>> cardSelections;

    private int currentSection;
    private int currentSet;
    private int currentCard;

    public CustomAccordion(User user, Accordion accordion, AbstractController controller, EventHandler<MouseEvent> onOpenTheme, Button sectionButton) {
        this.mainAccordion = accordion;
        this.user = user;
        this.controller = controller;
        this.onOpenTheme = onOpenTheme;
        this.sectionCheckBoxes = new ArrayList<>();
        this.cardSetCheckBoxes = new ArrayList<>();
        this.cardSelections = new ArrayList<>();

        CustomListCell.setController(controller);
        CustomListCell.setCustomAccordion(CustomAccordion.this);

        addSelectedSectionListener();
        addMainAccordionHeightListener();
        handleAddSectionButtonClick(sectionButton);

        showUserCards();
    }

    public void showUserCards() {
        mainAccordion.getPanes().clear();
        ArrayList<Section> sections = user.getSections();
        for(int i = 0; i < sections.size(); i++) {
            addSection(sections.get(i));
            ArrayList<CardSet> cardSets = sections.get(i).getCardSets();
            for(int j = 0; j < cardSets.size(); j++) {
                addCardSet(i, cardSets.get(j));
            }
        }
    }

/*----------------Section methods---------------------*/

    public void addSection(Section section) {
        //TitledPane -> VBox -> (ScrollPane -> Accordion) + AddButton
        Accordion sectionAccordion = new Accordion();
        ScrollPane sectionScrollPane = new ScrollPane(sectionAccordion);
        VBox sectionVBox = new VBox(sectionScrollPane);
        TitledPane sectionPane = new TitledPane(section.getName(), sectionVBox);

        setSectionVBoxLook(sectionVBox);
        setSectionScrollPaneLook(sectionScrollPane);
        setSectionPaneLook(sectionPane);

        sectionPane.setOnMouseClicked(onOpenTheme);

        initSectionCheckBox(sectionPane);

        //add new sectionPane to the end of mainAccordion and expand it.
        mainAccordion.getPanes().add(mainAccordion.getPanes().size(), sectionPane);
        mainAccordion.getPanes().get(mainAccordion.getPanes().size() - 1).setExpanded(true);

        addSelectedCardSetListener(sectionAccordion);
        addCardSetButton();
    }

    private void addCardSetButton() {
        Button addCardSetButton = new Button("Add new card set");
        VBox sectionVBox = (VBox)mainAccordion.getPanes().get(currentSection).getContent();

        setAddCardSetButtonLook(addCardSetButton);
        handleAddCardSetButtonClick(addCardSetButton, currentSection);

        sectionVBox.getChildren().add(addCardSetButton);
    }

    public void removeSection(int i) {
        mainAccordion.getPanes().remove(i);
        sectionCheckBoxes.remove(i);
        cardSetCheckBoxes.remove(i);
        cardSelections.remove(i);
    }

/*---------------Section elements look and feel----------*/

    private void setSectionScrollPaneLook(ScrollPane sectionScrollPane) {
        Accordion sectionAccordion = (Accordion) sectionScrollPane.getContent();
        sectionScrollPane.prefHeightProperty().bind(sectionAccordion.heightProperty());
        sectionScrollPane.setMinHeight(CARD_SET_HEIGHT);
        sectionScrollPane.setMaxHeight(SECTION_MAX_HEIGHT);

        sectionScrollPane.setFitToWidth(true);
        sectionScrollPane.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
    }

    private void setSectionVBoxLook(VBox sectionVBox){
        sectionVBox.setPadding(new Insets(0, 0, 0, INNER_ACCORDION_PADDING));

        sectionVBox.setStyle("-fx-background-color: #FFFFFF; -fx-border: none");
    }

    private void setSectionPaneLook(TitledPane sectionPane) {
        sectionPane.getStylesheets().add("memo/view/styles/ThemeAccordionStyle.css");
        sectionPane.getStyleClass().add("themePane");
    }

    private void setAddCardSetButtonLook(Button addCardSetButton) {
        addCardSetButton.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        addCardSetButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        addCardSetButton.getStylesheets().add("memo/view/styles/ThemeAccordionStyle.css");
        addCardSetButton.getStyleClass().add("addCardSetButton");
    }

/*-----------Listeners for Main Accordion----------------*/

    private void addSelectedSectionListener() {
        mainAccordion.expandedPaneProperty().addListener(new ChangeListener<TitledPane>() {
            @Override
            public void changed(ObservableValue<? extends TitledPane> observable, TitledPane oldValue, TitledPane newValue) {
                currentSection = mainAccordion.getPanes().indexOf(newValue);
                if (currentSection != mainAccordion.getPanes().size() - 1) {
                    controller.updateView(currentSection, currentSet, currentCard);
                }
            }
        });
    }

    /**
     * Bad method that updates outerScrollPane
     */
    private void addMainAccordionHeightListener(){
        mainAccordion.heightProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Platform.runLater(() -> {
                    mainAccordion.getParent().requestLayout();
                });
            }
        });
    }

/*-----------Listeners for Buttons----------------*/

    private void handleAddSectionButtonClick(Button addSectionButton) {
        addSectionButton.setOnMouseClicked((MouseEvent event) -> {
            controller.addSection(new Section("Empty"));
        });
    }

    private void handleAddCardSetButtonClick(Button addCardSetButton, int sectionNumber) {
        addCardSetButton.setOnMouseClicked((MouseEvent event) -> {
            controller.addCardSet(sectionNumber, new CardSet("Empty"));
        });
    }

/*-----------CheckBoxes part----------------------*/

    private void initSectionCheckBox(TitledPane sectionPane){
        CheckBox sectionCheckBox = addCheckBox(sectionPane, mainAccordion);
        sectionCheckBoxes.add(sectionCheckBox);
        cardSetCheckBoxes.add(new ArrayList<>());
        cardSelections.add(new ArrayList<>());
        handleSectionCheckBoxClick(sectionCheckBox);
    }

/*----------Not refactored part--------------------*/

    public void addCard(int i, int j, Card card) {
        ListView listView = getCardSetListView(i, j);
        Selectable<Card> selectableCard = new Selectable<>(card);
        selectableCard.setSelected(cardSetCheckBoxes.get(i).get(j).isSelected());
        cardSelections.get(i).get(j).add(selectableCard);
        listView.getItems().add(listView.getItems().size()-1, selectableCard);
        listView.setPrefHeight(CARD_HEIGHT*listView.getItems().size());
    }

    public void removeCard(int i, int j, int k) {
        ListView listView = getCardSetListView(i, j);
        listView.getItems().remove(k);
        cardSelections.get(i).get(j).remove(k);
        listView.setPrefHeight(CARD_HEIGHT*listView.getItems().size());
    }

    public void addCardSet(int i, CardSet cardSet) {
        Accordion sectionAccordion = getSectionAccordion(i);
        //sectionAccordion.setPadding(new Insets(0, 0, 0, INNER_ACCORDION_PADDING));
        ObservableList<Selectable<Card>> cardSetItems = createCardSetItems(cardSet);
        ListView cardSetListView = new ListView(cardSetItems);
        setCardSetListViewProperties(cardSetListView);
        addCurrentCardListener(cardSetListView);
        TitledPane cardSetPane = new TitledPane(cardSet.getName(), cardSetListView);
        setCardSetPaneStyle(cardSetPane);

        CheckBox cardSetCheckBox = addCheckBox(cardSetPane, sectionAccordion);
        cardSetCheckBox.setSelected(sectionCheckBoxes.get(i).isSelected());
        cardSetCheckBoxes.get(i).add(cardSetCheckBox);
        int j = cardSetCheckBoxes.get(i).indexOf(cardSetCheckBox);
        cardSelections.get(i).add(new ArrayList<>());
        cardSelections.get(i).get(j).addAll(Selectable.getSelectableList(cardSet.getCards()));
        handleCardSetCheckBoxClick(cardSetCheckBox, cardSetItems, i, j);

        setCardSetListViewCellFactory(cardSetListView, cardSetCheckBox, i, j);
        sectionAccordion.getPanes().add(sectionAccordion.getPanes().size(), cardSetPane);
        sectionAccordion.getPanes().get(sectionAccordion.getPanes().size() - 1).setExpanded(true);
    }

    public void removeCardSet(int i, int j) {
        Accordion sectionAccordion = getSectionAccordion(i);
        sectionAccordion.getPanes().remove(j);
        cardSetCheckBoxes.get(i).remove(j);
        cardSelections.get(i).remove(j);
    }

    public void changeSectionName(int sectionIndex, String newName){
        mainAccordion.getPanes().get(sectionIndex).setText(newName);
    }

    /************************ Private Help Methods **************************/

    /* CardSet connected methods */

    private ListView getCardSetListView(int sectionNumner, int cardSetNumber) {
        Accordion inner = getSectionAccordion(sectionNumner);
        ListView listView = (ListView) inner.getPanes().get(cardSetNumber).getContent();
        return listView;
    }

    private void addSelectedCardSetListener(Accordion sectionAccordion) {
        sectionAccordion.expandedPaneProperty().addListener(new ChangeListener<TitledPane>() {
            @Override
            public void changed(ObservableValue<? extends TitledPane> observable, TitledPane oldValue, TitledPane newValue) {
                currentSet = sectionAccordion.getPanes().indexOf(newValue);
            }
        });
    }

    private ObservableList<Selectable<Card>> createCardSetItems(CardSet cardSet) {
        ObservableList<Selectable<Card>> items = FXCollections.observableArrayList(Selectable.getSelectableList(cardSet.getCards()));
        items.add(new Selectable<>(new FakeCard()));
        return items;
    }

    private ListView setCardSetListViewProperties(ListView cardSetListView) {
        cardSetListView.setMaxHeight(CARD_SET_MAX_HEIGHT);
        cardSetListView.setPrefHeight(CARD_HEIGHT * cardSetListView.getItems().size());
        return cardSetListView;
    }

    private void setCardSetPaneStyle(TitledPane cardSetPane) {
        cardSetPane.getStylesheets().add("memo/view/styles/ThemeAccordionStyle.css");
        cardSetPane.getStyleClass().add("cardSetPane");
    }

    private void handleCardSetCheckBoxClick(CheckBox cardSetCheckBox, ObservableList<Selectable<Card>> cardSetItems, int sectionNumber, int cardSetNumber) {
        cardSetCheckBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                for (int k = 0; k < cardSetItems.size() - 1; k++) {
                    cardSetItems.get(k).setSelected(cardSetCheckBox.isSelected());
                    cardSelections.get(sectionNumber).get(cardSetNumber).get(k).setSelected(cardSetCheckBox.isSelected());
                }
                forceListViewRedraw(cardSetItems);
                boolean allCardSetCheckBoxesFired = true;
                for (int k = 0; k < cardSetCheckBoxes.get(sectionNumber).size(); k++) {
                    if (cardSetCheckBoxes.get(sectionNumber).get(k).isSelected() == false) {
                        allCardSetCheckBoxesFired = false;
                        sectionCheckBoxes.get(sectionNumber).setSelected(false);
                        break;
                    }
                }
                if (allCardSetCheckBoxesFired == true) {
                    sectionCheckBoxes.get(sectionNumber).setSelected(true);
                }
            }
        });
    }

    private void setCardSetListViewCellFactory(ListView cardSetListView, CheckBox cardSetCheckBox, int sectionNumber, int cardSetNumber) {
        cardSetListView.setCellFactory(new Callback<ListView<Selectable<Card>>, ListCell<Selectable<Card>>>() {
            @Override
            public ListCell<Selectable<Card>> call(ListView<Selectable<Card>> param) {
                return new CustomListCell(sectionCheckBoxes.get(sectionNumber), cardSetCheckBoxes.get(sectionNumber), cardSetCheckBox, cardSelections.get(sectionNumber).get(cardSetNumber));
            }
        });
    }

    private void addCurrentCardListener(ListView cardSetListView) {
        cardSetListView.getSelectionModel().getSelectedIndices().addListener(new ListChangeListener() {
            @Override
            public void onChanged(ListChangeListener.Change c) {
                currentCard = (Integer) c.getList().get(0);
            }
        });
    }

    /* Section connected methods */

    private Accordion getSectionAccordion(int sectionNumber) {
        VBox sectionVBox = (VBox)mainAccordion.getPanes().get(sectionNumber).getContent();
        ScrollPane sectionScrollPane = (ScrollPane)sectionVBox.getChildren().get(0);
        Accordion sectionAccordion = (Accordion) sectionScrollPane.getContent();
        return sectionAccordion;
    }

    private void handleSectionCheckBoxClick(CheckBox sectionCheckBox) {
        sectionCheckBox.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                int i = sectionCheckBoxes.indexOf(sectionCheckBox);
                ArrayList<CheckBox> innerCheckBoxes = cardSetCheckBoxes.get(i);
                for (int j = 0; j < innerCheckBoxes.size(); j++) {
                    cardSetCheckBoxes.get(i).get(j).setSelected(sectionCheckBox.isSelected());
                    ArrayList<Selectable<Card>> secondLevel = cardSelections.get(i).get(j);
                    ListView cardSetListView = getCardSetListView(i, j);
                    for (int k = 0; k < secondLevel.size(); k++) {
                        Selectable<Card> selectableCard = (Selectable<Card>) cardSetListView.getItems().get(k);
                        selectableCard.setSelected(sectionCheckBox.isSelected());
                        secondLevel.get(k).setSelected(sectionCheckBox.isSelected());
                        forceListViewRedraw(cardSetListView.getItems());
                    }
                }
            }

        });
    }

    private void setAddSectionButtonStyle(TitledPane addSectionButton) {
        addSectionButton.getStylesheets().add("memo/view/styles/ThemeAccordionStyle.css");
        addSectionButton.getStyleClass().add("addThemeButton");
    }

    /* Other helpers */

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
                if (insets != 0) {
                    insets--;
                }
                double value = acco.getWidth() - arrowSpace - breathingSpace - insets;
                return value;
            }
        });
        return checkBox;
    }

    public void forceListViewRedraw(ObservableList<Selectable<Card>> cards) { // Fucking shitcode, but works fine. StackOverflow has similar solution :(
        cards.add(new Selectable<>(new Card()));
        cards.remove(cards.size() - 1);
    }

    /*-------Setters and Getters----------*/
    public int getCurrentSection() {
        return currentSection;
    }

    public int getCurrentSet() {
        return currentSet;
    }

    public int getCurrentCard() {
        return currentCard;
    }

    public User getUser() {
        return user;
    }

    public Accordion getRoot() {
        return mainAccordion;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
