package memo.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import memo.utils.internationalization.Internationalizator;
import memo.utils.internationalization.InternationalizedLabeledComponent;

/**
 *
 * @author Pisarik
 */
public class EditThemeCode extends AbstractView{

    @FXML
    private AnchorPane thisPane;

    @FXML
    private Label nameLabel;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Button deleteButton;
    
    @FXML
    private TextField nameField;
    
    

    private CustomAccordion customAccordion;

    private Internationalizator internationalizator;
    
    public EditThemeCode() {
    }
    
    
 /*
    ***
    *** Initialize methods
    ***
     */    
    
    @Override
    public void manualInitialize(){
        setRootPane(thisPane);
        internationalizator = Internationalizator.newInstance();
        internationalizeComponents();
    }
    
    public void internationalizeComponents() {
        internationalizator.addObserver(new InternationalizedLabeledComponent(nameLabel, "key.name"));
        internationalizator.addObserver(new InternationalizedLabeledComponent(saveButton, "key.save"));
        internationalizator.addObserver(new InternationalizedLabeledComponent(deleteButton, "key.delete"));
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
        controller.changeSectionName(customAccordion.getCurrentSection(), nameField.getText());

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
