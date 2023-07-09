import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Admin implements LogInPortal {
    private final String name;
    private final String password;
    private final Scanner sc;
    private int dID;

    public Admin() {
        name = "Beff Jezos";
        password = "2021255";
        sc = new Scanner(System.in);
        dID = 1;
    }

    public String getName() { return name; }

    //~~~~~~~~~~~~~~~~~~~QUERIES~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public boolean logIn () {
        System.out.println("\nDear Admin\n");

        System.out.print("Enter Username: ");
        String aName = sc.nextLine();

        System.out.print("Enter Password: ");
        String passwd = sc.nextLine();

        if (aName.equals(name) && passwd.equals(password)){
            System.out.println("\nLogged In Successfully as " + name + " (Admin)\n");
            return true;
        }
        else return false;
    }
    public void addCategory(HashMap<Float, String> categories, String cName, float cID, HashMap<Float, Product> p) {
        categories.put(cID, cName);
        System.out.println("\nCategory - " + cID + " " + cName + " Added!\n");

        this.addProduct(p, cID, true);
    }

    public void remCategory(HashMap<Float, String> categories, float cID, String cName, HashMap<Float, Product> products) {
        categories.remove(cID);

        while (true) {
            boolean empty = true; float rem=0;
            for (Product p : products.values()) {
                if (p.getCategoryID() == cID) {
                    rem = p.getID();
                    empty = false;
                    break;
                }
            }

            if (empty) break;
            products.remove(rem);
        }

        System.out.println("\nCategory: " + cID + " " + cName + " Removed (and all Products under it)\n");
    }

    public void addProduct(HashMap<Float, Product> products ,float cID, boolean ignore) {
        String pName;
        float pID;

        while (true) {
            if (ignore) System.out.println("\nAdd a Product (Category can't be empty)\n");

            System.out.print("Product Name: ");
            pName = sc.nextLine(); boolean nameUsed = false;
            for (Product p : products.values()) {
                if (p.getName().equalsIgnoreCase(pName)) {
                    System.out.println("\nProduct Name Already Used! Use Another Product Name!\n");
                    nameUsed=true;
                }
            }
            if (nameUsed) continue;
            System.out.print("Product ID (of the form " + (int)cID + "._): ");
            pID = sc.nextFloat();
            sc.nextLine();

            if (products.containsKey(pID)) {
                if (products.get(pID).getName().equalsIgnoreCase(pName)) {
                    System.out.print("\nProduct ID and Name Already Used! Use Another Product ID!\n\nDo you want to Restock Quantity of this Product? (Y/N) ");
                    String restock = sc.nextLine();
                    if (restock.equalsIgnoreCase("Y")) {
                        System.out.print("Add number of units of the Product to add to the Stock: ");
                        int q = sc.nextInt();
                        sc.nextLine();
                        products.get(pID).restock(q);
                        System.out.println(); return;
                    }
                }
                else {
                    System.out.println("\nProduct ID Already Used! Use Another Product ID!\n");
                }
            } else break;
        }


        ArrayList<String> pDetails = new ArrayList<>();
        String line;
        while (true) {
            line = sc.nextLine();

            if (line.startsWith("Price:")) break;
            pDetails.add(line);
        }

        int q;
        while (true) {
            System.out.print("Quantity: ");
            q = sc.nextInt();
            sc.nextLine();
            if (q == 0) System.out.println("\nQuantity can't be 0!\n");
            else break;
        }

        float pPrice = Integer.parseInt(line.substring(11, line.length() - 2));
        Product p = new Product(pName, pID, cID, pPrice, pDetails, q);
        products.put(pID, p);

        System.out.println("\nProduct - " + pID + " " + pName + " Added!\n");

    }

    public void remProduct(HashMap<Float, Product> products, float pID, String pName, HashMap<Float, String> categories, HashMap<Integer, Deal> deals) {

        if (!products.containsKey(pID)) {
            System.out.println("\nProduct not even Added!\n");
        } else {
            float cID = products.get(pID).getCategoryID();
            products.remove(pID);
            boolean noMore = true;
            for (Product p : products.values()) {
                if (p.getCategoryID()==cID) {
                    noMore = false;
                    break;
                }
            }

            System.out.println("Product - " + pID + " " + pName + " Removed!\n");
            if (noMore) {
                System.out.print("\nThe Category has No Products anymore!\nThe Category will be deleted if you don't add products to it!!\n\nDo you want to add products to the category? (Y/N) ");
                String decide = sc.nextLine();
                if (decide.equalsIgnoreCase("N")) {
                    System.out.println("Category - " + cID + " " + categories.get(cID) + " Removed (Can't have a Category with no Products)!\n");
                    categories.remove(cID);
                } else {
                    addProduct(products, cID, false);
                }
            }

            ArrayList<Deal> deletedDeals = new ArrayList<>();
            for (Deal d: deals.values()) {
                if (d.getP1().getID()==pID || d.getP2().getID()==pID) {
                    deletedDeals.add(d);
                }
            }
            for (Deal d: deletedDeals) {
                deals.remove(d.getDealID());
            }

        }
    }

    public void discountProduct(HashMap<Float, Product> products, float pID, int e, int p, int n) {
        products.get(pID).setDiscountProduct(e,p,n);
        System.out.println("\nDiscount on Product - " + pID + " " + products.get(pID).getName() + " Set!\n");
    }

    public void addDeal(HashMap<Integer, Deal> deals,float pID1, float pID2, float[] dPrice, HashMap<Float, Product> products) {
        boolean exists = false;
        for (Deal _d : deals.values()) {
            if ((_d.getP1().getID() == pID1 && _d.getP2().getID() == pID2) || (_d.getP1().getID() == pID2 && _d.getP2().getID() == pID1)) {
                System.out.println("\nDeal Already Exists!\n");
                exists = true;
                break;
            }
        }
        if (!exists) {
            Deal d = new Deal(products.get(pID1), products.get(pID2), dPrice, dID);
            deals.put(dID, d);
            dID++;
        }
    }
}