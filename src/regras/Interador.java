package regras;

import java.awt.Color;
import java.io.File;
import java.util.List;

import regras.terrenos.Celula;

public class Interador
{
	private Controle ctrl;
	private static Interador interador = null;
	
	private Interador() 
	{
		ctrl = new Controle();
	}
	
	public static Interador getInterador()
	{
		if(interador == null)
			interador = new Interador();
		
		return interador;
	}	

	public void NewGame()
	{
		ctrl.NewGame();
	}
	
	public boolean GameOver()
	{
		return ctrl.GameOver();
	}
	
	public void Carregar(String local)
 	{
 		ctrl.Carregar(local);
 	}
 	
 	public void Salvar(String local)
 	{
 		ctrl.Salvar(local);
 	}
 		
	public Color getCorJogador(int pos)
	{
		return ctrl.getCorJogador(pos);
	}
	
	public void RetiraJogadores(int numeroParticipante)
	{
		ctrl.RetiraJogadores(numeroParticipante);
	}
	
	public String getImgJogador(int pos)
	{
		return ctrl.getImgJogador(pos);
	}
	
	public String getImgJogador(Celula pos)
	{
		return ctrl.getImgJogador(pos);
	}
	
	public String ComprarCartaImagem()
	{		
		return ctrl.ComprarCartaImagem();
	}

	public String GetImagemTerreno()
	{
		return ctrl.GetImagemTerreno();
	}
	
	public List<Celula> getPosicaoJogadores()
	{
		return ctrl.getPosicaoJogadores();
	}

	public Celula[][] getEspacos()
	{
		return ctrl.getEspacos();
	}

	public String getImgDado(int index)
	{
		return ctrl.dados.getImgResultado(index);
	}
	
	public int getValorDado1() 
	{
		return ctrl.dados.getValorDado1();
	}
	
	public int getValorDado2() 
	{
		return ctrl.dados.getValorDado2();
	}
	
	public Color RealizaTurno()
	{		
		return ctrl.RealizaTurno();
	}
	
	public Color RealizaTurnoSimulado(int dado1, int dado2)
	{		
		return ctrl.RealizaTurnoSimulado(dado1, dado2);
	}
	
	public Color getColorVez()
	{
		return ctrl.getColorVez();
	}
	
	public Color getCorJogadorAnterior()
	{
		return ctrl.getCorJogadorAnterior();
	}
	
	public Color getCorJogadorProximo()
	{
		return ctrl.getCorJogadorProximo();
	}
	
	public boolean JogadorLivre()
	{
		return ctrl.JogadorLivre();
	}
	
	public void VotoEncerramento(Color peao)
	{
		ctrl.VotoEncerramento(peao);
	}
	
	public boolean VerificaJaVotou(Color peao)
	{
		return ctrl.VerificaJaVotou(peao);
	}
	
	public boolean CompraDeTerreno(Color peao)
	{
		return ctrl.CompraDeTerreno(peao);
	}
	
	public boolean CompraDePropriedade(Color peao)
	{
		return ctrl.CompraDePropriedade(peao);
	}
	
	public Color DonoTerreno(Color peao)
	{
		return ctrl.DonoTerreno(peao);
	}

	public boolean VerificaTerrenoEmpresaOuTerritorio(Celula local) 
	{
		return ctrl.VerificaTerrenoEmpresaOuTerritorio(local);
	}
}
