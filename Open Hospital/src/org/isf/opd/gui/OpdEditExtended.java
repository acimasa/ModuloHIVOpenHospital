package org.isf.opd.gui;

/*------------------------------------------
 * OpdEdit - add/edit an OPD registration
 * -----------------------------------------
 * modification history
 * 11/12/2005 - Vero, Rick  - first beta version 
 * 07/11/2006 - ross - renamed from Surgery 
 *                   - added visit date, disease 2, diseas3
 *                   - disease is not mandatory if re-attendance
 * 			         - version is now 1.0 
 * 28/05/2008 - ross - added referral to / referral from check boxes
 * 12/06/2008 - ross - added patient data
 * 					 - fixed error on checking "male"/"female" option: should check after translation
 * 					 - version is not a resource into the boundle, is locale to the form
 *                   - form rearranged in x,y coordinates 
 * 			         - version is now 1.1 
 * 26/08/2008 - teo  - added patient chooser 
 * 01/09/2008 - alex - added constructor for call from Admission
 * 					 - set Patient oriented OPD
 * 					 - history management for the patients
 * 					 - version now is 1.2
 * 01/01/2009 - Fabrizio - modified age fields back to Integer type
 * 13/02/2009 - Alex - added possibility to edit patient through EditButton
 * 					   added Edit.png icon
 * 					   fixed a bug on the first element in the comboBox
 * 13/02/2009 - Alex - added trash button for resetting searchfield
 * 03/13/2009 - Alex - lastOpdVisit appears at the bottom
 * 					   added control on duplicated diseases
 * 					   added re-attendance checkbox for a clear view
 * 					   new/re-attendance managed freely
 * 07/13/2009 - Alex - note field for the visit recall last visit note when start OPD from
	  				   Admission and added Note even in Last OPD Visit
	  				   Extended patient search to patient code
 *------------------------------------------*/


import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventListener;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

import org.isf.disease.manager.DiseaseBrowserManager;
import org.isf.disease.model.Disease;
import org.isf.distype.manager.DiseaseTypeBrowserManager;
import org.isf.distype.model.DiseaseType;
import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.opd.manager.OpdBrowserManager;
import org.isf.opd.model.Opd;
import org.isf.patient.gui.PatientInsert;
import org.isf.patient.gui.PatientInsertExtended;
import org.isf.patient.manager.PatientBrowserManager;
import org.isf.patient.model.Patient;
import org.isf.utils.jobjects.VoLimitedTextField;
import org.isf.utils.time.RememberDates;

import com.toedter.calendar.JDateChooser;

