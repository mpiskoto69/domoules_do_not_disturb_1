
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

    @Override
    public void insert(int key) {
        if (root == null) {
            operations++;
            root = new TreeNode(key);
            levels = 1;
            return;
        }

        TreeNode current = root;
        TreeNode parent = null;

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
                    return; // duplicate
                }
            }
        }

        operations++;
        if (key < parent.key) {
            parent.left = new TreeNode(key);
        } else {
            parent.right = new TreeNode(key);
        }
    }

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

    @Override
    public boolean delete(int key) {
        TreeNode current = root;
        TreeNode parent = null;

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

        if (current == null) {
            return false;
        }

        levels++;

        // 0 ή 1 παιδί
        if (current.left == null || current.right == null) {
            operations++;

            TreeNode child = (current.left != null) ? current.left : current.right;

            if (parent == null) {
                root = child;
            } else if (parent.left == current) {
                parent.left = child;
            } else {
                parent.right = child;
            }

            return true;
        }

        // 2 παιδιά
        TreeNode successorParent = current;
        TreeNode successor = current.right;

        while (successor.left != null) {
            levels++;
            operations++;
            successorParent = successor;
            successor = successor.left;
        }

        current.key = successor.key;
        operations++;

        if (successorParent.left == successor) {
            successorParent.left = successor.right;
        } else {
            successorParent.right = successor.right;
        }

        return true;
    }

    @Override
    public List<Integer> rangeSearch(int low, int high) {
        List<Integer> result = new ArrayList<>();
        rangeSearchRec(root, low, high, result);
        return result;
    }

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