

package memo.utils.internationalization;

import java.awt.MenuItem;


public class InternationalizedAWTMenuItem extends InternationalizedComponent {

    private final MenuItem menuItem;
    
    public InternationalizedAWTMenuItem(MenuItem menuItem, String key) {
        super(key);
        this.menuItem = menuItem;
    }

    @Override
    public void setText(String text) {
        menuItem.setLabel(text);
    }
    
}
