package regras;

class SorteReves
{
	String img;//armazena o local da imagem do baralho
	double valor;// Caso o retorno =1 ganha tiket antiprisao; caso = 0 vai pra cadeia; demais valores afeta carteira
	boolean aposta;
	
	public SorteReves(String img, double valor)
	{
		this.img = img;
		this.valor = valor;
		this.aposta = false;
	}
	
	public SorteReves(String img, double valor, boolean aposta)
	{
		this.img = img;
		this.valor = valor;
		this.aposta = aposta;
	}
	
	public double valorCarta()
	{
		return valor;
	}
	
	public String getImagem()
	{
		return img;
	}
}
