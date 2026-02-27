/*

    Author: Alexander Doyle
    Email: adoyle2025@my.fit.edu
    Course: Data Structures and Algorithms
    Section: 2
    Description of this file: An Buy Order class to define how to
    compare itself with a SellOrder.

 */

public class BuyOrder extends Order {
    public BuyOrder(int time, String name, double price, double quantity) {
        super(time, name, price, quantity);
    }

    @Override
    public boolean requiresSwap(Order parentOrder) {
        // Ensure correct type
        if (parentOrder instanceof SellOrder) {
            throw new IllegalStateException("You cannot swap a BuyOrder and a SellOrder!");
        }

        // For a max heap, if price higher, swap up
        if (this.getPrice() > parentOrder.getPrice()) {
            return true;
        }

        // If prices equal, swap by time
        if (this.getPrice() == parentOrder.getPrice()) {
            return this.getTime() < parentOrder.getTime();
        }

        // If price lower, stay below parent
        return false;
    }
}
