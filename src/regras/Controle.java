package regras;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


import regras.terrenos.Territorio;

import regras.terrenos.Celula;

class Controle extends Observable
{
	//Armazena os observadores
	private List<Observer> observadores = new ArrayList<>();
	
	// Banco que administra o dinheiro
	double banco = 50000.0;

	//Armazena Informações dos dados
	Dados dados;
	
	// Informações dos jogadores
	Peao jogadores[] = new Peao[6];
	Peao jogadorProtegido;
	boolean votosGameOver[] = new boolean[6];
	
	//Mapa do tabuleiro onde será realizado as jogadas
	Mapa map;
	
	// Baralho do jogo
	Baralho deck;

	// Guarda a compra da carta
	String compra;

	// Guarda a vez do Próximo
	int vez;

	//Lista para observadores
	List<Object> infos = new ArrayList<Object>(); 
	
	//Info da partida tipos
	//0 - Não existe transaçoes
	//1 - Terreno com dono
	//2 - Efeito de carta Banco
	//3 - Efeito de carta especial
	//4 - Banco Imposto Receita
	int tipo;
	double cobranca;
	double cheque;
	double acumulo;
	Color dono;
	boolean volta;
	
	public Controle()
	{
		registrar(Registros.GetRegistros());
		NewGame();
	}
	
	
	///////////////////////////////Realiza as ações do jogo//////////////////////////////////////
	
	public Color RealizaTurno()
	{		
		dados.RolarDados();
		
		compra = null;
		
		if(map.VerificaPeaoPrisao(jogadores[vez].getCor()))
		{
			tipo = 0;
			return VerificaCumprimentoPena();
		}
		else
		{
			if(!dados.LimiteDeSequencia())
			{
				return RealizarMovimento();
			}
			else
			{
				if(jogadorProtegido != null)
				{
					if(!jogadorProtegido.getCor().equals(jogadores[vez].getCor()))
					{
						tipo = 0;
						return MandaParaCadeia();
					}
					else 
					{
						jogadorProtegido = null;
						deck.RecuperaTrunfo();
						return RealizarMovimento();
					}
				}
				else
				{
					tipo = 0;
					return MandaParaCadeia();
				}
			}
		}
				
	}
	
	public Color RealizaTurnoSimulado(int dado1, int dado2)
	{		
		dados.SimulaDados(dado1, dado2);
		
		compra = null;
		
		if(map.VerificaPeaoPrisao(jogadores[vez].getCor()))
		{
			tipo = 0;
			return VerificaCumprimentoPena();
		}
		else
		{
			if(!dados.LimiteDeSequencia())
			{
				return RealizarMovimento();
			}
			else
			{
				if(jogadorProtegido != null)
				{
					if(!jogadorProtegido.getCor().equals(jogadores[vez].getCor()))
					{
						tipo = 0;
						return MandaParaCadeia();
					}
					else 
					{
						jogadorProtegido = null;
						deck.RecuperaTrunfo();
						return RealizarMovimento();
					}
				}
				else
				{
					tipo = 0;
					return MandaParaCadeia();
				}
			}
		}
	}
	
 	public boolean CompraDeTerreno(Color peao)
	{
 		for(int i=0; i < jogadores.length; i++)
		{
			if(jogadores[i].getCor().equals(peao))
			{
				double valorPagar = map.OrçamentoTerreno(jogadores[i].posicao);
				
				if(valorPagar > jogadores[i].carteira)
				{
					return false;
				}
				
				jogadores[i].carteira -= valorPagar;
				banco += valorPagar;
				
				map.CompraTerreno(jogadores[i].getPosicao(), jogadores[i].getCor());
				jogadores[i].AdicionarPropriedade(jogadores[i].getPosicao());
				
				AtualizaRegistros();
				return true;
			}
		}
 		
 		return false;
		
	}
 	
 	public boolean CompraDePropriedade(Color peao)
	{
 		for(int i=0; i < jogadores.length; i++)
		{
 				
			if(jogadores[i].getCor().equals(peao))
			{
	 			if(!map.ValidaCompraPropriedadeTerreno(jogadores[i].posicao))
	 			{
	 				return false;
	 			}
	 			
				double valorPagar = map.OrçamentoPropriedadeTerreritorio(jogadores[i].posicao);
				
				if(valorPagar > jogadores[i].carteira)
				{
					return false;
				}
				
				jogadores[i].carteira -= valorPagar;
				banco += valorPagar;
				
				map.CompraPropriedadesTerritorio(jogadores[i].getPosicao());
				
				AtualizaRegistros();
				return true;
			}
		}
 		
 		return false;
		
	}
	
