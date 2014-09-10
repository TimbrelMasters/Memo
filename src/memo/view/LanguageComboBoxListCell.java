
package memo.view;

import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class LanguageComboBoxListCell extends ListCell<Map.Entry<Image, Locale>> {

    public LanguageComboBoxListCell() {
        super();
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    @Override
    protected void updateItem(Entry<Image, Locale> item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setGraphic(null);
        } else {
            setGraphic(new ImageView(item.getKey()));
        }
    }
}
