import java.util.ArrayList;

// One distinct salary grade, with all of its employee records.
public class Node {
    // An integer key represents a distinct salary grade, not an employee ID.
    private int key;
    // Cached subtree height lets rotations check balance in constant time.
    private int height;
    // Child links satisfy strict salary ordering on both sides.
    private Node left, right;
    // All employees in this bucket have salary equal to this node key.
    private ArrayList<Employee> records;

    // A newly inserted grade starts as a leaf with an empty employee bucket.
    public Node(int key) {
        this.key = key;
        height = 1;
        records = new ArrayList<Employee>();
    }

    // Read accessors support searching, balancing and command-level queries.
    public int getKey() { return key; }
    public int getHeight() { return height; }
    public Node getLeft() { return left; }
    public Node getRight() { return right; }
    public ArrayList<Employee> getRecords() { return records; }
    // Retain the lowercase setter supplied by the original template.
    public void setkey(int key) { this.key = key; }
    // Keep the UML spelling as an alias for the same key field.
    public void setKey(int key) { this.key = key; }
    // Rotations reconnect child links before refreshing cached heights.
    public void setLeft(Node node) { left = node; }
    public void setRight(Node node) { right = node; }
    // Successor replacement transfers the complete bucket together with its key.
    public void setRecords(ArrayList<Employee> newRecords) { records = newRecords; }
    // Insert into an existing grade without creating another node.
    public void updateData(Employee e) { records.add(e); }
    // Preserve the template text accessor without adding console output.
    public String getText() { return String.valueOf(key); }

    // Null subtrees have height zero; a leaf has height one.
    public void updateHeight() {
        height = 1 + Math.max(left == null ? 0 : left.height,
                              right == null ? 0 : right.height);
    }
}
