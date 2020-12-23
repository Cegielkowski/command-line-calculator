package pkg;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Calculator {

    /**
     * Tokenized input to be calculated
     */
    public final ArrayList<Token> tokens;
    /**
     * Stores indexes of brackets in tokens array
     */
    public LinkedList<BracketPair> bracketPairs = new LinkedList<>();

    /**
     * Constructor
     *
     * @param p
     */
    public Calculator(Parser p) {
        tokens = p.tokens;
        mapBrackets();
    }

    /**
     * Main evaluation method
     *
     * @return double
     */
    public double eval() {

        while (bracketPairs.size() > 0) {

            BracketPair bracketPair = bracketPairs.pop();

            List<Token> expr = tokens.subList(bracketPair.indexOpened + 1, bracketPair.indexClosed);

            evalSubExpr(expr);
            tokens.remove(bracketPair.indexOpened);
            tokens.remove(bracketPair.indexOpened + 1);

            for (BracketPair bp : bracketPairs) {
                if (bp.indexOpened > bracketPair.indexClosed) {
                    bp.indexOpened -= bracketPair.getDistance();
                }
                if (bp.indexClosed > bracketPair.indexClosed) {
                    bp.indexClosed -= bracketPair.getDistance();
                }
            }

        }

        evalSubExpr(tokens);

        return Double.parseDouble(tokens.get(0).content);
    }

    /**
     * Calculates expressions without/inside of brackets
     *
     * @param expr unbracketed part of expression to be calculated
     */
    private void evalSubExpr(List<Token> expr) {
        if (expr.size() > 1) {
            int operationIndex, i1, i2;
            Operation operation;

            Token mult = new Token("*");
            Token div = new Token("/");
            Token add = new Token("+");
            Token sub = new Token("-");

            while (expr.contains(mult) || expr.contains(div)) {
                i1 = expr.indexOf(mult);
                i2 = expr.indexOf(div);
                if (i1 < i2 && i1 > 0 || i2 == -1) {
                    operationIndex = i1;
                    operation = Operation.MULTIPLICATION;
                } else {
                    operationIndex = i2;
                    operation = Operation.DIVISION;
                }

                calculate(expr, operationIndex, operation);
            }

            while (expr.contains(add) || expr.contains(sub)) {
                i1 = expr.indexOf(add);
                i2 = expr.indexOf(sub);
                if (i1 < i2 && i1 > 0 || i2 == -1) {
                    operationIndex = i1;
                    operation = Operation.ADDITION;
                } else {
                    operationIndex = i2;
                    operation = Operation.SUBTRACTION;
                }

                calculate(expr, operationIndex, operation);
            }
        }
    }

    /**
     * execute single calculation operation
     *
     * @param expr
     * @param operationIndex index of token, containing operation symbol
     * @param operation      type of operation
     */
    private void calculate(List<Token> expr, int operationIndex, Operation operation) {
        double first = Double.parseDouble(expr.get(operationIndex - 1).content);
        double second = Double.parseDouble(expr.get(operationIndex + 1).content);
        double result = 0;
        switch (operation) {
            case ADDITION:
                result = first + second;
                break;
            case SUBTRACTION:
                result = first - second;
                break;
            case MULTIPLICATION:
                result = first * second;
                break;
            case DIVISION:
                result = first / second;
                break;
        }
        expr.set(operationIndex - 1, new Token(result + ""));
        expr.remove(operationIndex);
        expr.remove(operationIndex);
    }

    /**
     * Finds all brackets in tokens array and sort them into pairs
     */
    public void mapBrackets() {
        bracketPairs = new LinkedList<>();
        int depth = 0, maxDepth = 0;
        LinkedList<BracketPair> bracketStack = new LinkedList<>();

        for (int i = 0, length = tokens.size(); i < length; i++) {
            if (tokens.get(i).getType() == Token.Type.OPEN_BRACKET) {
                depth++;
                bracketPairs
                        .add(new BracketPair(i, depth)
                        );
                bracketStack
                        .push(bracketPairs.getLast()
                        );
                if (depth > maxDepth) {
                    maxDepth = depth;
                }
            } else if (tokens.get(i).getType() == Token.Type.CLOSE_BRACKET) {
                assert bracketStack.peek() != null;
                bracketStack.peek().setIndexClosed(i);
                bracketStack.pop();
                depth--;
            }
        }

        if (maxDepth != 0) {
            bracketPairs.sort(new DepthComparator());
        }

        for (BracketPair bracketPair : bracketPairs) {
            System.out.println(bracketPair.indexOpened + ", " + bracketPair.indexClosed);
        }

    }

    public enum Operation {
        ADDITION,
        SUBTRACTION,
        DIVISION,
        MULTIPLICATION
    }

    static class BracketPair {
        /**
         * defines nesting level
         */
        final int depth;

        /**
         * index of opening bracket in tokens array
         */
        int indexOpened;

        /**
         * index of closing bracket in tokens array
         */
        int indexClosed;

        /**
         * Constructor
         *
         * @param indexOpened
         * @param depth
         */
        public BracketPair(int indexOpened, int depth) {
            this.indexOpened = indexOpened;
            this.depth = depth;
        }

        public void setIndexClosed(int position) {
            indexClosed = position;
        }

        /**
         * Gets number of tokens from opening to closing bracket
         *
         * @return int
         */
        public int getDistance() {
            return indexClosed - indexOpened + 1;
        }
    }

    static class DepthComparator implements Comparator<BracketPair> {
        @Override
        public int compare(BracketPair a, BracketPair b) {
            return Integer.compare(b.depth, a.depth);
        }
    }
}
