package org.isf.admission.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EventListener;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.EventListenerList;
import javax.swing.table.DefaultTableModel;

import org.isf.admission.manager.AdmissionBrowserManager;
import org.isf.admission.model.Admission;
import org.isf.disease.model.Disease;
import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.malnutrition.gui.MalnutritionBrowser;
import org.isf.menu.gui.MainMenu;
import org.isf.opd.gui.OpdEdit;
import org.isf.opd.gui.OpdEditExtended;
import org.isf.opd.manager.OpdBrowserManager;
import org.isf.opd.model.Opd;
import org.isf.patient.gui.PatientInsert;
import org.isf.patient.gui.PatientInsertExtended;
import org.isf.patient.gui.PatientSummary;
import org.isf.patient.model.Patient;
import org.isf.utils.jobjects.ModalJFrame;
import org.isf.utils.table.TableSorter;
import org.isf.ward.model.Ward;

/*----------------------------------------------------
 * (org.isf.admission.gui)PatientDataBrowser
 * ---------------------------------------------------
 * modification history
 * 08/09/2008 - alex - added OPD in the table
 * 					 - modified EDIT and DELETE methods to match the selection
 * 					 - fixed record elimination in the view port
 * 					 - modified some panels in GUI
 *------------------------------------------*/

/**
* This class shows and allows to modify all patient data and all patient admissions 
* 
* last release  oct-23-06
* @author flavio
* 
*/
public class PatientDataBrowser extends ModalJFrame implements 
				PatientInsert.PatientListener, PatientInsertExtended.PatientListener, AdmissionBrowser.AdmissionListener, OpdEditExtended.SurgeryListener {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EventListenerList deleteAdmissionListeners = new EventListenerList();

    public interface DeleteAdmissionListener extends EventListener {
        public void deleteAdmissionUpdated(AWTEvent e);
    }

    public void addDeleteAdmissionListener(DeleteAdmissionListener l) {
        deleteAdmissionListeners.add(DeleteAdmissionListener.class, l);
    }

    public void removeDeleteAdmissionListener(DeleteAdmissionListener listener) {
        deleteAdmissionListeners.remove(DeleteAdmissionListener.class, listener);
    }

    
    private void fireDeleteAdmissionUpdated(Admission admission) {
        AWTEvent event = new AWTEvent(admission , AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};

        EventListener[] listeners = deleteAdmissionListeners.getListeners(DeleteAdmissionListener.class);
        for (int i = 0; i < listeners.length; i++)
            ((DeleteAdmissionListener)listeners[i]).deleteAdmissionUpdated(event);
    }	
	
	//---------------------------------------------------------------------
	
	public void patientInserted(AWTEvent e) {
	}

	public void patientUpdated(AWTEvent e) {
		jContentPane = null;
		initialize();		
	}
	
	public void surgeryInserted(AWTEvent e) {
	}
	
	public void surgeryUpdated(AWTEvent e) {
		jContentPane = null;
		initialize();
	}
	
	public void admissionInserted(AWTEvent e) {
	}

	public void admissionUpdated(AWTEvent e) {
		jContentPane = null;
		initialize();		
	}

	private Patient patient = null;
	private JFrame admittedPatientWindow = null;
	
	public PatientDataBrowser(AdmittedPatientBrowser parentWindow,  Patient myPatient) {
		super();
		patient = myPatient;
		admittedPatientWindow = parentWindow;
		initialize();
	}

	private void initialize() {

		this.setContentPane(getJContentPane());

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setTitle(MessageBundle.getMessage("angal.admission.patientdata"));
		
		pack();
		
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		
		Dimension mySize = getSize();
		
		setLocation((screenSize.width-mySize.width)/2,(screenSize.height-mySize.height)/2);
		setResizable(false);
		setVisible(true);
	}

	
	
	private JPanel jContentPane = null;

	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getPatientDataPanel(), java.awt.BorderLayout.NORTH);
			jContentPane.add(getButtonPanel(), java.awt.BorderLayout.SOUTH);
		}
		return jContentPane;
	}
	
	
	private JPanel patientData=null;
	private boolean isMalnutrition = false;
	
	private JPanel getPatientDataPanel(){
		patientData = new JPanel();
		patientData.setLayout(new BorderLayout());
		
		patientData.add(getTablesPanel(), BorderLayout.EAST);
		
		PatientSummary ps = new PatientSummary(patient);
		for (Admission elem : admList){
			if (elem.getType().equalsIgnoreCase("M")){
				isMalnutrition = true;
				break;
			}
		}
		if (isMalnutrition){
			patientData.add(ps.getPatientCompleteSummary(), BorderLayout.WEST);
		}
		else {
			//patientData.add(ps.getPatientDataPanel(), BorderLayout.WEST);			
			patientData.add(ps.getPatientCompleteSummary(), BorderLayout.WEST);
		}
		
		return patientData;
	}
	

	private ArrayList<Admission> admList;
	private ArrayList<Disease> disease;
