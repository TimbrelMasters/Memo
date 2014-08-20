package memo.view;

import javafx.event.ActionEvent;
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

    private CustomAccordion customAccordion;

    public EditThemeCode() {
    }

    @Override
    public void manualInitialize(){
        setRootPane(thisPane);
    }

/*
   ***
   *** Controls handle
   ***
    */

    @FXML
    private void OnDeleteClick(ActionEvent event){
        controller.removeSection(customAccordion.getCurrentSection());
    }

 /*
   ***
   *** Setters and Getters
   ***
    */

    public void setCustomAccordion(CustomAccordion customAccordion){
        this.customAccordion = customAccordion;
    }
}
