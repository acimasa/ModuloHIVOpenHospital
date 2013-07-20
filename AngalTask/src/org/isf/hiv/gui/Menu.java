package org.isf.hiv.gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.isf.utils.jobjects.ModalJFrame;
import org.isf.visits.manager.VisitManager;


public class Menu extends ModalJFrame{
	/**Why use inner class?
	 * It is a way of logically grouping classes that are only used in one place. 
	 * Oracle DOC
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel 	MenuPanel;
	private final JButton AdmitNewPatBtn;
	private final JButton EditRmPatBtn;
	private final JButton AddEditVisitBtn;
	private final JButton VisitsCalendarBtn;
	private final JButton SearchBtn;
	private final JButton SettingsBtn;
	private final MouseAdapter Azione;
	private final VisitManager visitMan;
	public Menu()
	{
		AdmitNewPatBtn		=	new JButton("AdmitTest");
		EditRmPatBtn		=	new JButton("RmPatBtn");
		AddEditVisitBtn 	= 	new JButton("AddEditBtnTest");
		VisitsCalendarBtn	=	new JButton("VisitsBtnTest");
		SearchBtn			=	new JButton("SearchTest");
		SettingsBtn 		= 	new JButton("SettingsTest");
		MenuPanel			= 	new JPanel();
		visitMan			=	VisitManager.getInstance();
		//Creo l'azione che sarà eseguita in seguito alla pressione
		//di specifici tasti
		Azione				= 	new MouseAdapter(){
			public void mouseClicked(MouseEvent e)
			{
				if(e.getSource().equals(SettingsBtn))
				{
					System.out.println("premuto il pusante settings");
					final ModalJFrame Selection = new ModalJFrame();
					final JButton SchedSettBtn = new JButton("Imposta Scheduler");
					final JButton SchedIABtn = new JButton("Imposta IA");
					Selection.setLayout(new FlowLayout(FlowLayout.RIGHT));
					Selection.add(SchedSettBtn);
					Selection.add(SchedIABtn);
					Selection.setVisible(true);
					Selection.pack();
					SchedSettBtn.addMouseListener(new MouseAdapter()
					{
						public void mouseClicked(MouseEvent e)
						{
							visitMan.showSchedulerConfig();
							Selection.dispose();
						}
					});
				}
				else if(e.getSource().equals(SearchBtn))
				{
					System.out.println("premuto il pusante ricerca");
				}
				else if(e.getSource().equals(VisitsCalendarBtn))
				{
					System.out.println("premuto il pusante calendario");
				}
				else if(e.getSource().equals(AddEditVisitBtn))
				{
					System.out.println("premuto il pusante edit visit");
				}
				else if(e.getSource().equals(EditRmPatBtn))
				{
					System.out.println("premuto il pusante rimuovi");
				}
				else if(e.getSource().equals(AdmitNewPatBtn))
				{
					System.out.println("premuto il pusante ametti");
				}
			}
			
		};
		MenuPanel.setLayout(new BoxLayout(MenuPanel,BoxLayout.Y_AXIS));
		MenuPanel.add(AdmitNewPatBtn);
		MenuPanel.add(EditRmPatBtn);
		MenuPanel.add(AddEditVisitBtn);
		MenuPanel.add(VisitsCalendarBtn);
		MenuPanel.add(SearchBtn);
		MenuPanel.add(SettingsBtn);
		SettingsBtn.addMouseListener(Azione);
		SearchBtn.addMouseListener(Azione);
		VisitsCalendarBtn.addMouseListener(Azione);
		AddEditVisitBtn.addMouseListener(Azione);
		EditRmPatBtn.addMouseListener(Azione);
		AdmitNewPatBtn.addMouseListener(Azione);
		this.add(MenuPanel,BorderLayout.WEST);
		this.pack();
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
}
