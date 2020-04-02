package regras;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import regras.terrenos.Celula;
import regras.terrenos.Companhia;
import regras.terrenos.Prisao;
import regras.terrenos.Terreno;
import regras.terrenos.Territorio;

class Mapa 
{

	// Posiçoes do tabuleiro
	double larg = 90.0, alt = 90.0, variante = 55.0;
	protected Celula espacos[][] = new Celula[4][10];

	// Ordem de movimentaçao do tabuleiro
	protected Terreno movimento[] = new Terreno[40];
	
	//Zonas de compra de cartas
	protected List<Terreno> zonaCompra = new ArrayList<Terreno>();
	
	//Armadilha vai para cadeia
	protected Terreno trap;
	
	//Impostos e Lucros
	protected Terreno imposto;
	protected Terreno lucro;

	//Prisao
	Prisao cadeia;
	
	//Companhias
	Companhia companhias[] = new Companhia[6];
	
	//Territórios
	Territorio territorios[] = new Territorio[22];
	
	//Grupos de territorios(mesma familia)
	List<Territorio> g1 = new ArrayList<Territorio>();
	List<Territorio> g2 = new ArrayList<Territorio>();
	List<Territorio> g3 = new ArrayList<Territorio>();
	List<Territorio> g4 = new ArrayList<Territorio>();
	List<Territorio> g5 = new ArrayList<Territorio>();
	List<Territorio> g6 = new ArrayList<Territorio>();
	List<Territorio> g7 = new ArrayList<Territorio>();
	List<Territorio> g8 = new ArrayList<Territorio>();
	
	Map<Color,List<Territorio>> grupos = new HashMap<Color,List<Territorio>>();
		
	public Mapa() 
	{
		GeraCelulas();
		GeraCompanhias();
		GeraTerritorios();
		MapeiaFamiliaTerritorios();
		GeraZonasDeCompras();
		GeraAvulsos();
	}

	/////////////////////////////////////ADM Terrenos//////////////////////////////////////////////////////

	public double OrçamentoTerreno(Celula local)
	{
		for(Companhia companhia : companhias)
		{			
			if(companhia.VerificaExiste(local))
				return companhia.getValorCompra();
		}
		
		for(Territorio territorio : territorios)
		{			
			if(territorio.VerificaExiste(local))
				return territorio.getValorCompra();
		}
		
		return 0;
	}
	
	public double OrçamentoPropriedadeTerreritorio(Celula local)
	{
		for(Territorio territorio : territorios)
		{			
			if(territorio.VerificaExiste(local))
				return territorio.getValorCompraCasa();
		}
		
		return 0;
	}
	
	public void CompraTerreno(Celula local, Color dono)
	{
		for(Companhia companhia : companhias)
		{			
			if(companhia.VerificaExiste(local) || companhia.getPosicao().equals(local))
				companhia.ObtemDono(dono);
		}
		
		for(Territorio territorio : territorios)
		{			
			if(territorio.VerificaExiste(local) || territorio.getPosicao().equals(local))
				territorio.ObtemDono(dono);
		}
		
	}
	
