

public class Deal implements Item{
    private final Product p1;
    private final Product p2;
    private final float[] price;
    private final int dealID;

    public Deal(Product P1, Product P2, float[] dPrice, int dID) {
        p1=P1;
        p2=P2;
        price = dPrice;
        dealID = dID;
    }

    public Product getP1() { return p1; }
    public Product getP2() { return p2; }
    public int getDealID() { return dealID; }
    public float[] getPrice() { return price; }

    @Override
    public void printDetails(int i) {
        System.out.print( "Deal ID - " + this.getDealID() + "\n");
        System.out.println("\tProduct 1: " + this.getP1().getID() + " " + this.getP1().getName());
        System.out.println("\tProduct 2: " + this.getP2().getID() + " " + this.getP2().getName());
        if (i<=2 && i>=0)
            System.out.println("\n\tDeal Price: Rs. " + this.getPrice()[i] + "/-\n");
        else System.out.println("\n\tDeal Pricing: \n\t\tRs. " + this.getPrice()[0] + "/- (ELITE)\n\t\tRs. " + this.getPrice()[1] + "/- (PRIME)\n\t\tRs. " + this.getPrice()[2] + "/- (NORMAL)");
    }

    @Override
    public void sell(int quantity) {
        p1.sell(quantity); p2.sell(quantity);
    }
}