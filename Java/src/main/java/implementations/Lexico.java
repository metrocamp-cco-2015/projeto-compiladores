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

	// Mensagens de erro gerais
	private static final String INVALID_FORMAT_TOKEN_ERROR = "O valor informado nao se encaixa nos padroes de nenhum dos tokens";
	private static final String EOF_WHILE_PROCESSING_TOKEN_ERROR = "Final do arquivo encontrado enquanto processava token";
	private static final String UNEXPECTED_ERROR = "Erro inesperado";

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

	// Mensagens de erro para processar números
	private static final String INVALID_NUMERIC_NOTATION_ERROR = "Formato de notacao numerica invalido";
	private static final String INVALID_LITERAL_IN_NUMBER_ERROR = "Valor literal invalido dentro do valor numerico";

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
					errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_FORMAT_TOKEN_ERROR, col, line));
					token = this.nextToken();
				}
		}
		return token;
	}

	/**
	 * Processa token do tipo ADDSUB.
	 * Em caso positivo, retorna um token do tipo ADDSUB na tabela de simbolos.
	 * Em caso negativo, e adicionado um novo erro e procura por um novo Token.
	 *
	 * @param lexemaBuilder
	 * @param col
	 * @param line
	 * @return
	 */
	private Token addSub(StringBuilder lexemaBuilder, long col, long line) {
		return new Token(TokenType.ADDSUB, lexemaBuilder.toString(), line, col);
	}

	/**
	 * Processa token do tipo MULTIDIV.
	 * Em caso positivo, retorna um token do tipo MULTIDIV na tabela de simbolos.
	 * Em caso negativo, e adicionado um novo erro e procura por um novo Token.
	 *
	 * @param lexemaBuilder
	 * @param col
	 * @param line
	 * @return
	 */
	private Token multDiv(StringBuilder lexemaBuilder, long col, long line) {
		return new Token(TokenType.MULTDIV, lexemaBuilder.toString(), line, col);
	}

	/**
	 * Processa token do tipo TERM.
	 * Em caso positivo, retorna um token do tipo TERM na tabela de simbolos.
	 * Em caso negativo, e adicionado um novo erro e procura por um novo Token.
	 * 
	 * @param lexemaBuilder
	 * @param col
	 * @param line
	 * @return
	 */
	private Token term(StringBuilder lexemaBuilder, long col, long line) {
		return new Token(TokenType.TERM, lexemaBuilder.toString(), line, col);
	}

	/**
	 * Processa um token do tipo L_PAR.
	 * Em caso positivo, retorna um token do tipo L_PAR.
	 * Em caso negativo, e adicionado um novo erro e procura por um novo Token.
	 *
	 * @param lexemaBuilder
	 * @param col
	 * @param line
	 * @return
	 */
	private Token lPar(StringBuilder lexemaBuilder, long col, long line) {
		return new Token(TokenType.L_PAR, lexemaBuilder.toString(), line, col);
	}

	/**
	 * Processa um token do tipo R_PAR.
	 * Em caso positivo, retorna um token do tipo R_PAR.
	 * Em caso negativo, e adicionado um novo erro e procura por um novo Token.
	 * 
	 * @param lexemaBuilder
	 * @param col
	 * @param line
	 * @return
	 */
	private Token rPar(StringBuilder lexemaBuilder, long col, long line) {
		return new Token(TokenType.R_PAR, lexemaBuilder.toString(), line, col);
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
	 * Em caso positivo, retorna um token do tipo RELOP na tabela de simbolos.
	 * Em caso negativo, e adicionado um novo erro e procura por um novo Token.
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
						errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_RELOP_CONTENT_ERROR, col, line));
						return this.nextToken();
					}
				}catch (EOFException e) {
					errorHandler.addError(new Error(lexemaBuilder.toString(), EOF_WHILE_PROCESSING_TOKEN_ERROR, col, line));
				}

			if (nextChar == '&') {
				token = new Token(TokenType.RELOP, lexemaBuilder.toString(), line, col);
			} else {
				errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_RELOP_ENDING_CHARACTER_ERROR, col, line));
				token = this.nextToken();
			}
		} catch (EOFException e) {
			errorHandler.addError(new Error(lexemaBuilder.toString(), EOF_WHILE_PROCESSING_TOKEN_ERROR, col, line));
			token = this.nextToken();
		} catch (IOException e) {
			errorHandler.addError(new Error(lexemaBuilder.toString(), UNEXPECTED_ERROR, col, line));
			token = this.nextToken();
		}
		return token;
	}

	/**
	 * Verifica se o conteudo do lexema pertence ao conjunto ATTRIB:
	 * - <<
	 *
	 * Em caso positivo, retorna um token do tipo ATTRIB.
	 * Em caso negativo, e adicionado um novo erro e procura por um novo Token.
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
				token = new Token(TokenType.ATTRIB, lexemaBuilder.toString(), line, col);
			} else {
				errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_ATTRIB_ENDING_CHARACTER_ERROR, col, line));
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
	 * Em caso negativo, e adicionado um novo erro e procura por um novo Token.
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
					errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_UNDERSCORE_FOLLOW_CHARACTER_ERROR, col, line));
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
	 * Em caso negativo, e adicionado um novo erro e procura por um novo Token.
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
					errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_COMMENT_ENDING_CHARACTER_ERROR, col, line));
					token = this.nextToken();
				}
			} else {
				errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_COMMENT_FOLLOW_CHARACTER_ERROR, col, line));
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
	 * Em caso positivo, retorna um token do tipo LITERAL.
	 * Em caso negativo, e adicionado um novo erro e procura por um novo Token.
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

			token = new Token(TokenType.LITERAL, lexemaBuilder.toString(), line, col);
		} catch (EOFException e) {
			errorHandler.addError(new Error(lexemaBuilder.toString(), EOF_WHILE_PROCESSING_TOKEN_ERROR, col, line));
			token = this.nextToken();
		} catch (IOException e) {
			errorHandler.addError(new Error(lexemaBuilder.toString(), UNEXPECTED_ERROR, col, line));
			token = this.nextToken();
		}

		return token;
	}

	/**
	 * Verifica se o conteudo do lexema lido pertence ao conjunto NUM_INT.
	 * Em caso positivo, retorna um token do tipo NUM_INT.
	 * Em caso negativo, e adicionado um novo erro e procura por um novo Token.
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

				if (nextChar == '+' || nextChar == '-' || Character.isDigit(nextChar)) {
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
						token = new Token(TokenType.NUM_INT, lexemaBuilder.toString(), line, col);
					} else {
						errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_NUMERIC_NOTATION_ERROR, col, line));
						token = this.nextToken();
					}
				} else {
					errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_NUMERIC_NOTATION_ERROR, col, line));
					token = this.nextToken();
				}
			} else {
				fileLoader.resetLastChar();
				lexemaBuilder.deleteCharAt((lexemaBuilder.length() - 1));

				token = new Token(TokenType.NUM_INT, lexemaBuilder.toString(), line, col);
			}
		} catch (EOFException e) {
			token = new Token(TokenType.NUM_INT, lexemaBuilder.toString(), line, col);
		} catch (IOException e) {
			errorHandler.addError(new Error(lexemaBuilder.toString(), UNEXPECTED_ERROR, col, line));
			token = this.nextToken();
		}

		return token;
	}

	/**
	 * Verifica se o conteudo do lexema lido pertence ao conjunto NUM_FLOAT.
	 * Em caso positivo, retorna um token do tipo NUM_FLOAT.
	 * Em caso negativo, e adicionado um novo erro e procura por um novo Token.
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
	
					if (nextChar == '+' || nextChar == '-' || Character.isDigit(nextChar)) {
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
							token = new Token(TokenType.NUM_FLOAT, lexemaBuilder.toString(), line, col);
						} else {
							errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_NUMERIC_NOTATION_ERROR, col, line));
							token = this.nextToken();
						}
					} else {
						errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_NUMERIC_NOTATION_ERROR, col, line));
						token = this.nextToken();
					}
				} else {
					fileLoader.resetLastChar();
					lexemaBuilder.deleteCharAt((lexemaBuilder.length() - 1));
					token = new Token(TokenType.NUM_FLOAT, lexemaBuilder.toString(), line, col);
				}
			} catch (EOFException e) {
				token = new Token(TokenType.NUM_FLOAT, lexemaBuilder.toString(), line, col);
			}
		} else {
			errorHandler.addError(new Error(lexemaBuilder.toString(), INVALID_LITERAL_IN_NUMBER_ERROR, col, line));
			token = this.nextToken();
		}
		return token;
	}
}
