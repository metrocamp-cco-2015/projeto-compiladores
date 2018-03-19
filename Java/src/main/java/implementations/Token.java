package src.main.java.implementations;

import src.main.java.utils.TokenType;

public class Token {
	
	private TokenType tokenType;

	// Texto encontrado no codigo processado que equivale ao token
	private String lexema;

	private int linha;
	private int coluna;

	public Token(final TokenType tokenType, final String lexema, final int linha, final int coluna){
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

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    public int getColuna() {
        return coluna;
    }

    public void setColuna(int coluna) {
        this.coluna = coluna;
    }

    public void printToken(){
	    //TODO implementar metodo para printar o token
    }
}
