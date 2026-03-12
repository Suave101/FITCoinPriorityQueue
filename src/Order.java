/*

  Author: Alexander Doyle
  Email: adoyle2025@my.fit.edu
  Course: Data Structures and Algorithms
  Section: 2
  Description of this file: An abstract class to define a general order

 */

import java.text.DecimalFormat;
import java.util.Objects;

public abstract class Order {
    private String time;
    private String name;
    private double price;
    private int quantity;
    private boolean canceled = false;

    private static DecimalFormat priceFormat = new DecimalFormat("0.##");

    /*
     * A constructor for an order
     */
    public Order(String time, String name, double price, int quantity) {
        this.time = time;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    /*
     * A method to determine weather a swap must occur between 2 orders of the same type
     */
    public abstract boolean requiresSwap(Order order);

    /*
     * Getter method for getting the quantity
     */
    public int getQuantity() {
        return this.quantity;
    }

    /*
     * Getter method for getting the price
     */
    public double getPrice() {
        return this.price;
    }

    /*
     * Getter method for getting the time
     */
    public String getTime() {
        return this.time;
    }

    /*
     * Getter method for getting the name
     */
    public String getName() {
        return this.name;
    }

    /*
     * Getter method for canceled
     */
    public boolean isCanceled() {
        return canceled;
    }

    /*
     * Setter method for canceled
     */
    public void cancel() {
        this.canceled = true;
    }

    /*
     * Safe trade quantity changer
     */
    public boolean tradeWithQuantity(int tradeQuantity) {
        if (tradeQuantity > this.quantity) {
            throw new IllegalArgumentException("You only have " + this.quantity + " coins. You tried to trade "
                    + tradeQuantity + " coins. You cannot trade more coins than you have!");
        }
        if (tradeQuantity == this.quantity) {
            this.quantity = 0;
            return true;
        }
        this.quantity -= tradeQuantity;
        return false;
    }

    /*
     * A static method to execute a trade order
     */
    public static zeroQuantity executeTrade(SellOrder sellOrder, BuyOrder buyOrder) {
        // Calculate sale price
        double salePrice;

        if (buyOrder.getPrice() == sellOrder.getPrice()) {
            // If the price is the same, and we just use buy order price
            salePrice = buyOrder.getPrice();
        } else {
            // In the unlikely case that the buy order has a higher price, the orders are executed at the average
            salePrice = ((buyOrder.getPrice() + sellOrder.getPrice()) / 2.0);
        }

        // Determine trade quantity
        int tradeQuantity = Math.min(buyOrder.getQuantity(), sellOrder.getQuantity());

        // Execute the reduction in quantity
        boolean sellFinished = sellOrder.tradeWithQuantity(tradeQuantity);
        boolean buyFinished = buyOrder.tradeWithQuantity(tradeQuantity);

        // Print the output strings
        if (salePrice == (long) salePrice) {
            // Case 110.0 -> "110"
            System.out.printf("ExecuteBuySellOrders %d %d%n", (long) salePrice, tradeQuantity);
        } else {
            // Case: 102.5 -> "102.50"
            System.out.printf("ExecuteBuySellOrders %.2f %d%n", salePrice, tradeQuantity);
        }
        System.out.println("Buyer: " + buyOrder.getName() + " " + buyOrder.getQuantity());
        System.out.println("Seller: " + sellOrder.getName() + " " + sellOrder.getQuantity());

        // Return what orders are at zero quantity
        if (sellFinished && buyFinished) {
            return zeroQuantity.BOTH;
        } else if (sellFinished) {
            return zeroQuantity.SELL;
        } else if (buyFinished) {
            return zeroQuantity.BUY;
        }
        return zeroQuantity.NEITHER;
    }

    public enum zeroQuantity {
        SELL,
        BUY,
        NEITHER,
        BOTH
    }
}
