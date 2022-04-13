/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class AddPartController implements Initializable {
    
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
    @FXML private boolean inhouse;
    @FXML private boolean error = false;
    @FXML private Label choiceBetweenCompanyOrID;
   
    // handles radio button selection  
    public void pushRadioBtn() {
    if(this.radioBtnToggleGroup.getSelectedToggle().equals(outsourcedRadioBtn)) {
        this.companyField.setDisable(false);
        this.choiceBetweenCompanyOrID.setText("company name");
        this.inhouse = false;}
    if(this.radioBtnToggleGroup.getSelectedToggle().equals(inHouseRadioBtn)) {
        this.companyField.setDisable(false);
        this.choiceBetweenCompanyOrID.setText("machine id");
        this.inhouse = true;
    }}
    
    // cancels adding a new part and returns to the main screen without making
    // any changes
    public void pushCancel(ActionEvent event) throws IOException {
        Parent main = FXMLLoader.load(getClass().getResource("main_page.fxml"));
        Scene scene = new Scene(main);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();}
    
    // displays an error message when user enters an invalid number or fails to
    // enter anything in a required field.
    public void displayErrorMessage(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add Part Error");
        alert.setHeaderText("invalid part");
        alert.setContentText(errorMessage);
        alert.showAndWait();
        
    }
    
    // Saves a new part and checks for any errors first.
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
        
            // creating new InHouse part
            if(inhouse){
                InHouse newPart = new InHouse();
                newPart.setName(name);
                newPart.setInv(Integer.parseInt(inv));
                newPart.setPrice(Double.parseDouble(price));
                newPart.setMin(Integer.parseInt(min));
                newPart.setMax(Integer.parseInt(max));
                newPart.setID(Inventory.getPartsSize());
                newPart.setMachineID(Integer.parseInt(company));

                // one last check for errors within a range using the checkIfValid method
                // of the Part class.
                try {
                    newPart.checkIfValid();
                if (newPart.checkIfValid() == true) {
                    
                    // add the new inHouse part
                    Inventory.addPart(newPart);
                }

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
                    
                // create a new outsourced part
                OutSourced newPart = new OutSourced();
                newPart.setName(name);
                newPart.setInv(Integer.parseInt(inv));
                newPart.setPrice(Double.parseDouble(price));
                newPart.setMin(Integer.parseInt(min));
                newPart.setMax(Integer.parseInt(max));
                newPart.setID(Inventory.getPartsSize());
                newPart.setCompany(company);

                // one last check for errors within a range using the checkIfValid method
                // of the Part class.
                try {
                        newPart.checkIfValid();
                    if (newPart.checkIfValid() == true) {
                        
                        // add the new outsourced part
                        Inventory.addPart(newPart);
                }
                    
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
        partIDField.setDisable(true);
        companyField.setDisable(true);
    }    
    
}
