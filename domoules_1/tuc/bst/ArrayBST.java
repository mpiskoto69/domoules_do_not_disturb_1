
import java.util.ArrayList;
import java.util.List;

public class ArrayBST implements TreeStructure {

    // Row identifiers in the 3 x K table
    private static final int INFO = 0;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;

    private static final int NULL = -1;

    /*
     * 3 x K representation:
     * table[INFO][i]  -> key stored at node i
     * table[LEFT][i]  -> index of left child of node i
     * table[RIGHT][i] -> index of right child of node i
     *
     * For free nodes, table[RIGHT][i] is used to link the free-list.
     */
    private int[][] table;

    private int root;
    private int avail;
    private int size;
    private int capacity;

    private int levels;
    private int operations;

    public ArrayBST(int capacity) {
        this.capacity = capacity;
        this.table = new int[3][capacity]; // 3 x K, as suggested in the assignment

        initializeFreeList();
        root = NULL;
        size = 0;
        resetMetrics();
    }

    /**
     * Resets the counters used for the experimental measurements.
     */
    public void resetMetrics() {
        levels = 0;
        operations = 0;
    }

    public int getLevels() {
        return levels;
    }

    public int getOperations() {
        return operations;
    }

    /**
     * Initializes the free-list:
     * avail = 0 -> 1 -> 2 -> ... -> capacity-1 -> -1
     */
    private void initializeFreeList() {
        avail = 0;

        for (int i = 0; i < capacity; i++) {
            table[INFO][i] = NULL;
            table[LEFT][i] = NULL;
            table[RIGHT][i] = i + 1;
        }

        table[RIGHT][capacity - 1] = NULL;
    }

    @Override
    public void printName() {
        System.out.println("BST Array");
    }

    /**
     * Takes one free node from the top of the free-list.
     * Returns -1 if no free node exists.
     */
    private int getNode() {
        if (avail == NULL) {
            return NULL;
        }

        int newNode = avail;
        avail = table[RIGHT][avail];

        table[INFO][newNode] = NULL;
        table[LEFT][newNode] = NULL;
        table[RIGHT][newNode] = NULL;

        return newNode;
    }

    /**
     * Returns a node to the top of the free-list.
     */
    private void freeNode(int nodeIndex) {
        table[INFO][nodeIndex] = NULL;
        table[LEFT][nodeIndex] = NULL;
        table[RIGHT][nodeIndex] = avail;
        avail = nodeIndex;
    }

    /**
     * Inserts a key into the BST.
     * If the key already exists, it is not inserted again.
     */
    @Override
    public void insert(int key) {
        // Empty tree case
        if (root == NULL) {
            int newNode = getNode();
            if (newNode == NULL) {
                throw new IllegalStateException("The ArrayBST is full.");
            }

            operations++;
            levels = 1;

            table[INFO][newNode] = key;
            root = newNode;
            size++;
            return;
        }

        int current = root;
        int parent = NULL;

        // Standard BST traversal using array indices
        while (current != NULL) {
            levels++;
            operations++;
            parent = current;

            operations++;
            if (key < table[INFO][current]) {
                current = table[LEFT][current];
            } else {
                operations++;
                if (key > table[INFO][current]) {
                    current = table[RIGHT][current];
                } else {
                    return; // duplicate key
                }
            }
        }

        int newNode = getNode();
        if (newNode == NULL) {
            throw new IllegalStateException("The ArrayBST is full.");
        }

        table[INFO][newNode] = key;

        operations++;
        if (key < table[INFO][parent]) {
            table[LEFT][parent] = newNode;
        } else {
            table[RIGHT][parent] = newNode;
        }

        size++;
    }

    /**
     * Searches for a key in the BST.
     * Returns the key if found, otherwise returns -1.
     */
    @Override
    public int search(int key) {
        int current = root;

        while (current != NULL) {
            levels++;
            operations++;

            if (key == table[INFO][current]) {
                return table[INFO][current];
            }

            operations++;
            if (key < table[INFO][current]) {
                current = table[LEFT][current];
            } else {
                current = table[RIGHT][current];
            }
        }

        return -1;
    }

    /**
     * Deletes a key from the BST.
     * Returns true if deletion was successful, false otherwise.
     */
    @Override
    public boolean delete(int key) {
        if (root == NULL) {
            return false;
        }

        int current = root;
        int parent = NULL;

        // Search for the node to delete
        while (current != NULL && table[INFO][current] != key) {
            levels++;
            operations++;

            parent = current;

            operations++;
            if (key < table[INFO][current]) {
                current = table[LEFT][current];
            } else {
                current = table[RIGHT][current];
            }
        }

        if (current == NULL) {
            return false;
        }

        levels++;

        int leftChild = table[LEFT][current];
        int rightChild = table[RIGHT][current];

        // Case 1: node has 0 or 1 child
        if (leftChild == NULL || rightChild == NULL) {
            operations++;

            int child = (leftChild != NULL) ? leftChild : rightChild;

            if (parent == NULL) {
                root = child;
            } else if (table[LEFT][parent] == current) {
                table[LEFT][parent] = child;
            } else {
                table[RIGHT][parent] = child;
            }

            freeNode(current);
            size--;
            return true;
        }

        // Case 2: node has 2 children
        int successorParent = current;
        int successor = table[RIGHT][current];

        // Find inorder successor
        while (table[LEFT][successor] != NULL) {
            levels++;
            operations++;
            successorParent = successor;
            successor = table[LEFT][successor];
        }

        // Copy successor key into current node
        table[INFO][current] = table[INFO][successor];
        operations++;

        // Remove successor from its old location
        int successorChild = table[RIGHT][successor];

        if (table[LEFT][successorParent] == successor) {
            table[LEFT][successorParent] = successorChild;
        } else {
            table[RIGHT][successorParent] = successorChild;
        }

        freeNode(successor);
        size--;
        return true;
    }

    /**
     * Returns all keys in the interval [low, high].
     */
    @Override
    public List<Integer> rangeSearch(int low, int high) {
        List<Integer> result = new ArrayList<>();
        rangeSearchRec(root, low, high, result);
        return result;
    }

    /**
     * Recursive helper for range search.
     */
    private void rangeSearchRec(int node, int low, int high, List<Integer> result) {
        if (node == NULL) {
            return;
        }

        levels++;
        operations++;

        if (low < table[INFO][node]) {
            rangeSearchRec(table[LEFT][node], low, high, result);
        }

        operations++;
        if (low <= table[INFO][node] && table[INFO][node] <= high) {
            result.add(table[INFO][node]);
        }

        operations++;
        if (table[INFO][node] < high) {
            rangeSearchRec(table[RIGHT][node], low, high, result);
        }
    }

    /**
     * Prints the keys of the BST in sorted order.
     */
    @Override
    public void inorder() {
        inorderRec(root);
        System.out.println();
    }

    private void inorderRec(int node) {
        if (node == NULL) {
            return;
        }

        inorderRec(table[LEFT][node]);
        System.out.print(table[INFO][node] + " ");
        inorderRec(table[RIGHT][node]);
    }

    /**
     * Prints the internal 3 x K table for debugging.
     */
    public void printTable() {
        System.out.println("root = " + root + ", avail = " + avail);
        System.out.println("Index\tInfo\tLeft\tRight");

        for (int i = 0; i < capacity; i++) {
            System.out.println(i + "\t" +
                    table[INFO][i] + "\t" +
                    table[LEFT][i] + "\t" +
                    table[RIGHT][i]);
        }
    }

    public int size() {
        return size;
    }

    public int getRootIndex() {
        return root;
    }

    public int getAvail() {
        return avail;
    }
}