package memo.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Pisarik
 */
public class EditThemeCode extends AbstractView{

    @FXML
    private AnchorPane thisPane;

    @FXML
    private TextField nameArea;

    public EditThemeCode() {
    }

    @Override
    public void manualInitialize(){
        setRootPane(thisPane);
    }


}
