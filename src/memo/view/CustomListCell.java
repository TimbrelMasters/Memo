
package memo.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
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

    private boolean isSimpleCell; 
    /* CONTOROLS FOR SIMPLE CELL */
    private final HBox simpleCellHBox;
    private final Label label;
    private final Pane pane;
    private final CheckBox checkBox;
    private boolean isSelected;
    /* CONTROLS FOR FAKECARD CELL */
    private final HBox fakeCardCellHBox;
    private final Button button;
    
    private static AbstractController controller;
    private static CustomAccordion customAccordion;
    
    public CustomListCell() {
        super();
        /* Simple CardCell initialization */ 
        simpleCellHBox = new HBox();
        label = new Label();
        pane = new Pane();
        checkBox = new CheckBox();
        checkBox.setPadding(new Insets(0, 6, 0, 0));
        simpleCellHBox.getChildren().addAll(label,pane,checkBox);
        HBox.setHgrow(pane, Priority.ALWAYS);
        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                CustomListCell.this.getItem().setIsSelected(newValue);
            }

        });
        /* Fake CardCell initialization */
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
                label.setText(item != null ? item.getName() : "<null>");
                checkBox.setSelected(item.getIsSelected());
                setGraphic(simpleCellHBox);
            }
        CustomListCell.this.setOnMouseClicked(null);
        }
        else {
            CustomListCell.this.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
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
