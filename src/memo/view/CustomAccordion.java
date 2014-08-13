
package memo.view;

import java.util.ArrayList;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
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

    private static final double LIST_VIEW_HEIGHT = 45;
    private static final double INNER_ACCORDION_PADDING = 24;
    private Accordion accordion;
    private User storage;



    public CustomAccordion(User storage) {
        this.storage = storage;
        accordion = new Accordion();
        accordion.setMinWidth(50);
        ArrayList<Section> themes = storage.getSections();
        for(int i = 0; i < themes.size(); i++) {
            Accordion inner = addFirstLevelLabel(themes.get(i).getName());
            inner.setMinWidth(50);
            inner.setPadding(new Insets(0, 0, 0, INNER_ACCORDION_PADDING));
            ArrayList<CardSet> decks = themes.get(i).getCardSets();
            for(int j = 0; j < decks.size(); j++) {
                ListView listView = addSecondLevelLabel(decks.get(j).getName(), inner);
                listView.setItems(FXCollections.observableArrayList(decks.get(j).getCardSet()));
                listView.setCellFactory(new Callback<ListView<Card>, ListCell<Card>>() {
                    @Override
                    public ListCell<Card> call(ListView<Card> param) {
                        return new CustomListCell();
                    }
                });
                listView.setPrefHeight(LIST_VIEW_HEIGHT*decks.get(j).getCardSet().size());
            }
        }
    }

    public Accordion addFirstLevelLabel(String name) {
        Accordion inner = new Accordion();
        TitledPane titledPane = new TitledPane(name, inner);
        addComboBox(titledPane, accordion);
        accordion.getPanes().add(titledPane);
        return inner;
    }

    public ListView addSecondLevelLabel(String name, Accordion inner) {
        ListView listView = new ListView();
        TitledPane titledPane = new TitledPane(name, listView);
        addComboBox(titledPane, inner);
        inner.getPanes().add(titledPane);
        return listView;
    }

    public void addThirdLevelLabel(Card card, ListView listView) {
        listView.getItems().add(card);
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

    public void addCard() {

        //accordion.getPanes()

    }

    public Accordion getRoot() {
        return accordion;
    }

}
