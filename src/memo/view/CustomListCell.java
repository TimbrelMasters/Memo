package memo.view;

import java.util.ArrayList;
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

public class CustomListCell extends ListCell<Selectable<Card>> {

    private ArrayList<Selectable<Card>> cardSelections;
    private boolean isSimpleCell; 
    /* CONTOROLS FOR SIMPLE CELL */
    private final HBox simpleCellHBox;
    private final Label label;
    private final Pane pane;
    private boolean isSelected;
    private CheckBox checkBox;
    /* CONTROLS FOR FAKECARD CELL */
    private final HBox fakeCardCellHBox;

    private static AbstractController controller;
    private static CustomAccordion customAccordion;
    private Card card;
    
    public CustomListCell(ArrayList<Selectable<Card>> cardSelections) {

        super();
        this.checkBox = new CheckBox();
        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                CustomListCell.this.getItem().setSelected(newValue);
            }
        });
        this.checkBox.setPadding(new Insets(0, 6, 0, 0));
        /* Simple CardCell initialization */
        simpleCellHBox = new HBox();
        label = new Label();
        pane = new Pane();
        simpleCellHBox.getChildren().addAll(label,pane, checkBox);
        HBox.setHgrow(pane, Priority.ALWAYS);
        /* Fake CardCell initialization */
        fakeCardCellHBox = new HBox();
    }

    
    
    @Override
    protected void updateItem(Selectable<Card> item, boolean empty) {
        super.updateItem(item, empty);
        if(item != null)
            isSimpleCell = item.getObject() instanceof FakeCard != true;
        if(isSimpleCell) {
            setText(null);
            if (item == null) {
                setGraphic(null);
            } else {
                label.setText(item.getObject().getName());
                System.out.println(item);
                checkBox.setSelected(item.isSelected());
                setGraphic(simpleCellHBox);
            }
            CustomListCell.this.setOnMouseClicked(null);
        }
        else {
            CustomListCell.this.setOnMouseClicked(new EventHandler<MouseEvent>(){
                public void handle(MouseEvent event) {
                    controller.addCard(customAccordion.getCurrentSection(), customAccordion.getCurrentSet(), new Card("Empty"));
                    MultipleSelectionModel<Selectable<Card>> selectionModel = CustomListCell.this.getListView().getSelectionModel();
                        selectionModel.clearAndSelect(CustomListCell.this.getListView().getItems().size()-2);
                }
            });
            setText("+ Add new Card");
            setGraphic(null);
        }
    }
    public String getName() {
        if(this.getItem()!=null)
            return this.getItem().getObject().getName();
        return "fuck";
    }

    public static void setController(AbstractController controller) {
        CustomListCell.controller = controller;
    }

    public static void setCustomAccordion(CustomAccordion accordion) {
        CustomListCell.customAccordion = accordion;
    }
}
