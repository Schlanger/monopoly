package gui;
import javax.swing.*;

import regras.Interador;
import regras.Registros;
import regras.terrenos.Celula;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.*;

public class Painel extends JPanel implements MouseListener
{
	
	//Dados das imagens usadas
	private Image i=null;//Painel
	private List<Image> peoes = new ArrayList<Image>();//Guarda a imagem dos peões que realmente estão no jogo
	
	//Informações dos dados
	private Image resultados[] = new Image[6];//Guarda as imagens dos dados
	private List<Celula> jogadores = new ArrayList<Celula>();//posicao dos jogadores presentes
	
	//Armazena carta da vez comprada
	private Image carta=null;
	
	//Armazena a cor do Turno do jogador
	private Color vez;
	private Color anterior;

	boolean demo;
	
	Map<Integer,JButton> buttonMap = new HashMap<Integer,JButton>();
	Map<Integer, JComboBox<Integer>> comboMap = new HashMap<Integer, JComboBox<Integer>>();
	
	public Painel()
	{
		//Padrao
		this.setLayout(null);
		//Armazenamento da exibição do tabuleiro
		try 
		{
			i = ImageIO.read(getClass().getResourceAsStream("../img/tabuleiro.png"));
		}
		catch(IOException e) {
		   System.out.println(e.getMessage());
		   System.exit(1);
		}

		//Inicializa o painel de dados
		ConfigDados();

		//Inicialização dos peões
		ConfigPeoes();
		vez = Interador.getInterador().getColorVez();
		anterior = Color.WHITE;
		
		demo = false;
		
		//Configuração de botoes
		ConfigCompraTerreno();
		ConfigCompraPropriedade();
		ConfigEncerrarPartida();
		ConfigSalvarPartida();
		ConfigCarregarPartida();
		ConfigSimularDados();
		ConfigJogarDados();
		ConfigComboBox();
		
    	addMouseListener(this);  	
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D gd2=(Graphics2D) g;
		
		//desenha o tabuleiro
		gd2.drawImage (i,0,0,null);
		
		//posiciona os peões
		for(int i= 0; i<jogadores.size(); i++)
		{
			gd2.drawImage(peoes.get(i),(int)jogadores.get(i).getX(),(int)jogadores.get(i).getY(),null);
		}
		
		//Desenha a carta comprada
		if(carta != null)
			gd2.drawImage (carta,140,140,150,200,null);
		
		//Desenha o espaço onde os dados estão localizados
		gd2.setColor(anterior);
		gd2.fill(new Rectangle2D.Double(150.0,420.0,140.0,130.0));
				
		//desenha os dados
		gd2.drawImage (resultados[Interador.getInterador().getValorDado1() - 1],160,430,50,50,null);
		gd2.drawImage (resultados[Interador.getInterador().getValorDado2() - 1],230,430,50,50,null);
		
		//Escreve informações dos players
		Font font = new Font("Serif", Font.PLAIN, 18);
        gd2.setFont(font);
        
        gd2.setColor(Color.black);
    	gd2.drawString("      Banco: " + Registros.GetBanco(), 310, 220 -18); 
        
        for(int i = 0; i < Registros.InfoPeoes().size(); i++)
        {
        	gd2.setColor(Registros.CoresInfoPeoes().get(i));
        	gd2.drawString(Registros.InfoPeoes().get(i), 310, 220 + (i*18)); 
        }
        
        font = new Font("Serif", Font.PLAIN, 14);
        gd2.setFont(font);
        gd2.setColor(anterior);
        gd2.drawString(Registros.Volta(), 310, 220 + (9*18)); 
    	gd2.drawString(Registros.Acao(), 310, 220 + (9*18) + 14); 
	}
	
		public void mouseEntered(MouseEvent e) {}
		
		public void mousePressed(MouseEvent e) {}
		
		public void mouseReleased(MouseEvent e) {}
		
		public void mouseExited(MouseEvent e) {}
		