	public boolean ValidaCompraPropriedadeTerreno(Celula local)
	{
		for(Territorio territorio : territorios)
		{			
			if(territorio.VerificaExiste(local))
			{
				if(territorio.getNumeroPropriedades()==0)
				{
					return true;
				}
				else if(territorio.getNumeroPropriedades() < 5)
				{
					for(Territorio item : grupos.get(territorio.getFamilia()))
					{
						if(item.getNumeroPropriedades() != territorio.getNumeroPropriedades())
						{
							return false;
						}
					}
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void CompraPropriedadesTerritorio(Celula local)
	{
		for(Territorio territorio : territorios)
		{			
			if(territorio.VerificaExiste(local)|| territorio.getPosicao().equals(local))
				if(ValidaCompraPropriedadeTerreno(local))
					territorio.CompraDePropriedade();
		}
		
	}
	
	public double VendaTerreno(Celula local)
	{
		double valor = 0;
		
		for(Companhia companhia : companhias)
		{			
			if(companhia.VerificaExiste(local))
			{
				valor = companhia.getValorVenda();
				companhia.PerdeDono();
				return valor;
			}
		}
		
		for(Territorio territorio : territorios)
		{			
			if(territorio.VerificaExiste(local))
			{
				valor = territorio.getValorVenda();
				territorio.PerdeDono();
				return valor;
			}
		}
		
		return valor;
	}
	
	public double CobrancaTerreno(Celula local)
	{
		for(Companhia companhia : companhias)
		{			
			if(companhia.VerificaExiste(local))
				return companhia.getTaxa();
		}
		
		for(Territorio territorio : territorios)
		{			
			if(territorio.VerificaExiste(local))
				return territorio.CobraAluguel();
		}
		
		return 0;
	}
	
	public boolean CobrancaEmpresa(Celula local)
	{
		for(Companhia companhia : companhias)
		{			
			if(companhia.VerificaExiste(local))
				return true;
		}
		
		return false;
	}
	
	public Color DonoTerreno(Celula local)
	{
		for(Companhia companhia : companhias)
		{			
			if(companhia.VerificaExiste(local))
				return companhia.getDono();
		}
		
		for(Territorio territorio : territorios)
		{			
			if(territorio.VerificaExiste(local))
				return territorio.getDono();
		}
		
		//publico
		return Color.white;
	}	
	
	public String VerificaImagemTerreno(Celula local)
	{
		for(Companhia companhia : companhias)
		{			
			if(companhia.VerificaExiste(local))
				return companhia.getImagem();
		}
		
		for(Territorio territorio : territorios)
		{			
			if(territorio.VerificaExiste(local))
				return territorio.getImagem();
		}
		
		return null;
	}
	
	public boolean VerificaTerrenoDeCompra(Celula local)
	{
		for(Companhia companhia : companhias)
		{			
			if(companhia.getPosicao().equals(local))
				return true;
		}
		
		for(Territorio territorio : territorios)
		{			
			if(territorio.getPosicao().equals(local))
				return true;
		}
		return false;
	}
	
	public Terreno getTerreno(Celula cel)
	{
		for(int i=0; i< movimento.length; i++)
		{
			if(movimento[i].VerificaExiste(cel))
				return movimento[i];
		}
		return null;
	}
	
	public String getNomeTerreno(Celula cel)
	{
		for(Companhia companhia : companhias)
		{			
			if(companhia.VerificaExiste(cel))
				return companhia.getImagem();
		}
		
		for(Territorio territorio : territorios)
		{			
			if(territorio.VerificaExiste(cel))
				return territorio.getImagem();
		}
		
		return null;
	}
	
	public Celula getCelulaTerreno(String nome)
	{
		for(Companhia companhia : companhias)
		{			
			if(companhia.getImagem().equals(nome))
				return companhia.getPosicao();
		}
		
		for(Territorio territorio : territorios)
		{			
			if(territorio.getImagem().equals(nome))
				return territorio.getPosicao();
		}
		
		return null;
	}
	
	//Retorna -1 se é companhia
	public int VerificaNumeroDeProPriedades(Celula local)
	{
		
		for(Territorio territorio : territorios)
		{			
			if(territorio.VerificaExiste(local) || territorio.getPosicao().equals(local))
				return territorio.getNumeroPropriedades();
		}
		
		return -1;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//////////////////////////////////Cadeia//////////////////////////////////////////////////////////////
	
  	public void RegistraPrisioneiro(Color peao)
	{
		cadeia.RecebePrisioneiro(peao);			
	}
	
	//Retorna -1 se n tiver ninguem na cadeia e 0 se cumpriu na cadeia
	public int TempoPrisioneiro(Color peao)
	{
		return cadeia.CumprimentoDaPrisao(peao);
	}
	
	public void LiberaPrisioneiro(Color peao)
	{
		cadeia.LiberaPrisioneiro(peao);
	}
	
	public Celula getPosicaoCadeia()
	{
		return cadeia.getPosicao();
	}

	public boolean VerificaPeaoPrisao(Color peao)
	{
		return cadeia.ExistenciaPrisioneiro(peao);
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	
	///////////////////////////////////Inicialização//////////////////////////////////////////////////////
	
	private void GeraCelulas() {
		// Inicialização das células
		double troca;
		double x, y, ajuste;
		boolean orientacao; // true vertical & false horizontal

		for (int i = 0; i < 4; i++)
		{
			switch (i)
			{
			case 0:
				x = 600.0;
				y = 600.0;
				ajuste = -1;
				orientacao = false;
				break;
			case 1:
				x = 10.0;
				y = 600.0;
				ajuste = -1;
				orientacao = true;
				break;
			case 2:
				x = 10.0;
				y = 10.0;
				ajuste = 1;
				orientacao = false;
				break;
			default:
				x = 600.0;
				y = 10.0;
				ajuste = 1;
				orientacao = true;
				break;
			}

			espacos[i][0] = new Celula(x, y, larg, alt);

			for (int j = 1; j < 10; j++)
			{					
				if(ajuste==1 && j==1)
				{
					if(orientacao == false)
					{
						troca = (ajuste * larg);
					}
					else 
					{
						troca = (ajuste * alt);
					}
				}
				else
					troca = (ajuste * variante);
					
				if (orientacao == false)
				{
					espacos[i][j] = new Celula(x += troca, y, variante, alt);
				} 
				else
				{
					espacos[i][j] = new Celula(x, y += troca, larg, variante);
				}
			}

		}

		int cont = 0;
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 10; j++)
			{
				movimento[cont] = new Terreno(espacos[i][j]);
				cont++;
			}
		}
	}

	private void GeraAvulsos()
	{
		cadeia = new Prisao(espacos[1][0]);
		trap = new Terreno(espacos[3][0]);
		imposto = new Terreno(espacos[2][4]);
		lucro = new Terreno(espacos[1][8]);
	}
		
	private void GeraCompanhias()
	{
		companhias[0] = new Companhia(espacos[0][5], "../img/companhias/company1.png", 200.0, 50.0);
		companhias[1] = new Companhia(espacos[0][7], "../img/companhias/company2.png", 200.0, 50.0);
		companhias[2] = new Companhia(espacos[1][5], "../img/companhias/company3.png", 150.0, 40.0);
		companhias[3] = new Companhia(espacos[2][5], "../img/companhias/company4.png", 150.0, 40.0);
		companhias[4] = new Companhia(espacos[3][2], "../img/companhias/company5.png", 200.0, 50.0);
		companhias[5] = new Companhia(espacos[3][5], "../img/companhias/company6.png", 200.0, 50.0);
	}
	
	public Companhia getCompanhia(String nome)
	{ 
		Companhia retorno = null;
		if(nome.endsWith("1.png"))
			retorno = companhias[0];
		else if(nome.endsWith("2.png"))
			retorno = companhias[1];
		else if(nome.endsWith("3.png"))
			retorno = companhias[2];
		else if(nome.endsWith("4.png"))
			retorno = companhias[3];
		else if(nome.endsWith("5.png"))
			retorno = companhias[4];
		else if(nome.endsWith("6.png"))
			retorno = companhias[5];
		
		return retorno;
		
	}
		
	private void GeraTerritorios()
	{
		territorios[0] = new Territorio(espacos[0][9], "../img/territorios/Av. 9 de Julho.png",Color.blue,
				220.0, 150.0, 150.0, new double[]{18.0,90.0,250.0,700.0,875.0, 1050.0});
		
		territorios[1] = new Territorio(espacos[3][4], "../img/territorios/Av. Atlântica.png",Color.green,
				300.0, 200.0, 200.0, new double[]{26.0,130.0,390.0,900.0,1100.0, 1250.0});
		
		territorios[2] = new Territorio(espacos[2][6], "../img/territorios/Av. Brasil.png",Color.yellow,
				160.0, 100.0, 100.0, new double[]{12.0,60.0,180.0,500.0,700.0, 900.0});
		
		territorios[3] = new Territorio(espacos[0][6], "../img/territorios/Av. Brigadero Faria Lima.png",Color.blue,
				240.0, 150.0, 150.0, new double[]{20.0,100.0,300.0,750.0,925.0, 1100.0});
		
		territorios[4] = new Territorio(espacos[1][1], "../img/territorios/Av. Europa.png",new Color(102, 0, 153),
				200.0, 100.0, 100.0, new double[]{16.0,80.0,220.0,600.0,800.0, 1000.0});
		
		territorios[5] = new Territorio(espacos[0][4], "../img/territorios/Av. Nossa S. de Copacabana.png",Color.pink,
				60.0, 50.0, 50.0, new double[]{4.0,20.0,60.0,180.0,320.0, 450.0});
		
		territorios[6] = new Territorio(espacos[1][4], "../img/territorios/Av. Pacaembú.png",new Color(102, 0, 153),
				180.0, 100.0, 100.0, new double[]{14.0,70.0,200.0,550.0,750.0, 950.0});
		
		territorios[7] = new Territorio(espacos[2][8], "../img/territorios/Av. Paulista.png",Color.yellow,
				140.0, 100.0, 100.0, new double[]{10.0,50.0,150.0,450.0,625.0, 750.0});
		
		territorios[8] = new Territorio(espacos[0][3], "../img/territorios/Av. Presidente Vargas.png",Color.pink,
				60.0, 50.0, 50.0, new double[]{2.0,10.0,30.0,90.0,160.0, 250.0});
		
		territorios[9] = new Territorio(espacos[0][8], "../img/territorios/Av. Rebouças.png",Color.blue,
				220.0, 150.0, 150.0, new double[]{18.0,90.0,250.0,700.0,875.0, 1050.0});
		
		territorios[10] = new Territorio(espacos[3][3], "../img/territorios/Av. Vieira Souto.png",Color.green,
				320.0, 200.0, 200.0, new double[]{28.0,150.0,450.0,1000.0,1200.0, 1400.0});
		
		territorios[11] = new Territorio(espacos[2][3], "../img/territorios/Botafogo.png",Color.red,
				100.0, 50.0, 50.0, new double[]{6.0,30.0,90.0,270.0,400.0, 500.0});
		
		territorios[12] = new Territorio(espacos[3][9], "../img/territorios/Brooklin.png",Color.magenta,
				260.0, 150.0, 150.0, new double[]{22.0,110.0,330.0,800.0,975.0, 1150.0});
		
		territorios[13] = new Territorio(espacos[3][1], "../img/territorios/Copacabana.png",Color.green,
				260.0, 150.0, 150.0, new double[]{22.0,110.0,330.0,800.0,975.0, 1150.0});
		
		territorios[14] = new Territorio(espacos[2][1], "../img/territorios/Flamengo.png",Color.red,
				120.0, 50.0, 50.0, new double[]{8.0,40.0,100.0,300.0,450.0, 600.0});
		
		territorios[15] = new Territorio(espacos[1][7], "../img/territorios/Interlagos.png",Color.orange,
				350.0, 200.0, 200.0, new double[]{35.0,175.0,500.0,1100.0,1300.0, 1500.0});
		
		territorios[16] = new Territorio(espacos[3][6], "../img/territorios/Ipanema.png",Color.green,
				300.0, 200.0, 200.0, new double[]{26.0,130.0,360.0,900.0,1100.0, 1275.0});
		
		territorios[17] = new Territorio(espacos[2][9], "../img/territorios/Jardim Europa.png",Color.yellow,
				140.0, 100.0, 100.0, new double[]{10.0,50.0,150.0,450.0,625.0, 750.0});
		
		territorios[18] = new Territorio(espacos[3][8], "../img/territorios/Jardim Paulista.png",Color.magenta,
				280.0, 150.0, 150.0, new double[]{24.0,120.0,360.0,850.0,1025.0, 1200.0});
		
		territorios[19] = new Territorio(espacos[0][1], "../img/territorios/Leblon.png",Color.pink,
				100.0, 50.0, 50.0, new double[]{6.0,30.0,90.0,270.0,400.0, 500.0});
		
		territorios[20] = new Territorio(espacos[1][9], "../img/territorios/Morumbi.png",Color.orange,
				400.0, 200.0, 200.0, new double[]{50.0,200.0,600.0,1400.0,1700.0, 2000.0});
		
		territorios[21] = new Territorio(espacos[1][3], "../img/territorios/Rua Augusta.png",new Color(102, 0, 153),
				180.0, 100.0, 100.0, new double[]{14.0,70.0,200.0,550.0,750.0, 950.0});
		
	}
	
	public Territorio getTerritorio(String nome)
	{
		Territorio retorno=null;
		
		if(nome.equals("../img/territorios/Av. 9 de Julho.png")) 
				retorno= territorios[0];
		else if (nome.equals("../img/territorios/Av. Atlântica.png"))
			retorno= territorios[1];
		else if(nome.equals("../img/territorios/Av. Brasil.png"))
			retorno= territorios[2];
		else if(nome.equals("../img/territorios/Av. Brigadero Faria Lima.png"))
			retorno= territorios[3];
		else if(nome.equals("../img/territorios/Av. Europa.png"))
			retorno= territorios[4];		
		else if(nome.equals("../img/territorios/Av. Nossa S. de Copacabana.png"))
			retorno= territorios[5];
		else if(nome.equals("../img/territorios/Av. Pacaembú.png"))
			retorno= territorios[6];
		else if(nome.equals("../img/territorios/Av. Paulista.png"))
			retorno= territorios[7];
		else if(nome.equals("../img/territorios/Av. Presidente Vargas.png"))
			retorno= territorios[8];
		else if(nome.equals("../img/territorios/Av. Rebouças.png"))
			retorno= territorios[9];
		else if(nome.equals("../img/territorios/Av. Vieira Souto.png"))
			retorno= territorios[10];
		else if(nome.equals("../img/territorios/Botafogo.png"))
			retorno= territorios[11];
		else if(nome.equals("../img/territorios/Brooklin.png"))
			retorno= territorios[12];
		else if(nome.equals("../img/territorios/Copacabana.png"))
			retorno= territorios[13];
		else if(nome.equals("../img/territorios/Flamengo.png"))
			retorno= territorios[14];
		else if(nome.equals("../img/territorios/Interlagos.png"))
			retorno= territorios[15];
		else if(nome.equals("../img/territorios/Ipanema.png"))
			retorno= territorios[16];
		else if(nome.equals("../img/territorios/Jardim Europa.png"))
			retorno= territorios[17];
		else if(nome.equals("../img/territorios/Jardim Paulista.png"))
			retorno= territorios[18];
		else if(nome.equals("../img/territorios/Leblon.png"))
			retorno= territorios[19];
		else if(nome.equals("../img/territorios/Morumbi.png"))
			retorno= territorios[20];
		else if(nome.equals("../img/territorios/Rua Augusta.png"))
			retorno= territorios[21];
		return retorno;
		
		
	}

	private void MapeiaFamiliaTerritorios()
	{
		grupos.put(Color.blue, g1);
		grupos.put(Color.green, g2);
		grupos.put(Color.yellow, g3);
		grupos.put(new Color(102, 0, 153), g4);
		grupos.put(Color.pink, g5);
		grupos.put(Color.red, g6);
		grupos.put(Color.magenta, g7);
		grupos.put(Color.orange, g8);
		
		for(Territorio territorio : territorios)
		{
			grupos.get(territorio.getFamilia()).add(territorio);
		}
		
	}
	
	private void GeraZonasDeCompras() 
	{
		zonaCompra.add(movimento[2]);
		zonaCompra.add(movimento[12]);
		zonaCompra.add(movimento[16]);
		zonaCompra.add(movimento[22]);
		zonaCompra.add(movimento[27]);
		zonaCompra.add(movimento[37]);
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	////////////////////////////Outros Campos/////////////////////////////////////////////////////////////
	
	public double LucrosOuImpostos(Celula local)
	{
		if(lucro.VerificaExiste(local))
			return 200.0;
		else if(imposto.VerificaExiste(local))
			return -200.0;
		
		return 0;
	}
	
	public boolean VerificaArmadilhaCadeia(Celula local)
	{
		if(trap.VerificaExiste(local))
			return true;
		
		return false;
	}
	
	public boolean VerificaZonaDeCompra(Celula local)
	{
		for(Terreno territorio : zonaCompra)
		{
			
			if(territorio.VerificaExiste(local))
				return true;
		}
		
		return false;
	}	
		
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	
}
