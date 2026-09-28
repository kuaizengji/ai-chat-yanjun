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

    private boolean isSpace(char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r' || c == '\f';
    }

    private boolean isOperatorChar(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || c == '%'
                || c == '$' || c == 'x' || c == 'X' || c == '\u00d7' || c == '\u00f7';
    }

    private boolean isOperatorToken(String token) {
        return token != null && token.length() == 1 && isOperatorChar(token.charAt(0));
    }

    private boolean isNumberToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        int i = 0;
        if (token.charAt(0) == '+' || token.charAt(0) == '-') {
            if (token.length() == 1) {
                return false;
            }
            i++;
        }
        boolean digit = false;
        boolean dot = false;
        for (; i < token.length(); i++) {
            char c = token.charAt(i);
            if (c >= '0' && c <= '9') {
                digit = true;
            } else if (c == '.' && !dot) {
                dot = true;
            } else {
                return false;
            }
        }
        return digit;
    }

    private boolean fitsInInt(String token) {
        if (token.indexOf('.') >= 0) {
            return true;
        }
        try {
            long value = Long.parseLong(token);
            return value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean containsDecimal(String[] tokens) {
        if (tokens == null) {
            return false;
        }
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] != null && tokens[i].indexOf('.') >= 0) {
                return true;
            }
        }
        return false;
    }

    private boolean containsMultiDigit(String[] tokens) {
        if (tokens == null) {
            return false;
        }
        for (int i = 0; i < tokens.length; i++) {
            if (!isNumberToken(tokens[i])) {
                continue;
            }
            int digits = 0;
            String token = tokens[i];
            for (int j = 0; j < token.length(); j++) {
                if (token.charAt(j) >= '0' && token.charAt(j) <= '9') {
                    digits++;
                }
            }
            if (digits >= 2) {
                return true;
            }
        }
        return false;
    }

    private boolean isStructurallyValid(String[] tokens) {
        if (tokens == null || tokens.length == 0) {
            return false;
        }
        int depth = 0;
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            if (isNumberToken(token)) {
                if (!fitsInInt(token)) {
                    return false;
                }
                depth++;
            } else if (isOperatorToken(token)) {
                if (depth < 2) {
                    return false;
                }
                depth--;
            } else {
                return false;
            }
        }
        return depth == 1;
    }

    private String[] scanTokens(String s) {
        String[] buf = new String[s.length()];
        int n = 0;
        int i = 0;
        while (i < s.length()) {
            char c = s.charAt(i);
            if (isSpace(c)) {
                i++;
                continue;
            }
            boolean nextIsNumber = i + 1 < s.length()
                    && (Character.isDigit(s.charAt(i + 1)) || s.charAt(i + 1) == '.');
            boolean sign = (c == '+' || c == '-') && nextIsNumber
                    && (i == 0 || isSpace(s.charAt(i - 1)) || isOperatorChar(s.charAt(i - 1)));
            if (Character.isDigit(c) || c == '.' || sign) {
                int start = i;
                if (sign) {
                    i++;
                }
                boolean digit = false;
                boolean dot = false;
                while (i < s.length()) {
                    char d = s.charAt(i);
                    if (Character.isDigit(d)) {
                        digit = true;
                        i++;
                    } else if (d == '.' && !dot) {
                        dot = true;
                        i++;
                    } else {
                        break;
                    }
                }
                if (!digit) {
                    buf[n++] = String.valueOf(c);
                    i = start + 1;
                    continue;
                }
                buf[n++] = s.substring(start, i);
            } else if (isOperatorChar(c)) {
                buf[n++] = String.valueOf(c);
                i++;
            } else {
                buf[n++] = String.valueOf(c);
                i++;
            }
        }
        String[] tokens = new String[n];
        for (int k = 0; k < n; k++) {
            tokens[k] = buf[k];
        }
        return tokens;
    }

    private String[] singleDigitTokens(String s) {
        StringBuilder compact = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (!isSpace(s.charAt(i))) {
                compact.append(s.charAt(i));
            }
        }
        String[] tokens = new String[compact.length()];
        for (int i = 0; i < compact.length(); i++) {
            tokens[i] = String.valueOf(compact.charAt(i));
        }
        return tokens;
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
        if (trimmed.length() >= 2
                && ((trimmed.charAt(0) == '"' && trimmed.charAt(trimmed.length() - 1) == '"')
                || (trimmed.charAt(0) == '\'' && trimmed.charAt(trimmed.length() - 1) == '\''))) {
            trimmed = trimmed.substring(1, trimmed.length() - 1).trim();
        }
        if (trimmed.isEmpty()) {
            return new String[0];
        }
        String normalized = trimmed.replace(',', ' ').replace(';', ' ');
        int end = normalized.length();
        while (end > 0) {
            char tail = normalized.charAt(end - 1);
            if (isSpace(tail) || tail == '=' || tail == '#' || tail == '\uFF1D') {
                end--;
            } else {
                break;
            }
        }
        normalized = normalized.substring(0, end).trim();
        if (normalized.isEmpty()) {
            return new String[0];
        }
        if (isNumberToken(normalized) && normalized.indexOf(' ') < 0
                && normalized.indexOf('\t') < 0) {
            return new String[] { normalized };
        }

        // Check if the expression contains whitespace (indicates separate tokens).
        // Split by whitespace; this also captures negative numbers like "-3".
        // No whitespace: treat each character as a separate token.
        // This handles the common single-digit continuous format.
        String[] greedy = scanTokens(normalized);
        boolean spaced = false;
        for (int i = 0; i < normalized.length(); i++) {
            if (isSpace(normalized.charAt(i))) {
                spaced = true;
                break;
            }
        }
        if (isStructurallyValid(greedy)
                && (spaced || containsMultiDigit(greedy) || containsDecimal(greedy))) {
            return greedy;
        }
        String[] single = singleDigitTokens(normalized);
        if (isStructurallyValid(single)) {
            return single;
        }
        return greedy;
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
        int depth = 0;
        
        try {
            for (String token : tokens) {
                if (isNumberToken(token)) {
                    // If the token is a number (including negative), push it onto the stack.
                    if (!fitsInInt(token)) {
                        return 0;
                    }
                    depth++;
                } else if (isOperatorToken(token)) {
                    // If the token is an operator, ensure there are at least two operands.
                    if (depth < 2) {
                        return 0; // Invalid: not enough operands for the operator
                    }
                    depth--; // Push a placeholder result
                } else {
                    return 0; // Invalid character encountered
                }
            }
        } catch (RuntimeException e) {
            return 0;
        }
        
        // A valid postfix expression should result in exactly one value on the stack.
        return (depth == 1) ? 1 : 0;
    }

    private Integer evalInt(String[] tokens) {
        int[] stack = new int[tokens.length];
        int topIndex = -1;
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            if (isNumberToken(token)) {
                stack[++topIndex] = Integer.parseInt(token);
            } else {
                int operand2 = stack[topIndex--]; // The second operand is popped first
                int operand1 = stack[topIndex--]; // The first operand is popped second
                int result = 0;
                // Perform the corresponding arithmetic operation.
                char op = token.charAt(0);
                if (op == '+') {
                    result = operand1 + operand2;
                } else if (op == '-') {
                    result = operand1 - operand2;
                } else if (op == '*' || op == 'x' || op == 'X' || op == '\u00d7') {
                    result = operand1 * operand2;
                } else if (op == '/' || op == '\u00f7') {
                    if (operand2 == 0) {
                        return null; // Prevent division by zero
                    }
                    result = operand1 / operand2;
                } else if (op == '%') {
                    if (operand2 == 0) {
                        return null; // Prevent division by zero
                    }
                    result = operand1 % operand2;
                } else if (op == '^' || op == '$') {
                    if (operand2 < 0) {
                        return null;
                    }
                    result = 1;
                    for (int p = 0; p < operand2; p++) {
                        result *= operand1;
                    }
                }
                stack[++topIndex] = result;
            }
        }
        return stack[topIndex]; // The final result is the only element left
    }

    private Double evalDouble(String[] tokens) {
        double[] stack = new double[tokens.length];
        int topIndex = -1;
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            if (isNumberToken(token)) {
                stack[++topIndex] = Double.parseDouble(token);
            } else {
                double operand2 = stack[topIndex--];
                double operand1 = stack[topIndex--];
                double result = 0;
                char op = token.charAt(0);
                if (op == '+') {
                    result = operand1 + operand2;
                } else if (op == '-') {
                    result = operand1 - operand2;
                } else if (op == '*' || op == 'x' || op == 'X' || op == '\u00d7') {
                    result = operand1 * operand2;
                } else if (op == '/' || op == '\u00f7') {
                    if (operand2 == 0) {
                        return null;
                    }
                    result = operand1 / operand2;
                } else if (op == '%') {
                    if (operand2 == 0) {
                        return null;
                    }
                    result = operand1 % operand2;
                } else if (op == '^' || op == '$') {
                    if (operand2 < 0) {
                        return null;
                    }
                    result = Math.pow(operand1, operand2);
                }
                stack[++topIndex] = result;
            }
        }
        if (Double.isNaN(stack[topIndex]) || Double.isInfinite(stack[topIndex])) {
            return null;
        }
        return stack[topIndex];
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
        try {
            if (containsDecimal(tokens)) {
                Double value = evalDouble(tokens);
                if (value == null) {
                    return -1; // Prevent division by zero
                }
                return (int) value.doubleValue(); // The final result is the only element left
            }
            Integer value = evalInt(tokens);
            if (value == null) {
                return -1; // Prevent division by zero
            }
            return value.intValue(); // The final result is the only element left
        } catch (RuntimeException e) {
            return -1;
        }
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