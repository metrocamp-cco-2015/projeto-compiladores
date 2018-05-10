/**
 * Andre Henrique Pereira
 * Ednaldo Leite Junior
 * Erik Ricardo Balthazar
 * Jean Carlos Guinami Frias
 * Leticia Machado
 * Vitor Matheus Reis Marcelo
 */
package src.main.java.implementations;

import src.main.java.utils.ErrorHandler;
import src.main.java.utils.TabSimbolos;
import src.main.java.utils.TokenType;

import java.io.IOException;

public class Sintatico {

	private Lexico lexico;
	private Token token;

	/**
	 * Realiza o processamento do arquivo ate encontrar um token EOF.
	 * Logo em seguida, imprime o relatorio de erros e a tabela de
	 * Simbolos.
	 *
	 * @throws IOException
	 */
	public void processar() throws IOException {
		System.out.println("Iniciando processamento do arquivo...\n\n\n");
		
		initProgram();
		
		/*while(token.getTokenType() != TokenType.EOF){
			System.out.println(token.asString());
			token = lexico.nextToken();
		}*/

		// Imprime relatorio de erros
		ErrorHandler.getInstance().showErrors();

		// Imprime Tabela de Simbolos
		TabSimbolos.getInstance().printTabSimb();
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
	 * @throws IOException
	 */
	private void initProgram() throws IOException {
		token = lexico.nextToken();
		
		if(token.getTokenType().equals(TokenType.PROGRAM)) {
			lastInitProgram();
		}else {
			//TODO lança erro por nao possuir o 'programa'
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEstá faltando o 'programa'");
			lastInitProgram();
		}
	}

	/**
	 * Verifica a sintaxe depois do token PROGRAM, ou a falta dele 
	 * @throws IOException
	 */
	private void lastInitProgram() throws IOException {
		if(token.getTokenType().equals(TokenType.ID)) {
			token = lexico.nextToken();
			if(token.getTokenType().equals(TokenType.TERM)) {
				//TODO processa BLOCO
				endProgram();
			}else {
				//TODO lança erro por nao possuir o ';'
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEstá faltando um ';'");
				//TODO processa BLOCO
				
				endProgram();
			}
		}else {
			token = lexico.nextToken();
			if(token.getTokenType().equals(TokenType.ID)) {
				token = lexico.nextToken();
				if(token.getTokenType().equals(TokenType.TERM)) {
					//TODO processa BLOCO
					endProgram();
				}else {
					//TODO lança erro por nao possuir o ';'
					System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEstá faltando um ';'");
					//TODO processa BLOCO
					
					endProgram();
				}
			}else {
				//TODO lança erro por nao possuir um ID
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEstá faltando o identificador do programa");
				//TODO processa BLOCO
				
				endProgram();
			}
		}
	}

	/**
	 * Verifica a sintaxe depois do BLOCO ser processado, verificando se o final do programa esta certo
	 * @throws IOException
	 */
	private void endProgram() throws IOException {
		Token token;
		token = lexico.nextToken();
		if(token.getTokenType().equals(TokenType.END_PROG)) {
			token = lexico.nextToken();
			if(!token.getTokenType().equals(TokenType.TERM)) {
				//TODO lança erro por nao possuir o token term
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEstá faltando um ';'");
			}
		}else {
			//TODO lança erro por nao possuir o token end_prog 
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEstá faltando o 'fimprog'");
		}
	}
	
	
}
