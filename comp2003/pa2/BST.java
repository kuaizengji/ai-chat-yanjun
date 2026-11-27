// Search-tree framework retained from the supplied template.
public class BST {
    // The inherited root is the single entry point used by AVL wrappers.
    Node root;

    // A new tree initially contains no salary nodes.
    public BST() { root = null; }
    // Expose the current root for traversal without maintaining a second root.
    public Node getRoot() { return root; }
    // Dynamic dispatch chooses AVL insertion and saves its possibly rotated root.
    public void insertNode(int x) { root = insertNode(root, x); }

    // The base implementation maintains BST order and cached subtree heights.
    protected Node insertNode(Node root, int x) {
        // An absent subtree ends this insertion or deletion search.
        if (root == null) {
            return new Node(x);
        }
        // Follow only the subtree that can contain the requested salary.
        if (x < root.getKey()) {
            root.setLeft(insertNode(root.getLeft(), x));
        } else if (x > root.getKey()) {
            root.setRight(insertNode(root.getRight(), x));
        }
        // Child changes must be reflected in the cached height on return.
        root.updateHeight();
        return root;
    }

    // Search from the shared root rather than a shadowed subclass field.
    public Node findNode(int x) { return findNode(root, x); }

    // Iterative search takes logarithmic time when the tree is AVL-balanced.
    protected Node findNode(Node root, int x) {
        while (root != null && root.getKey() != x) {
            root = x < root.getKey() ? root.getLeft() : root.getRight();
        }
        return root;
    }

    // Store the new root returned by virtual deletion after any rotations.
    public void deleteNode(int x) { root = deleteNode(root, x); }

    // Remove a complete salary node; record-level filtering belongs to SMS.
    protected Node deleteNode(Node root, int x) {
        // An absent subtree ends this insertion or deletion search.
        if (root == null) {
            return null;
        }
        // Follow only the subtree that can contain the requested salary.
        if (x < root.getKey()) {
            root.setLeft(deleteNode(root.getLeft(), x));
        } else if (x > root.getKey()) {
            root.setRight(deleteNode(root.getRight(), x));
        } else {
            if (root.getLeft() == null) {
                return root.getRight();
            }
            if (root.getRight() == null) {
                return root.getLeft();
            }
            // Two-child removal uses the smallest key in the right subtree.
            Node next = root.getRight();
            while (next.getLeft() != null) {
                next = next.getLeft();
            }
            // Move both the key and its records, never just the key.
            root.setKey(next.getKey());
            root.setRecords(next.getRecords());
            root.setRight(deleteNode(root.getRight(), next.getKey()));
        }
        // Child changes must be reflected in the cached height on return.
        root.updateHeight();
        return root;
    }

    // The leftmost node stores the minimum key of a nonempty subtree.
    public int minKey(Node root) {
        while (root.getLeft() != null) {
            root = root.getLeft();
        }
        return root.getKey();
    }
}
