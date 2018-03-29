package src.main.java.implementations;

import src.main.java.utils.FileLoader;
import src.main.java.utils.TabSimbolos;
import src.main.java.utils.TokenType;

import java.io.EOFException;
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
		long col;
		long line;
		
		do{
			nextChar = fileLoader.getNextChar();
		}while(nextChar == BLANK_SPACE);
		
		lexemaBuilder.append(nextChar);
		col = fileLoader.getColumn();
		line = fileLoader.getLine();
		
		switch (nextChar) {
			case '&':
				return relop(lexemaBuilder, col, line);
			case '+':
			case '-':
				return addSub(lexemaBuilder, col, line);
			case '*':
			case '/':
				return multDiv(lexemaBuilder, col, line);
			case '\'':
				return literal(lexemaBuilder, col, line);
			case '#':
				return comment(lexemaBuilder, col, line);
			case '<':
				return atrrib(lexemaBuilder, col, line);
			case ';':
				return term(lexemaBuilder, col, line);
			case '(':
				return lPar(lexemaBuilder, col, line);
			case ')':
				return rPar(lexemaBuilder, col, line);
			case '_':
				return id(lexemaBuilder, col, line);
			default:
				if(Character.isDigit(nextChar)){
					return numeric(lexemaBuilder, col, line);
				}else if(Character.isLetter(nextChar)){
					return id(lexemaBuilder, col, line);
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
		try {
			char nextChar;
			nextChar = fileLoader.getNextChar();
			lexemaBuilder.append(nextChar);
			
			switch (nextChar) {
				case '<':
					nextChar = fileLoader.getNextChar();
					lexemaBuilder.append(nextChar);
					
					if(nextChar == '=' || nextChar == '>'){
						nextChar = fileLoader.getNextChar();
						lexemaBuilder.append(nextChar);
					}
					break;
				case '>':
					nextChar = fileLoader.getNextChar();
					lexemaBuilder.append(nextChar);
					
					if(nextChar == '='){
						nextChar = fileLoader.getNextChar();
						lexemaBuilder.append(nextChar);
					}
					break;
				case '=':
					nextChar = fileLoader.getNextChar();
					lexemaBuilder.append(nextChar);
					break;
			default:
				//TODO throw exception
				break;
			}
			
			if(nextChar == '&'){
				return tabSimbolos.instalaToken(TokenType.RELOP ,lexemaBuilder.toString(),
						linha, coluna);
			}else{
				//TODO throw exception
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Token atrrib(StringBuilder lexemaBuilder, long coluna, long linha) {
		try {
			char nextChar;
			nextChar = fileLoader.getNextChar();
			lexemaBuilder.append(nextChar);
			
			if(nextChar == '-'){
				return tabSimbolos.instalaToken(TokenType.TERM ,lexemaBuilder.toString(),
						linha, coluna);
			}else{
				//TODO throw exception
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Token id(StringBuilder lexemaBuilder, long coluna, long linha) {
		try {
			char nextChar;
			boolean isId = true;
			
			nextChar = fileLoader.getNextChar();
			lexemaBuilder.append(nextChar);
			
			if(Character.isLetter(nextChar) && nextChar != '&'){
				nextChar = fileLoader.getNextChar();
				lexemaBuilder.append(nextChar);
			}else{
				// TODO throw exception
			}
			
			while(isId){
				if(Character.isLetter(nextChar) || Character.isDigit(nextChar) || nextChar != '&'){
					nextChar = fileLoader.getNextChar();
					lexemaBuilder.append(nextChar);
				}else{
					isId = false;
					fileLoader.resetLastChar();
				}
			}
			
			return tabSimbolos.instalaToken(TokenType.ID ,lexemaBuilder.toString(),
					linha, coluna);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private Token comment(StringBuilder lexemaBuilder, long coluna, long linha) {
		try {
			char nextChar;
			nextChar = fileLoader.getNextChar();
			
			if(nextChar == '{'){
				do{
					nextChar = fileLoader.getNextChar();
				}while(nextChar != '}');
				
				nextChar = fileLoader.getNextChar();
				if(nextChar == '#'){
					return nextToken();
				}else{
					//TODO throw exception
				}
			}else{
				//TODO throw exception
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	private Token literal(StringBuilder lexemaBuilder, long coluna, long linha) {
		try {
			char nextChar;
			boolean isLiteral = true;

			while(isLiteral){
				nextChar = fileLoader.getNextChar();
				lexemaBuilder.append(nextChar);
				
				if(nextChar == '\''){
					isLiteral = false;
				}
			}
			
			return tabSimbolos.instalaToken(TokenType.LITERAL ,lexemaBuilder.toString(),
					linha, coluna);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	private Token numeric(StringBuilder lexemaBuilder, long coluna, long linha) {
		try {
			char nextChar;
			nextChar = fileLoader.getNextChar();
			lexemaBuilder.append(nextChar);
			
			while(Character.isDigit(nextChar)){
				nextChar = fileLoader.getNextChar();
				lexemaBuilder.append(nextChar);
			}
			
			if(nextChar == '.'){
				return numericFloat(lexemaBuilder, coluna, linha);
			}else if(nextChar == 'e' || nextChar == 'E'){
				nextChar = fileLoader.getNextChar();
				lexemaBuilder.append(nextChar);
				
				if(nextChar == '+' || nextChar == '-'){
					nextChar = fileLoader.getNextChar();
					lexemaBuilder.append(nextChar);
					
					if(Character.isDigit(nextChar)){
						while(true){
							nextChar = fileLoader.getNextChar();
							if(!Character.isDigit(nextChar)){
								break;
							}
							lexemaBuilder.append(nextChar);
						}
						return tabSimbolos.instalaToken(TokenType.NUM_INT ,lexemaBuilder.toString(),
								linha, coluna);
					}else{
						//TODO throw exception
					}
				}else{
					//TODO throw exception
				}
			}else{
				fileLoader.resetLastChar();
				lexemaBuilder.deleteCharAt((lexemaBuilder.length() - 1));
				
				return tabSimbolos.instalaToken(TokenType.NUM_INT ,lexemaBuilder.toString(),
						linha, coluna);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	private Token numericFloat(StringBuilder lexemaBuilder, long coluna, long linha) throws EOFException, IOException {
		char nextChar;
		nextChar = fileLoader.getNextChar();
		lexemaBuilder.append(nextChar);
		
		if(Character.isDigit(nextChar)){
			do{
				nextChar = fileLoader.getNextChar();
				lexemaBuilder.append(nextChar);
			}while(Character.isDigit(nextChar));
			
			if(nextChar == 'e' || nextChar == 'E'){
				nextChar = fileLoader.getNextChar();
				lexemaBuilder.append(nextChar);
				
				if(nextChar == '+' || nextChar == '-'){
					nextChar = fileLoader.getNextChar();
					lexemaBuilder.append(nextChar);
					
					if(Character.isDigit(nextChar)){
						while(true){
							nextChar = fileLoader.getNextChar();
							if(!Character.isDigit(nextChar)){
								break;
							}
							lexemaBuilder.append(nextChar);
						}
						return tabSimbolos.instalaToken(TokenType.NUM_INT ,lexemaBuilder.toString(),
								linha, coluna);
					}else{
						//TODO throw exception
					}
				}else{
					//TODO throw exception
				}
			}else{
				fileLoader.resetLastChar();
				lexemaBuilder.deleteCharAt((lexemaBuilder.length() - 1));
				return tabSimbolos.instalaToken(TokenType.NUM_FLOAT ,lexemaBuilder.toString(),
						linha, coluna);
			}
		}else{
			//TODO throw exception
		}
		return null;
	}
}