 	private boolean VendaDeTerreno()
	{
		
		Celula bem = jogadores[vez].VenderPropriedade();
		
		if(bem == null)
			return false;
		
		double valorPagar = map.VendaTerreno(bem);
		
		jogadores[vez].carteira += valorPagar;
		banco -= valorPagar;
		
		AtualizaRegistros();
		return true;
	}
	
	private Color RealizarMovimento()
	{
		tipo = 0;
		int posicao = 0;
		boolean direitoRepetirJogada = dados.DentroDeSequencia();

		// localiza posiçao do peao
		for (; posicao < 40; posicao++) {
			if (this.map.movimento[posicao].VerificaExiste(jogadores[vez].posicao)) {
				break;
			}
		}

		// Limpa o terreno antes ocupado
		this.map.movimento[posicao].LimpaTerreno(jogadores[vez].posicao);

		// realiza avanço no tabuleiro
		if (posicao + dados.getResultado() < 40) {

			jogadores[vez].posicao = this.map.movimento[posicao + dados.getResultado()].PrimeiroQuadranteLivre();
			volta = false;
		} else {
			banco -= 200;
			jogadores[vez].carteira += 200.0;
			jogadores[vez].posicao = this.map.movimento[posicao + dados.getResultado() - 39].PrimeiroQuadranteLivre();
			volta = true;
		}
				
		if(map.VerificaArmadilhaCadeia(jogadores[vez].getPosicao()))
		{
			if(jogadorProtegido != null)
			{
				if(!jogadorProtegido.getCor().equals(jogadores[vez].getCor()))
				{
					Color prox = MandaParaCadeia();
					AtualizaRegistros();
					return prox;
				}
			}
			else 
			{
				jogadorProtegido = null;
				deck.RecuperaTrunfo();
			}
			
		}
		
		//Verifica o tipo de terreno e a ação que será realizada
		dono = map.DonoTerreno(jogadores[vez].posicao);
		
		if(dono != null && !dono.equals(Color.white))
		{
			this.compra = map.VerificaImagemTerreno(jogadores[vez].posicao);
			
			if(!dono.equals(jogadores[vez].getCor()))
			{
				cobranca = map.CobrancaTerreno(jogadores[vez].posicao);
				
				if(map.CobrancaEmpresa(jogadores[vez].posicao))
					cobranca *= dados.getResultado();
					
				jogadores[vez].carteira -= cobranca;
				
				for(int i = 0; i < jogadores.length; i++)
				{
					if(jogadores[i].getCor().equals(dono))
					{
						jogadores[i].carteira += cobranca;
					}
				}
				
				tipo = 1;
			}
		}
		else if(dono != null && dono.equals(Color.white))
		{
			
			if(map.VerificaZonaDeCompra(jogadores[vez].getPosicao()))
			{
				// Realiza a compra da vez ao se movimentar
				boolean retorno = CompraDaCarta(vez);
				direitoRepetirJogada = dados.DentroDeSequencia() && retorno;
			}			
			else
			{
				//Realiza imposto ou lucro
				cheque = map.LucrosOuImpostos(jogadores[vez].posicao);
				jogadores[vez].carteira += cheque;
				banco -= cheque;
				
				if(cheque != 0)tipo = 4;
			}
		}
				
		ValidaJogadorPartida();
		
		if(!direitoRepetirJogada)
		{
			// vai para a vez do próximo jogador
			ProximoJogador();
		}

		AtualizaRegistros();
		return jogadores[vez].cor;
	}
	
	private void AtualizaRegistros()
	{
		setChanged();
        notifyObservers();
	}
	
