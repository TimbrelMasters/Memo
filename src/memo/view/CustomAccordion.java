package memo.view;

import java.util.ArrayList;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import memo.controller.AbstractController;
import memo.model.Card;
import memo.model.CardSet;
import memo.model.Section;
import memo.model.User;
import memo.utils.internationalization.Internationalizator;
import memo.utils.internationalization.InternationalizedLabeledComponent;


public class CustomAccordion {

    private static final double INNER_ACCORDION_PADDING = 24;
    private static final double CARD_HEIGHT = 24;
    private static final double CARD_SET_HEIGHT = 26;
    private static final double CARD_SET_MAX_HEIGHT = 120;
    private static IntegerProperty SECTION_MAX_HEIGHT = new SimpleIntegerProperty(200);

    private final AbstractController controller;
    private final Accordion mainAccordion;
    private final EventHandler<MouseEvent> onOpenTheme;
    private final Internationalizator internationalizator;
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
        this.internationalizator = Internationalizator.newInstance();
        this.sectionCheckBoxes = new ArrayList<>();
        this.cardSetCheckBoxes = new ArrayList<>();
        this.cardSelections = new ArrayList<>();

        currentSection = -1;
        currentSet = -1;
        currentCard = -1;

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

        addSectionAccordionHeightListener(sectionAccordion);
        setSectionScrollPaneLook(sectionScrollPane);
        setSectionVBoxLook(sectionVBox);
        setSectionPaneLook(sectionPane);

        sectionPane.setOnMouseClicked(onOpenTheme);

        initSectionCheckBox(sectionPane);

        //add new sectionPane to the end of mainAccordion and expand it
        mainAccordion.getPanes().add(mainAccordion.getPanes().size(), sectionPane);
        mainAccordion.getPanes().get(mainAccordion.getPanes().size() - 1).setExpanded(true);
        currentSet = -1;
        currentCard = -1;

