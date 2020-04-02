package gui;
import java.awt.*;
import javax.swing.*;
import javax.swing.JButton;

import regras.Interador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JFrame
{
	private JFrame menu = new JFrame("Menu");
	private JButton carregar = new JButton("Continuar");
	private JButton ok = new JButton("OK");
	private JRadioButton dois = new JRadioButton("Dois", false);
	private JRadioButton tres = new JRadioButton("Três", false);
	private JRadioButton quatro = new JRadioButton("Quatro", false);
	private JRadioButton cinco = new JRadioButton("Cinco", false);
	private JRadioButton seis = new JRadioButton("Seis", false);	
	private ButtonGroup grupo = new ButtonGroup();
	private JLabel rotulo1=new JLabel("Quantas pessoas participarão dessa partida");
	private JLabel rotulo2=new JLabel("Ou prefere continuar um jogo salvo");
	
	
	public Menu(BancoImobiliario p)
	{
		int altura = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		int largura = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		menu.setBounds((largura/2)-325, (altura/2)-75, 650, 150);	
		menu.setTitle("Menu");
		menu.setSize(650,150);
		menu.setLayout( new FlowLayout() );
		menu.setDefaultCloseOperation(EXIT_ON_CLOSE);
		menu.add(rotulo1);
		grupo.add(dois);
		grupo.add(tres);
		grupo.add(quatro);
		grupo.add(cinco);
		grupo.add(seis);
		menu.add(dois);
		menu.add(tres);
		menu.add(quatro);
		menu.add(cinco);
		menu.add(seis);
		menu.add(ok);
		menu.add(rotulo2);
		menu.add(carregar);
		menu.setVisible(true);	
		ok.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e) { 
				if(dois.isSelected())
				{
					ParticipantesEfetivos(2);					
				}
				else if(tres.isSelected())
				{
					ParticipantesEfetivos(3);		
				}
				else if(quatro.isSelected())
				{
					ParticipantesEfetivos(4);
				}
				else if(cinco.isSelected()) 
				{
					ParticipantesEfetivos(5);		
				}
				else if(seis.isSelected())
				{	
					ParticipantesEfetivos(6);					
				}		
				
				Painel painel = (Painel)p.pa;
				
				painel.ConfigPeoes();
				painel.repaint();	
				
				p.setVisible(true);
				menu.dispose();				
			} });
		
			carregar.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
            JFileChooser chooserArquivo = new JFileChooser();
            int escolha = chooserArquivo.showOpenDialog(getParent());
            if (escolha == JFileChooser.APPROVE_OPTION)
            {
            	Interador.getInterador().NewGame();
				
                Interador.getInterador().Carregar(chooserArquivo.getSelectedFile().getAbsolutePath());
                
                Painel painel = (Painel)p.pa;
				
                painel.Refresh();
				painel.repaint();	
				
                p.setVisible(true);
                menu.dispose();
            }

            else
            {
                JOptionPane.showMessageDialog(null,"Erro ao abrir o arquivo","ALERTA!!", JOptionPane.INFORMATION_MESSAGE);
            }

            }});
		}
	
	 	void ParticipantesEfetivos(int n)
	 	{
	 		Interador.getInterador().RetiraJogadores(n);
	 	}
					
	}
	