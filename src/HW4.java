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
import java.util.Scanner;

public class HW4
{

    /*
     * Main method to parse the input and generate an output
     */
    public static void main(String[] args) throws FileNotFoundException {
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

        // Try for safety
        try (Scanner fileScanner = new Scanner(inputFile)) {
            // Loop through each line of the file after the initial line
            while (fileScanner.hasNextLine()) {
                // Get the next line in the file
                String line = fileScanner.nextLine();

                // Split the string into its command and args
                String[] commandAndArgs = line.split(" ");

                // Print the command we are running
                System.out.println(line);

                // Determine which method to run and run it
                switch (commandAndArgs[0]) {
                    case "EnterBuyOrder":
                        enterBuyOrder(Integer.parseInt(commandAndArgs[1]), commandAndArgs[2], Double.parseDouble(commandAndArgs[3]), Double.parseDouble(commandAndArgs[4]), buyerQueue);
                        break;
                    case "EnterSellOrder":
                        enterSellOrder(Integer.parseInt(commandAndArgs[1]), commandAndArgs[2], Double.parseDouble(commandAndArgs[3]), Double.parseDouble(commandAndArgs[4]), sellerQueue);
                        break;
                    case "DisplayHighestBuyOrder":
                        displayHighestBuyOrder(Integer.parseInt(commandAndArgs[1]));
                        break;
                    case "DisplayLowestSellOrder":
                        displayLowestSellOrder(Integer.parseInt(commandAndArgs[1]));
                        break;
                    default:
                        throw new UnsupportedOperationException("The data is formatted wrong and the command " + commandAndArgs[0] + " does not exist!");
                }
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
    public static void displayHighestBuyOrder(int time) {}

    /*
     * Command Method to display the lowest sell order
     */
    public static void displayLowestSellOrder(int time) {}

}
