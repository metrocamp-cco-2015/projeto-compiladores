package src.main.java.utils;

import src.main.java.implementations.Token;

public class FirstFollow {

    /**
     * Verifica se um token pertence ao conjunto FIRST de CMDS
     * @param token
     * @return
     */
    public boolean isFirstCmds(Token token) {
        if(token.getTokenType().equals(TokenType.DECLARE)
                || token.getTokenType().equals(TokenType.IF)
                || token.getTokenType().equals(TokenType.FOR)
                || token.getTokenType().equals(TokenType.WHILE)
                || token.getTokenType().equals(TokenType.ID)
                || isFollowCmds(token)) {
            return true;
        }
        return false;
    }

    /**
     * Verifica se um token pertence ao conjunto FOLLOW de CMDS
     * @param token
     * @return
     */
    private boolean isFollowCmds(Token token) {
        if(token.getTokenType().equals((TokenType.END))){
            return true;
        }
        return false;
    }

    public boolean isFirstCmd(Token token) {
        if(token.getTokenType().equals(TokenType.DECLARE)
                || token.getTokenType().equals(TokenType.IF)
                || token.getTokenType().equals(TokenType.FOR)
                || token.getTokenType().equals(TokenType.WHILE)
                || token.getTokenType().equals(TokenType.ID)) {
            return true;
        }
        return false;
    }

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

    public boolean isFirstExp(Token token) {
        if(token.getTokenType().equals(TokenType.ID)) {
            return true;
        }
        return false;
    }

    public boolean isFirstFid(Token token) {
        if(token.getTokenType().equals(TokenType.LOGIC_OP)
                || token.getTokenType().equals(TokenType.ADDSUB)
                || token.getTokenType().equals(TokenType.MULTDIV)) {
            return true;
        }
        return false;
    }

    public boolean isFirstNumInt(Token token) {
        if(token.getTokenType().equals(TokenType.ADDSUB)
                || token.getTokenType().equals(TokenType.MULTDIV)) {
            return true;
        }
        return false;
    }

    public boolean isFirstNumFloat(Token token) {
        if(token.getTokenType().equals(TokenType.ADDSUB)
                || token.getTokenType().equals(TokenType.MULTDIV)) {
            return true;
        }
        return false;
    }

    public boolean isFirstLPar(Token token) {
        if(token.getTokenType().equals(TokenType.L_PAR)
                || token.getTokenType().equals(TokenType.ID)
                || token.getTokenType().equals(TokenType.NUM_INT)
                || token.getTokenType().equals(TokenType.NUM_FLOAT)) {
            return true;
        }
        return false;
    }
}
