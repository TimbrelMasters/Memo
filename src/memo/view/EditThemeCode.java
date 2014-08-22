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
    private TextField nameField;

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
    private void OnDeleteClick(){
        controller.removeSection(customAccordion.getCurrentSection());
    }

    @FXML
    private void OnSaveClick(){
        
    }

 /*
   ***
   *** Setters and Getters
   ***
    */

    public void setCustomAccordion(CustomAccordion customAccordion){
        this.customAccordion = customAccordion;
    }

    public void setThemeName(String name){
        nameField.setText(name);
    }
}
