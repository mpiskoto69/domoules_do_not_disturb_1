package tuc.bst;

public class MainBSTTest {

    public static void main(String[] args) {

        System.out.println("==== TEST DynamicMemoryBST ====");

        DynamicMemoryBST tree1 = new DynamicMemoryBST();

        System.out.println("Insert elements:");
        tree1.insert(10);
        tree1.insert(5);
        tree1.insert(15);
        tree1.insert(3);
        tree1.insert(7);
        tree1.insert(12);
        tree1.insert(18);

        System.out.print("Inorder traversal: ");
        tree1.inorder();

        System.out.println("Search 7: " + tree1.search(7));
        System.out.println("Search 100: " + tree1.search(100));

        System.out.println("Range [4,15]: " + tree1.rangeSearch(4, 15));

        System.out.println("Delete 5: " + tree1.delete(5));

        System.out.print("Inorder after delete: ");
        tree1.inorder();

        System.out.println("Delete 100: " + tree1.delete(100));

        System.out.println();
        System.out.println("==== TEST ArrayBST ====");

        ArrayBST tree2 = new ArrayBST(50);

        System.out.println("Insert elements:");
        tree2.insert(10);
        tree2.insert(5);
        tree2.insert(15);
        tree2.insert(3);
        tree2.insert(7);
        tree2.insert(12);
        tree2.insert(18);

        System.out.print("Inorder traversal: ");
        tree2.inorder();

        System.out.println("Search 7: " + tree2.search(7));
        System.out.println("Search 100: " + tree2.search(100));

        System.out.println("Range [4,15]: " + tree2.rangeSearch(4, 15));

        System.out.println("Delete 5: " + tree2.delete(5));

        System.out.print("Inorder after delete: ");
        tree2.inorder();

        System.out.println("Delete 100: " + tree2.delete(100));

        System.out.println();
        System.out.println("Test completed.");
    }
}