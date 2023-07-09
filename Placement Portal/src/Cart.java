import java.util.HashMap;

public class Cart {
    private HashMap <Product, Integer> products;
    private HashMap <Deal, Integer> deals;

    public Cart () {
        products = new HashMap<>();
        deals = new HashMap<>();
    }

    public HashMap<Product, Integer> getProducts() { return products; }

    public HashMap<Deal, Integer> getDeals() { return deals; }

    public void empty() {
        products.clear();
        deals.clear();
    }

    public boolean isEmpty() {
        return (products.isEmpty() && deals.isEmpty());
    }

    public void addProduct(int quantity, Product p) { products.put(p, quantity); }

    public void addDeal(int dID, Deal d) { deals.put(d,dID); }

    public void printDetails(int i) {
        if (products.isEmpty()) System.out.println("\n\tNo Products in Cart!\n");
        else {
            System.out.println("\n\tPRODUCTS ADDED TO CART\n");
            this.printProducts();
        }

        if (deals.isEmpty()) System.out.println("\n\tNo Deals in Cart!\n");
        else {
            System.out.println("\n\tDEALS ADDED TO CART\n");
            this.printDeals(i);
        }
    }

    private void printProducts() {
        int k=1;

        for (Product p : products.keySet()) {
            System.out.print("\t" + k++ + ") ");
            p.printDetails(0);
            System.out.println("\t\tQuantity Added in Cart: " + products.get(p));
            System.out.println();
        }
    }

    private void printDeals(int i) {
        for (Deal d: deals.keySet()) {
            d.printDetails(i);
            System.out.println();
        }
    }
}