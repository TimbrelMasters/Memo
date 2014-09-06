

package memo.utils.internationalization;

import javafx.scene.control.Labeled;


public class InternationalizedLabeledObject extends InternationalizedComponent {

    private final Labeled labeled;
    
    public InternationalizedLabeledObject(Labeled labeled, String key) {
        super(key);
        this.labeled = labeled;
    }

    @Override
    public void setText(String text) {
        labeled.setText(text);
    }
    
}
