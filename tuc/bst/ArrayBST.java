package tuc.bst;

import java.util.ArrayList;
import java.util.List;

/*
 * Υλοποίηση BST με χρήση δισδιάστατου πίνακα Kx3.
 *
 * table[i][0] = key του κόμβου στη γραμμή i
 * table[i][1] = γραμμή αριστερού παιδιού
 * table[i][2] = γραμμή δεξιού παιδιού
 *
 * Όταν μια γραμμή είναι ελεύθερη, το table[i][2] χρησιμοποιείται
 * για να δείχνει στην επόμενη ελεύθερη γραμμή της free list.
 *
 * Η τιμή -1 σημαίνει "δεν υπάρχει".
 */
public class ArrayBST implements TreeStructure {

    private int[][] table; // K x 3
    private int root;      // γραμμή ρίζας
    private int avail;     // πρώτη ελεύθερη γραμμή
    private int size;      // πλήθος ενεργών κόμβων
    private int capacity;  // πλήθος γραμμών

    public ArrayBST(int capacity) {
        this.capacity = capacity;
        this.table = new int[capacity][3];

        initializeFreeList();
        root = -1;
        size = 0;
    }

    /*
     * Αρχικοποιεί όλες τις γραμμές ως ελεύθερες:
     * 0 -> 1 -> 2 -> ... -> capacity-1 -> -1
     *
     * Η σύνδεση των free nodes γίνεται μέσω της 3ης στήλης.
     */
    private void initializeFreeList() {
        avail = 0;

        for (int i = 0; i < capacity; i++) {
            table[i][0] = -1;      // key
            table[i][1] = -1;      // left child
            table[i][2] = i + 1;   // next free line
        }

        table[capacity - 1][2] = -1;
    }

    @Override
    public void printName() {
        System.out.println("BST Array");
    }

    /*
     * Παίρνει την πρώτη διαθέσιμη ελεύθερη γραμμή.
     */
    private int getNode() {
        if (avail == -1) {
            return -1;
        }

        int newNode = avail;
        avail = table[avail][2];

        table[newNode][1] = -1;
        table[newNode][2] = -1;

        return newNode;
    }

    /*
     * Επιστρέφει μια γραμμή στη free list.
     */
    private void freeNode(int line) {
        table[line][0] = -1;
        table[line][1] = -1;
        table[line][2] = avail;
        avail = line;
    }

    @Override
    public void insert(int key) {
        if (root == -1) {
            int newNode = getNode();
            if (newNode == -1) {
                throw new IllegalStateException("Το ArrayBST είναι γεμάτο.");
            }

            table[newNode][0] = key;
            root = newNode;
            size++;
            return;
        }

        int current = root;
        int parent = -1;

        while (current != -1) {
            parent = current;

            if (key < table[current][0]) {
                current = table[current][1];
            } else if (key > table[current][0]) {
                current = table[current][2];
            } else {
                // duplicate key, δεν ξαναεισάγεται
                return;
            }
        }

        int newNode = getNode();
        if (newNode == -1) {
            throw new IllegalStateException("Το ArrayBST είναι γεμάτο.");
        }

        table[newNode][0] = key;

        if (key < table[parent][0]) {
            table[parent][1] = newNode;
        } else {
            table[parent][2] = newNode;
        }

        size++;
    }

    @Override
    public int search(int key) {
        int current = root;

        while (current != -1) {
            if (key == table[current][0]) {
                return table[current][0];
            } else if (key < table[current][0]) {
                current = table[current][1];
            } else {
                current = table[current][2];
            }
        }

        return -1;
    }

    @Override
    public boolean delete(int key) {
        if (root == -1) {
            return false;
        }

        int current = root;
        int parent = -1;

        while (current != -1 && table[current][0] != key) {
            parent = current;

            if (key < table[current][0]) {
                current = table[current][1];
            } else {
                current = table[current][2];
            }
        }

        if (current == -1) {
            return false;
        }

        int leftChild = table[current][1];
        int rightChild = table[current][2];

        // Περίπτωση 1 ή 2: έχει 0 ή 1 παιδί
        if (leftChild == -1 || rightChild == -1) {
            int child;

            if (leftChild != -1) {
                child = leftChild;
            } else {
                child = rightChild;
            }

            if (parent == -1) {
                root = child;
            } else if (table[parent][1] == current) {
                table[parent][1] = child;
            } else {
                table[parent][2] = child;
            }

            freeNode(current);
            size--;
            return true;
        }

        // Περίπτωση 3: δύο παιδιά
        int successorParent = current;
        int successor = table[current][2];

        while (table[successor][1] != -1) {
            successorParent = successor;
            successor = table[successor][1];
        }

        // αντιγραφή μόνο του key
        table[current][0] = table[successor][0];

        // ο successor έχει το πολύ ένα παιδί: δεξί
        int successorChild = table[successor][2];

        if (table[successorParent][1] == successor) {
            table[successorParent][1] = successorChild;
        } else {
            table[successorParent][2] = successorChild;
        }

        freeNode(successor);
        size--;
        return true;
    }

    @Override
    public List<Integer> rangeSearch(int low, int high) {
        List<Integer> result = new ArrayList<>();
        rangeSearchRec(root, low, high, result);
        return result;
    }

    /*
     * Αναζήτηση εύρους τιμών με pruning.
     */
    private void rangeSearchRec(int node, int low, int high, List<Integer> result) {
        if (node == -1) {
            return;
        }

        if (low < table[node][0]) {
            rangeSearchRec(table[node][1], low, high, result);
        }

        if (low <= table[node][0] && table[node][0] <= high) {
            result.add(table[node][0]);
        }

        if (table[node][0] < high) {
            rangeSearchRec(table[node][2], low, high, result);
        }
    }

    @Override
    public void inorder() {
        inorderRec(root);
        System.out.println();
    }

    /*
     * Inorder διάσχιση: αριστερά - ρίζα - δεξιά
     */
    private void inorderRec(int node) {
        if (node == -1) {
            return;
        }

        inorderRec(table[node][1]);
        System.out.print(table[node][0] + " ");
        inorderRec(table[node][2]);
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