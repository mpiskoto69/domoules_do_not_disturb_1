

import java.util.List;

/**
 * Common interface for the two BST implementations.
 */
public interface TreeStructure {

    void insert(int key);

    int search(int key);

    boolean delete(int key);

    List<Integer> rangeSearch(int low, int high);

    void inorder();

    void printName();
}