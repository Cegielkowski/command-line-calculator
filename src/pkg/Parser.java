package pkg;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Parser {

    /**
     * Tokenized input to be calculated
     */
    public final ArrayList<Token> tokens = new ArrayList<>();

    /**
     * Constructor
     *
     * @param str string to be parsed
     */
    public Parser(String str) {
        StringTokenizer st = new StringTokenizer(str, "+-*/()", true);

        while (st.hasMoreTokens()) {
            tokens.add(new Token(st.nextToken()));
        }
    }
}


