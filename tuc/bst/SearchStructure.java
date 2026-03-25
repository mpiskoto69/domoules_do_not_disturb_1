
import java.util.List;

/**
 * Common interface for the sorted-array structure.
 */
public interface SearchStructure {

    void insert(int key);

    int search(int key);

    boolean delete(int key);

    List<Integer> rangeSearch(int low, int high);

    void printName();
}