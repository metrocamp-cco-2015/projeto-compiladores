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

		// TODO descomentar após os testes
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
			//TODO lança erro por nao possuir o 'programa'
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEstá faltando o 'programa'");
			
			if(token.getTokenType().equals(TokenType.ID)) {
				token = lexico.nextToken();
				if(token.getTokenType().equals(TokenType.TERM)) {
					//Processa o Bloco
					procBloco();
					//Processa o final do programa
					procEndS();
				}else {
					//TODO lança erro por nao possuir o ';'
					System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEstá faltando um ';'");
					
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
				//TODO lança erro por nao possuir o ';'
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEstá faltando um ';'");
				
				lexico.resetLastToken(token);
				//Processa o Bloco
				procBloco();
				//Processa o final do programa
				procEndS();
			}
		}else {
			//TODO lança erro por nao possuir um ID
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEstá faltando o identificador do programa");
			if(!token.getTokenType().equals(TokenType.TERM)) {
				//TODO lança erro por nao possuir o ';'
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEstá faltando um ';'");
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
				//TODO lança erro por nao possuir o token term
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEstá faltando um ';'");
			}
		}else {
			//TODO lança erro por nao possuir o token end_prog 
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEstá faltando o 'fimprog'");
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
	 * Verifica a sintaxe do X	 esta correta
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
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEstá faltando o '+' ou '-'");
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
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEstá faltando um identificador, ou um número.");
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
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEstá faltando um operador (+, -, * ou /).");
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
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEstá faltando um (, ou um identificador, ou um número.");
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
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEstá faltando um (, ou um identificador, ou um número.");
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
	
}
