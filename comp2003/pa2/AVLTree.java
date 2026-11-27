// An AVL tree keyed by integer salary, not by individual employee.
public class AVLTree extends BST {
    // Use the inherited root: a second root would break inherited wrappers.
    // Construction delegates initialization to the shared BST root.
    public AVLTree() { super(); }

    // Null contributes zero height when computing balance factors.
    private int height(Node node) { return node == null ? 0 : node.getHeight(); }
    // A positive balance factor means the left subtree is taller.
    private int balance(Node node) {
        return height(node.getLeft()) - height(node.getRight());
    }

    // Promote the left child and repair heights from the bottom up.
    private Node rotateRight(Node top) {
        Node child = top.getLeft();
        top.setLeft(child.getRight());
        child.setRight(top);
        top.updateHeight();
        child.updateHeight();
        return child;
    }

    // Promote the right child and repair heights from the bottom up.
    private Node rotateLeft(Node top) {
        Node child = top.getRight();
        top.setRight(child.getLeft());
        child.setLeft(top);
        top.updateHeight();
        child.updateHeight();
        return child;
    }

    // Every modified ancestor is repaired in constant work per tree level.
    private Node rebalance(Node node) {
        node.updateHeight();
        // A left-right case needs an inner left rotation before the outer right one.
        if (balance(node) > 1) {
            if (balance(node.getLeft()) < 0) {
                node.setLeft(rotateLeft(node.getLeft()));
            }
            return rotateRight(node);
        }
        // A right-left case needs an inner right rotation before the outer left one.
        if (balance(node) < -1) {
            if (balance(node.getRight()) > 0) {
                node.setRight(rotateRight(node.getRight()));
            }
            return rotateLeft(node);
        }
        return node;
    }

    // Insert a distinct salary key and rebalance on the recursive return path.
    public Node insertNode(Node root, int x) {
        if (root == null) {
            return new Node(x);
        }
        if (x < root.getKey()) {
            root.setLeft(insertNode(root.getLeft(), x));
        } else if (x > root.getKey()) {
            root.setRight(insertNode(root.getRight(), x));
        } else {
            return root;
        }
        return rebalance(root);
    }

    // Delete the entire bucket and rebalance ancestors, including zero-child-balance cases.
    public Node deleteNode(Node root, int x) {
        if (root == null) {
            return null;
        }
        if (x < root.getKey()) {
            root.setLeft(deleteNode(root.getLeft(), x));
        } else if (x > root.getKey()) {
            root.setRight(deleteNode(root.getRight(), x));
        } else {
            // With at most one child, the surviving child becomes the subtree root.
            if (root.getLeft() == null) {
                return root.getRight();
            }
            if (root.getRight() == null) {
                return root.getLeft();
            }
            // The successor has no left child, so its old position is easy to remove.
            Node next = root.getRight();
            while (next.getLeft() != null) {
                next = next.getLeft();
            }
            // The successor's entire salary bucket belongs to its key.
            root.setKey(next.getKey());
            root.setRecords(next.getRecords());
            root.setRight(deleteNode(root.getRight(), next.getKey()));
        }
        return rebalance(root);
    }

    // Loading/moving records is silent: only SMS prints command responses.
    public void addEmployee(Employee employee) {
        // Equal salaries reuse one node; only the record list grows.
        insertNode(employee.getSalary());
        findNode(employee.getSalary()).updateData(employee);
    }

    // A global ID check scans records because salary ordering cannot locate IDs.
    public Employee findEmployee(int id) { return findEmployee(getRoot(), id); }

    // This traversal takes linear time in the total nodes and employee records.
    private Employee findEmployee(Node node, int id) {
        if (node == null) {
            return null;
        }
        for (Employee employee : node.getRecords()) {
            if (employee.getId() == id) {
                return employee;
            }
        }
        Employee found = findEmployee(node.getLeft(), id);
        return found != null ? found : findEmployee(node.getRight(), id);
    }

    // Count nodes, not employees: several employees can share one grade.
    // Reject invalid ranks internally; the assignment guarantees valid console ranks.
    public Node kthLargest(int k) {
        if (k < 1) {
            return null;
        }
        return kthLargest(getRoot(), new int[] {k});
    }

    // Reverse inorder visits larger grades first; the counter is shared across calls.
    private Node kthLargest(Node node, int[] remaining) {
        if (node == null) {
            return null;
        }
        Node found = kthLargest(node.getRight(), remaining);
        if (found != null) {
            return found;
        }
        // Count one grade even if partial adjustment left its bucket empty.
        if (--remaining[0] == 0) {
            return node;
        }
        return kthLargest(node.getLeft(), remaining);
    }
}
