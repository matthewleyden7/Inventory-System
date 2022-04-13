
package inventory_system;

import java.io.IOException;
import static java.lang.Integer.parseInt;
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

/**
 *
 * @author matth
 */
public class main_page_controller implements Initializable {
    
    // set button variables
    static boolean startProgram = true;
    @FXML private Button addPartBtn;
    @FXML private Button deletePartBtn;
    @FXML private Button modifyPartBtn;
    @FXML private Button addProductBtn;
    @FXML private Button searchProductBtn;
    @FXML private Button searchPartBtn;
    
    
    // parts table
    @FXML private TableView<Part> partsTable;
    @FXML private TableColumn<Part, Integer> partIDColumn; 
    @FXML private TableColumn<Part, String> partNameColumn; 
    @FXML private TableColumn<Part, Integer> inventoryColumn;
    @FXML private TableColumn<Part, Double> priceColumn;
    
    // products table
    @FXML private TableView<Product> productsTable;
    @FXML private TableColumn<Product, Integer> partIDColumn2; 
    @FXML private TableColumn<Product, String> partNameColumn2; 
    @FXML private TableColumn<Product, Integer> inventoryColumn2;
    @FXML private TableColumn<Product, Double> priceColumn2;
   
    // search textfields
    @FXML private TextField searchProduct;
    @FXML private TextField searchPart;
    
    // label that alerts you of errors as well as how to navigate the inventory
    // system main page.
    @FXML private Label errorLabel;

    

    // opens the add product screen to add a new product
    public void pushAddProduct(ActionEvent e) throws IOException {
        
        Parent addProduct = FXMLLoader.load(getClass().getResource("AddProduct.fxml"));
        Scene scene = new Scene(addProduct);
        Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();}
   
    // opens the add part screen
    public void pushAddPart(ActionEvent e) throws IOException {
        Parent addPart = FXMLLoader.load(getClass().getResource("AddPart.fxml"));
        Scene scene = new Scene(addPart);
        Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();}
    
    // delete a product
    public void pushDeleteProduct(){
        if(productsTable.getSelectionModel().getSelectedItem() == null){errorLabel.setText("Please choose a product to delete");}
        else {
        ObservableList<Product> selection, Products;
        Products = productsTable.getItems();
        selection = productsTable.getSelectionModel().getSelectedItems();
        for (Product p: selection){Products.remove(p); errorLabel.setText(p.getName() + " removed from inventory");}}}
    
    // delete a part
    public void pushDeletePart(){
        if(partsTable.getSelectionModel().getSelectedItem() == null){errorLabel.setText("Please choose a part to delete");}
        else {
        //ObservableList<Part> selection, Parts;
        //Parts = partsTable.getItems();
        Part selectio = partsTable.getSelectionModel().getSelectedItem();
        Inventory.deletePart(selectio);}}
        //for (Part p: selection){Parts.remove(p); errorLabel.setText(p.getName() + " removed from inventory");}}}
    
    // check whether user has selected a product to modify and open the modify
    // product screen.
    public void pushModifyProduct(ActionEvent e) throws IOException {
       
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        if(selectedProduct == null){
            errorLabel.setText("Please choose a product to modify");
        }else{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ModifyProduct.fxml"));
            Parent product = loader.load();
            Scene tableViewScene = new Scene(product);
            ModifyProductController controller = loader.getController();
            controller.InitData(selectedProduct);

            Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
            window.setScene(tableViewScene);
            window.show();}}

    // check whether the user has selected a part and open the modify part screen
    public void pushModifyPart(ActionEvent e) throws IOException {
       
        Part selectedPart = partsTable.getSelectionModel().getSelectedItem();
        if(selectedPart == null){
            errorLabel.setText("Please choose a part to modify");
        }else{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ModifiedPart.fxml"));
            Parent part = loader.load();
            Scene tableViewScene = new Scene(part);
            ModifiedPartController controller = loader.getController();
            controller.InitData(selectedPart);

            Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
            window.setScene(tableViewScene);
            window.show();}}

    // search for a product
    public void pushSearchProduct(){
       
        ObservableList<Product> selection, Products;
        selection = Inventory.getProducts();
        String find = searchProduct.getText();
        if(find.equals("")){errorLabel.setText("Please type a product to search for");
        } else {
            for(Product p: selection){
                if(p.getName().equals(find)){productsTable.getSelectionModel().select(p);}
                if(Integer.toString(p.getID()).equals(find)){productsTable.getSelectionModel().select(p);}}}
        
        // if search does not find a product, error message, else, no error message
        if(productsTable.getSelectionModel().getSelectedItems().size() == 0){errorLabel.setText("product not found");}
        else {errorLabel.setText("");}}
    
    // search for a part. Can search by name, part number, machine ID or company name
    public void pushSearchPart(){
       
        ObservableList<Part> selection, Parts;
        selection = Inventory.getParts();
        String find = searchPart.getText();
        if(find.equals("")){errorLabel.setText("Please type a part to search for");
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
        if(partsTable.getSelectionModel().getSelectedItems().size() == 0){errorLabel.setText("part not found");}
        else {errorLabel.setText("");}}
    
    // exit program if exit button is pushed.
    public void pushExit(){
        System.exit(0);
    }
                
           
                
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // initialize the main screen, including part and products tables, as 
        // well as an error label in the lower left of screen that indicates to
        // user what to do if a button is pressed without certain requirements
        // being met.
      
        errorLabel.setText("");
        errorLabel.setFont(Font.font("Mongolian baiti", 14));
        partIDColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getID()).asObject());
        partNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        inventoryColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getInv()).asObject());
        priceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        partIDColumn2.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getID()).asObject());
        partNameColumn2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        inventoryColumn2.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getInv()).asObject());
        priceColumn2.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        
        // fill parts table with initial parts
        if (startProgram){
            InHouse part1 = new InHouse(24876, "socket", 2.50, 1, 100, 34, 0);
            Inventory.addPart(part1);
             InHouse part2 = new InHouse(21299, "backlight", 5.00, 1, 100, 83, 1);
            Inventory.addPart(part2);
            InHouse part3 = new InHouse(92870, "LED driver", 26.0, 4, 50, 23, 2);
            Inventory.addPart(part3);
            OutSourced part4 = new OutSourced("Element", "wifi board", 23.0, 1, 40, 28, 3);
            Inventory.addPart(part4);
            OutSourced part5 = new OutSourced("AudioVox", "speaker(base)", 59.0, 1, 89, 30, 4);
            Inventory.addPart(part5);
            startProgram = false;
            
        }
        partsTable.setItems(Inventory.getParts());
        productsTable.setItems(Inventory.getProducts());
       
    }    
    
}
