package org.isf.lab.gui;

/*------------------------------------------
 * LabBrowser - list all exams
 * -----------------------------------------
 * modification history
 * 02/03/2006 - theo, Davide - first beta version
 * 08/11/2006 - ross - changed button Show into Results
 *                     fixed the exam deletion
 * 					   version is now 1.0 
 * 04/01/2009 - ross - do not use roll, use add(week,-1)!
 *                     roll does not change the year! 
 *------------------------------------------*/

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.isf.exa.manager.ExamBrowsingManager;
import org.isf.exa.model.Exam;
import org.isf.exatype.model.ExamType;
import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.lab.gui.LabEdit.LabEditListener;
import org.isf.lab.gui.LabEditExtended.LabEditExtendedListener;
import org.isf.lab.gui.LabNew.LabListener;
import org.isf.lab.manager.LabManager;
import org.isf.lab.manager.LabRowManager;
import org.isf.lab.model.Laboratory;
import org.isf.lab.model.LaboratoryForPrint;
import org.isf.lab.model.LaboratoryRow;
import org.isf.menu.gui.MainMenu;
import org.isf.utils.jobjects.ModalJFrame;
import org.isf.utils.jobjects.VoDateTextField;

public class LabBrowser extends ModalJFrame implements LabListener, LabEditListener, LabEditExtendedListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void labInserted() {
		jTable.setModel(new LabBrowsingModel());
		
	}
	
	public void labUpdated() {
		filterButton.doClick();
	}
	
	private static final String VERSION=MessageBundle.getMessage("angal.versione");
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	private JPanel jContentPane = null;
	private JPanel jButtonPanel = null;
	private JButton buttonEdit = null;
	private JButton buttonNew = null;
	private JButton buttonDelete = null;
	private JButton buttonClose = null;
	private JButton printTableButton = null;
	private JButton filterButton = null;
	private JPanel jSelectionPanel = null;
	private JTable jTable = null;
	private JComboBox comboExams = null;
	private int pfrmHeight;
	private ArrayList<Laboratory> pLabs;
	private String[] pColums = { MessageBundle.getMessage("angal.lab.datem"), MessageBundle.getMessage("angal.lab.patient"), MessageBundle.getMessage("angal.lab.examm"), MessageBundle.getMessage("angal.lab.resultm") };
	private boolean[] columnsResizable = {false, true, true, false};
	private int[] pColumwidth = { 100, 200, 200, 200 };
	private int[] maxWidth = {150, 200, 200, 200};
	private boolean[] columnsVisible = { true, GeneralData.LABEXTENDED, true, true};
	private LabManager manager;
	private LabBrowsingModel model;
	private Laboratory laboratory;
	private int selectedrow;
	private String typeSelected = null;
	private VoDateTextField dateFrom = null;
	private VoDateTextField dateTo = null;
	private final JFrame myFrame;

	/**
	 * This is the default constructor
	 */
	public LabBrowser() {
		super();
		myFrame = this;
		manager = new LabManager();
		initialize();
		setResizable(false);
		setVisible(true);
	}

	/**
	 * This method initializes this Frame, sets the correct Dimensions
	 * 
	 * @return void
	 */
	private void initialize() {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screensize = kit.getScreenSize();
		final int pfrmBase = 20;
		final int pfrmWidth = 14;
		final int pfrmHeight = 12;
		this.setBounds((screensize.width - screensize.width * pfrmWidth
				/ pfrmBase) / 2, (screensize.height - screensize.height
				* pfrmHeight / pfrmBase) / 2, screensize.width * pfrmWidth
				/ pfrmBase, screensize.height * pfrmHeight / pfrmBase);
		this.setContentPane(getJContentPane());
		this.setTitle(MessageBundle.getMessage("angal.lab.laboratorybrowsing")+" ("+VERSION+")");
	}

	/**
	 * This method initializes jContentPane, adds the main parts of the frame
	 * 
	 * @return jContentPanel (JPanel)
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJButtonPanel(), java.awt.BorderLayout.SOUTH);
			jContentPane.add(getJSelectionPanel(), java.awt.BorderLayout.WEST);
			jContentPane.add(new JScrollPane(getJTable()),
					java.awt.BorderLayout.CENTER);
			validate();
		}
		return jContentPane;
	}

	/**
	 * This method initializes JButtonPanel, that contains the buttons of the
	 * frame (on the bottom)
	 * 
	 * @return JButtonPanel (JPanel)
	 */
	private JPanel getJButtonPanel() {
		if (jButtonPanel == null) {
			jButtonPanel = new JPanel();
			if (MainMenu.checkUserGrants("btnlaboratorynew")) jButtonPanel.add(getButtonNew(), null);
			if (MainMenu.checkUserGrants("btnlaboratoryedit")) jButtonPanel.add(getButtonEdit(), null);
			if (MainMenu.checkUserGrants("btnlaboratorydel")) jButtonPanel.add(getButtonDelete(), null);
			jButtonPanel.add((getCloseButton()), null);
			jButtonPanel.add((getPrintTableButton()), null);
		}
		return jButtonPanel;
	}

	private JButton getPrintTableButton() {
		if (printTableButton == null) {
			printTableButton = new JButton(MessageBundle.getMessage("angal.lab.printtable"));
			printTableButton.setMnemonic(KeyEvent.VK_P);
			printTableButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					LabRowManager rowManager = new LabRowManager();
					ArrayList<LaboratoryRow> rows = null;
					typeSelected = ((Exam) comboExams.getSelectedItem())
							.toString();
					if (typeSelected.equalsIgnoreCase(MessageBundle.getMessage("angal.lab.all")))
						typeSelected = null;
					ArrayList<LaboratoryForPrint> labs = manager
							.getLaboratoryForPrint(typeSelected, dateFrom
									.getDate(), dateTo.getDate());
					for (int i = 0; i < labs.size(); i++) {
						if (labs.get(i).getResult().equalsIgnoreCase(
								MessageBundle.getMessage("angal.lab.multipleresults"))) {
							rows = rowManager.getLabRow(labs.get(i).getCode());
							
							if (rows == null || rows.size() == 0) {
								labs.get(i).setResult(MessageBundle.getMessage("angal.lab.allnegative"));
							} else {
								labs.get(i).setResult(MessageBundle.getMessage("angal.lab.positive")+" : "+rows.get(0).getDescription());
								for (int j=1;j<rows.size();j++) {
									labs.get(i).setResult(
											labs.get(i).getResult() + ","
													+ rows.get(j).getDescription());
								}
							}
						}
					}
					new LabPrintFrame(myFrame, labs);
				}

			});
		}
		return printTableButton;
	}

	private JButton getButtonEdit() {
		if (buttonEdit == null) {
			buttonEdit = new JButton(MessageBundle.getMessage("angal.lab.edit"));
			buttonEdit.setMnemonic(KeyEvent.VK_S);
			buttonEdit.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent event) {
					selectedrow = jTable.getSelectedRow();
					if (selectedrow < 0) {
						JOptionPane.showMessageDialog(null,
								MessageBundle.getMessage("angal.lab.pleaseselectarow"), MessageBundle.getMessage("angal.hospital"),
								JOptionPane.PLAIN_MESSAGE);
						return;
					} 
					laboratory = (Laboratory) (((LabBrowsingModel) model).getValueAt(selectedrow, -1));
					if (GeneralData.LABEXTENDED) {
						LabEditExtended editrecord = new LabEditExtended(myFrame, laboratory, false);
						editrecord.addLabEditExtendedListener(LabBrowser.this);
						editrecord.setVisible(true);
					} else {
						LabEdit editrecord = new LabEdit(myFrame, laboratory, false);
						editrecord.addLabEditListener(LabBrowser.this);
						editrecord.setVisible(true);
					}
				}
			});
		}
		return buttonEdit;
	}

	/**
	 * This method initializes buttonNew, that loads LabEdit Mask
	 * 
	 * @return buttonNew (JButton)
	 */
	private JButton getButtonNew() {
		if (buttonNew == null) {
			buttonNew = new JButton(MessageBundle.getMessage("angal.lab.new"));
			buttonNew.setMnemonic(KeyEvent.VK_N);
			buttonNew.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent event) {
					laboratory = new Laboratory(0, new Exam("", "",
							new ExamType("", ""), 0, "", 0),
							new GregorianCalendar(), "P", 0, "", 0, "");
					if (GeneralData.LABEXTENDED) {
						if (GeneralData.LABMULTIPLEINSERT) {
							LabNew editrecord = new LabNew(myFrame);
							editrecord.addLabListener(LabBrowser.this);
							editrecord.setVisible(true);
						} else {
							LabEditExtended editrecord = new LabEditExtended(myFrame, laboratory, true);
							editrecord.addLabEditExtendedListener(LabBrowser.this);
							editrecord.setVisible(true);
						}
					} else {
						LabEdit editrecord = new LabEdit(myFrame, laboratory, true);
						editrecord.addLabEditListener(LabBrowser.this);
						editrecord.setVisible(true);
					}
				}
			});
		}
		return buttonNew;
	}

	/**
	 * This method initializes buttonDelete, that delets the selected records
	 * 
	 * @return buttonDelete (JButton)
	 */
	private JButton getButtonDelete() {
		if (buttonDelete == null) {
			buttonDelete = new JButton(MessageBundle.getMessage("angal.lab.delete"));
			buttonDelete.setMnemonic(KeyEvent.VK_D);
			buttonDelete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					if (jTable.getSelectedRow() < 0) {
						JOptionPane.showMessageDialog(null,
								MessageBundle.getMessage("angal.lab.pleaseselectarow"), MessageBundle.getMessage("angal.hospital"),
								JOptionPane.PLAIN_MESSAGE);
						return;
					} else {
						Laboratory lab = (Laboratory) (((LabBrowsingModel) model)
								.getValueAt(jTable.getSelectedRow(), -1));
						int n = JOptionPane.showConfirmDialog(null,
								MessageBundle.getMessage("angal.lab.deletefollowinglabexam")+"; " +
								"\n"+ MessageBundle.getMessage("angal.lab.registationdate")+"=" + getConvertedString(lab.getDate()) + 
								"\n "+ MessageBundle.getMessage("angal.lab.examdate")+"=" + getConvertedString(lab.getExamDate()) + 
								"\n "+ MessageBundle.getMessage("angal.lab.exam")+"=" + lab.getExam() + 
								"\n "+ MessageBundle.getMessage("angal.lab.patient")+" =" + lab.getPatName() + 
								"\n "+ MessageBundle.getMessage("angal.lab.result")+" =" + lab.getResult() + 
								"\n ?",
								MessageBundle.getMessage("angal.hospital"), JOptionPane.YES_NO_OPTION);

						if ((n == JOptionPane.YES_OPTION)
								&& (manager.deleteLaboratory(lab))) {
							pLabs.remove(pLabs.size() - jTable.getSelectedRow()
									- 1);
							model.fireTableDataChanged();
							jTable.updateUI();
						}
					}
				}

			});
		}
		return buttonDelete;
	}

	/**
	 * This method initializes buttonClose, that disposes the entire Frame
	 * 
	 * @return buttonClose (JButton)
	 */
	private JButton getCloseButton() {
		if (buttonClose == null) {
			buttonClose = new JButton(MessageBundle.getMessage("angal.lab.close"));
			buttonClose.setMnemonic(KeyEvent.VK_C);
			buttonClose.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return buttonClose;
	}

	/**
	 * This method initializes JSelectionPanel, that contains the filter objects
	 * 
	 * @return JSelectionPanel (JPanel)
	 */
	private JPanel getJSelectionPanel() {
		if (jSelectionPanel == null) {
			jSelectionPanel = new JPanel();
			jSelectionPanel.setPreferredSize(new Dimension(200, pfrmHeight));
			jSelectionPanel.add(new JLabel(MessageBundle.getMessage("angal.lab.selectanexam")), null);
			jSelectionPanel.add(getComboExams(), null);
			jSelectionPanel.add(new JLabel(MessageBundle.getMessage("angal.lab.datem") +":"+ MessageBundle.getMessage("angal.lab.from")), null);
			jSelectionPanel.add(getDateFromPanel());
			jSelectionPanel.add(new JLabel(MessageBundle.getMessage("angal.lab.datem") +":"+MessageBundle.getMessage("angal.lab.to") +"     "), null);
			jSelectionPanel.add(getDateToPanel());
			jSelectionPanel.add(getFilterButton());
		}
		return jSelectionPanel;
	}

	/**
	 * This method initializes jTable, that contains the information about the
	 * Laboratory Tests
	 * 
	 * @return jTable (JTable)
	 */
	private JTable getJTable() {
		if (jTable == null) {
			model = new LabBrowsingModel();
			jTable = new JTable(model);
			int columnLengh = pColumwidth.length;
			if (!GeneralData.LABEXTENDED) {
				columnLengh--;
			}
			for (int i=0;i<columnLengh; i++){
				jTable.getColumnModel().getColumn(i).setMinWidth(pColumwidth[i]);
				if (!columnsResizable[i]) jTable.getColumnModel().getColumn(i).setMaxWidth(maxWidth[i]);
			}
		}
		return jTable;
	}

	/**
	 * This method initializes comboExams, that allows to choose which Exam the
	 * user want to display on the Table
	 * 
	 * @return comboExams (JComboBox)
	 */
	private JComboBox getComboExams() {
		ExamBrowsingManager managerExams = new ExamBrowsingManager();
		if (comboExams == null) {
			comboExams = new JComboBox();
			comboExams.setPreferredSize(new Dimension(200, 30));
			comboExams.addItem(new Exam("", MessageBundle.getMessage("angal.lab.all"), new ExamType("", ""), 0, "",
					0));
			ArrayList<Exam> type = managerExams.getExams(); // for
			// efficiency
			// in
			// the sequent for
			for (Exam elem : type) {
				comboExams.addItem(elem);
			}
			comboExams.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					typeSelected = ((Exam) comboExams.getSelectedItem())
							.toString();
					if (typeSelected.equalsIgnoreCase(MessageBundle.getMessage("angal.lab.all")))
						typeSelected = null;

				}
			});
		}
		return comboExams;
	}

	/**
	 * This method initializes dateFrom, which is the Panel that contains the
	 * date (From) input for the filtering
	 * 
	 * @return dateFrom (JPanel)
	 */
	private VoDateTextField getDateFromPanel() {
		if (dateFrom == null) {
			GregorianCalendar now = new GregorianCalendar();
			//04/01/2009 - ross - do not use roll, use add(week,-1)!
			//now.roll(GregorianCalendar.WEEK_OF_YEAR, false);
			now.add(GregorianCalendar.WEEK_OF_YEAR, -1);
			dateFrom = new VoDateTextField("dd/mm/yyyy", now, 10);
		}
		return dateFrom;
	}

	/**
	 * This method initializes dateTo, which is the Panel that contains the date
	 * (To) input for the filtering
	 * 
	 * @return dateTo (JPanel)
	 */
	private VoDateTextField getDateToPanel() {
		if (dateTo == null) {
			GregorianCalendar now = new GregorianCalendar();
			dateTo = new VoDateTextField("dd/mm/yyyy", now, 10);
			dateTo.setDate(now);
		}
		return dateTo;
	}

	/**
	 * This method initializes filterButton, which is the button that perform
	 * the filtering and calls the methods to refresh the Table
	 * 
	 * @return filterButton (JButton)
	 */
	private JButton getFilterButton() {
		if (filterButton == null) {
			filterButton = new JButton(MessageBundle.getMessage("angal.lab.search"));
			filterButton.setMnemonic(KeyEvent.VK_S);
			filterButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					typeSelected = ((Exam) comboExams.getSelectedItem()).toString();
					if (typeSelected.equalsIgnoreCase(MessageBundle.getMessage("angal.lab.all")))
						typeSelected = null;
					model = new LabBrowsingModel(typeSelected, dateFrom.getDate(), dateTo.getDate());
					model.fireTableDataChanged();
					jTable.updateUI();
				}
			});
		}
		return filterButton;
	}

	/**
	 * This class defines the model for the Table
	 * 
	 * @author theo
	 * 
	 */
	class LabBrowsingModel extends DefaultTableModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private LabManager manager = new LabManager();

		public LabBrowsingModel(String exam, GregorianCalendar dateFrom, GregorianCalendar dateTo) {
			pLabs = manager.getLaboratory(exam, dateFrom, dateTo);
		}

		public LabBrowsingModel() {
			LabManager manager = new LabManager();
			pLabs = manager.getLaboratory();
		}

		public int getRowCount() {
			if (pLabs == null)
				return 0;
			return pLabs.size();
		}

		public String getColumnName(int c) {
			return pColums[getNumber(c)];
		}

		public int getColumnCount() {
			int c = 0;
			for (int i = 0; i < columnsVisible.length; i++) {
				if (columnsVisible[i]) {
					c++;
				}
			}
			return c;
		}

		/** 
	     * This method converts a column number in the table
	     * to the right number of the datas.
	     */
	    protected int getNumber(int col) {
	    	// right number to return
	        int n = col;    
	        int i = 0;
	        do {
	            if (!columnsVisible[i]) {
	            	n++;
	            }
	            i++;
	        } while (i < n);
	        // If we are on an invisible column, 
	        // we have to go one step further
	        while (!columnsVisible[n]) {
	        	n++;
	        }
	        return n;
	    }
	    
		/**
		 * Note: We must get the objects in a reversed way because of the query
		 * 
		 * @see org.isf.lab.service.IoOperations
		 */
		public Object getValueAt(int r, int c) {
			if (c == -1) {
				return pLabs.get(pLabs.size() - r - 1);
			} else if (getNumber(c) == 0) {
				return //getConvertedString(pLabs.get(pLabs.size() - r - 1).getDate());
					   dateFormat.format(pLabs.get(pLabs.size() - r - 1).getExamDate().getTime());
			} else if (getNumber(c) == 1) {
				return pLabs.get(pLabs.size() - r - 1).getPatName(); //Alex: added
			} else if (getNumber(c) == 2) {
				return pLabs.get(pLabs.size() - r - 1).getExam();
			} else if (getNumber(c) == 3) {
				return pLabs.get(pLabs.size() - r - 1).getResult();
			}
			return null;
		}

		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			// return super.isCellEditable(arg0, arg1);
			return false;
		}
	}

	/**
	 * This method updates the Table because a laboratory test has been updated
	 * Sets the focus on the same record as before
	 * 
	 */
	/*public void laboratoryUpdated() {
		pLabs.set(pLabs.size() - selectedrow - 1, laboratory);
		((LabBrowsingModel) jTable.getModel()).fireTableDataChanged();
		jTable.updateUI();
		if ((jTable.getRowCount() > 0) && selectedrow > -1)
			jTable.setRowSelectionInterval(selectedrow, selectedrow);
	}*/

	/**
	 * This method updates the Table because a laboratory test has been inserted
	 * Sets the focus on the first record
	 * 
	 */
	/*public void laboratoryInserted() {
		pLabs.add(pLabs.size(), laboratory);
		((LabBrowsingModel) jTable.getModel()).fireTableDataChanged();
		if (jTable.getRowCount() > 0)
			jTable.setRowSelectionInterval(0, 0);
	}
*/
	/**
	 * This method is needed to display the date in a more understandable format
	 * 
	 * @param time
	 * @return String
	 */
	private String getConvertedString(GregorianCalendar time) {
		String string = "";
		if (time!=null) {
			string=String
					.valueOf(time.get(GregorianCalendar.DAY_OF_MONTH));
			string += "/" + String.valueOf(time.get(GregorianCalendar.MONTH) + 1);
			string += "/" + String.valueOf(time.get(GregorianCalendar.YEAR));
			string += "  "
					+ String.valueOf(time.get(GregorianCalendar.HOUR_OF_DAY));
			string += ":" + String.valueOf(time.get(GregorianCalendar.MINUTE));
			string += ":" + String.valueOf(time.get(GregorianCalendar.SECOND));
		}
		return string;
	}
}
