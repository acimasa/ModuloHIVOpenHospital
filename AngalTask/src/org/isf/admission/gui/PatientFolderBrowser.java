package org.isf.admission.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EventListener;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.isf.admission.manager.AdmissionBrowserManager;
import org.isf.admission.model.Admission;
import org.isf.disease.model.Disease;
import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.lab.model.Laboratory;
import org.isf.opd.manager.OpdBrowserManager;
import org.isf.opd.model.Opd;
import org.isf.patient.gui.PatientInsert;
import org.isf.patient.gui.PatientInsertExtended;
import org.isf.patient.gui.PatientSummary;
import org.isf.patient.model.Patient;
import org.isf.stat.manager.GenericReportPatient;
import org.isf.utils.jobjects.ModalJFrame;
import org.isf.utils.table.TableSorter;
import org.isf.ward.model.Ward;




/** 
* This class shows patient data and the list of admissions and lab exams.
* 
* last release  jun-14-08
* @author chiara
* 
*/

/*----------------------------------------------------------
 * modification history
 * ====================
 * 14/06/08 - chiara - first version
 *                     
 * 30/06/08 - fabrizio - implemented automatic selection of exams within the admission period
 * 
 * 05/09/08 - alessandro - second version:
 * 						 - same PatientSummary than PatientDataBrowser
 * 						 - includes OPD in the table
 -----------------------------------------------------------*/
