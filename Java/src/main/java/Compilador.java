package src.main.java;

import src.main.java.implementations.Sintatico;

import java.util.Arrays;

public class Compilador {

	public static void main(String[] args) throws Exception {
		if(args.length <= 0){
			throw new Exception("USAGE ERROR");
		} else {
			Sintatico sintatico = new Sintatico(args[0]);
			sintatico.processar();
		}
	}

}
