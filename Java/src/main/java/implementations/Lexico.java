/**
 * Andre Henrique Pereira
 * Ednaldo Leite Junior
 * Erik Ricardo Balthazar
 * Jean Carlos Guinami Frias
 * Leticia Machado
 * Vitor Matheus Reis Marcelo
 */
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

	private static final String INVALID_TOKEN_ERROR = "Token invalido";
	private static final String UNEXPECTED_ERROR = "Erro inesperado";
	//TODO incluir strings especificas para cada erro
	/**
	 * Construtor do Lexico.
	 *
	 * @param filename
	 */
	public Lexico(final String filename) {
		try {
			fileLoader = new FileLoader(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Le o proximo token atraves do FileLoader. A partir do caracter lido, e
	 * realizada uma verificacao de seu conteudo e aplicada a regra cabivel.
	 *
	 * @return
	 * @throws IOException
	 */
	public Token nextToken() throws IOException {

		StringBuilder lexemaBuilder = new StringBuilder();
		char nextChar;
		long col;
		long line;
		Token token = null;
		
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

	/**
	 * Instala um token do tipo ADDSUB na tabela de simbolos.
	 *
	 * @param lexemaBuilder
	 * @param col
	 * @param line
	 * @return
	 */
	private Token addSub(StringBuilder lexemaBuilder, long col, long line) {
		return tabSimbolos.instalaToken(TokenType.ADDSUB, lexemaBuilder.toString(), line, col);
	}

	/**
	 * Instala um token do tipo MULTIDIV na tabela de simbolos.
	 *
	 * @param lexemaBuilder
	 * @param col
	 * @param line
	 * @return
	 */
	private Token multDiv(StringBuilder lexemaBuilder, long col, long line) {
		return tabSimbolos.instalaToken(TokenType.MULTDIV, lexemaBuilder.toString(), line, col);
	}

	/**
	 * Instala um token do tipo TERM na tabela de simbolos.
	 *
	 * @param lexemaBuilder
	 * @param col
	 * @param line
	 * @return
	 */
	private Token term(StringBuilder lexemaBuilder, long col, long line) {
		return tabSimbolos.instalaToken(TokenType.TERM, lexemaBuilder.toString(), line, col);
	}

	/**
	 * Instala um token do tipo L_PAR na tabela de simbolos.
	 *
	 * @param lexemaBuilder
	 * @param col
	 * @param line
	 * @return
	 */
	private Token lPar(StringBuilder lexemaBuilder, long col, long line) {
		return tabSimbolos.instalaToken(TokenType.L_PAR, lexemaBuilder.toString(), line, col);
	}

	/**
	 * Instala um token do tipo R_PAR na tabela de simbolos.
	 *
	 * @param lexemaBuilder
	 * @param col
	 * @param line
	 * @return
	 */
	private Token rPar(StringBuilder lexemaBuilder, long col, long line) {
		return tabSimbolos.instalaToken(TokenType.R_PAR, lexemaBuilder.toString(), line, col);
	}

	/**
	 * Verifica se o conteudo do lexema pertence ao conjunto RELOP:
	 * - &>&
	 * - &<&
	 * - &=&
	 * - &<=&
	 * - &>=&
	 * - &<>&
	 *
	 * Em caso positivo, instala um token do tipo ADDSUB na tabela de simbolos.
	 *
	 * @param lexemaBuilder
	 * @param col
	 * @param line
	 * @return
	 * @throws IOException 
	 */
	private Token relop(StringBuilder lexemaBuilder, long col, long line) throws IOException {
		Token token = null;
		
		try {
			char nextChar;
			nextChar = fileLoader.getNextChar();
			lexemaBuilder.append(nextChar);

				try {
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
				}catch (EOFException e) {
					errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_TOKEN_ERROR, col, line));
				}

			if (nextChar == '&') {
				token = tabSimbolos.instalaToken(TokenType.RELOP, lexemaBuilder.toString(), line, col);
			} else {
				errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_TOKEN_ERROR, col, line));
				token = this.nextToken();
			}
		} catch (EOFException e) {
			errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_TOKEN_ERROR, col, line));
			token = this.nextToken();
		} catch (IOException e) {
			errorHandler.addError(new Error(lexemaBuilder.toString(), UNEXPECTED_ERROR, col, line));
			token = this.nextToken();
		}
		return token;
	}

	/**
	 * Verifica se o conteudo do lexema pertence ao conjunto ATTRIB:
	 * - <-
	 *
	 * Em caso positivo, instala um token do tipo ATTRIB na tabela de simbolos.
	 *
	 * @param lexemaBuilder
	 * @param col
	 * @param line
	 * @return
	 * @throws IOException
	 */
	private Token atrrib(StringBuilder lexemaBuilder, long col, long line) throws IOException {
		Token token = null;
		
		try {
			char nextChar;
			nextChar = fileLoader.getNextChar();
			lexemaBuilder.append(nextChar);

			if (nextChar == '<') {
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

	/**
	 * Verifica se o conteudo do lexema lido pertence ao conjunto ID.
	 * Em caso positivo, instala um token do tipo ID na tabela de simbolos.
	 *
	 * @param lexemaBuilder
	 * @param col
	 * @param line
	 * @return
	 * @throws IOException
	 */
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
				if ((Character.isLetter(nextChar) && nextChar != '&') || Character.isDigit(nextChar) || nextChar == '_') {
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

	/**
	 * Verifica se o conteudo do lexema lido pertence ao conjunto de comentarios.
	 * Em caso positivo, ignora o lexema.
	 *
	 * @param lexemaBuilder
	 * @param col
	 * @param line
	 * @return
	 * @throws IOException
	 */
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

	/**
	 * Verifica se o conteudo do lexema lido pertence ao conjunto LITERAL.
	 * Em caso positivo, instala um token do tipo LITERAL na tabela de simbolos.
	 *
	 * @param lexemaBuilder
	 * @param col
	 * @param line
	 * @return
	 * @throws IOException
	 */
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
		} catch (EOFException e) {
			errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_TOKEN_ERROR, col, line));
			token = this.nextToken();
		} catch (IOException e) {
			errorHandler.addError(new Error(lexemaBuilder.toString(), UNEXPECTED_ERROR, col, line));
			token = this.nextToken();
		}

		return token;
	}

	/**
	 * Verifica se o conteudo do lexema lido pertence ao conjunto NUM_INT.
	 * Em caso positivo, instala um token do tipo NUM_INT na tabela de simbolos.
	 *
	 * @param lexemaBuilder
	 * @param col
	 * @param line
	 * @return
	 * @throws IOException
	 */
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
		} catch (EOFException e) {
			token = tabSimbolos.instalaToken(TokenType.NUM_INT, lexemaBuilder.toString(), line, col);
		} catch (IOException e) {
			errorHandler.addError(new Error(lexemaBuilder.toString(), UNEXPECTED_ERROR, col, line));
			token = this.nextToken();
		}

		return token;
	}

	/**
	 * Verifica se o conteudo do lexema lido pertence ao conjunto NUM_FLOAT.
	 * Em caso positivo, instala um token do tipo NUM_FLOAT na tabela de simbolos.
	 *
	 * @param lexemaBuilder
	 * @param col
	 * @param line
	 * @return
	 * @throws IOException
	 */
	private Token numericFloat(StringBuilder lexemaBuilder, long col, long line) throws EOFException, IOException {
		Token token = null;
		char nextChar;
		nextChar = fileLoader.getNextChar();
		lexemaBuilder.append(nextChar);

		if (Character.isDigit(nextChar)) {
			try {
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
			} catch (EOFException e) {
				token = tabSimbolos.instalaToken(TokenType.NUM_FLOAT, lexemaBuilder.toString(), line, col);
			}
		} else {
			errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_TOKEN_ERROR, col, line));
			token = this.nextToken();
		}
		return token;
	}
}
