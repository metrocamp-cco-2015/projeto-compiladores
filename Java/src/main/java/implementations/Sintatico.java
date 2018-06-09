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
			//TODO lan�ar erro de exception
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

		if(token.getTokenType().equals(TokenType.PROGRAM)) {
			procContS();
		}else {
			errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.PROGRAM.name());
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando o 'programa'");

			if(token.getTokenType().equals(TokenType.ID)) {
				token = lexico.nextToken();
				if(token.getTokenType().equals(TokenType.TERM)) {
					//Processa o Bloco
					procBloco();
					//Processa o final do programa
					procEndS();
				}else {
					errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.TERM.name() + " (';')");
					System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando um ';'");

					lexico.resetLastToken(token);
					//Processa o Bloco
					procBloco();
					//Processa o final do programa
					procEndS();
				}
			}else {
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
		if(token.getTokenType().equals(TokenType.ID)) {
			token = lexico.nextToken();
			if(token.getTokenType().equals(TokenType.TERM)) {
				//Processa o Bloco
				token = lexico.nextToken();
				if(firstFollow.isFirstBloco(token)) {
					lexico.resetLastToken(token);
					procBloco();
					//Processa o final do programa
					procEndS();
				}else {
<<<<<<< HEAD
					errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "BLOCO");
=======
					//TODO lan�a erro por nao estar no first do bloco
>>>>>>> vitor
					System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nN�o est� no first do bloco");
				}
			}else {
				errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.TERM.name() + " (';')");
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando um ';'");

				lexico.resetLastToken(token);
				//Processa o Bloco
				procBloco();
				//Processa o final do programa
				procEndS();
			}
		}else {
			errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.ID.name());
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando o identificador do programa");
			if(!token.getTokenType().equals(TokenType.TERM)) {
				errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.TERM.name() + " (';')");
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando um ';'");
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
		if(token.getTokenType().equals(TokenType.END_PROG)) {
			token = lexico.nextToken();
			if(!token.getTokenType().equals(TokenType.TERM)) {
				errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.TERM.name() + " (';')");
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando um ';'");
			}
		}else {
			errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.END_PROG.name());
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando o 'fimprog'");
		}
	}

	/**
	 * Verifica se a sintaxe do BLOCO esta correta
	 * @throws Exception
	 */
	private void procBloco() throws Exception {
		token = lexico.nextToken();

		if(token.getTokenType().equals(TokenType.BEGIN)) {
			
			token = lexico.nextToken();
			if(firstFollow.isFirstCmds(token)) {
				lexico.resetLastToken(token);
				procCmds();
				
				token = lexico.nextToken();
				if(!token.getTokenType().equals(TokenType.END)) {
					errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.END.name());
					System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nNao conseguiu processar BLOCO.");
				}
			} else {
				lexico.resetLastToken(token);
			}
		}else if(firstFollow.isFirstCmd(token)){
			lexico.resetLastToken(token);
			procCmd();
		}else {
<<<<<<< HEAD
			errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "CMD");
=======
			//TODO Lan�a erro por n�o possui o first de CMD
