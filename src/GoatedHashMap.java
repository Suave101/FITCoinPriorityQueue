/*

  Author: Alexander Doyle
  Email: adoyle2025@my.fit.edu
  Course: Data Structures and Algorithms
  Section: 2
  Description of this file: A hashmap data structure to make my program faster

*/

public class GoatedHashMap {
    private final int size = 31;
    private HashNode[] map = new HashNode[size];

    // A HashNode class to store key and value
    private class HashNode {
        private Order value;
        private int key;
        private HashNode next;
        private HashNode prev;
        private boolean isHead;

        // A HashNode constructor to create a HashNode with order and key
        public HashNode(Order order, int key) {
            this.value = order;
            this.key = key;
            this.next = null;
            this.prev = null;
        }

        // Create Head HashNode contractor
        public HashNode() {
            this.next = null;
            this.prev = null;
            this.isHead = true;
        }

        // Prev setter method
        public void setPrev(HashNode prev) {
            this.prev = prev;
        }

        // Recursively adds a node to the linked list
        public void addNode(HashNode node) {
            // If node exists, assume we are doing a ChangeOrder operation
            if (!this.isHead && this.value.getName().equals(node.value.getName())) {
                this.value = node.value;
            } else if (this.next == null) {
                // Else add the node as usual
                this.next = node;
                node.setPrev(this);
            } else {
                // Recurse if needed
                this.next.addNode(node);
            }
        }

        // Recursively remove method
        public void removeNodeByName(String name) {
            // If equal to order remove cur node
            if (!this.isHead && this.value != null && this.value.getName().equals(name)) {
                if (this.next != null) {
                    this.next.prev = this.prev;
                }
                this.prev.next = this.next;
            } else {
                // Else, recurse to the next node
                if (this.next != null) {
                    this.next.removeNodeByName(name);
                }
            }
        }

        // Recursive Method to see if linked list contains order
        public Order containsOrderByName(String name) {
            if (!this.isHead && this.value.getName().equals(name)) {
                // Base Case 1
                return this.value;
            } else {
                if (this.next != null) {
                    // Recursive expansion
                    return this.next.containsOrderByName(name);
                }
            }
            // Base Case 2
            return null;
        }
    }

    // A constructor to make a hashmap
    public GoatedHashMap() {}

    // Add mapped item Method
    public void addOrder(Order order) {
        // Get the index for the map
        int index = getIndex(order.getName());

        // Create node to add
        HashNode node = new HashNode(order, order.hashCode());

        // If there is an item there, add the order to the linked list
        if (map[index] != null) {
            map[index].addNode(node);
        } else { // Else, just add the item
            // Create Head Node
            HashNode headNode = new HashNode();

            // Add new node to head node
            headNode.addNode(node);

            // Add head node to map
            map[index] = headNode;
        }
    }

    // Remove order method
    public void removeOrderByName(String name) throws Exception {
        // Get order index
        int index = getIndex(name);

        // If null at index, return error, else remove item
        if (map[index] == null) {
            throw new Exception("This element does not exist for " + name);
        } else {
            map[index].removeNodeByName(name);
        }
    }

    // Get order method
    public Order getOrderByName(String name) {
        // Get order index
        int index = getIndex(name);

        // Check recursively for order at index
        if (map[index] != null) {
            return map[index].containsOrderByName(name);
        }

        // Else return false if index is null pointer
        return null;
    }

    // Hash Function Method
    private int getIndex(String name) {
        return (name.hashCode() & 0x7FFFFFFF) % size;
    }
}
