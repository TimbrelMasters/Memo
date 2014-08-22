

package memo.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;


public class Selectable <T> {
    
    private T object;
    public BooleanProperty selectedProperty;

    public Selectable(T object) {
        this.object = object;
        this.selectedProperty = new SimpleBooleanProperty(false);
    }

    public void setSelected(boolean isSelected) {
        this.selectedProperty.set(isSelected);
    }

    public boolean isSelected() {
        return selectedProperty.get();
    }
    
    public static <T> ArrayList<Selectable<T>> getSelectableList(ArrayList<T> list) {
        ArrayList<Selectable<T>> result = new ArrayList<>();
        for(int i = 0; i < list.size(); i++) {
            result.add(new Selectable<>(list.get(i)));
        }
        return result;
    }

    public T getObject() {
        return object;
    }
    
}
