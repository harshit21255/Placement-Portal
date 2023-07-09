import java.util.ArrayList;

public class Product implements Item{
    private final String name;
    private final float ID;
    private final ArrayList<String> details;
    private final float categoryID;
    private final float price;
    private int[] discountProduct;
    private int maxQuantity;

    public Product (String pName, float pID, float cID, float pPrice, ArrayList<String> deets, int q) {
        name = pName;
        ID = pID;
        categoryID = cID;
        price = pPrice;
        details = deets;
        discountProduct = new int[] {0,0,0};
        maxQuantity = q;
    }

    public String getName() { return name; }

    public float getID() { return ID; }

    public float getCategoryID() { return categoryID; }
    public ArrayList<String> getDetails() { return details; }

    public float getPrice() { return price; }
    public int getMaxQuantity() { return maxQuantity; }
    public void restock(int add) { maxQuantity+=add; }

    public void setDiscountProduct(int e, int p, int n) {
        discountProduct = new int[]{e, p, n};
    }
    @Override
    public void printDetails(int i) {
        System.out.println("\tProduct Name: " + this.getName());
        System.out.println("\t\tProduct ID: " + this.getID());
        for (String s : this.getDetails())
            System.out.println("\t\t" + s);
        System.out.println("\t\tPrice: Rs. " + this.getPrice());
    }

    public int getDiscountProduct(int i) {
        return discountProduct[i];
    }

    public void sell(int quantity) { maxQuantity-=quantity; }
}