public class OpdEditExtended extends JDialog implements PatientInsertExtended.PatientListener, PatientInsert.PatientListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//@Override
	public void patientInserted(AWTEvent e) {
		//owner.dispose();
		//new OpdBrowser((Patient)e.getSource());
		opdPatient = (Patient) e.getSource();
		setPatient(opdPatient);
		jComboPatResult.addItem(opdPatient);
		jComboPatResult.setSelectedItem(opdPatient);
		jPatientEditButton.setEnabled(true);
	}

	//@Override
	public void patientUpdated(AWTEvent e) {
		setPatient(opdPatient);
	}

	private EventListenerList surgeryListeners = new EventListenerList();
	
	public interface SurgeryListener extends EventListener {
		public void surgeryUpdated(AWTEvent e);
		public void surgeryInserted(AWTEvent e);
	}
	
	public void addSurgeryListener(SurgeryListener l) {
		surgeryListeners.add(SurgeryListener.class, l);
	}
	
	public void removeSurgeryListener(SurgeryListener listener) {
		surgeryListeners.remove(SurgeryListener.class, listener);
	}
	
	private void fireSurgeryInserted() {
		AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};
		
		EventListener[] listeners = surgeryListeners.getListeners(SurgeryListener.class);
		for (int i = 0; i < listeners.length; i++)
			((SurgeryListener)listeners[i]).surgeryInserted(event);
	}
	private void fireSurgeryUpdated() {
		AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};
		
		EventListener[] listeners = surgeryListeners.getListeners(SurgeryListener.class);
		for (int i = 0; i < listeners.length; i++)
			((SurgeryListener)listeners[i]).surgeryUpdated(event);
	}
	
	private static final String VERSION="1.3"; 

	private static final Integer panelWidth=800; 
	private static final Integer labelWidth=140; 
	private static final Integer fieldWidth=200; 
	private static final Integer panelHeight=600; 
	private static final Integer fieldHeight=20; 
	private static final Integer labelLeft=30; 
	private static final Integer fieldLeft=180; 
	private static final String LastOPDLabel = "<html><i>"+MessageBundle.getMessage("angal.opd.lastopdvisitm")+"</i></html>:";
	private static final String LastNoteLabel = "<html><i>"+MessageBundle.getMessage("angal.opd.lastopdnote")+"</i></html>:";
	
	private JPanel jPanelMain = null;
	private JPanel jPanelData = null;
	private JPanel jPanelButtons = null;
	private JPanel jNewPatientPanel= null;
	private JLabel jLabelDate = null;
	private JPanel jPanelDate= null;
	private JPanel jDiseasePanel1 = null;
	private JPanel jDiseasePanel2 = null;
	private JPanel jDiseasePanel3 = null;
	private JLabel jLabelDiseaseType1 = null;
	private JLabel jLabelDisease1 = null;
	private JLabel jLabelDis2 = null;
	private JLabel jLabelDis3 = null;
	

	private JComboBox diseaseTypeBox = null;
	private JComboBox diseaseBox1 = null;
	private JComboBox diseaseBox2 = null;
	private JComboBox diseaseBox3 = null;
	private JLabel jLabelAge = null;
	private JLabel jLabelSex = null;
	private GregorianCalendar dateIn = null;
	private DateFormat currentDateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALIAN);
	private JDateChooser OpdDateFieldCal = null; 
	private JButton okButton = null;
	private JButton cancelButton = null;
	private JCheckBox rePatientCheckBox = null;
	private JCheckBox newPatientCheckBox = null;
	private JCheckBox referralToCheckBox = null;
	private JCheckBox referralFromCheckBox = null;
	private JPanel jPanelSex = null;
	private ButtonGroup group=null;

	private JLabel jLabelfirstName = null;
	private JLabel jLabelsecondName = null;
	private JLabel jLabeladdress = null;
	private JLabel jLabelcity = null;
	private JLabel jLabelnextKin = null;

	private JPanel jPanelPatient = null;

	private VoLimitedTextField jFieldFirstName= null;
	private VoLimitedTextField jFieldSecondName= null;
	private VoLimitedTextField jFieldAddress= null;
	private VoLimitedTextField jFieldCity= null;
	private VoLimitedTextField jFieldNextKin= null;
	private VoLimitedTextField jFieldAge = null;

	private Opd opd;
	private boolean insert;
	private DiseaseType allType= new DiseaseType(MessageBundle.getMessage("angal.opd.alltype"),MessageBundle.getMessage("angal.opd.alltype"));

	//ADDED : teo
	private VoLimitedTextField jTextPatientSrc;
	private JComboBox jComboPatResult;
	private JPanel jSearchPanel = null;
	private JLabel jSearchLabel = null;
	private JRadioButton radiof;
	private JRadioButton radiom;
	private String s;
	//ADDED : alex
	private JButton jPatientEditButton = null;
	private JButton jSearchButton = null;
	private JPanel jLastOpdPanel = null;
	private JPanel jLastNotePanel = null;
	private JLabel jLabelLastOpdVisit = null;
	private JLabel jFieldLastOpdVisit = null;
	private JLabel jLabelLastOpdNote = null;
	private JLabel jFieldLastOpdNote = null;
	
	private Patient opdPatient = null;
	private JPanel jNotePanel = null;
	private JScrollPane jNoteScrollPane = null;
	private JTextArea jNoteTextArea = null;
	private JPanel jPatientNotePanel = null;
	private JScrollPane jPatientScrollNote = null;
	private JTextArea jPatientNote = null;
	private JPanel jOpdNumberPanel = null;
	private JTextField jOpdNumField = null;
	private JLabel jOpdNumLabel = null;
	
	/*
	 * Managers and Arrays
	 */
	private DiseaseTypeBrowserManager typeManager = new DiseaseTypeBrowserManager();
	private DiseaseBrowserManager manager = new DiseaseBrowserManager();
	private ArrayList<DiseaseType> types = typeManager.getDiseaseType();
	private ArrayList<Disease> diseasesOPD = manager.getDiseaseOpd();
	private ArrayList<Disease> diseasesAll = manager.getDiseaseAll();
	private OpdBrowserManager opdManager = new OpdBrowserManager();
	private ArrayList<Opd> opdArray = new ArrayList<Opd>();
	private PatientBrowserManager patBrowser = new PatientBrowserManager();
	private ArrayList<Patient> pat = new ArrayList<Patient>();

	private Disease lastOPDDisease1;
	
	/**
	 * This method initializes 
	 * 
	 */
	public OpdEditExtended(JFrame owner, Opd old, boolean inserting) {
		super(owner, true);
		opd=old;
		insert=inserting;
		if(!insert) {
			if (opd.getpatientCode() != 0) { 
				PatientBrowserManager patBrowser = new PatientBrowserManager();
				opdPatient = patBrowser.getPatientAll(opd.getpatientCode());
			} else { //old OPD has no PAT_ID => Create Patient from OPD
				opdPatient = new Patient(opd);
				opdPatient.setCode(0);
			}
		}
		initialize();
	}
	
	public OpdEditExtended(JFrame owner, Opd opd, Patient patient, boolean inserting) {
		super(owner, true);
		this.opd = opd;
		opdPatient = patient;
		insert=inserting;
		initialize();
	}
	
	private void setPatient(Patient p) {
			jFieldAge.setText(String.valueOf(p.getAge()));
			jFieldFirstName.setText(p.getFirstName());
			jFieldAddress.setText(p.getAddress());
			jFieldCity.setText(p.getCity());
			jFieldSecondName.setText(p.getSecondName());
			jFieldNextKin.setText(p.getNextKin());
			jPatientNote.setText(opdPatient.getNote());
			setMyBorder(jPanelPatient, MessageBundle.getMessage("angal.opd.patient") + " (code: " + opdPatient.getCode() + ")");
			
			if(p.getSex() == 'M') {
				//Alex: SET SELECTED INSTEAD OF DOCLICK(), no listeners
				radiom.setSelected(true);				
			} else if(p.getSex() == 'F') {
				//Alex: SET SELECTED INSTEAD OF DOCLICK(), no listeners
				radiof.setSelected(true);			
			}
			
			if (insert) getLastOpd(p.getCode());
	}
	
	private void resetPatient() {
		jFieldAge.setText("");
		jFieldFirstName.setText("");
		jFieldAddress.setText("");
		jFieldCity.setText("");
		jFieldSecondName.setText("");
		jFieldNextKin.setText("");
		jPatientNote.setText("");
		setMyBorder(jPanelPatient, MessageBundle.getMessage("angal.opd.patient"));
		radiom.setSelected(true);
		opdPatient=null;
	}
	
	//Alex: Resetting history from the last OPD visit for the patient
	private boolean getLastOpd(int code)
	{
		Opd lastOpd = opdManager.getLastOpd(code);
		
		if (lastOpd == null) {
			newPatientCheckBox.setSelected(true);
			rePatientCheckBox.setSelected(false);
			jLabelLastOpdVisit.setText("");
			jFieldLastOpdVisit.setText("");
			jLabelLastOpdNote.setText("");
			jFieldLastOpdNote.setText("");
			jNoteTextArea.setText("");
			
			return false;
		}
		
		lastOPDDisease1 = null;
		Disease lastOPDDisease2 = null;
		Disease lastOPDDisease3 = null;
		
		for (Disease disease : diseasesOPD) {
			
			if (lastOpd.getDisease() != null && disease.getCode().compareTo(lastOpd.getDisease()) == 0) {
					lastOPDDisease1 = disease;
			}
			if (lastOpd.getDisease2() != null && disease.getCode().compareTo(lastOpd.getDisease2()) == 0) {
				lastOPDDisease2 = disease;
			}
			if (lastOpd.getDisease3() != null && disease.getCode().compareTo(lastOpd.getDisease3()) == 0) {
				lastOPDDisease3 = disease;
			}
		}
		
		StringBuilder lastOPDDisease = new StringBuilder();
		lastOPDDisease.append(MessageBundle.getMessage("angal.opd.on")).append(" ").append(currentDateFormat.format(lastOpd.getVisitDate().getTime())).append(" - ");
		if (lastOPDDisease1 != null) {
			setAttendance();
			lastOPDDisease.append(lastOPDDisease1.getDescription());
		} 
		if (lastOPDDisease2 != null) lastOPDDisease.append(", ").append(lastOPDDisease2.getDescription());
		if (lastOPDDisease3 != null) lastOPDDisease.append(", ").append(lastOPDDisease3.getDescription());
		jLabelLastOpdVisit.setText(LastOPDLabel);
		jFieldLastOpdVisit.setText(lastOPDDisease.toString());
		jLabelLastOpdNote.setText(LastNoteLabel);
		String note = lastOpd.getNote();
		jFieldLastOpdNote.setText(note.equals("") ? MessageBundle.getMessage("angal.opd.nonote") : note);
		jNoteTextArea.setText(lastOpd.getNote());
		
		return true;		
	}
	
	private void setAttendance() {
		if (!insert) return;
		Object selectedObject = diseaseBox1.getSelectedItem();
		if(selectedObject instanceof Disease) {
			Disease disease = (Disease) selectedObject;
			if (disease.getCode().equals(lastOPDDisease1.getCode())) {
				rePatientCheckBox.setSelected(true);
				newPatientCheckBox.setSelected(false);
			} else {
				rePatientCheckBox.setSelected(false);
				newPatientCheckBox.setSelected(true);
			}
		}
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setBounds(100, 50, panelWidth, panelHeight);
		this.setContentPane(getMainPanel());
		this.setResizable(false);

		if (insert) {
			this.setTitle(MessageBundle.getMessage("angal.opd.newopdregistration")+"("+VERSION+")");
		} else {
			this.setTitle(MessageBundle.getMessage("angal.opd.editopdregistration")+"("+VERSION+")");
		}
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//this.setVisible(true);
		if (insert) {
			jTextPatientSrc.requestFocusInWindow();
		} else {
			jNoteTextArea.requestFocusInWindow();
		}
		this.addWindowListener(new WindowAdapter(){
			
			public void windowClosing(WindowEvent e) {
				//to free memory
				pat.clear();
				opdArray.clear();
				diseasesAll.clear();
				diseasesOPD.clear();
				types.clear();
				jComboPatResult.removeAllItems();
				diseaseTypeBox.removeAllItems();
				diseaseBox1.removeAllItems();
				diseaseBox2.removeAllItems();
				diseaseBox3.removeAllItems();
				dispose();
			}			
		});
	}

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getMainPanel() {
		if (jPanelMain == null) {
			jPanelMain = new JPanel();
			jPanelMain.setLayout(null);
			jPanelMain.setBounds(0, 0, panelWidth, panelHeight);
			jPanelMain.add(getDataPanel(), null);
			jPanelMain.add(getJButtonPanel(), null);
		}
		return jPanelMain;
	}

	
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getDataPanel() {
		if (jPanelData == null) {
			jPanelData = new JPanel();
			jPanelData.setLayout(null);
			jPanelData.setBounds(0, 0, 800, 500);

			jPanelData.add(getJNewPatientPanel(), null);
			jPanelData.add(getJDatePanel(), null);
			jPanelData.add(getJOpdNumberPanel());
			jPanelData.add(getJDiseasePanel1(), null);
			jPanelData.add(getJDiseasePanel2(), null);
			jPanelData.add(getJDiseasePanel3(), null);
			jPanelData.add(getLastOpdPanel(), null);
			jPanelData.add(getLastNotePanel(), null);
			jPanelData.add(getJSearchPanel(), null);
			jPanelMain.add(getJNotePanel(), null);
			jPanelData.add(getjPanelPatient(), null);
			
		}
		return jPanelData;
	}
	
	

	private JPanel getLastOpdPanel() {
		if (jLastOpdPanel == null){
			jLastOpdPanel = new JPanel();
			jLastOpdPanel.setLayout(null);
			jLastOpdPanel.setBounds(0,240,panelWidth,fieldHeight);
			
			jLabelLastOpdVisit = new JLabel();
			jLabelLastOpdVisit.setText("");
			jLabelLastOpdVisit.setForeground(Color.RED);
			jLabelLastOpdVisit.setBounds(labelLeft,0,labelWidth,fieldHeight);

			jFieldLastOpdVisit = new JLabel();
			jFieldLastOpdVisit.setBounds(fieldLeft,0,400,fieldHeight);
			jFieldLastOpdVisit.setText("");
			jFieldLastOpdVisit.setFocusable(false);

			jLastOpdPanel.add(jLabelLastOpdVisit);
			jLastOpdPanel.add(jFieldLastOpdVisit);
		}
		return jLastOpdPanel;
	}
	
	private JPanel getLastNotePanel() {
		if (jLastNotePanel == null){
			jLastNotePanel = new JPanel();
			jLastNotePanel.setLayout(null);
			jLastNotePanel.setBounds(0,260,panelWidth,fieldHeight);
			
			jLabelLastOpdNote = new JLabel();
			jLabelLastOpdNote.setText("");
			jLabelLastOpdNote.setForeground(Color.RED);
			jLabelLastOpdNote.setBounds(labelLeft,0,labelWidth,fieldHeight);

			jFieldLastOpdNote = new JLabel();
			jFieldLastOpdNote.setBounds(fieldLeft,0,400,fieldHeight);
			jFieldLastOpdNote.setText("");
			jFieldLastOpdNote.setFocusable(false);

			jLastNotePanel.add(jLabelLastOpdNote);
			jLastNotePanel.add(jFieldLastOpdNote);
		}
		return jLastNotePanel;
	}
	
	private JPanel getJNewPatientPanel() {
		String referralTo="";
		String referralFrom="";
		if (jNewPatientPanel == null){
			jNewPatientPanel = new JPanel();
			jNewPatientPanel.setBounds(0, 0, panelWidth, 30);
			rePatientCheckBox = new JCheckBox(MessageBundle.getMessage("angal.opd.reattendance"));
			newPatientCheckBox = new JCheckBox(MessageBundle.getMessage("angal.opd.newattendance"));
			newPatientCheckBox.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (newPatientCheckBox.isSelected()) {
						newPatientCheckBox.setSelected(true);
						rePatientCheckBox.setSelected(false);
					} else {
						newPatientCheckBox.setSelected(false);
						rePatientCheckBox.setSelected(true);
					}
				}
			});
			rePatientCheckBox.addActionListener(new ActionListener() {

				//@Override
				public void actionPerformed(ActionEvent e) {
					if (rePatientCheckBox.isSelected()) {
						rePatientCheckBox.setSelected(true);
						newPatientCheckBox.setSelected(false);
					} else {
						newPatientCheckBox.setSelected(true);
						rePatientCheckBox.setSelected(false);
					}
				}
			});
			newPatientCheckBox.setBounds(0, 0, 60, 90);
			jNewPatientPanel.add(rePatientCheckBox);
			jNewPatientPanel.add(newPatientCheckBox);
			if(!insert){
				if (opd.getNewPatient().equals("N"))
					newPatientCheckBox.setSelected(true);
				else
					rePatientCheckBox.setSelected(true);
			}
			referralFromCheckBox = new JCheckBox(MessageBundle.getMessage("angal.opd.referral.from"));
			referralFromCheckBox.setBounds(100, 0, 60, 90);
			jNewPatientPanel.add(referralFromCheckBox);
			if(!insert){
				referralFrom = opd.getReferralFrom();
				if (referralFrom == null) referralFrom="";
				if (referralFrom.equals("R"))referralFromCheckBox.setSelected(true);
			}
			referralToCheckBox = new JCheckBox(MessageBundle.getMessage("angal.opd.referral.to"));
			referralToCheckBox.setBounds(200, 0, 60, 90);
			jNewPatientPanel.add(referralToCheckBox);
			if(!insert){
				referralTo = opd.getReferralTo();
				if (referralTo == null) referralTo="";
				if (referralTo.equals("R"))referralToCheckBox.setSelected(true);
			}
		}
		return jNewPatientPanel;
	}
	

	private JPanel getJDatePanel() {
		if (jPanelDate == null){
			jPanelDate = new JPanel();
			jPanelDate.setLayout(null);
			jPanelDate.setBounds(0,40,300,fieldHeight+10);

			jLabelDate= new JLabel();
			jLabelDate.setText(MessageBundle.getMessage("angal.opd.attendancedate"));
			jLabelDate.setBounds(labelLeft, 5, labelWidth, fieldHeight);
			
			String d = "";

			java.util.Date myDate = null;
			if (insert) {
				if (RememberDates.getLastOpdVisitDate()==null) {
					dateIn = new GregorianCalendar();
				}				
				else {
					dateIn=RememberDates.getLastOpdVisitDateGregorian();
				}
			}
			 else {
				dateIn  = opd.getVisitDate();
			}
			if (dateIn==null) {
				d="";
			}
			else {
				myDate = dateIn.getTime();
				d = currentDateFormat.format(myDate);
			}
			try {
				OpdDateFieldCal = new JDateChooser(currentDateFormat.parse(d), "dd/MM/yy");
				OpdDateFieldCal.setLocale(new Locale(GeneralData.LANGUAGE));
				OpdDateFieldCal.setDateFormatString("dd/MM/yy");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			OpdDateFieldCal.setBounds(fieldLeft,5,100,fieldHeight);

			jPanelDate.add(jLabelDate);
			jPanelDate.add(OpdDateFieldCal);
		}
		return jPanelDate;
	}

	private JPanel getJDiseasePanel1() {
		if (jDiseasePanel1 == null){
			jDiseasePanel1 = new JPanel();
			jDiseasePanel1.setLayout(null);
			jDiseasePanel1.setBounds(0,120,panelWidth,(fieldHeight+10)*2);
			jLabelDiseaseType1 = new JLabel();
			jLabelDiseaseType1.setText(MessageBundle.getMessage("angal.opd.diseasetype"));
			jLabelDiseaseType1.setBounds(labelLeft,0,labelWidth,fieldHeight);

			getDiseaseTypeBox();
			diseaseTypeBox.setBounds(fieldLeft,0,400,fieldHeight);

			jLabelDisease1 = new JLabel();
			jLabelDisease1.setText(MessageBundle.getMessage("angal.opd.diagnosis"));
			jLabelDisease1.setBounds(labelLeft,fieldHeight+10,labelWidth,fieldHeight);

			getDiseaseBox();
			diseaseBox1.setBounds(fieldLeft,fieldHeight+10,400,fieldHeight);
			
			jDiseasePanel1.add(jLabelDiseaseType1);
			jDiseasePanel1.add(diseaseTypeBox);
			jDiseasePanel1.add(jLabelDisease1);
			jDiseasePanel1.add(diseaseBox1);
		}
		return jDiseasePanel1;
	}
	
	private JPanel getJOpdNumberPanel() {
		if (jOpdNumberPanel == null) {
			
			jOpdNumberPanel = new JPanel();
			jOpdNumberPanel.setLayout(null);
			jOpdNumberPanel.setBounds(330, 40, labelWidth + 100 +10,fieldHeight+10);
			
			jOpdNumLabel = new JLabel();
			jOpdNumLabel.setText(MessageBundle.getMessage("angal.opd.opdnumber"));
			jOpdNumLabel.setBounds(80, 5, 60, fieldHeight);
			
			jOpdNumField = new JTextField();
			jOpdNumField.setBounds(labelWidth+10,5,100,fieldHeight);
			jOpdNumField.setEditable(false);
			jOpdNumField.setFocusable(false);
			jOpdNumField.setText(getOpdNum());
			
			jOpdNumberPanel.add(jOpdNumLabel);
			jOpdNumberPanel.add(jOpdNumField);
			
		
		}
		return jOpdNumberPanel;
	}

	private String getOpdNum() {
		int OpdNum;
		if (!insert) return ""+opd.getYear();
		GregorianCalendar date = new GregorianCalendar();
		opd.setYear(opdManager.getProgYear(date.get(Calendar.YEAR))+1);
		OpdNum = opd.getYear();
		return ""+OpdNum;
	}
	
	private JPanel getJNotePanel() {
		if ((jNoteScrollPane == null) && (jNotePanel == null)) {
			
			jNotePanel = new JPanel();
			jNotePanel.setLayout(null);
			jNotePanel.setBounds(600, 60, 180, 190);
			
			jNoteScrollPane = new JScrollPane(getJTextArea());
			jNoteScrollPane.setVerticalScrollBar(new JScrollBar());
			jNoteScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			jNoteScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jNoteScrollPane.setBounds(10,30,160,140);
			jNoteScrollPane.validate();
			jNotePanel = setMyBorder(jNotePanel, MessageBundle
					.getMessage("angal.opd.noteandsymptom"));
			jNotePanel.add(jNoteScrollPane, null);
		}
		return jNotePanel;
	}
	
	private JTextArea getJTextArea() {
		if (jNoteTextArea == null) {
			jNoteTextArea = new JTextArea(40, 15);
			jNoteTextArea.setAutoscrolls(true);
			if (!insert) {
				jNoteTextArea.setText(opd.getNote());
			}
			jNoteTextArea.setWrapStyleWord(true);
			jNoteTextArea.setLineWrap(true);
			jNoteTextArea.setPreferredSize(new Dimension(jNoteTextArea
					.getSize()));
		}
		return jNoteTextArea;
	}

	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	public JComboBox getDiseaseTypeBox() {
		if (diseaseTypeBox == null) {
			diseaseTypeBox = new JComboBox();
			
			DiseaseType elem2=null;
			diseaseTypeBox.setMaximumSize(new Dimension(400,50));
			diseaseTypeBox.addItem(allType);
			for (DiseaseType elem : types) {
				if(!insert  && opd.getDiseaseType() != null){
					if(opd.getDiseaseType().equals(elem.getCode())){
						elem2=elem;}
				}
				diseaseTypeBox.addItem(elem);
			}
			if (elem2!=null) { 
				diseaseTypeBox.setSelectedItem(elem2);
			}
			else {
				diseaseTypeBox.setSelectedIndex(0);
			}
			diseaseTypeBox.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					diseaseBox1.removeAllItems();
					getDiseaseBox();					
				}
			});
		}
		return diseaseTypeBox;
	}
	
	/**
	 * This method initializes jComboBox1	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	public JComboBox getDiseaseBox() {
		if (diseaseBox1 == null) {
			diseaseBox1 = new JComboBox();
			diseaseBox1.setMaximumSize(new Dimension(400, 50));
			
			diseaseBox1.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					setAttendance();
				}
			});
		};
		Disease elem2 = null;
		diseaseBox1.addItem("");
		
		for (Disease elem : diseasesOPD) {
			if (((DiseaseType)diseaseTypeBox.getSelectedItem()).equals(allType))
				diseaseBox1.addItem(elem);
			else if (elem.getType().equals((DiseaseType)diseaseTypeBox.getSelectedItem()))
				diseaseBox1.addItem(elem);
			if(!insert && opd.getDisease()!=null){
				if(opd.getDisease().equals(elem.getCode())){
					elem2 = elem;}
				
			}
		}
		if (!insert) {
			if (elem2 != null) {
				diseaseBox1.setSelectedItem(elem2);
			} else { //try in the canceled diseases
				if (opd.getDisease()!=null) {
					for (Disease elem : diseasesAll) {
						if (opd.getDisease().compareTo(elem.getCode()) == 0) {
							JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.opd.disease1mayhavebeencanceled"));
							diseaseBox1.addItem(elem);
							diseaseBox1.setSelectedItem(elem);
						}
					}
				}
			}
		}
		
		
		return diseaseBox1;
	}
	
	public JPanel getJDiseasePanel2() {
		if (jDiseasePanel2 == null){
			jDiseasePanel2 = new JPanel();
			jDiseasePanel2.setLayout(null);
			jDiseasePanel2.setBounds(0,180,panelWidth,fieldHeight+10 );
			jLabelDis2 = new JLabel();
			jLabelDis2.setText(MessageBundle.getMessage("angal.opd.diagnosisnfulllist"));
			jLabelDis2.setBounds(labelLeft,0,labelWidth,fieldHeight);

			getDiseaseBox2();
			diseaseBox2.setBounds(fieldLeft,0,400,fieldHeight);
			
			jDiseasePanel2.add(jLabelDis2);
			jDiseasePanel2.add(diseaseBox2);
			
		}
		return jDiseasePanel2;
	}
	
	public JComboBox getDiseaseBox2() {
		if (diseaseBox2 == null) {
			diseaseBox2 = new JComboBox();
			diseaseBox2.setMaximumSize(new Dimension(400, 50));
		};
		Disease elem2=null;
		diseaseBox2.addItem("");

		for (Disease elem : diseasesOPD) {
			diseaseBox2.addItem(elem);
			if(!insert && opd.getDisease2()!=null){
				if(opd.getDisease2().equals(elem.getCode())){
					elem2 = elem;}
			} 
		}
		if (elem2!= null) {
			diseaseBox2.setSelectedItem(elem2);
		} else { //try in the canceled diseases
			if (opd.getDisease2()!=null) {
				for (Disease elem : diseasesAll) {
					
					if (opd.getDisease2().compareTo(elem.getCode()) == 0) {
						JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.opd.disease2mayhavebeencanceled"));
						diseaseBox2.addItem(elem);
						diseaseBox2.setSelectedItem(elem);
					}
				}
			}
		}
		return diseaseBox2;
	}

	
	public JPanel getJDiseasePanel3() {
		if (jDiseasePanel3 == null){
			jDiseasePanel3 = new JPanel();
			jDiseasePanel3.setLayout(null);
			jDiseasePanel3.setBounds(0,210,panelWidth,fieldHeight+10 );
			jLabelDis3 = new JLabel();
			jLabelDis3.setText(MessageBundle.getMessage("angal.opd.diagnosisnfulllist3"));
			jLabelDis3.setBounds(labelLeft,0,labelWidth,fieldHeight);

			getDiseaseBox3();
			diseaseBox3.setBounds(fieldLeft,0,400,fieldHeight);

			jDiseasePanel3.add(jLabelDis3);
			jDiseasePanel3.add(diseaseBox3);
		}
		return jDiseasePanel3;
	}
	
	public JPanel getJSearchPanel()
	{
		if (jSearchPanel == null){
			jSearchPanel = new JPanel();
			jSearchPanel.setLayout(null);
			jSearchPanel.setBounds(0,90,panelWidth,fieldHeight+10 );
			jSearchLabel = new JLabel();
			jSearchLabel.setText(MessageBundle.getMessage("angal.opd.search"));
			jSearchLabel.setBounds(labelLeft,0,labelWidth,fieldHeight);
			jTextPatientSrc = new VoLimitedTextField(16,20);
			jTextPatientSrc.setBounds(labelLeft+labelWidth+10,0,fieldWidth -25,fieldHeight);
			jTextPatientSrc.addKeyListener(new KeyListener() {

				public void keyPressed(KeyEvent e) {
					int key = e.getKeyCode();
				     if (key == KeyEvent.VK_ENTER) {
				    	 jSearchButton.doClick();
				     }
				}

				public void keyReleased(KeyEvent e) {
				}

				public void keyTyped(KeyEvent e) {
				}
			});
		
			jSearchPanel.add(jSearchLabel);
			jSearchPanel.add(jTextPatientSrc);
			jSearchPanel.add(getJSearchButton());
			jSearchPanel.add(getJPatientEditButton());
			jSearchPanel.add(getSearchBox(s));
			
		}
		return jSearchPanel;
	}
	
	private JButton getJSearchButton() {
		if (jSearchButton == null) {
			jSearchButton = new JButton();
			jSearchButton.setIcon(new ImageIcon("rsc/icons/zoom_r_button.png"));
			jSearchButton.setBorderPainted(false);
			jSearchButton.setPreferredSize(new Dimension(20, 20));
			jSearchButton.setBounds(labelLeft+labelWidth+fieldWidth-15,0,20,fieldHeight);
			jSearchButton.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					jComboPatResult.removeAllItems();
					pat = patBrowser.getPatientWithHeightAndWeight(jTextPatientSrc.getText());
					getSearchBox(jTextPatientSrc.getText());
				}
			});
		}
		return jSearchButton;
	}

	private JComboBox getSearchBox(String s) {
		
		String key = s;
		String[] s1;
		
		if(jComboPatResult == null)
		{
			jComboPatResult = new JComboBox();
			jComboPatResult.setMaximumSize(new Dimension(400,50));
			jComboPatResult.setBounds(labelLeft+labelWidth+fieldWidth+10,0,180,fieldHeight);
			//ADDED for Updating
			if (opdPatient != null) {
				
				jComboPatResult.addItem(opdPatient);
				jComboPatResult.setEnabled(false);
				jTextPatientSrc.setEnabled(false);
				return jComboPatResult;
			}
						
			jComboPatResult.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
									
					if (jComboPatResult.getSelectedItem() != null) {
						if (jComboPatResult.getSelectedItem().toString().compareTo(MessageBundle.getMessage("angal.opd.newpatient"))==0) {
							if (GeneralData.PATIENTEXTENDED) {
								PatientInsertExtended newrecord = new PatientInsertExtended(OpdEditExtended.this, new Patient(), true);
								newrecord.addPatientListener(OpdEditExtended.this);
								newrecord.setVisible(true);
//								owner.dispose();
//								dispose();
							} else {
								PatientInsert newrecord = new PatientInsert(OpdEditExtended.this, new Patient(), true);
								newrecord.addPatientListener(OpdEditExtended.this);
								newrecord.setVisible(true);
//								owner.dispose();
//								dispose();
							}
							
						} else if (jComboPatResult.getSelectedItem().toString().compareTo(MessageBundle.getMessage("angal.opd.selectapatient"))==0) {
							jPatientEditButton.setEnabled(false);
							//resetOpd();
							//disableOpd();
							
						} else { //if (jComboPatResult.getSelectedIndex()>0) {
							opdPatient=(Patient)jComboPatResult.getSelectedItem();
							setPatient(opdPatient);
							getjPanelPatient();
							jPatientEditButton.setEnabled(true);
						}	
					}
				}
			});
		}
		
		if (key == null || key.compareTo("") == 0) {
			jComboPatResult.addItem(MessageBundle.getMessage("angal.opd.selectapatient"));
			jComboPatResult.addItem(MessageBundle.getMessage("angal.opd.newpatient"));
			jLabelLastOpdVisit.setText("");
			jFieldLastOpdVisit.setText("");
			jLabelLastOpdNote.setText("");
			jFieldLastOpdNote.setText("");
			if (jNoteTextArea != null)
					jNoteTextArea.setText("");
			if (jPanelPatient != null) resetPatient();
		}
				
		for (Patient elem : pat) {
			if(key != null)
			{
				s1 = key.split(" ");
				String name = elem.getSearchString();
				int a = 0;
				for (int i = 0; i < s1.length; i++) {
					if(name.contains(s1[i].toLowerCase()) == true) {
						a++;
					}
				}
				if (a == s1.length)	jComboPatResult.addItem(elem);
			} else 
				jComboPatResult.addItem(elem);
		}
		//ADDED: Workaround for no items
		if (jComboPatResult.getItemCount() == 0) {
			
				opdPatient=null;
				//resetOpd();
				//disableOpd();
				if (jPanelPatient != null) resetPatient();
				jPatientEditButton.setEnabled(true);
		}
		//ADDED: Workaround for one item only
		if (jComboPatResult.getItemCount() == 1) {
			
				opdPatient=(Patient)jComboPatResult.getSelectedItem();
				setPatient(opdPatient);
				getjPanelPatient();
				jPatientEditButton.setEnabled(true);
		}
		//ADDED: Workaround for first item
		if (jComboPatResult.getItemCount() > 0) {
			
			if (jComboPatResult.getItemAt(0) instanceof Patient) {
				opdPatient=(Patient)jComboPatResult.getItemAt(0);
				setPatient(opdPatient);
				getjPanelPatient();
				jPatientEditButton.setEnabled(true);
			}
		}
		jTextPatientSrc.requestFocus();
		return jComboPatResult;
	}
	
	//ADDED: Alex
	private JButton getJPatientEditButton() {
		
		if (jPatientEditButton == null) {
			jPatientEditButton = new JButton();
			jPatientEditButton.setIcon(new ImageIcon("rsc/icons/edit_button.png"));
			jPatientEditButton.setBorderPainted(false);
			jPatientEditButton.setPreferredSize(new Dimension(20, 20));
			jPatientEditButton.setBounds(labelLeft+labelWidth+fieldWidth+190,0,20,fieldHeight);
			jPatientEditButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (opdPatient != null) {
						if (GeneralData.PATIENTEXTENDED) {
							PatientInsertExtended editrecord = new PatientInsertExtended(OpdEditExtended.this, opdPatient, false);
							editrecord.addPatientListener(OpdEditExtended.this);
							editrecord.setVisible(true);
						} else {
							PatientInsert editrecord = new PatientInsert(OpdEditExtended.this, opdPatient, false);
							editrecord.addPatientListener(OpdEditExtended.this);
							editrecord.setVisible(true);
						}
					} 
				}
			});
			if (!insert) jPatientEditButton.setEnabled(false);
		}	
		
		return jPatientEditButton;
	}
	
	private JComboBox getDiseaseBox3() {
		if (diseaseBox3 == null) {
			diseaseBox3 = new JComboBox();
			diseaseBox3.setMaximumSize(new Dimension(400, 50));
		};
		Disease elem2=null;
		diseaseBox3.addItem("");

		for (Disease elem : diseasesOPD) {
			diseaseBox3.addItem(elem);
			if(!insert && opd.getDisease3()!=null){
				if(opd.getDisease3().equals(elem.getCode())){
					elem2 = elem;}
			}
		}
		if (elem2!= null) {
			diseaseBox3.setSelectedItem(elem2);
		} else { //try in the canceled diseases
			if (opd.getDisease3()!=null) {	
				for (Disease elem : diseasesAll) {
					if (opd.getDisease3().compareTo(elem.getCode()) == 0) {
						JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.opd.disease3mayhavebeencanceled"));
						diseaseBox3.addItem(elem);
						diseaseBox3.setSelectedItem(elem);
					}
				}
			}
		}
		return diseaseBox3;
	}
	
	//alex: metodo ridefinito, i settaggi avvegono tramite SetPatient()
	private JPanel getjPanelPatient() {
		if (jPanelPatient == null){
			jPanelPatient= new JPanel();
			jPanelPatient.setLayout(null);
			jPanelPatient.setBounds(30, 290, 720, 200);
			 
			jLabelfirstName = new JLabel();
			jLabelfirstName.setText(MessageBundle.getMessage("angal.opd.first.name") + "\t");
			jFieldFirstName= new VoLimitedTextField(50,20);
			jLabelsecondName = new JLabel();
			jLabelsecondName.setText(MessageBundle.getMessage("angal.opd.second.name") + "\t");
			jFieldSecondName= new VoLimitedTextField(50,20);
			jLabeladdress  = new JLabel();
			jLabeladdress.setText(MessageBundle.getMessage("angal.opd.address"));
			jFieldAddress= new VoLimitedTextField(50,20);
			jLabelcity = new JLabel();
			jLabelcity.setText(MessageBundle.getMessage("angal.opd.city"));
			jFieldCity= new VoLimitedTextField(50,20);
			jLabelnextKin = new JLabel();
			jLabelnextKin.setText(MessageBundle.getMessage("angal.opd.nextkin"));
			jFieldNextKin= new VoLimitedTextField(50,20);
			jLabelAge = new JLabel();
			jLabelAge.setText(MessageBundle.getMessage("angal.opd.age"));
			jFieldAge = new VoLimitedTextField(50,20);
			jLabelSex = new JLabel();
			jLabelSex.setText(MessageBundle.getMessage("angal.opd.sex"));
			jPanelSex = new JPanel();
			group=new ButtonGroup();
			radiom= new JRadioButton(MessageBundle.getMessage("angal.opd.male"));
			radiof= new JRadioButton(MessageBundle.getMessage("angal.opd.female"));
			
			group.add(radiom);
			group.add(radiof);
			jPanelSex.add(radiom);
			jPanelSex.add(radiof);
			
			jLabelfirstName.setBounds(labelLeft+30,20,labelWidth,fieldHeight);
			jFieldFirstName.setBounds(fieldLeft,20,fieldWidth,fieldHeight);
			jLabelsecondName.setBounds(labelLeft+30,20+fieldHeight+1,labelWidth,fieldHeight);
			jFieldSecondName.setBounds(fieldLeft,20+fieldHeight+1,fieldWidth,fieldHeight);
			jLabeladdress.setBounds(labelLeft+30,20+(fieldHeight+1)*2,labelWidth,fieldHeight);
			jFieldAddress.setBounds(fieldLeft,20+(fieldHeight+1)*2,fieldWidth,fieldHeight);
			jLabelcity.setBounds(labelLeft+30,20+(fieldHeight+1)*3,labelWidth,fieldHeight);
			jFieldCity.setBounds(fieldLeft,20+(fieldHeight+1)*3,fieldWidth,fieldHeight);
			jLabelnextKin.setBounds(labelLeft+30,20+(fieldHeight+1)*4,labelWidth,fieldHeight);
			jFieldNextKin.setBounds(fieldLeft,20+(fieldHeight+1)*4,fieldWidth,fieldHeight);
			jLabelAge.setBounds(labelLeft+30,20+(fieldHeight+1)*5,labelWidth,fieldHeight);
			jFieldAge.setBounds(fieldLeft,20+(fieldHeight+1)*5,fieldWidth,fieldHeight);
			jLabelSex.setBounds(labelLeft+30,20+(fieldHeight+1)*6,labelWidth,fieldHeight);
			jPanelSex.setBounds(fieldLeft,20+(fieldHeight+1)*6,fieldWidth,fieldHeight*2);
				
			jFieldAge.setEditable(false);
			jFieldFirstName.setEditable(false);
			jFieldSecondName.setEditable(false);
			jFieldAddress.setEditable(false);
			jFieldCity.setEditable(false);
			jFieldNextKin.setEditable(false);
			radiom.setEnabled(false);
			radiof.setEnabled(false);
			jFieldAge.setFocusable(false);
			jFieldFirstName.setFocusable(false);
			jFieldSecondName.setFocusable(false);
			jFieldAddress.setFocusable(false);
			jFieldCity.setFocusable(false);
			jFieldNextKin.setFocusable(false);
			radiom.setFocusable(false);
			radiof.setFocusable(false);
			
			jPanelPatient.add(jLabelfirstName);
			jPanelPatient.add(jFieldFirstName);
			jPanelPatient.add(jLabelsecondName);
			jPanelPatient.add(jFieldSecondName);
			jPanelPatient.add(jLabeladdress);
			jPanelPatient.add(jFieldAddress);
			jPanelPatient.add(jLabelcity );
			jPanelPatient.add(jFieldCity);
			jPanelPatient.add(jLabelnextKin);
			jPanelPatient.add(jFieldNextKin);
			jPanelPatient.add(jLabelAge);
			jPanelPatient.add(jFieldAge);
			jPanelPatient.add(jLabelSex);
			jPanelPatient.add(jPanelSex);
			jPanelPatient.add(getJPatientNote());

			setMyBorder(jPanelPatient, MessageBundle.getMessage("angal.opd.patient"));
			
			if(opdPatient != null) setPatient(opdPatient);
		}
		return jPanelPatient;
	}
	
	private JPanel getJPatientNote() {
		if ((jPatientNotePanel == null) && (jPatientScrollNote == null)) {
			
			jPatientNotePanel = new JPanel();
			jPatientNotePanel.setLayout(null);
			jPatientNotePanel.setBounds(fieldLeft + fieldWidth + 10, 20, 180, 160);
			
			jPatientScrollNote = new JScrollPane(getJPatientNoteArea());
			jPatientScrollNote.setVerticalScrollBar(new JScrollBar());
			jPatientScrollNote.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			jPatientScrollNote.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jPatientScrollNote.setBounds(0,0,180,160);
			jPatientScrollNote.setAutoscrolls(false);
			jPatientScrollNote.validate();
			jPatientNotePanel.add(jPatientScrollNote, null);
		}
		return jPatientNotePanel;
	}
	
	private JTextArea getJPatientNoteArea() {
		if (jPatientNote == null) {
			jPatientNote = new JTextArea(40, 15);
			if (!insert) {
				jPatientNote.setText(opdPatient.getNote());
			}
			jPatientNote.setLineWrap(true);
			jPatientNote.setPreferredSize(new Dimension(jPatientNote
					.getSize()));
			jPatientNote.setEditable(false);
			jPatientNote.setFocusable(false);
		}
		return jPatientNote;
	}

	/**
	 * This method initializes jPanelButtons	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJButtonPanel() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			jPanelButtons.setBounds(0, 500, panelWidth, 50);
			//jPanelButtons.setBackground(java.awt.Color.WHITE);
			getOkButton();
			getCancelButton();
			jPanelButtons.add(okButton, null);
			jPanelButtons.add(cancelButton, null);
		}
		return jPanelButtons;
	}
	
	/**
	 * This method initializes okButton	
	 * 	
	 * @return javax.swing.JButton	
	 */

	//alex: modified method to take data from Patient Object instead from jTextFields
	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton(MessageBundle.getMessage("angal.opd.ok"));
            okButton.setMnemonic(KeyEvent.VK_O);
			okButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					boolean result = false;
					GregorianCalendar gregDate = new GregorianCalendar();
					String newPatient="";
					String referralTo=null;
					String referralFrom=null;
					String disease=null;
					String disease2=null;
					String disease3=null;
					String diseaseType=null;
					String diseaseDesc="";
					String diseaseTypeDesc="";

					if (diseaseBox1.getSelectedIndex()==0) {
						JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.opd.pleaseselectadisease"));
						return;
					}
					if (opdPatient == null) {
						
						JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.opd.pleaseselectapatient"));
						return;
					}

					if (newPatientCheckBox.isSelected()){
						newPatient="N";
					}else{
						newPatient="R";
					}
					if (referralToCheckBox.isSelected()){
						referralTo="R";
					}else{
						referralTo="";
					}
					if (referralFromCheckBox.isSelected()){
						referralFrom="R";
					}else{
						referralFrom="";
					}
					//disease
					if (diseaseBox1.getSelectedIndex()>0) {
						disease=((Disease)diseaseBox1.getSelectedItem()).getCode();					
						diseaseDesc=((Disease)diseaseBox1.getSelectedItem()).getDescription();
						diseaseTypeDesc=((Disease)diseaseBox1.getSelectedItem()).getType().getDescription();
						diseaseType=(((Disease)diseaseBox1.getSelectedItem()).getType().getCode());
					}
					//disease2
					if (diseaseBox2.getSelectedIndex()>0) {
						disease2=((Disease)diseaseBox2.getSelectedItem()).getCode();
					}
					//disease3
					if (diseaseBox3.getSelectedIndex()>0) {
						disease3=((Disease)diseaseBox3.getSelectedItem()).getCode();					
					}
					//Check double diseases
					if (disease2 != null && disease == disease2) {
						JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.opd.duplicatediseasesnotallowed"));
						disease2 = null;
						return;
					}
					if (disease3 != null && disease == disease3) {
						JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.opd.duplicatediseasesnotallowed"));
						disease3 = null;
						return;
					}
					if (disease2 != null && disease3 != null && disease2 == disease3) {
						JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.opd.duplicatediseasesnotallowed"));
						disease3 = null;
						return;
					}
					String d = currentDateFormat.format(OpdDateFieldCal.getDate());
					if (d.equals("")) {
						JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.opd.pleaseinsertattendancedate"));
						return;
					}
					opd.setNote(jNoteTextArea.getText());
					opd.setpatientCode(opdPatient.getCode()); //ADDED : alex
					opd.setFullName(opdPatient.getName());
					opd.setNewPatient(newPatient);
					opd.setReferralFrom(referralFrom);
					opd.setReferralTo(referralTo);
					opd.setAge(opdPatient.getAge());
					opd.setSex(opdPatient.getSex());
					
					opd.setfirstName(opdPatient.getFirstName());
					opd.setsecondName(opdPatient.getSecondName());
					opd.setaddress(opdPatient.getAddress());
					opd.setcity(opdPatient.getCity());
					opd.setnextKin(opdPatient.getNextKin());
					
					opd.setDisease(disease);						
					opd.setDiseaseType(diseaseType);
					opd.setDiseaseDesc(diseaseDesc);
					opd.setDiseaseTypeDesc(diseaseTypeDesc);
					opd.setDisease2(disease2);
					opd.setDisease3(disease3);
					//alex: salva la vera data di visista scelta
					gregDate.setTime(OpdDateFieldCal.getDate());
					opd.setVisitDate(gregDate);
					if (insert){
						GregorianCalendar date =new GregorianCalendar();
						opd.setYear(opdManager.getProgYear(date.get(Calendar.YEAR))+1);
						//remember for later use
						RememberDates.setLastOpdVisitDate(gregDate);
						result = opdManager.newOpd(opd);
						if (result) {
							fireSurgeryInserted();
						}
						if (!result) JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.opd.thedatacouldnotbesaved"));
						else  dispose();
					}
					else {    //Update
						result = opdManager.updateOpd(opd);
						if (result) {
							fireSurgeryUpdated();
						};
						if (!result) JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.opd.thedatacouldnotbesaved"));
						else  dispose();
					};
				};
			}
			);	
		}
		return okButton;
	}
	
	/**
	 * This method initializes cancelButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton(MessageBundle.getMessage("angal.opd.cancel"));
            cancelButton.setMnemonic(KeyEvent.VK_C);
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//to free Memory
					pat.clear();
					opdArray.clear();
					diseasesAll.clear();
					diseasesOPD.clear();
					types.clear();
					jComboPatResult.removeAllItems();
					diseaseTypeBox.removeAllItems();
					diseaseBox1.removeAllItems();
					diseaseBox2.removeAllItems();
					diseaseBox3.removeAllItems();
					dispose();
				}
			});
		}
		return cancelButton;
	}
	
	/*
	 * set a specific border+title to a panel
	 */
	private JPanel setMyBorder(JPanel c, String title) {
		javax.swing.border.Border b2 = BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(title), BorderFactory
						.createEmptyBorder(0, 0, 0, 0));
		c.setBorder(b2);
		return c;
	}
}