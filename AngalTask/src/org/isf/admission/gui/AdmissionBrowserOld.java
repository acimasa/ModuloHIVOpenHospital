package org.isf.admission.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

import org.isf.admission.manager.AdmissionBrowserManager;
import org.isf.admission.model.Admission;
import org.isf.admission.model.AdmittedPatient;
import org.isf.admtype.model.AdmissionType;
import org.isf.disctype.model.DischargeType;
import org.isf.disease.manager.DiseaseBrowserManager;
import org.isf.disease.model.Disease;
import org.isf.dlvrrestype.manager.DeliveryResultTypeBrowserManager;
import org.isf.dlvrrestype.model.DeliveryResultType;
import org.isf.dlvrtype.manager.DeliveryTypeBrowserManager;
import org.isf.dlvrtype.model.DeliveryType;
import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.operation.manager.OperationBrowserManager;
import org.isf.operation.model.Operation;
import org.isf.patient.gui.PatientSummary;
import org.isf.patient.model.Patient;
import org.isf.pregtreattype.manager.PregnantTreatmentTypeBrowserManager;
import org.isf.pregtreattype.model.PregnantTreatmentType;
import org.isf.utils.jobjects.VoDateTextField;
import org.isf.utils.jobjects.VoLimitedTextField;
import org.isf.utils.time.RememberDates;
import org.isf.ward.manager.WardBrowserManager;
import org.isf.ward.model.Ward;

import com.toedter.calendar.JDateChooser;




/**
 * This class shows essential patient data and allows to create an admission
 * record or modify an existing one
 * 
 * release 2.5 nov-10-06
 * 
 * @author flavio
 * 
 */


/*----------------------------------------------------------
 * modification history
 * ====================
 * 23/10/06 - flavio - borders set to not resizable
 *                     changed Disease IN (/OUT) into Dignosis IN (/OUT)
 *                     
 * 10/11/06 - ross - added RememberDate for admission Date
 * 				   - only diseses with flag In Patient (IPD) are displayed
 *                 - on Insert. in edit all are displayed
 *                 - the correct way should be to display the IPD + the one aready registered
 * 18/08/08 - Alex/Andrea - Calendar added
 * 13/02/09 - Alex - Cosmetic changes to UI
 * 10/01/11 - Claudia - insert ward beds availability 
 -----------------------------------------------------------*/
public class AdmissionBrowserOld extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private EventListenerList admissionListeners = new EventListenerList();

	public interface AdmissionListener extends EventListener {
		public void admissionUpdated(AWTEvent e);

		public void admissionInserted(AWTEvent e);
	}

	public void addAdmissionListener(AdmissionListener l) {
		admissionListeners.add(AdmissionListener.class, l);
	}

	public void removeAdmissionListener(AdmissionListener listener) {
		admissionListeners.remove(AdmissionListener.class, listener);
	}

	private void fireAdmissionInserted(Admission anAdmission) {
		AWTEvent event = new AWTEvent(anAdmission, AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
		};

		EventListener[] listeners = admissionListeners
				.getListeners(AdmissionListener.class);
		for (int i = 0; i < listeners.length; i++)
			((AdmissionListener) listeners[i]).admissionInserted(event);
	}

	// if patient isDischarged send "null"
	// else send the ward code
	private void fireAdmissionUpdated(boolean isDischarge) {
		String ward = "null";
		if (!isDischarge){
			ward = admission.getWardId();
		}
		AWTEvent event = new AWTEvent(ward,	AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
		};

		EventListener[] listeners = admissionListeners
				.getListeners(AdmissionListener.class);
		for (int i = 0; i < listeners.length; i++)
			((AdmissionListener) listeners[i]).admissionUpdated(event);
	}

	private Patient patient = null;

	private boolean editing = false;

	private Admission admission = null;

	private PatientSummary ps = null;

	// used in the switch
