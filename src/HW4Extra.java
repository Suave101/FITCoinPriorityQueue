/*

  Author: Alexander Doyle
  Email: adoyle2025@my.fit.edu
  Course: Data Structures and Algorithms
  Section: 2
  Description of this file: A Bitcoin inspired double Priority Queue
  implementation for the buying and selling of a ficticious coin named Fitcoin.

 */

/*
    Writeup:
    Question 1:
        By implementing a custom HashMap (GoatedHashMap), we achieve
        O(1) time complexity for looking up a buyer or seller's
        order that already exists by their name. Without this map.
        we would have to iteratively scan the entire priority queue
        array to find the user's old order which takes O(N) time.
    Question 2:
        Instead of manually finding and deleting the old order from
        the heap, (which would require shifting elements in an array
        , taking O(N) time), we use a cancel method on the order such
        that when an order is canceled it takes O(1) time and during
        any operation that requires to get or remove the root, we clean
        the heap of any canceled orders at the root taking O(log(N))
        time for each canceled order. This avoids the trivial O(N)
        shifting process.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Scanner;

public class HW4Extra {
    private static DecimalFormat priceFormat = new DecimalFormat("0.##");
    /*
     * Main method to parse the input and generate an output
     */
    public static void main(String[] args) throws FileNotFoundException {

        // The two queues that we are working with
        PriorityQueue sellerQueue = new PriorityQueue(SellOrder.class);
        PriorityQueue buyerQueue = new PriorityQueue(BuyOrder.class);

        // Two buyer and seller maps we are working with
        GoatedHashMap buyerMap = new GoatedHashMap();
        GoatedHashMap sellerMap = new GoatedHashMap();

        // Parse the input and execute provided commands
        parseAndExecuteFile(sellerQueue, buyerQueue, buyerMap, sellerMap, args[0]);
    }

    /*
     * Parses the input file and executes the
     */
    public static void parseAndExecuteFile(PriorityQueue sellerQueue, PriorityQueue buyerQueue, GoatedHashMap buyerMap, GoatedHashMap sellerMap, String file) throws FileNotFoundException {
        // Ensure the PriorityQueues are of the correct type
        if (!(sellerQueue.isType(SellOrder.class) && buyerQueue.isType(BuyOrder.class))) {
            throw new IllegalStateException("Your sellerQueue and or buyerQueue are of the wrong type!");
        }

        // Start parsing the file input

        // The input file object that allows for access of the input file
        File inputFile = new File(file);

        // Try for safety
        try (Scanner fileScanner = new Scanner(inputFile)) {
            // Loop through each line of the file after the initial line
            while (fileScanner.hasNextLine()) {
                // Get the next line in the file
                String line = fileScanner.nextLine();

                // Split the string into its command and args
                String[] commandAndArgs = line.split(" ");

                // Determine which method to run and run it
                switch (commandAndArgs[0]) {
                    case "EnterBuyOrder":
                        enterBuyOrder(commandAndArgs[1], commandAndArgs[2], Double.parseDouble(commandAndArgs[3]), Integer.parseInt(commandAndArgs[4]), buyerQueue, buyerMap);
                        executeTrades(sellerQueue, buyerQueue);
                        break;
                    case "EnterSellOrder":
                        enterSellOrder(commandAndArgs[1], commandAndArgs[2], Double.parseDouble(commandAndArgs[3]), Integer.parseInt(commandAndArgs[4]), sellerQueue, sellerMap);
                        executeTrades(sellerQueue, buyerQueue);
                        break;
                    case "DisplayHighestBuyOrder":
                        displayHighestBuyOrder(commandAndArgs[1], buyerQueue);
                        break;
                    case "DisplayLowestSellOrder":
                        displayLowestSellOrder(commandAndArgs[1], sellerQueue);
                        break;
                    case "ChangeBuyOrder":
                        changeBuyOrder(commandAndArgs[1], commandAndArgs[2], Double.parseDouble(commandAndArgs[3]), Integer.parseInt(commandAndArgs[4]), buyerQueue, buyerMap);
                        executeTrades(sellerQueue, buyerQueue);
                        break;
                    case "ChangeSellOrder":
                        changeSellOrder(commandAndArgs[1], commandAndArgs[2], Double.parseDouble(commandAndArgs[3]), Integer.parseInt(commandAndArgs[4]), sellerQueue, sellerMap);
                        executeTrades(sellerQueue, buyerQueue);
                        break;
                    case "CancelBuyOrder":
                        cancelOrder(commandAndArgs[1], commandAndArgs[2], buyerMap, "Buy");
                        executeTrades(sellerQueue, buyerQueue);
                        break;
                    case "CancelSellOrder":
                        cancelOrder(commandAndArgs[1], commandAndArgs[2], sellerMap, "Sell");
                        executeTrades(sellerQueue, buyerQueue);
                        break;
                    default:
                        throw new UnsupportedOperationException("The data is formatted wrong and the command " + commandAndArgs[0] + " does not exist!");
                }
            }
        }
    }

    /*
     * Cancels an order
     */
    public static void cancelOrder(String time, String name, GoatedHashMap map, String type) {
        // Check if person exists
        Order oldOrder = map.getOrderByName(name);
        if (oldOrder == null) {
            System.out.println("Cancel" + type + "Order " + time + " " + name + " noBuyerError");
        } else {
            // Cancel the order
            oldOrder.cancel();

            // Remove the user from your custom HashMap
            try {
                map.removeOrderByName(name);
            } catch (Exception e) {
                // Ignore, we already know they exist
            }

            // Print success
            System.out.println("Cancel" + type + "Order " + time + " " + name);
        }
    }

    /*
     * Changes a buy order
     */
    public static void changeSellOrder(String time, String name, double price, int quantity, PriorityQueue sellerQueue, GoatedHashMap sellerMap) {
        // Gets the old order from the seller map
        Order oldOrder = sellerMap.getOrderByName(name);

        // If order does not exist, say so
        if (oldOrder == null) {
            System.out.println("ChangeSellOrder " + time + " " + name + " " + priceFormat.format(price) + " " + quantity + " noBuyerError");
        } else {
            // Else, cancel the old order
            oldOrder.cancel();

            // Create a new order
            SellOrder newOrder = new SellOrder(time, name, price, quantity);

            // Add it to the map
            sellerMap.addOrder(newOrder);

            // Add it to the priority queue
            sellerQueue.insert(newOrder);

            // Print that we are awesome and did it!
            System.out.println("ChangeSellOrder " + time + " " + name + " " + priceFormat.format(price) + " " + quantity);
        }
    }

    /*
     * Changes a buy order
     */
    public static void changeBuyOrder(String time, String name, double price, int quantity, PriorityQueue buyerQueue, GoatedHashMap buyerMap) {
        // Gets the old order from the buyer map
        Order oldOrder = buyerMap.getOrderByName(name);

        // If order does not exist, say so
        if (oldOrder == null) {
            System.out.println("ChangeBuyOrder " + time + " " + name + " " + priceFormat.format(price) + " " + quantity + " noBuyerError");
        } else {
            // Else, cancel the old order
            oldOrder.cancel();

            // Create a new order
            BuyOrder newOrder = new BuyOrder(time, name, price, quantity);

            // Add it to the buyer map
            buyerMap.addOrder(newOrder);

            // Add it to the priority queue
            buyerQueue.insert(newOrder);

            // Print that we are awesome and did it!
            System.out.println("ChangeBuyOrder " + time + " " + name + " " + priceFormat.format(price) + " " + quantity);
        }
    }

    /*
     * Execute the trades until not possible
     */
    public static void executeTrades(PriorityQueue sellerQueue, PriorityQueue buyerQueue) {
        // Ensure the PriorityQueues are of the correct type
        if (!(sellerQueue.isType(SellOrder.class) && buyerQueue.isType(BuyOrder.class))) {
            throw new IllegalStateException("Your sellerQueue and or buyerQueue are of the wrong type!");
        }

        // Check for empty queue
        if (!(buyerQueue.hasRoot() && sellerQueue.hasRoot())) {
            return;
        }

        while (buyerQueue.getRoot().getPrice() >= sellerQueue.getRoot().getPrice()) {
            // Execute the trade and store
            Order.zeroQuantity depletedOrders = Order.executeTrade((SellOrder) sellerQueue.getRoot(), (BuyOrder) buyerQueue.getRoot());

            switch (depletedOrders) {
                case BUY:
                    buyerQueue.removeMin();
                    break;
                case SELL:
                    sellerQueue.removeMin();
                    break;
                case BOTH:
                    buyerQueue.removeMin();
                    sellerQueue.removeMin();
                    break;
                case NEITHER:
                    break;
                default:
                    throw new IllegalStateException("Depleted orders value must exist!");
            }

            // Check if the queue is empty on next iteration
            if (!(buyerQueue.hasRoot() && sellerQueue.hasRoot())) {
                break;
            }
        }
    }

    /*
     * Command Method to enter a buy order
     */
    public static void enterBuyOrder(String time, String name, double price, int quantity, PriorityQueue buyerQueue, GoatedHashMap buyerMap) {
        System.out.println("EnterBuyOrder " + time + " " + name + " " + priceFormat.format(price) + " " + quantity);
        BuyOrder newOrder = new BuyOrder(time, name, price, quantity);
        buyerQueue.insert(newOrder);
        buyerMap.addOrder(newOrder);
    }

    /*
     * Command Method to enter a sell order
     */
    public static void enterSellOrder(String time, String name, double price, int quantity, PriorityQueue sellerQueue, GoatedHashMap sellerMap) {
        System.out.println("EnterSellOrder " + time + " " + name + " " + priceFormat.format(price) + " " + quantity);
        SellOrder newOrder = new SellOrder(time, name, price, quantity);
        sellerQueue.insert(newOrder);
        sellerMap.addOrder(newOrder);
    }

    /*
     * Command Method to display the highest buy order
     */
    public static void displayHighestBuyOrder(String time, PriorityQueue buyerQueue) {
        // Ensure the PriorityQueue is of the correct type
        if (!buyerQueue.isType(BuyOrder.class)) {
            throw new IllegalStateException("Your buyerQueue is of the wrong type!");
        }



        if (buyerQueue.hasRoot()) {
            // Get the highest buy order
            BuyOrder highestBuyOrder = (BuyOrder) buyerQueue.getRoot();

            // Print it out
            System.out.println("DisplayHighestBuyOrder " + time + " " + highestBuyOrder.getName() + " " + highestBuyOrder.getTime() + " " + formatPrice(highestBuyOrder.getPrice()) + " " + highestBuyOrder.getQuantity());
        } else {
            System.out.println("DisplayHighestBuyOrder " + time + " none");
        }
    }

    /*
     * Command Method to display the lowest sell order
     */
    public static void displayLowestSellOrder(String time, PriorityQueue sellerQueue) {
        // Ensure the PriorityQueues are of the correct type
        if (!sellerQueue.isType(SellOrder.class)) {
            throw new IllegalStateException("Your sellerQueue is of the wrong type!");
        }

        if (sellerQueue.hasRoot()) {
            // Get the lowest sell order
            SellOrder lowestSellOrder = (SellOrder) sellerQueue.getRoot();

            // Print it out
            System.out.println("DisplayLowestSellOrder " + time + " " + lowestSellOrder.getName() + " " + lowestSellOrder.getTime() + " " + formatPrice(lowestSellOrder.getPrice()) + " " + lowestSellOrder.getQuantity());
        } else {
            // Print it out
            System.out.println("DisplayLowestSellOrder " + time + " none");
        }
    }

    /*
     * Formats price into correct string format
     */
    public static String formatPrice(double price) {
        return priceFormat.format(Math.round(100 * price) / 100);
    }

}
