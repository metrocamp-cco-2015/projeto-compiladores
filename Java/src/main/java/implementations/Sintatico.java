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
import src.main.java.utils.TokenType;

public class Sintatico {

	private Lexico lexico;
	private Token token;
	private FirstFollow firstFollow = new FirstFollow();

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

		// TODO descomentar ap�s os testes
		// Imprime Tabela de Simbolos
		//TabSimbolos.getInstance().printTabSimb();
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
	//TODO melhorar esse metodo
	private void procS() throws Exception {
		token = lexico.nextToken();

		if(token.getTokenType().equals(TokenType.PROGRAM)) {
			procContS();
		}else {
			//TODO lan�a erro por nao possuir o 'programa'
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando o 'programa'");

			if(token.getTokenType().equals(TokenType.ID)) {
				token = lexico.nextToken();
				if(token.getTokenType().equals(TokenType.TERM)) {
					//Processa o Bloco
					procBloco();
					//Processa o final do programa
					procEndS();
				}else {
					//TODO lan�a erro por nao possuir o ';'
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
		token = lexico.nextToken();
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
					//TODO lan�a erro por nao estar no first do bloco
					System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nN�o est� no first do bloco");
				}
			}else {
				//TODO lan�a erro por nao possuir o ';'
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando um ';'");

				lexico.resetLastToken(token);
				//Processa o Bloco
				procBloco();
				//Processa o final do programa
				procEndS();
			}
		}else {
			//TODO lan�a erro por nao possuir um ID
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando o identificador do programa");
			if(!token.getTokenType().equals(TokenType.TERM)) {
				//TODO lan�a erro por nao possuir o ';'
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
				//TODO lan�a erro por nao possuir o token term
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando um ';'");
			}
		}else {
			//TODO lan�a erro por nao possuir o token end_prog
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
					// TODO lanca erro por nao vir o token 'end'
					System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nNao conseguiu processar BLOCO.");
				}
			} else {
				lexico.resetLastToken(token);
			}
		}else if(firstFollow.isFirstCmd(token)){
			lexico.resetLastToken(token);
			procCmd();
		}else {
			//TODO Lan�a erro por n�o possui o first de CMD
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
            //TODO lanca erro por nao conseguir processar o token dentro da sintaxe esperada para CMD
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
				//TODO lanca erro por nao vir token do tipo END
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
                                	//TODO lanca erro por nao possuir first do CNDB
                                    System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nFirst de CNDB n�o encontrado.");
                                }
                        	}else {
                        		//TODO lanca erro por nao possuir first do BLOCO
                                System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nFirst de Bloco n�o encontrado.");
                        	}
                        } else {
                            //TODO lanca erro por nao conseguir processar o token THEN dentro da sintaxe esperada para COND
                            System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken THEN não encontrado.");
                        }

                    } else {
                        //TODO lanca erro por nao conseguir processar o token R_PAR dentro da sintaxe esperada para COND
                        System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken R_PAR não encontrado.");
                    }
            	}else {
            		//TODO lanca erro por nao vir o first de EXPLO
    	            System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken n�o esperado, first EXPLO");
            	}
            } else {
                //TODO lanca erro por nao conseguir processar o token L_PAR dentro da sintaxe esperada para COND
                System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken L_PAR não encontrado.");
            }

        } else {
            //TODO lanca erro por nao conseguir processar o token IF dentro da sintaxe esperada para COND
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
        		//TODO lanca erro por nao possuir o FIRST do Bloco
                System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nFirst de Bloco n�o encontrado.");
        	}
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
                        //TODO lanca erro por nao conseguir processar o token TERM dentro da sintaxe esperada para DECLARE
                        System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken TERM não encontrado.");
                    }

                } else {
                    //TODO lanca erro por nao conseguir processar o token TYPE dentro da sintaxe esperada para DECLARE
                    System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken TYPE não encontrado.");
                }

            } else {
                //TODO lanca erro por nao conseguir processar o token ID dentro da sintaxe esperada para DECLARE
                System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken ID não encontrado.");
            }

        } else {
            //TODO lanca erro por nao conseguir processar o token DECLARE dentro da sintaxe esperada para DECLARE
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
				//TODO lanca erro por nao encontrar o first de Explo
	            System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nFirst de Explo n�o encontrado.");
			}
		}else {
			if (firstFollow.isFollowFvallog(token)) {
				lexico.resetLastToken(token);
			}else {
				//TODO lanca erro por nao encontrar o first de Explo
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
				//TODO lanca erro por nao encontrar o first de Expnum
	            System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nFirst de Expnum n�o encontrado.");
			}
		} else {
			//TODO lanca erro por nao encontrar o first de Opnum
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
			//TODO lanca erro por nao possuir um sinal de + ou de -
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

			//TODO lanca erro por nao possuir um ID, NUM_INT ou NUM_FLOAT
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando um identificador, ou um n�mero.");
		}
	}

	private void procFnumInt() throws IOException {
		token = lexico.nextToken();

		if(firstFollow.isFirstOpnum(token)) {
			lexico.resetLastToken(token);
			procOpnum();
			
			if(firstFollow.isFirstFopnum_1(token)) {
				procFopnum_1();
			}else {
				//TODO lanca erro por nao possuir um MULTDIV
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando um operador (* ou /).");
			}
		}else{
			//TODO lanca erro por nao possuir um ADDSUB
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando um operador (+ ou -).");
		}

	}

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
				//TODO lanca erro por nao possuir First de Fexpnum_2
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nFirst de Fexpnum_2 n�o encontrado.");
			}
		}else{
			//TODO lanca erro por nao possuir um L_PAR, ID, NUM_INT ou NUM_FLOAT
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando um (, ou um identificador, ou um n�mero.");
		}
	}

	private void procFexpnum_2() throws IOException{
		token = lexico.nextToken();

		if(token.getTokenType().equals(TokenType.RELOP)) {
			
			token = lexico.nextToken();
			if(firstFollow.isFirstExpnum(token)) {
				lexico.resetLastToken(token);
				procExpnum();
			}else {
				//TODO lanca erro por nao possuir um L_PAR, ID, NUM_INT ou NUM_FLOAT
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando um (, ou um identificador, ou um n�mero.");
			}
		}else{
			if(firstFollow.isFollowFexpnum_2(token)) {
				lexico.resetLastToken(token);
			}else {
				//TODO lanca erro por nao possuir um TERM
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando um terminal.");
			}
		}
	}

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
				//TODO lanca erro por nao possuir um L_PAR, ID, NUM_INT ou NUM_FLOAT
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando um (, ou um identificador, ou um n�mero.");
			}
		}else {
			//TODO lanca erro por nao possuir um L_PAR, ID, NUM_INT ou NUM_FLOAT
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nEst� faltando um (, ou um identificador, ou um n�mero.");
		}
	}

	private void procFexpnum_3() throws IOException{
		token = lexico.nextToken();

		if(token.getTokenType().equals(TokenType.RELOP)){
			token = lexico.nextToken();
			
			if(firstFollow.isFirstExpnum(token)){
				lexico.resetLastToken(token);
				procExpnum();
			}else{
				//TODO lanca erro por nao possuir um L_PAR, ID, NUM_INT ou NUM_FLOAT
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
			}
		}else{
			if(firstFollow.isFollowFexpnum_3(token)) {
				lexico.resetLastToken(token);
			}else {
				//TODO lanca erro por nao follow de Fexpnum_3
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nFollow de Fexpnum_3 n�o encontrado.");
			}
		}
	}

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
				//TODO lanca erro
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
			}
		}else{
			//TODO lanca erro
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
		}
	}
	
	private void procFexpnum() throws IOException{
		token = lexico.nextToken();
		
		if(token.getTokenType().equals(TokenType.R_PAR)){
			token = lexico.nextToken();
			
			if(firstFollow.isFirstFrpar(token)) {
				lexico.resetLastToken(token);
				procFrpar();
			
			}else{
				//TODO lanca erro
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
			}
		}else{
			//TODO lanca erro
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
		}
	}
	
	private void procFrpar() throws IOException{
		token = lexico.nextToken();
		
		if(token.getTokenType().equals(TokenType.RELOP)) {
			token = lexico.nextToken();
			
			if(firstFollow.isFirstExpnum(token)) {
				lexico.resetLastToken(token);
				procExpnum();
			}else {
				//TODO lanca erro
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
			}
		}else {
			lexico.resetLastToken(token);
		}
	}
	
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
							//TODO lanca erro
							System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
						}
					}else{
						//TODO lanca erro
						System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
					}
				}else{
					//TODO lanca erro
					System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
				}
		} else if(token.getTokenType().equals(TokenType.RELOP)) {
			token = lexico.nextToken();
			
			if (firstFollow.isFirstExpnum(token)) {
				lexico.resetLastToken(token);
				procExpnum();
			}
		} else {
			//TODO lanca erro
			System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
		}
	}

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
				//TODO lanca erro por nao possuir First de Fexpnum_3
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nFirst de Fexpnum_3 nao encontrado.");
			}
		}else {
			//TODO lanca erro por nao possuir First de Expnum
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
			//TODO lanca erro por não ser FOR nem WHILE
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
									//Todo Processa o Bloco
									procBloco();
								} else {
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
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken não é ID.");
			}
		} else {
			//TODO lanca erro por não ser FOR nem WHILE
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
					//TODO lanca erro.
					System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken não é ')'");
				}
			}else {
				//TODO First de Expnum nao encontrado.
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
			//TODO First de EXPNUM nao encontrado.
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
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken não é L_PAR");
			}
		} else {
				//TODO lanca erro.
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
			}
		} else if (token.getTokenType().equals(TokenType.ID)) {
			token = lexico.nextToken();

			if (firstFollow.isFirstFid1(token)) {
				lexico.resetLastToken(token);
				procFid_1();
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
						}
					}
				}
			}
		} else {
				//TODO lanca erro
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
						//TODO lanca erro por nao conseguir processar o token TERM dentro da sintaxe esperada para ATRIB
						System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken TERM não encontrado.");
					}
				}else {
					//TODO lanca erro por nao conseguir localizar o first de Exp
					System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nErro por nao conseguir localizar o first de Exp.");
				}
			} else {
				//TODO lanca erro por nao conseguir processar o token ATTRIB dentro da sintaxe esperada para ATRIB
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken ATTRIB não encontrado.");
			}
		} else {
			//TODO lanca erro por nao conseguir processar o token ID dentro da sintaxe esperada para ATRIB
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
			}
		} else if (token.getTokenType().equals(TokenType.L_PAR)) {
			token = lexico.nextToken();

			if (firstFollow.isFirstFlpar(token)) {
				lexico.resetLastToken(token);
				procFlpar();
			}
		} else if (!token.getTokenType().equals(TokenType.LITERAL)) {
			//TODO lanca erro
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
				//TODO lanca erro
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
			}
		} else {
			//TODO lanca erro
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
			//TODO lanca erro
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
				//TODO procExpnum();
				//procExpnum();
			} else {
				//TODO lanca erro
				System.out.println("Linha: " + token.getLinha() + "\nColuna: " + token.getColuna() + "\nToken incorreto.");
			}
		} else {
			lexico.resetLastToken(token);
		}
	}
}
