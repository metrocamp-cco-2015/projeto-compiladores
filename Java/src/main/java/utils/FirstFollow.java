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
    public boolean isFollowCmds(Token token) {
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
    
    public boolean isFirstFnumint(Token token) {
        if(token.getTokenType().equals(TokenType.ADDSUB)
                || token.getTokenType().equals(TokenType.MULTDIV)) {
            return true;
        }
        return false;
    }
    
    public boolean isFirstFopnum_1(Token token) {
    	if(token.getTokenType().equals(TokenType.ID)
                || token.getTokenType().equals(TokenType.NUM_INT)
                || token.getTokenType().equals(TokenType.NUM_FLOAT)
                || token.getTokenType().equals(TokenType.L_PAR)) {
            return true;
        }
        return false;
    }
    
    public boolean isFirstFexpnum_2(Token token) {
    	if(token.getTokenType().equals(TokenType.RELOP)) {
            return true;
        }
        return false;
    }
    
    public boolean isFirstFnumfloat(Token token) {
        if(token.getTokenType().equals(TokenType.ADDSUB)
                || token.getTokenType().equals(TokenType.MULTDIV)) {
            return true;
        }
        return false;
    }
    
    public boolean isFirstFopnum_2(Token token) {
    	if(token.getTokenType().equals(TokenType.ID)
                || token.getTokenType().equals(TokenType.NUM_INT)
                || token.getTokenType().equals(TokenType.NUM_FLOAT)
                || token.getTokenType().equals(TokenType.L_PAR)) {
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
    
    public boolean isFirstFexpnum(Token token) {
    	if(token.getTokenType().equals(TokenType.R_PAR)) {
    		return true;
    	}
    	return false;
    }
    public boolean isFirstFrpar(Token token) {
    	if(token.getTokenType().equals(TokenType.RELOP)) {
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

    public boolean isFirstFNumInt(Token token) {
        if(token.getTokenType().equals(TokenType.ADDSUB)
                || token.getTokenType().equals(TokenType.MULTDIV)) {
            return true;
        }
        return false;
    }

    public boolean isFirstFNumFloat(Token token) {
        if(token.getTokenType().equals(TokenType.ADDSUB)
                || token.getTokenType().equals(TokenType.MULTDIV)) {
            return true;
        }
        return false;
    }

    public boolean isFirstFlpar(Token token) {
        if(token.getTokenType().equals(TokenType.L_PAR)
                || token.getTokenType().equals(TokenType.ID)
                || token.getTokenType().equals(TokenType.NUM_INT)
                || token.getTokenType().equals(TokenType.NUM_FLOAT)) {
            return true;
        }
        return false;
    }

    public boolean isFirstFOpnum(Token token) {
        if(token.getTokenType().equals(TokenType.L_PAR)
                || token.getTokenType().equals(TokenType.ID)
                || token.getTokenType().equals(TokenType.NUM_INT)
                || token.getTokenType().equals(TokenType.NUM_FLOAT)) {
            return true;
        }
        return false;
    }

    public boolean isFirstFExpnum_1(Token token) {
        if(token.getTokenType().equals(TokenType.RELOP)) {
            return true;
        }
        return false;
    }
    
    public boolean isFirstCndb(Token token) {
        if(token.getTokenType().equals(TokenType.ELSE)) {
            return true;
        }
        return false;
    }
    
    public boolean isFollowFvallog(Token token) {
        if(token.getTokenType().equals(TokenType.TERM)
        		|| token.getTokenType().equals(TokenType.R_PAR)) {
            return true;
        }
        return false;
    }
    
    public boolean isFollowXexpnum(Token token) {
        if(token.getTokenType().equals(TokenType.RELOP)
        		|| token.getTokenType().equals(TokenType.TO)
        		|| token.getTokenType().equals(TokenType.BEGIN)
        		|| token.getTokenType().equals(TokenType.DECLARE)
        		|| token.getTokenType().equals(TokenType.IF)
        		|| token.getTokenType().equals(TokenType.ID)
        		|| token.getTokenType().equals(TokenType.FOR)
        		|| token.getTokenType().equals(TokenType.WHILE)
        		|| token.getTokenType().equals(TokenType.TERM)
        		|| token.getTokenType().equals(TokenType.R_PAR)) {
            return true;
        }
        return false;
    }

    public boolean isFollowFexpnum_2(Token token) {
		if(token.getTokenType().equals(TokenType.TERM)) {
			return true;
		}
		return false;
    }
    
    public boolean isFollowFexpnum_3(Token token) {
		if(token.getTokenType().equals(TokenType.TERM)) {
			return true;
		}
		return false;
    }
    
    public boolean isFirstFexpnum_3(Token token) {
		if(token.getTokenType().equals(TokenType.RELOP)) {
			return true;
		}
		return false;
    }
    
    public boolean isFirstRepf(Token token) {
		if(token.getTokenType().equals(TokenType.FOR)) {
			return true;
		}
		return false;
    }
    
    public boolean isFirstRepw(Token token) {
		if(token.getTokenType().equals(TokenType.WHILE)) {
			return true;
		}
		return false;
    }
    
    public boolean isFirstDecl(Token token) {
		if(token.getTokenType().equals(TokenType.DECLARE)) {
			return true;
		}
		return false;
    }
    
    public boolean isFirstCond(Token token) {
		if(token.getTokenType().equals(TokenType.IF)) {
			return true;
		}
		return false;
    }
    
    public boolean isFirstRep(Token token) {
		if(token.getTokenType().equals(TokenType.FOR)
				|| token.getTokenType().equals(TokenType.WHILE)) {
			return true;
		}
		return false;
    }
    
    public boolean isFirstAttrib(Token token) {
		if(token.getTokenType().equals(TokenType.ID)) {
			return true;
		}
		return false;
    }
	
}
