package src.main.java.implementations;

import src.main.java.utils.ErrorHandler;
import src.main.java.utils.TabSimbolos;
import src.main.java.utils.TokenType;

import java.io.IOException;

public class Sintatico {

	private Lexico lexico;
	
	public void processar() throws IOException {
		System.out.println("Iniciando processamento do arquivo...");
		Token token = lexico.nextToken();
		while(token.getTokenType() != TokenType.EOF){
			System.out.println(token.asString());
			token = lexico.nextToken();
		}

		// Imprime relatorio de erros
		ErrorHandler.getInstance().showErrors();

		// Imprime Tabela de Simbolos
		TabSimbolos.getInstance().printTabSimb();
	}
	
	public Sintatico(final String filename){
		this.lexico = new Lexico(filename);
	}
	
}
