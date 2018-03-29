package src.main.java.implementations;

import src.main.java.utils.TokenType;

public class Token {
	
	private TokenType tokenType;

	// Texto encontrado no codigo processado que equivale ao token
	private String lexema;

	private long linha;
	private long coluna;

	public Token(final TokenType tokenType, final String lexema, final long linha, final long coluna){
	    this.tokenType = tokenType;
	    this.lexema = lexema;
	    this.linha = linha;
	    this.coluna = coluna;
    }

    public Token() {
	    //do nothing
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public long getLinha() {
        return linha;
    }

    public void setLinha(long linha) {
        this.linha = linha;
    }

    public long getColuna() {
        return coluna;
    }

    public void setColuna(long coluna) {
        this.coluna = coluna;
    }

    public void printToken(){
	    System.out.print("tokenType: " + this.tokenType.toString() + ", ");
        System.out.print("lexema: " + this.lexema + ", ");
        System.out.print("linha: " + this.linha + ", ");
        System.out.print("coluna: " + this.coluna);
    }
}
