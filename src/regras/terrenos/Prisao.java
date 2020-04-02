package regras.terrenos;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Prisao extends Terreno
{
	List<Color> prisioneiros = new ArrayList<Color>();
	List<Integer> turnos = new ArrayList<Integer>();
	
	public Prisao(Celula cel)
	{
		super(cel);
	}
	
	public boolean ExistenciaPrisioneiro(Color individuo)
	{
		for(Color preso : prisioneiros)
		{
			if(preso.equals(individuo))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public void RecebePrisioneiro(Color novo)
	{
		prisioneiros.add(novo);
		turnos.add(0);
	}
	
	//Retorna -1 se n tiver ninguem na cadeia e 0 se cumpriu na cadeia
	public int CumprimentoDaPrisao(Color individuo)
	{
		for(int i=0; i <  prisioneiros.size(); i++)
		{
			if(prisioneiros.get(i).equals(individuo))
			{
				Integer novo = turnos.get(i) + 1;
				turnos.remove(i);
				
				if(novo == 4)
				{
					prisioneiros.remove(i);
					return 0;
				}
				else
				{
					turnos.add(i, novo);
					return novo;
				}
			}
		}
		
		return -1;
	}
	
	public void LiberaPrisioneiro(Color individuo)
	{
		for(int i=0; i <  prisioneiros.size(); i++)
		{
			if(prisioneiros.get(i).equals(individuo))
			{
				turnos.remove(i);
				prisioneiros.remove(i);				
			}
		}
	}
}
