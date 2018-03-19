package src.main.java.implementations;

import src.main.java.utils.ErrorHandler;
import src.main.java.utils.TabSimbolos;
import src.main.java.utils.TokenType;

public class Sintatico {

	private Lexico lexico;
	
	public void processar(){
		Token token = lexico.nextToken();
		while(token.getTokenType() != TokenType.EOF){
			token.printToken();
//			token.lexico.nextToken();
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
