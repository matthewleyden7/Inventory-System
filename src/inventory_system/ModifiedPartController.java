
package inventory_system;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javax.xml.bind.ValidationException;

/**
 * FXML Controller class
 *
 * @author matth
 */

public class ModifiedPartController implements Initializable {

    // setting up all buttons and textfields
    @FXML private Button cancelBtn;
    @FXML private TextField partIDField;
    @FXML private TextField nameField;
    @FXML private TextField inventoryField;
    @FXML private TextField priceField;
    @FXML private TextField maxField;
    @FXML private TextField minField;
    @FXML private TextField companyField;
    @FXML private RadioButton outsourcedRadioBtn;
    @FXML private RadioButton inHouseRadioBtn;
    @FXML private ToggleGroup radioBtnToggleGroup;
    @FXML private Button saveBtn;
    @FXML private boolean error = false;
    @FXML private boolean inhouse;
    @FXML private Label choiceBetweenCompanyOrID;

    static Part selectedPart;
    static InHouse inHousePart;
    static OutSourced outSourcedPart;
    
    // initializing data from selected part from the main screen
    public void InitData(Part selection){
        selectedPart = selection;
        nameField.setText(selectedPart.getName());
        inventoryField.setText(Integer.toString(selectedPart.getInv()));
        priceField.setText(Double.toString(selectedPart.getPrice()));
        maxField.setText(Integer.toString(selectedPart.getMax()));
        minField.setText(Integer.toString(selectedPart.getMin()));
        partIDField.setText(Integer.toString(selectedPart.getID()));
        if(selectedPart instanceof InHouse){
            inHousePart = (InHouse)selection; 
            companyField.setText(Integer.toString(inHousePart.getMachineID()));
            this.radioBtnToggleGroup.selectToggle(inHouseRadioBtn);
            this.choiceBetweenCompanyOrID.setText("machine id");
            this.inhouse = true;}
        if(selectedPart instanceof OutSourced){
            outSourcedPart = (OutSourced)selection; 
            companyField.setText(outSourcedPart.getCompany());
            this.radioBtnToggleGroup.selectToggle(outsourcedRadioBtn);
            this.choiceBetweenCompanyOrID.setText("Company name");
            this.inhouse = false;}}
    
    // select radio button to change options
    public void pushRadioBtn() {
        if(this.radioBtnToggleGroup.getSelectedToggle().equals(outsourcedRadioBtn)) {
            this.choiceBetweenCompanyOrID.setText("company name");
            this.inhouse = false;}
        if(this.radioBtnToggleGroup.getSelectedToggle().equals(inHouseRadioBtn)) {
            this.choiceBetweenCompanyOrID.setText("machine id");
            this.inhouse = true;
    }}
    
