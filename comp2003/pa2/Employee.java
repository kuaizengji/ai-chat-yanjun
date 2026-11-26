// A single employee; the salary is also the key of its owning AVL node.
public class Employee {
    // Identity does not change when an employee moves to another salary grade.
    private final int id;
    // Preserve first and last names separately for the required query output.
    private final String fname;
    private final String lname;
    // Department comparisons use the exact case-sensitive input text.
    private final String department;
    // Salary is mutable because adjustments migrate records between AVL nodes.
    private int salary;

    // Construct one complete record before adding it to a salary bucket.
    public Employee(int id, String fname, String lname, String department, int salary) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.department = department;
        this.salary = salary;
    }

    // Expose the stable identity used for duplicate checks and ID commands.
    public int getId() { return id; }
    // Return original name components without modifying their spelling.
    public String getFname() { return fname; }
    public String getLname() { return lname; }
    // Department queries and updates filter records using this field.
    public String getDepartment() { return department; }
    // The current salary must agree with the key of the owning node.
    public int getSalary() { return salary; }
    // Change salary only as part of removing and reinserting the record.
    public void setSalary(int salary) { this.salary = salary; }
}
