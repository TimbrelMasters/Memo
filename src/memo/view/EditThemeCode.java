package memo.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Pisarik
 */
public class EditThemeCode extends AbstractView{

    @FXML
    private AnchorPane thisPane;

    @FXML
    private TextArea nameArea;

    public EditThemeCode() {
    }

    @Override
    public void manualInitialize(){
        setRootPane(thisPane);
    }


}
