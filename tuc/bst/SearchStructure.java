package tuc.bst;

import java.util.List;

/*
 * Κοινό interface για τις 3 δομές της εργασίας.
 */
public interface SearchStructure {

    void insert(int key);

    int search(int key);

    boolean delete(int key);

    List<Integer> rangeSearch(int low, int high);

    void printName();
}