//	private JFrame myParentFrame = null;

	/*
	 * from AdmittedPatientBrowser
	 */
	public AdmissionBrowserOld(JFrame parentFrame, AdmittedPatient admPatient,
			boolean editing) {
		super(parentFrame,
				(editing ? MessageBundle.getMessage("angal.admission.editadmissionrecord"): MessageBundle.getMessage("angal.admission.newadmission")), true);
		addAdmissionListener((AdmissionListener) parentFrame);
		this.editing = editing;
		patient = admPatient.getPatient();
		if (("" + patient.getSex()).equalsIgnoreCase("F")) {
			enablePregnancy = true;
		}
		ps = new PatientSummary(patient);
		AdmissionBrowserManager abm = new AdmissionBrowserManager();

		if (editing) {
			admission = abm.getCurrentAdmission(patient);
			if (admission.getWardId().equalsIgnoreCase("M")) {
				viewingPregnancy = true;
			} else {
			}
		} else {
			admission = new Admission();
		}

//		myParentFrame = parentFrame;
		initialize(parentFrame);
	}

	/*
	 * from PatientDataBrowser
	 */
	public AdmissionBrowserOld(JFrame parentFrame, JFrame admissionListener,
			Patient aPatient, Admission anAdmission) {
		super(parentFrame, MessageBundle.getMessage("angal.admission.editadmissionrecord"), true);
		addAdmissionListener((AdmissionListener) admissionListener);
		addAdmissionListener((AdmissionListener) parentFrame);
		this.editing = true;
		patient = aPatient;
		if (("" + patient.getSex()).equalsIgnoreCase("F")) {
			enablePregnancy = true;
		}
		ps = new PatientSummary(patient);

		AdmissionBrowserManager abm = new AdmissionBrowserManager();

		admission = abm.getAdmission(anAdmission.getId());
		if (admission.getWardId().equalsIgnoreCase("M")) {
			viewingPregnancy = true;
		} else {
		}
		initialize(parentFrame);
	}

	private void initialize(JFrame parent) {
		
		this.add(getJContentPane(), BorderLayout.CENTER);
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);

	}

	private JPanel jContentPane = null;

	/*
	 * entire frame is data + buttons
	 */	
	private JPanel getJContentPane() {
		if (jContentPane == null) {
		jContentPane = new JPanel();		
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getDataPanel(), java.awt.BorderLayout.CENTER);
			jContentPane.add(getButtonPanel(), java.awt.BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	
	/*
	 * data is patient data + admission data
	 */
	private JPanel getDataPanel() {
		JPanel data = new JPanel();
		data.setLayout(new BorderLayout());
		data.add(getPatientDataPanel(), java.awt.BorderLayout.WEST);
		data.add(getAdmissionDataPanel(), java.awt.BorderLayout.CENTER);
		return data;
	}

	private JTextArea textArea = null;

	/*
	 * the left side: patient data
	 */
	private JPanel getPatientDataPanel() {
		JPanel data = new JPanel();
		data.setLayout(new BoxLayout(data, BoxLayout.Y_AXIS));
		data.add(ps.getPatientDataPanel());

		textArea = new JTextArea(7, 20);
		if (editing && admission.getNote() != null) {
			textArea.setText(admission.getNote());
		}
		
		JScrollPane scrollPane = new JScrollPane(textArea);
		JPanel p = new JPanel(new BorderLayout());
		p.add(scrollPane, BorderLayout.CENTER);
		p = setMyBorder(p, MessageBundle.getMessage("angal.admission.admissionnotes"));
		
		data.add(p);
		return data;
	}
	
	/*
	 * the right side
	 */
	private JPanel getAdmissionDataPanel() {
		JPanel data = new JPanel();
		data.setLayout(new BorderLayout());
		data.add(getWardFHUYProgPanel(), java.awt.BorderLayout.NORTH);
		data.add(getAdmissionOpDataPanel(), java.awt.BorderLayout.CENTER);
		return data;
	}

	/*
	 * the variant part: normal or pregnancy
	 */

	// enable is if patient is female
	private boolean enablePregnancy = false;

	// viewing is if you set ward to pregnancy
	private boolean viewingPregnancy = false;

	
	
	private JPanel admissionDataPanel = null;

	/*
	 * show normal admission and extended field according with patient pregnancy
	 * status
	 */
	private JPanel getAdmissionOpDataPanel() {

		if (admissionDataPanel == null){
			admissionDataPanel = new JPanel();
		}
		else {
			admissionDataPanel.removeAll();
		}
		
		admissionDataPanel.setLayout(new BoxLayout(admissionDataPanel, BoxLayout.Y_AXIS));

		admissionDataPanel.add(getDiseaseInPanel());
		admissionDataPanel.add(getDateInPanel());
		admissionDataPanel.add(getDiseaseOutPanel());
		admissionDataPanel.add(getOperationPanel());
		admissionDataPanel.add(getOperationDatePanel());
		admissionDataPanel.add(getPregnancyValues());
		admissionDataPanel.add(getDateOutPanel());
		
		return admissionDataPanel;
	}
	
	
	private JPanel pregnancyValuePanel = null;
	/*
	 * returns a panel with pregnancy fields or an empty panel
	 */
	private JPanel getPregnancyValues() {

		if (!enablePregnancy)
			viewingPregnancy = false;

		if (pregnancyValuePanel == null){
			pregnancyValuePanel = new JPanel();
		}
		else {
			pregnancyValuePanel.removeAll();
		}
		
		pregnancyValuePanel.setLayout(new BoxLayout(pregnancyValuePanel,
				BoxLayout.Y_AXIS));

		if (viewingPregnancy) {
			pregnancyValuePanel.add(getVisitDataPanel());
			pregnancyValuePanel.add(getDeliveryDataPanel());
			pregnancyValuePanel.add(getDateControlPanel());			
		}
		else {
			pregnancyValuePanel.add(new JPanel());
		}
		pack();
		return pregnancyValuePanel;

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

	/*
	 * pregnancy methods and properties
	 */

	private GregorianCalendar visitDate = null;

	private float weight = 0.0f;

	private VoDateTextField visitDateField = null;

	private VoLimitedTextField weightField = null;
	
	private JDateChooser visitDateFieldCal = null; // Calendar

	private JComboBox treatmTypeBox = null;

	private ArrayList<PregnantTreatmentType> treatmTypeList = null;

	private final int preferredWidthDates = 110;
	
	private final int preferredWidthTypes = 220;
		
	private final int preferredWidthDiseaseBox = 400;
	
	private final int preferredHeight = 20;
	
	private final int preferredHeightLine = 60;

	/*
	 * pregnancy: 1st row: visit date, weight and treatment type
	 */
	private JPanel getVisitDataPanel() {
		JPanel p = new JPanel(new BorderLayout());
		
		if (editing && admission.getVisitDate() != null) {
			visitDate = admission.getVisitDate();
			Date myDate = visitDate.getTime();
			visitDateFieldCal = new JDateChooser(myDate, "dd/MM/yy"); // Calendar
		} else {
			visitDate = new GregorianCalendar();
			visitDateFieldCal = new JDateChooser(null, "dd/MM/yy"); // Calendar
		}
		visitDateFieldCal.setLocale(new Locale(GeneralData.LANGUAGE));
		visitDateFieldCal.setDateFormatString("dd/MM/yy");

		JPanel dateP = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 3));
		dateP.add(visitDateFieldCal); // Calendar
		dateP.setPreferredSize(new Dimension(preferredWidthDates, preferredHeightLine));
		dateP.setMaximumSize(new Dimension(preferredWidthDates, preferredHeightLine));
		dateP = setMyBorder(dateP, MessageBundle.getMessage("angal.admission.visitdate"));
		
		weightField = new VoLimitedTextField(5, 5);
		if (editing && admission.getWeight() != null) {
			weight = admission.getWeight().floatValue();
			weightField.setText(String.valueOf(weight));
		}	
		
		JPanel wP = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 3));
		wP.add(weightField);
		wP = setMyBorder(wP, MessageBundle.getMessage("angal.admission.weight"));

		PregnantTreatmentTypeBrowserManager abm = new PregnantTreatmentTypeBrowserManager();
		treatmTypeBox = new JComboBox();
		treatmTypeBox.addItem("");
		treatmTypeList = abm.getPregnantTreatmentType();
		for (PregnantTreatmentType elem : treatmTypeList) {
			treatmTypeBox.addItem(elem);
			if (editing) {
				if (admission.getPregTreatmentType() != null
						&& admission.getPregTreatmentType().equalsIgnoreCase(
								elem.getCode())) {
					treatmTypeBox.setSelectedItem(elem);
				}
			}
		}

		JPanel typeP = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 3));
		typeP.add(treatmTypeBox);
		typeP.setPreferredSize(new Dimension(preferredWidthTypes, preferredHeightLine));
		typeP.setMaximumSize(new Dimension(preferredWidthTypes, preferredHeightLine));
		typeP = setMyBorder(typeP, MessageBundle.getMessage("angal.admission.treatmenttype"));

		p.add(dateP, BorderLayout.WEST);
		p.add(wP, BorderLayout.CENTER);
		p.add(typeP, BorderLayout.EAST);
		return p;
	}

	private GregorianCalendar deliveryDate = null;

	private VoDateTextField deliveryDateField = null;

	private JDateChooser deliveryDateFieldCal = null;
	
	private JComboBox deliveryTypeBox = null;

	private JComboBox deliveryResultTypeBox = null;

	private ArrayList<DeliveryType> deliveryTypeList = null;

	private ArrayList<DeliveryResultType> deliveryResultTypeList = null;

	/*
	 * pregnancy: 2nd row: delivery date type and type result
	 */
	private JPanel getDeliveryDataPanel() {
		JPanel p = new JPanel(new BorderLayout());

		if (editing && admission.getDeliveryDate() != null) {
			deliveryDate = admission.getDeliveryDate();
			Date myDate = deliveryDate.getTime();
//			String d = currentDateFormat.format(myDate);
			// deliveryDateField = new VoDateTextField("dd/mm/yy", d, 15);
			deliveryDateFieldCal = new JDateChooser(myDate, "dd/MM/yy"); // Calendar
			deliveryDateFieldCal.setLocale(new Locale(GeneralData.LANGUAGE));
			deliveryDateFieldCal.setDateFormatString("dd/MM/yy");
		} else {
			//deliveryDateField = new VoDateTextField("dd/mm/yy", 15);
			deliveryDateFieldCal = new JDateChooser(null, "dd/MM/yy");
			deliveryDateFieldCal.setLocale(new Locale(GeneralData.LANGUAGE));
			deliveryDateFieldCal.setDateFormatString("dd/MM/yy");
		}

		JPanel dateP = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 3));
		//dateP.add(deliveryDateField);
		dateP.add(deliveryDateFieldCal);
		dateP.setPreferredSize(new Dimension(preferredWidthDates, preferredHeightLine));
		dateP.setMaximumSize(new Dimension(preferredWidthDates, preferredHeightLine));
		dateP = setMyBorder(dateP, MessageBundle.getMessage("angal.admission.deliverydate"));

		DeliveryTypeBrowserManager dtbm = new DeliveryTypeBrowserManager();
		deliveryTypeBox = new JComboBox();
		deliveryTypeBox.addItem("");
		deliveryTypeList = dtbm.getDeliveryType();
		for (DeliveryType elem : deliveryTypeList) {
			deliveryTypeBox.addItem(elem);
			if (editing) {
				if (admission.getDeliveryTypeId() != null
						&& admission.getDeliveryTypeId().equalsIgnoreCase(
								elem.getCode())) {
					deliveryTypeBox.setSelectedItem(elem);
				}
			}
		}
		JPanel DeliveryTypeP = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 3));
		DeliveryTypeP.add(deliveryTypeBox);
		DeliveryTypeP = setMyBorder(DeliveryTypeP, MessageBundle.getMessage("angal.admission.deliverytype"));

		DeliveryResultTypeBrowserManager drtbm = new DeliveryResultTypeBrowserManager();
		deliveryResultTypeBox = new JComboBox();
		deliveryResultTypeBox.addItem("");
		deliveryResultTypeList = drtbm.getDeliveryResultType();
		for (DeliveryResultType elem : deliveryResultTypeList) {
			deliveryResultTypeBox.addItem(elem);
			if (editing) {
				if (admission.getDeliveryResultId() != null
						&& admission.getDeliveryResultId().equalsIgnoreCase(
								elem.getCode())) {
					deliveryResultTypeBox.setSelectedItem(elem);
				}
			}
		}
		JPanel DeliveryResultTypeP = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 3));
		DeliveryResultTypeP.add(deliveryResultTypeBox);
		DeliveryResultTypeP.setPreferredSize(new Dimension(preferredWidthTypes, preferredHeightLine));
		DeliveryResultTypeP.setMaximumSize(new Dimension(preferredWidthTypes, preferredHeightLine));
		DeliveryResultTypeP = setMyBorder(DeliveryResultTypeP, MessageBundle.getMessage("angal.admission.deliveryresultype"));

		p.add(dateP, BorderLayout.WEST);
		p.add(DeliveryTypeP, BorderLayout.CENTER);
		p.add(DeliveryResultTypeP, BorderLayout.EAST);
		return p;
	}

	private GregorianCalendar ctrl1Date = null;

	private GregorianCalendar ctrl2Date = null;

	private GregorianCalendar abortDate = null;

//	private VoDateTextField ctrl1DateField = null;
	
	private JDateChooser ctrl1DateFieldCal = null;

//	private VoDateTextField ctrl2DateField = null;
	
	private JDateChooser ctrl2DateFieldCal = null;

