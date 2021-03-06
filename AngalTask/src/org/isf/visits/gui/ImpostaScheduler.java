package org.isf.visits.gui;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.isf.visits.model.ListWorkingDay;
import org.isf.visits.model.WorkingDay;
import org.isf.visits.manager.VisitManager;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;

public class ImpostaScheduler extends JFrame implements Observer{

	private final JPanel 			contentPane;
	private final JTable 			VisitDayTable;
	private final JTable 			NoVisitDayTable;
	private final VisitManager 		Manager;
	private final MouseAdapter 		Azione;
	private final JScrollPane 		scrollPaneVisitDayTab;
	private final JButton			btnModifyVisitDay;
	private final JButton 			btnRemoveVisitDay;
	private final JScrollPane 		scrollPaneNoVisitDayTab;
	private final JButton 			btnAddNoVisitDay;
	private final JButton 			btnAddVisitDay;
	private final JButton 			btnRmNoVisitDay;
	private final JComboBox 		comboBox;
	private ArrayList <WorkingDay> 	GiorniRimossi;
	private ListWorkingDay 			ListaGiorniLavorativi;
	private JLabel 					lblTimePerVisit;
	private JButton 				btnSave;
	private JButton 				btnCancel;
	private GroupLayout 			gl_contentPane;

	public ImpostaScheduler() {
		setTitle("Scheduler Settings");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Manager			 			=	VisitManager.getInstance();
		scrollPaneVisitDayTab 		= 	new JScrollPane();
		btnModifyVisitDay 			= 	new JButton("Modify Visit Day");
		btnRemoveVisitDay 			= 	new JButton("Remove Visit Day");
		scrollPaneNoVisitDayTab 	= 	new JScrollPane();
		btnAddNoVisitDay 			= 	new JButton("+ Add No Visit Day");
		btnAddVisitDay			 	= 	new JButton("+ Add Visit Day");
		contentPane 				= 	new JPanel();
		btnRmNoVisitDay 			= 	new JButton("Remove No Visit Day");
		comboBox					= 	new JComboBox();
		lblTimePerVisit 			= 	new JLabel("Time per Visit");
		btnSave 					= 	new JButton("Save");
		btnCancel 					= 	new JButton("Cancel");
		gl_contentPane 				= 	new GroupLayout(contentPane);
		NoVisitDayTable 			= 	new JTable();
		VisitDayTable 				= 	new JTable(){
										public boolean isCellEditable(int row, int col) {
											return false;
										}};
		GiorniRimossi				=	new ArrayList <WorkingDay>();
		ListaGiorniLavorativi		=	Manager.getListWorkingDay();
		Azione 						= 	new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if(e.getSource().equals(btnAddVisitDay))
				{
					Manager.showInsertWorkingDay();
				}
				else if(e.getSource().equals(btnRemoveVisitDay))
				{
					WorkingDay GiornoRimosso	= new WorkingDay();
					int riga					= VisitDayTable.getSelectedRow();
					if(riga!=-1)
					{
						GiornoRimosso.setDay(VisitDayTable.getValueAt(riga, 0).toString());
						//salvo la lista dei giorni rimossi, per rimuoverli dal DB
						GiorniRimossi.add(GiornoRimosso);
						Manager.removeWD(GiornoRimosso);
					}
					else
					{
						JOptionPane.showMessageDialog(null,"No row selected","Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				else if(e.getSource().equals(btnModifyVisitDay))
				{
					Manager.showModWorkingDay();
				}
				else if(e.getSource().equals(btnAddNoVisitDay))
				{
					Manager.showInsertNoWorkingDay();
				}
			}
			
		};
		
		setBounds(100, 100, 558, 463);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		btnAddVisitDay.addMouseListener(Azione);
		btnAddNoVisitDay.addMouseListener(Azione);
		btnRmNoVisitDay.addMouseListener(Azione);
		btnRemoveVisitDay.addMouseListener(Azione);
		btnModifyVisitDay.addMouseListener(Azione);
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"10", "20", "30", "40", "50", "60", "70", "80", "90", "100", "110", "120"}));
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPaneVisitDayTab, GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnAddVisitDay)
					.addGap(18)
					.addComponent(btnModifyVisitDay)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnRemoveVisitDay)
					.addContainerGap(115, Short.MAX_VALUE))
				.addComponent(scrollPaneNoVisitDayTab, GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnAddNoVisitDay)
					.addGap(18)
					.addComponent(btnRmNoVisitDay)
					.addPreferredGap(ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
					.addComponent(btnSave)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnCancel)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblTimePerVisit)
					.addGap(18)
					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(371, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(scrollPaneVisitDayTab, GroupLayout.PREFERRED_SIZE, 134, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnAddVisitDay)
						.addComponent(btnModifyVisitDay)
						.addComponent(btnRemoveVisitDay))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTimePerVisit)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(20)
					.addComponent(scrollPaneNoVisitDayTab, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnAddNoVisitDay)
								.addComponent(btnRmNoVisitDay))
							.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnSave)
								.addComponent(btnCancel)))))
		);
		NoVisitDayTable.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Day", "Month"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, true
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		NoVisitDayTable.getColumnModel().getColumn(0).setResizable(false);
		scrollPaneNoVisitDayTab.setViewportView(NoVisitDayTable);
		/*VisitDayTable.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Day", "From", "To", "Break From", "Break To"
			}
		));*/
		update(null,null);
		scrollPaneVisitDayTab.setViewportView(VisitDayTable);
		contentPane.setLayout(gl_contentPane);
		init();
	}
	
	private void init()
	{
		/*
		 * procedere alla lettura dei dati nel database
		 * per inizializzare la tabella
		 */
		
	}

	@Override
	public void update(Observable arg0, Object arg1) 
	{
		DefaultTableModel Nuovo 	  = 	new DefaultTableModel();
		ArrayList <WorkingDay> Giorni =		Manager.getListWorkingDay().GiorniLavorativi;
		Nuovo.addColumn("Day");
		Nuovo.addColumn("From");
		Nuovo.addColumn("To");
		Nuovo.addColumn("Break From");
		Nuovo.addColumn("Break To");
		for(WorkingDay i : Giorni)
		{
			System.out.println(i.getDay());
			Nuovo.addRow(new String[]{i.getDay(),i.getStartHour()+":"+i.getStartMinute(),
					+i.getEndHour()+":"+i.getEndMinute(),i.getStartHourPause()+":"+i.getStartMinutePause(),
					i.getEndHourPause()+":"+i.getEndMinutePause()});
		}
		VisitDayTable.setModel(Nuovo);
	}
	
}
