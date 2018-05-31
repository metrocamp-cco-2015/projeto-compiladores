/**
 * Andre Henrique Pereira
 * Ednaldo Leite Junior
 * Erik Ricardo Balthazar
 * Jean Carlos Guinami Frias
 * Leticia Machado
 * Vitor Matheus Reis Marcelo
 */
package src.main.java.implementations;

import java.io.IOException;

import src.main.java.utils.ErrorHandler;
import src.main.java.utils.FirstFollow;
import src.main.java.utils.TokenType;

public class Sintatico {

	private Lexico lexico;
	private Token token;
	private FirstFollow firstFollow = new FirstFollow();

	/**
	 * Realiza o processamento do arquivo ate encontrar um token EOF.
	 * Logo em seguida, imprime o relatorio de erros e a tabela de
	 * Simbolos.
	 *
	 * @throws IOException
	 */
	public void processar() throws IOException {
		System.out.println("Iniciando processamento do arquivo...\n\n\n");

		try {
			procS();
		} catch (Exception e) {
			// TODO lanca erro de execucao
			e.printStackTrace();
		}

		/*while(token.getTokenType() != TokenType.EOF){
			System.out.println(token.asString());
			token = lexico.nextToken();
		}*/

		// Imprime relatorio de erros
		ErrorHandler.getInstance().showErrors();

		// TODO descomentar ap�s os testes
		// Imprime Tabela de Simbolos
		//TabSimbolos.getInstance().printTabSimb();
	}

	/**
	 * Construtor do Sintatico.
	 *
	 * @param filename
	 */
	public Sintatico(final String filename){
		this.lexico = new Lexico(filename);
	}

	/**
	 * Inicializa o processamento dos Tokens verificando a sintaxe da linguagem
	 * @throws Exception
	 */
	//TODO melhorar esse metodo
	private void procS() throws Exception {
		token = lexico.nextToken();

		if(token.getTokenType().equals(TokenType.PROGRAM)) {
			procContS();
		}else {
			//TODO lan�a erro por nao possuir o 'programa'
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando o 'programa'");

			if(token.getTokenType().equals(TokenType.ID)) {
				token = lexico.nextToken();
				if(token.getTokenType().equals(TokenType.TERM)) {
					//Processa o Bloco
					procBloco();
					//Processa o final do programa
					procEndS();
				}else {
					//TODO lan�a erro por nao possuir o ';'
					System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando um ';'");

					lexico.resetLastToken(token);
					//Processa o Bloco
					procBloco();
					//Processa o final do programa
					procEndS();
				}
			}else {
				procContS();
			}
		}
	}

	/**
	 * Continuacao do processamento do S
	 *
	 * @throws IOException
	 * @throws Exception
	 */
	private void procContS() throws IOException, Exception {
		//Processa o programa depois de ter lido o token 'PROGRAM'
		token = lexico.nextToken();
		if(token.getTokenType().equals(TokenType.ID)) {
			token = lexico.nextToken();
			if(token.getTokenType().equals(TokenType.TERM)) {
				//Processa o Bloco
				procBloco();
				//Processa o final do programa
				procEndS();
			}else {
				//TODO lan�a erro por nao possuir o ';'
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando um ';'");

				lexico.resetLastToken(token);
				//Processa o Bloco
				procBloco();
				//Processa o final do programa
				procEndS();
			}
		}else {
			//TODO lan�a erro por nao possuir um ID
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando o identificador do programa");
			if(!token.getTokenType().equals(TokenType.TERM)) {
				//TODO lan�a erro por nao possuir o ';'
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando um ';'");
				lexico.resetLastToken(token);
			}
			//Processa o Bloco
			procBloco();
			//Processa o final do programa
			procEndS();
		}
	}

	/**
	 * Verifica a sintaxe depois do BLOCO ser processado, verificando se o final do programa esta certo
	 * @throws IOException
	 */
	private void procEndS() throws IOException {
		token = lexico.nextToken();
		if(token.getTokenType().equals(TokenType.END_PROG)) {
			token = lexico.nextToken();
			if(!token.getTokenType().equals(TokenType.TERM)) {
				//TODO lan�a erro por nao possuir o token term
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando um ';'");
			}
		}else {
			//TODO lan�a erro por nao possuir o token end_prog
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando o 'fimprog'");
		}
	}

	/**
	 * Verifica se a sintaxe do BLOCO esta correta
	 * @throws Exception
	 */
	private void procBloco() throws Exception {
		token = lexico.nextToken();

		if(token.getTokenType().equals(TokenType.BEGIN)) {
			// TODO processa CMDS

			token = lexico.nextToken();
			if(!token.getTokenType().equals(TokenType.END)) {
				// TODO lanca erro por nao vir o token 'end'
			}
		}else {
			// TODO processa CMD
		}
	}

