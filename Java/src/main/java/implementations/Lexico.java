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
		
		//Metodo substituido pelo metodo a baixo
		/*while(fileLoader.getNextChar() != BLANK_SPACE) {
			lexemaBuilder.append(fileLoader.getNextChar());
		}*/
		
		do{
			nextChar = fileLoader.getNextChar();
		}while(nextChar == BLANK_SPACE);
		
		lexemaBuilder.append(nextChar);
		
		switch (nextChar) {
			case '&':
				//TODO metodo para RELOP
				break;
			case '+':
				//TODO metodo ADD
				break;
			case '-':
				//TODO metodo SUB
				break;
			case '*':
				//TODO metodo MULT
				break;
			case '/':
				//TODO metodo DIV
				break;
			case '<':
				//TODO metodo ATRRIB
				break;
			case ';':
				//TODO metodo TERM
				break;
			case '(':
				//TODO metodo L_PAR
				break;
			case ')':
				//TODO metodo R_PAR
				break;
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
	
	public void processaToken(String palavra){
		
	}
	
}
