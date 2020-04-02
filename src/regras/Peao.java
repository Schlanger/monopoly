package regras;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import regras.terrenos.Celula;

class Peao 
{
	Color cor;
	String img;
	double carteira;
	boolean falencia;
	Celula posicao;
	List<Celula> propriedades = new ArrayList<Celula>();
	
	public Peao(Color cor, String img, double carteira, boolean falencia, Celula posicao)
	{
		this.cor = cor;
		this.img = img;
		this.carteira = carteira;
		this.falencia = falencia;
		this.posicao = posicao;
	}

	public Color getCor() {
		return cor;
	}

	public void setCor(Color cor) {
		this.cor = cor;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public double getCarteira() {
		return carteira;
	}

	public void setCarteira(double carteira) {
		this.carteira = carteira;
	}

	public boolean isFalencia() {
		return falencia;
	}

	public void setFalencia(boolean falencia) {
		this.falencia = falencia;
	}

	public Celula getPosicao() {
		return posicao;
	}

	public void setPosicao(Celula posicao) {
		this.posicao = posicao;
	}
	
	public void AdicionarPropriedade(Celula propriedade)
	{
		this.propriedades.add(propriedade);
	}
	
	public Celula VenderPropriedade()
	{
		if(propriedades.isEmpty())
			return null;
		
		Celula retorno = propriedades.get(0);
		propriedades.remove(0);
				
		return retorno;
	}
	
}
