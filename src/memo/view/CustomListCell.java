package memo.view;

import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import memo.controller.AbstractController;
import memo.model.Card;

public class CustomListCell extends ListCell<Card> {

    private int sectionNumber;
    private int cardSetNumber;
    private ArrayList<CheckBox> checkBoxes;
    private boolean isSimpleCell; 
    /* CONTOROLS FOR SIMPLE CELL */
    private final HBox simpleCellHBox;
    private final Label label;
    private final Pane pane;
    private boolean isSelected;
    private CheckBox checkBox;
    /* CONTROLS FOR FAKECARD CELL */
    private final HBox fakeCardCellHBox;
    private final Button button;

    private static AbstractController controller;
    private static CustomAccordion customAccordion;
    private Card card;
    
    public CustomListCell(int sectionNumber, int cardSetNumber, ArrayList<CheckBox> checkBoxes) {

        super();
        this.sectionNumber = sectionNumber;
        this.cardSetNumber = cardSetNumber;
        this.checkBox = new CheckBox();
        this.checkBox.setPadding(new Insets(0, 6, 0, 0));
        this.checkBoxes = checkBoxes;
        /* Simple CardCell init>>>>>>> origin/masterialization */
        simpleCellHBox = new HBox();
        label = new Label();
        pane = new Pane();
        simpleCellHBox.getChildren().addAll(label,pane, checkBox);
        HBox.setHgrow(pane, Priority.ALWAYS);
        /* Fake CardCell initialization */
        /*for(int i = 0; i < checkBoxes.size(); i++) {
            checkBoxes.get(i).selectedProperty().addListener(new ChangeListener<Boolean>(){

                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    //checkBoxes.get(CustomListCell.this.getIndex()).setSelected(newValue);
                    checkBox.setSelected(newValue);
                    updateItem(CustomListCell.this.getItem(), false);
                }
                
            });
        }*/
        //checkBox.selectedProperty().bindBidirectional(checkBoxes.get(CustomListCell.this.getIndex()).selectedProperty());

        fakeCardCellHBox = new HBox();
        button = new Button("add new Card");
        //button.setPrefHeight(23.1);
        fakeCardCellHBox.getChildren().add(button);
    }

    
    
    @Override
    protected void updateItem(Card item, boolean empty) {
        super.updateItem(item, empty);
        isSimpleCell = item instanceof FakeCard != true;
        if(isSimpleCell) {
            setText(null);
            if (item == null) {
                setGraphic(null);
            } else {
                label.setText(item.getName());
                //checkBox.setSelected(checkBoxes.get(CustomListCell.this.getIndex()).isSelected());
                setGraphic(simpleCellHBox);
            }
            CustomListCell.this.setOnMouseClicked(null);
        }
        else {
            CustomListCell.this.setOnMouseClicked(new EventHandler<MouseEvent>(){
                public void handle(MouseEvent event) {
                    controller.addCard(customAccordion.getCurrentSection(), customAccordion.getCurrentSet(), new Card("Empty"));
                    MultipleSelectionModel<Card> selectionModel = CustomListCell.this.getListView().getSelectionModel();
                        selectionModel.clearAndSelect(CustomListCell.this.getListView().getItems().size()-2);
                }
            });
            setText("+ Add new Card");
            setGraphic(null);
        }
    }
    public String getName() {
        if(this.getItem()!=null)
            return this.getItem().getName();
        return "fuck";
    }

    public static void setController(AbstractController controller) {
        CustomListCell.controller = controller;
    }

    public static void setCustomAccordion(CustomAccordion accordion) {
        CustomListCell.customAccordion = accordion;
    }
}
