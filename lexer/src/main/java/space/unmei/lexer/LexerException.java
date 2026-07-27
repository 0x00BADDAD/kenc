package space.unmei.lexer;


public class LexerException extends Exception {
    private final int line;
    private final int column;

    public LexerException(String message, int line, int column) {
        super(message);
        this.line = line;
        this.column = column;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }
}

