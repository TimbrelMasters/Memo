package memo.view;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Pisarik
 */
public class MainViewCode extends AbstractView{

    @FXML
    AnchorPane thisPane;

    public MainViewCode(){
    }

    @Override
    public void manualInitialize(){
        setRootPane(thisPane);
    }
}