public class PatientFolderBrowser extends ModalJFrame implements 
				PatientInsert.PatientListener, PatientInsertExtended.PatientListener, AdmissionBrowser.AdmissionListener  {



	/**
	 * 
	 */
	private static final long serialVersionUID = -3427327158197856822L;
	
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
	
	//---------------------------------------------------------------------
	
	public void patientInserted(AWTEvent e) {
	}

	public void patientUpdated(AWTEvent e) {
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
	
	public PatientFolderBrowser(AdmittedPatientBrowser listener,  Patient myPatient) {
		super();
		patient = myPatient;
		initialize();
	}

	private void initialize() {

		this.setContentPane(getJContentPane());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle(MessageBundle.getMessage("angal.admission.patientdata"));

		pack();
		setLocationRelativeTo(null);
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
	
	private JPanel patientData = null;
	
	private JPanel getPatientDataPanel(){
		patientData = new JPanel();
		patientData.setLayout(new BorderLayout());
		patientData.add(getTablesPanel(), BorderLayout.EAST);
		
		PatientSummary ps = new PatientSummary(patient);
		JPanel pp = ps.getPatientCompleteSummary();
		patientData.add(pp, BorderLayout.WEST);
		
		return patientData;
	}
	
	private ArrayList<Admission> admList;
	private ArrayList<Laboratory> labList;	
	private ArrayList<Disease> disease;
//	private ArrayList<Operation> operation;
	private ArrayList<Ward> ward;
	private ArrayList<Opd> opdList;
	
	private String[] pColums = {MessageBundle.getMessage("angal.admission.datem"),MessageBundle.getMessage("angal.admission.wards"), MessageBundle.getMessage("angal.admission.diagnosisinm"), MessageBundle.getMessage("angal.admission.diagnosisoutm"), MessageBundle.getMessage("angal.admission.statusm") };
	private int[] pColumwidth = {120, 150, 200, 200, 120 };
	
	private String[] plColums = { MessageBundle.getMessage("angal.lab.datem"), MessageBundle.getMessage("angal.lab.examm"), MessageBundle.getMessage("angal.lab.codem"),MessageBundle.getMessage("angal.lab.resultm") };
	private int[] plColumwidth = { 150, 200, 50, 200 };

	private DefaultTableModel admModel;
	private DefaultTableModel labModel;
	TableSorter sorter;
	TableSorter sorterLab;
	
	//Alex: added sorters, for Java6 only
//	private TableRowSorter<AdmissionBrowserModel> adm_sorter;
//	private TableRowSorter<LabBrowserModel> lab_sorter;

	private JTable admTable;
	private JTable labTable;

	private JScrollPane scrollPane;
	private JScrollPane scrollPaneLab;
	
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
		scrollPane.setPreferredSize(new Dimension(500,200));
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
		
		labModel = new LabBrowserModel();
		sorterLab = new TableSorter(labModel);
		labTable = new JTable(sorterLab);
		sorterLab.sortByColumn(0, false);
		
		for (int i=0;i<plColums.length; i++){
			labTable.getColumnModel().getColumn(i).setPreferredWidth(plColumwidth[i]);
		}
				
		scrollPaneLab = new JScrollPane(labTable);
		scrollPaneLab.setPreferredSize(new Dimension(500,200));
		tablesPanel.add(scrollPaneLab, BorderLayout.SOUTH);		
		
		ListSelectionModel listSelectionModel = admTable.getSelectionModel();
		listSelectionModel.addListSelectionListener(new ListSelectionListener() {
//			private Object Object;

			public void valueChanged(ListSelectionEvent e) {
				// Check that mouse has been released.
				if (!e.getValueIsAdjusting()) {
					GregorianCalendar startDate = null;
					GregorianCalendar endDate = null;
					int selectedRow = admTable.getSelectedRow();
					Object selectedObject = sorter.getValueAt(selectedRow, -1);
					Object selectedObject2;
										
					//String Selection = (String) admTable.getValueAt(selectedRow, 1);
					//selectedRow = admTable.convertRowIndexToModel(selectedRow); for Java6 only
					
					//if (Selection.compareTo("OPD") != 0) {
					if (selectedObject instanceof Admission) {
						
						Admission ad = (Admission) selectedObject;
						//Admission admission = (Admission) (((AdmissionBrowserModel) admModel)
							//	.getValueAt(selectedRow, -1));
						startDate = ad.getAdmDate();
						endDate = ad.getDisDate();
										
						
					} else {
						
						Opd opd2 = null;
						Admission ad2 = null;
						if (selectedRow > 0) {
							selectedObject2 = sorter.getValueAt(selectedRow-1, -1);
							if (selectedObject2 instanceof Opd) opd2 = (Opd) selectedObject2;
							else ad2 = (Admission) selectedObject2;
						}
							
						Opd opd = (Opd) selectedObject;
						//Opd opd = (Opd) (((AdmissionBrowserModel) admModel)
							//	.getValueAt(selectedRow, -1));
						startDate = opd.getVisitDate();
						if (opd2 != null) endDate = opd2.getVisitDate();
						if (ad2 != null) endDate = ad2.getAdmDate();
					}
					// Clear past selection, if any.
					labTable.clearSelection();
					for (int i = 0; i < labList.size(); i++) {
						//Laboratory laboratory = labList.get(i);
						Laboratory laboratory = (Laboratory) sorterLab.getValueAt(i, -1);
						Date examDate = laboratory.getExamDate().getTime();
						
						// Check that the exam date is included between admission date and discharge date.
						// If the patient has not been discharged yet (and then discharge date doesn't exist)
						// check only that the exam date is the same or after the admission date.
						// On true condition select the corresponding table row.
						if (!examDate.before(startDate.getTime()) &&
								(null == endDate ? true : !examDate.after(endDate.getTime())))  {
							
							labTable.addRowSelectionInterval(i, i);
							
						}
					}
				}
			}
		});
		
		return tablesPanel;
	}

	private JPanel getButtonPanel() {
		JPanel buttonPanel; 
			buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
			buttonPanel.add(getLaunchReportButton(), null);
			buttonPanel.add(getCloseButton(), null);
		return buttonPanel;
	}

	
	private JButton launchReportButton = null;
	private JButton closeButton=null;

	private JButton getLaunchReportButton() {
		if (launchReportButton == null) {
			launchReportButton = new JButton();
			launchReportButton.setMnemonic(KeyEvent.VK_R);
			launchReportButton.setText(MessageBundle.getMessage("angal.admission.launchreport"));
			launchReportButton.addActionListener(new ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// GenericReportMY rpt3 = new GenericReportMY(new Integer(6), new Integer(2008), "hmis108_adm_by_diagnosis_in");
					new GenericReportPatient(patient.getCode(), GeneralData.PATIENTSHEET);
				}
			});
		}
		return launchReportButton;
	}
	
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
	
	class LabBrowserModel extends DefaultTableModel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -8245833681073162426L;

		public LabBrowserModel() {
			org.isf.lab.manager.LabManager lbm = new org.isf.lab.manager.LabManager();
			labList = lbm.getLaboratory(patient);
		}
		
		public int getRowCount() {
			if (labList == null)
				return 0;
			return labList.size();
		}

		public String getColumnName(int c) {
			return plColums[c];
		}

		public int getColumnCount() {
			return plColums.length;
		}	
		
		
		public Object getValueAt(int r, int c) {
			if (c == -1) {
				return labList.get(r);
			} else if (c == 0) {
				//System.out.println(labList.get(r).getExam().getExamtype().getDescription());
				
				Date examDate = labList.get(r).getExamDate().getTime();	
				DateFormat currentDateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALIAN);
				return currentDateFormat.format(examDate);
			} else if (c == 1) {
				return labList.get(r).getExam().getDescription();
			}else if (c == 2) {
				return labList.get(r).getCode();
			} else if (c == 3) {
				return labList.get(r).getResult();
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
