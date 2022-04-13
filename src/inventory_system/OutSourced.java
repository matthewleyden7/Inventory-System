
package inventory_system;

/**
 *
 * @author matth
 */
public class OutSourced extends Part {
    
   private String company;
   
   // default constructor
   public OutSourced(){};
   
   // constructor
   public OutSourced(String company, String name, double price, int min, int max, int inv, int partID) {
        super(name, price, min, max, inv, partID);
        this.company = company;
    }
   
   // getter and setter for company name
   public void setCompany(String name){this.company = name;}
   public String getCompany(){return this.company;}
    
}
