/**
 * Andre Henrique Pereira
 * Ednaldo Leite Junior
 * Erik Ricardo Balthazar
 * Jean Carlos Guinami Frias
 * Leticia Machado
 * Vitor Matheus Reis Marcelo
 */
package src.main.java;

import src.main.java.implementations.Sintatico;

public class Compilador {

	/**
	 * Classe Main.
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		if(args.length <= 0){
			throw new Exception("USAGE ERROR");
		} else {
			System.out.println("Lendo o arquivo: " + args[0]);
			Sintatico sintatico = new Sintatico(args[0]);
			sintatico.processar();
		}
	}

}
