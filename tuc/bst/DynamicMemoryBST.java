package tuc.bst;

import java.util.ArrayList;
import java.util.List;

/*
 * Υλοποίηση BST με κλασική δυναμική παραχώρηση μνήμης.
 * Κάθε κόμβος είναι αντικείμενο TreeNode με left/right references.
 */
public class DynamicMemoryBST implements TreeStructure {

    private TreeNode root;

    public DynamicMemoryBST() {
        root = null;
    }

    @Override
    public void printName() {
        System.out.println("BST with Dynamic Memory");
    }

    @Override
    public void insert(int key) {
        root = insertRec(root, key);
    }

    /*
     * Εισαγωγή σε BST.
     * Αν το key υπάρχει ήδη, δεν ξαναεισάγεται.
     */
    private TreeNode insertRec(TreeNode node, int key) {
        if (node == null) {
            return new TreeNode(key);
        }

        if (key < node.key) {
            node.left = insertRec(node.left, key);
        } else if (key > node.key) {
            node.right = insertRec(node.right, key);
        }
        // Αν key == node.key, δεν κάνουμε τίποτα.

        return node;
    }

    @Override
    public int search(int key) {
        TreeNode found = searchRec(root, key);
        return (found == null) ? -1 : found.key;
    }

    /*
     * Κλασική αναζήτηση σε BST.
     */
    private TreeNode searchRec(TreeNode node, int key) {
        if (node == null) {
            return null;
        }

        if (key == node.key) {
            return node;
        }

        if (key < node.key) {
            return searchRec(node.left, key);
        }

        return searchRec(node.right, key);
    }

    @Override
    public boolean delete(int key) {
        if (search(key) == -1) {
            return false;
        }

        root = deleteRec(root, key);
        return true;
    }

    /*
     * Διαγραφή κόμβου από BST.
     * Περιπτώσεις:
     * 1) φύλλο
     * 2) ένας απόγονος
     * 3) δύο απόγονοι
     */
    private TreeNode deleteRec(TreeNode node, int key) {
        if (node == null) {
            return null;
        }

        if (key < node.key) {
            node.left = deleteRec(node.left, key);
        } else if (key > node.key) {
            node.right = deleteRec(node.right, key);
        } else {
            // Βρέθηκε ο κόμβος προς διαγραφή

            // Περίπτωση 1 ή 2
            if (node.left == null) {
                return node.right;
            }

            if (node.right == null) {
                return node.left;
            }

            // Περίπτωση 3: δύο παιδιά
            TreeNode minNode = findMin(node.right);
            node.key = minNode.key;
            node.right = deleteRec(node.right, minNode.key);
        }

        return node;
    }

    /*
     * Επιστρέφει τον κόμβο με το μικρότερο key σε ένα υποδέντρο.
     * Πηγαίνουμε συνεχώς αριστερά.
     */
    private TreeNode findMin(TreeNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    @Override
    public List<Integer> rangeSearch(int low, int high) {
        List<Integer> result = new ArrayList<>();
        rangeSearchRec(root, low, high, result);
        return result;
    }

    /*
     * Αναζήτηση όλων των keys στο διάστημα [low, high].
     * Εκμεταλλευόμαστε την ιδιότητα του BST για να κόβουμε άχρηστα κλαδιά.
     */
    private void rangeSearchRec(TreeNode node, int low, int high, List<Integer> result) {
        if (node == null) {
            return;
        }

        if (low < node.key) {
            rangeSearchRec(node.left, low, high, result);
        }

        if (low <= node.key && node.key <= high) {
            result.add(node.key);
        }

        if (node.key < high) {
            rangeSearchRec(node.right, low, high, result);
        }
    }

    @Override
    public void inorder() {
        inorderRec(root);
        System.out.println();
    }

    /*
     * Inorder διάσχιση:
     * αριστερά - ρίζα - δεξιά
     * Σε BST τυπώνει τα keys σε αύξουσα σειρά.
     */
    private void inorderRec(TreeNode node) {
        if (node == null) {
            return;
        }

        inorderRec(node.left);
        System.out.print(node.key + " ");
        inorderRec(node.right);
    }
}