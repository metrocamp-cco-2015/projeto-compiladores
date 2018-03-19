package src.main.java;

import java.util.Arrays;

public class Compilador {

	public static void main(String[] args) {
		//testa se parâmetro foi passado (nome de arquivo a ser processado)
		//args.isEmpty => Erro Usage
			//Sintatico s = new Sintatico(args[0])
			//s.processsar();
		
		Arrays.asList(args).forEach((p) -> System.out.println(p));
	}

}