//	private VoDateTextField abortDateField = null;
	
	private JDateChooser abortDateFieldCal = null;

	/*
	 * pregnancy: 3rd row: control and abort date
	 */
	private JPanel getDateControlPanel() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));

		if (editing && admission.getCtrlDate1() != null) {
			ctrl1Date = admission.getCtrlDate1();
			Date myDate = ctrl1Date.getTime();
			ctrl1DateFieldCal = new JDateChooser(myDate, "dd/MM/yy");
		} else {
			ctrl1DateFieldCal = new JDateChooser(null, "dd/MM/yy");
		}
		ctrl1DateFieldCal.setLocale(new Locale(GeneralData.LANGUAGE));
		ctrl1DateFieldCal.setDateFormatString("dd/MM/yy");
		// ctrl1DateField.setColumns(15);
		JPanel date1P = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 3));
		//date1P.add(ctrl1DateField);
		date1P.add(ctrl1DateFieldCal);
		date1P.setPreferredSize(new Dimension(preferredWidthDates, preferredHeightLine));
		date1P.setMaximumSize(new Dimension(preferredWidthDates, preferredHeightLine));
		date1P = setMyBorder(date1P, MessageBundle.getMessage("angal.admission.controln1date"));

		if (editing && admission.getCtrlDate2() != null) {
			ctrl2Date = admission.getCtrlDate2();
			Date myDate = ctrl2Date.getTime();
			ctrl2DateFieldCal = new JDateChooser(myDate, "dd/MM/yy");
		} else {
			ctrl2DateFieldCal = new JDateChooser(null, "dd/MM/yy");
		}
		ctrl2DateFieldCal.setLocale(new Locale(GeneralData.LANGUAGE));
		ctrl2DateFieldCal.setDateFormatString("dd/MM/yy");

		// ctrl2DateField.setColumns(15);
		JPanel date2P = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 3));
		// date2P.add(ctrl2DateField);
		date2P.add(ctrl2DateFieldCal);
		date2P.setPreferredSize(new Dimension(preferredWidthDates, preferredHeightLine));
		date2P.setMaximumSize(new Dimension(preferredWidthDates, preferredHeightLine));
		date2P = setMyBorder(date2P, MessageBundle.getMessage("angal.admission.controln2date"));

		if (editing && admission.getAbortDate() != null) {
			abortDate = admission.getAbortDate();
			Date myDate = abortDate.getTime();
			abortDateFieldCal = new JDateChooser(myDate, "dd/MM/yy");
		} else {
			abortDateFieldCal = new JDateChooser(null, "dd/MM/yy");
		}
		abortDateFieldCal.setLocale(new Locale(GeneralData.LANGUAGE));
		abortDateFieldCal.setDateFormatString("dd/MM/yy");			

		// abortDateField.setColumns(15);
		JPanel abortP = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 3));
		// abortP.add(abortDateField);
		abortP.add(abortDateFieldCal);
		abortP.setPreferredSize(new Dimension(preferredWidthTypes, preferredHeightLine));
		abortP.setMaximumSize(new Dimension(preferredWidthTypes, preferredHeightLine));
		abortP = setMyBorder(abortP, MessageBundle.getMessage("angal.admission.abortdate"));

		p.add(date1P);
		p.add(date2P);
		p.add(Box.createHorizontalGlue());
		p.add(abortP);
		return p;
	}

	
	
	/*
	 * normal admission values
	 */

	private JComboBox wardBox;

	private ArrayList<Ward> wardList = null;

	// save value during a swith
	private Ward saveWard = null;

	
	
	
	private String saveYProg = null;

	private JTextField yProgTextField = null;

	private JTextField FHUTextField = null;

	/*
	 * admission sheet: first row: select ward insert yprog & fhu
	 */
	private JPanel getWardFHUYProgPanel() {

		JPanel p = new JPanel(new BorderLayout());

		WardBrowserManager wbm = new WardBrowserManager();
		wardBox = new JComboBox();
		wardBox.addItem("");
		wardList = wbm.getWard();
		for (Ward elem : wardList) {
			// System.out.println(elem.getDescription()+" "+elem.getCode());
			// if patient is a male you don't see pregnancy case
			if (elem.getCode().equalsIgnoreCase("M")&& !enablePregnancy){
				continue;
			}
			else {
				if (elem.getBeds() > 0)
					wardBox.addItem(elem);
			}	
			if (saveWard != null) {
				if (saveWard.getCode().equalsIgnoreCase(elem.getCode())) {
					wardBox.setSelectedItem(elem);
				}
			} else if (editing) {
				if (admission.getWardId().equalsIgnoreCase(elem.getCode())) {
					wardBox.setSelectedItem(elem);
				}
			}
		}
		wardBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// set yProg
				if (wardBox.getSelectedIndex() == 0) {
					yProgTextField.setText("");
					return;
				} else {
					//String wardId = wardList.get(wardBox.getSelectedIndex() - 1).getCode();
					String wardId = ((Ward)wardBox.getSelectedItem()).getCode();
					if (wardId.equalsIgnoreCase(admission.getWardId())) {
						yProgTextField.setText("" + admission.getYProg());
					} else {
						AdmissionBrowserManager abm = new AdmissionBrowserManager();
						int nextProg = abm.getNextYProg(wardId);
						yProgTextField.setText("" + nextProg);
						
						//get default selected warn default beds number

						int nBeds = (((Ward)wardBox.getSelectedItem()).getBeds()).intValue();
						int usedBeds = abm.getUsedWardBed(wardId);
						int freeBeds = nBeds - usedBeds ;
						if (freeBeds <= 0 )
					     	JOptionPane.showMessageDialog(null,
							              MessageBundle.getMessage("angal.admission.wardwithnobedsavailable"));
					}
				}
						
				
				// switch panel
				
				if (((Ward) wardBox.getSelectedItem())
						.getCode().equalsIgnoreCase("M")) {
					if (viewingPregnancy) {
					}
					else if (!enablePregnancy) {
						// now is impossible
						JOptionPane.showMessageDialog(null,
								MessageBundle.getMessage("angal.admission.patientmustbeafemale"));
						wardBox.setSelectedIndex(0);
					}
					else {
						saveWard = (Ward) wardBox
								.getSelectedItem();
						saveYProg = yProgTextField.getText();
						viewingPregnancy = true;
						pregnancyValuePanel = getPregnancyValues();
						
						validate();
						repaint();
					}
				} else {
					if (viewingPregnancy) {
						saveWard = (Ward) wardBox
								.getSelectedItem();
						saveYProg = yProgTextField.getText();
						viewingPregnancy = false;
						pregnancyValuePanel = getPregnancyValues();
						
						validate();
						repaint();
					}
				}

			}
		});
		JPanel wardP = new JPanel();
		wardP.add(wardBox);
		wardP = setMyBorder(wardP, MessageBundle.getMessage("angal.admission.ward"));

		if (saveYProg != null) {
			yProgTextField = new JTextField(saveYProg);
		} else if (editing) {
			yProgTextField = new JTextField("" + admission.getYProg());
		} else {
			yProgTextField = new JTextField("");
		}
		yProgTextField.setColumns(11);

		if (editing) {
			FHUTextField = new JTextField(admission.getFHU());
		} else {
			FHUTextField = new JTextField();
		}
		FHUTextField.setColumns(20);

		JPanel yP = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 3));
		yP.add(yProgTextField);
		yP = setMyBorder(yP, MessageBundle.getMessage("angal.admission.progressiveinyear"));

		JPanel FHUP = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 3));
		FHUP.add(FHUTextField);
		FHUP = setMyBorder(FHUP, MessageBundle.getMessage("angal.admission.fromhealthunit"));

		p.add(wardP, BorderLayout.WEST);
		p.add(FHUP, BorderLayout.CENTER);
		p.add(yP, BorderLayout.EAST);
		return p;
	}

	private JComboBox diseaseInBox;

	private ArrayList<Disease> diseaseList = null;

	private JCheckBox malnuCheck;

	/*
	 * admission sheet: second row: select disease in
	 */
	private JPanel getDiseaseInPanel() {
		JPanel p = new JPanel();
		BorderLayout layout = new BorderLayout();
		layout.setHgap(10);
		p.setLayout(layout);
		
		boolean found=false;
		malnuCheck = new JCheckBox();
		if (editing && admission.getType().equalsIgnoreCase("M")) {
			malnuCheck.setSelected(true);
		} else {
			malnuCheck.setSelected(false);
		}
		JPanel malnuPanel = new JPanel(new BorderLayout());
		malnuPanel.add(malnuCheck, BorderLayout.WEST);
		malnuPanel.add(new JLabel(MessageBundle.getMessage("angal.admission.malnutrition")), BorderLayout.CENTER);

		DiseaseBrowserManager dbm = new DiseaseBrowserManager();
		diseaseInBox = new JComboBox();
		diseaseInBox.setMaximumSize(new Dimension(preferredWidthDiseaseBox, preferredHeight));
		diseaseInBox.setPreferredSize(new Dimension(preferredWidthDiseaseBox, preferredHeight));
		diseaseInBox.addItem("");
		diseaseList = dbm.getDiseaseIpd();
		
		for (Disease elem : diseaseList) {
			diseaseInBox.addItem(elem);
			if (editing) {
				if (admission.getDiseaseInId() != null
						&& admission.getDiseaseInId().equalsIgnoreCase(
								elem.getCode())) {
					diseaseInBox.setSelectedItem(elem);
					found=true;
				}
			}
		}
		if (editing && !found && admission.getDiseaseInId()!=null)  {//get the description !!!!
			diseaseInBox.addItem(MessageBundle.getMessage("angal.admission.no") + admission.getDiseaseInId()+ MessageBundle.getMessage("angal.admission.notfoundasinpatientdisease"));
			diseaseInBox.setSelectedIndex(diseaseInBox.getItemCount()-1);
			}
	
		JPanel diseaseP = new JPanel();
		diseaseP.add(diseaseInBox);
		diseaseP = setMyBorder(diseaseP, MessageBundle.getMessage("angal.admission.diagnosisin"));

		p.add(malnuPanel, BorderLayout.WEST);
		p.add(diseaseP, BorderLayout.CENTER);
		return p;
	}

	private GregorianCalendar dateIn = null;

	private VoDateTextField dateInField = null;
	
	private JDateChooser dateInFieldCal = null;

	private JComboBox admTypeBox = null;

	private ArrayList<AdmissionType> admTypeList = null;

	private DateFormat currentDateFormat = DateFormat.getDateInstance(
			DateFormat.SHORT, Locale.ITALIAN);

	// to adjust some size...
	private int preferredSize = 0;

	private int minSize = 0;

	/*
	 * admission sheet: 3th row: insert date and type admission
	 */
	private JPanel getDateInPanel() {
		JPanel p = new JPanel(new BorderLayout());

		java.util.Date myDate = null;
		if (editing) {
			dateIn = admission.getAdmDate();
		} else {
			//dateIn = new GregorianCalendar();
			dateIn = RememberDates.getLastAdmInDateGregorian();
		}

		myDate = dateIn.getTime();
		dateInFieldCal = new JDateChooser(myDate, "dd/MM/yy"); // Calendar
		dateInFieldCal.setLocale(new Locale(GeneralData.LANGUAGE));
		dateInFieldCal.setDateFormatString("dd/MM/yy");

		JPanel dateP = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 3));
		dateP.add(dateInFieldCal);
		dateP = setMyBorder(dateP, MessageBundle.getMessage("angal.admission.admissiondate"));

		AdmissionBrowserManager abm = new AdmissionBrowserManager();
		admTypeBox = new JComboBox();
		admTypeBox.addItem("");
		admTypeList = abm.getAdmissionType();
		for (AdmissionType elem : admTypeList) {
			admTypeBox.addItem(elem);
			if (editing) {
				if (admission.getAdmType().equalsIgnoreCase(elem.getCode())) {
					admTypeBox.setSelectedItem(elem);
				}
			}
		}

		JPanel typeP = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 3));
		typeP.add(admTypeBox);
		typeP = setMyBorder(typeP, MessageBundle.getMessage("angal.admission.admissiontype"));
		preferredSize = (int) (admTypeBox.getPreferredSize().width * 1.2);
		minSize = (int) (admTypeBox.getMinimumSize().width * 1.2);
		admTypeBox.setMinimumSize(new Dimension(preferredSize, admTypeBox
				.getMinimumSize().height));
		admTypeBox.setPreferredSize(new Dimension(minSize, admTypeBox
				.getPreferredSize().height));

		p.add(dateP, BorderLayout.WEST);
		p.add(typeP, BorderLayout.EAST);
		return p;
	}

	private JComboBox diseaseOut1Box = null;
	private JComboBox diseaseOut2Box = null;
	private JComboBox diseaseOut3Box = null;

	/*
	 * 
	 * admission sheet: 4th row: the disease out
	 */
	private JPanel getDiseaseOutPanel() {
		JPanel diseaseOutPanel = new JPanel();
		diseaseOutPanel.setLayout(new BoxLayout(diseaseOutPanel, BoxLayout.Y_AXIS));
		diseaseOutPanel = setMyBorder(diseaseOutPanel, MessageBundle.getMessage("angal.admission.diagnosisout"));
		diseaseOutPanel.add(getDiseaseOut1Panel());
		diseaseOutPanel.add(getDiseaseOut2Panel());
		diseaseOutPanel.add(getDiseaseOut3Panel());
		return diseaseOutPanel;
	}

	/**
	 * @return
	 */
	private JPanel getDiseaseOut1Panel() {
		boolean found = false;
		
		JLabel label = new JLabel(MessageBundle.getMessage("angal.admission.diagnosisout1"));

		diseaseOut1Box = new JComboBox();
		diseaseOut1Box.setMaximumSize(new Dimension(preferredWidthDiseaseBox, preferredHeight));
		diseaseOut1Box.setPreferredSize(new Dimension(preferredWidthDiseaseBox, preferredHeight));
		diseaseOut1Box.addItem("");
		for (Disease elem : diseaseList) {
			
			diseaseOut1Box.addItem(elem);
			if (editing) {
				if (admission.getDiseaseOutId1() != null
						&& admission.getDiseaseOutId1().equalsIgnoreCase(
								elem.getCode())) {
					diseaseOut1Box.setSelectedItem(elem);
					found=true;
				}
			}
		}
		if (editing && !found && admission.getDiseaseOutId1()!=null) {//get the description !!!!
			diseaseOut1Box.addItem(MessageBundle.getMessage("angal.admission.no") + admission.getDiseaseOutId1() + " " + MessageBundle.getMessage("angal.admission.notfoundasinpatientdisease"));
			diseaseOut1Box.setSelectedIndex(diseaseInBox.getItemCount()-1);
		}
		JPanel diseaseOut1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		diseaseOut1.add(label);
		diseaseOut1.add(diseaseOut1Box);
		
		return diseaseOut1;
	}
	
	

	/*
	 * 
	 * admission sheet: 4th row: the disease out
	 */
	private JPanel getDiseaseOut2Panel() {
		boolean found = false;
		
		JLabel label = new JLabel(MessageBundle.getMessage("angal.admission.diagnosisout2"));

		diseaseOut1Box = new JComboBox();
		diseaseOut1Box.setMaximumSize(new Dimension(preferredWidthDiseaseBox, preferredHeight));
		diseaseOut1Box.setPreferredSize(new Dimension(preferredWidthDiseaseBox, preferredHeight));
		diseaseOut1Box.addItem("");
		for (Disease elem : diseaseList) {
			
			diseaseOut1Box.addItem(elem);
			if (editing) {
				if (admission.getDiseaseOutId1() != null
						&& admission.getDiseaseOutId1().equalsIgnoreCase(
								elem.getCode())) {
					diseaseOut1Box.setSelectedItem(elem);
					found=true;
				}
			}
		}
		if (editing && !found && admission.getDiseaseOutId1()!=null) {//get the description !!!!
			diseaseOut1Box.addItem(MessageBundle.getMessage("angal.admission.no") + admission.getDiseaseOutId1() + " " + MessageBundle.getMessage("angal.admission.notfoundasinpatientdisease"));
			diseaseOut1Box.setSelectedIndex(diseaseInBox.getItemCount()-1);
		}
		JPanel diseaseOut2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		diseaseOut2.add(label);
		diseaseOut2.add(diseaseOut1Box);
		
		return diseaseOut2;
	}
	
	
	/*
	 * 
	 * admission sheet: 4th row: the disease out
	 */
	private JPanel getDiseaseOut3Panel() {
		boolean found = false;
		
		JLabel label = new JLabel(MessageBundle.getMessage("angal.admission.diagnosisout3"));

		diseaseOut3Box = new JComboBox();
		diseaseOut3Box.setMaximumSize(new Dimension(preferredWidthDiseaseBox, preferredHeight));
		diseaseOut3Box.setPreferredSize(new Dimension(preferredWidthDiseaseBox, preferredHeight));
		diseaseOut3Box.addItem("");
		for (Disease elem : diseaseList) {
			
			diseaseOut3Box.addItem(elem);
			if (editing) {
				if (admission.getDiseaseOutId3() != null
						&& admission.getDiseaseOutId3().equalsIgnoreCase(
								elem.getCode())) {
					diseaseOut3Box.setSelectedItem(elem);
					found=true;
				}
			}
		}
		if (editing && !found && admission.getDiseaseOutId3()!=null) {//get the description !!!!
			diseaseOut3Box.addItem(MessageBundle.getMessage("angal.admission.no") + admission.getDiseaseOutId3() + " " + MessageBundle.getMessage("angal.admission.notfoundasinpatientdisease"));
			diseaseOut3Box.setSelectedIndex(diseaseInBox.getItemCount()-1);
		}
		JPanel diseaseOut3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		diseaseOut3.add(label);
		diseaseOut3.add(diseaseOut3Box);

		return diseaseOut3;
	}

	private JComboBox operationBox = null;

	private JRadioButton operationResultRadioP = null;

	private JRadioButton operationResultRadioN = null;

	private JRadioButton operationResultRadioU = null;
	
	private ArrayList<Operation> operationList = null;
	
	/*
	 * simply an utility
	 */
	private JRadioButton getRadioButton(String label, char mn, boolean active) {
		JRadioButton rb = new JRadioButton(label);
		rb.setMnemonic(KeyEvent.VK_A + (mn - 'A'));
		rb.setSelected(active);
		rb.setName(label);
		return rb;
	}

	/*
	 * admission sheet: 5th row: insert select operation type and result
	 */
	private JPanel getOperationPanel() {
		JPanel p = new JPanel();
		BoxLayout layout = new BoxLayout(p, BoxLayout.X_AXIS);
		p.setLayout(layout);

		OperationBrowserManager obm = new OperationBrowserManager();
		operationBox = new JComboBox();
		operationBox.addItem("");
		operationList = obm.getOperation();
		for (Operation elem : operationList) {
			operationBox.addItem(elem);
			if (editing) {
				if (admission.getOperationId() != null
						&& admission.getOperationId().equalsIgnoreCase(
								elem.getCode())) {
					operationBox.setSelectedItem(elem);
				}
			}
		}
		
		operationBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (operationBox.getSelectedIndex()==0){
					// operationDateField.setText("");
				    operationDateFieldCal.setDate(null);
				}
				else {
					/*if (!operationDateField.getText().equals("")){
					// leave old date value
				}*/
				if (operationDateFieldCal.getDate()!=null){
					// leave old date value
				}
				
				else {
						// set today date
					operationDateFieldCal.setDate((new GregorianCalendar()).getTime());
					}
				}
			}
		});
		
		
		JPanel typeOpP = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 3));
		typeOpP.add(operationBox);
		//typeOpP.setPreferredSize(new Dimension(preferredWidth,70));
		typeOpP = setMyBorder(typeOpP, MessageBundle.getMessage("angal.admission.operationtype"));
		
		if (editing) {
			if (admission.getOpResult() == null)
				operationResultRadioU = getRadioButton(MessageBundle.getMessage("angal.admission.unknown"), 'U', true);
			else
				operationResultRadioU = getRadioButton(MessageBundle.getMessage("angal.admission.unknown"), 'U', false);
			if (admission.getOpResult() != null
					&& admission.getOpResult().equalsIgnoreCase("P"))
				operationResultRadioP = getRadioButton(MessageBundle.getMessage("angal.admission.positive"), 'P', true);
			else
				operationResultRadioP = getRadioButton(MessageBundle.getMessage("angal.admission.positive"), 'P', false);
			if (admission.getOpResult() != null
					&& admission.getOpResult().equalsIgnoreCase("N"))
				operationResultRadioN = getRadioButton(MessageBundle.getMessage("angal.admission.negative"), 'N', true);
			else
				operationResultRadioN = getRadioButton(MessageBundle.getMessage("angal.admission.negative"), 'N', false);
		} else {
			operationResultRadioP = getRadioButton(MessageBundle.getMessage("angal.admission.positive"), 'P', false);
			operationResultRadioN = getRadioButton(MessageBundle.getMessage("angal.admission.negative"), 'N', false);
			operationResultRadioU = getRadioButton(MessageBundle.getMessage("angal.admission.unknown"), 'U', true);
			// System.out.println(operationResultRadioP==null);
		}

		ButtonGroup resultGroup = new ButtonGroup();
		resultGroup.add(operationResultRadioP);
		resultGroup.add(operationResultRadioN);
		resultGroup.add(operationResultRadioU);

		JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		//resultPanel.setPreferredSize(new Dimension(preferredWidth,70));
		resultPanel.add(operationResultRadioP);
		resultPanel.add(operationResultRadioN);
		resultPanel.add(operationResultRadioU);

		resultPanel = setMyBorder(resultPanel, MessageBundle.getMessage("angal.admission.operationresult"));

		p.add(typeOpP);
		//p.add(Box.createHorizontalGlue());
		p.add(resultPanel);

		return p;
	}

	
	private GregorianCalendar operationDate = null;
	
	private JDateChooser operationDateFieldCal = null;

	private VoDateTextField operationDateField = null;

	private float trsfUnit = 0.0f;
	private VoLimitedTextField trsfUnitField = null;
	/*
	 * admission sheet: 6th row: insert operation date and transusional unit
	 */
	private JPanel getOperationDatePanel() {
		JPanel p = new JPanel();
		BoxLayout layout = new BoxLayout(p, BoxLayout.X_AXIS);
		p.setLayout(layout);
				
		Date myDate = null;
		if (editing && admission.getOpDate() != null) {
			operationDate = admission.getOpDate();
			myDate = operationDate.getTime();
		} 
		operationDateFieldCal = new JDateChooser(myDate, "dd/MM/yy");
		operationDateFieldCal.setLocale(new Locale(GeneralData.LANGUAGE));
	    operationDateFieldCal.setDateFormatString("dd/MM/yy");
	    
		JPanel opDateP = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 3));
		opDateP.setPreferredSize(new Dimension(preferredWidthDates, preferredHeightLine));
		opDateP.setMaximumSize(new Dimension(preferredWidthDates, preferredHeightLine));
		opDateP = setMyBorder(opDateP, MessageBundle.getMessage("angal.admission.operationdate"));
		opDateP.add(operationDateFieldCal);
		
		trsfUnitField = new VoLimitedTextField(5, 5);
		if (editing && admission.getTransUnit() != null) {
			trsfUnit = admission.getTransUnit().floatValue();
			trsfUnitField.setText(""+ trsfUnit);
		}	
		
		JPanel wP = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 3));
		wP.setPreferredSize(new Dimension(preferredWidthTypes, preferredHeightLine));
		wP.setMaximumSize(new Dimension(preferredWidthTypes, preferredHeightLine));
		wP = setMyBorder(wP, MessageBundle.getMessage("angal.admission.transfusionalunit"));
		wP.add(trsfUnitField);
		
		p.add(opDateP);
		p.add(Box.createHorizontalGlue());
		p.add(wP);

		return p;
	}
	
	
	
	
	private GregorianCalendar dateOut = null;

	private VoDateTextField dateOutField = null;
	
	private JDateChooser dateOutFieldCal = null;

	private JComboBox disTypeBox = null;

	private ArrayList<DischargeType> disTypeList = null;

	/*
	 * admission sheet: 7th row: insert date and type discharge
	 */
	private JPanel getDateOutPanel() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));

		Date myDate = null;
		if (editing && admission.getDisDate() != null) {
			dateOut = admission.getDisDate();
			myDate = dateOut.getTime();
		}
		dateOutFieldCal = new JDateChooser(myDate, "dd/MM/yy");
		dateOutFieldCal.setLocale(new Locale(GeneralData.LANGUAGE));
		dateOutFieldCal.setDateFormatString("dd/MM/yy");

		// dateOutField.setColumns(15);

		JPanel dateP = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 3));
		// dateP.add(dateOutField);
		dateP.add(dateOutFieldCal);
		dateP.setPreferredSize(new Dimension(preferredWidthDates, preferredHeightLine));
		dateP.setMaximumSize(new Dimension(preferredWidthDates, preferredHeightLine));
		dateP = setMyBorder(dateP, MessageBundle.getMessage("angal.admission.dischargedate"));

		AdmissionBrowserManager abm = new AdmissionBrowserManager();
		disTypeBox = new JComboBox();
		disTypeBox.addItem("");
		disTypeList = abm.getDischargeType();
		for (DischargeType elem : disTypeList) {
			disTypeBox.addItem(elem);
			if (editing) {
				if (admission.getDisType() != null
						&& admission.getDisType()
								.equalsIgnoreCase(elem.getCode())) {
					disTypeBox.setSelectedItem(elem);
				}
			}
		}

		JPanel typeP = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 3));
		typeP.add(disTypeBox);
		typeP = setMyBorder(typeP, MessageBundle.getMessage("angal.admission.dischargetype"));
		typeP.setPreferredSize(new Dimension(preferredWidthTypes, preferredHeightLine));
		typeP.setMaximumSize(new Dimension(preferredWidthTypes, preferredHeightLine));

		p.add(dateP);
		p.add(Box.createHorizontalGlue());
		p.add(typeP);
		return p;
	}

	private JPanel buttonPanel = null;

	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel(new BorderLayout());

			JLabel l = new JLabel(MessageBundle.getMessage("angal.admission.indicatesrequiredfields"));

			JPanel lP = new JPanel();
			lP.add(l);

			JPanel bP = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
			bP.add(getSaveButton(), null);
			bP.add(getCloseButton(), null);

			buttonPanel.add(lP, BorderLayout.WEST);
			buttonPanel.add(bP, BorderLayout.CENTER);
		}

		return buttonPanel;
	}

	private JButton closeButton = null;

	private JButton getCloseButton() {
		if (closeButton == null) {
			closeButton = new JButton();
			closeButton.setText(MessageBundle.getMessage("angal.admission.close"));
			closeButton.setMnemonic(KeyEvent.VK_C);
			closeButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return closeButton;
	}

	private JButton saveButton = null;

	private JButton getSaveButton() {

		if (saveButton == null) {
			saveButton = new JButton();
			saveButton.setText(MessageBundle.getMessage("angal.admission.save"));
			saveButton.setMnemonic(KeyEvent.VK_S);
			saveButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

					GregorianCalendar today = new GregorianCalendar();

					/*
					 * is it an admission update or a discharge? if we have a
					 * valid discharge date isDischarge will be true
					 */
					boolean isDischarge = false;

					/*
					 * set if ward pregnancy is selected
					 */
					boolean isPregnancy = false;

					// get ward id (not null)
					if (wardBox.getSelectedIndex() == 0) {
						JOptionPane.showMessageDialog(null,
								MessageBundle.getMessage("angal.admission.pleaseselectavalidward"));
						return;
					} else {
						//System.out.println(MessageBundle.getMessage("angal.admission.wardis")+((Ward)wardBox.getSelectedItem()).getDescription());
						//admission.setWardId(wardList.get(wardBox.getSelectedIndex() - 1).getCode());
						admission.setWardId(((Ward)(wardBox.getSelectedItem())).getCode());
					}

					if (admission.getWardId().equalsIgnoreCase("M")) {
						isPregnancy = true;
					}

					// get disease in id ( it can be null)
					if (diseaseInBox.getSelectedIndex() == 0) {
						//admission.setDiseaseInId(null);
						JOptionPane.showMessageDialog(null,
								MessageBundle.getMessage("angal.admission.pleaseselectavaliddiseasein"));
						return;
						// System.out.println("diseaseInId is null");
					} else {
						try {
							admission.setDiseaseInId(diseaseList.get(
									diseaseInBox.getSelectedIndex() - 1).getCode());
						} catch (IndexOutOfBoundsException e1) {
							/*
							 * Workaround in case a fake-disease is selected
							 * (ie when previous disease has been deleted) 
							 */
							admission.setDiseaseInId(null);
						}
						// System.out.println("diseaseInId is
						// "+admission.getDiseaseInId());
					}

					// get disease out id ( it can be null)
					if (diseaseOut1Box.getSelectedIndex() == 0) {
						admission.setDiseaseOutId1(null);
						// System.out.println("diseaseOutId is null");
					} else {
						admission
								.setDiseaseOutId1(diseaseList.get(
										diseaseOut1Box.getSelectedIndex() - 1)
										.getCode());
						// System.out.println("diseaseOutId is
						// "+admission.getDiseaseOutId());
					}
					
					// get disease out id 2 ( it can be null)
					if (diseaseOut2Box.getSelectedIndex() == 0) {
						admission.setDiseaseOutId2(null);
						// System.out.println("diseaseOutId is null");
					} else {
						admission
								.setDiseaseOutId2(diseaseList.get(
										diseaseOut2Box.getSelectedIndex() - 1)
										.getCode());
						// System.out.println("diseaseOutId is
						// "+admission.getDiseaseOutId());
					}
					
					// get disease out id 3 ( it can be null)
					if (diseaseOut3Box.getSelectedIndex() == 0) {
						admission.setDiseaseOutId3(null);
						// System.out.println("diseaseOutId is null");
					} else {
						admission
								.setDiseaseOutId3(diseaseList.get(
										diseaseOut3Box.getSelectedIndex() - 1)
										.getCode());
						// System.out.println("diseaseOutId is
						// "+admission.getDiseaseOutId());
					}

					// get year prog ( not null)
					try {
						int x = Integer.parseInt(yProgTextField.getText());
						if (x < 0) {
							JOptionPane.showMessageDialog(null,
									MessageBundle.getMessage("angal.admission.pleaseinsertacorrectprogressiveid"));
							return;
						} else {
							admission.setYProg(x);
							// System.out.println("yP is "+x);
						}
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null,
								MessageBundle.getMessage("angal.admission.pleaseinsertacorrectprogressiveid"));
						return;
					}

					// get FHU (it can be null)
					String s = FHUTextField.getText();
					if (s.equals("")) {
						admission.setFHU(null);
						// System.out.println("FHU is null");
					} else {
						admission.setFHU(FHUTextField.getText());
						// System.out.println("FHU is "+admission.getFHU());
					}

					// check and get date in (not null)
					// String d = dateInField.getText().trim();
					String d = currentDateFormat.format(dateInFieldCal.getDate());

					try {
						currentDateFormat.setLenient(false);
						Date date = currentDateFormat.parse(d);
						dateIn = new GregorianCalendar();
						dateIn.setTime(date);
						if (dateIn.after(today)) {
							JOptionPane
									.showMessageDialog(null,
											MessageBundle.getMessage("angal.admission.futuredatenotallowed"));
							d = currentDateFormat.format(today);
							// dateInField.setText(d);
							dateInFieldCal.setDate(currentDateFormat.parse(d));
							return;
						}
						if (dateIn.before(today)) {
							AdmissionBrowserManager abm = new AdmissionBrowserManager();
							ArrayList<Admission> admList = abm
									.getAdmissions(patient);
							// check for invalid date
							for (Admission ad : admList) {
								if (editing && ad.getId() == admission.getId()) {
									continue;
								}
								if (ad.getAdmDate().before(dateIn)
										&& ad.getDisDate() != null
										&& ad.getDisDate().after(dateIn)) {
									JOptionPane
											.showMessageDialog(null,
													MessageBundle.getMessage("angal.admission.ininserteddatepatientwasalreadyadmitted"));
									d = currentDateFormat.format(today);
									// dateInField.setText(d);
									dateInFieldCal.setDate(currentDateFormat.parse(d));
									return;
								}
							}
						}
						// updateDisplay
						d = currentDateFormat.format(date);
						// dateInField.setText(d);
						dateInFieldCal.setDate(currentDateFormat.parse(d));
						admission.setAdmDate(dateIn);
						RememberDates.setLastAdmInDate(dateIn);
						// System.out.println("adm date is
						// "+admission.getAdmDate().getTime());
						// java.sql.Date dd = new
						// java.sql.Date(dateIn.getTimeInMillis());
						// System.out.println("adm date is "+dd.getTime());

					} catch (ParseException pe) {
						JOptionPane.showMessageDialog(null,
								MessageBundle.getMessage("angal.admission.pleaseinsertavalidadmissiondate"));
						dateInField.setText(currentDateFormat.format(dateIn
								.getTime()));// CONTROLLARE
						return;
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(null,
								MessageBundle.getMessage("angal.admission.pleaseinsertavalidadmissiondate"));
						dateInField.setText(currentDateFormat.format(dateIn
								.getTime()));// CONTROLLARE
						return;
					}

					// get admission type (not null)
					if (admTypeBox.getSelectedIndex() == 0) {
						JOptionPane.showMessageDialog(null,
								MessageBundle.getMessage("angal.admission.pleaseselectavalidadmissiondate"));
						return;
					} else {
						admission.setAdmType(admTypeList.get(
								admTypeBox.getSelectedIndex() - 1).getCode());
						// System.out.println("admType is
						// "+admission.getAdmType());
					}

					// check and get date out (it can be null)
					// if set date out, isDischarge is set
					// d = dateOutField.getText().trim();
					if (dateOutFieldCal.getDate()!=null) {
						d = currentDateFormat.format(dateOutFieldCal.getDate());
					} else d="";

					if (d.equals("")) {
						// only if we are editing the last admission
						// or if it is a new admission
						// no if we are editing an old admission
						AdmissionBrowserManager abm = new AdmissionBrowserManager();
						ArrayList<Admission> admList = abm
								.getAdmissions(patient);
						Admission last = null;
						if (admList.size() > 0) {
							last = admList.get(admList.size() - 1);
						} else {
							last = admission;
						}
						// System.out.println(editing+" "+last.getId()+"
						// "+admission.getId());
						if (!editing
								|| (editing && admission.getId() == last
										.getId())) {
							// ok
						} else {
							JOptionPane
									.showMessageDialog(
											null,
											MessageBundle.getMessage("angal.admission.pleaseinsertadischargedate")
													+ MessageBundle.getMessage("angal.admission.youareeditinganoldadmission"));
							return;

						}

						admission.setDisDate(null);
						// System.out.println("dis date out is null");
					} else {
						try {
							currentDateFormat.setLenient(false);
							Date date = currentDateFormat.parse(d);
							dateOut = new GregorianCalendar();
							dateOut.setTime(date);

							// date control
							if (dateOut.before(dateIn)) {
								JOptionPane
										.showMessageDialog(null,
												MessageBundle.getMessage("angal.admission.dischargedatemustbeafteradmissiondate"));
								return;
							}
							if (admission.getDiseaseOutId1() == null) {
								int yes = JOptionPane.showConfirmDialog(null, MessageBundle.getMessage("angal.admission.diagnosisoutsameasdiagnosisin"));
								if (yes == JOptionPane.YES_OPTION) {
									admission.setDiseaseOutId1(admission.getDiseaseInId());
								} else {
									JOptionPane
									.showMessageDialog(null,
											MessageBundle.getMessage("angal.admission.pleaseselectatleastfirstdiagnosisout"));
									return;
								}
							}
							if (dateOut.after(today)) {
								JOptionPane
										.showMessageDialog(null,
												MessageBundle.getMessage("angal.admission.futuredatenotallowed"));
								return;
							} else {
								AdmissionBrowserManager abm = new AdmissionBrowserManager();
								ArrayList<Admission> admList = abm
										.getAdmissions(patient);
								// check for invalid date
								boolean invalidDate = false;
								Date invalidStart = new Date();
								Date invalidEnd = new Date();
								for (Admission ad : admList) {
									// case current admission : let it be
									if (editing
											&& ad.getId() == admission.getId()) {
										continue;
									}
									// found an open admission
									// only if i close my own first of it
									if (ad.getDisDate() == null) {
										if (!dateOut.after(ad.getAdmDate()))
											;// ok
										else {
											JOptionPane
													.showMessageDialog(
															null,
															MessageBundle.getMessage("angal.admission.intheselecteddatepatientwasadmittedagain"));
											return;
										}
									}
									// general case
									else {
										/*System.out.println(MessageBundle.getMessage("angal.admission.adstart")
												+ ad.getAdmDate().getTime()
												+ " "
												+ MessageBundle.getMessage("angal.admission.stop")
												+ " "
												+ ad.getDisDate().getTime());
										System.out.println(MessageBundle.getMessage("angal.admission.weneed")
												+ " "
												+ dateIn.getTime() + MessageBundle.getMessage("angal.admission.to")
												+ " "
												+ dateOut.getTime());*/
										
										//
										// DateIn >= adOut
										if (dateIn.after(ad.getDisDate())
												|| dateIn.equals(ad
														.getDisDate())) {
											//System.out.println("ok1");
											// ok
										}
										// dateOut <= adIn
										else if (dateOut
												.before(ad.getAdmDate())
												|| dateOut.equals(ad
														.getAdmDate())) {
											//System.out.println("ok2");
											// ok
										} else {
											invalidDate = true;
											invalidStart = ad.getAdmDate()
													.getTime();
											invalidEnd = ad.getDisDate()
													.getTime();
											break;
										}
									}
								}
								if (invalidDate) {
									JOptionPane
											.showMessageDialog(
													null,
													MessageBundle.getMessage("angal.admission.invalidadmissionperiod")
															+ MessageBundle.getMessage("angal.admission.theadmissionbetween")
															+ " "
															+ currentDateFormat
																	.format(invalidStart)
															+ " "
															+ MessageBundle.getMessage("angal.admission.and")
															+ " "
															+ currentDateFormat
																	.format(invalidEnd)
															+ " "
															+ MessageBundle.getMessage("angal.admission.alreadyexists"));
									//dateOutField.setText("");
									dateOutFieldCal.setDate(null);
									return;
								}

							}

							// updateDisplay
							d = currentDateFormat.format(date);
							//dateOutField.setText(d);
							dateOutFieldCal.setDate(currentDateFormat.parse(d));
							admission.setDisDate(dateOut);
							isDischarge = true;
							// System.out.println("dis date out
							// "+admission.getDisDate());
						} catch (ParseException pe) {
							System.out.println(pe);
							JOptionPane.showMessageDialog(null,
									MessageBundle.getMessage("angal.admission.pleaseinsertavaliddischargedate"));
							dateOutField.setText("");//CONTROLLARE
							return;
						} catch (IllegalArgumentException iae) {
							System.out.println(iae);
							JOptionPane.showMessageDialog(null,
									MessageBundle.getMessage("angal.admission.pleaseinsertavaliddischargedate"));
							dateOutField.setText("");//CONTROLLARE
							return;
						}
					}

					// get operation ( it can be null)
					if (operationBox.getSelectedIndex() == 0) {
						admission.setOperationId(null);
					} else {
						admission.setOperationId(operationList.get(
								operationBox.getSelectedIndex() - 1).getCode());
						// System.out.println("operationType is
						// "+admission.getOperationId());
					}

					
					// get operation date (may be null)
					//d = operationDateField.getText().trim();
					if (operationDateFieldCal.getDate()!=null) {
						d = currentDateFormat.format(operationDateFieldCal.getDate());
					} else d="";
					if (d.equals("")) {
						//System.out.println("visit is null");
						admission.setOpDate(null);
					} 
					else {
						try {
							Date date = currentDateFormat.parse(d);
							operationDate = new GregorianCalendar();
							operationDate.setTime(date);
							// updateDisplay
							d = currentDateFormat.format(date);
							//operationDateField.setText(d);
							operationDateFieldCal.setDate(currentDateFormat.parse(d));

							GregorianCalendar limit;
							if (admission.getDisDate() == null) {
								limit = today;
							} else {
								limit = admission.getDisDate();
							}

							if (operationDate.before(dateIn)
									|| operationDate.after(limit)) {
								JOptionPane.showMessageDialog(null,
										MessageBundle.getMessage("angal.admission.pleaseinsertavalidvisitdateplease"));
								return;
							}

							admission.setOpDate(operationDate);
						} catch (ParseException pe) {
							JOptionPane.showMessageDialog(null,
									MessageBundle.getMessage("angal.admission.pleaseinsertavalidvisitdateplease"));
							operationDateField.setText(currentDateFormat
									.format(operationDate.getTime()));
							return;
						} catch (IllegalArgumentException iae) {
							JOptionPane.showMessageDialog(null,
									MessageBundle.getMessage("angal.admission.pleaseinsertavalidvisitdateplease"));
							operationDateField.setText(currentDateFormat
									.format(operationDate.getTime()));
							return;
						}
					}// else
					
					
					
					
					// get operation result (can be null)
					if (operationResultRadioN.isSelected()) {
						// System.out.println("N");
						admission.setOpResult("N");
					} else if (operationResultRadioP.isSelected()) {
						// System.out.println("P");
						admission.setOpResult("P");
					} else {
						// System.out.println("null");
						admission.setOpResult(null);
					}

					// System.out.println("Operation result is
					// "+(admission.getOpResult()==null?"null":admission.getOpResult()
					// ));

					// get discharge type (it can be null)
					// if isDischarge, null value not allowed

					if (disTypeBox.getSelectedIndex() == 0) {
						if (isDischarge) {
							JOptionPane.showMessageDialog(null,
									MessageBundle.getMessage("angal.admission.pleaseselectavaliddischargetype"));
							return;
						} else {
							admission.setDisType(null);
							// System.out.println("disType is null");
						}
					} else {
						if (isDischarge) {
							admission.setDisType(disTypeList.get(
									disTypeBox.getSelectedIndex() - 1).getCode());
						} else {
							admission.setDisType(null);
						}
						// System.out.println("disType is
						// "+admission.getAdmType());
					}

					// field notes
					if (textArea.getText().equals("")) {
						admission.setNote(null);
					} else {
						admission.setNote(textArea.getText());
					}

					
					// get transfusional unit (it can be null)
					try {
						if (trsfUnitField.getText().equals("")) {
							admission.setTransUnit(new Float(0.0f));
						} else {
							float f = Float.parseFloat(trsfUnitField.getText());
							if (f < 0.0f) {
								JOptionPane
										.showMessageDialog(null,
												MessageBundle.getMessage("angal.admission.pleaseinsertavalidunitvalue"));
								return;
							} else {
								admission.setTransUnit(new Float(f));
								// System.out.println("weight is "+f);
							}
						}
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null,
								MessageBundle.getMessage("angal.admission.pleaseinsertavalidunitvalue"));
						return;
					}
					
					
					// fields for pregnancy status
					if (isPregnancy) {

						// get visit date (may be null)
						//d = visitDateField.getText().trim();
						if (visitDateFieldCal.getDate()!=null) {
							d = currentDateFormat.format(visitDateFieldCal.getDate());
						} else d="";
						if (d.equals("")) {
							//System.out.println("visit is null");
							admission.setVisitDate(null);
						} 
						else {
							try {
								Date date = currentDateFormat.parse(d);
								visitDate = new GregorianCalendar();
								visitDate.setTime(date);
								// updateDisplay
								d = currentDateFormat.format(date);
								//visitDateField.setText(d);
								visitDateFieldCal.setDate(currentDateFormat.parse(d));

								GregorianCalendar limit;
								if (admission.getDisDate() == null) {
									limit = today;
								} else {
									limit = admission.getDisDate();
								}

								if (visitDate.before(dateIn)
										|| visitDate.after(limit)) {
									JOptionPane.showMessageDialog(null,
											MessageBundle.getMessage("angal.admission.pleaseinsertavalidvisitdateplease"));
									return;
								}

								admission.setVisitDate(visitDate);
							} catch (ParseException pe) {
								JOptionPane.showMessageDialog(null,
										MessageBundle.getMessage("angal.admission.pleaseinsertavalidvisitdateplease"));
								visitDateField.setText(currentDateFormat
										.format(visitDate.getTime()));//CONTROLLARE
								return;
							} catch (IllegalArgumentException iae) {
								JOptionPane.showMessageDialog(null,
										MessageBundle.getMessage("angal.admission.pleaseinsertavalidvisitdateplease"));
								visitDateField.setText(currentDateFormat
										.format(visitDate.getTime()));//CONTROLLARE
								return;
							}
						}// else

						// get weight (it can be null)
						try {
							if (weightField.getText().equals("")) {
								admission.setWeight(null);
							} else {
								float f = Float.parseFloat(weightField
										.getText());
								if (f < 0.0f) {
									JOptionPane
											.showMessageDialog(null,
													MessageBundle.getMessage("angal.admission.pleaseinsertavalidweightvalue"));
									return;
								} else {
									admission.setWeight(new Float(f));
									// System.out.println("weight is "+f);
								}
							}
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(null,
									MessageBundle.getMessage("angal.admission.pleaseinsertavalidweightvalue"));
							return;
						}

						// get treatment type(may be null)
						if (treatmTypeBox.getSelectedIndex() == 0) {
							admission.setPregTreatmentType(null);
							/*
							 * JOptionPane.showMessageDialog(null, "please
							 * select a valid pregnancy treatment type");
							 * return;
							 */
						} else {
							admission.setPregTreatmentType(treatmTypeList.get(
									treatmTypeBox.getSelectedIndex() - 1)
									.getCode());

						}

						// get delivery date
						//d = deliveryDateField.getText().trim();
						if (deliveryDateFieldCal.getDate()!=null) {
							d = currentDateFormat.format(deliveryDateFieldCal.getDate());
						} else d="";
						
						if (d.equals("")) {
							admission.setDeliveryDate(null);
							// System.out.println("delivery date out is null");
						} else {
							try {
								Date date = currentDateFormat.parse(d);
								deliveryDate = new GregorianCalendar();
								deliveryDate.setTime(date);

								// date control

								GregorianCalendar start;
								if (admission.getVisitDate() == null) {
									start = admission.getAdmDate();
								} else {
									start = admission.getVisitDate();
								}

								GregorianCalendar limit;
								if (admission.getDisDate() == null) {
									limit = today;
								} else {
									limit = admission.getDisDate();
								}

								if (deliveryDate.before(start)
										|| deliveryDate.after(limit)) {
									JOptionPane
											.showMessageDialog(null,
													MessageBundle.getMessage("angal.admission.pleaseinsertavaliddeliverydate"));
									//deliveryDateField.setText("");
									deliveryDateFieldCal.setDate(null);
									return;
								}

								// updateDisplay
								d = currentDateFormat.format(date);
								//deliveryDateField.setText(d);
								deliveryDateFieldCal.setDate(currentDateFormat.parse(d));
								admission.setDeliveryDate(deliveryDate);

							} catch (ParseException pe) {
								JOptionPane.showMessageDialog(null,
										MessageBundle.getMessage("angal.admission.pleaseinsertavaliddeliverydate"));
								deliveryDateField.setText("");//CONTROLLARE
								return;
							} catch (IllegalArgumentException iae) {
								JOptionPane.showMessageDialog(null,
										MessageBundle.getMessage("angal.admission.pleaseinsertavaliddeliverydate"));
								deliveryDateField.setText("");//CONTROLLARE
								return;
							}
						}

						// get delivery type

						if (deliveryTypeBox.getSelectedIndex() == 0) {

							admission.setDeliveryTypeId(null);
							// System.out.println("deliveryType is null");
						} else {
							admission.setDeliveryTypeId(deliveryTypeList.get(
									deliveryTypeBox.getSelectedIndex() - 1)
									.getCode());
						}

						// get delivery result type

						if (deliveryResultTypeBox.getSelectedIndex() == 0) {

							admission.setDeliveryResultId(null);
							// System.out.println("deliveryType is null");
						} else {
							admission
									.setDeliveryResultId(deliveryResultTypeList
											.get(
													deliveryResultTypeBox
															.getSelectedIndex() - 1)
											.getCode());
						}

						// get ctrl1 date
						//d = ctrl1DateField.getText().trim();
						if (ctrl1DateFieldCal.getDate()!=null) {
							d = currentDateFormat.format(ctrl1DateFieldCal.getDate());
						} else d="";

						if (d.equals("")) {
							admission.setCtrlDate1(null);
							// System.out.println("ctrl1 date out is null");
						} else {
							try {
								Date date = currentDateFormat.parse(d);
								ctrl1Date = new GregorianCalendar();
								ctrl1Date.setTime(date);

								// date control

								if (admission.getDeliveryDate() == null) {
									JOptionPane
											.showMessageDialog(null,
													MessageBundle.getMessage("angal.admission.controln1datenodeliverydatefound"));
									return;
								}
								GregorianCalendar limit;
								if (admission.getDisDate() == null) {
									limit = today;
								} else {
									limit = admission.getDisDate();
								}

								if (ctrl1Date.before(deliveryDate)
										|| ctrl1Date.after(limit)) {
									JOptionPane
											.showMessageDialog(null,
													MessageBundle.getMessage("angal.admission.pleaseinsertavalidcontroln1date"));									return;
								}

								// updateDisplay
								d = currentDateFormat.format(date);
								//ctrl1DateField.setText(d);
								ctrl1DateFieldCal.setDate(currentDateFormat.parse(d));
								admission.setCtrlDate1(ctrl1Date);

							} catch (ParseException pe) {
								JOptionPane
										.showMessageDialog(null,
												MessageBundle.getMessage("angal.admission.pleaseinsertavalidcontroln1date"));
								//ctrl1DateField.setText("");
								ctrl1DateFieldCal.setDate(null);
								return;
							} catch (IllegalArgumentException iae) {
								JOptionPane
										.showMessageDialog(null,
												MessageBundle.getMessage("angal.admission.pleaseinsertavalidcontroln1date"));
								//ctrl1DateField.setText("");
								ctrl1DateFieldCal.setDate(null);
								return;
							}
						}

						// get ctrl2 date
						//d = ctrl2DateField.getText().trim();
						if (ctrl2DateFieldCal.getDate()!=null) {
							d = currentDateFormat.format(ctrl2DateFieldCal.getDate());
						} else d="";

						if (d.equals("")) {
							admission.setCtrlDate2(null);
							// System.out.println("ctrl2 date is null");
						} else {
							if (admission.getCtrlDate1() == null) {
								JOptionPane
										.showMessageDialog(null,
												MessageBundle.getMessage("angal.admission.controldaten2controldaten1notfound"));
								//ctrl2DateField.setText("");
								ctrl2DateFieldCal.setDate(null);
								return;
							}
							try {
								Date date = currentDateFormat.parse(d);
								ctrl2Date = new GregorianCalendar();
								ctrl2Date.setTime(date);

								// date control
								GregorianCalendar limit;
								if (admission.getDisDate() == null) {
									limit = today;
								} else {
									limit = admission.getDisDate();
								}
								if (ctrl2Date.before(ctrl1Date)
										|| ctrl2Date.after(limit)) {
									JOptionPane
											.showMessageDialog(null,
													MessageBundle.getMessage("angal.admission.pleaseinsertavalidcontroln2date"));
									return;
								}

								// updateDisplay
								d = currentDateFormat.format(date);
								//ctrl2DateField.setText(d);
								ctrl2DateFieldCal.setDate(currentDateFormat.parse(d));
								admission.setCtrlDate2(ctrl2Date);

							} catch (ParseException pe) {
								JOptionPane
										.showMessageDialog(null,
												MessageBundle.getMessage("angal.admission.pleaseinsertavalidcontroln2date"));
								//ctrl2DateField.setText("");
								ctrl2DateFieldCal.setDate(null);
								return;
							} catch (IllegalArgumentException iae) {
								JOptionPane
										.showMessageDialog(null,
												MessageBundle.getMessage("angal.admission.pleaseinsertavalidcontroln2date"));
								//ctrl2DateField.setText("");
								ctrl2DateFieldCal.setDate(null);
								return;
							}
						}

						// get abort date
						//d = abortDateField.getText().trim();
						if (abortDateFieldCal.getDate()!=null) {
							d = currentDateFormat.format(abortDateFieldCal.getDate());
						} else d="";

						if (d.equals("")) {
							admission.setAbortDate(null);
							// System.out.println("abort date is null");
						} else {
							try {
								Date date = currentDateFormat.parse(d);
								abortDate = new GregorianCalendar();
								abortDate.setTime(date);

								// date control
								GregorianCalendar limit;
								if (admission.getDisDate() == null) {
									limit = today;
								} else {
									limit = admission.getDisDate();
								}
								if (ctrl2Date != null
										&& abortDate.before(ctrl2Date)
										|| ctrl1Date != null
										&& abortDate.before(ctrl1Date)
										|| abortDate.before(visitDate)
										|| abortDate.after(limit)) {
									JOptionPane.showMessageDialog(null,
											MessageBundle.getMessage("angal.admission.pleaseinsertavalidabortdate"));
									return;
								}

								// updateDisplay
								d = currentDateFormat.format(date);
								//abortDateField.setText(d);
								abortDateFieldCal.setDate(currentDateFormat.parse(d));
								admission.setAbortDate(abortDate);

							} catch (ParseException pe) {
								JOptionPane.showMessageDialog(null,
										MessageBundle.getMessage("angal.admission.pleaseinsertavalidabortdate"));
								//abortDateField.setText("");
								abortDateFieldCal.setDate(null);
								return;
							} catch (IllegalArgumentException iae) {
								JOptionPane.showMessageDialog(null,
										MessageBundle.getMessage("angal.admission.pleaseinsertavalidabortdate"));
								//abortDateField.setText("");
								abortDateFieldCal.setDate(null);
								return;
							}
						}

					}// isPregnancy

					// set not editable fields

					admission.setPatId(patient.getCode());

					if (admission.getDisDate() == null) {
						admission.setAdmitted(1);
					} else {
						admission.setAdmitted(0);
					}

					if (malnuCheck.isSelected()) {
						admission.setType("M");
					} else {
						admission.setType("N");
					}

					admission.setDeleted("N");

					// IOoperation result
					boolean result = false;
					
					//ready to save...
					
					AdmissionBrowserManager abm = new AdmissionBrowserManager();
					if (!editing && !isDischarge) {
						// System.out.println("New Admission...");
						result = abm.newAdmission(admission);
						if (result) {
							fireAdmissionInserted(admission);
							dispose();
						}
					} else if (!editing && isDischarge) {
						// System.out.println("New Admission with
						// discharge...");
						result = abm.newAdmission(admission);
						if (result) {
							fireAdmissionUpdated(isDischarge);
							dispose();
						}
					} else {
						// System.out.println("Update Admission or
						// Discharge...");
						result = abm.updateAdmission(admission);
						if (result) {
							// System.out.println("Update Admission");
							fireAdmissionUpdated(isDischarge);
							dispose();
						}
					}

					if (!result) {
						JOptionPane.showMessageDialog(null,
								MessageBundle.getMessage("angal.admission.thedatacouldnotbesaved"));
					}

					else {
						dispose();
					}
				}
			});
		}
		return saveButton;
	}

}// class
