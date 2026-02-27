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

    public abstract boolean requiresSwap(Order order);

    /*
     * A safe reduction method to remove quantity
     */
    public void reduceQuantity(double amount) {
        assert amount > 0;
        assert amount <= quantity;
        this.quantity -= amount;
    }

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
}
