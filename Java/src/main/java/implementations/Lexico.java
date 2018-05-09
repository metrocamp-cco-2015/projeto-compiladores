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

	private static final String INVALID_TOKEN_ERROR = "Token inv�lido";
	private static final String UNEXPECTED_ERROR = "Erro inesperado";

<<<<<<< Updated upstream
=======
	// Mensagens de erro para processar RELOP
	private static final String INVALID_RELOP_CONTENT_ERROR = "Conteúdo de operador RELOP invalido";
	private static final String INVALID_RELOP_ENDING_CHARACTER_ERROR = "Finalizacao do operador RELOP invalida";

	// Mensagens de erro para processar ATTRIB
	private static final String INVALID_ATTRIB_ENDING_CHARACTER_ERROR = "Finalizacao do operador ATTRIB invalida";

	// Mensagens de erro para processar ID
	private static final String INVALID_UNDERSCORE_FOLLOW_CHARACTER_ERROR = "ID iniciado com '_' deve ser seguido de um caracter";

	// Mensagens de erro para processar COMMENT
	private static final String INVALID_COMMENT_FOLLOW_CHARACTER_ERROR = "O inicio de um comentario deve ser seguido de '{'";
	private static final String INVALID_COMMENT_ENDING_CHARACTER_ERROR = "Finalizacao de comentario invalida";

	// Mensagens de erro para processar numeros
	private static final String INVALID_NUMERIC_NOTATION_ERROR = "Formato de notacao numerica invalido";
	private static final String INVALID_LITERAL_IN_NUMBER_ERROR = "Valor literal invalido dentro do valor numerico";
	
	// Buffer de tokens, utilizado para armazenar tokens processados, mas nao utilizados pelo Sintatico
	private Token buffer;

	/**
	 * Construtor do Lexico.
	 *
	 * @param filename
	 */
