
package inventory_system;

import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;

/**
 *
 * @author matth
 */
public class Inventory {
    // all parts saved in the Parts list
    private static ObservableList<Part> Parts = observableArrayList();
    
// all products saved in the Products list
    private static ObservableList<Product> Products = observableArrayList();
    
    // adding or deleting parts from the inventory, getting part information.
    public static void addPart(Part part){Parts.add(part);}
    public static void deletePart(Part partToRemove) {Parts.remove(partToRemove);} 
    public static ObservableList<Part> getParts(){return Parts;}
    
    // modifies a part. replaces the selected part with modified part information
    // at the same index.
    public static boolean modifyPart(Part selectedPart, Part modifiedPart) {
    int partIndex;
    partIndex = Parts.indexOf(selectedPart);
    Parts.remove(selectedPart);
    Parts.add(partIndex, modifiedPart);
    return true;} 
    
    // adding new products or getting product information
    public static void addProduct(Product product){Products.add(product);}
    public static ObservableList<Product> getProducts(){return Products;}

     
    // get size of Parts list (used for assigning new part ID's)
    public static int getPartsSize(){return Parts.size();}
    // get size of Products list (used for assigning new product ID's)
    public static int getProductsSize(){return Products.size();}

}