>>>>>>> vitor
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nNao possui o first de CMD");
		}
	}

    /**
     * Verifica se a sintaxe do CMD esta correta
     * @throws Exception
     */
	private void procCmd() throws Exception {
		token = lexico.nextToken();

		if(firstFollow.isFirstDecl(token)){
			lexico.resetLastToken(token);
			procDecl();
		} else if(firstFollow.isFirstCond(token)){
			lexico.resetLastToken(token);
			procCond();
		} else if(firstFollow.isFirstRep(token)){
			lexico.resetLastToken(token);
			procRep();
		} else if(firstFollow.isFirstAttrib(token)) {
			lexico.resetLastToken(token);
			procAttrib();
		} else {
			errorHandler.addSyntacticError(token, WRONG_SYNTAX, "CMD");
            System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nNao conseguiu processar CMD.");
        }
	}

    /**
     * Verifica se a sintaxe do CMDS esta correta
     * @throws Exception
     */
	private void procCmds() throws Exception{
		token = lexico.nextToken();
		
		if(firstFollow.isFirstCmds(token)) {
			if(token.getTokenType().equals(TokenType.DECLARE)){
				lexico.resetLastToken(token);
				procDecl();
			} else if(token.getTokenType().equals(TokenType.IF)){
				lexico.resetLastToken(token);
				procCond();
			} else if(token.getTokenType().equals(TokenType.FOR)){
				lexico.resetLastToken(token);
				procRepF();
			} else if(token.getTokenType().equals(TokenType.WHILE)){
				lexico.resetLastToken(token);
				procRepw();
			} else if(token.getTokenType().equals(TokenType.ID)){
				lexico.resetLastToken(token);
				procAttrib();
			}
			
			token = lexico.nextToken();
			if(firstFollow.isFirstCmds(token)) {
				lexico.resetLastToken(token);
				procCmds();
			}else {
				lexico.resetLastToken(token);
			}
		}else {
			if(firstFollow.isFollowCmds(token)) {
				lexico.resetLastToken(token);
			}else {
<<<<<<< HEAD
				errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.END.name());
=======
				//TODO lanca erro por nao vir token do tipo END
>>>>>>> vitor
	            System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken n�o esperado, END");
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
            	
            	if(firstFollow.isFirstExplo(token)) {
            		lexico.resetLastToken(token);
            		procExplo();
            		token = lexico.nextToken();

                    if (token.getTokenType().equals(TokenType.R_PAR)) {
                        token = lexico.nextToken();

                        if (token.getTokenType().equals(TokenType.THEN)) {
                            token = lexico.nextToken();
                        	if(firstFollow.isFirstBloco(token)) {
                        		lexico.resetLastToken(token);
                                procBloco();
                                token = lexico.nextToken();
                                if(firstFollow.isFirstCndb(token)) {
                                	lexico.resetLastToken(token);
                                	procCndb();
                                }else {
<<<<<<< HEAD
									errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "CNDB");
                                    System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nFirst de CNDB n�o encontrado.");
                                }
                        	}else {
								errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "BLOCO");
=======
                                	//TODO lanca erro por nao possuir first do CNDB
                                    System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nFirst de CNDB n�o encontrado.");
                                }
                        	}else {
                        		//TODO lanca erro por nao possuir first do BLOCO
>>>>>>> vitor
                                System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nFirst de Bloco n�o encontrado.");
                        	}
                        } else {
							errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.THEN.name());
                            System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken THEN não encontrado.");
                        }

                    } else {
						errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.R_PAR.name() + " (')')");
                        System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken R_PAR não encontrado.");
                    }
            	}else {
<<<<<<< HEAD
					errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXPLO");
=======
            		//TODO lanca erro por nao vir o first de EXPLO
>>>>>>> vitor
    	            System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken n�o esperado, first EXPLO");
            	}
            } else {
				errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.L_PAR.name() + " ('(')");
                System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken L_PAR não encontrado.");
            }

        } else {
			errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.IF.name());
            System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken IF não encontrado.");
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
        	if(firstFollow.isFirstBloco(token)) {
        		lexico.resetLastToken(token);
        		procBloco();
        	}else {
<<<<<<< HEAD
				errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "BLOCO");
=======
        		//TODO lanca erro por nao possuir o FIRST do Bloco
