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
import src.main.java.utils.TokenType;

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
	 * Verifica a sintaxe a FVALLOG esta correta
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
	
}
