package bst;

import java.util.ArrayList;
import java.util.List;

public class ArrayBST implements TreeStructure {

    private static final int INFO = 0;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private static final int NULL = -1;

    private int[][] table; // K x 3
    private int root;
    private int avail;
    private int size;
    private int capacity;

    public ArrayBST(int capacity) {
        this.capacity = capacity;
        this.table = new int[capacity][3];

        initializeFreeList();
        root = NULL;
        size = 0;
    }

    private void initializeFreeList() {
        avail = 0;

        for (int i = 0; i < capacity; i++) {
            table[i][INFO] = NULL;
            table[i][LEFT] = NULL;
            table[i][RIGHT] = i + 1;
        }

        table[capacity - 1][RIGHT] = NULL;
    }

    @Override
    public void printName() {
        System.out.println("BST Array");
    }

    private int getNode() {
        if (avail == NULL) {
            return NULL;
        }

        int newNode = avail;
        avail = table[avail][RIGHT];

        table[newNode][INFO] = NULL;
        table[newNode][LEFT] = NULL;
        table[newNode][RIGHT] = NULL;

        return newNode;
    }

    private void freeNode(int line) {
        table[line][INFO] = NULL;
        table[line][LEFT] = NULL;
        table[line][RIGHT] = avail;
        avail = line;
    }

    @Override
    public void insert(int key) {
        if (root == NULL) {
            int newNode = getNode();
            if (newNode == NULL) {
                throw new IllegalStateException("Το ArrayBST είναι γεμάτο.");
            }

            table[newNode][INFO] = key;
            root = newNode;
            size++;
            return;
        }

        int current = root;
        int parent = NULL;

        while (current != NULL) {
            parent = current;

            if (key < table[current][INFO]) {
                current = table[current][LEFT];
            } else if (key > table[current][INFO]) {
                current = table[current][RIGHT];
            } else {
                return;
            }
        }

        int newNode = getNode();
        if (newNode == NULL) {
            throw new IllegalStateException("Το ArrayBST είναι γεμάτο.");
        }

        table[newNode][INFO] = key;

        if (key < table[parent][INFO]) {
            table[parent][LEFT] = newNode;
        } else {
            table[parent][RIGHT] = newNode;
        }

        size++;
    }

    @Override
    public int search(int key) {
        int current = root;

        while (current != NULL) {
            if (key == table[current][INFO]) {
                return table[current][INFO];
            } else if (key < table[current][INFO]) {
                current = table[current][LEFT];
            } else {
                current = table[current][RIGHT];
            }
        }

        return -1;
    }

    @Override
    public boolean delete(int key) {
        if (root == NULL) {
            return false;
        }

        int current = root;
        int parent = NULL;

        while (current != NULL && table[current][INFO] != key) {
            parent = current;

            if (key < table[current][INFO]) {
                current = table[current][LEFT];
            } else {
                current = table[current][RIGHT];
            }
        }

        if (current == NULL) {
            return false;
        }

        int leftChild = table[current][LEFT];
        int rightChild = table[current][RIGHT];

        if (leftChild == NULL || rightChild == NULL) {
            int child = (leftChild != NULL) ? leftChild : rightChild;

            if (parent == NULL) {
                root = child;
            } else if (table[parent][LEFT] == current) {
                table[parent][LEFT] = child;
            } else {
                table[parent][RIGHT] = child;
            }

            freeNode(current);
            size--;
            return true;
        }

        int successorParent = current;
        int successor = table[current][RIGHT];

        while (table[successor][LEFT] != NULL) {
            successorParent = successor;
            successor = table[successor][LEFT];
        }

        table[current][INFO] = table[successor][INFO];

        int successorChild = table[successor][RIGHT];

        if (table[successorParent][LEFT] == successor) {
            table[successorParent][LEFT] = successorChild;
        } else {
            table[successorParent][RIGHT] = successorChild;
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

    private void rangeSearchRec(int node, int low, int high, List<Integer> result) {
        if (node == NULL) {
            return;
        }

        if (low < table[node][INFO]) {
            rangeSearchRec(table[node][LEFT], low, high, result);
        }

        if (low <= table[node][INFO] && table[node][INFO] <= high) {
            result.add(table[node][INFO]);
        }

        if (table[node][INFO] < high) {
            rangeSearchRec(table[node][RIGHT], low, high, result);
        }
    }

    @Override
    public void inorder() {
        inorderRec(root);
        System.out.println();
    }

    private void inorderRec(int node) {
        if (node == NULL) {
            return;
        }

        inorderRec(table[node][LEFT]);
        System.out.print(table[node][INFO] + " ");
        inorderRec(table[node][RIGHT]);
    }

    public void printTable() {
        System.out.println("root = " + root + ", avail = " + avail);
        System.out.println("Line\tInfo\tLeft\tRight");

        for (int i = 0; i < capacity; i++) {
            System.out.println(i + "\t" + table[i][INFO] + "\t" + table[i][LEFT] + "\t" + table[i][RIGHT]);
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