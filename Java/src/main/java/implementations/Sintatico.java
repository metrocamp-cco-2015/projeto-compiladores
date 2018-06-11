/**
 * Andre Henrique Pereira
 * Ednaldo Leite Junior
 * Erik Ricardo Balthazar
 * Jean Carlos Guinami Frias
 * Leticia Machado
 * Vitor Matheus Reis Marcelo
 */
package src.main.java.implementations;

import java.io.IOException;

import src.main.java.utils.ErrorHandler;
import src.main.java.utils.FirstFollow;
import src.main.java.utils.TabSimbolos;
import src.main.java.utils.TokenType;

public class Sintatico {

	//Mensagens de ERRO
	private static final String UNEXPECTED_TOKEN = "Esperava um token do tipo <STRING_TO_REPLACE>";
	private static final String WRONG_SYNTAX = "Não encontrou um token esperado para processar a sintaxe para <STRING_TO_REPLACE>";
	private static final String FIRST = " (FIRST não encontrado)";
	private static final String FOLLOW = " (FOLLOW não encontrado)";

	private Lexico lexico;
	private Token token;
	private FirstFollow firstFollow = new FirstFollow();
	private ErrorHandler errorHandler = ErrorHandler.getInstance();

	/**
	 * Realiza o processamento do arquivo ate encontrar um token EOF.
	 * Logo em seguida, imprime o relatorio de erros e a tabela de
	 * Simbolos.
	 *
	 * @throws IOException
	 */
	public void processar() throws IOException {
		System.out.println("Iniciando processamento do arquivo...\n\n\n");

		try {
			procS();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Imprime relatorio de erros
		ErrorHandler.getInstance().showErrors();

		// Imprime Tabela de Simbolos
		TabSimbolos.getInstance().printTabSimb();
	}

	/**
	 * Construtor do Sintatico.
	 *
	 * @param filename
	 */
	public Sintatico(final String filename){
		this.lexico = new Lexico(filename);
	}

	/**
	 * Inicializa o processamento dos Tokens verificando a sintaxe da linguagem
	 * @throws Exception
	 */
	private void procS() throws Exception {
		token = lexico.nextToken();

		if (token.getTokenType().equals(TokenType.PROGRAM)) {
			procContS();
		} else {
			errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.PROGRAM.name());

			if (token.getTokenType().equals(TokenType.ID)) {
				token = lexico.nextToken();
				if (token.getTokenType().equals(TokenType.TERM)) {
					//Processa o Bloco
					procBloco();
					//Processa o final do programa
					procEndS();
				} else {
					errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.TERM.name() + " (';')");

					lexico.resetLastToken(token);
					//Processa o Bloco
					procBloco();
					//Processa o final do programa
					procEndS();
				}
			} else {
				procContS();
			}
		}
	}

	/**
	 * Continuacao do processamento do S
	 *
	 * @throws IOException
	 * @throws Exception
	 */
	private void procContS() throws IOException, Exception {
		//Processa o programa depois de ter lido o token 'PROGRAM'
		if (token.getTokenType().equals(TokenType.ID)) {
			token = lexico.nextToken();
			if (token.getTokenType().equals(TokenType.TERM)) {
				//Processa o Bloco
				token = lexico.nextToken();
				if (firstFollow.isFirstBloco(token)) {
					lexico.resetLastToken(token);
					procBloco();
					//Processa o final do programa
					procEndS();
				} else {
					errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "BLOCO");
				}
			} else {
				errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.TERM.name() + " (';')");

				lexico.resetLastToken(token);
				//Processa o Bloco
				procBloco();
				//Processa o final do programa
				procEndS();
			}
		} else {
			errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.ID.name());
			if (!token.getTokenType().equals(TokenType.TERM)) {
				errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.TERM.name() + " (';')");
				lexico.resetLastToken(token);
			}
			//Processa o Bloco
			procBloco();
			//Processa o final do programa
			procEndS();
		}
	}

	/**
	 * Verifica a sintaxe depois do BLOCO ser processado, verificando se o final do programa esta certo
	 * @throws IOException
	 */
	private void procEndS() throws IOException {
		token = lexico.nextToken();
		if (token.getTokenType().equals(TokenType.END_PROG)) {
			token = lexico.nextToken();
			if (!token.getTokenType().equals(TokenType.TERM)) {
				errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.TERM.name() + " (';')");
			}
		} else {
			errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.END_PROG.name());
		}
	}

	/**
	 * Verifica se a sintaxe do BLOCO esta correta
	 * @throws Exception
	 */
	private void procBloco() throws Exception {
		token = lexico.nextToken();

		if (token.getTokenType().equals(TokenType.BEGIN)) {

			token = lexico.nextToken();
			if (firstFollow.isFirstCmds(token)) {
				lexico.resetLastToken(token);
				procCmds();

				token = lexico.nextToken();
				if (!token.getTokenType().equals(TokenType.END)) {
					errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.END.name());
				}
			} else {
				lexico.resetLastToken(token);
			}
		} else if (firstFollow.isFirstCmd(token)){
			lexico.resetLastToken(token);
			procCmd();
		} else {
			errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "CMD");
		}
	}

    /**
     * Verifica se a sintaxe do CMD esta correta
     * @throws Exception
     */
	private void procCmd() throws Exception {
		token = lexico.nextToken();

		if (firstFollow.isFirstDecl(token)){
			lexico.resetLastToken(token);
			procDecl();
		} else if (firstFollow.isFirstCond(token)){
			lexico.resetLastToken(token);
			procCond();
		} else if (firstFollow.isFirstRep(token)){
			lexico.resetLastToken(token);
			procRep();
		} else if (firstFollow.isFirstAttrib(token)) {
			lexico.resetLastToken(token);
			procAttrib();
		} else {
			errorHandler.addSyntacticError(token, WRONG_SYNTAX, "CMD");
    }
	}

    /**
     * Verifica se a sintaxe do CMDS esta correta
     * @throws Exception
     */
	private void procCmds() throws Exception{
		token = lexico.nextToken();

		if (firstFollow.isFirstCmds(token)) {
			if (token.getTokenType().equals(TokenType.DECLARE)){
				lexico.resetLastToken(token);
				procDecl();
			} else if (token.getTokenType().equals(TokenType.IF)){
				lexico.resetLastToken(token);
				procCond();
			} else if (token.getTokenType().equals(TokenType.FOR)){
				lexico.resetLastToken(token);
				procRepF();
			} else if (token.getTokenType().equals(TokenType.WHILE)){
				lexico.resetLastToken(token);
				procRepw();
			} else if (token.getTokenType().equals(TokenType.ID)){
				lexico.resetLastToken(token);
				procAttrib();
			}

			token = lexico.nextToken();
			if (firstFollow.isFirstCmds(token)) {
				lexico.resetLastToken(token);
				procCmds();
			} else {
				lexico.resetLastToken(token);
			}
		} else {
			if (firstFollow.isFollowCmds(token)) {
				lexico.resetLastToken(token);
			} else {
				errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.END.name());
			}
		}
	}

    /**
     * Verifica se a sintaxe do COND esta correta
     * @throws Exception
     */
	private void procCond() throws Exception {
        token = lexico.nextToken();

        if (token.getTokenType().equals(TokenType.IF)) {
            token = lexico.nextToken();

            if (token.getTokenType().equals(TokenType.L_PAR)) {
            	token = lexico.nextToken();

            	if (firstFollow.isFirstExplo(token)) {
            		lexico.resetLastToken(token);
            		procExplo();
            		token = lexico.nextToken();

                    if (token.getTokenType().equals(TokenType.R_PAR)) {
                        token = lexico.nextToken();

                        if (token.getTokenType().equals(TokenType.THEN)) {
                            token = lexico.nextToken();
                        	if (firstFollow.isFirstBloco(token)) {
                        		lexico.resetLastToken(token);
                                procBloco();
                                token = lexico.nextToken();
                                if (firstFollow.isFirstCndb(token)) {
                                	lexico.resetLastToken(token);
                                	procCndb();
                                } else {
									                errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "CNDB");
                                }
                        	} else {
								            errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "BLOCO");
                        	}
                        } else {
							            errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.THEN.name());
                        }

                    } else {
						          errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.R_PAR.name() + " (')')");
                    }
            	} else {
					      errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXPLO");
            	}
            } else {
				      errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.L_PAR.name() + " ('(')");
            }

        } else {
			    errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.IF.name());
        }
	}

    /**
     * Verifica se a sintaxe do CNDB esta correta
     * @throws Exception
     */
    private void procCndb() throws Exception {
        token = lexico.nextToken();

        if (token.getTokenType().equals(TokenType.ELSE)) {
        	token = lexico.nextToken();
        	if (firstFollow.isFirstBloco(token)) {
        		lexico.resetLastToken(token);
        		procBloco();
        	} else {
				    errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "BLOCO");
        	}
        } else {
        	lexico.resetLastToken(token);
        }
    }

    /**
     * Verifica se a sintaxe do DECL esta correta
     * @throws Exception
     */
	private void procDecl() throws Exception {
        token = lexico.nextToken();

        if (token.getTokenType().equals(TokenType.DECLARE)) {
            token = lexico.nextToken();

            if (token.getTokenType().equals(TokenType.ID)) {
                token = lexico.nextToken();

                if (token.getTokenType().equals(TokenType.TYPE)) {
                    token = lexico.nextToken();

                    if (!token.getTokenType().equals(TokenType.TERM)) {
						          errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.TERM.name() + " (';')");
                    }

                } else {
					        errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.TYPE.name() + " ('text', 'num' ou 'logico')");
                }

            } else {
				      errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.ID.name());
            }

        } else {
			    errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.DECLARE.name());
        }
    }

	/**
	 * Verifica a sintaxe do FVALLOG esta correta
	 * @throws IOException
	 */
	private void procFvallog() throws IOException {
		token = lexico.nextToken();
		if (token.getTokenType().equals(TokenType.LOGIC_OP)) {
			token = lexico.nextToken();
			if (firstFollow.isFirstExplo(token)) {
				lexico.resetLastToken(token);
				procExplo();
			} else {
				errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXPLO");
			}
		} else {
			if (firstFollow.isFollowFvallog(token)) {
				lexico.resetLastToken(token);
			} else {
				errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXPLO");
			}
		}
	}

	/**
	 * Verifica a sintaxe do XEXPNUM esta correta
	 * @throws IOException
	 */
	private void procXexpnum() throws IOException {
		token = lexico.nextToken();

		if (firstFollow.isFirstOpnum(token)) {
			lexico.resetLastToken(token);
			procOpnum();

			token = lexico.nextToken();
			if (firstFollow.isFirstExpnum(token)) {
				lexico.resetLastToken(token);
				procExpnum();
			} else {
				errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXPNUM");
			}
		} else {
			errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "OPNUM");
		}
	}

	/**
	 * Verifica a sintaxe do OPNUM esta correta
	 * @throws IOException
	 */
	private void procOpnum() throws IOException {
		token = lexico.nextToken();

		if (!token.getTokenType().equals(TokenType.ADDSUB)
				&& !token.getTokenType().equals(TokenType.MULTDIV)) {
			errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.ADDSUB.name() + " ou " + TokenType.MULTDIV.name());
		}
	}

	/**
	 * Verifica a sintaxe do VAL esta correta
	 * @throws IOException
	 */
	private void procVal() throws IOException {
		token = lexico.nextToken();

		if (!token.getTokenType().equals(TokenType.ID)
				&& !token.getTokenType().equals(TokenType.NUM_INT)
				&& !token.getTokenType().equals(TokenType.NUM_FLOAT)) {

			errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.ID.name() +
					" ou " + TokenType.NUM_INT.name() +
					" ou " + TokenType.NUM_FLOAT.name());
		}
	}

	/**
	 * Verifica a sintaxe do FNUMINT esta correta
	 * @throws IOException
	 */
	private void procFnumInt() throws IOException {
		token = lexico.nextToken();

		if (firstFollow.isFirstOpnum(token)) {
			lexico.resetLastToken(token);
			procOpnum();

			if (firstFollow.isFirstFopnum_1(token)) {
				procFopnum_1();
			} else {
				errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.MULTDIV.name() + " ('*', '/')");
			}
		} else{
			errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.ADDSUB.name() + " ('+', '-')");
		}

	}

	/**
	 * Verifica a sintaxe do FOPNUM_1 esta correta
	 * @throws IOException
	 */
	private void procFopnum_1() throws IOException {
		token = lexico.nextToken();

		if (firstFollow.isFirstExpnum(token)) {
			lexico.resetLastToken(token);
			procExpnum();
			token = lexico.nextToken();
			if (firstFollow.isFirstFexpnum_2(token)) {
				lexico.resetLastToken(token);
				procFexpnum_2();
			} else {
				errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "FEXPNUM_2");
			}
		} else{
			errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.L_PAR.name() +
					" ou " + TokenType.ID.name() +
					" ou " + TokenType.NUM_INT.name() +
					" ou " + TokenType.NUM_FLOAT.name());
		}
	}

	/**
	 * Verifica a sintaxe do FEXPNUM_2 esta correta
	 * @throws IOException
	 */
	private void procFexpnum_2() throws IOException{
		token = lexico.nextToken();

		if (token.getTokenType().equals(TokenType.RELOP)) {

			token = lexico.nextToken();
			if (firstFollow.isFirstExpnum(token)) {
				lexico.resetLastToken(token);
				procExpnum();
			} else {
				errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.L_PAR.name() +
						" ou " + TokenType.ID.name() +
						" ou " + TokenType.NUM_INT.name() +
						" ou " + TokenType.NUM_FLOAT.name());
			}
		} else {
			if (firstFollow.isFollowFexpnum_2(token)) {
				lexico.resetLastToken(token);
			} else {
				errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.TERM.name() + " (';')");
			}
		}
	}

	/**
	 * Verifica a sintaxe do FNUMFLOAT esta correta
	 * @throws IOException
	 */
	private void procFnumfloat() throws IOException{
		token = lexico.nextToken();

		if (firstFollow.isFirstOpnum(token)) {
			lexico.resetLastToken(token);
			procOpnum();
			token = lexico.nextToken();

			if (firstFollow.isFirstFopnum_2(token)) {
				lexico.resetLastToken(token);
				procFopnum_2();
				token = lexico.nextToken();
			} else {
				errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.L_PAR.name() +
						" ou " + TokenType.ID.name() +
						" ou " + TokenType.NUM_INT.name() +
						" ou " + TokenType.NUM_FLOAT.name());
			}
		} else {
			errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.L_PAR.name() +
					" ou " + TokenType.ID.name() +
					" ou " + TokenType.NUM_INT.name() +
					" ou " + TokenType.NUM_FLOAT.name());
		}
	}

	/**
	 * Verifica a sintaxe do FEXPNUM_3 esta correta
	 * @throws IOException
	 */
	private void procFexpnum_3() throws IOException{
		token = lexico.nextToken();

		if (token.getTokenType().equals(TokenType.RELOP)){
			token = lexico.nextToken();

			if (firstFollow.isFirstExpnum(token)){
				lexico.resetLastToken(token);
				procExpnum();
			} else{
				errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.L_PAR.name() +
						" ou " + TokenType.ID.name() +
						" ou " + TokenType.NUM_INT.name() +
						" ou " + TokenType.NUM_FLOAT.name());
			}
		} else{
			if (firstFollow.isFollowFexpnum_3(token)) {
				lexico.resetLastToken(token);
			} else {
				errorHandler.addSyntacticError(token, WRONG_SYNTAX + FOLLOW, "FEXPNUM_3");
			}
		}
	}

	/**
	 * Verifica a sintaxe do FLPAR esta correta
	 * @throws IOException
	 */
	private void procFlpar() throws Exception {
		token = lexico.nextToken();

		if (firstFollow.isFirstExpnum(token)) {
			lexico.resetLastToken(token);
			procExpnum();
			token = lexico.nextToken();

			if (firstFollow.isFirstFexpnum(token)) {
				lexico.resetLastToken(token);
				procFexpnum();
			} else{
				errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "FEXPNUM");
			}
		} else{
			errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXPNUM");
		}
	}

	/**
	 * Verifica a sintaxe do FEXPNUM esta correta
	 * @throws IOException
	 */
	private void procFexpnum() throws IOException{
		token = lexico.nextToken();

		if (token.getTokenType().equals(TokenType.R_PAR)){
			token = lexico.nextToken();

			if (firstFollow.isFirstFrpar(token)) {
				lexico.resetLastToken(token);
				procFrpar();

			} else{
				errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "FRPAR");
			}
		} else{
			errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.R_PAR.name() + " (')')");
		}
	}

	/**
	 * Verifica a sintaxe do FRPAR esta correta
	 * @throws IOException
	 */
	private void procFrpar() throws IOException{
		token = lexico.nextToken();

		if (token.getTokenType().equals(TokenType.RELOP)) {
			token = lexico.nextToken();

			if (firstFollow.isFirstExpnum(token)) {
				lexico.resetLastToken(token);
				procExpnum();
			} else {
				errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXPNUM");
			}
		} else {
			lexico.resetLastToken(token);
		}
	}

	/**
	 * Verifica a sintaxe do FID_1 esta correta
	 * @throws IOException
	 */
	private void procFid_1() throws IOException{
		token = lexico.nextToken();

		if (firstFollow.isFirstFvallog(token)){
			lexico.resetLastToken(token);
			procFvallog();

		} else if (firstFollow.isFirstOpnum(token)){
			lexico.resetLastToken(token);
				procOpnum();
				token = lexico.nextToken();

				if (firstFollow.isFirstExpnum(token)) {
					lexico.resetLastToken(token);
					procExpnum();
					token = lexico.nextToken();

					if (token.getTokenType().equals(TokenType.RELOP)) {
						token = lexico.nextToken();

						if (firstFollow.isFirstExpnum(token)){
							lexico.resetLastToken(token);
							procExpnum();

						} else{
							errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXPNUM");
						}
					} else{
						errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.RELOP.name());
					}
				} else{
					errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXPNUM");
				}
		} else if (token.getTokenType().equals(TokenType.RELOP)) {
			token = lexico.nextToken();

			if (firstFollow.isFirstExpnum(token)) {
				lexico.resetLastToken(token);
				procExpnum();
			} else {
				errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXPNUM");
			}
		} else {
			errorHandler.addSyntacticError(token, WRONG_SYNTAX, "FID_1");
		}
	}

	/**
	 * Verifica a sintaxe do FOPNUM_2 esta correta
	 * @throws IOException
	 */
	private void procFopnum_2() throws IOException {
		token = lexico.nextToken();

		if (firstFollow.isFirstExpnum(token)) {
			lexico.resetLastToken(token);
			procExpnum();
			token = lexico.nextToken();

			if (firstFollow.isFirstFexpnum_3(token)){
				lexico.resetLastToken(token);
				procFexpnum_3();
			} else {
				errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "FEXPNUM_3");
			}
		} else {
			errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXPNUM");
		}
	}

	/**
	 * Verifica a sintaxe do REP esta correta
	 * @throws Exception
	 */
	private void procRep() throws Exception {
		token = lexico.nextToken();

		if (firstFollow.isFirstRepf(token)) {
			lexico.resetLastToken(token);
			procRepF();
		} else if (firstFollow.isFirstRepw(token)) {
			lexico.resetLastToken(token);
			procRepw();
		} else {
			errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.FOR.name() +
					" ou " + TokenType.WHILE.name());
		}
	}

	/**
	 * Verifica a sintaxe do REPF esta correta
	 * @throws Exception
	 */
	private void procRepF() throws Exception {
		token = lexico.nextToken();

		if (token.getTokenType().equals(TokenType.FOR)) {
			token = lexico.nextToken();

			if (token.getTokenType().equals(TokenType.ID)) {
				token = lexico.nextToken();

				if (token.getTokenType().equals(TokenType.ATTRIB)) {
					token = lexico.nextToken();

					if (firstFollow.isFirstExpnum(token)) {
						lexico.resetLastToken(token);
						procExpnum();
						token = lexico.nextToken();

						if (token.getTokenType().equals(TokenType.TO)) {
							token = lexico.nextToken();

							if (firstFollow.isFirstExpnum(token)) {
								lexico.resetLastToken(token);
								procExpnum();
								token = lexico.nextToken();

								if (firstFollow.isFirstBloco(token)) {
									lexico.resetLastToken(token);
									procBloco();
								} else {
									errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "BLOCO");
								}
							} else {
								errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXPNUM");
							}
						} else {
							errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.TO.name());
						}
					} else {
						errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXPNUM");
					}
				} else {
					errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.ATTRIB.name() + " ('<<')");
				}
			} else {
				errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.ID.name());
			}
		} else {
			errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.FOR.name());
		}
	}

	/**
	 * Verifica a sintaxe do EXPNUM esta correta
	 * @throws IOException
	 */
	private void procExpnum() throws IOException {
		token = lexico.nextToken();

		if (token.getTokenType().equals(TokenType.L_PAR)) {
			token = lexico.nextToken();

			if (firstFollow.isFirstExpnum(token)) {
				lexico.resetLastToken(token);
				procExpnum();
				token = lexico.nextToken();

				if (!token.getTokenType().equals(TokenType.R_PAR)) {
					errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.R_PAR.name() + " (')')");
				}
			} else {
				errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXPNUM");
			}
		} else if (firstFollow.isFirstVal(token)) {
			lexico.resetLastToken(token);
			procVal();
			token = lexico.nextToken();

			if (firstFollow.isFirstXexpnum(token)) {
				lexico.resetLastToken(token);
				procXexpnum();
			} else {
				lexico.resetLastToken(token);
			}
		} else {
			errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXPNUM");
		}
	}

	/**
	 * Verifica a sintaxe do REPW esta correta
	 * @throws Exception
	 */
	private void procRepw() throws Exception {
		token = lexico.nextToken();

		if (token.getTokenType().equals(TokenType.WHILE)) {
			token = lexico.nextToken();

			if (token.getTokenType().equals(TokenType.L_PAR)) {
				token = lexico.nextToken();

				if (firstFollow.isFirstExplo(token)) {
					lexico.resetLastToken(token);
					procExplo();
					token = lexico.nextToken();

					if (token.getTokenType().equals(TokenType.R_PAR)) {
						token = lexico.nextToken();

						if (firstFollow.isFirstBloco(token)) {
							lexico.resetLastToken(token);
							procBloco();
						} else {
							errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "BLOCO");
						}
					} else {
						errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.R_PAR.name() + " (')')");
					}
				} else {
					errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXPLO");
				}
			} else {
				errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.L_PAR.name() + " ('(')");
			}
		} else {
			errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.WHILE.name());
		}
	}

	/**
	 * Verifica a sintaxe do EXPLO esta correta
	 * @throws IOException
	 */
	private void procExplo() throws IOException {
		token = lexico.nextToken();

		if (token.getTokenType().equals(TokenType.LOGIC_VAL)) {
			token = lexico.nextToken();

			if (firstFollow.isFirstFvallog(token)) {
				lexico.resetLastToken(token);
				procFvallog();
			} else {
				lexico.resetLastToken(token);
			}
		} else if (token.getTokenType().equals(TokenType.ID)) {
			token = lexico.nextToken();

			if (firstFollow.isFirstFid1(token)) {
				lexico.resetLastToken(token);
				procFid_1();
			} else {
				lexico.resetLastToken(token);
			}

		} else if (token.getTokenType().equals(TokenType.NUM_INT) || token.getTokenType().equals(TokenType.NUM_FLOAT)) {
			token = lexico.nextToken();

			if (firstFollow.isFirstOpnum(token)) {
				lexico.resetLastToken(token);
				procOpnum();
				token = lexico.nextToken();

				if (firstFollow.isFirstExpnum(token)) {
					lexico.resetLastToken(token);
					procExpnum();
					token = lexico.nextToken();

					if (token.getTokenType().equals(TokenType.RELOP)) {
						token = lexico.nextToken();

						if (firstFollow.isFirstExpnum(token)) {
							lexico.resetLastToken(token);
							procExpnum();
						} else {
							errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXPNUM");
						}
					} else {
						errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.RELOP.name()
					}
				} else {
				  errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXPNUM");
				}
			} else {
				errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "OPNUM");
			}
		} else {
			errorHandler.addSyntacticError(token, WRONG_SYNTAX, "EXPLO");
		}
	}

	/**
	 * Verifica se a sintaxe do ATTRIB esta correta
	 * @throws Exception
	 */
	private void procAttrib() throws Exception {
		token = lexico.nextToken();

		if (token.getTokenType().equals(TokenType.ID)) {
			token = lexico.nextToken();

			if (token.getTokenType().equals(TokenType.ATTRIB)) {
				token = lexico.nextToken();

				if (firstFollow.isFirstExp(token)) {
					lexico.resetLastToken(token);
					procExp();
					token = lexico.nextToken();

					if (!token.getTokenType().equals(TokenType.TERM)) {
						errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.TERM.name() + " (';')");
					}
				} else {
					errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXP");
				}
			} else {
				errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.ATTRIB.name() + " ('<<')");
			}
		} else {
			errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.ID.name());
		}
	}

	/**
	 * Verifica se a sintaxe do EXP esta correta
	 * @throws Exception
	 */
	private void procExp() throws Exception {
		token = lexico.nextToken();

		if (token.getTokenType().equals(TokenType.LOGIC_VAL)) {
			token = lexico.nextToken();

			if (firstFollow.isFirstFvallog(token)) {
				lexico.resetLastToken(token);
				procFvallog();
			} else {
				lexico.resetLastToken(token);
			}
		} else if (token.getTokenType().equals(TokenType.ID)) {
			token = lexico.nextToken();

			if (firstFollow.isFirstFid(token)) {
				lexico.resetLastToken(token);
				procFid();
			} else {
				lexico.resetLastToken(token);
			}
		} else if (token.getTokenType().equals(TokenType.NUM_INT)) {
			token = lexico.nextToken();

			if (firstFollow.isFirstFNumInt(token)) {
				lexico.resetLastToken(token);
				procFnumInt();
			} else {
				lexico.resetLastToken(token);
			}
		} else if (token.getTokenType().equals(TokenType.NUM_FLOAT)) {
			token = lexico.nextToken();

			if (firstFollow.isFirstFNumFloat(token)) {
				lexico.resetLastToken(token);
				procFnumfloat();
			} else {
				lexico.resetLastToken(token);
			}
		} else if (token.getTokenType().equals(TokenType.L_PAR)) {
			token = lexico.nextToken();

			if (firstFollow.isFirstFlpar(token)) {
				lexico.resetLastToken(token);
				procFlpar();
			} else {
				errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "FLPAR");
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
			}
		} else if (!token.getTokenType().equals(TokenType.LITERAL)) {
			errorHandler.addSyntacticError(token, WRONG_SYNTAX, "EXP");
		}
	}

	/**
	 * Verifica se a sintaxe do FID esta correta
	 * @throws Exception
	 */
	private void procFid() throws Exception {
		token = lexico.nextToken();

		if (firstFollow.isFirstFvallog(token)) {
			lexico.resetLastToken(token);
			procFvallog();
		} else if (firstFollow.isFirstOpnum(token)) {
			lexico.resetLastToken(token);
			procOpnum();
			token = lexico.nextToken();

			if (firstFollow.isFirstFOpnum(token)) {
				lexico.resetLastToken(token);
				procFOpnum();
			} else {
				errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "FOPNUM");
			}
		} else {
			errorHandler.addSyntacticError(token, WRONG_SYNTAX, "FID");
		}
	}

	/**
	 * Verifica se a sintaxe do FOPNUM esta correta
	 * @throws Exception
	 */
	private void procFOpnum() throws Exception {
		token = lexico.nextToken();

		if (firstFollow.isFirstExpnum(token)) {
			lexico.resetLastToken(token);
			procExpnum();
			token = lexico.nextToken();

			if (firstFollow.isFirstFExpnum_1(token)) {
				lexico.resetLastToken(token);
				procFExpnum_1();
			} else {
				lexico.resetLastToken(token);
			}
		} else {
			errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXPNUM");
		}
	}

	/**
	 * Verifica se a sintaxe do FEXPNUM_1 esta correta
	 * @throws Exception
	 */
	private void procFExpnum_1() throws Exception {
		token = lexico.nextToken();

		if (token.getTokenType().equals(TokenType.RELOP)) {
			token = lexico.nextToken();

			if (firstFollow.isFirstExpnum(token)) {
				lexico.resetLastToken(token);
				procExpnum();
			} else {
				errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXPNUM");
			}
		} else {
			lexico.resetLastToken(token);
		}
	}
}
