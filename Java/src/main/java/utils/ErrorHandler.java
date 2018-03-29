package src.main.java.utils;

import java.util.ArrayList;

public class ErrorHandler {

    private static ErrorHandler instance = new ErrorHandler();
    private ArrayList<Error> errors = new ArrayList<>();

    public static ErrorHandler getInstance() {
        return ErrorHandler.instance;
    }

    public void addError(Error error) {
        ErrorHandler.getInstance().errors.add(error);
    }

    public void showErrors(){
        if (!errors.isEmpty()) {

            System.out.println("Relatorio de Erros");

            errors.forEach(error -> {
                System.out.println("\nLexema: " + error.getLexema());
                System.out.println("Linha: " + error.getLine() + " ; Coluna: " + error.getCol());
                System.out.println("Mensagem: " + error.getMessage());
            });
        } else {
            System.out.println("Nao foram encontrados Erros");
        }
;
    }

}
