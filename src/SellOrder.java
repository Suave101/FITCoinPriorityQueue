/*

  Author: Alexander Doyle
  Email: adoyle2025@my.fit.edu
  Course: Data Structures and Algorithms
  Section: 2
  Description of this file: An Sell Order class to define how to
  compare itself with a BuyOrder.

 */

public class SellOrder extends Order {
    public SellOrder(int time, String name, double price, double quantity) {
        super(time, name, price, quantity);
    }

    @Override
    public boolean requiresSwap(Order parentOrder) {
        // Ensure correct type
        if (parentOrder instanceof BuyOrder) {
            throw new IllegalStateException("You cannot swap a BuyOrder and a SellOrder!");
        }

        // For a min heap, if price lower, swap up
        if (this.getPrice() < parentOrder.getPrice()) {
            return true;
        }

        // If prices equal, swap by time
        if (this.getPrice() == parentOrder.getPrice()) {
            return this.getTime() < parentOrder.getTime();
        }

        //If price higher, stay below parent
        return false;
    }
}