		public void mouseClicked(MouseEvent e)
		{
			for(int i=0; i < Interador.getInterador().getEspacos().length; i++)
			{
				for(int j=0; j < Interador.getInterador().getEspacos()[i].length; j++)
				{
					if(Interador.getInterador().getEspacos()[i][j].EstaContida((double)e.getX(), (double)e.getY()))
					{
						if(Interador.getInterador().VerificaTerrenoEmpresaOuTerritorio(Interador.getInterador().getEspacos()[i][j]))
						{
							if(Registros.InfoTerreno(Interador.getInterador().getEspacos()[i][j])!=null)
								JOptionPane.showMessageDialog(this,Registros.InfoTerreno(Interador.getInterador().getEspacos()[i][j]));
						}
					}
				}
			}
			
			//JOptionPane.showMessageDialog(p,e.getX() + " " + e.getY());
			
		}
		
		private void ConfigDados()
		{
					
			//Armazena Possiveis valores dos dados
			try 
			{
				for(int i=0; i<6; i++)
				{
					resultados[i] = ImageIO.read(getClass().getResource(Interador.getInterador().getImgDado(i)));
				}
			}
			catch(IOException e) {
				System.out.println(e.getMessage());
				System.exit(1);
			}
		}
	
		public void ConfigPeoes()
		{	
			//Pega os participantes
			jogadores = Interador.getInterador().getPosicaoJogadores();
			
			//Armazenamento das exibiçoes dos peoes
			peoes.clear();
			try 
			{
				for(int pos = 0; pos<jogadores.size(); pos++)
					peoes.add(ImageIO.read(getClass().getResource(Interador.getInterador().getImgJogador(jogadores.get(pos)))));
			}
			catch(IOException e) {
				System.out.println(e.getMessage());
				System.exit(1);
			}
		}

