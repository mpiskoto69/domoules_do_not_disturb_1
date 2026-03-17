
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

    @Override
    public int search(int key) {
        int index = binarySearchIndex(key);
        return (index == NOT_FOUND) ? NOT_FOUND : array[index];
    }

    @Override
    public void insert(int key) {
        if (size == capacity) {
            throw new IllegalStateException("Ο πίνακας είναι γεμάτος.");
        }

        if (binarySearchIndex(key) != NOT_FOUND) {
            return; // duplicate
        }

        int position = findInsertPosition(key);

        for (int i = size; i > position; i--) {
            operations++;
            array[i] = array[i - 1];
        }

        operations++;
        array[position] = key;
        size++;
    }

    @Override
    public boolean delete(int key) {
        int position = binarySearchIndex(key);

        if (position == NOT_FOUND) {
            return false;
        }

        for (int i = position; i < size - 1; i++) {
            operations++;
            array[i] = array[i + 1];
        }

        size--;
        return true;
    }

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