//	private ArrayList<Operation> operation;
	private ArrayList<Ward> ward;
	private ArrayList<Opd> opdList;
	
//	private String[] pColums = {MessageBundle.getMessage("angal.admission.admissionm"),MessageBundle.getMessage("angal.admission.wards"), MessageBundle.getMessage("angal.admission.diagnosis"), MessageBundle.getMessage("angal.admission.operation"), MessageBundle.getMessage("angal.admission.result"), MessageBundle.getMessage("angal.admission.discharge") };
//	private int[] pColumwidth = {120, 150, 200, 200, 100, 120 };

	//Alex: modified to include OPD
	private String[] pColums = {MessageBundle.getMessage("angal.admission.datem"),MessageBundle.getMessage("angal.admission.wards"), MessageBundle.getMessage("angal.admission.diagnosisinm"), MessageBundle.getMessage("angal.admission.diagnosisoutm"), MessageBundle.getMessage("angal.admission.statusm") };
	private int[] pColumwidth = {120, 150, 200, 200, 120 };
	
	private DefaultTableModel admModel;
	
	//Alex: added table sorter, for java6 only
	//private TableRowSorter<AdmissionBrowserModel> adm_sorter;

	private JTable admTable;
	private TableSorter sorter;
	
	private JScrollPane scrollPane;
	
	private JPanel tablesPanel=null;
		
	private JPanel getTablesPanel(){
		tablesPanel = new JPanel(new BorderLayout());
		
		//Alex: added sorters, for Java6 only
//		admModel = new AdmissionBrowserModel();
//		admTable = new JTable(admModel);
		
		//Alex: Java5 compatible
		admModel = new AdmissionBrowserModel();
		sorter = new TableSorter(admModel);
		admTable = new JTable(sorter);      
		//sorter.addMouseListenerToHeaderInTable(admTable); no needed
		sorter.sortByColumn(0, false);
				
		for (int i=0;i<pColums.length; i++){
			admTable.getColumnModel().getColumn(i).setPreferredWidth(pColumwidth[i]);
		}
				
		scrollPane = new JScrollPane(admTable);
		scrollPane.setPreferredSize(new Dimension(500,440));
		tablesPanel.add(scrollPane, BorderLayout.CENTER);
		
		//Alex: added sorter, for Java6 only
//		adm_sorter = new TableRowSorter<AdmissionBrowserModel>((AdmissionBrowserModel) admTable.getModel());
//		for(int i=0; i < admTable.getColumnCount(); i++)
//			adm_sorter.setComparator(i, new TableSorter1());
//		admTable.setRowSorter(adm_sorter);
//		//Alex: perform auto sorting on date descending
//		ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
//		sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
//		adm_sorter.setSortKeys(sortKeys);
//		adm_sorter.sort();
		
		return tablesPanel;
	}
	
	
	private JPanel getButtonPanel() {
		JPanel buttonPanel; 
			buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
//			if (MainMenu.checkUserGrants("btndataeditpat")) buttonPanel.add(getEditPatientButton(), null);  
			if (MainMenu.checkUserGrants("btndataedit")) buttonPanel.add(getEditButton(), null); 
			if (MainMenu.checkUserGrants("btndatadel")) buttonPanel.add(getDeleteButton(), null);  
			if (MainMenu.checkUserGrants("btndatamalnut")) buttonPanel.add(getMalnutritionButton(), null);  
			buttonPanel.add(getCloseButton(), null);
		return buttonPanel;
	}

	
	private JButton closeButton=null;