>>>>>>> vitor
                System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nFirst de Bloco n�o encontrado.");
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
                        System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken TERM não encontrado.");
                    }

                } else {
					errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.TYPE.name() + " ('text', 'num' ou 'logico')");
                    System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken TYPE não encontrado.");
                }

            } else {
				errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.ID.name());
                System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken ID não encontrado.");
            }

        } else {
			errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.DECLARE.name());
            System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken DECLARE não encontrado.");
        }
    }

	/**
	 * Verifica a sintaxe do FVALLOG esta correta
	 * @throws IOException
	 */
	private void procFvallog() throws IOException {
		token = lexico.nextToken();
		if(token.getTokenType().equals(TokenType.LOGIC_OP)) {
			token = lexico.nextToken();
			if(firstFollow.isFirstExplo(token)) {
				lexico.resetLastToken(token);
				procExplo();
			}else {
<<<<<<< HEAD
				errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXPLO");
=======
				//TODO lanca erro por nao encontrar o first de Explo
>>>>>>> vitor
	            System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nFirst de Explo n�o encontrado.");
			}
		}else {
			if (firstFollow.isFollowFvallog(token)) {
				lexico.resetLastToken(token);
			}else {
<<<<<<< HEAD
				errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXPLO");
=======
				//TODO lanca erro por nao encontrar o first de Explo
>>>>>>> vitor
	            System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nFollow de Fvallog n�o encontrado.");
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
			}else {
<<<<<<< HEAD
				errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXPNUM");
	            System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nFirst de Expnum n�o encontrado.");
			}
		} else {
			errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "OPNUM");
=======
				//TODO lanca erro por nao encontrar o first de Expnum
	            System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nFirst de Expnum n�o encontrado.");
			}
		} else {
			//TODO lanca erro por nao encontrar o first de Opnum
>>>>>>> vitor
            System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nFirst de Opnum n�o encontrado.");
		}
	}

	/**
	 * Verifica a sintaxe do OPNUM esta correta
	 * @throws IOException
	 */
	private void procOpnum() throws IOException {
		token = lexico.nextToken();

		if(!token.getTokenType().equals(TokenType.ADDSUB)
				&& !token.getTokenType().equals(TokenType.MULTDIV)) {
			errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.ADDSUB.name() + " ou " + TokenType.MULTDIV.name());
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando o '+' ou '-'");
		}
	}

	/**
	 * Verifica a sintaxe do VAL esta correta
	 * @throws IOException
	 */
	private void procVal() throws IOException {
		token = lexico.nextToken();

		if(!token.getTokenType().equals(TokenType.ID)
				&& !token.getTokenType().equals(TokenType.NUM_INT)
				&& !token.getTokenType().equals(TokenType.NUM_FLOAT)) {

			errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.ID.name() +
					" ou " + TokenType.NUM_INT.name() +
					" ou " + TokenType.NUM_FLOAT.name());
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando um identificador, ou um n�mero.");
		}
	}

	/**
	 * Verifica a sintaxe do FNUMINT esta correta
	 * @throws IOException
	 */
	private void procFnumInt() throws IOException {
		token = lexico.nextToken();

		if(firstFollow.isFirstOpnum(token)) {
			lexico.resetLastToken(token);
			procOpnum();
			
			if(firstFollow.isFirstFopnum_1(token)) {
				procFopnum_1();
			}else {
				errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.MULTDIV.name() + " ('*', '/')");
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando um operador (* ou /).");
			}
		}else{
			errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.ADDSUB.name() + " ('+', '-')");
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando um operador (+ ou -).");
		}

	}

	/**
	 * Verifica a sintaxe do FOPNUM_1 esta correta
	 * @throws IOException
	 */
	private void procFopnum_1() throws IOException {
		token = lexico.nextToken();

		if(firstFollow.isFirstExpnum(token)) {
			lexico.resetLastToken(token);
			procExpnum();
			token = lexico.nextToken();
			if(firstFollow.isFirstFexpnum_2(token)) {
				lexico.resetLastToken(token);
				procFexpnum_2();
			}else {
<<<<<<< HEAD
				errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "FEXPNUM_2");
=======
				//TODO lanca erro por nao possuir First de Fexpnum_2
>>>>>>> vitor
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nFirst de Fexpnum_2 n�o encontrado.");
			}
		}else{
			errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.L_PAR.name() +
					" ou " + TokenType.ID.name() +
					" ou " + TokenType.NUM_INT.name() +
					" ou " + TokenType.NUM_FLOAT.name());
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando um (, ou um identificador, ou um n�mero.");
		}
	}

	/**
	 * Verifica a sintaxe do FEXPNUM_2 esta correta
	 * @throws IOException
	 */
	private void procFexpnum_2() throws IOException{
		token = lexico.nextToken();

		if(token.getTokenType().equals(TokenType.RELOP)) {
			
			token = lexico.nextToken();
			if (firstFollow.isFirstExpnum(token)) {
				lexico.resetLastToken(token);
				procExpnum();
<<<<<<< HEAD
			}else {
				errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.L_PAR.name() +
						" ou " + TokenType.ID.name() +
						" ou " + TokenType.NUM_INT.name() +
						" ou " + TokenType.NUM_FLOAT.name());
=======
			} else {
				//TODO lanca erro por nao possuir um L_PAR, ID, NUM_INT ou NUM_FLOAT
>>>>>>> vitor
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando um (, ou um identificador, ou um n�mero.");
			}
		} else {
			if (firstFollow.isFollowFexpnum_2(token)) {
				lexico.resetLastToken(token);
<<<<<<< HEAD
			}else {
				errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.TERM.name() + " (';')");
=======
			} else {
				//TODO lanca erro por nao possuir um TERM
>>>>>>> vitor
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando um terminal.");
			}
		}
	}

	/**
	 * Verifica a sintaxe do FNUMFLOAT esta correta
	 * @throws IOException
	 */
	private void procFnumfloat() throws IOException{
		token = lexico.nextToken();
		
		if(firstFollow.isFirstOpnum(token)) {
			lexico.resetLastToken(token);
			procOpnum();
			token = lexico.nextToken();
			
			if(firstFollow.isFirstFopnum_2(token)) {
				lexico.resetLastToken(token);
				procFopnum_2();
				token = lexico.nextToken();
			}else {
<<<<<<< HEAD
				errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.L_PAR.name() +
						" ou " + TokenType.ID.name() +
						" ou " + TokenType.NUM_INT.name() +
						" ou " + TokenType.NUM_FLOAT.name());
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando um (, ou um identificador, ou um n�mero.");
			}
		}else {
			errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.L_PAR.name() +
					" ou " + TokenType.ID.name() +
					" ou " + TokenType.NUM_INT.name() +
					" ou " + TokenType.NUM_FLOAT.name());
=======
				//TODO lanca erro por nao possuir um L_PAR, ID, NUM_INT ou NUM_FLOAT
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando um (, ou um identificador, ou um n�mero.");
			}
		}else {
			//TODO lanca erro por nao possuir um L_PAR, ID, NUM_INT ou NUM_FLOAT
>>>>>>> vitor
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando um (, ou um identificador, ou um n�mero.");
		}
	}

	/**
	 * Verifica a sintaxe do FEXPNUM_3 esta correta
	 * @throws IOException
	 */
	private void procFexpnum_3() throws IOException{
		token = lexico.nextToken();

		if(token.getTokenType().equals(TokenType.RELOP)){
			token = lexico.nextToken();
			
			if(firstFollow.isFirstExpnum(token)){
				lexico.resetLastToken(token);
				procExpnum();
			}else{
				errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.L_PAR.name() +
						" ou " + TokenType.ID.name() +
						" ou " + TokenType.NUM_INT.name() +
						" ou " + TokenType.NUM_FLOAT.name());
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
			}
		}else{
			if(firstFollow.isFollowFexpnum_3(token)) {
				lexico.resetLastToken(token);
			}else {
<<<<<<< HEAD
				errorHandler.addSyntacticError(token, WRONG_SYNTAX + FOLLOW, "FEXPNUM_3");
=======
				//TODO lanca erro por nao follow de Fexpnum_3
>>>>>>> vitor
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nFollow de Fexpnum_3 n�o encontrado.");
			}
		}
	}

	/**
	 * Verifica a sintaxe do FLPAR esta correta
	 * @throws IOException
	 */
	private void procFlpar() throws Exception {
		token = lexico.nextToken();
		
		if(firstFollow.isFirstExpnum(token)) {
			lexico.resetLastToken(token);
			procExpnum();
			token = lexico.nextToken();
			
			if(firstFollow.isFirstFexpnum(token)) {
				lexico.resetLastToken(token);
				procFexpnum();			
			}else{
				errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "FEXPNUM");
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
			}
		}else{
			errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXPNUM");
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
		}
	}

	/**
	 * Verifica a sintaxe do FEXPNUM esta correta
	 * @throws IOException
	 */
	private void procFexpnum() throws IOException{
		token = lexico.nextToken();
		
		if(token.getTokenType().equals(TokenType.R_PAR)){
			token = lexico.nextToken();
			
			if(firstFollow.isFirstFrpar(token)) {
				lexico.resetLastToken(token);
				procFrpar();
			
			}else{
				errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "FRPAR");
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
			}
		}else{
			errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.R_PAR.name() + " (')')");
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto ')'.");
		}
	}

	/**
	 * Verifica a sintaxe do FRPAR esta correta
	 * @throws IOException
	 */
	private void procFrpar() throws IOException{
		token = lexico.nextToken();
		
		if(token.getTokenType().equals(TokenType.RELOP)) {
			token = lexico.nextToken();
			
			if(firstFollow.isFirstExpnum(token)) {
				lexico.resetLastToken(token);
				procExpnum();
			}else {
				errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXPNUM");
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
			}
		}else {
			lexico.resetLastToken(token);
		}
	}

	/**
	 * Verifica a sintaxe do FID_1 esta correta
	 * @throws IOException
	 */
	private void procFid_1() throws IOException{
		token = lexico.nextToken();
		
		if(firstFollow.isFirstFvallog(token)){
			lexico.resetLastToken(token);
			procFvallog();
		
		}else if(firstFollow.isFirstOpnum(token)){
			lexico.resetLastToken(token);
				procOpnum();
				token = lexico.nextToken();
				
				if(firstFollow.isFirstExpnum(token)) {
					lexico.resetLastToken(token);
					procExpnum();
					token = lexico.nextToken();
					
					if(token.getTokenType().equals(TokenType.RELOP)) {
						token = lexico.nextToken();
						
						if(firstFollow.isFirstExpnum(token)){
							lexico.resetLastToken(token);
							procExpnum();
						
						}else{
							errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXPNUM");
							System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
						}
					}else{
						errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.RELOP.name());
						System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
					}
				}else{
					errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXPNUM");
					System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
				}
		} else if(token.getTokenType().equals(TokenType.RELOP)) {
			token = lexico.nextToken();
			
			if (firstFollow.isFirstExpnum(token)) {
				lexico.resetLastToken(token);
				procExpnum();
			} else {
				//TODO lanca erro
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
			}
		} else {
			errorHandler.addSyntacticError(token, WRONG_SYNTAX, "FID_1");
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
		}
	}

	/**
	 * Verifica a sintaxe do FOPNUM_2 esta correta
	 * @throws IOException
	 */
	private void procFopnum_2() throws IOException {
		token = lexico.nextToken();
		
		if(firstFollow.isFirstExpnum(token)) {
			lexico.resetLastToken(token);
			procExpnum();
			token = lexico.nextToken();
			
			if(firstFollow.isFirstFexpnum_3(token)){
				lexico.resetLastToken(token);
				procFexpnum_3();
			}else {
				errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "FEXPNUM_3");
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nFirst de Fexpnum_3 nao encontrado.");
			}
		}else {
			errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXPNUM");
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nFirst de Expnum nao encontrado.");
		}
	}

	/**
	 * Verifica a sintaxe do REP esta correta
	 * @throws Exception 
	 */
	private void procRep() throws Exception {
		token = lexico.nextToken();

		if(firstFollow.isFirstRepf(token)) {
			lexico.resetLastToken(token);
			procRepF();
		}else if (firstFollow.isFirstRepw(token)) {
			lexico.resetLastToken(token);
			procRepw();
		} else {
			errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.FOR.name() +
					" ou " + TokenType.WHILE.name());
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken não é FOR nem WHILE.");
		}
	}

	/**
	 * Verifica a sintaxe do REPF esta correta
	 * @throws Exception 
	 */
	private void procRepF() throws Exception {
		token = lexico.nextToken();

		if(token.getTokenType().equals(TokenType.FOR)) {
			token = lexico.nextToken();

			if(token.getTokenType().equals(TokenType.ID)) {
				token = lexico.nextToken();

				if(token.getTokenType().equals(TokenType.ATTRIB)) {
					token = lexico.nextToken();

					if(firstFollow.isFirstExpnum(token)) {
						lexico.resetLastToken(token);
						procExpnum();
						token = lexico.nextToken();

						if(token.getTokenType().equals(TokenType.TO)) {
							token = lexico.nextToken();

							if(firstFollow.isFirstExpnum(token)) {
								lexico.resetLastToken(token);
								procExpnum();
								token = lexico.nextToken();

								if(firstFollow.isFirstBloco(token)) {
									lexico.resetLastToken(token);
									procBloco();
								} else {
<<<<<<< HEAD
									errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "BLOCO");
									System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken não é isFirstBloco()");
								}
							} else {
								errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXPNUM");
								System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken não é isFirstExpnum()");
							}
						} else {
							errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.TO.name());
							System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken não é TO.");
						}
					} else {
						errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXPNUM");
						System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken não é isFirstExpnum().");
					}
				} else {
					errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.ATTRIB.name() + " ('<<')");
					System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken não é ATRIB.");
				}
			} else {
				errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.ID.name());
