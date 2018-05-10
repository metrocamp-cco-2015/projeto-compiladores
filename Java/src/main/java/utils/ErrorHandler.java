/**
 * Andre Henrique Pereira
 * Ednaldo Leite Junior
 * Erik Ricardo Balthazar
 * Jean Carlos Guinami Frias
 * Leticia Machado
 * Vitor Matheus Reis Marcelo
 */
package src.main.java.utils;

import java.util.ArrayList;

public class ErrorHandler {

    private static ErrorHandler instance = new ErrorHandler();
    private ArrayList<Error> errors = new ArrayList<>();

    /**
     * Recupera a instancia do ErroHandler.
     *
     * @return
     */
    public static ErrorHandler getInstance() {
        return ErrorHandler.instance;
    }

    /**
     * Recebe um objeto do tipo Error e o inclui na lista de erros
     * do ErrorHandler.
     *
     * @param error
     */
    public void addError(Error error) {
        ErrorHandler.getInstance().errors.add(error);
    }

    /**
     * Caso a lista de erros nao esteja vazia, imprime cada um dos
     * itens no console.
     */
    public void showErrors(){
        if (!errors.isEmpty()) {

            System.out.println("\n\nRelatorio de Erros");

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
