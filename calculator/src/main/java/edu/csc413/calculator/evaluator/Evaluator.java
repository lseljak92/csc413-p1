package edu.csc413.calculator.evaluator;
import edu.csc413.calculator.operators.Operator;

import java.util.Stack;
import java.util.StringTokenizer;

public class Evaluator {
    private Stack<Operand> operandStack;
    private Stack<Operator> operatorStack;
    private StringTokenizer tokenizer;
    //Add parenthesis as delimiters
    private static final String DELIMITERS = "+-*^/() ";

    public Evaluator() {
        operandStack = new Stack<>();
        operatorStack = new Stack<>();
    }

    public int eval(String expression) {
        String token;

        // The 3rd argument is true to indicate that the delimiters should be used
        // as tokens, too. But, we'll need to remember to filter out spaces.
        this.tokenizer = new StringTokenizer(expression, DELIMITERS, true);


        while (this.tokenizer.hasMoreTokens()) {
            // filter out spaces
            if (!(token = this.tokenizer.nextToken()).equals(" ")) {
                // check if token is an operand
                if (Operand.check(token)) {
                    operandStack.push(new Operand(token));
                } else {
                    if (!Operator.check(token)) {
                        System.out.println("*****invalid token******");
                        throw new RuntimeException("*****invalid token******");
                    }

                    Operator newOperator = Operator.getOperator(token);


                    // Following logic flow from algorithm http://csis.pace.edu/~murthy/ProgrammingProblems/16_Evaluation_of_infix_expressions
                    // If newOperator is not a left parenthesis and if stack is not empty
                    if (!operatorStack.isEmpty() && newOperator.priority() != -1) {
                        if (newOperator.priority() == 0) {
                            while (operatorStack.peek().priority() != -1) {
                                calculate();
                            }
                            // Top of stack equals "(". Pop it from stack and ignore.
                            operatorStack.pop();
                        } else {
                            while (operatorStack.peek().priority() >= newOperator.priority()) {
                                calculate();
                                // After execution, if stack is empty, exit while loop to avoid EmptyStackException
                                if (operatorStack.isEmpty())
                                    break;
                            }
                            // Add newOperator to the stack
                            operatorStack.push(newOperator);
                        }
                    }
                    // If newOperator is a left parenthesis or if the stack is empty, add it to the stack
                    else {
                        operatorStack.push(newOperator);
                    }
                }
            }
        }

        // Iterate through stack until empty
        emptyStack();

        // Return the top value of operand stack and pop it from the stack
        return operandStack.pop().getValue();
    }
    // Private method to calculate expressions.
    // Pop operator from operator stack, pop two values from operand stack.
    // Finally execute operation and push result to operand stack
    private void calculate(){
        Operator oldOpr = operatorStack.pop();
        Operand op2 = operandStack.pop();
        Operand op1 = operandStack.pop();
        operandStack.push(oldOpr.execute(op1, op2));
    }
    // Method that processes the operator stack until empty
    private void emptyStack(){
        while(!operatorStack.isEmpty()){
            calculate();
        }
    }
}