=======
									//TODO lanca erro por não ser isFirstBloco()
									System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken não é isFirstBloco()");
								}
							} else {
								//TODO lanca erro por não ser isFirstExpnum()
								System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken não é isFirstExpnum()");
							}
						} else {
							//TODO lanca erro por não ser TO
							System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken não é TO.");
						}
					} else {
						//TODO lanca erro por não ser isFirstExpnum()
						System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken não é isFirstExpnum().");
					}
				} else {
					//TODO lanca erro por não ser ATRIB
					System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken não é ATRIB.");
				}
			} else {
				//TODO lanca erro por não ser ID
>>>>>>> vitor
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken não é ID.");
			}
		} else {
			errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.FOR.name());
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken não é FOR nem WHILE.");
		}
	}

	/**
	 * Verifica a sintaxe do EXPNUM esta correta
	 * @throws IOException
	 */
	private void procExpnum() throws IOException {
		token = lexico.nextToken();

		if(token.getTokenType().equals(TokenType.L_PAR)) {
			token = lexico.nextToken();

			if(firstFollow.isFirstExpnum(token)) {
				lexico.resetLastToken(token);
				procExpnum();
				token = lexico.nextToken();

				if(!token.getTokenType().equals(TokenType.R_PAR)) {
					errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.R_PAR.name() + " (')')");
					System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken não é ')'");
				}
			}else {
				errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXPNUM");
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nFirst de Expnum nao encontrado");
			}
		} else if(firstFollow.isFirstVal(token)) {
			lexico.resetLastToken(token);
			procVal();
			token = lexico.nextToken();

			if(firstFollow.isFirstXexpnum(token)) {
				lexico.resetLastToken(token);
				procXexpnum();
			}else {
				lexico.resetLastToken(token);
			}
		} else {
			errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXPNUM");
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nFirst de EXPNUM nao encontrado.");
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
<<<<<<< HEAD
							errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "BLOCO");
							System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken não é isFirstBloco()");
						}
					} else {
						errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.R_PAR.name() + " (')')");
						System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken não é R_PAR");
					}
				} else {
					errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXPLO");
					System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken não é isFirstExplo()");
				}
			} else {
				errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.L_PAR.name() + " ('(')");
