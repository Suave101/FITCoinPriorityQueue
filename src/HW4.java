/*

  Author: Alexander Doyle
  Email: adoyle2025@my.fit.edu
  Course: Data Structures and Algorithms
  Section: 2
  Description of this file: A Bitcoin inspired double Priority Queue
  implementation for the buying and selling of a ficticious coin named Fitcoin.

 */

import java.io.File;
import java.io.FileNotFoundException;
import java.math.RoundingMode;
import java.util.Scanner;
import java.text.DecimalFormat;

public class HW4 {

    // Decimal Format for Price
    private static final DecimalFormat fmtPrice = new DecimalFormat("0.##");
    private static final DecimalFormat fmtQuantity = new DecimalFormat("0.################");

    /*
     * Main method to parse the input and generate an output
     */
    public static void main(String[] args) throws FileNotFoundException {
        // Ensure rounding is correct
        fmtPrice.setRoundingMode(RoundingMode.HALF_UP);

        // The two queues that we are working with
        PriorityQueue sellerQueue = new PriorityQueue(SellOrder.class);
        PriorityQueue buyerQueue = new PriorityQueue(BuyOrder.class);

        // Parse the input and execute provided commands
        parseAndExecuteFile(sellerQueue, buyerQueue, args[0]);
    }

    /*
     * Parses the input file and executes the
     */
    public static void parseAndExecuteFile(PriorityQueue sellerQueue, PriorityQueue buyerQueue, String file) throws FileNotFoundException {
        // Ensure the PriorityQueues are of the correct type
        if (!(sellerQueue.isType(SellOrder.class) && buyerQueue.isType(BuyOrder.class))) {
            throw new IllegalStateException("Your sellerQueue and or buyerQueue are of the wrong type!");
        }

        // Start parsing the file input

        // The input file object that allows for access of the input file
        File inputFile = new File(file);

        // Storing if the last line was an entry to see if we need to start the execution loop
        boolean lastCommandWasEntry = false;

        // Try for safety
        try (Scanner fileScanner = new Scanner(inputFile)) {
            // Loop through each line of the file after the initial line
            while (fileScanner.hasNextLine()) {
                // Get the next line in the file
                String line = fileScanner.nextLine();

                // Split the string into its command and args
                String[] commandAndArgs = line.split(" ");

                // Print the command we are running
                System.out.print(line);

                // Determine which method to run and run it
                switch (commandAndArgs[0]) {
                    case "EnterBuyOrder":
                        lastCommandWasEntry = true;
                        enterBuyOrder(Integer.parseInt(commandAndArgs[1]), commandAndArgs[2], Double.parseDouble(commandAndArgs[3]), Double.parseDouble(commandAndArgs[4]), buyerQueue);
                        break;
                    case "EnterSellOrder":
                        lastCommandWasEntry = true;
                        enterSellOrder(Integer.parseInt(commandAndArgs[1]), commandAndArgs[2], Double.parseDouble(commandAndArgs[3]), Double.parseDouble(commandAndArgs[4]), sellerQueue);
                        break;
                    case "DisplayHighestBuyOrder":
                        // If the last entry was an entry, we need to execute trades before we display the highest buy order
                        if (lastCommandWasEntry) {
                            executeTrades(sellerQueue, buyerQueue);
                        }
                        displayHighestBuyOrder(buyerQueue);
                        lastCommandWasEntry = false;
                        break;
                    case "DisplayLowestSellOrder":
                        // If the last entry was an entry, we need to execute trades before we display the lowest sell order
                        if (lastCommandWasEntry) {
                            executeTrades(sellerQueue, buyerQueue);
                        }
                        displayLowestSellOrder(sellerQueue);
                        lastCommandWasEntry = false;
                        break;
                    default:
                        throw new UnsupportedOperationException("The data is formatted wrong and the command " + commandAndArgs[0] + " does not exist!");
                }

                // New line after output
                System.out.println();
            }
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

        // New line
        System.out.println();

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
        }
    }

    /*
     * Command Method to enter a buy order
     */
    public static void enterBuyOrder(int time, String name, double price, double quantity, PriorityQueue buyerQueue) {
        BuyOrder newOrder = new BuyOrder(time, name, price, quantity);
        buyerQueue.insert(newOrder);
    }

    /*
     * Command Method to enter a sell order
     */
    public static void enterSellOrder(int time, String name, double price, double quantity, PriorityQueue sellerQueue) {
        SellOrder newOrder = new SellOrder(time, name, price, quantity);
        sellerQueue.insert(newOrder);
    }

    /*
     * Command Method to display the highest buy order
     */
    public static void displayHighestBuyOrder(PriorityQueue buyerQueue) {
        // Ensure the PriorityQueue is of the correct type
        if (!buyerQueue.isType(BuyOrder.class)) {
            throw new IllegalStateException("Your buyerQueue is of the wrong type!");
        }

        // Get the highest buy order
        BuyOrder highestBuyOrder = (BuyOrder) buyerQueue.getRoot();

        // Print it out
        System.out.print(" " + highestBuyOrder.getName() + " " + highestBuyOrder.getTime() + " " + fmtPrice.format(highestBuyOrder.getPrice()) + " " + highestBuyOrder.getQuantity());
    }

    /*
     * Command Method to display the lowest sell order
     */
    public static void displayLowestSellOrder(PriorityQueue sellerQueue) {
        // Ensure the PriorityQueues are of the correct type
        if (!sellerQueue.isType(SellOrder.class)) {
            throw new IllegalStateException("Your sellerQueue is of the wrong type!");
        }

        // Get the lowest sell order
        SellOrder lowestSellOrder = (SellOrder) sellerQueue.getRoot();

        // Print it out
        System.out.print(" " + lowestSellOrder.getName() + " " + lowestSellOrder.getTime() + " " + lowestSellOrder.getPrice() + " " + lowestSellOrder.getQuantity());
    }

}
