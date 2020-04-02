package gui;
import java.awt.*;
import javax.swing.*;

public class BancoImobiliario extends JFrame
{
	   public final int LARG_DEFAULT=715;
	   public final int ALT_DEFAULT=750;
	   JPanel pa;
	   Menu m;
	   
	   public static BancoImobiliario p;
	   
	   public BancoImobiliario()
	   {
		   setTitle("Banco Imobiliário");
		   Toolkit tk=Toolkit.getDefaultToolkit();
		   Dimension screenSize=tk.getScreenSize();
		   int sl=screenSize.width;
		   int sa=screenSize.height;
		   int x=sl/2-LARG_DEFAULT/2;
		   int y=sa/2-ALT_DEFAULT/2;	  
		   setBounds(x,y,LARG_DEFAULT,ALT_DEFAULT);
		   setDefaultCloseOperation(EXIT_ON_CLOSE);  
		   pa = new Painel();
		   getContentPane().add(pa); 
		   setVisible(false);
		   m = new Menu(this);
	   }
	   	   
	   public static void main(String args[]) 
	   {		 		   
		   p = new BancoImobiliario();			 
	   }
	   
	   public Menu getMenu()
	   {
		   return this.m;
	   }	   
	   
	}