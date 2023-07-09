import java.util.*;

public class Customer extends User implements LogInPortal{
    private final String name;
    private final String password;
    private float wallet;
    private String category;
    private ArrayList<Integer> coupons;
    private Cart cart;

    private final Scanner sc = new Scanner(System.in);

    public Customer(String cuName, String cuPass) {
        name = cuName;
        password = cuPass;
        category = "NORMAL";
        wallet = 1000F;
        coupons = new ArrayList<>();
        cart = new Cart();
    }

    public String getName() { return name; }

    protected String getPassword() { return password; }

    public String getCategory() { return category; }
    protected void setCategory(String s) { category = s; }

    protected ArrayList<Integer> getCoupons() { return coupons; }
    protected void setCoupons(ArrayList<Integer> _coupons) { coupons = _coupons; }

    protected Cart getCart() { return cart; }
    protected void setCart(Cart c) { cart = c; }
    protected void setWallet(float w) { wallet = w; }

    public void addCoupon(int c) { coupons.add(c); Collections.sort(coupons);}
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~QUERIES~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public boolean logIn () {
        System.out.print("Enter Password: "); String cPass = sc.nextLine();

        if (cPass.equals(password)) {
            System.out.println("\nLogged In Successfully as " + name + "! (Customer)\n");
            return true;
        }
        else {
            System.out.println("\nIncorrect Password! Try Again!\n");
            return false;
        }
    }

    public void addProductToCart(int q, Product P) {
        if (P.getMaxQuantity()==0) System.out.println("\nProduct OUT OF STOCK!\n");
        else if (q>P.getMaxQuantity()) System.out.println("\nCan't Add " + q + " Product(s) to Cart!\n" + '\n' +
                "Only " + P.getMaxQuantity() + " Product(s) in stock!\n");
        else {
            cart.addProduct(q,P);
            System.out.println("\nProduct - " + P.getID() + " " + P.getName() + " [" + q + " unit(s)] Added to Cart!\n");
        }
    }

    public void addDealToCart(int dID, Deal d) {
        if (d.getP1().getMaxQuantity()==0 || d.getP2().getMaxQuantity()==0)
            System.out.println("\nOne or Both of the Products in Deal are OUT OF STOCK\nCan't add Deal to Cart!\n");
        else {
            cart.addDeal(dID, d);
            System.out.println("\nDeal " + dID + " [" + d.getP2().getName() + " AND " + d.getP1().getName() + "] Added to Cart!\n");
        }
    }
    public void viewCoupons() {
        if (coupons.isEmpty()) {
            System.out.println("\nNo Coupons Available!");
            if (!category.equals("NORMAL")) System.out.println("Buy Products worth Rs. 5000 or more to Get Coupons!");
            System.out.println();

        } else {
            System.out.println("\nAvailable Coupons:-\n");
            for (int c = coupons.size() - 1; c >= 0; c--) {
                System.out.println("\tCoupon " + (coupons.size() - c) + ": " + coupons.get(c) + "%");
            }
        }
    }

    public float getWallet() { return wallet; }

    public void viewCart() { if (cart.isEmpty()) System.out.println("\nCart is Empty!\n");
                             else cart.printDetails(getCategoryNumber()); }

    public void emptyCart() { cart.empty();
        System.out.println("\nCart Emptied!\n");}

