package regras.terrenos;

import java.awt.Color;

public class Companhia extends Terreno
{
	
	Color dono;
	String img;
	double valorCompra;
	double taxa;
	
	public Companhia(Celula cel, String img, double valorCompra, double taxa)
	{
		super(cel);
		this.dono = null;
		this.img = img;
		this.valorCompra = valorCompra;
		this.taxa = taxa;
	}
	
	public double getValorCompra()
	{
		return valorCompra;
	}
	
	public double getValorVenda()
	{
		return valorCompra*0.9;
	}
	
	public double getTaxa()
	{
		return taxa;
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
		dono = null;
	}
}
