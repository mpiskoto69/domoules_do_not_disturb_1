import java.util.ArrayList;
import java.util.List;

/*
 * Υλοποίηση δομής δυαδικής αναζήτησης με χρήση
 * μονοδιάστατου ταξινομημένου πίνακα.
 *
 * Ο πίνακας παραμένει πάντα ταξινομημένος.
 * Δεν επιτρέπονται διπλότυπα κλειδιά.
 */
public class SortedArrayBinarySearch implements SearchStructure {

    private static final int NOT_FOUND = -1;

    private int[] array;
    private int size;
    private int capacity;

    public SortedArrayBinarySearch(int capacity) {
        this.capacity = capacity;
        this.array = new int[capacity];
        this.size = 0;
    }

    @Override
    public void printName() {
        System.out.println("Binary Search");
    }

    /*
     * Επιστρέφει το key αν βρεθεί, αλλιώς -1.
     */
    @Override
    public int search(int key) {
        int index = binarySearchIndex(key);
        return (index == NOT_FOUND) ? NOT_FOUND : array[index];
    }

    /*
     * Εισαγωγή νέου κλειδιού στον ταξινομημένο πίνακα.
     * Αν το κλειδί υπάρχει ήδη, δεν ξαναεισάγεται.
     */
    @Override
    public void insert(int key) {
        if (size == capacity) {
            throw new IllegalStateException("Ο πίνακας είναι γεμάτος.");
        }

        // Αν υπάρχει ήδη, δεν κάνουμε τίποτα
        if (binarySearchIndex(key) != NOT_FOUND) {
            return;
        }

        // Βρες τη σωστή θέση εισαγωγής
        int position = findInsertPosition(key);

        // Μετακίνησε όλα τα μεγαλύτερα στοιχεία μία θέση δεξιά
        for (int i = size; i > position; i--) {
            array[i] = array[i - 1];
        }

        array[position] = key;
        size++;
    }

    /*
     * Διαγραφή κλειδιού.
     * Επιστρέφει true αν διαγράφηκε, false αν δεν βρέθηκε.
     */
    @Override
    public boolean delete(int key) {
        int position = binarySearchIndex(key);

        if (position == NOT_FOUND) {
            return false;
        }

        // Μετακίνηση αριστερά για να κλείσει το κενό
        for (int i = position; i < size - 1; i++) {
            array[i] = array[i + 1];
        }

        size--;
        return true;
    }

    /*
     * Επιστρέφει λίστα με όλα τα κλειδιά στο διάστημα [low, high].
     */
    @Override
    public List<Integer> rangeSearch(int low, int high) {
        List<Integer> result = new ArrayList<>();

        if (size == 0) {
            return result;
        }

        // Βρες το πρώτο στοιχείο που είναι >= low
        int start = findFirstGreaterOrEqual(low);

        if (start == size) {
            return result;
        }

        // Πρόσθεσε στοιχεία μέχρι να ξεπεράσουμε το high
        for (int i = start; i < size && array[i] <= high; i++) {
            result.add(array[i]);
        }

        return result;
    }

    /*
     * Κλασικό binary search που επιστρέφει index.
     * Αν δεν βρεθεί το key, επιστρέφει -1.
     */
    private int binarySearchIndex(int key) {
        int left = 0;
        int right = size - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (array[mid] == key) {
                return mid;
            }

            if (key < array[mid]) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return NOT_FOUND;
    }

    /*
     * Βρίσκει τη θέση όπου πρέπει να εισαχθεί το key,
     * ώστε ο πίνακας να παραμείνει ταξινομημένος.
     */
    private int findInsertPosition(int key) {
        int left = 0;
        int right = size - 1;
        int answer = size;

        while (left <= right) {
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

    /*
     * Βρίσκει το πρώτο index i τέτοιο ώστε array[i] >= value.
     * Αν δεν υπάρχει τέτοιο στοιχείο, επιστρέφει size.
     */
    private int findFirstGreaterOrEqual(int value) {
        int left = 0;
        int right = size - 1;
        int answer = size;

        while (left <= right) {
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

    /*
     * Εκτυπώνει τον πίνακα μέχρι το size.
     * Χρήσιμο για testing/debugging.
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