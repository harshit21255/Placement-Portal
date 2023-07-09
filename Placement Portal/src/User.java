import java.util.HashMap;

public class User {
    protected void exploreProductCatalogue(HashMap<Float, Product> products, HashMap<Float, String> categories) {
        if (products.isEmpty()){
            System.out.println("\nNo Products Added!\n");
        }
        else {
            System.out.println("\n\tCATEGORIES\n");

            int l = 0;
            for (float i : categories.keySet()) {
                System.out.println(++l + ")" + "\tCategory ID: " + (int) i );
                System.out.println("\n\t\t" + categories.get(i).toUpperCase() + "\n");

                int k = 0;
                for (float j : products.keySet()) {
                    if (products.get(j).getCategoryID() == i) {
                        System.out.print("\t" + ++k + ") ");
                        products.get(j).printDetails(0);
                        System.out.println("\t\tStock Available: " + products.get(j).getMaxQuantity() + " unit(s)\n");
                    }
                }
            }
        }
    }

    protected  void viewDeals(HashMap<Integer, Deal> deals) {

        if (!deals.isEmpty()) {
            System.out.println("\t\t AVAILABLE DEALS \n");

            for (Deal d: deals.values()) {
                d.printDetails(4);
                System.out.println();
            }
        }
        else System.out.println("\nDear User, there are no deals for now!!! Please check regularly for exciting deals!\n");
    }
 }