package src.main.java.utils;

import java.util.HashMap;
import java.util.Map;

import src.main.java.implementations.Token;

public class TabSimbolos {
	
	private static TabSimbolos instance = new TabSimbolos();
	private Map<String, Token> tabela;
	
	private TabSimbolos() {
		tabela = new HashMap<String, Token>();
		tabela.put("verdadeiro", new Token(TokenType.LOGIC_VAL, "verdadeiro", 0, 0));
		tabela.put("falso", new Token(TokenType.LOGIC_VAL, "falso", 0, 0));
		tabela.put("nao", new Token(TokenType.LOGIC_OP, "nao", 0, 0));
		tabela.put("e", new Token(TokenType.LOGIC_OP, "e", 0, 0));
		tabela.put("ou", new Token(TokenType.LOGIC_OP, "ou", 0, 0));
		tabela.put("logico", new Token(TokenType.TYPE, "logico", 0, 0));
		tabela.put("texto", new Token(TokenType.TYPE, "texto", 0, 0));
		tabela.put("num", new Token(TokenType.TYPE, "num", 0, 0));
		tabela.put("programa", new Token(TokenType.PROGRAM, "programa", 0, 0));
		tabela.put("fimprog", new Token(TokenType.END_PROG, "fimprog", 0, 0));
		tabela.put("inicio", new Token(TokenType.BEGIN, "inicio", 0, 0));
		tabela.put("end", new Token(TokenType.END, "end", 0, 0));
		tabela.put("se", new Token(TokenType.IF, "se", 0, 0));
		tabela.put("entao", new Token(TokenType.THEN, "entao", 0, 0));
		tabela.put("senao", new Token(TokenType.ELSE, "senao", 0, 0));
		tabela.put("para", new Token(TokenType.FOR, "para", 0, 0));
		tabela.put("enquanto", new Token(TokenType.WHILE, "enquanto", 0, 0));
		tabela.put("declare", new Token(TokenType.DECLARE, "declare", 0, 0));
		tabela.put("ate", new Token(TokenType.TO, "ate", 0, 0));
	}

	public static TabSimbolos getInstance() {
		return instance;
	}
	
	public Token instalaToken(TokenType tokenType, String lexema, long linha, long coluna){
		Token token = tabela.get(lexema);
		
		if(token == null){
			token = new Token(tokenType, lexema, linha, coluna);
			tabela.put(lexema, token);
		}else{
			token.setLinha(linha);
			token.setColuna(coluna);
		}
		return token;
	}
}