	/**
	 * Verifica a sintaxe do FVALLOG esta correta
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	//TODO remover SuppressWarnings quando finalizar
	private void procFvallog() throws IOException {
		token = lexico.nextToken();
		if(token.getTokenType().equals(TokenType.LOGIC_OP)) {
			//TODO processa o procExplo
		}else {
			lexico.resetLastToken(token);
		}
	}

	/**
	 * Verifica a sintaxe do XEXPNUM esta correta
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	//TODO remover SuppressWarnings quando finalizar
	private void procXexpnum() throws IOException {
		token = lexico.nextToken();

		if(token.getTokenType().equals(TokenType.ADDSUB)) {
			lexico.resetLastToken(token);
			procOpnum();
		}else {
			lexico.resetLastToken(token);
		}
	}

	/**
	 * Verifica a sintaxe do OPNUM esta correta
	 * @throws IOException
	 */
	private void procOpnum() throws IOException {
		token = lexico.nextToken();

		if(token.getTokenType().equals(TokenType.ADDSUB)) {

		}else {
			//TODO lanca erro por nao possuir um sinal de + ou de -
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando o '+' ou '-'");
		}
	}

	/**
	 * Verifica a sintaxe do VAL esta correta
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	// TODO remover SuppressWarnings quando finalizar
	private void procVal() throws IOException {
		token = lexico.nextToken();

		if(!token.getTokenType().equals(TokenType.ID)
				|| !token.getTokenType().equals(TokenType.NUM_INT)
				|| !token.getTokenType().equals(TokenType.NUM_FLOAT)) {

			//TODO lanca erro por nao possuir um ID, NUM_INT ou NUM_FLOAT
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando um identificador, ou um n�mero.");
		}
	}

	private void procFnumInt() throws IOException {
		token = lexico.nextToken();

		if(token.getTokenType().equals(TokenType.ADDSUB)
			|| token.getTokenType().equals(TokenType.MULTDIV)) {

			lexico.resetLastToken(token);
			procOpnum();
			procFopnum_1();

		}else{
			//TODO lanca erro por nao possuir um ADDSUB ou MULTDIV
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando um operador (+, -, * ou /).");
		}

	}

	private void procFopnum_1() throws IOException {
		token = lexico.nextToken();

		if(token.getTokenType().equals(TokenType.L_PAR)
			|| token.getTokenType().equals(TokenType.ID)
			|| token.getTokenType().equals(TokenType.NUM_INT)
			|| token.getTokenType().equals(TokenType.NUM_FLOAT)) {

			lexico.resetLastToken(token);
			//TODO processa EXPNUM
			//TODO processa FEXPNUM_2

		}else{
			//TODO lanca erro por nao possuir um L_PAR, ID, NUM_INT ou NUM_FLOAT
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando um (, ou um identificador, ou um n�mero.");
		}
	}

	private void procFexpnum_2() throws IOException{
		token = lexico.nextToken();

		if(token.getTokenType().equals(TokenType.RELOP)) {

			if(token.getTokenType().equals(TokenType.L_PAR)
				||token.getTokenType().equals(TokenType.ID)
				||token.getTokenType().equals(TokenType.NUM_INT)
				||token.getTokenType().equals(TokenType.NUM_FLOAT)) {
					//TODO processa EXPNUM

			}else {
				//TODO lanca erro por nao possuir um L_PAR, ID, NUM_INT ou NUM_FLOAT
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando um (, ou um identificador, ou um n�mero.");
			}
		}else{
			lexico.resetLastToken(token);
		}
	}

	private void procFnumfloat() throws IOException{
		token = lexico.nextToken();

		if(token.getTokenType().equals(TokenType.ADDSUB)
			||token.getTokenType().equals(TokenType.MULTDIV)) {
			procOpnum();
		}
	}

	private void procFexpnum_3() throws IOException{
		token = lexico.nextToken();

		if(token.getTokenType().equals(TokenType.RELOP)){
			if(firstFollow.isFirstExpnum(token){
				procExpnum();
				//TODO processa EXPNUM
			}else{
				//TODO lanca erro por nao possuir um L_PAR, ID, NUM_INT ou NUM_FLOAT
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando um (, identificador, ou um n�mero.");
			}
		}else{
			lexico.resetLastToken(token);
		}
	}

	private void Flpar() throws IOException {




	}

	/**
	 * Verifica a sintaxe do REP esta correta
	 * @throws IOException
	 */
	private void procRep() throws IOException {
		token = lexico.nextToken();

		if(token.getTokenType().equals(TokenType.FOR)) {
			lexico.resetLastToken(token);
			procRepF();
		}else if (token.getTokenType().equals(TokenType.WHILE)) {
			lexico.resetLastToken(token);
			//TODO função não implementada procRepW().
			//procRepW();
		} else {
			//TODO lanca erro por não ser FOR nem WHILE
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken não é FOR nem WHILE.");
		}
	}

