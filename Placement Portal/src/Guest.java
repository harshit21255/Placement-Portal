import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Guest extends User {

    public void giveAccess() {
        Scanner sc = new Scanner(System.in);
        HashMap<String, Customer> customers = new HashMap<>();
        HashMap<Float, String> categories = new HashMap<>();
        HashMap<Float, Product> products = new HashMap<>();
        HashMap<Integer, Deal> deals = new HashMap<>();
        Admin admin = new Admin();

        System.out.println("\nWELCOME TO FLIPZON\n  ");

        while (true) {
            System.out.println("""
                    HOME PAGE
                    
                    \t1) Enter as Admin
                    \t2) Explore the Product Catalog
                    \t3) Show Available Deals
                    \t4) Enter as Customer
                    \t5) Exit the Application
                    """);

            int o1 = sc.nextInt(); sc.nextLine();

            //          Enter as Admin
            if (o1==1) {
                if (admin.logIn()) {
                    giveAdminAccess(admin, categories, products, deals);
                } else System.out.println("\nWrong Username or Password! :(\n");

            }
            //          Explore the Product Catalogue
            else if (o1==2) {
                exploreProductCatalogue(products, categories);
                System.out.println();
            }
            //          Show Deals
            else if (o1==3) {
                viewDeals(deals);
                System.out.println();
            }
            //          Enter As Customer
            else if (o1==4) {

                while (true) {
                    System.out.println("""
                            
                            \t1) Sign up
                            \t2) Log in
                            \t3) Back
                            """);

                    int o1_4 = sc.nextInt();
                    sc.nextLine();

                    //      Customer Sign Up
                    if (o1_4 == 1) {
                        System.out.println("\nCUSTOMER REGISTRATION\n");
                        System.out.print("Enter Name: "); String cName = sc.nextLine(); boolean inIt = false;
                        for (String c : customers.keySet()) {
                            if (c.equals(cName)) {
                                inIt = true; break;
                            }
                        }

                        if (inIt) {
                            System.out.println("\nUser Already Signed Up!");
                        } else {
                            System.out.print("Enter Password: "); String cPass = sc.nextLine();

                            customers.put(cName, new Customer(cName, cPass));
                            System.out.println("\nCustomer Successfully Registered!\n");
                        }
                    }
                    //      Customer Log In
                    else if (o1_4 == 2) {
                        System.out.println("\nCUSTOMER LOG IN\n");

                        System.out.print("Enter Name: "); String cName = sc.nextLine();
                        Customer customer=null;
                        for (Customer c : customers.values()) {
                            if (c.getName().equals(cName)) {
                                customer = c;
                                break;
                            }
                        }

                        if (customer!=null) {
                            if (customer.logIn()) {

                                giveCustomerAccess(customer, categories, products, deals, customers);
                            }
                        } else System.out.println("\nUser Not Registered!\n");
                    }
                    else if (o1_4 == 3) break; else System.out.println("\nEnter Valid Option!!\n");
                }
            }
            //          EXIT
            else if (o1==5) {
                System.out.println("\nThanks for using FLIPZON!\n");
                break;
            } else System.out.println("\nEnter Valid Option!!\n");
        }
    }

    private void giveAdminAccess(Admin admin, HashMap<Float, String> categories, HashMap<Float, Product> products, HashMap<Integer, Deal> deals) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\nWelcome " + admin.getName() + "!\n" +
                    "\nPlease choose any one of the following actions:\n" +
                    "\t1) Add category\n" +
                    "\t2) Delete category\n" +
                    "\t3) Add Product\n" +
                    "\t4) Delete Product\n" +
                    "\t5) Set Discount on Product\n" +
                    "\t6) Add giveaway deal\n" +
                    "\t7) Sign Out\n");

            int o1_1 = sc.nextInt(); sc.nextLine();

            //                          Add Category
            if (o1_1==1) {
                System.out.print("\nAdd Category ID: ");
                float cID = sc.nextFloat(); sc.nextLine();

                if (categories.containsKey(cID)) {
                    System.out.println("\nDear Admin, the category ID is already used!!! Please set a different and a unique category ID\n");
                } else {
                    System.out.print("Add Name of Category: ");
                    String cName = sc.nextLine();
                    if (categories.containsValue(cName))
                        System.out.println("\nDear Admin, the category Name is already used!!! Please set a different and a unique category Name\n");
                    else admin.addCategory(categories, cName, cID, products);
                }


            }
            //                          Delete Category
            else if (o1_1==2) {

                System.out.println("\nDear Admin, Delete a Category\n");
                System.out.print("\tEnter Category Name: "); String cName = sc.nextLine();
                System.out.print("\tEnter Category ID: "); float cID = sc.nextInt(); sc.nextLine();

                if (!categories.containsKey(cID)) {
                    System.out.println("\nCategory Not Added!\n");
                }
                else {
                    admin.remCategory(categories,cID, cName, products);
                }

            }
            //                          Add Product
            else if(o1_1==3) {
                System.out.print("\nEnter Category ID: ");
                float cID = sc.nextFloat(); sc.nextLine();

                if (categories.containsKey(cID)) {
                    System.out.println();
                    admin.addProduct(products, cID, false);
                } else System.out.println("\nCategory ID Invalid! Category " + cID + " Not Added!\n");

            }
            //                          Delete Product
            else if (o1_1==4) {
                System.out.println("\nDear Admin, to Delete a Product...\n");
                System.out.print("\tEnter Product ID: "); float pID = sc.nextFloat(); sc.nextLine();
                System.out.print("\tEnter Product Name: "); String pName = sc.nextLine();

                admin.remProduct(products, pID, pName, categories, deals);

            }
            //                          Add Discounts for Elite/Prime/Normal
            else if (o1_1==5) {
                System.out.println("""

                        Dear Admin give the Product ID you want to add the discount to!
                        
                        Enter the Product ID :""");
                float pID = sc.nextFloat(); sc.nextLine();
                if (!products.containsKey(pID)) {
                    System.out.println("\nProduct not even Added!\n");
                }
                else {
                    System.out.println("\nEnter discount for Elite, Prime and Normal customers respectively (in % terms):");
                    int e = sc.nextInt(), p = sc.nextInt(), n = sc.nextInt();
                    admin.discountProduct(products, pID, e, p, n);
                }
            }
            //                          Add Deals
            else if (o1_1==6){
                boolean exists = false;
                System.out.println("\nDear Admin give the Product IDs you want to combine and giveaway a deal for\n");

                System.out.println("Enter the first Product ID: "); float pID1 = sc.nextFloat(); sc.nextLine();
                System.out.println("\nEnter the Second Product ID: "); float pID2 = sc.nextFloat(); sc.nextLine();
                if (products.containsKey(pID1) && products.containsKey(pID2)) {
                    exists = true;
                } else System.out.println("\nOne or More of the Products not Added!\n");

                if (exists) {
                    float dPriceN, dPriceE, dPriceP;
                    while (true) {
                        System.out.println("\nEnter the combined price of each user type (Should be less than their combined price): ");
                        System.out.print("\nElite User: Rs. ");
                        dPriceE = sc.nextFloat();
                        System.out.print("Prime User: Rs. ");
                        dPriceP = sc.nextFloat();
                        System.out.print("Normal User: Rs. ");
                        dPriceN = sc.nextFloat();

                        Product P1 = products.get(pID1);
                        Product P2 = products.get(pID2);

                        boolean boolE = dPriceE < ((P1.getPrice() + P2.getPrice()) * ((100F - P1.getDiscountProduct(0)) / 100F) );
                        boolean boolP = dPriceP < ((P1.getPrice() + P2.getPrice()) * ((100F - P1.getDiscountProduct(1)) / 100F) );
                        boolean boolN = dPriceN < ((P1.getPrice() + P2.getPrice()) * ((100F - P1.getDiscountProduct(2)) / 100F) );
                        if (boolE && boolP && boolN) break;
                        else
                            System.out.println("\nCombined price not less than sum of the two Products (after Product discount) for one or more type of users!\n");
                    }

                    float[] dPrice = new float[] {dPriceE, dPriceP, dPriceN};
                    admin.addDeal(deals, pID1, pID2, dPrice, products);
                }

            }else if (o1_1==7) break; else System.out.println("\nEnter Valid Option!!\n");

        }
    }

    private void giveCustomerAccess(Customer customer , HashMap<Float, String> categories, HashMap<Float, Product> products, HashMap<Integer, Deal> deals, HashMap<String, Customer> customers) {
        Scanner sc = new Scanner(System.in);

        while(true) {
            System.out.println("\nWelcome " + customer.getName() + "!!\n" +
                    "\t1) Browse Products\n" +
                    "\t2) Browse Deals\n" +
                    "\t3) Add a Product to Cart\n" +
                    "\t4) Add a Deal to Cart\n" +
                    "\t5) View Coupons\n" +
                    "\t6) Check Account Balance\n" +
                    "\t7) View Cart\n" +
                    "\t8) Empty Cart\n" +
                    "\t9) Checkout Cart\n" +
                    "\t10) Upgrade Customer Status\n" +
                    "\t11) Add Amount to Wallet\n" +
                    "\t12) Sign Out\n");

            Cart currentCart = customer.getCart();
            ArrayList<Product> deletedProducts = new ArrayList<>();
            ArrayList<Deal> deletedDeals = new ArrayList<>();

            for (Product inCart : currentCart.getProducts().keySet())
                if (!products.containsKey(inCart.getID()) || inCart.getMaxQuantity()<currentCart.getProducts().get(inCart)) deletedProducts.add(inCart);

            for (Deal inCart : currentCart.getDeals().keySet())
                if (!deals.containsKey(inCart.getDealID()) || inCart.getP1().getMaxQuantity()==0 || inCart.getP2().getMaxQuantity()==0) deletedDeals.add(inCart);

            for (Product p : deletedProducts)
                currentCart.getProducts().remove(p);

            for (Deal d : deletedDeals)
                currentCart.getDeals().remove(d);


            int o1_4_2 = sc.nextInt(); sc.nextLine();


            if (o1_4_2==1) {
                customer.exploreProductCatalogue(products, categories);
            }
            else if (o1_4_2==2) {
                customer.viewDeals(deals);
            }
            else if (o1_4_2==3) {
                System.out.println("Enter Product ID and quantity:");
                float pID = sc.nextFloat(); int quantity = sc.nextInt();
                if(products.containsKey(pID))
                    customer.addProductToCart(quantity, products.get(pID));
                else System.out.println("\nProduct Not Available\n");
            }
            else if (o1_4_2==4) {
                System.out.print("Enter Deal ID: ");
                int dealID = sc.nextInt();
                if (deals.containsKey(dealID))
                    customer.addDealToCart(dealID, deals.get(dealID));
                else System.out.println("\nDeal Not Available\n");
            }
            else if (o1_4_2==5) {
                customer.viewCoupons();
            }
            else if (o1_4_2==6) {
                System.out.println("\nYour Current Balance is: Rs. " + customer.getWallet() + "\n");
            }
            else if (o1_4_2==7) {
                customer.viewCart();
            }

            else if (o1_4_2==8) {
                customer.emptyCart();
            }
            else if (o1_4_2==9) {
                customer.checkoutCart();
            }
            //      Update Status
            else if (o1_4_2==10) {

                String currentStatus = customer.getCategory();
                System.out.println("\nCurrent Status: " + currentStatus);

                System.out.print("\nChoose New Status: "); String newStatus = sc.nextLine();

                Customer newC = customer.updateStatus(newStatus);
                if (newC!=null) customer = newC;
            }
            else if (o1_4_2==11) {
                System.out.print("\nEnter Amount to be Added : "); float add = sc.nextFloat(); sc.nextLine();
                customer.addToWallet(add);
            }
            else if (o1_4_2==12) break; else System.out.println("\nEnter Valid Option!!\n");
        }
        customers.put(customer.getName(), customer);
    }
}