    public void checkoutCart() {
        if (!cart.isEmpty()) {
            if (sufficientFunds()) {
                System.out.println("\nProceeding to Checkout. Details:-\n");

                float totalCost = 0F; float totalDiscount = 0F; float subTotal = 0F;
                int coupon; int productDiscount; int membershipDiscount = getMembershipDiscount();
                int quantity; float itemTotal; int discount;

                if (coupons.isEmpty()) coupon = 0;
                else coupon = coupons.get(coupons.size() - 1);
                boolean couponUsed = false;

                for (Deal d : cart.getDeals().keySet()) {
                    d.printDetails(getCategoryNumber());
                    System.out.println("Deal Price for You (" + category + ") : Rs. " + d.getPrice()[getCategoryNumber()] + "/-");

                    itemTotal = d.getPrice()[getCategoryNumber()];
                    subTotal += itemTotal;
                    totalCost += itemTotal + this.calcDelivery(itemTotal);

                    System.out.println();
                    d.sell(1);

                }

                int i = 0;
                for (Product p : cart.getProducts().keySet()) {
                    quantity = cart.getProducts().get(p);
                    System.out.print("\t" + ++i + ") ");
                    p.printDetails(0);
                    System.out.println("\t\tQuantity Purchased: " + quantity + "\n");

                    itemTotal = p.getPrice() * quantity;
                    subTotal += itemTotal;

                    productDiscount = getProductDiscount(p);
                    discount = Math.max(productDiscount, Math.max(membershipDiscount, coupon)); totalDiscount += (itemTotal / 100F) * (float) discount;
                    totalCost += itemTotal + this.calcDelivery(itemTotal) - ((itemTotal / 100F) * (float) discount);

                    System.out.println("\t\tProduct SubTotal: Rs. " + itemTotal);
                    System.out.println("\t\tDiscount: " + discount + "% of " + itemTotal + " = Rs. " + (itemTotal / 100F) * (float) discount);
                    System.out.println();
                    if ((discount == coupon) && !coupons.isEmpty()) couponUsed = true;
                    p.sell(quantity);
                }

                if (couponUsed) coupons.remove(coupons.size() - 1);

                System.out.println("\nSubTotal: Rs. " + subTotal );
                System.out.println("\nTotal Delivery Charges: Rs. " + this.printDelivery(subTotal) );
                System.out.println("\nTotal Discount: Rs. " + totalDiscount );
                System.out.println("\nTotal Cost: Rs. " + totalCost );
                this.addToWallet(-totalCost);

                System.out.println("Order placed. It will be delivered in " + this.getDeliveryTime());
                System.out.println("\n");
                cart.empty();

                if (totalCost >= 5000) {
                    int I = coupons.size();
                    giveCoupons();
                    if (!category.equals("NORMAL")) {
                        System.out.print("You have won " + (coupons.size() - I) + " coupon(s) of ");
                        if ((coupons.size() - I) == 1) System.out.println(coupons.get(I));
                        else {
                            for (; I < coupons.size() - 2; I++)
                                System.out.print(coupons.get(I) + "%, ");

                            System.out.println(coupons.get(I) + "% and " + coupons.get(I + 1) + "% discount. Congratulations!!\n");
                        }
                    }
                }

            } else {
                System.out.println("\nInsufficient balance!! Please try again\n");
            }
        } else System.out.println("\nCart is Empty!\n");
    }

    public void addToWallet(float inc_dec) {
        if (inc_dec<0 && Math.abs(inc_dec)>wallet) System.out.println("\nInsufficient balance!! Please try again\n");
        else {
            wallet += inc_dec;
            System.out.println("\nAmount Successfully Added!\n");
        }
    }

    public Customer updateStatus(String _new) {
        if (category.equals("NORMAL")) {
            if (_new.equals("PRIME")) {
                if (wallet>=200F)
                    return new Prime(this);
                else {
                    System.out.println("\nInsufficient Balance\n");
                    return null;
                }

            } else if (_new.equals("ELITE")) {
                if (wallet>=300F)
                    return new Elite(this);
                else {
                    System.out.println("\nInsufficient Balance\n");
                    return null;
                }
            }
        }
        else if (category.equals("PRIME")) {
            if (_new.equals("ELITE")) {
                if (wallet>=100F)
                    return new Elite(this);
                else {
                    System.out.println("\nInsufficient Balance\n");
                    return null;
                }
            } else {
                System.out.println("\nCan't Downgrade!\n");
                return null;
            }
        }
        System.out.println("\nCan't Downgrade!\n");
        return null;
    }

//    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    protected boolean sufficientFunds() {
        float totalCost = 0F; float subTotal=0F; float itemTotal;
        int discount;  int coupon; int productDiscount; int quantity;
        int membershipDiscount=getMembershipDiscount();

        if (coupons.isEmpty()) coupon = 0;
        else coupon = coupons.get(coupons.size()-1);

        for (Deal d: cart.getDeals().keySet()) {
            itemTotal = d.getPrice()[getCategoryNumber()];

            subTotal += itemTotal;
            totalCost += itemTotal;
        }

        for (Product p : cart.getProducts().keySet()) {
            quantity = cart.getProducts().get(p);
            itemTotal = p.getPrice()*quantity;

            productDiscount = getProductDiscount(p);
            discount = Math.max(productDiscount, Math.max(membershipDiscount, coupon));

            subTotal+=itemTotal;
            totalCost += itemTotal - ((itemTotal/100F)*(float)discount);
        }

        totalCost += this.calcDelivery(subTotal);
        return (totalCost <= wallet);
    }

    protected float calcDelivery(float amount) { return (100F + (amount/100F)*5F); }

    protected void giveCoupons() {
        Random rand = new Random(); int numOfCoupons = 0;
        for (int i=0; i<numOfCoupons; i++) {
            addCoupon(rand.nextInt(5, 16));
        }
    }

    protected int getMembershipDiscount() { return 0; }

    protected int getProductDiscount(Product p) {
        return p.getDiscountProduct(2);
    }

    protected String getDeliveryTime() { return "7-10 days.";}

    protected int getCategoryNumber() { return 2; }

    protected String printDelivery(float amount) {
        return ("100 + 5% of " + amount + " = 100 + " + (amount/100F)*5F + " = Rs. " + this.calcDelivery(amount));
    }
}