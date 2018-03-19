package src.main.java.implementations;

import src.main.java.utils.TokenType;

public class Token {
	
	private TokenType tokenType;

	// Texto encontrado no c�digo processado que equivale ao token
	private String lexema;

	private int linha;
	private int coluna;

	public Token(final TokenType tokenType, final String lexema, final int linha, final int coluna){

	    this.tokenType = tokenType;
	    this.lexema = lexema;
	    this.linha = linha;
	    this.coluna = coluna;
    }
}
