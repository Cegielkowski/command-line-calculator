package pkg;

public class Token {

    public final String content;
    public Type type;

    /**
     * Constructor
     *
     * @param str
     */
    public Token(String str) {
        content = str;

        defineType();
    }

    private void defineType() {
        if (isInteger()) {
            type = Type.NUM;
        } else if (isAddition()) {
            type = Type.ADD;
        } else if (isSubtraction()) {
            type = Type.SUBTRACT;
        } else if (isMultiplication()) {
            type = Type.MULTIPLY;
        } else if (isDivision()) {
            type = Type.DIVIDE;
        } else if (isOpenBracket()) {
            type = Type.OPEN_BRACKET;
        } else if (isClosedBracket()) {
            type = Type.CLOSE_BRACKET;
        } else {
            type = Type.NULL;
        }
    }

    public Type getType() {
        return type;
    }

    private boolean isInteger() {
        try {
            Double.parseDouble(content);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    private boolean isAddition() {
        return content.equals("+");
    }

    private boolean isSubtraction() {
        return content.equals("-");
    }

    private boolean isMultiplication() {
        return content.equals("*");
    }

    private boolean isDivision() {
        return content.equals("/");
    }

    private boolean isOpenBracket() {
        return content.equals("(");
    }

    private boolean isClosedBracket() {
        return content.equals(")");
    }

    /**
     * Overrides to compare by value, not reference
     *
     * @param object object (Token) to be compared to
     * @return boolean
     */
    @Override
    public boolean equals(Object object) {
        try {
            Token Token = (Token) object;
            return this.getType().equals(Token.getType()) && this.content.equals(Token.content);
        } catch (ClassCastException e) {
            return false;
        }
    }

    public enum Type {
        NULL,
        NUM,
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE,
        OPEN_BRACKET,
        CLOSE_BRACKET
    }
}
