
package inventory_system;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.bind.ValidationException;

/**
 *
 * @author matth
 */
public class Product {
    
    // private variables for product
    private String name;
    private double price;
    private int min;
    private int max;
    private int inv;
    private int productID;

    // default constructor
    public Product(){};

    // setters / getters
    public void setName(String name) {this.name = name;}
    public void setPrice(double price) {this.price = price;}
    public void setMin(int min) {this.min = min;}
    public void setMax(int max) {this.max = max;}
    public void setInv(int inv) {this.inv = inv;}
    public String getName() {return name;}
    public double getPrice() {return price;}
    public int getMin() {return min;}
    public int getMax() {return max;}
    public int getInv() {return inv;}
    public int getID(){return productID;}
    public void setID(int productID) {this.productID = productID;}
    
    // create observable list to hold parts that are added to the product, getter and setter
    private ObservableList<Part> productParts = FXCollections.observableArrayList();
    public ObservableList<Part> getProductParts() {return productParts;}
    public void setProductParts(ObservableList<Part> productParts) {this.productParts = productParts;}   
    
    // exception controls for products
    public boolean checkIfValid() throws ValidationException {
        
        if (getName().equals("")) {throw new ValidationException("Please enter a product name.");}
        if ((getInv() <= 0)) {throw new ValidationException("Please enter an inventory greater than 0.");}
        if (getPrice() <= 0.00) {throw new ValidationException("Please enter a price greater than $0.00.");}
        if (getMin() <= 0) {throw new ValidationException("Please enter a minimum inventory greater than 0.");}
        if (getMax() <= 0) {throw new ValidationException("Please enter a maximum inventory greater than 0.");}
        if (getMax() < getMin()) {throw new ValidationException("The maximum inventory must be greater than the minimum. Please enter a maximum that is greater than " + getMin());}
        if (getMin() > getMax()) {throw new ValidationException("The minimum inventory needs to be less than the maximum. Please enter a minimum that is less than " + getMax());}
        if ((getInv() < getMin()) || (getInv() > getMax())) {throw new ValidationException("The inventory must be between the minimum (" + getMin() + ") and the maximum (" + getMax() + ")" );}

        return true;
    }
}