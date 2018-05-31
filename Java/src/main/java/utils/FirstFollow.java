package src.main.java.utils;

import src.main.java.implementations.Token;

public class FirstFollow {

    public boolean isFirstVal(Token token) {
        if(token.getTokenType().equals(TokenType.ID)
                || token.getTokenType().equals(TokenType.NUM_INT)
                || token.getTokenType().equals(TokenType.NUM_FLOAT)) {
         return true;
        }
        return false;
    }

    public boolean isFirstExpnum(Token token) {
        if(token.getTokenType().equals(TokenType.ID)
                || token.getTokenType().equals(TokenType.NUM_INT)
                || token.getTokenType().equals(TokenType.NUM_FLOAT)
                || token.getTokenType().equals(TokenType.L_PAR)) {
            return true;
        }
        return false;
    }

    public boolean isFirstXexpnum(Token token) {
        if(token.getTokenType().equals(TokenType.ADDSUB)
                || token.getTokenType().equals(TokenType.MULTDIV)) {
            return true;
        }
        return false;
    }

}
