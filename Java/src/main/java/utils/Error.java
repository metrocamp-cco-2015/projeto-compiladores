package src.main.java.utils;

public class Error {

    private String lexema;
    private String message;
    private long col;
    private long line;

    public Error(String lexema, String message, long col, long line) {
        super();
        this.lexema = lexema;
        this.message = message;
        this.col = col;
        this.line = line;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getLine() {
        return line;
    }

    public void setLine(long line) {
        this.line = line;
    }

    public long getCol() {
        return col;
    }

    public void setCol(long col) {
        this.col = col;
    }
}
