/**
 * Andre Henrique Pereira
 * Ednaldo Leite Junior
 * Erik Ricardo Balthazar
 * Jean Carlos Guinami Frias
 * Leticia Machado
 * Vitor Matheus Reis Marcelo
 */
package src.main.java.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirstAndFollow {

	private static FirstAndFollow instance = new FirstAndFollow();
	private Map<String, ArrayList<TokenType>> tabelaFirst;
	private Map<String, ArrayList<TokenType>> tabelaFollow;
	
	
	
	/**
	 * Cria um mapa contendo as palavras reservadas. Cada item do mapa
	 * consiste de um identificador literal da palavra reservada e um
	 * token que a representa.
	 */
	private FirstAndFollow() {
		setTabFirst();
	}

	/**
	 * Recupera a instancia da TabSimbolos.
	 *
	 * @return
	 */
	public static FirstAndFollow getInstance() {
		return instance;
	}
	
	private void  setTabFirst() {
		tabelaFirst = new HashMap<String, ArrayList<TokenType>>();
		ArrayList<TokenType> list = new ArrayList<TokenType>();
		
		//Insere first da regra 'S'
		list.add(TokenType.PROGRAM);
		tabelaFirst.put("S", list);
		
		//Limpa a lista e insere first das regras 'DECL', 'CMDS' e 'CMD'
		list.clear();
		list.add(TokenType.DECLARE);
		tabelaFirst.put("DECL", list);
		
		list.add(TokenType.IF);
		list.add(TokenType.FOR);
		list.add(TokenType.WHILE);
		list.add(TokenType.ID);
		tabelaFirst.put("CMDS", list);
		tabelaFirst.put("CMD", list);
		
		//Limpa a lista e insere first da regra 'COND'
		list.clear();
		list.add(TokenType.IF);
		tabelaFirst.put("COND", list);
		
		//Limpa a lista e insere first da regra 'CNDB'
		list.clear();
		list.add(TokenType.ELSE);
		tabelaFirst.put("CONB", list);
		
		//Limpa a lista e insere first da regra 'ATRIB'
		list.clear();
		list.add(TokenType.ID);
		tabelaFirst.put("ATRIB", list);
		
		//Limpa a lista e insere first da regra 'EXP'
		list.clear();
		list.add(TokenType.LOGIC_VAL);
		list.add(TokenType.ID);
		list.add(TokenType.NUM_INT);
		tabelaFirst.put("EXP", list);
	}
}
