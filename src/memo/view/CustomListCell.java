
package memo.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import memo.model.Card;

public class CustomListCell extends ListCell<Card> {

    private final HBox hBox;
    private final Label label;
    private final Pane pane;
    private final CheckBox checkBox;
    private boolean isSelected;
    public CustomListCell() {
        super();
        hBox = new HBox();
        label = new Label();
        pane = new Pane();
        checkBox = new CheckBox();
        checkBox.setPadding(new Insets(0, 6, 0, 0));
        hBox.getChildren().addAll(label,pane,checkBox);
        HBox.setHgrow(pane, Priority.ALWAYS);
        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                CustomListCell.this.getItem().setIsSelected(newValue);
            }

        });
    }

    @Override
    protected void updateItem(Card item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        if (item == null) {
            setGraphic(null);
        } else {
            label.setText(item != null ? item.getName() : "<null>");
            checkBox.setSelected(item.getIsSelected());
            setGraphic(hBox);
        }
    }

}
