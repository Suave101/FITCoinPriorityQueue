/*

  Author: Alexander Doyle
  Email: adoyle2025@my.fit.edu
  Course: Data Structures and Algorithms
  Section: 2
  Description of this file: A Bitcoin inspired double Priority Queue
  implementation for the buying and selling of a ficticious coin named Fitcoin.

 */

public class HW4
{

    /*
     * Main method to parse the input and generate an output
     */
    public static void main(String[] args) {
        // The two queues that we are working with
        PriorityQueue sellerQueue = new PriorityQueue(SellOrder.class);
        PriorityQueue buyerQueue = new PriorityQueue(BuyOrder.class);

        // Parse the input and output what is required
        parseFile(sellerQueue, buyerQueue, args[0]);
    }

    /*
     * Parses the input file and adds the items to their respective queues
     */
    public static void parseFile(PriorityQueue sellerQueue, PriorityQueue buyerQueue, String file) {
        // Ensure the PriorityQueues are of the correct type
        if (!(sellerQueue.isType(SellOrder.class) && buyerQueue.isType(BuyOrder.class))) {
            throw new IllegalStateException("Your sellerQueue and or buyerQueue are of the wrong type!");
        }

        // Start parsing the file input
    }

}
