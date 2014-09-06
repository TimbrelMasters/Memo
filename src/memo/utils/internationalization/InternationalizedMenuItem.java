

package memo.utils.internationalization;

import javafx.scene.control.MenuItem;

public class InternationalizedMenuItem extends InternationalizedComponent {

    private final MenuItem menuItem;
    
    public InternationalizedMenuItem(MenuItem menuItem, String key) {
        super(key);
        this.menuItem = menuItem;
    }

    @Override
    public void setText(String text) {
        menuItem.setText(text);
    }
    
}