	private void ProximoJogador()
	{
		do
		{
			if (vez == 5)
				vez = 0;
			else
				vez++;
		}while(jogadores[vez].falencia);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	
	//////////////////////////////Manipulação Baralho////////////////////////////////////////////
	
	public boolean JogadorLivre()
	{
		return !map.VerificaPeaoPrisao( jogadores[vez].getCor());
	}
	
	//Caso o jogador é enviado para a cadeia pela compra de carta do baralho o retorno é false
	//impedindo a possibilidade dele jogar os dados novamente
	private boolean CompraDaCarta(int vez)
		{
			SorteReves compra = deck.ComprarCarta();
			this.compra = compra.getImagem();

			if (compra.valor == 0) {
				if (jogadores[vez] == jogadorProtegido)
				{
					deck.RecuperaTrunfo();
					jogadorProtegido = null;
				}
				else
				{
					// enviar para cadeia
					MandaParaCadeia();
					return false;
				}
				tipo = 0;
			}
			else if (compra.valor == 1)
			{
				// ticked de sair da cadeia
				jogadorProtegido = jogadores[vez];
				tipo = 0;
			}
			else
			{

				acumulo = 0;

				if (compra.aposta)
				{
					for (int i = 0; i < 6; i++)
					{
						if (i != vez && jogadores[i].falencia != true)
						{
							acumulo += compra.valor;
							jogadores[i].carteira -= compra.valor;
						}
					}

					jogadores[vez].carteira += acumulo;
					tipo = 3;
				}
				else
				{
					acumulo = compra.valor;
					jogadores[vez].carteira += compra.valor;
					banco -= compra.valor;
					tipo = 2;
				}
				
			}
			return true;
		}
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	
	////////////////////////////////Manipulação Cadeia////////////////////////////////////////////
	
	private Color VerificaCumprimentoPena()
	{
		if(dados.DentroDeSequencia())
		{
			map.LiberaPrisioneiro(jogadores[vez].getCor());	
			dados.ZeraSequencia();
			return RealizarMovimento();
		}
		else
		{
			//Cumpriu a pena total
			if(map.TempoPrisioneiro(jogadores[vez].getCor()) == 0)
			{
				//Paga valor para o Banco
				jogadores[vez].carteira -= 50.0;
				banco += 50;
				
				//realiza normal o movimento
				return RealizarMovimento();
			}
		}
		
		// vai para a vez do próximo jogador
		ProximoJogador();
		return jogadores[vez].getCor();
		
	}
		
	private Color MandaParaCadeia()
	{
		//Manda o jogador da vez para cadeia
		map.RegistraPrisioneiro(jogadores[vez].getCor());
		
		for(int pos=0 ; pos < 40 ; pos++)
		{
			if(this.map.movimento[pos].getPosicao().equals(map.getPosicaoCadeia()))
			{
				jogadores[vez].posicao =this.map.movimento[pos].PrimeiroQuadranteLivre();
			}
		}
		
		// vai para a vez do próximo jogador
		ProximoJogador();
		return jogadores[vez].getCor();
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////
	
	///////////////////////////////////Compartilhados com Painel//////////////////////////////////
	
	
 	public String GetImagemTerreno()
	{
		if(dados.DentroDeSequencia() && !map.VerificaPeaoPrisao(jogadores[vez].getCor()))
			return map.VerificaImagemTerreno(jogadores[vez].getPosicao());
		else if(vez==0)
			return map.VerificaImagemTerreno(jogadores[5].getPosicao());
		else
			return map.VerificaImagemTerreno(jogadores[vez - 1].getPosicao());
	}	
	
	public String ComprarCartaImagem()
	{
		if (compra != null)
			return compra;

		return null;
	}

	public Color getCorJogador(int pos)
	{
		return jogadores[pos].getCor();
	}
	
	public Color getCorJogadorAnterior()
	{
		if(dados.sequencia == 0)
		{
			for(int i = vez -1; i > -1; i--)
			{
				if(!jogadores[i].falencia)				
					return jogadores[i].getCor();
			}
			
			if(vez < 5)
				for(int i = 5; i > vez; i--)
				{
					if(!jogadores[i].falencia)
						return jogadores[i].getCor();
				}
		}
		else
			return jogadores[vez].getCor();;
		
		return null;
	}
	
	public Color getCorJogadorProximo()
	{
		for(int i = vez + 1; i < 6; i++)
		{
			if(!jogadores[i].falencia)				
				return jogadores[i].getCor();
		}
		
		if(vez > 0)
			for(int i = 0; i > vez; i++)
			{
				if(!jogadores[i].falencia)
					return jogadores[i].getCor();
			}
		
		return null;
	}
	
	public String getImgJogador(int pos)
	{
		return jogadores[pos].getImg();
	}
	
	public String getImgJogador(Celula pos)
	{
		for(int i = 0; i < jogadores.length; i++)
			if(jogadores[i].posicao.equals(pos))
				return jogadores[i].getImg();
		
		return null;
	}


	public List<Celula> getPosicaoJogadores()
	{
		List<Celula> lista = new ArrayList<Celula>();

		for (int i = 0; i < 6; i++) 
		{
			if (!jogadores[i].falencia)
				lista.add(jogadores[i].posicao);
		}

		return lista;
	}

	public Celula[][] getEspacos()
	{
		return map.espacos;
	}
	
 	public Color getColorVez()
	{
		return jogadores[vez].getCor();
	}
	
 	public Color DonoTerreno(Color peao)
	{		
		for(int i = 0; i < jogadores.length; i++)
		{
			if(jogadores[i].getCor().equals(peao))
			{
				return map.DonoTerreno(jogadores[i].getPosicao());
			}
		}
		return null;
	}
	
 	public boolean VerificaTerrenoEmpresaOuTerritorio(Celula local) 
 	{
 		return map.VerificaTerrenoDeCompra(local);
 	}
 	
	//////////////////////////////////////////////////////////////////////////////////////////////
	
	///////////////////////////////////Inicializador e Finalizador////////////////////////////////
	
	public void NewGame()
	{
		// Configuraçoes iniciais
		map = new Mapa();
		InicializaJogadores();
		InicializaVotos();
		deck = new Baralho();
		dados = new Dados();
		compra = null;
		jogadorProtegido = null;
		
		// Indica o primeiro
		vez = 0;
		AtualizaRegistros();		
	}
	
	public void RetiraJogadores(int numeroParticipante)
	{
		for(int i = numeroParticipante; i < jogadores.length ; i++)
		{
			jogadores[i].setFalencia(true);
			votosGameOver[i] = true;
		}
		
		AtualizaRegistros();
	}
	
 	public boolean GameOver()
	{
		int count = 0;
		
		//verifica por falência
		for(int i=0; i < jogadores.length; i++)
			if(jogadores[i].falencia)
				count++;
		if(count>=jogadores.length-1)
			return true;
		
		//Verifica por votação
		count = 0;
		for(int i=0; i < votosGameOver.length; i++)
			if(votosGameOver[i])
				count++;
		if(count==votosGameOver.length)
			return true;
		
		return false;
	}
	
 	public void Carregar(String local)
 	{
 		BufferedReader buffRead;
        String linha = "";
   
        try 
		{
			
			buffRead = new BufferedReader(new FileReader(local));
					
										
				linha = buffRead.readLine();
				banco = Double.parseDouble(linha);
	
				linha = buffRead.readLine();
				dados.dado1 = Integer.parseInt(linha);
				linha = buffRead.readLine();
				dados.dado2 = Integer.parseInt(linha);
				
				linha = buffRead.readLine();
				if (!linha.equals("-1"))
				{
					deck.RetiraTrunfo();
					jogadorProtegido = jogadores[Integer.parseInt(linha)];
				}
				else
					jogadorProtegido = null;				
				
				linha = buffRead.readLine();
				if (!linha.equals("null") && linha.startsWith("../img/sorteReves/"))
					compra = linha;
								
				linha = buffRead.readLine();

				vez = Integer.parseInt(linha);
				
				linha = buffRead.readLine();
				linha = buffRead.readLine();
								
				for(int jogador = 0; jogador<6;jogador++)
				{		
					jogadores[jogador].carteira = Double.parseDouble(linha);
							
					linha = buffRead.readLine();
					
					this.map.movimento[jogador].LimpaTerreno(jogadores[vez].posicao);
					
					jogadores[jogador].posicao = map.movimento[Integer.parseInt(linha)].PrimeiroQuadranteLivre();
								
					linha = buffRead.readLine();								

					if(linha.equals("false"))
						jogadores[jogador].falencia = false;
					else 
						jogadores[jogador].falencia = true;
								
					linha = buffRead.readLine();
							
					if(linha.equals("false"))
						votosGameOver[jogador] = false;
					else 
						votosGameOver[jogador] = true;
							
					linha = buffRead.readLine();
				
					if(linha.equals("cadeia"))
					{
						map.cadeia.RecebePrisioneiro(jogadores[jogador].getCor());
					}
								
					linha = buffRead.readLine();	
					
					int controlePropriedade = -1;
					while(!linha.equals("------------------------------------------------------")) 
					{
						map.CompraTerreno(map.getCelulaTerreno(linha), jogadores[jogador].getCor());
						jogadores[jogador].AdicionarPropriedade(map.getCelulaTerreno(linha));
						controlePropriedade ++;
						
						if(map.VerificaNumeroDeProPriedades(map.getCelulaTerreno(linha))!=-1)
						{								
							linha  = buffRead.readLine();
							int numprop = Integer.parseInt(linha);
							
							for(int i=0; i < numprop; i++)
								map.CompraPropriedadesTerritorio(jogadores[jogador].propriedades.get(controlePropriedade));									 										
						}
						
						linha = buffRead.readLine();
					}
					
					linha = buffRead.readLine();					
				}							
				buffRead.close();

		} catch (Exception e) {
			
			e.printStackTrace();
		}

		
	     AtualizaRegistros();
				      
 	}
 	
 	public void Salvar(String local)
 	{
 		File arq = new File(local + ".txt");
 		
        try {

            arq.createNewFile();

            FileWriter fileWriter = new FileWriter(arq, false);
        
            PrintWriter printWriter = new PrintWriter(fileWriter);

            printWriter.println(banco);
            
            printWriter.println(dados.getValorDado1());
            printWriter.println(dados.getValorDado2());
       
            if(jogadorProtegido!=null)
            {
            	for(int i = 0; i<jogadores.length; i++)
            		if(jogadores[i].getCor().equals(jogadorProtegido.getCor()))
            			{
            			    printWriter.println(i);    
            			    break;
            			}
            }

            else
            	printWriter.println("-1");
            
            if(compra != null)
            	printWriter.println(compra);
            else
            	printWriter.println(map.VerificaImagemTerreno(jogadores[vez].posicao));
            
            printWriter.println(vez);
            
            printWriter.println("------------------------------------------------------");
           
            for(int i=0; i<jogadores.length; i++)
            {
            	printWriter.println(jogadores[i].carteira);
            	
            	for (int posicao = 0; posicao < 40; posicao++) 
            	{
        			if (this.map.movimento[posicao].VerificaExiste(jogadores[i].posicao)) 
        			{
                    	printWriter.println(posicao);
        				break;
        			}
        		}
            	
            	printWriter.println(jogadores[i].falencia);
            	printWriter.println(votosGameOver[i]);
            	
            	if(map.VerificaPeaoPrisao(jogadores[i].getCor()))
            		printWriter.println("cadeia");
            	else
            		printWriter.println("livre");
            	
            	for(Celula cel: jogadores[i].propriedades)
            	{
            		printWriter.println(map.getNomeTerreno(cel));
            		
            		if(map.VerificaNumeroDeProPriedades(cel) != -1)
                		printWriter.println(map.VerificaNumeroDeProPriedades(cel));            		
            	} 
            	
            	printWriter.println("------------------------------------------------------");
            }
                      
            //o método flush libera a escrita no arquivo
            printWriter.flush();

            //No final precisamos fechar o arquivo
            printWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        
 	}
 	
	private void InicializaJogadores()
	{
		int casa = 0;
		jogadores[0] = new Peao(Color.red, "../img/pinos/pin0.png", 2458.0, false,
				map.movimento[casa].PrimeiroQuadranteLivre());
		
		jogadores[1] = new Peao(Color.blue, "../img/pinos/pin1.png", 2458.0, false,
				map.movimento[casa].PrimeiroQuadranteLivre());
		
		jogadores[2] = new Peao(new Color(255, 102, 0), "../img/pinos/pin2.png", 2458.0, false,
				map.movimento[casa].PrimeiroQuadranteLivre());
		
		jogadores[3] = new Peao(Color.yellow, "../img/pinos/pin3.png", 2458.0, false,
				map.movimento[casa].PrimeiroQuadranteLivre());
		
		jogadores[4] = new Peao(new Color(102, 0, 153), "../img/pinos/pin4.png", 2458.0, false,
				map.movimento[casa].PrimeiroQuadranteLivre());
		
		jogadores[5] = new Peao(Color.gray, "../img/pinos/pin5.png", 2458.0, false,
				map.movimento[casa].PrimeiroQuadranteLivre());
	}

	private void InicializaVotos()
	{
		//Zera votação
		for(int i=0; i < votosGameOver.length; i++)
		{
			if(jogadores[i].falencia)
				votosGameOver[i] = true;
			else
				votosGameOver[i] = false;
		}
	}
	
	public void VotoEncerramento(Color peao)
	{
		for(int i=0; i < jogadores.length; i++)
		{
			if(jogadores[i].getCor().equals(peao))
			{
				votosGameOver[i] = true;
				AtualizaRegistros();
				break;
			}
		}
	}
	
	public boolean VerificaJaVotou(Color peao)
	{
		for(int i=0; i < jogadores.length; i++)
		{
			if(jogadores[i].getCor().equals(peao))
			{
				return votosGameOver[i];
			}
		}
		return false;
	}

	private void ValidaJogadorPartida()
	{
		if (jogadores[vez].carteira < 0)
		{
			boolean loop;
			// PerdePropriedades
			do
			{				
				loop = VendaDeTerreno();
				
			}while(jogadores[vez].carteira < 0 && loop);
			
			// Declara falencia
			if (jogadores[vez].carteira < 0)
			{
				jogadores[vez].falencia = true;
				votosGameOver[vez] = true;
			}
			
		}
		
	}
	
	public void notifyObservers()
	{		
		
        for (Observer observer : this.observadores)
        {
        	if(observer.getClass().equals(Registros.GetRegistros().getClass()))
        		observer.update(this, infos);
        }
	}

	public void registrar(Observer observer) 
	{
		observadores.add(observer);
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////
	
}
