

package memo.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Selectable <T> {
    
    private T object;
    private boolean isSelected;

    public Selectable(T object) {
        this.object = object;
        this.isSelected = false;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }
    
    public static List<Selectable> getSelectableList(List list) {
        ArrayList<Selectable> result = new ArrayList<>();
        for(int i = 0; i < list.size(); i++) {
            result.add(new Selectable(list.get(i)));
        }
        return result;
    }
    
}
