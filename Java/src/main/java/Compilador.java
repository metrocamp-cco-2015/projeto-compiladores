package src.main.java;

import java.util.Arrays;

public class Compilador {

	public static void main(String[] args) throws Exception {
		//testa se parâmetro foi passado (nome de arquivo a ser processado)
			//Sintatico s = new Sintatico(args[0])
			//s.processsar();
		if(args.length <= 0){
			throw new Exception("USAGE ERROR");
		} else {
			
			Arrays.asList(args).forEach((p) -> System.out.println(p));
		}
	}

}
