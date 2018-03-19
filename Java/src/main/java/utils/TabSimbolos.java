package src.main.java.utils;

import java.util.HashMap;
import java.util.Map;

import src.main.java.implementations.Token;

public class TabSimbolos {
	
	private static TabSimbolos instance = new TabSimbolos();
	private Map<String, Token> tabela;
	
	private TabSimbolos() {
		tabela = new HashMap<String, Token>();
//		tabela.put("for", new Token(TokenType.FOR, "for", 0, 0));
	}

	public static TabSimbolos getInstance() {
		return instance;
	}

	public static void setInstance(TabSimbolos instance) {
		TabSimbolos.instance = instance;
	}
	
	public Token instalaToken(String lexema, int linha, int col){
		Token tk = tabela.get(lexema);
		
		if(tk == null){
			//tk = new Token(TokenType.ID, lexema, linha, col);
			tabela.put(lexema, tk);
		}else{
//			tk.setLinha(linha);
//			tk.setCol(col);
		}
		return tk;
	}
}
