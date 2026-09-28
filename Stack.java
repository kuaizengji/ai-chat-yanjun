import java.util.regex.Pattern;

/**
 * The Stack class represents a last-in-first-out (LIFO) stack of Integer values.
 * It also provides utility methods to validate and compute postfix expressions.
 * 
 * @author Student
 * @version 1.0
 */
public class Stack {

    // Array to store the elements of the stack. It holds Integer objects.
    private Integer[] values;
    
    // Index of the top element. Initialized to -1 indicating an empty stack.
    private int top;

    /**
     * Constructs an empty stack with the specified maximum capacity.
     * 
     * @param size the maximum number of elements the stack can hold.
     */
    public Stack(int size) {
        values = new Integer[size];
        top = -1; // Stack is initially empty
    }

    /**
     * Checks whether the stack is currently empty.
     * 
     * @return true if the stack contains no elements, false otherwise.
     */
    public boolean isEmpty() {
        return top == -1;
    }

    /**
     * Checks whether the stack has reached its maximum capacity.
     * 
     * @return true if the stack is full, false otherwise.
     */
    public boolean isFull() {
        return top == values.length - 1;
    }

    /**
     * Retrieves the top element of the stack without removing it.
     * 
     * @return the top element as an Integer, or null if the stack is empty.
     */
    public Integer top() {
        if (isEmpty()) {
            return null; // Return null if there is no top element
        }
        return values[top];
    }

    /**
     * Pushes a new element onto the top of the stack.
     * The double value is cast to an integer for storage.
     * 
     * @param x the double value to be pushed onto the stack.
     * @return the pushed element as an Integer, or null if the stack is full.
     */
    public Integer push(double x) {
        if (isFull()) {
            return null; // Cannot push if stack is full
        }
        top++; // Move the top index upwards
        values[top] = (int) x; // Cast double to int for storage
        return values[top];
    }

    /**
     * Removes and returns the top element of the stack.
     * 
     * @return the removed element as a Double, or null if the stack is empty.
     */
    public Double pop() {
        if (isEmpty()) {
            return null; // Cannot pop from an empty stack
        }
        Double removed = values[top].doubleValue(); // Convert Integer to Double
        values[top] = null; // Clear the reference for garbage collection
        top--; // Move the top index downwards
        return removed;
    }

    /**
     * Displays the current elements of the stack in a formatted manner.
     * The top element is printed first, followed by the rest.
     */
    public void displayStack() {
        if (isEmpty()) {
            System.out.println("Stack is empty.");
            return;
        }
        System.out.println("Top --> " + values[top]);
        for (int i = top - 1; i >= 0; i--) {
            System.out.println("        " + values[i]);
        }
    }

    /**
     * Tokenizes a postfix expression into individual numbers and operators.
     * Handles both space-separated formats (e.g., "5 3 +") and continuous 
     * single-digit formats (e.g., "53+"), as well as negative numbers.
     * 
     * @param postfix the postfix expression string.
     * @return an array of string tokens representing numbers and operators.
     */
    private String[] tokenize(String postfix) {
        if (postfix == null) {
            return new String[0];
        }
        String trimmed = postfix.trim();
        if (trimmed.isEmpty()) {
            return new String[0];
        }
        if (trimmed.matches("-?\\d+")) {
            return new String[] { trimmed };
        }
        
        // Check if the expression contains whitespace (indicates separate tokens).
        if (trimmed.matches(".*\\s+.*")) {
            // Split by whitespace; this also captures negative numbers like "-3".
            return trimmed.split("\\s+");
        } else {
            // No whitespace: treat each character as a separate token.
            // This handles the common single-digit continuous format.
            String[] tokens = new String[trimmed.length()];
            for (int i = 0; i < trimmed.length(); i++) {
                tokens[i] = String.valueOf(trimmed.charAt(i));
            }
            return tokens;
        }
    }

