import java.util.Random;

public class Elite extends Customer{
    public Elite(Customer c) {
        super(c.getName(), c.getPassword());
        if (c.getCategory().equals("NORMAL")) setWallet(c.getWallet()-300F);
        if (c.getCategory().equals("PRIME")) setWallet(c.getWallet()-100F);
        setCategory("ELITE");
        setCoupons(c.getCoupons());
        setCart(c.getCart());
    }
    @Override
    protected float calcDelivery(float amount) {
        return (100F + (amount/100F)*0F);
    }

    @Override
    protected void giveCoupons() {
        Random rand = new Random();
        int numOfCoupons = rand.nextInt(3,5);
        for (int i=0; i<numOfCoupons; i++) {
            addCoupon(rand.nextInt(5, 16));
        }
    }

    @Override
    protected int getMembershipDiscount() { return 10; }

    @Override
    protected int getProductDiscount(Product p) {
        return p.getDiscountProduct(0);
    }

    @Override
    protected String getDeliveryTime() { return "2 days.";}

    @Override
    protected int getCategoryNumber() { return 0; }

    @Override
    protected String printDelivery(float amount) {
        return ("" + this.calcDelivery(amount));
    }

}