package src.main.java.implementations;

import src.main.java.utils.Error;
import src.main.java.utils.ErrorHandler;
import src.main.java.utils.FileLoader;
import src.main.java.utils.TabSimbolos;
import src.main.java.utils.TokenType;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Lexico {

	private FileLoader fileLoader;
	private TabSimbolos tabSimbolos = TabSimbolos.getInstance();
	private ErrorHandler errorHandler = ErrorHandler.getInstance();

	private static final String INVALID_TOKEN_ERROR = "Token inválido";
	private static final String UNEXPECTED_ERROR = "Erro inesperado";

	public Lexico(final String filename) {
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
			try{
				nextChar = fileLoader.getNextChar();
			} catch(EOFException e) {
				return new Token(TokenType.EOF, "", 0, 0);
			}
			
		}while(Character.isWhitespace(nextChar));
		
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
					errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_TOKEN_ERROR, col, line));
					this.nextToken();
				}
		}
		return tabSimbolos.instalaToken(TokenType.ID ,lexemaBuilder.toString(),
				fileLoader.getLine(), fileLoader.getColumn());
	}

	private Token addSub(StringBuilder lexemaBuilder, long col, long line) {
		return tabSimbolos.instalaToken(TokenType.ADDSUB, lexemaBuilder.toString(), line, col);
	}

	private Token multDiv(StringBuilder lexemaBuilder, long col, long line) {
		return tabSimbolos.instalaToken(TokenType.MULTDIV, lexemaBuilder.toString(), line, col);
	}

	private Token term(StringBuilder lexemaBuilder, long col, long line) {
		return tabSimbolos.instalaToken(TokenType.TERM, lexemaBuilder.toString(), line, col);
	}

	private Token lPar(StringBuilder lexemaBuilder, long col, long line) {
		return tabSimbolos.instalaToken(TokenType.L_PAR, lexemaBuilder.toString(), line, col);
	}

	private Token rPar(StringBuilder lexemaBuilder, long col, long line) {
		return tabSimbolos.instalaToken(TokenType.R_PAR, lexemaBuilder.toString(), line, col);
	}

	private Token relop(StringBuilder lexemaBuilder, long col, long line) {
		try {
			char nextChar;
			nextChar = fileLoader.getNextChar();
			lexemaBuilder.append(nextChar);

			switch (nextChar) {
			case '<':
				nextChar = fileLoader.getNextChar();
				lexemaBuilder.append(nextChar);

				if (nextChar == '=' || nextChar == '>') {
					nextChar = fileLoader.getNextChar();
					lexemaBuilder.append(nextChar);
				}
				break;
			case '>':
				nextChar = fileLoader.getNextChar();
				lexemaBuilder.append(nextChar);

				if (nextChar == '=') {
					nextChar = fileLoader.getNextChar();
					lexemaBuilder.append(nextChar);
				}
				break;
			case '=':
				nextChar = fileLoader.getNextChar();
				lexemaBuilder.append(nextChar);
				break;
			default:
				errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_TOKEN_ERROR, col, line));
				this.nextToken();
				break;
			}

			if (nextChar == '&') {
				return tabSimbolos.instalaToken(TokenType.RELOP, lexemaBuilder.toString(), line, col);
			} else {
				errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_TOKEN_ERROR, col, line));
				this.nextToken();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Token atrrib(StringBuilder lexemaBuilder, long col, long line) {
		try {
			char nextChar;
			nextChar = fileLoader.getNextChar();
			lexemaBuilder.append(nextChar);

			if (nextChar == '-') {
				return tabSimbolos.instalaToken(TokenType.TERM, lexemaBuilder.toString(), line, col);
			} else {
				errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_TOKEN_ERROR, col, line));
				this.nextToken();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Token id(StringBuilder lexemaBuilder, long col, long line) throws IOException {
		try {
			char nextChar;
			boolean isId = true;

			nextChar = fileLoader.getNextChar();
			lexemaBuilder.append(nextChar);

			if (Character.isLetter(nextChar) && nextChar != '&') {
				nextChar = fileLoader.getNextChar();
				lexemaBuilder.append(nextChar);
			} else {
				errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_TOKEN_ERROR, col, line));
				this.nextToken();
			}

			while (isId) {
				if ((Character.isLetter(nextChar) && nextChar != '&') || Character.isDigit(nextChar)) {
					lexemaBuilder.append(nextChar);
					nextChar = fileLoader.getNextChar();
				} else {
					isId = false;
					fileLoader.resetLastChar();
				}
			}

			return tabSimbolos.instalaToken(TokenType.ID, lexemaBuilder.toString(), line, col);

		} catch (IOException e) {
			errorHandler.addError(new Error(lexemaBuilder.toString(), UNEXPECTED_ERROR, col, line));
			this.nextToken();
			e.printStackTrace();
		}
		return null;
	}

	private Token comment(StringBuilder lexemaBuilder, long col, long line) throws IOException {
		try {
			char nextChar;
			nextChar = fileLoader.getNextChar();

			if (nextChar == '{') {
				do {
					nextChar = fileLoader.getNextChar();
				} while (nextChar != '}');

				nextChar = fileLoader.getNextChar();
				if (nextChar == '#') {
					return nextToken();
				} else {
					errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_TOKEN_ERROR, col, line));
					this.nextToken();
				}
			} else {
				errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_TOKEN_ERROR, col, line));
				this.nextToken();
			}
		} catch (IOException e) {
			errorHandler.addError(new Error(lexemaBuilder.toString(), UNEXPECTED_ERROR, col, line));
			this.nextToken();
			e.printStackTrace();
		}

		return null;
	}

	private Token literal(StringBuilder lexemaBuilder, long col, long line) throws IOException {
		try {
			char nextChar;
			boolean isLiteral = true;

			while (isLiteral) {
				nextChar = fileLoader.getNextChar();
				lexemaBuilder.append(nextChar);

				if (nextChar == '\'') {
					isLiteral = false;
				}
			}

			return tabSimbolos.instalaToken(TokenType.LITERAL, lexemaBuilder.toString(), line, col);
		} catch (IOException e) {
			errorHandler.addError(new Error(lexemaBuilder.toString(), UNEXPECTED_ERROR, col, line));
			this.nextToken();
			e.printStackTrace();
		}

		return null;
	}

	private Token numeric(StringBuilder lexemaBuilder, long col, long line) throws IOException {
		try {
			char nextChar;
			nextChar = fileLoader.getNextChar();
			lexemaBuilder.append(nextChar);

			while (Character.isDigit(nextChar)) {
				nextChar = fileLoader.getNextChar();
				lexemaBuilder.append(nextChar);
			}

			if (nextChar == '.') {
				return numericFloat(lexemaBuilder, col, line);
			} else if (nextChar == 'e' || nextChar == 'E') {
				nextChar = fileLoader.getNextChar();
				lexemaBuilder.append(nextChar);

				if (nextChar == '+' || nextChar == '-') {
					nextChar = fileLoader.getNextChar();
					lexemaBuilder.append(nextChar);

					if (Character.isDigit(nextChar)) {
						while (true) {
							nextChar = fileLoader.getNextChar();
							if (!Character.isDigit(nextChar)) {
								break;
							}
							lexemaBuilder.append(nextChar);
						}
						return tabSimbolos.instalaToken(TokenType.NUM_INT, lexemaBuilder.toString(), line, col);
					} else {
						errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_TOKEN_ERROR, col, line));
						this.nextToken();
					}
				} else {
					errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_TOKEN_ERROR, col, line));
					this.nextToken();
				}
			} else {
				fileLoader.resetLastChar();
				lexemaBuilder.deleteCharAt((lexemaBuilder.length() - 1));

				return tabSimbolos.instalaToken(TokenType.NUM_INT, lexemaBuilder.toString(), line, col);
			}
		} catch (IOException e) {
			errorHandler.addError(new Error(lexemaBuilder.toString(), UNEXPECTED_ERROR, col, line));
			this.nextToken();
			e.printStackTrace();
		}

		return null;
	}

	private Token numericFloat(StringBuilder lexemaBuilder, long col, long line) throws EOFException, IOException {
		char nextChar;
		nextChar = fileLoader.getNextChar();
		lexemaBuilder.append(nextChar);

		if (Character.isDigit(nextChar)) {
			do {
				nextChar = fileLoader.getNextChar();
				lexemaBuilder.append(nextChar);
			} while (Character.isDigit(nextChar));

			if (nextChar == 'e' || nextChar == 'E') {
				nextChar = fileLoader.getNextChar();
				lexemaBuilder.append(nextChar);

				if (nextChar == '+' || nextChar == '-') {
					nextChar = fileLoader.getNextChar();
					lexemaBuilder.append(nextChar);

					if (Character.isDigit(nextChar)) {
						while (true) {
							nextChar = fileLoader.getNextChar();
							if (!Character.isDigit(nextChar)) {
								break;
							}
							lexemaBuilder.append(nextChar);
						}
						return tabSimbolos.instalaToken(TokenType.NUM_INT, lexemaBuilder.toString(), line, col);
					} else {
						errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_TOKEN_ERROR, col, line));
						this.nextToken();
					}
				} else {
					errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_TOKEN_ERROR, col, line));
					this.nextToken();
				}
			} else {
				fileLoader.resetLastChar();
				lexemaBuilder.deleteCharAt((lexemaBuilder.length() - 1));
				return tabSimbolos.instalaToken(TokenType.NUM_FLOAT, lexemaBuilder.toString(), line, col);
			}
		} else {
			errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_TOKEN_ERROR, col, line));
			this.nextToken();
		}
		return null;
	}
}
