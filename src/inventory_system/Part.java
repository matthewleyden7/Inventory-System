/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory_system;

import javax.xml.bind.ValidationException;

/**
 *
 * @author matth
 */
abstract class Part {
    
    // private variables for part
    private String name;
    private double price;
    private int min;
    private int max;
    private int inv;
    private int partID;

    // default constructor
    public Part(){};
    
    // constructor
    public Part(String name, double price, int min, int max, int inv, int partID) {
        this.name = name;
        this.price = price;
        this.min = min;
        this.max = max;
        this.inv = inv;
        this.partID = partID;
    }
    
    // getters / setters
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
    public int getID() {return partID;}
    public void setID(int partID) {this.partID = partID;}
    
    // exception controls for parts
    public boolean checkIfValid() throws ValidationException {
        
        if (getName().equals("")) {throw new ValidationException("Please enter a part name.");}
        if (getInv() <= 0) {throw new ValidationException("Please enter an inventory number greater than 0.");}
        if (getPrice() <= 0.00) {throw new ValidationException("Please enter a price greater than $0.00.");}
        if (getMin() <= 0) {throw new ValidationException("Please enter a minimum inventory greater than 0.");}
        if (getMax() <= 0) {throw new ValidationException("Please enter a maximum inventory greater than 0.");}
        if (getMax() < getMin()) {throw new ValidationException("The maximum inventory must be greater than the minimum. Please enter a maximum that is greater than " + getMin());}
        if (getMin() > getMax()) {throw new ValidationException("The minimum inventory needs to be less than the maximum. Please enter a minimum that is less than " + getMax());}
        if ((getInv() < getMin()) || (getInv() > getMax())) {throw new ValidationException("The inventory must be between the minimum (" + getMin() + ") and the maximum (" + getMax() + ")" );}

        return true;
    }
}
    

