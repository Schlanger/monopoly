package regras;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import regras.terrenos.Celula;
import regras.terrenos.Companhia;
import regras.terrenos.Territorio;

public class Registros implements Observer
{
	//PEOES
	static double banco;
	static List<String> infoPeao = new ArrayList<String>();
	static List<Color> corPeao = new ArrayList<Color>();
	Map<Color,String> jogadoresNome = new HashMap<Color,String>();
	static String acao;
	static String volta;
	
	//EMPRESAS E TERRITORIOS
	static int contador;
	static Map<Integer,Celula> localizaTerreno = new HashMap<Integer,Celula>();
	static Map<Celula,String> localizaTerrenoInfo = new HashMap<Celula,String>();
	static List<String> terrenoInfo = new ArrayList<String>();
	
	private static Registros registro = null;
	
	private Registros() 
	{
		
	}

	public static List<String> TerrenosList()
	{
		return terrenoInfo;
	}
	
	public static Registros GetRegistros()
	{
		if(registro == null)
			registro = new Registros();
		return registro;
	}
	
	public static List<String> InfoPeoes()
	{
		return infoPeao;
	}
	
	public static String Acao()
	{
		return acao;
	}
	
	public static String Volta()
	{
		return volta;
	}
	
	public static List<Color> CoresInfoPeoes() 
	{
		return corPeao;
	}
	
	public static String GetBanco() 
	{
		return String.format("%7.2f", banco)+"$";
	}
	
	public static String InfoTerreno(Celula cel)
	{
		for(int i=0; i<contador; i++)
		{
			if(localizaTerreno.get(i).equals(cel))
			{
				return localizaTerrenoInfo.get(localizaTerreno.get(i));
			}
		}
		
		return null;
	}
	
	@Override
	public void update(Observable o, Object arg)
	{		
		Controle cont = (Controle)o;
		String informacao;
		
		/////////////GERA REGISTROS DE JOGADORES NO PAINEL///////////////////////
		infoPeao.clear();
		corPeao.clear();		
		
		if(jogadoresNome.isEmpty())
		{
			for(int i=0; i < cont.jogadores.length; i++)
			{
				jogadoresNome.put(cont.jogadores[i].getCor(),"Jogador"+(i+1));
			}
		}
		
		for(int i=0; i < cont.jogadores.length; i++)
		{
			informacao = "";
			if(!cont.jogadores[i].isFalencia())
			{			
				corPeao.add(cont.jogadores[i].getCor());
				if(cont.jogadorProtegido != null && cont.jogadores[i].getCor().equals(cont.jogadorProtegido.getCor()))
				{
					informacao += "T ";
				}
				else
				{
					informacao += "  ";
				}
				if((cont.vez-1 ==i && cont.dados.sequencia==0)|| (i==5 &&cont.vez ==0) || (cont.vez==i && cont.dados.sequencia>0))
				{
					informacao += "vez ";
				}
				else
				{
					informacao += "    ";
				}
				informacao += jogadoresNome.get(cont.jogadores[i].getCor()) + ": " + String.format("%7.2f", cont.jogadores[i].getCarteira())+"$" ;
				
				if(cont.votosGameOver[i])
				{
					informacao += " Terminar";
				}
				infoPeao.add(informacao);
			}
			
			banco = cont.banco;
						
		}
		////////////////////////////////////////////////////////////////////
		
		////////////////////GERA INFO TERRITORIOS E EMPRESAS/////////////////
		localizaTerreno.clear();
		localizaTerrenoInfo.clear();
		contador=0;
		
		for(Territorio territorio : cont.map.territorios)
		{
			informacao = "Dono: ";
			if(territorio.getDono()!=null)
			{
				informacao += jogadoresNome.get(territorio.getDono());
				informacao += "\nValor do aluguel: " + String.format("%6.2f", territorio.CobraAluguel()) +"$";
			}
			else
			{
				informacao += "Nenhum";
				informacao += "\nValor de compra: " + String.format("%6.2f", territorio.getValorCompra()) +"$";
			}
									
			informacao += "\nNumero de casas: " + territorio.getNumCasas();
			informacao += "\nNumero de hoteis: " + territorio.getNumHoteis();
			informacao += "\nNumero total de propriedades: " + territorio.getNumeroPropriedades();
			
			terrenoInfo.add(informacao);
			localizaTerreno.put(contador, territorio.getPosicao());
			localizaTerrenoInfo.put(territorio.getPosicao(), terrenoInfo.get(terrenoInfo.size()-1));
			contador++;
		}
		
		for(Companhia companhia : cont.map.companhias)
		{
			informacao = "Dono: ";
			if(companhia.getDono()!=null)
			{
				informacao += jogadoresNome.get(companhia.getDono());
				informacao += "\nValor da taxa: " + String.format("%6.2f", companhia.getTaxa()) +"$ por valor de dado";
			}
			else
			{
				informacao += "Nenhum";
				informacao += "\nValor de compra: " + String.format("%6.2f", companhia.getValorCompra()) +"$";
			}
			
			terrenoInfo.add(informacao);
			localizaTerreno.put(contador, companhia.getPosicao());
			localizaTerrenoInfo.put(companhia.getPosicao(), terrenoInfo.get(terrenoInfo.size()-1));
			contador++;
		}
		
		/////////////////////////////////////////////////////////////////////
		
		///////////////////////GERA AÇAO REALIZADA///////////////////////////
		if(cont.volta)
		{
			volta = jogadoresNome.get(cont.getCorJogadorAnterior()) + " realizou 1 volta e recebeu $200";
		}
		else
		{
			volta = "";
		}
		
		switch (cont.tipo)
		{
			case 0:
				acao = "";
				break;
			case 1:				
				acao = jogadoresNome.get(cont.getCorJogadorAnterior()) + " pagou $" + String.format("%6.2f", cont.cobranca)
				+ " para o " + jogadoresNome.get(cont.dono) ;
				break;
			case 2:
				acao = jogadoresNome.get(cont.getCorJogadorAnterior());
				if( cont.acumulo < 0)
					acao += " pagou $" + String.format("%6.2f", -cont.acumulo) + " para o banco";
				else
					acao += " ganhou $" + String.format("%6.2f", cont.acumulo) + " para o banco";
				break;
			case 3:				
				acao = jogadoresNome.get(cont.getCorJogadorAnterior()) + " ganhou $" + String.format("%6.2f", cont.acumulo)
				+ " total de cada jogador";
				break;
			default:
				acao = jogadoresNome.get(cont.getCorJogadorAnterior());
				if( cont.acumulo < 0)
					acao += " pagou imposto $" + String.format("%6.2f", 200.00) + " para o banco";
				else
					acao += " gerou receita de $" + String.format("%6.2f", 200.00);
				break;
		}
		
		////////////////////////////////////////////////////////////////////
	}
	
	
}
