
import java.util.ArrayList;
import java.util.List;

public class SortedArrayBinarySearch implements SearchStructure {

    private static final int NOT_FOUND = -1;

    private int[] array;
    private int size;
    private int capacity;

    private int levels;
    private int operations;

    public SortedArrayBinarySearch(int capacity) {
        this.capacity = capacity;
        this.array = new int[capacity];
        this.size = 0;
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

    @Override
    public void printName() {
        System.out.println("Binary Search");
    }

    /**
     * Searches for a key using binary search.
     * Returns the key if found, otherwise returns -1.
     */
    @Override
    public int search(int key) {
        int index = binarySearchIndex(key);
        return (index == NOT_FOUND) ? NOT_FOUND : array[index];
    }

    /**
     * Inserts a key into the sorted array.
     * If the key already exists, it is not inserted again.
     */
    @Override
    public void insert(int key) {
        if (size == capacity) {
            throw new IllegalStateException("The sorted array is full.");
        }

        // Do not insert duplicates
        if (binarySearchIndex(key) != NOT_FOUND) {
            return;
        }

        int position = findInsertPosition(key);

        // Shift elements one position to the right
        for (int i = size; i > position; i--) {
            operations++;
            array[i] = array[i - 1];
        }

        operations++;
        array[position] = key;
        size++;
    }

    /**
     * Deletes a key from the sorted array.
     * Returns true if deletion was successful, false otherwise.
     */
    @Override
    public boolean delete(int key) {
        int position = binarySearchIndex(key);

        if (position == NOT_FOUND) {
            return false;
        }

        // Shift elements one position to the left
        for (int i = position; i < size - 1; i++) {
            operations++;
            array[i] = array[i + 1];
        }

        size--;
        return true;
    }

    /**
     * Returns all keys in the interval [low, high].
     */
    @Override
    public List<Integer> rangeSearch(int low, int high) {
        List<Integer> result = new ArrayList<>();

        if (size == 0) {
            return result;
        }

        int start = findFirstGreaterOrEqual(low);

        if (start == size) {
            return result;
        }

        for (int i = start; i < size && array[i] <= high; i++) {
            levels++;
            operations++;
            result.add(array[i]);
        }

        return result;
    }

    /**
     * Standard binary search.
     * Returns the index of the key if found, otherwise returns -1.
     */
    private int binarySearchIndex(int key) {
        int left = 0;
        int right = size - 1;

        while (left <= right) {
            levels++;
            operations++;

            int mid = left + (right - left) / 2;

            if (array[mid] == key) {
                return mid;
            }

            operations++;
            if (key < array[mid]) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return NOT_FOUND;
    }

    /**
     * Finds the correct insertion position for a new key.
     */
    private int findInsertPosition(int key) {
        int left = 0;
        int right = size - 1;
        int answer = size;

        while (left <= right) {
            levels++;
            operations++;

            int mid = left + (right - left) / 2;

            if (array[mid] > key) {
                answer = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return answer;
    }

    /**
     * Finds the index of the first element >= value.
     * If no such element exists, returns size.
     */
    private int findFirstGreaterOrEqual(int value) {
        int left = 0;
        int right = size - 1;
        int answer = size;

        while (left <= right) {
            levels++;
            operations++;

            int mid = left + (right - left) / 2;

            if (array[mid] >= value) {
                answer = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return answer;
    }

    /**
     * Prints the contents of the sorted array.
     */
    public void printArray() {
        for (int i = 0; i < size; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println();
    }

    public int size() {
        return size;
    }
}