>>>>>>> Stashed changes
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
		Token token = null;
		
		if(this.buffer != null) {
			token = this.buffer;
			this.buffer = null;
			return token;
		}
		
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
				token = relop(lexemaBuilder, col, line);
				break;
			case '+':
			case '-':
				token =  addSub(lexemaBuilder, col, line);
				break;
			case '*':
			case '/':
				token =  multDiv(lexemaBuilder, col, line);
				break;
			case '\'':
				token =  literal(lexemaBuilder, col, line);
				break;
			case '#':
				token =  comment(lexemaBuilder, col, line);
				break;
			case '<':
				token =  atrrib(lexemaBuilder, col, line);
				break;
			case ';':
				token =  term(lexemaBuilder, col, line);
				break;
			case '(':
				token =  lPar(lexemaBuilder, col, line);
				break;
			case ')':
				token =  rPar(lexemaBuilder, col, line);
				break;
			case '_':
				token =  id(lexemaBuilder, col, line);
				break;
			default:
				if(Character.isDigit(nextChar)){
					token =  numeric(lexemaBuilder, col, line);
				}else if(Character.isLetter(nextChar)){
					token =  id(lexemaBuilder, col, line);
				}else{
					errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_TOKEN_ERROR, col, line));
					token = this.nextToken();
				}
		}
		return token;
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
		Token token = null;
		
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
				return this.nextToken();
			}

			if (nextChar == '&') {
				token = tabSimbolos.instalaToken(TokenType.RELOP, lexemaBuilder.toString(), line, col);
			} else {
				errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_TOKEN_ERROR, col, line));
				token = this.nextToken();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return token;
	}

	private Token atrrib(StringBuilder lexemaBuilder, long col, long line) throws IOException {
		Token token = null;
		
		try {
			char nextChar;
			nextChar = fileLoader.getNextChar();
			lexemaBuilder.append(nextChar);

			if (nextChar == '-') {
				token = tabSimbolos.instalaToken(TokenType.ATTRIB, lexemaBuilder.toString(), line, col);
			} else {
				errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_TOKEN_ERROR, col, line));
				token = this.nextToken();
			}
		} catch (IOException e) {
			errorHandler.addError(new Error(lexemaBuilder.toString(), UNEXPECTED_ERROR, col, line));
			token = this.nextToken();
		}
		return token;
	}

	private Token id(StringBuilder lexemaBuilder, long col, long line) throws IOException {
		Token token = null;
		try {
			char nextChar;
			boolean isId = true;
			
			if(lexemaBuilder.toString().equals("_")) {
				nextChar = fileLoader.getNextChar();
				lexemaBuilder.append(nextChar);
				
				if(!Character.isLetter(nextChar)) {
					errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_TOKEN_ERROR, col, line));
					return this.nextToken();
				}
			}

			nextChar = fileLoader.getNextChar();
			lexemaBuilder.append(nextChar);
			
			while (isId) {
				if ((Character.isLetter(nextChar) && nextChar != '&') || Character.isDigit(nextChar)) {
					nextChar = fileLoader.getNextChar();
					lexemaBuilder.append(nextChar);
				} else {
					isId = false;
					lexemaBuilder.deleteCharAt(lexemaBuilder.length() - 1);
					fileLoader.resetLastChar();
				}
			}

			token = tabSimbolos.instalaToken(TokenType.ID, lexemaBuilder.toString(), line, col);

			
		} catch (EOFException e) {
			token = tabSimbolos.instalaToken(TokenType.ID, lexemaBuilder.toString(), line, col);
		} catch (IOException e) {
			errorHandler.addError(new Error(lexemaBuilder.toString(), UNEXPECTED_ERROR, col, line));
			token = this.nextToken();
		} 
		return token;
	}

	private Token comment(StringBuilder lexemaBuilder, long col, long line) throws IOException {
		Token token = null;
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
					token = this.nextToken();
				}
			} else {
				errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_TOKEN_ERROR, col, line));
				token = this.nextToken();
			}
		} catch (IOException e) {
			errorHandler.addError(new Error(lexemaBuilder.toString(), UNEXPECTED_ERROR, col, line));
			token = this.nextToken();
		}

		return token;
	}

	private Token literal(StringBuilder lexemaBuilder, long col, long line) throws IOException {
		Token token = null;
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

			token = tabSimbolos.instalaToken(TokenType.LITERAL, lexemaBuilder.toString(), line, col);
		} catch (IOException e) {
			errorHandler.addError(new Error(lexemaBuilder.toString(), UNEXPECTED_ERROR, col, line));
			token = this.nextToken();
		}

		return token;
	}

	private Token numeric(StringBuilder lexemaBuilder, long col, long line) throws IOException {
		Token token = null;
		try {
			char nextChar;
			nextChar = fileLoader.getNextChar();
			lexemaBuilder.append(nextChar);

			while (Character.isDigit(nextChar)) {
				nextChar = fileLoader.getNextChar();
				lexemaBuilder.append(nextChar);
			}

			if (nextChar == '.') {
				token = numericFloat(lexemaBuilder, col, line);
			} else if (nextChar == 'e' || nextChar == 'E') {
				nextChar = fileLoader.getNextChar();
				lexemaBuilder.append(nextChar);

				if (nextChar == '+') {
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
						token = tabSimbolos.instalaToken(TokenType.NUM_INT, lexemaBuilder.toString(), line, col);
					} else {
						errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_TOKEN_ERROR, col, line));
						token = this.nextToken();
					}
				} else {
					errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_TOKEN_ERROR, col, line));
					token = this.nextToken();
				}
			} else {
				fileLoader.resetLastChar();
				lexemaBuilder.deleteCharAt((lexemaBuilder.length() - 1));

				token = tabSimbolos.instalaToken(TokenType.NUM_INT, lexemaBuilder.toString(), line, col);
			}
		} catch (IOException e) {
			errorHandler.addError(new Error(lexemaBuilder.toString(), UNEXPECTED_ERROR, col, line));
			token = this.nextToken();
		}

		return token;
	}

	private Token numericFloat(StringBuilder lexemaBuilder, long col, long line) throws EOFException, IOException {
		Token token = null;
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

				if (nextChar == '+') {
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
						token = tabSimbolos.instalaToken(TokenType.NUM_FLOAT, lexemaBuilder.toString(), line, col);
					} else {
						errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_TOKEN_ERROR, col, line));
						token = this.nextToken();
					}
				} else {
					errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_TOKEN_ERROR, col, line));
					token = this.nextToken();
				}
			} else {
				fileLoader.resetLastChar();
				lexemaBuilder.deleteCharAt((lexemaBuilder.length() - 1));
				token = tabSimbolos.instalaToken(TokenType.NUM_FLOAT, lexemaBuilder.toString(), line, col);
			}
		} else {
			errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_TOKEN_ERROR, col, line));
			token = this.nextToken();
		}
		return token;
	}

	/**
	 * Reset do ultimo Token, armazenando o token no buffer do Lexico. 
	 * 
	 * @param token
	 * 		- Token que sera resetado e armazenado no buffer
	 */
	public void resetLastToken(Token token) {
		this.buffer = token;
	}
}
