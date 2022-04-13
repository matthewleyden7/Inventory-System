
package inventory_system;

/**
 *
 * @author matth
 */
public class InHouse extends Part{  
    
    private int ID;

    // default constructor
    public InHouse() {}

    // InHouse constructor
    public InHouse(int ID, String name, double price, int min, int max, int inv, int partID) {
        super(name, price, min, max, inv, partID);
        this.ID = ID;
    }
    
    // setter and getter for machine ID
    public int getMachineID(){return this.ID;}
    public void setMachineID(int ID){this.ID = ID;}
   
}