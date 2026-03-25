
import java.util.ArrayList;
import java.util.List;

public class DynamicMemoryBST implements TreeStructure {

    private TreeNode root;
    private int levels;
    private int operations;

    public DynamicMemoryBST() {
        root = null;
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
        System.out.println("BST with Dynamic Memory");
    }

    /**
     * Inserts a key into the BST.
     * If the key already exists, it is not inserted again.
     */
    @Override
    public void insert(int key) {
        // Case 1: empty tree
        if (root == null) {
            operations++;
            root = new TreeNode(key);
            levels = 1;
            return;
        }

        TreeNode current = root;
        TreeNode parent = null;

        // Traverse the tree until we find the correct null position
        while (current != null) {
            levels++;
            operations++;
            parent = current;

            operations++;
            if (key < current.key) {
                current = current.left;
            } else {
                operations++;
                if (key > current.key) {
                    current = current.right;
                } else {
                    return; // duplicate key, do not insert
                }
            }
        }

        // Insert the new node as a child of parent
        operations++;
        if (key < parent.key) {
            parent.left = new TreeNode(key);
        } else {
            parent.right = new TreeNode(key);
        }
    }

    /**
     * Searches for a key in the BST.
     * Returns the key if found, otherwise returns -1.
     */
    @Override
    public int search(int key) {
        TreeNode current = root;

        while (current != null) {
            levels++;
            operations++;

            if (key == current.key) {
                return current.key;
            }

            operations++;
            if (key < current.key) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        return -1;
    }

    /**
     * Deletes a key from the BST.
     * Returns true if deletion was successful, false otherwise.
     */
    @Override
    public boolean delete(int key) {
        TreeNode current = root;
        TreeNode parent = null;

        // Search for the node to delete
        while (current != null && current.key != key) {
            levels++;
            operations++;
            parent = current;

            operations++;
            if (key < current.key) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        // Key not found
        if (current == null) {
            return false;
        }

        levels++;

        // Case 1: node has 0 or 1 child
        if (current.left == null || current.right == null) {
            operations++;

            TreeNode child = (current.left != null) ? current.left : current.right;

            if (parent == null) {
                // Deleting the root
                root = child;
            } else if (parent.left == current) {
                parent.left = child;
            } else {
                parent.right = child;
            }

            return true;
        }

        // Case 2: node has 2 children
        TreeNode successorParent = current;
        TreeNode successor = current.right;

        // Find inorder successor (smallest node in right subtree)
        while (successor.left != null) {
            levels++;
            operations++;
            successorParent = successor;
            successor = successor.left;
        }

        // Copy successor key into current node
        current.key = successor.key;
        operations++;

        // Remove successor from its original position
        if (successorParent.left == successor) {
            successorParent.left = successor.right;
        } else {
            successorParent.right = successor.right;
        }

        return true;
    }

    /**
     * Returns all keys in the interval [low, high].
     */
    @Override
    public List<Integer> rangeSearch(int low, int high) {
        List<Integer> result = new ArrayList<>();
        rangeSearchRec(root, low, high, result);
        return result;
    }

    /**
     * Recursive helper for range search.
     */
    private void rangeSearchRec(TreeNode node, int low, int high, List<Integer> result) {
        if (node == null) {
            return;
        }

        levels++;
        operations++;

        if (low < node.key) {
            rangeSearchRec(node.left, low, high, result);
        }

        operations++;
        if (low <= node.key && node.key <= high) {
            result.add(node.key);
        }

        operations++;
        if (node.key < high) {
            rangeSearchRec(node.right, low, high, result);
        }
    }

    /**
     * Prints the keys of the BST in sorted order.
     */
    @Override
    public void inorder() {
        inorderRec(root);
        System.out.println();
    }

    private void inorderRec(TreeNode node) {
        if (node == null) {
            return;
        }

        inorderRec(node.left);
        System.out.print(node.key + " ");
        inorderRec(node.right);
    }
}