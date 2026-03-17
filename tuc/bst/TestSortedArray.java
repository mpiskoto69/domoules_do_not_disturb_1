package Tuc.bst;

public class TestSortedArray {

    public static void main(String[] args) {
        SortedArrayBinarySearch structure = new SortedArrayBinarySearch(50);

        structure.printName();

        structure.insert(10);
        structure.insert(5);
        structure.insert(15);
        structure.insert(3);
        structure.insert(7);
        structure.insert(12);
        structure.insert(18);

        System.out.print("Array: ");
        structure.printArray();

        System.out.println("Search 7: " + structure.search(7));
        System.out.println("Search 100: " + structure.search(100));

        System.out.println("Range [4, 15]: " + structure.rangeSearch(4, 15));

        System.out.println("Delete 5: " + structure.delete(5));
        System.out.print("Array after delete 5: ");
        structure.printArray();

        System.out.println("Delete 100: " + structure.delete(100));

        structure.insert(10); // duplicate
        System.out.print("Array after duplicate insert of 10: ");
        structure.printArray();
    }
}