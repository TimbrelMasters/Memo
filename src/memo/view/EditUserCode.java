package memo.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import memo.utils.internationalization.Internationalizator;
import memo.utils.internationalization.InternationalizedLabeledComponent;

/**
 *
 * @author Pisarik
 */
public class EditUserCode extends AbstractView{

    @FXML private BorderPane thisPane;
    @FXML private Label titleLabel;

    @FXML private Label nameLabel;
    @FXML private TextField nameField;

    @FXML private Button saveButton;
    @FXML private Button deleteButton;



    private CustomAccordion customAccordion;

    private Internationalizator internationalizator;

    public EditUserCode() {
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
        internationalizator.addObserver(new InternationalizedLabeledComponent(titleLabel, "key.userSettings"));
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
        //controller.removeSection(customAccordion.getCurrentSection());
    }

    @FXML
    private void OnSaveClick(){
       controller.changeUserName(nameField.getText());
    }

 /*
   ***
   *** Setters and Getters
   ***
    */

    public void setCustomAccordion(CustomAccordion customAccordion){
        this.customAccordion = customAccordion;
    }

    public void setUserName(String name){
        nameField.setText(name);
    }
}
