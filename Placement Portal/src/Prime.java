import java.util.Random;

public class Prime extends Customer{
    public Prime(Customer c) {
        super(c.getName(), c.getPassword());
        setWallet(c.getWallet()-200F);
        setCategory("PRIME");
        setCoupons(c.getCoupons());
        setCart(c.getCart());
    }

    @Override
    protected float calcDelivery(float amount) { return (100F + (amount/100F)*2F); }

    @Override
    protected void giveCoupons() {
        Random rand = new Random();
        int numOfCoupons = rand.nextInt(1,3);
        for (int i=0; i<numOfCoupons; i++) {
            addCoupon(rand.nextInt(5, 16));
        }
    }

    @Override
    protected int getMembershipDiscount() { return 5; }

    @Override
    protected int getProductDiscount(Product p) { return p.getDiscountProduct(2); }

    @Override
    protected String getDeliveryTime() { return "3-6 days.";}

    @Override
    protected int getCategoryNumber() { return 1; }

    @Override
    protected String printDelivery(float amount) {
        return ("100 + 2% of " + amount + " = 100 + " + (amount/100F)*2F + " = Rs. " + this.calcDelivery(amount));
    }
}