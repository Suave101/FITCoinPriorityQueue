/*

  Author: Alexander Doyle
  Email: adoyle2025@my.fit.edu
  Course: Data Structures and Algorithms
  Section: 2
  Description of this file: An abstract class to define a general order

 */

public abstract class Order {
    private int time;
    private String name;
    private double price;
    private double quantity;

    /*
     * A constructor for an order
     */
    public Order(int time, String name, double price, double quantity) {
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
    public double getQuantity() {
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
    public int getTime() {
        return this.time;
    }

    /*
     * Getter method for getting the name
     */
    public String getName() {
        return this.name;
    }

    /*
     * Safe trade quantity changer
     */
    public boolean tradeWithQuantity(double tradeQuantity) {
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
        // Guard Case
        if (buyOrder.getPrice() < sellOrder.getPrice()) {
            throw new IllegalStateException("Market Violation: Buyer price is lower than the seller price!");
        }

        // Calculate sale price
        double salePrice = buyOrder.getPrice(); // If they are equal it does not matter
        if (buyOrder.getPrice() < sellOrder.getPrice()) {
            // In the unlikely case that the buy order has a higher price, the orders are executed at the average
            salePrice = (buyOrder.getPrice() + sellOrder.getPrice()) / 2.0;
        }

        // Determine trade quantity
        double tradeQuantity = Math.min(buyOrder.getQuantity(), sellOrder.getQuantity());

        // Execute the reduction in quantity
        boolean sellFinished = sellOrder.tradeWithQuantity(tradeQuantity);
        boolean buyFinished = buyOrder.tradeWithQuantity(tradeQuantity);

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