//	private JButton patientButton=null;
	private JButton editButton=null;
	private JButton deleteButton=null;
	private JButton malnutritionButton=null;
	
	private JButton getCloseButton() {
		if (closeButton == null) {
			closeButton = new JButton();
			closeButton.setMnemonic(KeyEvent.VK_C);
			closeButton.setText(MessageBundle.getMessage("angal.admission.close"));  
			closeButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return closeButton;
	}

//	private JButton getEditPatientButton() {
//		if (patientButton == null) {			
//			patientButton = new JButton();
//			patientButton.setMnemonic(KeyEvent.VK_P);
//			patientButton.setText(MessageBundle.getMessage("angal.admission.editpatient"));  
//			patientButton.addActionListener(new java.awt.event.ActionListener() {
//				public void actionPerformed(java.awt.event.ActionEvent e) {
//					if (GeneralData.PATIENTEXTENDED){
//						PatientInsertExtended editor = new PatientInsertExtended(PatientDataBrowser.this, patient, false);
//						editor.addPatientListener(PatientDataBrowser.this);
//						editor.setVisible(true);
//					}
//					else {
//						PatientInsert editor = new PatientInsert(PatientDataBrowser.this, patient, false);
//						editor.addPatientListener(PatientDataBrowser.this);
//						editor.setVisible(true);
//					}
//				}
//			});
//		}
//		return patientButton;
//	}
	
	private JButton getEditButton() {
		if (editButton == null) {			
			editButton = new JButton();
			editButton.setMnemonic(KeyEvent.VK_A);
			editButton.setText(MessageBundle.getMessage("angal.admission.editm"));  
			editButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (admTable.getSelectedRow() < 0) {
						JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.admission.pleaseselectarow"),
								MessageBundle.getMessage("angal.hospital"), JOptionPane.PLAIN_MESSAGE);
						return;
					}
					
					int selectedRow = admTable.getSelectedRow();
					Object selectedObj = sorter.getValueAt(selectedRow, -1);

					if (selectedObj instanceof Admission) {
											
						Admission ad = (Admission) sorter.getValueAt(selectedRow, -1);
						new AdmissionBrowser(PatientDataBrowser.this,admittedPatientWindow, patient, ad);
					} else {
					
						Opd opd = (Opd)sorter.getValueAt(selectedRow, -1);
						if (GeneralData.OPDEXTENDED) {
							OpdEditExtended newrecord = new OpdEditExtended(PatientDataBrowser.this, opd, false);
							newrecord.setVisible(true);
						} else {
							OpdEdit newrecord = new OpdEdit(PatientDataBrowser.this, opd, false);
							newrecord.setVisible(true);
						}
					}
				}
			});
		}
		return editButton;	
	}
			
	private JButton getDeleteButton() {
		if (deleteButton == null) {			
			deleteButton = new JButton();
			deleteButton.setMnemonic(KeyEvent.VK_D);
			deleteButton.setText(MessageBundle.getMessage("angal.admission.deletem"));  
			deleteButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (admTable.getSelectedRow() < 0) {
						JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.admission.pleaseselectarow"),
								MessageBundle.getMessage("angal.hospital"), JOptionPane.PLAIN_MESSAGE);
						return;
					}
					
					int selectedRow = admTable.getSelectedRow();
					Object selectedObj = sorter.getValueAt(selectedRow, -1);
					
					if (selectedObj instanceof Admission) {
						
						Admission adm = (Admission) sorter.getValueAt(selectedRow, -1);
						AdmissionBrowserManager abm = new AdmissionBrowserManager();
						
						int n = JOptionPane.showConfirmDialog(
		                        null,
		                        MessageBundle.getMessage("angal.admission.deleteselectedadmission"),
		                        MessageBundle.getMessage("angal.hospital"),
		                        JOptionPane.YES_NO_OPTION);
	
						if ((n == JOptionPane.YES_OPTION) && abm.setDeleted(adm.getId())){
							admList.remove(adm);
							admModel.fireTableDataChanged();
							admTable.updateUI();
							sorter.sortByColumn(0, false);
							if (adm.getAdmitted()==1){
								fireDeleteAdmissionUpdated(adm);
							}
							PatientDataBrowser.this.requestFocus();
						}	
					} else {
						Opd opd = (Opd) sorter.getValueAt(selectedRow, -1);
						OpdBrowserManager delOpd = new OpdBrowserManager();
						
						int n = JOptionPane.showConfirmDialog(
		                        null,
		                        MessageBundle.getMessage("angal.admission.deleteselectedopd"),
		                        MessageBundle.getMessage("angal.hospital"),
		                        JOptionPane.YES_NO_OPTION);
						
						if ((n == JOptionPane.YES_OPTION) && (delOpd.deleteOpd(opd))){
							opdList.remove(opd);
							admModel.fireTableDataChanged();
							admTable.updateUI();
							sorter.sortByColumn(0, false);
						}	
					}
				}
			});
		}
		return deleteButton;	
	}
	
	
	private JButton getMalnutritionButton() {
		if (malnutritionButton == null) {			
			malnutritionButton = new JButton();
			malnutritionButton.setMnemonic(KeyEvent.VK_M);
			malnutritionButton.setText(MessageBundle.getMessage("angal.admission.malnutritioncontrol"));  
			malnutritionButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (admTable.getSelectedRow() < 0) {
						JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.admission.pleaseselectarow"),
								MessageBundle.getMessage("angal.hospital"), JOptionPane.PLAIN_MESSAGE);
						return;
					}
					int selectedRow = admTable.getSelectedRow();
					Object selectedObj = sorter.getValueAt(selectedRow, -1);
					//String Selection = (String) admTable.getValueAt(selectedRow, 1);
					//selectedRow = admTable.convertRowIndexToModel(selectedRow);
					
					if (selectedObj instanceof Admission) {
						
						Admission ad = (Admission) sorter.getValueAt(selectedRow, -1);
						//Admission ad = (Admission) (((AdmissionBrowserModel) admModel)
							//	.getValueAt(selectedRow, -1));
						if (ad.getType().equalsIgnoreCase("M")){
							new MalnutritionBrowser(PatientDataBrowser.this, ad);
						}
						else {
							JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.admission.theselectedadmissionhasnoconcernwithmalnutrition"),
									MessageBundle.getMessage("angal.hospital"), JOptionPane.PLAIN_MESSAGE);
							return;
						}
					} else {
						JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.admission.opdhasnoconcernwithmalnutrition"),
								MessageBundle.getMessage("angal.hospital"), JOptionPane.PLAIN_MESSAGE);
						return;
					}
				}
			});
		}
		return malnutritionButton;	
	}
	
