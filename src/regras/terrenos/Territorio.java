package regras.terrenos;

import java.awt.Color;

public class Territorio extends Terreno
{
	Color dono;
	Color familia;
	String img;
	double valorCompra;
	double valorCompraCasa;
	double valorCompraHotel;
	double aluguel[];// a principio sao 6 valores possíveis
	int casas;//só podem 4
	int hoteis;// só pode 1
	
	public Territorio(Celula cel, String img, Color familia, double valorCompra,double valorCompraCasa,
	double valorCompraHotel, double[] aluguel)
	{
		super(cel);
		this.familia = familia;
		this.dono = null;
		this.img = img;
		this.valorCompra = valorCompra;
		this.valorCompraCasa = valorCompraCasa;
		this.valorCompraHotel = valorCompraHotel;
		this.aluguel = aluguel;
		
		this.casas = 0;
		this.hoteis = 0;
	}
	
	public double CobraAluguel()
	{
		if(casas+hoteis>aluguel.length)
			return 0; //erro no programa
		
		return aluguel[casas + hoteis];
	}
	
	public double getValorCompra()
	{
		return valorCompra;
	}
	
	public double getValorCompraCasa()
	{
		return valorCompraCasa;
	}
	
	public double getValorVenda()
	{
		double valor = valorCompra*0.9 + valorCompraCasa*casas*0.9 + valorCompraHotel*hoteis*0.9;
		return valor;
	}
	
	public int getNumeroPropriedades()
	{
		return casas + hoteis;
	}
	
	public void setNumeroPropriedades(int num)
	{
		if(num<=4)
		{
			casas = num;
			hoteis = 0;
		}
		else
		{
			casas= 4;
			hoteis =1;
		}
	}
	
	public Color getFamilia()
	{
		return this.familia;
	}
	
	public String getImagem()
	{
		return img;
	}
	
	public Color getDono()
	{
		return dono;
	}
	
	public void setDono(Color proprietario)
	{
		dono =proprietario;
		
	}
	
	public boolean ExisteDono()
	{
		if(dono == null)
			return false;
		
		return true;
	}
	
	public void ObtemDono(Color comprador)
	{
		dono = comprador;
	}
	
	public void PerdeDono()
	{
		casas=0;
		hoteis=0;
		dono = null;
	}

	public void CompraDePropriedade()
	{
		if(casas<4)
			casas++;
		else if(hoteis<1)
			hoteis++;
	}
	
	public int getNumCasas()
	{
		return casas;
	}
	
	public int getNumHoteis()
	{
		return hoteis;
	}

}
