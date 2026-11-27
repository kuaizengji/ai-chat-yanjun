import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

// Console front end for the salary-keyed AVL employee database.
public class SMS {
    // All persistent employee records are stored in salary-keyed AVL buckets.
    private final AVLTree tree = new AVLTree();

    // Tests may audit the actual structure without changing console output.
    public AVLTree getTree() { return tree; }

    // Read the required relative filename; never write changes back to it.
    public void loadEmployees(String filename) throws IOException {
        // Resource management closes the relative input file after loading.
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Ignore an optional byte-order mark and surrounding whitespace.
                line = line.replace("\uFEFF", "").trim();
                if (line.isEmpty()) {
                    continue;
                }
                // A column-heading line is optional; it is not an employee.
                if (line.matches("(?i)id(?:[,\\s].*)?")) {
                    continue;
                }
                // The supplied database guarantees unique IDs; loading is silent.
                tree.addEmployee(parseEmployee(line));
            }
        }
    }

    // CSV is illustrated in the handout; whitespace-separated rows also work.
    static Employee parseEmployee(String line) {
        line = line.trim();
        // Optional braces surround a complete record, not the department name.
        if (line.startsWith("{") && line.endsWith("}")) {
            line = line.substring(1, line.length() - 1);
        }
        String[] fields;
        // Commas preserve multiword departments as one field.
        if (line.indexOf(',') >= 0) {
            fields = line.split(",", -1);
        } else {
            String[] words = line.trim().split("\\s+");
            if (words.length < 5) {
                throw new IllegalArgumentException("Invalid employee record");
            }
            fields = new String[] {words[0], words[1], words[2], "", words[words.length - 1]};
            // For whitespace input, join the middle words back into the department.
            StringBuilder department = new StringBuilder();
            for (int i = 3; i < words.length - 1; i++) {
                if (i > 3) {
                    department.append(' ');
                }
                department.append(words[i]);
            }
            fields[3] = department.toString();
        }
        // Each record must contain ID, two names, department and integer salary.
        if (fields.length != 5) {
            throw new IllegalArgumentException("Invalid employee record");
        }
        for (int i = 0; i < fields.length; i++) {
            fields[i] = fields[i].trim();
        }
        // Integer parsing follows the key type required by the handout UML.
        return new Employee(Integer.parseInt(fields[0]), fields[1], fields[2],
                            fields[3], Integer.parseInt(fields[4]));
    }

    // Removal is the only missing-grade message that ends with an exclamation mark.
    private String missingGrade(String action, int salary) {
        String ending = action.equals("remove") ? "not found!" : "not found";
        return "Failed to " + action + ": Salary grade " + salary + " " + ending;
    }

    // Drop one ID from this bucket. The caller retires the grade when it becomes empty.
    private boolean removeIdAt(Node node, int id) {
        ArrayList<Employee> records = node.getRecords();
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getId() == id) {
                records.remove(i);
                return true;
            }
        }
        return false;
    }

    // Remove every matching record in this grade and report whether any were removed.
    private boolean stripDepartment(Node node, String department) {
        ArrayList<Employee> records = node.getRecords();
        boolean removed = false;
        for (int i = records.size() - 1; i >= 0; i--) {
            if (records.get(i).getDepartment().equals(department)) {
                records.remove(i);
                removed = true;
            }
        }
        return removed;
    }

    // ID lookup scans only the selected salary bucket.
    private Employee employeeAt(Node node, int id) {
        for (Employee employee : node.getRecords()) {
            if (employee.getId() == id) {
                return employee;
            }
        }
        return null;
    }

    // Select first, then mutate; never iterate a list while reinserting into it.
    private ArrayList<Employee> select(Node node, String department) {
        ArrayList<Employee> selected = new ArrayList<Employee>();
        for (Employee employee : node.getRecords()) {
            if (department == null || employee.getDepartment().equals(department)) {
                selected.add(employee);
            }
        }
        return selected;
    }

    // Partial adjustments deliberately keep their source grade as in appendix example nine.
    private void move(Node source, ArrayList<Employee> selected, int increment) {
        // Detach the selected snapshot before reinserting to handle zero increments safely.
        source.getRecords().removeAll(selected);
        for (Employee employee : selected) {
            employee.setSalary(employee.getSalary() + increment);
            tree.addEmployee(employee);
        }
        // Retain an emptied source grade for partial adjustments, as in PDF test 9.
    }

    // Return precisely one response; prompts belong only to the console loop.
    public String execute(String line) {
        line = line.trim();
        if (line.equals("e")) {
            return "Goodbye!";
        }
        // Any whitespace may separate the command name from its argument.
        String[] head = line.split("\\s+", 2);
        if (head.length < 2) {
            throw new IllegalArgumentException("Invalid command");
        }
        // Split the command name once so spaces inside department names remain intact.
        String command = head[0];
        String argument = head[1].trim();
        // Reject a duplicate ID before either creating a grade or appending a record.
        if (command.equals("i")) {
            Employee employee = parseEmployee(argument);
            Employee duplicate = tree.findEmployee(employee.getId());
            if (duplicate != null) {
                return "Cannot insert duplicates - employee with ID " + employee.getId()
                    + " is already assigned to salary grade " + duplicate.getSalary();
            }
            boolean newGrade = tree.findNode(employee.getSalary()) == null;
            tree.addEmployee(employee);
            if (newGrade) {
                return "A new node with salary grade " + employee.getSalary() + " has been created";
            }
            return "The node with salary grade " + employee.getSalary()
                + " has been inserted a new employee record with ID " + employee.getId();
        }
        if (command.equals("kthLargest")) {
            int k = Integer.parseInt(argument);
            Node result = tree.kthLargest(k);
            // The handout guarantees valid k; invalid k has no specified output.
            if (result == null) {
                throw new IllegalArgumentException("Invalid k");
            }
            return "The top " + k + " salary grade is " + result.getKey();
        }
        // A space-separated remove command is accepted when no comma is present.
        if ((command.equals("rId") || command.equals("rDept")) && argument.indexOf(',') < 0) {
            argument = argument.replaceFirst("\\s+", ",");
        }
        // Comma-separated arguments may include optional spaces after each delimiter.
        String[] parts = argument.split(",", -1);
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }
        int salary = Integer.parseInt(parts[0]);
        Node node = tree.findNode(salary);
        if (node == null) {
            // Revised specification uses adjust, not query, for all missing adjustment grades.
            String action = command.equals("rId") || command.equals("rDept") ? "remove"
                : command.equals("a") || command.equals("aId") || command.equals("aDept") ? "adjust" : "query";
            return missingGrade(action, salary);
        }
        // Count employees in the bucket, not the number of salary nodes.
        if (command.equals("q")) {
            return "Totally " + node.getRecords().size() + " employees in salary grade " + salary;
        }
        // Detailed ID output is returned as one line, regardless of PDF line wrapping.
        if (command.equals("qId")) {
            int id = Integer.parseInt(parts[1]);
            Employee employee = employeeAt(node, id);
            if (employee == null) {
                return "Failed to query: Employee with ID " + id + " not found in salary grade " + salary;
            }
            return "Successful query: Employee with ID " + id + "; Name: " + employee.getFname()
                + " " + employee.getLname() + "; Department: " + employee.getDepartment()
                + " in salary grade " + salary;
        }
        // A missing department in an existing grade has a zero employee count.
        if (command.equals("qDept")) {
            return "Totally " + select(node, parts[1]).size() + " employees from " + parts[1]
                + " in salary grade " + salary;
        }
        // Whole-grade adjustment removes the old key before merging at the new key.
        if (command.equals("a")) {
            if (node.getRecords().isEmpty()) {
                return "Failed to adjust: no records are found in salary grade " + salary;
            }
            int increment = Integer.parseInt(parts[1]);
            ArrayList<Employee> selected = select(node, null);
            // Save the records before deleting: two-child deletion may replace this node.
            tree.deleteNode(salary);
            for (Employee employee : selected) {
                employee.setSalary(employee.getSalary() + increment);
                // The supplied database guarantees unique IDs; loading is silent.
                tree.addEmployee(employee);
            }
            return "Adjusted salary for all employees by " + increment + " dollars";
        }
        // Select a snapshot so moving employees cannot invalidate list iteration.
        if (command.equals("aDept")) {
            int increment = Integer.parseInt(parts[1]);
            String department = parts[2];
            ArrayList<Employee> selected = select(node, department);
            if (selected.isEmpty()) {
                return "Failed to adjust: employees from " + department + " not found";
            }
            move(node, selected, increment);
            return "Adjusted salary for all employees from " + department + " by " + increment + " dollars";
        }
        // Single-person adjustment reuses the same safe migration procedure.
        if (command.equals("aId")) {
            int increment = Integer.parseInt(parts[1]);
            int id = Integer.parseInt(parts[2]);
            Employee employee = employeeAt(node, id);
            if (employee == null) {
                return "No such an employee with ID " + id + " in salary grade " + salary;
            }
            ArrayList<Employee> selected = new ArrayList<Employee>();
            selected.add(employee);
            move(node, selected, increment);
            return "Adjusted salary for the employee with ID " + id + " by " + increment + " dollars";
        }
        // Removal stays inside the salary named by this command.
        if (command.equals("rId")) {
            int id = Integer.parseInt(parts[1]);
            if (!removeIdAt(node, id)) {
                return "Failed to remove: No such employee with ID " + id + " or no records in salary grade " + salary;
            }
            if (node.getRecords().isEmpty()) {
                tree.deleteNode(salary);
            }
            return "Successfully removed: Employee with ID " + id + " in salary grade " + salary;
        }
        // Department removal is restricted to the salary specified in this command.
        if (command.equals("rDept")) {
            String department = parts[1];
            if (!stripDepartment(node, department)) {
                return "Failed to remove: No employees from " + department + " in salary grade " + salary;
            }
            if (node.getRecords().isEmpty()) {
                tree.deleteNode(salary);
            }
            return "Successfully removed: Employees from " + department + " in salary grade " + salary;
        }
        throw new IllegalArgumentException("Invalid command");
    }

    // Only this entry point owns prompts and the single console scanner.
    public static void main(String[] args) {
        SMS system = new SMS();
        try {
            system.loadEmployees("employeeList.txt");
        // A load failure is diagnostic only; it must not look like a successful command.
        } catch (IOException | IllegalArgumentException exception) {
            System.err.println("Cannot load employeeList.txt: " + exception.getMessage());
            return;
        }
        // Exactly one input Scanner, constructed outside the command loop.
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.print("Input a command: ");
            System.out.flush();
            // End-of-file terminates cleanly without trying to parse another command.
            if (!input.hasNextLine()) {
                break;
            }
            String line = input.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            // Print exactly one response; internal AVL changes never print status text.
            System.out.println(system.execute(line));
            if (line.equals("e")) {
                break;
            }
        }
    }
}
