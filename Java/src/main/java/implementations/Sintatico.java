package src.main.java.implementations;

import src.main.java.utils.TokenType;

import java.io.IOException;

public class Sintatico {

	private Lexico lexico;
	private TabSimbolos tabSimbolos = TabSimbolos.getInstance();
	
	public void processar() throws IOException {
		Token token = lexico.nextToken();
		while(token.getTokenType() != TokenType.EOF){
			token.printToken();
<<<<<<< Updated upstream
			lexico.nextToken();
=======
//			lexico.nextToken();
>>>>>>> Stashed changes
		}
	}
	
//	// Imprime relatorio de erros
//	ErrorHandler.getInstance().errorReport();
//
//	// Imprime Tabela de Simbolos
//	TabSimbolos.getInstance().printTabSimb();
	
	public Sintatico(final String filename){
		this.lexico = new Lexico(filename);
	}
	
}
