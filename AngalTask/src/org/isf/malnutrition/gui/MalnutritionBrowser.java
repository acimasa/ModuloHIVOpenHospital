package org.isf.malnutrition.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.isf.admission.model.Admission;
import org.isf.malnutrition.manager.MalnutritionManager;
import org.isf.malnutrition.model.Malnutrition;
import org.isf.generaldata.MessageBundle;

public class MalnutritionBrowser extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Malnutrition malnutrition;

	private JPanel jContentPane;

	private JPanel buttonPanel;

	private JButton newButton;

	private JButton closeButton;

	private JButton deleteButton;

	private JButton editButton;

	private JDialog myDialog;

	private String admId;

	private JTable table;

	private String[] pColums = { MessageBundle.getMessage("angal.malnutrition.datesuppm"),MessageBundle.getMessage("angal.malnutrition.dateconfm"),MessageBundle.getMessage("angal.malnutrition.heightm"),MessageBundle.getMessage("angal.malnutrition.weightm") };

	private int[] pColumwidth = { 200, 200, 150, 150 };

	private DefaultTableModel model;

	private ArrayList<Malnutrition> pMaln;

	private int selectedrow;

	private Admission adm;

	public MalnutritionBrowser(JFrame owner, Admission aAdm) {
		super(owner, true);
		myDialog = this;
		adm = aAdm;
		admId = String.valueOf(adm.getId());
		setTitle(MessageBundle.getMessage("angal.malnutrition.malnutritionbrowser"));
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screensize = kit.getScreenSize();
		final int pfrmBase = 10;
		final int pfrmWidth = 5;
		final int pfrmHeight = 4;
		this.setBounds((screensize.width - screensize.width * pfrmWidth
				/ pfrmBase) / 2, (screensize.height - screensize.height
				* pfrmHeight / pfrmBase) / 2, screensize.width * pfrmWidth
				/ pfrmBase, screensize.height * pfrmHeight / pfrmBase);
		add(getJContentPane());
		setVisible(true);
	}

	private JPanel getJContentPane() {
		jContentPane = new JPanel();
		jContentPane.setLayout(new BoxLayout(jContentPane, BoxLayout.Y_AXIS));
		jContentPane.add(new JScrollPane(getTable()));
		jContentPane.add(getButtonPanel());
		validate();
		return jContentPane;
	}

	private JPanel getButtonPanel() {
		buttonPanel = new JPanel();
		buttonPanel.add(getNewButton());
		buttonPanel.add(getEditButton());
		buttonPanel.add(getCloseButton());
		buttonPanel.add(getDeleteButton());
		return buttonPanel;
	}

	private JButton getNewButton() {
		newButton = new JButton(MessageBundle.getMessage("angal.malnutrition.new"));
		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				malnutrition = new Malnutrition(0, null, null, adm.getId(), 0, 0, 0);
				Malnutrition last = new Malnutrition(0, null, null, adm.getId(), 0, 0, 0);
				new InsertMalnutrition(myDialog, malnutrition, true);	
				if (!last.equals(malnutrition)) {
					pMaln.add(pMaln.size(), malnutrition);
					((MalnBrowsingModel) table.getModel())
							.fireTableDataChanged();
					if (table.getRowCount() > 0)
						table.setRowSelectionInterval(0, 0);
				}
			}
		});
		return newButton;

	}

	private JButton getEditButton() {
		editButton = new JButton(MessageBundle.getMessage("angal.malnutrition.edit"));
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (table.getSelectedRow() < 0) {
					JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.malnutrition.pleaseselectarow"),
							MessageBundle.getMessage("angal.hospital"), JOptionPane.PLAIN_MESSAGE);
					return;
				} else {
					selectedrow = table.getSelectedRow();
					malnutrition = (Malnutrition) (((MalnBrowsingModel) model)
							.getValueAt(selectedrow, -1));
					Malnutrition last = new Malnutrition(
							malnutrition.getCode(), malnutrition.getDateSupp(),
							malnutrition.getDateConf(),
							malnutrition.getAdmId(), malnutrition.getHeight(),
							malnutrition.getWeight(), malnutrition.getCode());
					new InsertMalnutrition(myDialog, malnutrition, false);
					if (!last.equals(malnutrition)) {
						pMaln.set(selectedrow, malnutrition);
						((MalnBrowsingModel) table.getModel())
								.fireTableDataChanged();
						table.updateUI();
						if ((table.getRowCount() > 0) && selectedrow > -1)
							table.setRowSelectionInterval(selectedrow,
									selectedrow);
					}
				}
			}
		});
		return editButton;
	}

	private JButton getDeleteButton() {
		deleteButton = new JButton(MessageBundle.getMessage("angal.malnutrition.delete"));
		deleteButton.setMnemonic(KeyEvent.VK_D);
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				MalnutritionManager manager = new MalnutritionManager();
				if (table.getSelectedRow() < 0) {
					JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.malnutrition.pleaseselectarow"),
							MessageBundle.getMessage("angal.hospital"), JOptionPane.PLAIN_MESSAGE);
					return;
				} else {
					Malnutrition m = (Malnutrition) (((MalnBrowsingModel) model)
							.getValueAt(table.getSelectedRow(), -1));
					int n = JOptionPane.showConfirmDialog(null,
							MessageBundle.getMessage("angal.malnutrition.deletemedical")+"?", MessageBundle.getMessage("angal.hospital"),
							JOptionPane.YES_NO_OPTION);

					if ((n == JOptionPane.YES_OPTION)
							&& (manager.deleteMalnutrition(m))) {
						pMaln.remove(table.getSelectedRow());
						model.fireTableDataChanged();
						table.updateUI();
					}
				}
			}
		});

		return deleteButton;
	}

	private JButton getCloseButton() {
		closeButton = new JButton(MessageBundle.getMessage("angal.malnutrition.close"));
		closeButton.setMnemonic(KeyEvent.VK_C);
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myDialog.dispose();
			};
		});
		return closeButton;
	}

	private JTable getTable() {
		model = new MalnBrowsingModel(admId);
		table = new JTable(model);
		table.getColumnModel().getColumn(0).setMaxWidth(pColumwidth[0]);
		table.getColumnModel().getColumn(1).setMaxWidth(pColumwidth[1]);
		table.getColumnModel().getColumn(2).setMaxWidth(pColumwidth[2]);
		table.getColumnModel().getColumn(3).setMaxWidth(pColumwidth[3]);
		return table;
	}

	class MalnBrowsingModel extends DefaultTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public MalnBrowsingModel(String s) {
			MalnutritionManager manager = new MalnutritionManager();
			pMaln = manager.getMalnutrition(s);
		}

		public int getRowCount() {
			if (pMaln == null)
				return 0;
			return pMaln.size();
		}

		public String getColumnName(int c) {
			return pColums[c];
		}

		public int getColumnCount() {
			return pColums.length;
		}

		public Object getValueAt(int r, int c) {
			if (c == -1) {
				return pMaln.get(r);
			} else if (c == 0) {
				return getConvertedString(pMaln.get(r).getDateSupp());
			} else if (c == 1) {
				return getConvertedString(pMaln.get(r).getDateConf());
			} else if (c == 2) {
				return pMaln.get(r).getHeight();
			} else if (c == 3) {
				return pMaln.get(r).getWeight();
			}
			return null;
		}

		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			// return super.isCellEditable(arg0, arg1);
			return false;
		}
	}

	private String getConvertedString(GregorianCalendar time) {
		if (time == null)
			return MessageBundle.getMessage("angal.malnutrition.nodate");
		String string = String
				.valueOf(time.get(GregorianCalendar.DAY_OF_MONTH));
		string += "/" + String.valueOf(time.get(GregorianCalendar.MONTH) + 1);
		String year = String.valueOf(time.get(GregorianCalendar.YEAR));
		year = year.substring(2, year.length());
		string += "/" + year;
		return string;
	}

}