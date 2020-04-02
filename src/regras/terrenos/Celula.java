package regras.terrenos;

public class Celula
{
	double x;
	double y;
	double larg,alt;
	
	public Celula (double x,double y,double l,double a) {
		this.x=x;
		this.y=y;
		larg=l;
		alt=a;
	}

	public double getLarg() {
		return larg;
	}

	public void setLarg(double larg) {
		this.larg = larg;
	}

	public double getAlt() {
		return alt;
	}

	public void setAlt(double alt) {
		this.alt = alt;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}		
	
	public boolean equals(Celula c)
	{
		if(c == null)
			return false;
		
		return (this.x == c.x && this.y == c.y && this.alt == c.alt && this.larg == c.larg);
	}
	
	public boolean EstaContida(double x, double y)
	{
		return (this.x <= x && this.y <= y && this.alt + this.y >= y && this.larg + this.x >= x);
	}
}
