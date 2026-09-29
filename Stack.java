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
     * Checks a comma-separated postfix expression.
     * A whole number, with an optional sign, is one operand.
     * An operator must be one of + - * /.
     *
     * @param postfix the postfix expression.
     * @return true when exactly one value would remain.
     */
    public static boolean isValidPostfix(String postfix) {
        if (postfix == null) {
            return false; // missing text is not an expression
        }
        // Keep empty pieces, so a trailing comma is not valid.
        String[] tokens = postfix.trim().split(",", -1);
        int depth = 0;
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i].trim();
            if (isOperator(token)) {
                // An operator uses two values and leaves one.
                if (depth < 2) {
                    return false;
                }
                depth--;
            } else if (parseOperand(token) != null) {
                depth++; // an operand adds one value
            } else {
                return false; // unknown piece
            }
        }
        return depth == 1;
    }

    /**
     * Evaluates a comma-separated postfix expression with integer arithmetic.
     * The right operand is taken off the stack first.
     *
     * @param postfix the postfix expression.
     * @return the integer result, or Integer.MIN_VALUE when it cannot be computed.
     */
    public static int computePostfix(String postfix) {
        if (!isValidPostfix(postfix)) {
            return Integer.MIN_VALUE;
        }
        String[] tokens = postfix.trim().split(",", -1);
        // assume the length of postfix will not exceed 50
        Stack stack = new Stack(Math.max(50, tokens.length));
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i].trim();
            if (isOperator(token)) {
                // get top 2 value in stack; the first pop is the right operand
                Double right = stack.pop();
                Double left = stack.pop();
                if (left == null || right == null) {
                    return Integer.MIN_VALUE;
                }
                int a = right.intValue();
                int b = left.intValue();
                int result;
                // Perform the corresponding arithmetic operation.
                switch (token) {
                    case "+":
                        result = b + a;
                        break;
                    case "-":
                        result = b - a;
                        break;
                    case "*":
                        result = b * a;
                        break;
                    default:
                        if (a == 0) {
                            return Integer.MIN_VALUE; // division by zero
                        }
                        result = b / a;
                        break;
                }
                stack.push(result);
            } else {
                Integer operand = parseOperand(token);
                if (operand == null) {
                    return Integer.MIN_VALUE;
                }
                stack.push(operand.intValue());
            }
        }
        if (stack.isEmpty()) {
            return Integer.MIN_VALUE;
        }
        // the top is the ans
        return stack.pop().intValue();
    }

    // a whole number, ignoring surrounding spaces; null when it is not one
    private static Integer parseOperand(String token) {
        try {
            return Integer.valueOf(Integer.parseInt(token.trim()));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // true when the piece is exactly one of the four operator signs
    private static boolean isOperator(String token) {
        return "+".equals(token) || "-".equals(token)
                || "*".equals(token) || "/".equals(token);
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
