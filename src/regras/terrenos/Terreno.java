package regras.terrenos;

public class Terreno
{
	//dimensao do peao q ocupara o quadrante
	int alt = 25; int larg = 25;
	
	//Armazena a celula pertencente
	Celula posicao;
	
	//Armazena os pontos de posicionamento possiveis do peao
	Celula quadrantes[] = new Celula[6];
	boolean liberado[] = new boolean[6];//confere ocupação
			
	public Terreno(Celula cel)
	{
		this.posicao = cel;
		
		//Auxiliares 
		boolean vertical = (cel.getLarg()/3 < cel.getAlt()/3);//true caso seja vertical; isso vê se ele será 3x2 ou 2x3	

		//atribui campo liberado na inicializaçao
		for(int i=0; i<6; i++)
		{
			liberado[i] = true;
		}
		
		//posiciona os quadrantes de acordo com os quadrantes
		if(vertical)
		{
			quadrantes[0] = new Celula(cel.getX(), cel.getY(), alt, larg);
			quadrantes[1] = new Celula(cel.getX()+larg, cel.getY(), alt, larg);
			quadrantes[2] = new Celula(cel.getX(), cel.getY()+alt, alt, larg);
			quadrantes[3] = new Celula(cel.getX()+larg, cel.getY()+alt, alt, larg);
			quadrantes[4] = new Celula(cel.getX(), cel.getY()+alt*2, alt, larg);
			quadrantes[5] = new Celula(cel.getX()+larg, cel.getY()+alt*2, alt, larg);
		}
		else
		{
			quadrantes[0] = new Celula(cel.getX(), cel.getY(), alt, larg);
			quadrantes[1] = new Celula(cel.getX()+larg, cel.getY(), alt, larg);
			quadrantes[2] = new Celula(cel.getX()+larg*2, cel.getY(), alt, larg);
			quadrantes[3] = new Celula(cel.getX(), cel.getY()+alt, alt, larg);
			quadrantes[4] = new Celula(cel.getX()+larg, cel.getY()+alt, alt, larg);
			quadrantes[5] = new Celula(cel.getX()+larg*2, cel.getY()+alt, alt, larg);
		}
	}
	
	public Celula getPosicao() 
	{
		return this.posicao;
	}
	
	public Celula PrimeiroQuadranteLivre()
	{
		for(int i=0; i<6;i++)
		{
			if(liberado[i])
			{
				liberado[i]=false;
				return quadrantes[i];
			}
		}
		
		return null;
	}

	public boolean VerificaExiste(Celula cel)
	{
		for(int i=0; i<6; i++)
		{
			if(quadrantes[i].equals(cel))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean LimpaTerreno(Celula cel)
	{
		for(int i=0; i<6; i++)
		{
			if(quadrantes[i].equals(cel))
			{
				liberado[i]=true;
				return true;
			}
		}
		
		return false;
	}
	
}