    /**
     * Validates whether a given string is a correctly formatted postfix expression.
     * Uses a temporary local stack to avoid modifying the instance's state.
     * 
     * @param postfix the string containing the postfix expression to validate.
     * @return 1 if the expression is valid, 0 otherwise.
     */
    public int isValidPostfix(String postfix) {
        if (postfix == null || postfix.trim().isEmpty()) {
            return 0; // Empty string is not a valid expression
        }
        
        String[] tokens = tokenize(postfix);
        if (tokens.length == 0) {
            return 0;
        }
        
        // Use a local standard library stack for validation to keep the instance stack clean.
        java.util.Stack<Integer> tempStack = new java.util.Stack<>();
        
        for (String token : tokens) {
            if (token.matches("-?\\d+")) {
                // If the token is a number (including negative), push it onto the stack.
                try {
                    tempStack.push(Integer.parseInt(token));
                } catch (NumberFormatException e) {
                    return 0;
                }
            } else if (token.matches("[+\\-*/]")) {
                // If the token is an operator, ensure there are at least two operands.
                if (tempStack.size() < 2) {
                    return 0; // Invalid: not enough operands for the operator
                }
                tempStack.pop();
                tempStack.pop();
                tempStack.push(0); // Push a placeholder result
            } else {
                return 0; // Invalid character encountered
            }
        }
        
        // A valid postfix expression should result in exactly one value on the stack.
        return (tempStack.size() == 1) ? 1 : 0;
    }

    /**
     * Computes the result of a valid postfix expression.
     * Assumes the expression contains only integers and basic operators (+, -, *, /).
     * 
     * @param postfix the string containing the postfix expression to compute.
     * @return the integer result of the expression, or -1 if the expression is invalid.
     */
    public int computePostfix(String postfix) {
        if (isValidPostfix(postfix) == 0) {
            return -1; // Return -1 for invalid expressions
        }
        
        String[] tokens = tokenize(postfix);
        
        // Use a local stack to compute the result without affecting the instance stack.
        java.util.Stack<Integer> tempStack = new java.util.Stack<>();
        
        for (String token : tokens) {
            if (token.matches("-?\\d+")) {
                tempStack.push(Integer.parseInt(token));
            } else {
                int operand2 = tempStack.pop(); // The second operand is popped first
                int operand1 = tempStack.pop(); // The first operand is popped second
                int result = 0;
                
                // Perform the corresponding arithmetic operation.
                switch (token) {
                    case "+":
                        result = operand1 + operand2;
                        break;
                    case "-":
                        result = operand1 - operand2;
                        break;
                    case "*":
                        result = operand1 * operand2;
                        break;
                    case "/":
                        if (operand2 == 0) {
                            return -1; // Prevent division by zero
                        }
                        result = operand1 / operand2;
                        break;
                }
                tempStack.push(result);
            }
        }
        return tempStack.pop(); // The final result is the only element left
    }

    /**
     * The main method serves as a test driver for the Stack class.
     * It tests basic stack operations and postfix expression evaluation.
     * 
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        // Test basic Stack operations
        Stack myStack = new Stack(5);
        System.out.println("Is stack empty? " + myStack.isEmpty());
        myStack.push(10.5);
        myStack.push(20.7);
        System.out.println("Top element: " + myStack.top());
        myStack.displayStack();
        System.out.println("Popped element: " + myStack.pop());
        
        // Test postfix expression validation and computation
        String[] validExprs = {"5 3 +", "53+", "10 20 +", "3 4 5 * +", "-5 3 +"};
        String[] invalidExprs = {"5 + 3", "", "3 +"};
        
        System.out.println("\n--- Valid Expressions ---");
        for (String expr : validExprs) {
            System.out.println("Expr: \"" + expr + "\" | Valid: " 
                + myStack.isValidPostfix(expr) + " | Result: " 
                + myStack.computePostfix(expr));
        }
        
        System.out.println("\n--- Invalid Expressions ---");
        for (String expr : invalidExprs) {
            System.out.println("Expr: \"" + expr + "\" | Valid: " 
                + myStack.isValidPostfix(expr) + " | Result: " 
                + myStack.computePostfix(expr));
        }
    }
}