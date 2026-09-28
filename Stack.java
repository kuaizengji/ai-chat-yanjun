/* Requirement:
	(1) UML of Stack(03 Stacks.pptx, page 9):
		|-----------------------------------------------|
		|                    Stack                      |
		|-----------------------------------------------|
		|   - values: Double[]                          |
		|   - top: int                                  |
		|-----------------------------------------------|
		|   + Stack(int size)                           |
		|   + isEmpty(): boolean                        |
		|   + isFull(): boolean                         |
		|   + top(): Double                             |
		|   + push(double x): Double                    |
		|   + pop(): Double                             |
		|   + displayStack(): void                      |
		|-----------------------------------------------|
	
	(2) You are NOT allowed to modify the code originally given in Stack.java
	(3) You are NOT allowed to modify the filename of Stack.java
	(4) You are NOT allowed to use Chinese characters in codes or comments
	(5)	You should add some comments and indentations to make the codes user friendly
	(6) Following is sample output after running main() in Stack.java:
	
		true
		The stack has 2 items:
		top -->	|	  5.0000	|
				|	 -3.0000	|
				+---------------+
		The stack has 4 items:
		top -->	|	  2.0000	|
				|	  1.0000	|
				|	  5.0000	|
				|	 -3.0000	|
				+---------------+
		The top is: 2.0
		true
		The stack is empty:
		top -->	+---------------+
		
	*/
public class Stack {

    private int top;
    private Double[] values;

    public Stack(int size) {
        values = new Double[size];
        top = -1;
    }

    public boolean isEmpty() {
        // top range [0, values.length)
        return top == -1;
    }

    public boolean isFull() {
        // top range [0, values.length)
        return top == values.length - 1;
    }

    public double top() {
        // return the top value
        return values[top];
    }

    public Double push(double x) {
        if (isFull())
            return null; // if full, cannot push
        values[++top] = Double.valueOf(x);
        return top();
    }

    public Double pop() {
        if (isEmpty())
            return null; // if empty, cannot pop
        return values[top--];
    }

    public void displayStack() {
        System.out.print("top -->");
        // iterate this stack
        for (int i = top; i >= 0; i--)
            // Display the values in the stack
            System.out.printf("\t|  %8.4f\t|\n", values[i]);
        System.out.println("\t+---------------+");
    }

    // begin Programming Assignment 1

    /**
     * Checks whether a postfix expression is valid.
     * Tokens are separated by commas, as in "11,1,3,*,/".
     * A space-separated expression is also accepted when it contains no comma.
     *
     * @param postfix the postfix expression.
     * @return 0 if the expression is valid, 1 otherwise.
     */
    public static int isValidPostfix(String postfix) {
        // 0 means the expression is valid, 1 means it is not
        return postfixShape(postfix) == 1 ? 0 : 1;
    }

    // returns 1 when the tokens form one postfix value, 0 otherwise
    private static int postfixShape(String postfix) {
        String[] tokens = tokenize(postfix);
        if (tokens.length == 0) {
            return 0; // empty input is not a valid expression
        }
        int depth = 0;
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            if (isNumber(token)) {
                depth++; // an operand increases the stack depth
            } else if (isOperator(token)) {
                // an operator consumes two operands and pushes one result
                if (depth < 2) {
                    return 0;
                }
                depth--;
            } else {
                return 0; // unknown token
            }
        }
        // exactly one value must remain
        return depth == 1 ? 1 : 0;
    }

    /**
     * Evaluates a postfix expression.
     * Arithmetic is done with double values, and only the final result is
     * converted to int. Returns -1 when the expression is invalid.
     *
     * @param postfix the postfix expression.
     * @return the integer result, or -1 when the expression is invalid.
     */
    public static int computePostfix(String postfix) {
        if (postfixShape(postfix) == 0) {
            return -1;
        }
        // assume the length of postfix will not exceed 50
        String[] tokens = tokenize(postfix);
        Stack stack = new Stack(Math.max(50, tokens.length));
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            if (isNumber(token)) {
                stack.push(Double.parseDouble(token));
            } else {
                // get top 2 value in stack
                Double b = stack.pop();
                Double a = stack.pop();
                if (a == null || b == null) {
                    return -1;
                }
                double result = 0.00;
                // Perform the corresponding arithmetic operation.
                switch (token) {
                    case "+":
                        result = a + b;
                        break;
                    case "-":
                        result = a - b;
                        break;
                    case "*":
                        result = a * b;
                        break;
                    case "/":
                        if (b == 0) {
                            return -1; // Prevent division by zero
                        }
                        result = a / b;
                        break;
                    default:
                        return -1;
                }
                // push the result
                stack.push(result);
            }
        }
        if (stack.isEmpty()) {
            return -1;
        }
        // the top is the ans
        double value = stack.pop();
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return -1;
        }
        return (int) value;
    }

    // split by ',' when the expression uses commas, otherwise by whitespace
    private static String[] tokenize(String postfix) {
        if (postfix == null) {
            return new String[0];
        }
        String trimmed = postfix.trim();
        if (trimmed.isEmpty()) {
            return new String[0];
        }
        String[] raw;
        if (trimmed.indexOf(',') >= 0) {
            raw = trimmed.split(",");
        } else {
            raw = trimmed.split("\\s+");
        }
        String[] tokens = new String[raw.length];
        for (int i = 0; i < raw.length; i++) {
            tokens[i] = raw[i].trim();
        }
        // A compact single-digit expression such as "53+" has no comma or space.
        if (tokens.length == 1 && !isNumber(tokens[0]) && !isOperator(tokens[0])) {
            String compact = tokens[0];
            String[] chars = new String[compact.length()];
            for (int i = 0; i < compact.length(); i++) {
                chars[i] = String.valueOf(compact.charAt(i));
            }
            return chars;
        }
        return tokens;
    }

    // true when token is an integer or a decimal, with an optional sign
    private static boolean isNumber(String token) {
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

    // true when token is one of the four arithmetic operators
    private static boolean isOperator(String token) {
        return "+".equals(token) || "-".equals(token) || "*".equals(token) || "/".equals(token);
    }

    // end Programming Assignment 1

    public static void main(String[] args) {
        Stack myStack = new Stack(4);
        System.out.println(myStack.isEmpty());
        myStack.push(-3);
        myStack.push(5);
        System.out.println("The stack has 2 items:");
        myStack.displayStack();
        myStack.push(1);
        myStack.push(2);
        myStack.push(-1);
        System.out.println("The stack has 4 items:");
        myStack.displayStack();
        System.out.println("The top is: " + myStack.top());
        System.out.println(myStack.isFull());
        myStack.pop();
        myStack.pop();
        myStack.pop();
        myStack.pop();
        System.out.println("The stack is empty:");
        myStack.displayStack();
    }

}
