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
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javax.xml.bind.ValidationException;

/**
 * FXML Controller class
 *
 * @author matth
 */
public class ModifyProductController implements Initializable {

    @FXML private Button cancelBtn;
    @FXML private TextField partIDField;
    @FXML private TextField nameField;
    @FXML private TextField inventoryField;
    @FXML private TextField priceField;
    @FXML private TextField maxField;
    @FXML private TextField minField;
    @FXML private TextField searchPart;
    @FXML private Button saveBtn;
    @FXML private boolean error;
   
    
    // partsTable
    @FXML private TableView<Part> partsTable;
    @FXML private TableColumn<Part, Integer> partIDColumn; 
    @FXML private TableColumn<Part, String> partNameColumn; 
    @FXML private TableColumn<Part, Integer> inventoryColumn;
    @FXML private TableColumn<Part, Double> priceColumn;
    
    // Product parts table
    @FXML private TableView<Part> productPartsTable;
    @FXML private TableColumn<Part, Integer> partIDColumn2; 
    @FXML private TableColumn<Part, String> partNameColumn2; 
    @FXML private TableColumn<Part, Integer> inventoryColumn2;
    @FXML private TableColumn<Part, Double> priceColumn2;


    static Product selectedProduct;
    private ObservableList<Part> selection = FXCollections.observableArrayList();
    
   
    // initializing data from selected product on main screen, including parts for product
    public void InitData(Product productSelected){
        
    selectedProduct = productSelected;
    partIDField.setText(Integer.toString(selectedProduct.getID()));
    nameField.setText(selectedProduct.getName());
    inventoryField.setText(Integer.toString(selectedProduct.getInv()));
    priceField.setText(Double.toString(selectedProduct.getPrice()));
    maxField.setText(Integer.toString(selectedProduct.getMax()));
    minField.setText(Integer.toString(selectedProduct.getMin()));
    productPartsTable.setItems(productSelected.getProductParts());
    this.selection.addAll(productSelected.getProductParts());
    }
    
    // return to main screen with no changes
    public void pushCancel(ActionEvent e) throws IOException {
        Parent main = FXMLLoader.load(getClass().getResource("main_page.fxml"));
        Scene scene = new Scene(main);
        Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();}
    
    // remove a part from the product parts table
    public void pushRemove(){
        
        Part selectedPart = productPartsTable.getSelectionModel().getSelectedItem();
        if(selectedPart == null) {displayErrorMessage("Please select a part to remove");
        } else {
            selection.remove(selectedPart);
            selectedProduct.setProductParts(selection);
            productPartsTable.setItems(selectedProduct.getProductParts());}}
           
    
    // add a part to new product
    public void pushAdd(){
        Part selectedPart = partsTable.getSelectionModel().getSelectedItem();
            if (selectedPart == null) {displayErrorMessage("Please select a part to add");
            } else {
                if(selectedPart.getInv() < selectedPart.getMin()){displayErrorMessage("You don't have any more " + selectedPart.getName() + " in your inventory");}
                else {
                selection.add(selectedPart);
                productPartsTable.setItems(selection);}}}
                
    
    // search for a part
    public void pushSearchPart(){
       
        ObservableList<Part> selection, Parts;
        selection = Inventory.getParts();
        String find = searchPart.getText();
        if(find.equals("")){displayErrorMessage("Please type a part to search for");
        } else {
            for(Part p: selection){
                if (p.getName().equals(find)) {partsTable.getSelectionModel().select(p);}
                if (Integer.toString(p.getID()).equals(find)) {partsTable.getSelectionModel().select(p);}
                if (p instanceof OutSourced){
                    if(((OutSourced)p).getCompany().equals(find)){partsTable.getSelectionModel().select(p);}}
                else {
                    if(Integer.toString(((InHouse)p).getMachineID()).equals(find)){partsTable.getSelectionModel().select(p);}
                }}}
         
        // if search does not find a product, error message, else, no error message
        if(partsTable.getSelectionModel().getSelectedItems().size() == 0){displayErrorMessage("Part was not found");}}
        
    
    // displays an error message when user enters an invalid number or fails to
    // enter anything in a required field.
    public void displayErrorMessage(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Product error");
        alert.setHeaderText("Invalid product");
        alert.setContentText(errorMessage);
        alert.showAndWait();
        
    }
    // save a new product
    public void pushSave(ActionEvent e) throws IOException {
        ArrayList<String> errorList = new ArrayList<String>();
        
        // error checking
        if ((productPartsTable.getItems().size() == 0) && (partsTable.getItems().size() == 0)){errorList.add("You have no parts in your inventory. Please add some parts first.");}
        
        String name = this.nameField.getText();
        if (name.equals("")) {errorList.add("name field required"); error = true;}
        
         // check whether user entered an inventory number
        String inv = this.inventoryField.getText();
        try {Integer.parseInt(inv);}catch(NumberFormatException ex){errorList.add("Please enter an inventory number"); error = true;}
        
        // check whether the user entered a price
        String price = this.priceField.getText();
        try {Double.parseDouble(price);}catch(NumberFormatException ex){errorList.add("Please enter a product price greater than 0"); error = true;}
        
        // check whether the user entered a number into the minimum field
        String min = this.minField.getText();
        try {Integer.parseInt(min);}catch(NumberFormatException ex){errorList.add("Please enter a minimum inventory"); error = true;}
        
        // check whether the user entered a number into the maximum field
        String max = this.maxField.getText();
        try {Integer.parseInt(max);}catch(NumberFormatException ex){errorList.add("Please enter a maximum inventory"); error = true;}
        
        // check whether the user has added at least 1 part to their new product
        if(productPartsTable.getItems().size() == 0){errorList.add("Please add parts to your product"); error = true;}
       
        
        // if boolean "error" is true, this means there is a problem that needs to be fixed.
        // We use the displayErrorMessage method to display the first error in the
        // errorList.
        if (error == true) {
            
            displayErrorMessage(errorList.get(0));
            System.out.println("Product information needs corrections.");
            // set boolean error back to false
            error = false;
            
        } else {
          
                
            // updating product details
            if(selectedProduct.getName() != name){selectedProduct.setName(name);}
            if(selectedProduct.getInv() != Integer.parseInt(inv)){selectedProduct.setInv(Integer.parseInt(inv));}
            if(selectedProduct.getMin() != Integer.parseInt(min)){selectedProduct.setMin(Integer.parseInt(min));}
            if(selectedProduct.getMax() != Integer.parseInt(max)){selectedProduct.setMax(Integer.parseInt(max));}
            if(selectedProduct.getPrice() != Double.parseDouble(price)){selectedProduct.setPrice(Double.parseDouble(price));}

            // one last check for errors within a range using the checkIfValid method
            // of the Product class.
            try {
                selectedProduct.checkIfValid();
                if (selectedProduct.checkIfValid() == true) {
                    System.out.println("Product " + selectedProduct.getName() + " successfully updated");
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // initialize the part and product tables on the modify part screen. 
        // fill parts table with current available parts in inventory.
        partIDColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getID()).asObject());
        partNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        inventoryColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getInv()).asObject());
        priceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        partIDColumn2.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getID()).asObject());
        partNameColumn2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        inventoryColumn2.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getInv()).asObject());
        priceColumn2.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        partsTable.setItems(Inventory.getParts());
        partIDField.setDisable(true);
      
    }    
    
}
