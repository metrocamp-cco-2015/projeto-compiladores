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

    public boolean isFirstBloco(Token token) {
        if(token.getTokenType().equals(TokenType.BEGIN)
                || token.getTokenType().equals(TokenType.DECLARE)
                || token.getTokenType().equals(TokenType.IF)
                || token.getTokenType().equals(TokenType.ID)
                || token.getTokenType().equals(TokenType.FOR)
                || token.getTokenType().equals(TokenType.WHILE)) {
            return true;
        }
        return false;
    }

    public boolean isFirstExplo(Token token) {
        if(token.getTokenType().equals(TokenType.LOGIC_VAL)
                || token.getTokenType().equals(TokenType.ID)
                || token.getTokenType().equals(TokenType.NUM_INT)
                || token.getTokenType().equals(TokenType.NUM_FLOAT)
                || token.getTokenType().equals(TokenType.L_PAR)) {
            return true;
        }
        return false;
    }

    public boolean isFirstFvallog(Token token) {
        if(token.getTokenType().equals(TokenType.LOGIC_OP)) {
            return true;
        }
        return false;
    }

    public boolean isFirstFid1(Token token) {
        if(token.getTokenType().equals(TokenType.RELOP)
                || token.getTokenType().equals(TokenType.LOGIC_OP)
                || token.getTokenType().equals(TokenType.ADDSUB)
                || token.getTokenType().equals(TokenType.MULTDIV)) {
            return true;
        }
        return false;
    }

    public boolean isFirstOpnum(Token token) {
        if(token.getTokenType().equals(TokenType.ADDSUB)
                || token.getTokenType().equals(TokenType.MULTDIV)) {
            return true;
        }
        return false;
    }

}