        addSelectedCardSetListener(sectionAccordion);
        addCardSetButton(sectionVBox, mainAccordion.getPanes().size() - 1);
    }

    private void addCardSetButton(VBox sectionVBox, int sectionIndex) {
        Button addCardSetButton = new Button("Add new card set"); // It might not be initialized with string.
                                                                  // Just = new Button(); Should be discussed!!

        internationalizator.addObserver(new InternationalizedLabeledComponent(addCardSetButton, "key.addNewCardSet"));

        setAddCardSetButtonLook(addCardSetButton);
        handleAddCardSetButtonClick(addCardSetButton, sectionIndex);

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

        VBox.setVgrow(sectionScrollPane, Priority.ALWAYS);

        sectionScrollPane.prefHeightProperty().bind(sectionAccordion.heightProperty());
        sectionScrollPane.setMinHeight(0);
        sectionScrollPane.maxHeightProperty().bind(SECTION_MAX_HEIGHT);

        sectionScrollPane.getStylesheets().add("memo/view/styles/ThemeAccordionStyle.css");
        sectionScrollPane.getStyleClass().add("whiteScrollPane");
    }

    private void setSectionVBoxLook(VBox sectionVBox){
        sectionVBox.setPadding(new Insets(0, 0, 0, INNER_ACCORDION_PADDING));
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

/*---------------------CardSet methods-------------------*/
    public void addCardSet(int sectionIndex, CardSet cardSet) {
        Accordion sectionAccordion = getSectionAccordion(sectionIndex);

        ObservableList<Selectable<Card>> cardSetItems = FXCollections.observableArrayList(Selectable.getSelectableList(cardSet.getCards()));
        ListView cardSetListView = new ListView(cardSetItems);
        VBox cardSetVBox = new VBox(cardSetListView);
        TitledPane cardSetPane = new TitledPane(cardSet.getName(), cardSetVBox);

        setCardSetListViewLook(cardSetListView);
        setCardSetVBoxLook(cardSetVBox);
        setCardSetPaneLook(cardSetPane);

        addSelectedCardListener(cardSetListView);

        initCardSetCheckBox(cardSetPane, sectionIndex);
        initCardListCheckBoxes(cardSetListView, cardSet, sectionIndex);

        //add new cardSetPane to the end of sectionAccordion and expand it
        sectionAccordion.getPanes().add(sectionAccordion.getPanes().size(), cardSetPane);
        sectionAccordion.getPanes().get(sectionAccordion.getPanes().size() - 1).setExpanded(true);
        currentCard = -1;

        addCardButton(cardSetVBox, sectionIndex, sectionAccordion.getPanes().size() - 1);
    }

    private void addCardButton(VBox cardSetVBox, int sectionIndex, int cardSetIndex){
        Button addCardButton = new Button("Add new card");

        internationalizator.addObserver(new InternationalizedLabeledComponent(addCardButton, "key.addNewCard"));

        setAddCardButtonLook(addCardButton);
        handleAddCardButtonClick(addCardButton, sectionIndex, cardSetIndex);

        cardSetVBox.getChildren().add(addCardButton);
    }

    public void removeCardSet(int sectionIndex, int cardSetIndex) {
        Accordion sectionAccordion = getSectionAccordion(sectionIndex);
        sectionAccordion.getPanes().remove(cardSetIndex);
        cardSetCheckBoxes.get(sectionIndex).remove(cardSetIndex);
        cardSelections.get(sectionIndex).remove(cardSetIndex);
    }

/*---------------CardSet elements look and feel----------*/

    private void setCardSetListViewLook(ListView cardSetListView) {
        cardSetListView.setMaxHeight(CARD_SET_MAX_HEIGHT);
        cardSetListView.setPrefHeight(CARD_HEIGHT * cardSetListView.getItems().size());

        cardSetListView.getStylesheets().add("memo/view/styles/ThemeAccordionStyle.css");
        cardSetListView.getStyleClass().add("cardList");
    }

    private void setCardSetVBoxLook(VBox cardSetVBox){
        cardSetVBox.setPadding(new Insets(0, 0, 0, INNER_ACCORDION_PADDING));
    }

    private void setCardSetPaneLook(TitledPane cardSetPane) {
        cardSetPane.getStylesheets().add("memo/view/styles/ThemeAccordionStyle.css");
        cardSetPane.getStyleClass().add("cardSetPane");
    }

    private void setAddCardButtonLook(Button addCardButton) {
        addCardButton.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        addCardButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        addCardButton.getStylesheets().add("memo/view/styles/ThemeAccordionStyle.css");
        addCardButton.getStyleClass().add("addCardButton");
    }

/*------------------------Card methods-----------------------*/

    public void addCard(int sectionIndex, int cardSetIndex, Card card) {
        ListView listView = getCardSetListView(sectionIndex, cardSetIndex);
        Selectable<Card> selectableCard = new Selectable<>(card);
        selectableCard.setSelected(cardSetCheckBoxes.get(sectionIndex).get(cardSetIndex).isSelected());
        cardSelections.get(sectionIndex).get(cardSetIndex).add(selectableCard);
        listView.getItems().add(listView.getItems().size(), selectableCard);
        listView.setPrefHeight(CARD_HEIGHT*listView.getItems().size());
    }

    public void removeCard(int sectionIndex, int cardSetIndex, int cardIndex) {
        ListView listView = getCardSetListView(sectionIndex, cardSetIndex);
        listView.getItems().remove(cardIndex);
        cardSelections.get(sectionIndex).get(cardSetIndex).remove(cardIndex);
        listView.setPrefHeight(CARD_HEIGHT*listView.getItems().size());
    }

/*-----------Listeners for Buttons----------------*/

    private void handleAddSectionButtonClick(Button addSectionButton) {
        addSectionButton.setOnMouseClicked((MouseEvent event) -> {
            controller.addSection(new Section("Empty"));
        });
    }

    private void handleAddCardSetButtonClick(Button addCardSetButton, int sectionIndex) {
        addCardSetButton.setOnMouseClicked((MouseEvent event) -> {
            controller.addCardSet(sectionIndex, new CardSet("Empty"));
        });
    }

    private void handleAddCardButtonClick(Button addCardButton, int sectionIndex, int cardSetIndex){
        addCardButton.setOnMouseClicked((MouseEvent event) -> {
            controller.addCard(sectionIndex, cardSetIndex, new Card("Empty"));
        });
    }

/*------------------Listeners for current section\cardSet\card--------*/

    private void addSelectedSectionListener() {
        mainAccordion.expandedPaneProperty().addListener((ObservableValue<? extends TitledPane> observable, TitledPane oldValue, TitledPane newValue) -> {
            currentSection = mainAccordion.getPanes().indexOf(newValue);
            controller.updateView(currentSection, currentSet, currentCard);
        });
    }

    private void addSelectedCardSetListener(Accordion sectionAccordion) {
        sectionAccordion.expandedPaneProperty().addListener((ObservableValue<? extends TitledPane> observable, TitledPane oldValue, TitledPane newValue) -> {
            currentSet = sectionAccordion.getPanes().indexOf(newValue);
        });
    }

    private void addSelectedCardListener(ListView cardSetListView) {
        cardSetListView.getSelectionModel().getSelectedIndices().addListener((ListChangeListener.Change c) -> {
            currentCard = (Integer) c.getList().get(0);
        });
    }

/*------------------CheckBoxes part------------------*/

    private void initSectionCheckBox(TitledPane sectionPane){
        CheckBox sectionCheckBox = addCheckBox(sectionPane, mainAccordion);

        sectionCheckBoxes.add(sectionCheckBox);
        cardSetCheckBoxes.add(new ArrayList<>());
        cardSelections.add(new ArrayList<>());

        handleSectionCheckBoxClick(sectionCheckBox);
    }

    private void initCardSetCheckBox(TitledPane cardSetPane, int sectionIndex){
        Accordion sectionAccordion = getSectionAccordion(sectionIndex);
        CheckBox cardSetCheckBox = addCheckBox(cardSetPane, sectionAccordion);

        cardSetCheckBox.setSelected(sectionCheckBoxes.get(sectionIndex).isSelected());
        cardSetCheckBoxes.get(sectionIndex).add(cardSetCheckBox);

        int cardSetIndex = cardSetCheckBoxes.get(sectionIndex).size()-1;
        handleCardSetCheckBoxClick(cardSetCheckBox, sectionIndex, cardSetIndex);
    }

    private void initCardListCheckBoxes(ListView cardSetListView, CardSet cardSet, int sectionIndex){
        cardSelections.get(sectionIndex).add(new ArrayList<>());
        int cardSetIndex = cardSelections.get(sectionIndex).size() - 1;

        cardSelections.get(sectionIndex).get(cardSetIndex).addAll(Selectable.getSelectableList(cardSet.getCards()));

        setCardSetListViewCellFactory(cardSetListView, sectionIndex, cardSetIndex);
    }

    private void setCardSetListViewCellFactory(ListView cardSetListView, int sectionIndex, int cardSetIndex) {
        cardSetListView.setCellFactory(new Callback<ListView<Selectable<Card>>, ListCell<Selectable<Card>>>() {
            @Override
            public ListCell<Selectable<Card>> call(ListView<Selectable<Card>> param) {
                ListCell<Selectable<Card>> result = new CustomListCell(sectionCheckBoxes.get(sectionIndex), cardSetCheckBoxes.get(sectionIndex), cardSetCheckBoxes.get(sectionIndex).get(cardSetIndex),
                        cardSelections.get(sectionIndex).get(cardSetIndex));

                result.getStylesheets().add("memo/view/styles/ThemeAccordionStyle.css");
                result.getStyleClass().add("cardCell");

                return result;
            }
        });
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
                if (insets != 0) {
                    insets--;
                }
                double value = acco.getWidth() - arrowSpace - breathingSpace - insets;
                return value;
            }
        });
        return checkBox;
    }

