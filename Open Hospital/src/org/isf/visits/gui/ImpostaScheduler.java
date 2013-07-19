package org.isf.visits.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Date;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTable;

import javax.swing.table.DefaultTableModel;

import org.isf.utils.jobjects.ModalJFrame;
import org.isf.visits.model.WorkingDay;
import org.isf.visits.manager.VisitManager;

public class ImpostaScheduler extends ModalJFrame{
	
	private static final long serialVersionUID = 1L;
	JPanel 				WorkingPanel;
	JPanel 				NoWorkingPanel;
	JPanel 				TimeVisitPanel;
	JButton 			AddVisitDayBtn;
	JButton 			ModVisitDayBtn;
	JButton 			RmVisitDayBtn;
	JButton 			AddNoVisitDayBtn;
	JButton 			RmNoVisitDayBtn;
	WorkingDay 			GiorniLavorativi;
	Date[] 				GiorniNonLavorativi;
	VisitManager 	  	Manager;
	DefaultTableModel 	WorkingTableData;
	DefaultTableModel	NoWorkingTableData;
	JTable				WorkingTable;
	JTable				NoWorkingTable;
	
	public ImpostaScheduler(JFrame Owner)
	{
		//Inizio creazione tabella giorni lavorativi
		WorkingTable		= 	new JTable();
		WorkingTableData	=	new DefaultTableModel();
		AddVisitDayBtn 		=	new JButton("InserisciTEST");
		ModVisitDayBtn 		=	new JButton("ModificaGiorno");
		RmVisitDayBtn 		=	new JButton("Rimuovi");
		WorkingPanel 		=	new JPanel();
		WorkingTableData.addColumn("Test1");
		WorkingTableData.addColumn("Test1");
		WorkingTableData.addColumn("Test1");
		WorkingPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		WorkingPanel.add(AddVisitDayBtn);
		WorkingPanel.add(ModVisitDayBtn);
		WorkingPanel.add(RmVisitDayBtn);
		WorkingTable		= 	new JTable(WorkingTableData);
		WorkingPanel.add(WorkingTable);
		//Fine creazione pannello giorni lavorativi
		//Inizio creazione pannello giorni non lavorativi
		
		NoWorkingPanel		=	new JPanel();
		AddNoVisitDayBtn	=	new JButton("Aggiungi Giorno non lavorativi");
		RmNoVisitDayBtn		=	new JButton("Rimuovi Giorni non lavorativo");
		NoWorkingTableData 	=	new DefaultTableModel();
		NoWorkingTableData.addColumn("Test1");
		NoWorkingTableData.addColumn("Test1");
		NoWorkingTableData.addColumn("Test1");
		NoWorkingTable		= 	new JTable(NoWorkingTableData);
		NoWorkingPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		NoWorkingPanel.add(NoWorkingTable);
		NoWorkingPanel.add(AddNoVisitDayBtn);
		NoWorkingPanel.add(RmNoVisitDayBtn);
		//Fine Creazione pannello giorni festivi
		this.add(WorkingPanel,BorderLayout.NORTH);
		this.add(NoWorkingPanel,BorderLayout.SOUTH);
		
		initComponents();
		this.showAsModal(Owner); //disattiva la finestra sottostante
	}
	/* Metodo dedicato al prelievo e all'inizializzazione delle tabelle
	 * all'interno della classe
	 */
	private void initComponents()
	{
		
	}
}