		private void ConfigJogarDados()
		{
			JButton jogarDados = new JButton("Jogar Dados");
			jogarDados.setBounds(160,500,120,35);
			jogarDados.setBackground(vez);			  
			jogarDados.setFocusPainted(false);			
			
			if(vez.equals(Color.yellow))
				jogarDados.setForeground(Color.black);
			else
				jogarDados.setForeground(Color.white);
			jogarDados.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{ 
					anterior = vez;
					
					if(!Interador.getInterador().JogadorLivre())
					{
						JOptionPane.showMessageDialog(null,"Jogador Está na cadeia","ALERTA!!", JOptionPane.INFORMATION_MESSAGE);
					}										
					
					if(demo==false)
						vez = Interador.getInterador().RealizaTurno();
					else
						vez = Interador.getInterador().RealizaTurnoSimulado((int)comboMap.get(1).getSelectedItem(), (int)comboMap.get(2).getSelectedItem());
														
					jogarDados.setBackground(vez);
										
					if(vez.equals(Color.yellow))
						jogarDados.setForeground(Color.black);
					else
						jogarDados.setForeground(Color.white);
					
					ConfigPeoes();
						
					carta = null;
					try 
					{
						if(Interador.getInterador().ComprarCartaImagem()!=null)
							carta = ImageIO.read(getClass().getResource(Interador.getInterador().ComprarCartaImagem()));
						if(Interador.getInterador().GetImagemTerreno()!=null)
							carta = ImageIO.read(getClass().getResource(Interador.getInterador().GetImagemTerreno()));
					}
					catch(IOException ex) {
					   System.out.println(ex.getMessage());
					   System.exit(1);
					}
										
					//Botões de compra
					buttonMap.get(1).setBackground(anterior);
					buttonMap.get(3).setBackground(anterior);
					if(Interador.getInterador().DonoTerreno(anterior)==null)
					{
						buttonMap.get(1).setVisible(true);
						buttonMap.get(3).setVisible(false);
					}
					else
					{
						if(Interador.getInterador().DonoTerreno(anterior).equals(anterior))
						{
							buttonMap.get(1).setVisible(false);	
							buttonMap.get(3).setVisible(true);
						}
						else
						{
							buttonMap.get(1).setVisible(false);	
							buttonMap.get(3).setVisible(false);
						}
					}
						
					//Botão de Voto
					buttonMap.get(2).setBackground(anterior);					
					if(!Interador.getInterador().VerificaJaVotou(anterior))
					{						
						buttonMap.get(2).setVisible(true);
					}
					else
					{
						buttonMap.get(2).setVisible(false);
					}
					
					repaint(); 
					
					if(Interador.getInterador().GameOver())
					{
						RecarregaJogo();
						BancoImobiliario.p.m = new Menu(BancoImobiliario.p);
						BancoImobiliario.p.m.setVisible(true);
					}
				} 
			}); 
			
			buttonMap.put(0, jogarDados);
			this.add(jogarDados);
		}

		private void ConfigCompraTerreno()
		{
			JButton comprarTerreno = new JButton("Comprar Terreno");
			comprarTerreno.setBounds(320,500,120,35);
			comprarTerreno.setBackground(anterior);			  
			comprarTerreno.setFocusPainted(false);			
			
			if(anterior.equals(Color.white))
				comprarTerreno.setVisible(false);
				
			if(vez.equals(Color.yellow))
				comprarTerreno.setForeground(Color.black);
			else
				comprarTerreno.setForeground(Color.white);
			
			comprarTerreno.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{ 
					if(Interador.getInterador().DonoTerreno(anterior) != null)
					{
						JOptionPane.showMessageDialog(null,"Terreno possui dono","ALERTA!", JOptionPane.INFORMATION_MESSAGE);
					}
					else
					{
						boolean avalia =Interador.getInterador().CompraDeTerreno(anterior);
						if(avalia)
							JOptionPane.showMessageDialog(null,"Compra realizada com sucesso","SUCESSO!", JOptionPane.INFORMATION_MESSAGE);
						else
							JOptionPane.showMessageDialog(null,"Saldo insuficiente para realizar a compra","FALHA!", JOptionPane.INFORMATION_MESSAGE);
					}
					
					comprarTerreno.setVisible(false);
					repaint();
				} 
			}); 
			
			buttonMap.put(1, comprarTerreno);
			this.add(comprarTerreno);
		}
		
		private void ConfigCompraPropriedade()
		{
			JButton comprarPropriedade = new JButton("Comprar Propriedade");
			comprarPropriedade.setBounds(320,500,120,35);
			comprarPropriedade.setBackground(anterior);			  
			comprarPropriedade.setFocusPainted(false);			
			
			if(anterior.equals(Color.white))
				comprarPropriedade.setVisible(false);
				
			if(vez.equals(Color.yellow))
				comprarPropriedade.setForeground(Color.black);
			else
				comprarPropriedade.setForeground(Color.white);
			
			comprarPropriedade.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{ 
					boolean avalia =Interador.getInterador().CompraDePropriedade(anterior);
					if(avalia)
						JOptionPane.showMessageDialog(null,"Compra realizada com sucesso","SUCESSO!", JOptionPane.INFORMATION_MESSAGE);
					else
						JOptionPane.showMessageDialog(null,"Saldo insuficiente para realizar a compra","FALHA!", JOptionPane.INFORMATION_MESSAGE);
					
					comprarPropriedade.setVisible(false);
					repaint();
				} 
			}); 
			
			buttonMap.put(3, comprarPropriedade);
			this.add(comprarPropriedade);
		}
		
		private void ConfigEncerrarPartida()
		{
			JButton voto = new JButton("Votar Encerrar");
			voto.setBounds(450,500,120,35);
			voto.setBackground(anterior);			  
			voto.setFocusPainted(false);			
			
			if(anterior.equals(Color.white))
				voto.setVisible(false);
				
			if(vez.equals(Color.yellow))
				voto.setForeground(Color.black);
			else
				voto.setForeground(Color.white);
			
			voto.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{ 
					voto.setVisible(false);
					
					Interador.getInterador().VotoEncerramento(anterior);
					
					if(Interador.getInterador().GameOver())
					{
						RecarregaJogo();
						BancoImobiliario.p.m = new Menu(BancoImobiliario.p);
						BancoImobiliario.p.m.setVisible(true);
					}
					repaint();
				} 
			}); 
			
			buttonMap.put(2, voto);
			this.add(voto);
		}
		
		private void ConfigSalvarPartida()
		{
			JButton salvar = new JButton("Salvar Jogo");
			salvar.setBounds(450,120,120,35);		  
			salvar.setFocusPainted(false);			
						
			salvar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{ 
					JFileChooser chooserArquivo = new JFileChooser();
		            int escolha = chooserArquivo.showSaveDialog(getParent());		            
		            if (escolha == JFileChooser.APPROVE_OPTION)
		            {
						Interador.getInterador().Salvar(chooserArquivo.getSelectedFile().getAbsolutePath());		                		                
		            }
				} 
			}); 
			
			buttonMap.put(4, salvar);
			this.add(salvar);
		}
		
		private void ConfigCarregarPartida()
		{
			JButton carregar = new JButton("Carregar Jogo");
			carregar.setBounds(320,120,120,35);		  
			carregar.setFocusPainted(false);			
						
			carregar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{ 
					
					JFileChooser chooserArquivo = new JFileChooser();
					int escolha = chooserArquivo.showOpenDialog(getParent());
					
					if (escolha == JFileChooser.APPROVE_OPTION)
					{
						Interador.getInterador().NewGame();
						
		                Interador.getInterador().Carregar(chooserArquivo.getSelectedFile().getAbsolutePath());
		                
		                Refresh();
						
						repaint();		                
		            }
										
				} 
			}); 
			
			buttonMap.put(5, carregar);
			this.add(carregar);
		}
		
		private void ConfigSimularDados()
		{
			JButton simulador = new JButton("Simular Dados");
			simulador.setBounds(160,380,120,35);		  
			simulador.setFocusPainted(false);			
						
			simulador.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{ 
					if(demo==false)
					{
						demo=true;
						simulador.setText("Dado Normal");
						comboMap.get(1).setVisible(true);
						comboMap.get(2).setVisible(true);
						repaint();
					}
					else
					{
						demo=false;
						simulador.setText("Simular Dados");
						comboMap.get(1).setVisible(false);
						comboMap.get(2).setVisible(false);
						repaint();
					}
				} 
			}); 
			
			buttonMap.put(6, simulador);
			this.add(simulador);
		}
		
		private void ConfigComboBox()
		{
			JComboBox<Integer> dado1 = new JComboBox<Integer>();
			JComboBox<Integer> dado2 = new JComboBox<Integer>();
			 
			for(int i = 0; i < 6; i++)
			{
				dado1.addItem(i+1);
				dado2.addItem(i+1);
			}
			
			dado1.setSelectedIndex(0);
			dado2.setSelectedIndex(0);
			
			dado1.setBounds(320,450,35,25);
			dado2.setBounds(360,450,35,25);
			
			dado1.setVisible(false);
			dado2.setVisible(false);
			
			comboMap.put(1, dado1);
			comboMap.put(2, dado2);
			
			this.add(dado1);
			this.add(dado2);
		}
		
		private void RecarregaJogo()
		{				
			//Aqui pegar dados do resultado final para exibiçao
			JOptionPane.showMessageDialog(this,"GAME OVER!!");
				
			Interador.getInterador().NewGame();						
			vez = Interador.getInterador().getColorVez();
			anterior = Color.white;
				
			buttonMap.get(0).setBackground(vez);				
			buttonMap.get(1).setVisible(false);
				
			ConfigPeoes();
				
			carta = null;
				
			repaint();			
		}
		
		public void Refresh()
		{
			vez = Interador.getInterador().getColorVez();
			anterior = Interador.getInterador().getCorJogadorAnterior();
				
			buttonMap.get(0).setBackground(vez);
				
			ConfigPeoes();
				
			carta = null;
			try 
			{
				if(Interador.getInterador().ComprarCartaImagem()!=null)
					carta = ImageIO.read(getClass().getResource(Interador.getInterador().ComprarCartaImagem()));
				else if(Interador.getInterador().GetImagemTerreno()!=null)
					carta = ImageIO.read(getClass().getResource(Interador.getInterador().GetImagemTerreno()));
			}
			catch(IOException ex) {
			   System.out.println(ex.getMessage());
			   System.exit(1);
			}
			
			//Botões de compra
			buttonMap.get(1).setBackground(anterior);
			buttonMap.get(3).setBackground(anterior);
			if(Interador.getInterador().DonoTerreno(anterior)==null)
			{
				buttonMap.get(1).setVisible(true);
				buttonMap.get(3).setVisible(false);
			}
			else
			{
				if(Interador.getInterador().DonoTerreno(anterior).equals(anterior))
				{
					buttonMap.get(1).setVisible(false);	
					buttonMap.get(3).setVisible(true);
				}
				else
				{
					buttonMap.get(1).setVisible(false);	
					buttonMap.get(3).setVisible(false);
				}
			}
				
			//Botão de Voto
			buttonMap.get(2).setBackground(anterior);					
			if(!Interador.getInterador().VerificaJaVotou(anterior))
			{						
				buttonMap.get(2).setVisible(true);
			}
			else
			{
				buttonMap.get(2).setVisible(false);
			}
		}
}