    // displays an error message when user enters an invalid number or fails to
    // enter anything in a required field.
    public void displayErrorMessage(String errorMessage) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Add Part Error");
    alert.setHeaderText("invalid part");
    alert.setContentText(errorMessage);
    alert.showAndWait();

    }
    
    // cancel button pressed, return to main screen without saving changes
    public void pushCancel(ActionEvent event) throws IOException {
        Parent mainScreenParent = FXMLLoader.load(getClass().getResource("main_page.fxml"));
        Scene mainScreenScene = new Scene(mainScreenParent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainScreenScene);
        window.show();}
    
    // handles saving part details
    public void pushSave(ActionEvent e) throws IOException {
        ArrayList<String> errorList = new ArrayList<String>();
        
        // error checking (put errors into a string array called errorList and 
        // choose the first error that needs to be corrected and display)
       
        // parse the name
        String name = this.nameField.getText();
        if (name.equals("")) {errorList.add("name field required"); error = true;}
        
        // check whether user entered and inventory number
        String inv = this.inventoryField.getText();
        try {Integer.parseInt(inv);}catch(NumberFormatException ex){errorList.add("Invalid inventory number"); error = true;}
        
        // check whether the user entered a price
        String price = this.priceField.getText();
        try {Double.parseDouble(price);}catch(NumberFormatException ex){errorList.add("Invalid price"); error = true;}
        
        // check whether the user entered a number into the minimum field
        String min = this.minField.getText();
        try {Integer.parseInt(min);}catch(NumberFormatException ex){errorList.add("you must enter a minimum inventory"); error = true;}
        
        // check whether the user entered a number into the maximum field
        String max = this.maxField.getText();
        try {Integer.parseInt(max);}catch(NumberFormatException ex){errorList.add("you must enter a maximum inventory"); error = true;}
        
        // check whether the user either didn't press either radio button or 
        // did not enter a machine id (for in house) or company name (for outsourced)
        String company = companyField.getText();
        if(companyField.isDisabled()){errorList.add("Please choose whether this is an in house or outsourced part"); error = true;}
        if((company.equals("")) && (inhouse == true)){errorList.add("Please enter a machine id"); error = true;}
        if(inhouse == true){try {Integer.parseInt(company);}catch(NumberFormatException ex){errorList.add("Please enter a valid in house ID number"); error = true;}}
        if((company.equals("")) && (inhouse == false)){errorList.add("Please enter a company name"); error = true;}
      
        
        // if boolean "error" is true, this means there is a problem that needs to be fixed.
        // We use the displayErrorMessage method to display the first error in the
        // errorList.
        if (error == true) {
            displayErrorMessage(errorList.get(0));
            System.out.println("Part information needs corrections.");
            // set boolean error back to false
            error = false;
        } else {
     
            // saving part details. Allows for changing of company or machine ID 
            // if necessary. Allows for changing InHouse part to OutSourced part
            // or vice versa
            if(inhouse){
                InHouse modifiedPart = new InHouse();
                modifiedPart.setName(name);
                modifiedPart.setInv(Integer.parseInt(inv));
                modifiedPart.setPrice(Double.parseDouble(price));
                modifiedPart.setMin(Integer.parseInt(min));
                modifiedPart.setMax(Integer.parseInt(max));
                modifiedPart.setID(selectedPart.getID());
                modifiedPart.setMachineID(Integer.parseInt(company));
 
                // one last check for errors within a range using the checkIfValid method
                // of the Part class.
                try {
                    modifiedPart.checkIfValid();
                if (modifiedPart.checkIfValid() == true) {
                   System.out.println("part updated");
                   
                   // replace the selectedPart with the modifiedPart with same
                   // information (unless information changed by the user). uses
                   // the modifyPart method of the inventory class
                   Inventory.modifyPart(selectedPart, modifiedPart);}

                // change back to main screen
                Parent main = FXMLLoader.load(getClass().getResource("main_page.fxml"));
                Scene scene = new Scene(main);
                Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();

                // if checkIfValid method of the part class discovers an error,
                // display the error message.
                } catch (ValidationException except) {displayErrorMessage(except.getMessage().toString());}
                     
            } else {
                
                OutSourced modifiedPart = new OutSourced();
                modifiedPart.setName(name);
                modifiedPart.setInv(Integer.parseInt(inv));
                modifiedPart.setPrice(Double.parseDouble(price));
                modifiedPart.setMin(Integer.parseInt(min));
                modifiedPart.setMax(Integer.parseInt(max));
                modifiedPart.setID(selectedPart.getID());
                modifiedPart.setCompany(company);
      
                // one last check for errors within a range using the checkIfValid method
                // of the Part class.
                try {
                    modifiedPart.checkIfValid();
                if (modifiedPart.checkIfValid() == true) {
                   
                    System.out.println("part updated");
                   
                   // replace the selectedPart with the modifiedPart with same
                   // information (unless information changed by the user). uses
                   // the modifyPart method of the inventory class
                   Inventory.modifyPart(selectedPart, modifiedPart);}

                // change back to main screen
                Parent main = FXMLLoader.load(getClass().getResource("main_page.fxml"));
                Scene scene = new Scene(main);
                Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();

                // if checkIfValid method of the part class discovers an error,
                // display the error message.
                } catch (ValidationException except) {displayErrorMessage(except.getMessage().toString());}
           }
        }
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
 
        radioBtnToggleGroup = new ToggleGroup();
        this.outsourcedRadioBtn.setToggleGroup(radioBtnToggleGroup);
        this.inHouseRadioBtn.setToggleGroup(radioBtnToggleGroup);
        this.partIDField.setDisable(true);
    }    
    
}