//Alex: nuovo AdmissionBrowserModel con OPD
class AdmissionBrowserModel extends DefaultTableModel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -453243229156512947L;

		public AdmissionBrowserModel() {
			AdmissionBrowserManager manager = new AdmissionBrowserManager();
			admList = manager.getAdmissions(patient);
			Collections.sort(admList);
			Collections.reverse(admList);
			org.isf.disease.manager.DiseaseBrowserManager dbm = new org.isf.disease.manager.DiseaseBrowserManager();
			disease = dbm.getDisease();
//			org.isf.operation.manager.OperationBrowserManager obm = new org.isf.operation.manager.OperationBrowserManager();
//			operation = obm.getOperation();
			org.isf.ward.manager.WardBrowserManager wbm = new org.isf.ward.manager.WardBrowserManager();
			ward = wbm.getWard();
			OpdBrowserManager opd = new OpdBrowserManager();
			opdList = opd.getOpdList(patient.getCode());
		}
		
		
		public int getRowCount() {
			if (admList == null && opdList == null)
				return 0;
			
			return admList.size() + opdList.size();
		}

		public String getColumnName(int c) {
			return pColums[c];
		}

		public int getColumnCount() {
			return pColums.length;
		}	
		
		
		public Object getValueAt(int r, int c) {
			if (c == -1) {
				if (r < admList.size())	{
					return admList.get(r);
				} else {
					int z = r - admList.size();
					return opdList.get(z);
				}
			
			} else if (c == 0) {
				if (r < admList.size()) {
					Date myDate = (admList.get(r)).getAdmDate().getTime();	
					DateFormat currentDateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALIAN);
					return currentDateFormat.format(myDate);
				} else {
					int z = r - admList.size();
					Date myDate = (opdList.get(z)).getVisitDate().getTime();
					DateFormat currentDateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALIAN);
					return currentDateFormat.format(myDate);
				}
				
			} else if (c == 1) {				
				if (r < admList.size()) {
					String id = admList.get(r).getWardId();
					for (Ward elem : ward) {
						if (elem.getCode().equalsIgnoreCase(id))
							return elem.getDescription();
					}
				} else {
					return "OPD";
				}
			}
			else if (c == 2) {
				String id = null;
				if (r < admList.size()) {
					id = admList.get(r).getDiseaseInId();
					if (id == null){
						id = "";
					}	
				} else {
					int z = r - admList.size();
					id = opdList.get(z).getDisease();
					if (id == null){
						id = "";
					}
				}
				for (Disease elem : disease) {
					if (elem.getCode().equalsIgnoreCase(id))
						return elem.getDescription();
				}				
				return MessageBundle.getMessage("angal.admission.nodisease");
	
			}else if (c == 3) {
				String id = null;
				if (r < admList.size()) {
					id = admList.get(r).getDiseaseOutId1();
					if (id == null){
						id = "";
					}
				} else {
					int z = r - admList.size();
					id = opdList.get(z).getDisease3();
					if (id == null){
						id = opdList.get(z).getDisease2();
						if (id == null){
							id = "";
						}
					}
				}
				for (Disease elem : disease) {
					if (elem.getCode().equalsIgnoreCase(id))
						return elem.getDescription();
				}				
				return MessageBundle.getMessage("angal.admission.nodisease");
				
			}  else if (c == 4) {
				if (r < admList.size()) {
					if (admList.get(r).getDisDate()==null)
						return MessageBundle.getMessage("angal.admission.present");
					else {
						Date myDate = admList.get(r).getDisDate().getTime();
						DateFormat currentDateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALIAN);
						return currentDateFormat.format(myDate);
					}
				} else {
					int z = r - admList.size();
					String status = opdList.get(z).getNewPatient();
					return (status.compareTo("R")==0?MessageBundle.getMessage("angal.opd.reattendance"):MessageBundle.getMessage("angal.opd.newattendance"));
				}
			} 
			
			return null;
		}




		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			// return super.isCellEditable(arg0, arg1);
			return false;
		}
	}
}// class
