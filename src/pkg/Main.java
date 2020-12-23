package pkg;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        try {
            System.out.print("Type the expression ( Example: 1 + (3 * 4) ");

            Scanner in = new Scanner(System.in);

            String str = in.nextLine().replace(" ", "");

            checkInput(str);

            Parser parser = new Parser(str);

            Calculator calc = new Calculator(parser);

            System.out.print(str + " = " + calc.eval() + System.lineSeparator());
        } catch (Exception e) {
            System.out.print("Something went wrong");
        }


    }

    /**
     * If input contains unavailable tokens/symbols - returns error
     *
     * @param str input string to be checked
     */
    private static void checkInput(String str) {

        String availableExpressions = "(\\(|\\)|\\+|\\-|/|\\*|\\.|\\d+|\\s+)";

        Pattern p = Pattern.compile("^.*([^" + availableExpressions + "]).*$");

        Matcher m = p.matcher(str);
        if (m.find()) {
            System.out.println("invalid token: " + m.group());
            System.exit(65);
        }

    }
}
