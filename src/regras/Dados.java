package regras;

import java.util.Random;


class Dados
{
	int dado1;
	int dado2;
	int sequencia;
	String resultadosImg[] = new String[6];
	
	public Dados()
	{
		//valores padrões
		dado1 = 1;
		dado2 = 1;
		sequencia = 0;
		
		//define as imagens guardadas
		for(int i=0; i<6; i++)
		{
			String local = "../img/dados/die_face_" + Integer.toString(i+1) +".png";
			resultadosImg[i]=local;
		}
	}
	
	public int getResultado()
	{
		return dado1 + dado2;
	}
	
	public int getValorDado1()
	{
		return dado1;
	}
	
	public int getValorDado2()
	{
		return dado2;
	}
	
	public String getImgDado1()
	{
		return resultadosImg[dado1-1];
	}
	
	public String getImgDado2()
	{
		return resultadosImg[dado2-1];
	}
	
	public String getImgResultado(int index)
	{
		return resultadosImg[index];
	}
	
	public void RolarDados()
	{
		Random rd = new Random();
		dado1 = rd.nextInt(6)+1;
		dado2 = rd.nextInt(6)+1;
		
		if(dado1 == dado2)
			sequencia++;
		else
			sequencia = 0;
	}
	
	public void SimulaDados(int val1, int val2)
	{
		dado1 = val1;
		dado2 = val2;
		
		if(dado1 == dado2)
			sequencia++;
		else
			sequencia = 0;
	}
	
	public boolean DentroDeSequencia()
	{
		return dado1 == dado2;
	}
	
	public boolean LimiteDeSequencia()
	{
		if(sequencia > 3)
		{
			sequencia = 0;
			return true;
		}
		
		return false;
	}
	
	public void ZeraSequencia()
	{
		sequencia = 0;
	}
}
