package src.main.java.implementations;

import src.main.java.utils.FileLoader;
import src.main.java.utils.TabSimbolos;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Lexico {

	private static final char BLANK_SPACE = ' ';
	private FileLoader fileLoader;
	private TabSimbolos tabSimbolos = TabSimbolos.getInstance();
	
	public Lexico(final String filename){
		try {
			fileLoader = new FileLoader(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Token nextToken() throws IOException {

		StringBuilder lexemaBuilder = new StringBuilder();

		while(fileLoader.getNextChar() != BLANK_SPACE) {
			lexemaBuilder.append(fileLoader.getNextChar());
		}

		return tabSimbolos.instalaToken(lexemaBuilder.toString(),
				fileLoader.getLine(), fileLoader.getColumn());

	}
	
}
