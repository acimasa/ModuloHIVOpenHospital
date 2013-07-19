package org.isf.hiv.gui;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.isf.utils.jobjects.ModalJFrame;


public class Menu extends ModalJFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel 	MenuPanel;
	private JButton AdmitNewPatBtn;
	private JButton EditRmPatBtn;
	private JButton AddEditVisitBtn;
	private JButton VisitsCalendarBtn;
	private JButton SearchBtn;
	private JButton SettingsBtn;
	
	public Menu(JFrame Owner)
	{
		AdmitNewPatBtn		=	new JButton("AdmitTest");
		EditRmPatBtn		= 	new JButton("EditTest");
		EditRmPatBtn		=	new JButton("RmPatBtn");
		AddEditVisitBtn 	= 	new JButton("AddEditBtnTest");
		VisitsCalendarBtn	=	new JButton("VisitsBtnTest");
		SearchBtn			=	new JButton("SearchTest");
		SettingsBtn 		= 	new JButton("SettingsTest");
		MenuPanel			= 	new JPanel();
		MenuPanel.setLayout(new BoxLayout(MenuPanel,BoxLayout.Y_AXIS));
		MenuPanel.add(AdmitNewPatBtn);
		MenuPanel.add(EditRmPatBtn);
		MenuPanel.add(AddEditVisitBtn);
		MenuPanel.add(VisitsCalendarBtn);
		MenuPanel.add(SearchBtn);
		MenuPanel.add(SettingsBtn);
		this.add(MenuPanel,BorderLayout.CENTER);
		this.showAsModal(Owner);
	}
	
}