/*----------------CheckBoxes handlers------------------*/

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
                    }
                }
                forceVisibleCardListRedraw();
            }

        });
    }

    private void handleCardSetCheckBoxClick(CheckBox cardSetCheckBox, int sectionIndex, int cardSetIndex) {
        cardSetCheckBox.setOnMouseClicked((MouseEvent event) -> {
            ObservableList<Selectable<Card>> cardSetItems = getCardSetListView(sectionIndex, cardSetIndex).getItems();

            for (int cardIndex = 0; cardIndex < cardSetItems.size(); cardIndex++) {
                cardSetItems.get(cardIndex).setSelected(cardSetCheckBox.isSelected());
                cardSelections.get(sectionIndex).get(cardSetIndex).get(cardIndex).setSelected(cardSetCheckBox.isSelected());
            }
            forceVisibleCardListRedraw();

            boolean allCardSetCheckBoxesSelected = true;
            for (int cardSetInd = 0; allCardSetCheckBoxesSelected && cardSetInd < cardSetCheckBoxes.get(sectionIndex).size(); cardSetInd++) {
                allCardSetCheckBoxesSelected = allCardSetCheckBoxesSelected && cardSetCheckBoxes.get(sectionIndex).get(cardSetInd).isSelected();
            }
            sectionCheckBoxes.get(sectionIndex).setSelected(allCardSetCheckBoxesSelected);
        });
    }

/*-------------------Shit methods------------------ */

    /**
     * Bad method that updates listView checkBoxes
     */
    public void forceVisibleCardListRedraw() {
        boolean isCardListVisible = currentSection * currentSet >= 0;
        if (isCardListVisible){
            ObservableList<Selectable<Card>> cards = getCardSetListView(currentSection, currentSet).getItems();

            cards.add(new Selectable<>(new Card()));
            cards.remove(cards.size()-1);
        }

    }

    /**
     * Bad method that updates mainScrollPane
     */
    private void addMainAccordionHeightListener(){
        mainAccordion.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            Platform.runLater(() -> {
                mainAccordion.getParent().requestLayout();
            });
        });
    }

    private void addSectionAccordionHeightListener(Accordion sectionAccordion){
        sectionAccordion.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            Platform.runLater(() -> {
                sectionAccordion.getParent().requestLayout();
            });
        });
    }

/*--------------------private getters------------------*/

    private Accordion getSectionAccordion(int sectionIndex) {
        VBox sectionVBox = (VBox)mainAccordion.getPanes().get(sectionIndex).getContent();
        ScrollPane sectionScrollPane = (ScrollPane)sectionVBox.getChildren().get(0);
        Accordion sectionAccordion = (Accordion) sectionScrollPane.getContent();
        return sectionAccordion;
    }

    private ListView getCardSetListView(int sectionIndex, int cardSetIndex) {
        Accordion inner = getSectionAccordion(sectionIndex);
        VBox vBox = (VBox) inner.getPanes().get(cardSetIndex).getContent();
        return (ListView)vBox.getChildren().get(0);
    }

/*-----------------Setters and Getters-----------------*/

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

    public void changeSectionName(int sectionIndex, String newName){
        mainAccordion.getPanes().get(sectionIndex).setText(newName);
    }

}
