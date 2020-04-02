package regras;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

class Baralho
{
	List<SorteReves> listaCartas = new ArrayList<SorteReves>();
	Deque<SorteReves> deck = new LinkedList<SorteReves>();
	SorteReves cartaFora;
	SorteReves trunfo;
	
	public Baralho() 
	{
		GeraCartas();
		Embaralhar();
	}
	
	public SorteReves ComprarCarta()
	{
		SorteReves compra = deck.pollFirst();
		if(compra.valor == 1)
			cartaFora = compra;
		else
			deck.addLast(compra);
		return compra;
	}
	
	public void RecuperaTrunfo()
	{
		if(cartaFora != null)
		{
			deck.addLast(cartaFora);
			cartaFora = null;
		}
		else
		{
			deck.addLast(new SorteReves( "../img/sorteReves/chance9.png", 1));
		}
	}
	
	public void RetiraTrunfo()
	{	
		GeraCartasSemTrunfo();
		Embaralhar();
	}
	
	private void Embaralhar() 
	{
		if(!deck.isEmpty())
			deck.clear();
		
		Random rd = new Random();
		int index; 
		
		while(!listaCartas.isEmpty())
		{
			index = rd.nextInt(listaCartas.size());
			
			if(rd.nextBoolean())
				deck.addFirst(listaCartas.get(index));
			else
				deck.addLast(listaCartas.get(index));
			
			listaCartas.remove(index);
		}
	}
	
	private void GeraCartas() 
	{
		listaCartas.add(new SorteReves( "../img/sorteReves/chance1.png", 25));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance2.png", 150));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance3.png", 80));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance4.png", 200));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance5.png", 50));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance6.png", 50));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance7.png", 100));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance8.png", 100));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance9.png", 1));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance10.png", 200));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance11.png", 50, true));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance12.png", 45));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance13.png", 100));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance14.png", 100));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance15.png", 20));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance16.png", -15));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance17.png", -25));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance18.png", -45));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance19.png", -30));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance20.png", -100));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance21.png", -100));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance22.png", -40));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance23.png", 0));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance24.png", -30));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance25.png", -50));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance26.png", -25));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance27.png", -30));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance28.png", -45));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance29.png", -50));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance30.png", -50));
		
	}
	
	private void GeraCartasSemTrunfo() 
	{
		listaCartas.add(new SorteReves( "../img/sorteReves/chance1.png", 25));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance2.png", 150));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance3.png", 80));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance4.png", 200));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance5.png", 50));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance6.png", 50));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance7.png", 100));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance8.png", 100));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance10.png", 200));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance11.png", 50, true));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance12.png", 45));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance13.png", 100));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance14.png", 100));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance15.png", 20));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance16.png", -15));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance17.png", -25));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance18.png", -45));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance19.png", -30));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance20.png", -100));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance21.png", -100));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance22.png", -40));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance23.png", 0));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance24.png", -30));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance25.png", -50));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance26.png", -25));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance27.png", -30));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance28.png", -45));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance29.png", -50));
		listaCartas.add(new SorteReves( "../img/sorteReves/chance30.png", -50));
		
	}
	
}