	/**
	 * Verifica a sintaxe do REPF esta correta
	 * @throws IOException
	 */
	private void procRepF() throws IOException {
		token = lexico.nextToken();

		if(token.getTokenType().equals(TokenType.FOR)) {
			token = lexico.nextToken();

			if(token.getTokenType().equals(TokenType.ID)) {
				token = lexico.nextToken();

				if(token.getTokenType().equals(TokenType.ATTRIB)) {
					token = lexico.nextToken();

					if(firstFollow.isFirstExpnum(token)) {
						lexico.resetLastToken(token);
						procExpnum();
						token = lexico.nextToken();

						if(token.getTokenType().equals(TokenType.TO)) {
							token = lexico.nextToken();

							if(firstFollow.isFirstExpnum(token)) {
								lexico.resetLastToken(token);
								procExpnum();
								token = lexico.nextToken();

								if(firstFollow.isFirstBloco(token)) {
									lexico.resetLastToken(token);
									//Todo Processa o Bloco
									//procBloco();
									token = lexico.nextToken();
								}
							}
						}
					}
				}
			}
		} else {
			//TODO lanca erro por não ser FOR nem WHILE
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken não é FOR nem WHILE.");
		}
	}

	/**
	 * Verifica a sintaxe do EXPNUM esta correta
	 * @throws IOException
	 */
	private void procExpnum() throws IOException {
		token = lexico.nextToken();

		if(token.getTokenType().equals(TokenType.L_PAR)) {
			lexico.resetLastToken(token);
			procVal();
			token = lexico.nextToken();

			if(firstFollow.isFirstExpnum(token)) {
				lexico.resetLastToken(token);
				procExpnum();
				token = lexico.nextToken();

				if(!token.getTokenType().equals(TokenType.R_PAR)) {
					//TODO lanca erro.
					System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken não é ')'");
				}

			}
		} else if(firstFollow.isFirstVal(token)) {
			lexico.resetLastToken(token);
			procVal();
			token = lexico.nextToken();

			if(firstFollow.isFirstXexpnum(token)) {
				lexico.resetLastToken(token);
				procXexpnum();
			}

		} else {
			//TODO lanca erro.
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken não é FOR nem WHILE.");
		}
	}

	/**
	 * Verifica a sintaxe do REPW esta correta
	 * @throws IOException
	 */
	private void procRepw() throws IOException {
		token = lexico.nextToken();

		if (token.getTokenType().equals(TokenType.WHILE)) {
			token = lexico.nextToken();

			if (token.getTokenType().equals(TokenType.L_PAR)) {
				token = lexico.nextToken();

				if (firstFollow.isFirstExplo(token)) {
					lexico.resetLastToken(token);
					procExplo();
					token = lexico.nextToken();

					if (token.getTokenType().equals(TokenType.R_PAR)) {
						token = lexico.nextToken();

						if (firstFollow.isFirstBloco(token)) {
							lexico.resetLastToken(token);
							// TODO procBloco().
							//procBloco();
						}
					}
				}
			}
		} else {
				//TODO lanca erro.
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken não é WHILE.");
		}
	}

	/**
	 * Verifica a sintaxe do EXPLO esta correta
	 * @throws IOException
	 */
	private void procExplo() throws IOException {
		token = lexico.nextToken();

		if (token.getTokenType().equals(TokenType.LOGIC_VAL)) {
			token = lexico.nextToken();

			if (firstFollow.isFirstFvallog(token)) {
				lexico.resetLastToken(token);
				procFvallog();
			}

		} else if (token.getTokenType().equals(TokenType.ID)) {
			token = lexico.nextToken();

			if (firstFollow.isFirstFid1(token)) {
				lexico.resetLastToken(token);
				// TODO processa procFid1().
				//procFid1();
			}

		} else if (token.getTokenType().equals(TokenType.NUM_INT) || token.getTokenType().equals(TokenType.NUM_FLOAT)) {
			token = lexico.nextToken();

			if (firstFollow.isFirstOpnum(token)) {
				lexico.resetLastToken(token);
				// TODO processa procOpnum().
				//procOpnum();
				token = lexico.nextToken();

				if (firstFollow.isFirstExpnum(token)) {
					lexico.resetLastToken(token);
					procExpnum();
					token = lexico.nextToken();

					if (token.getTokenType().equals(TokenType.RELOP)) {
						token = lexico.nextToken();

						if (firstFollow.isFirstExpnum(token)) {
							lexico.resetLastToken(token);
							procExpnum();
						}
					}
				}
			}
		} else {
				//TODO lanca erro
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
		}

}
}
