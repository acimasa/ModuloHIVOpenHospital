package org.isf.visits.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.DefaultComboBoxModel;

import org.isf.visits.manager.VisitManager;
import org.isf.visits.model.ListWorkingDay;
import org.isf.visits.model.WorkingDay;

import java.awt.Dialog.ModalityType;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InsertWorkingDay extends JDialog {

	private final VisitManager		Man;
	private final JPanel contentPanel = new JPanel();
	private final String Ore[];
	private final String Minuti[];
	private final String Giorni[];
	private final DefaultComboBoxModel GiorniModel;
	private final DefaultComboBoxModel OreStartModel;
	private final DefaultComboBoxModel MinStartModel;
	private final DefaultComboBoxModel OreStartBreakModel;
	private final DefaultComboBoxModel MinStartBreakModel;
	private final DefaultComboBoxModel OreFineModel;
	private final DefaultComboBoxModel MinFineModel;
	private final DefaultComboBoxModel OreFineBreakModel;
	private final DefaultComboBoxModel MinFineBreakModel;
	private		  JComboBox 		comboDayBox;
	private		  JComboBox 		startHourBox;
	private		  JComboBox 		startMinuteBox;
	private 	  JComboBox 		endHourBox;
	private 	  JComboBox 		endMinuteBox;
	private 	  JComboBox 		starHourBreakBox;
	private 	  JComboBox 		startMinuteBreakBox;
	private 	  JComboBox 		endHourBreakBox;
	private 	  JComboBox 		endMinuteBreakBox;
	private 	  GroupLayout 		gl_contentPanel;
	private 		JLabel 			lblStartAt;
	private 		JLabel 			lblTo;
	private 		JLabel 			lblBreakFrom;
	private 		JLabel 			lblEndAt;
	private final MouseAdapter 		Azione;
	private 	  JButton 			okButton;
	private 	  JButton 			cancelButton;
	private 	  ListWorkingDay	ListaGiorniLavorativi;
	
	public InsertWorkingDay(final String Behaviour) 
	{
		Man						= 	VisitManager.getInstance();
		ListaGiorniLavorativi	=	Man.getListWorkingDay();
		
		Ore						=	new String[]{"1", "2", "3", "4", 
								"5", "6", "7","8","9",
								"10","11","12","13","14",
								"15","16","17","18","19","20",
								"21","22","23","24"};
		Minuti					=	new String[]{"5", "10", "15", "20", 
								"25", "30", "35", "40",
								"45", "50", "55"};
		Giorni 					=	new String[]{"Sunday", "Monday", "Tursday", 
								"Wednesday", "Thursday", "Friday", "Saturday"}; 
		GiorniModel				=	new DefaultComboBoxModel(Giorni);
		OreStartModel			=	new DefaultComboBoxModel(Ore);
		MinStartModel			=	new DefaultComboBoxModel(Minuti);
		OreFineModel			=	new DefaultComboBoxModel(Ore);
		MinFineModel			=	new DefaultComboBoxModel(Minuti);
		OreStartBreakModel		=	new DefaultComboBoxModel(Ore);
		MinStartBreakModel		=	new DefaultComboBoxModel(Minuti);
		OreFineBreakModel		=	new DefaultComboBoxModel(Ore);
		MinFineBreakModel		=	new DefaultComboBoxModel(Minuti);
		lblStartAt 				= 	new JLabel("Start at");
		lblTo 					= 	new JLabel("to");
		lblBreakFrom 			= 	new JLabel("Break from");
		lblEndAt 				= 	new JLabel("End at");
		okButton				=	new JButton("Ok");
		cancelButton			=	new JButton("Cancel");
		comboDayBox 			= 	new JComboBox();
		startHourBox	 		= 	new JComboBox();
		startMinuteBox 			= 	new JComboBox();
		endHourBox				= 	new JComboBox();
		endMinuteBox 			=	new JComboBox();
		starHourBreakBox		= 	new JComboBox();
		startMinuteBreakBox 	= 	new JComboBox();
		endHourBreakBox 		= 	new JComboBox();
		endMinuteBreakBox 		= 	new JComboBox();
		gl_contentPanel 		= 	new GroupLayout(contentPanel);
		startHourBox.setModel(OreStartModel);
		
		Azione				=  	new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				
				if(e.getSource().equals(cancelButton))
					dispose();
				/*
				 * Se è stato premuto il pulsante per l'aggiunta della visita
				 * allora procede all'inserimento di questa richiamando
					le opportuna funzione e quindi eseguendo un comportamento,
					altrienti procede ad eseguire la modifica di un giorno di visita 
					selezionato
				 */
				else if(Behaviour.compareTo("aggiungi")==0)
				{
					WorkingDay Giorno= getWorkingDay();
					Man.addWorkingDay(Giorno);
					dispose();
	
				}
				else
				{
					WorkingDay Giorno= getWorkingDay();
					Man.modWorkingDay(Giorno);
					dispose();
				}
			}
		};
		
		setResizable(false);
		setTitle("Working Day Settings");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 432, 141);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		comboDayBox.setModel(GiorniModel);	
		startMinuteBox.setModel(MinStartModel);
		endHourBox.setModel(OreFineModel);
		endMinuteBox.setModel(MinFineModel);
		starHourBreakBox.setModel(OreStartBreakModel);
		startMinuteBreakBox.setModel(MinStartBreakModel);
		endHourBreakBox.setModel(OreFineBreakModel);
		endMinuteBreakBox.setModel(MinFineBreakModel);
		
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addComponent(comboDayBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(lblBreakFrom)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(starHourBreakBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(5)
							.addComponent(startMinuteBreakBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblTo)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(endHourBreakBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(endMinuteBreakBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(lblStartAt)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(startHourBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(startMinuteBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblEndAt)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(endHourBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(endMinuteBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(140, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboDayBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblStartAt)
						.addComponent(startHourBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(startMinuteBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblEndAt)
						.addComponent(endHourBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(endMinuteBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBreakFrom)
						.addComponent(starHourBreakBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblTo)
						.addComponent(endHourBreakBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(endMinuteBreakBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(startMinuteBreakBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(159, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton.addMouseListener(Azione);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton.addMouseListener(Azione);
				buttonPane.add(cancelButton);
			}
		}
	}
	/*
	 * Preleva il giorno lavorativo dalla GUI e ritorna il tipo
	 * WorkingDay correttamente istanziato e inizializzato con
	 * i valore prelevati
	 */
	private WorkingDay getWorkingDay()
	{	
		Integer Convertitore;
		WorkingDay GiornoLavorativo = new WorkingDay();
		GiornoLavorativo.setDay((String)comboDayBox.getSelectedItem());
		
		Convertitore= new Integer((String)endHourBox.getSelectedItem());
		GiornoLavorativo.setEndHour(Convertitore.intValue());
		
		Convertitore= new Integer((String)endHourBreakBox.getSelectedItem());
		GiornoLavorativo.setEndHourPause(Convertitore.intValue());
		
		Convertitore= new Integer((String)endMinuteBox.getSelectedItem());
		GiornoLavorativo.setEndMinute(Convertitore.intValue());
		
		Convertitore= new Integer((String)endMinuteBreakBox.getSelectedItem());
		GiornoLavorativo.setEndMinutePause(Convertitore.intValue());
		
		Convertitore= new Integer((String)startHourBox.getSelectedItem());
		GiornoLavorativo.setStartHour(Convertitore.intValue());
		
		Convertitore= new Integer((String)starHourBreakBox.getSelectedItem());
		GiornoLavorativo.setStartHourPause(Convertitore.intValue());
		
		Convertitore= new Integer((String)startMinuteBox.getSelectedItem());
		GiornoLavorativo.setStartMinute(Convertitore.intValue());
		
		Convertitore= new Integer((String)startMinuteBreakBox.getSelectedItem());
		GiornoLavorativo.setStartMinutePause(Convertitore.intValue());
		
		Convertitore=null;
		return GiornoLavorativo;
	}

}

