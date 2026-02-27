/*

    Author: Alexander Doyle
    Email: adoyle2025@my.fit.edu
    Course: Data Structures and Algorithms
    Section: 2
    Description of this file: A priority queue implemented with an ArrayList with optimized swapping due to the hole
    shifting algorithm as opposed to swapping each node each iteration.

 */

import java.util.ArrayList;

public class PriorityQueue {

    private ArrayList<Order> priorityQueueStorage = new ArrayList<>();

    private Class<? extends Order> orderType;

    public PriorityQueue(Class<? extends Order> orderType) {
        this.orderType = orderType;
    }

    public PriorityQueue(ArrayList<Order> array, boolean isPriorityQueue, Class<? extends Order> orderType) {
        this.orderType = orderType;
        if (isPriorityQueue) {
            this.priorityQueueStorage.addAll(array);
        } else {
            for (Order order : array) {
                insert(order);
            }
        }
    }

    public boolean isType(Class<? extends Order> type) {
        return orderType == type;
    }

    public int getParentIndex(int index) {
        // Edge Case: We are at the root
        if (index == 0) {
            return -1;
        }

        // Error Case:
        if (index < 0) {
            throw new IllegalStateException("You cannot have anything above the root!");
        }
        return (index - 1) / 2;
    }

    public int getChildIndex(int index, Child child) {
        if (child.equals(Child.LEFT)) {
            return (2 * index) + 1;
        } else {
            return (2 * index) + 2;
        }
    }

    public boolean hasChild(int index, Child child) {
        return priorityQueueStorage.size() > getChildIndex(index, child);
    }

    public enum Child {
        LEFT,
        RIGHT
    }

    public void insert(Order element) {
        if (!orderType.isInstance(element)) {
            throw new IllegalArgumentException("You cannot put a " + element.getClass().getSimpleName() + " in a " + orderType.getSimpleName() + " queue!");
        }
        // 1. Insert at the next spot in line
        priorityQueueStorage.add(element);

        // 2. Swap with parents until parent is less than cur
        upHeap(priorityQueueStorage.size() - 1);
    }

    public void upHeap(int index) {
        // Swap up with parents until less than cur
        Order upChild = priorityQueueStorage.get(index);

        // Iterate through the parents until we satisfy the heap requirements
        while (index > 0) {
            // Get the index of the parent
            int parentIndex = getParentIndex(index);

            // If swap needed, swap
            if (upChild.requiresSwap(priorityQueueStorage.get(parentIndex))) {
                priorityQueueStorage.set(index, priorityQueueStorage.get(parentIndex));
                index = parentIndex;
            } else {
                break;
            }
        }
        priorityQueueStorage.set(index, upChild);
    }

    public Order removeMin() {
        // Check if the list is empty
        if (priorityQueueStorage.isEmpty()) {
            return null;
        }

        // Check if the heap is size 1
        if (priorityQueueStorage.size() == 1) {
            return priorityQueueStorage.remove(0);
        }

        // Save the root node value for return
        Order output = priorityQueueStorage.get(0);

        // Remove the root node and replace it with the last node
        priorityQueueStorage.set(0, priorityQueueStorage.remove(priorityQueueStorage.size() - 1));

        // Swap down until Priority Queue conditions are met
        downHeap(0);

        return output;
    }

    public void downHeap(int index) {
        // Original Order
        Order originalOrder = priorityQueueStorage.get(index);

        // Iterate while the current index has a right child
        while (hasChild(index, Child.LEFT)) {
            // Get the left index
            int leftIndex = getChildIndex(index, Child.LEFT);

            // Assume that we would swap with the left node
            int swapIndex = leftIndex;

            // If there is a right node, see if we would swap with it instead of the assumed left node because,
            // If the right node is better than the left node, then it should be higher than the left node
            // when it has been moved up a rank
            if (hasChild(index, Child.RIGHT)) {
                int rightIndex = getChildIndex(index, Child.RIGHT);
                // If it requires swap, we know that we would swap with the right child
                if (priorityQueueStorage.get(rightIndex).requiresSwap(priorityQueueStorage.get(leftIndex))) {
                    swapIndex = rightIndex;
                }
            }

            // Now compare swap index with cur index
            if (priorityQueueStorage.get(swapIndex).requiresSwap(originalOrder)) {
                // Shift child up
                priorityQueueStorage.set(index, priorityQueueStorage.get(swapIndex));

                // Move the space for a new child down
                index = swapIndex;
            } else {
                // We found the right spot!!!
                break;
            }
        }

        // If swaps have occurred, do the final swap move
        priorityQueueStorage.set(index, originalOrder);
    }

}