=======
							//TODO lanca erro por não ser isFirstBloco()
							System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken não é isFirstBloco()");
						}
					} else {
						//TODO lanca erro por não ser R_PAR
						System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken não é R_PAR");
					}
				} else {
					//TODO lanca erro por não ser isFirstExplo()
					System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken não é isFirstExplo()");
				}
			} else {
				//TODO lanca erro por não ser L_PAR
>>>>>>> vitor
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken não é L_PAR");
			}
		} else {
			errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.WHILE.name());
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken não é WHILE.");
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
							//TODO lanca erro
							System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
						}
					} else {
						//TODO lanca erro
						System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
					}
				} else {
					//TODO lanca erro
					System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
				}
			} else {
				//TODO lanca erro
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
			}
		} else {
			errorHandler.addSyntacticError(token, WRONG_SYNTAX, "EXPLO");
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
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
						System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken TERM não encontrado.");
					}
				}else {
					errorHandler.addSyntacticError(token, WRONG_SYNTAX + FIRST, "EXP");
					System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nErro por nao conseguir localizar o first de Exp.");
				}
			} else {
				errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.ATTRIB.name() + " ('<<')");
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken ATTRIB não encontrado.");
			}
		} else {
			errorHandler.addSyntacticError(token, UNEXPECTED_TOKEN, TokenType.ID.name());
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken ID não encontrado.");
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
			}else {
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
				//TODO lanca erro
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
			}
		} else if (!token.getTokenType().equals(TokenType.LITERAL)) {
			errorHandler.addSyntacticError(token, WRONG_SYNTAX, "EXP");
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
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
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
			}
		} else {
			errorHandler.addSyntacticError(token, WRONG_SYNTAX, "FID");
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
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
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
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
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
			}
		} else {
			lexico.resetLastToken(token);
		}
	}
}
