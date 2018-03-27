package src.main.java.implementations;

import src.main.java.utils.FileLoader;
import src.main.java.utils.TabSimbolos;
import src.main.java.utils.TokenType;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Lexico {

	private static final char BLANK_SPACE = ' ';
	private FileLoader fileLoader;
	private TabSimbolos tabSimbolos = TabSimbolos.getInstance();
	
	public Lexico(final String filename){
		try {
			fileLoader = new FileLoader(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Token nextToken() throws IOException {

		StringBuilder lexemaBuilder = new StringBuilder();
		char nextChar;
		long coluna;
		long linha;
		
		//Metodo substituido pelo metodo a baixo
		/*while(fileLoader.getNextChar() != BLANK_SPACE) {
			lexemaBuilder.append(fileLoader.getNextChar());
		}*/
		
		do{
			nextChar = fileLoader.getNextChar();
		}while(nextChar == BLANK_SPACE);
		
		lexemaBuilder.append(nextChar);
		coluna = fileLoader.getColumn();
		linha = fileLoader.getLine();
		
		switch (nextChar) {
			case '&':
				//TODO metodo para RELOP
				break;
			case '+':
				return addSub(lexemaBuilder, coluna, linha);
			case '-':
				return addSub(lexemaBuilder, coluna, linha);
			case '*':
				return multDiv(lexemaBuilder, coluna, linha);
			case '/':
				return multDiv(lexemaBuilder, coluna, linha);
			case '<':
				//TODO metodo ATRRIB
				break;
			case ';':
				return term(lexemaBuilder, coluna, linha);
			case '(':
				return lPar(lexemaBuilder, coluna, linha);
			case ')':
				return rPar(lexemaBuilder, coluna, linha);
			case '_':
				//TODO metodo ID
				break;
			default:
				if(Character.isDigit(nextChar)){
					//TODO metodo NUMERICO
					break;
				}else if(Character.isLetter(nextChar)){
					//TODO metodo ID
					break;
				}else{
					//TODO metodo de excecao
				}
		}
		return tabSimbolos.instalaToken(TokenType.ID ,lexemaBuilder.toString(),
				fileLoader.getLine(), fileLoader.getColumn());
	}

	private Token addSub(StringBuilder lexemaBuilder, long coluna, long linha) {
		return tabSimbolos.instalaToken(TokenType.ADDSUB ,lexemaBuilder.toString(),
				linha, coluna);
	}
	
	private Token multDiv(StringBuilder lexemaBuilder, long coluna, long linha) {
		return tabSimbolos.instalaToken(TokenType.MULTDIV ,lexemaBuilder.toString(),
				linha, coluna);
	}
	
	private Token term(StringBuilder lexemaBuilder, long coluna, long linha) {
		return tabSimbolos.instalaToken(TokenType.TERM ,lexemaBuilder.toString(),
				linha, coluna);
	}
	
	private Token lPar(StringBuilder lexemaBuilder, long coluna, long linha) {
		return tabSimbolos.instalaToken(TokenType.L_PAR ,lexemaBuilder.toString(),
				linha, coluna);
	}
	
	private Token rPar(StringBuilder lexemaBuilder, long coluna, long linha) {
		return tabSimbolos.instalaToken(TokenType.R_PAR ,lexemaBuilder.toString(),
				linha, coluna);
	}
	
	private Token relop(StringBuilder lexemaBuilder, long coluna, long linha) {
		char nextChar;
		
		try {
			nextChar = fileLoader.getNextChar();
			
			if(nextChar == '&'){
				return tabSimbolos.instalaToken(TokenType.RELOP ,lexemaBuilder.toString(),
						linha, coluna);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public void processaToken(String palavra){
		
	}
	
}
