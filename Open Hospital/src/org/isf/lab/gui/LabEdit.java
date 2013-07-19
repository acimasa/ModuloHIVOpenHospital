
/*------------------------------------------
 * LabEdit - Add/edit a laboratory exam
 * -----------------------------------------
 * modification history
 * 02/03/2006 - Davide - first beta version
 * 03/11/2006 - ross - changed title, enlarged window
 *                   - changed note from textfield to textarea
 * 			         - version is now 1.0 
 * 08/11/2006 - ross - added age, sex, exam date, material
 *                   - added editing capability 
 * 18/08/2008 - Teo  - Add scroll capabilities at note JTextArea
 * 13/02/2009 - Alex - add calendar
 *------------------------------------------*/

package org.isf.lab.gui;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.border.EtchedBorder;
import javax.swing.event.EventListenerList;

import org.isf.admission.manager.AdmissionBrowserManager;
import org.isf.exa.manager.ExamBrowsingManager;
import org.isf.exa.manager.ExamRowBrowsingManager;
import org.isf.exa.model.Exam;
import org.isf.exa.model.ExamRow;
import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.lab.manager.LabManager;
import org.isf.lab.manager.LabRowManager;
import org.isf.lab.model.Laboratory;
import org.isf.lab.model.LaboratoryRow;
import org.isf.patient.manager.PatientBrowserManager;
import org.isf.patient.model.Patient;
import org.isf.utils.jobjects.VoLimitedTextField;
import org.isf.utils.time.RememberDates;

import com.toedter.calendar.JDateChooser;

public class LabEdit extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1055379190540460482L;

	//LISTENER INTERFACE --------------------------------------------------------
	private EventListenerList labEditListener = new EventListenerList();
	
	public interface LabEditListener extends EventListener {
		public void labUpdated();
	}
	
	public void addLabEditListener(LabEditListener l) {
		labEditListener.add(LabEditListener.class, l);
		
	}
	
	private void fireLabUpdated() {
		new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};
		
		EventListener[] listeners = labEditListener.getListeners(LabEditListener.class);
		for (int i = 0; i < listeners.length; i++)
			((LabEditListener)listeners[i]).labUpdated();
	}
	//---------------------------------------------------------------------------
	
	private static final String VERSION=MessageBundle.getMessage("angal.versione"); 
	
	private boolean insert = false;

	private Laboratory lab = null;
	private JPanel jContentPane = null;
	private JPanel buttonPanel = null;
	private JPanel dataPanel = null;
	private JPanel resultPanel = null;
	private JLabel examLabel = null;
	private JLabel noteLabel = null;
	private JLabel patientLabel = null;
	private JCheckBox inPatientCheckBox = null;
	private JLabel nameLabel = null;
	private JLabel ageLabel = null;
	private JLabel sexLabel = null;
	private JLabel examDateLabel = null;
	private JLabel matLabel = null;
	private JButton okButton = null;
	private JButton cancelButton = null;
	private JComboBox matComboBox = null;
	private JComboBox examComboBox = null;
	private JComboBox examRowComboBox = null;
	private JComboBox patientComboBox = null;
	private Exam examSelected = null;
	private JScrollPane noteScrollPane = null;

	private JTextArea noteTextArea = null;

	private VoLimitedTextField patTextField = null;
	private VoLimitedTextField ageTextField = null;
	private VoLimitedTextField sexTextField = null;

//	private VoDateTextField examDateField = null;
	private JDateChooser examDateFieldCal = null;
	private DateFormat currentDateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALIAN);
	private GregorianCalendar dateIn = null;

	
	private static final Integer panelWidth=500; 
	private static final Integer labelWidth=50; 
	private static final Integer dataPanelHeight=150; 
	private static final Integer resultPanelHeight=350; 
	private static final Integer buttonPanelHeight=40; 

	
	private ArrayList<ExamRow> eRows = null;

	public LabEdit(JFrame owner, Laboratory laboratory, boolean inserting) {
		super(owner, true);
		insert = inserting;
		lab = laboratory;
		initialize();
	}

	private void initialize() {

		this.setBounds(30,30,panelWidth+20,dataPanelHeight+resultPanelHeight+buttonPanelHeight+30);
		this.setContentPane(getJContentPane());
		this.setResizable(false);
		if (insert) {
			this.setTitle(MessageBundle.getMessage("angal.lab.newlaboratoryexam")+"("+VERSION+")");
		} else {
			this.setTitle(MessageBundle.getMessage("angal.lab.editlaboratoryexam")+"("+VERSION+")");
		}
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//this.setVisible(true);
	}


	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			// data panel
			jContentPane.add(getDataPanel());
			resultPanel = new JPanel();
			resultPanel.setBounds(0, dataPanelHeight, panelWidth, resultPanelHeight);
			if (!insert) {
				examSelected = lab.getExam();
				if (examSelected.getProcedure() == 1)
					resultPanel = getFirstPanel();
				else if (examSelected.getProcedure() == 2)
					resultPanel = getSecondPanel();
			}
			resultPanel.setBorder(BorderFactory.createTitledBorder(
					BorderFactory.createLineBorder(Color.GRAY), MessageBundle.getMessage("angal.lab.result")));
			jContentPane.add(resultPanel);
			jContentPane.add(getButtonPanel()); // Generated
		}
		return jContentPane;
	}

	private JPanel getDataPanel() {
		if (dataPanel == null) {
			//initialize data panel
			dataPanel = new JPanel();
			dataPanel.setLayout(null);
			dataPanel.setBounds(0, 0, panelWidth, dataPanelHeight);
			//exam date
			examDateLabel = new JLabel(MessageBundle.getMessage("angal.lab.date"));
			examDateLabel.setBounds(5, 10, labelWidth, 20);
			//examDateField=getExamDateField();
			//examDateField.setBounds(labelWidth+5, 10, 70, 20);
			examDateFieldCal = getExamDateFieldCal();
			examDateFieldCal.setLocale(new Locale(GeneralData.LANGUAGE));
			examDateFieldCal.setDateFormatString("dd/MM/yy");
			examDateFieldCal.setBounds(labelWidth+5, 10, 90, 20);
			//material
			matLabel = new JLabel(MessageBundle.getMessage("angal.lab.material"));
			matLabel.setBounds(155, 10, labelWidth, 20);
			matComboBox= getMatComboBox();
			matComboBox.setBounds(215, 10, 280, 20);
			//exam combo
			examLabel = new JLabel(MessageBundle.getMessage("angal.lab.exam"));
			examLabel.setBounds(5, 35, labelWidth, 20);
			examComboBox=getExamComboBox();
			examComboBox.setBounds(labelWidth+5, 35, 440, 20);

			//patient (in or out) data
			patientLabel = new JLabel(MessageBundle.getMessage("angal.lab.patient"));
			patientLabel.setBounds(5, 60, labelWidth, 20);
			inPatientCheckBox = getInPatientCheckBox();
			inPatientCheckBox.setBounds(labelWidth+5, 60, labelWidth, 20);
			patientComboBox=getPatientComboBox();
			patientComboBox.setBounds((labelWidth+5)*2, 60, 385, 20);

			nameLabel = new JLabel(MessageBundle.getMessage("angal.lab.name"));
			nameLabel.setBounds(5, 85, labelWidth, 20);
			patTextField=getPatientTextField();
			patTextField.setBounds(labelWidth+5, 85, 180, 20);
			ageLabel = new JLabel(MessageBundle.getMessage("angal.lab.age"));
			ageLabel.setBounds(250, 85, 35, 20);
			ageTextField=getAgeTextField();
			ageTextField.setBounds(290, 85, 50, 20);
			sexLabel = new JLabel(MessageBundle.getMessage("angal.lab.sexmf"));
			sexLabel.setBounds(375, 85, 80, 20);
			sexTextField=getSexTextField();
			sexTextField.setBounds(445, 85, 50, 20);
			//note			
			noteLabel = new JLabel(MessageBundle.getMessage("angal.lab.note"));
			noteLabel.setBounds(5, 110, labelWidth, 20);
			noteTextArea=getNoteTextArea();
			noteTextArea.setBounds(labelWidth+5, 110, 440, 40);
			noteTextArea.setEditable(true);
			noteTextArea.setWrapStyleWord(true);
			noteTextArea.setAutoscrolls(true);
			
			//add all to the data panel
			dataPanel.add(examDateLabel, null);
			dataPanel.add(examDateFieldCal, null);
			dataPanel.add(matLabel, null);
			dataPanel.add(matComboBox, null);
			dataPanel.add(examLabel, null);
			dataPanel.add(examComboBox, null);
			dataPanel.add(patientLabel, null);
			dataPanel.add(inPatientCheckBox,null);
			dataPanel.add(patientComboBox,null);
			dataPanel.add(nameLabel, null);
			dataPanel.add(patTextField);
			dataPanel.add(ageLabel, null);
			dataPanel.add(ageTextField);
			dataPanel.add(sexLabel, null);
			dataPanel.add(sexTextField);
			dataPanel.add(noteLabel, null);
			
			dataPanel.setPreferredSize(new Dimension(150,200));
			
			/*
			 * Teo : Adding scroll capabilities at note textArea
			 */
			if(noteScrollPane == null)
			{
				noteScrollPane = new JScrollPane(noteTextArea);
				noteScrollPane.setBounds(labelWidth+5, 110, 440, 40);
				noteScrollPane.createVerticalScrollBar();
				noteScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				noteScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				noteScrollPane.setAutoscrolls(true);
				dataPanel.add(noteScrollPane);
			}
			
		}
		return dataPanel;
	}

	
// REPLACED BY CALENDAR	
//	private VoDateTextField getExamDateField() {
//		String d = "";
//		java.util.Date myDate = null;
//		if (insert)
//			dateIn=RememberDates.getLastLabExamDateGregorian();
//		 else 
//			dateIn  = lab.getExamDate();
//
//		if (dateIn!=null) {
//			myDate = dateIn.getTime();
//			d = currentDateFormat.format(myDate);
//		}
//		return (new VoDateTextField("dd/mm/yy", d, 15));
//	}

	private JDateChooser getExamDateFieldCal() {
		java.util.Date myDate = null;
		if (insert) {
			dateIn = RememberDates.getLastLabExamDateGregorian();
		} else { 
			dateIn = lab.getExamDate();
		}
		if (dateIn != null) {
			myDate = dateIn.getTime();
		}
		return (new JDateChooser(myDate, "dd/MM/yy"));
	}
	
	private JCheckBox getInPatientCheckBox() {
		if (inPatientCheckBox == null) {
			inPatientCheckBox = new JCheckBox(MessageBundle.getMessage("angal.lab.in"));
			if (!insert)
				inPatientCheckBox.setSelected(lab.getInOutPatient().equalsIgnoreCase("I"));
			lab.setInOutPatient((inPatientCheckBox.isSelected()?"I":"R"));
		}
		return inPatientCheckBox;
	}

	private JComboBox getPatientComboBox() {
		if (patientComboBox == null) {
			patientComboBox = new JComboBox();
			Patient patSelected=null;
			PatientBrowserManager patBrowser = new PatientBrowserManager();
			ArrayList<Patient> pat = patBrowser.getPatient();
			patientComboBox.addItem(MessageBundle.getMessage("angal.lab.selectapatient"));
			for (Patient elem : pat) {
				if (!insert) {
					if (elem.getCode()==lab.getPatId()) {
						patSelected=elem;
					}
				}
				patientComboBox.addItem(elem);
			}
			if (patSelected!=null)
				patientComboBox.setSelectedItem(patSelected);
			
			patientComboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (patientComboBox.getSelectedIndex()>0) {
						AdmissionBrowserManager admMan = new AdmissionBrowserManager();
						Patient pat=(Patient)patientComboBox.getSelectedItem();
						patTextField.setText(pat.getName());
						ageTextField.setText(pat.getAge()+"");
						sexTextField.setText(pat.getSex()+"");
						inPatientCheckBox.setSelected(admMan.getCurrentAdmission(pat) != null ? true : false);
					}
				}
			});
		}
		return patientComboBox;
	}

	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.setBounds(0, dataPanelHeight+resultPanelHeight, panelWidth, buttonPanelHeight);
			buttonPanel.add(getOkButton(), null);
			buttonPanel.add(getCancelButton(), null);
		}
		return buttonPanel;
	}

	private JComboBox getExamComboBox() {
		if (examComboBox == null) {
			examComboBox = new JComboBox();
			Exam examSel=null;
			ExamBrowsingManager manager = new ExamBrowsingManager();
			ArrayList<Exam> exams = manager.getExams();
			examComboBox.addItem(MessageBundle.getMessage("angal.lab.selectanexam"));
			for (Exam elem : exams) {
				if (!insert && elem.getCode()!=null) {
					if (elem.getCode().equalsIgnoreCase((lab.getExam().getCode()))) {
						examSel=elem;
					}
				}
				examComboBox.addItem(elem);
			}
			examComboBox.setSelectedItem(examSel);
			
			examComboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (!(examComboBox.getSelectedItem() instanceof String)) {
						examSelected = (Exam) examComboBox
								.getSelectedItem();

						if (examSelected.getProcedure() == 1)
							resultPanel = getFirstPanel();
						else if (examSelected.getProcedure() == 2)
							resultPanel = getSecondPanel();

						validate();
						repaint();
					}
				}
			});
			resultPanel = null;
		}
		return examComboBox;
	}

	
	private JComboBox getMatComboBox() {
		if (matComboBox == null) {
			matComboBox = new JComboBox();
			matComboBox.addItem("");
			matComboBox.addItem(MessageBundle.getMessage("angal.lab.blood"));
			matComboBox.addItem(MessageBundle.getMessage("angal.lab.urine"));
			matComboBox.addItem(MessageBundle.getMessage("angal.lab.stool"));
			matComboBox.addItem(MessageBundle.getMessage("angal.lab.sputum"));
			matComboBox.addItem(MessageBundle.getMessage("angal.lab.cfs"));
			matComboBox.addItem(MessageBundle.getMessage("angal.lab.swabs"));
			matComboBox.addItem(MessageBundle.getMessage("angal.lab.tissues"));
			if (!insert) {
				try {	
					matComboBox.setSelectedItem(lab.getMaterial());
					}
				catch (Exception e) {}
			}
		}
		return matComboBox;
	}

	
	//prova per gestire un campo note al posto di uno volimited
	private JTextArea getNoteTextArea() {
		if (noteTextArea == null) {
			noteTextArea = new JTextArea(10,30);
			if (!insert){
				noteTextArea.setText(lab.getNote());
			}
			noteTextArea.setLineWrap(true);
			noteTextArea.setPreferredSize(new Dimension(10,30));
			noteTextArea.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		}
		return noteTextArea;
	}
	
	
	
	private VoLimitedTextField getPatientTextField() {
		if (patTextField == null) {
			patTextField = new VoLimitedTextField(100);
			if (!insert) {
				patTextField.setText(lab.getPatName());
			}
		}
		return patTextField;
	}

	
	private VoLimitedTextField getAgeTextField() {
		if (ageTextField == null) {
			ageTextField = new VoLimitedTextField(3);
			if (insert) {
				ageTextField.setText("");
				}
			else {
				try {	
					Integer intAge=lab.getAge();
					ageTextField.setText(intAge.toString());
					}
				catch (Exception e) {
					ageTextField.setText("");
					}
				}
			}
		return ageTextField;
	}
	
	private VoLimitedTextField getSexTextField() {
		if (sexTextField == null) {
			sexTextField = new VoLimitedTextField(1);
			if (!insert) {
				sexTextField.setText(lab.getSex());
			}
		}
		return sexTextField;
	}
	
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText(MessageBundle.getMessage("angal.lab.cancel"));
			cancelButton.setMnemonic(KeyEvent.VK_C);
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return cancelButton;
	}

	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setText(MessageBundle.getMessage("angal.lab.ok"));
			okButton.setMnemonic(KeyEvent.VK_O);
			okButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (examComboBox.getSelectedIndex() == 0) {
						JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.lab.pleaseselectanexam"));
						return;
					}
					//materiale
					if (matComboBox.getSelectedIndex() == 0) {
						JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.lab.pleaseselectamaterial"));
						return;
					}
					String matSelected=(String)matComboBox.getSelectedItem();
					examSelected=(Exam)examComboBox.getSelectedItem();
					// exam  date	
					String d = currentDateFormat.format(examDateFieldCal.getDate());
					GregorianCalendar gregDate = new GregorianCalendar();
					gregDate.setTime(examDateFieldCal.getDate());
					if (d.equals("")) {
						JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.lab.pleaseinsertexamdate"));
						return;
					}
					try {
						currentDateFormat.setLenient(false);
						Date myDate = currentDateFormat.parse(d);
						gregDate.setTime(myDate);
					} catch (ParseException pe) {
						System.out.println(pe);
						JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.lab.pleaseinsertavalidexamdate"));
						//dateOutField.setText("");
						return;
					}
					//patient id
					Integer patId=-1;
					if (patientComboBox.getSelectedIndex()>0)
						patId=((Patient)(patientComboBox.getSelectedItem())).getCode();
					String sex=sexTextField.getText().toUpperCase();
					if (!(sex.equals("M") || sex.equals("F"))) {
						JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.lab.pleaseinsertmformaleorfforfemale"));
						return;
					}
					
					ArrayList<String> labRow = new ArrayList<String>();
					LabManager manager = new LabManager();
					lab.setDate(new GregorianCalendar());
					lab.setExamDate(gregDate);
					RememberDates.setLastLabExamDate(gregDate);
					lab.setMaterial(matSelected);
					lab.setExam(examSelected);
					lab.setNote(noteTextArea.getText());
					lab.setInOutPatient((inPatientCheckBox.isSelected()?"I":"R"));
					lab.setPatId(patId);
					lab.setPatName(patTextField.getText());
					int tmpAge=0;
					try {
						tmpAge=Integer.parseInt(ageTextField.getText());
					}
					catch (Exception ex) {
					}
					lab.setAge(tmpAge);
					lab.setSex(sexTextField.getText());
					
					if (examSelected.getProcedure() == 1)
						lab.setResult(examRowComboBox.getSelectedItem()
								.toString());
					else if (examSelected.getProcedure() == 2) {
						lab.setResult(MessageBundle.getMessage("angal.lab.multipleresults"));
						for (int i = 0; i < resultPanel.getComponentCount(); i++) {
							if (((SubPanel) resultPanel.getComponent(i))
									.getSelectedResult().equalsIgnoreCase("P")) {
								labRow.add(eRows.get(i).getDescription());
							}
						}
					}
					boolean result = false;
					if (insert) {
						if (examSelected.getProcedure() == 1)
							result = manager.newLabFirstProcedure(lab);
						else if (examSelected.getProcedure() == 2)
							result = manager.newLabSecondProcedure(lab,	labRow);
					} else {
						if (examSelected.getProcedure() == 1)
							result = manager.editLabFirstProcedure(lab);
						else if (examSelected.getProcedure() == 2)
							result = manager.editLabSecondProcedure(lab,	labRow);
					}

					if (!result)
						JOptionPane.showMessageDialog(null,
								MessageBundle.getMessage("angal.lab.thedatacouldnotbesaved"));
					else {
						fireLabUpdated();
						dispose();
					}
				}
			});
		}
		return okButton;
	}

	private JPanel getFirstPanel() {
		resultPanel.removeAll();
		String result="";
		examRowComboBox = new JComboBox();
		examRowComboBox.setMaximumSize(new Dimension(200, 25));
		examRowComboBox.setMinimumSize(new Dimension(200, 25));
		examRowComboBox.setPreferredSize(new Dimension(200, 25));
		if (insert) {
			result=examSelected.getDefaultResult();
		} else {
			result=lab.getResult();
		}
		examRowComboBox.addItem(result);

		ExamRowBrowsingManager rowManager = new ExamRowBrowsingManager();
		ArrayList<ExamRow> rows = rowManager.getExamRow(examSelected.getCode());
		for (ExamRow r : rows) {
			if (!r.getDescription().equals(result))
				examRowComboBox.addItem(r.getDescription());
		}
		resultPanel.add(examRowComboBox);

		return resultPanel;
	}

	private JPanel getSecondPanel() {
		resultPanel.removeAll();
		resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
		String examId = examSelected.getCode();
		ExamRowBrowsingManager eRowManager = new ExamRowBrowsingManager();
		eRows = null;
		eRows = eRowManager.getExamRow(examId);
		if (insert) {
			for (ExamRow r : eRows)
				resultPanel.add(new SubPanel(r, "N"));
		} else {
			LabRowManager lRowManager = new LabRowManager();

			ArrayList<LaboratoryRow> lRows = lRowManager.getLabRow(lab
					.getCode());
			boolean find;
			for (ExamRow r : eRows) {
				find = false;
				for (LaboratoryRow lR : lRows) {
					if (r.getDescription()
							.equalsIgnoreCase(lR.getDescription()))
						find = true;
				}
				if (find) {
					resultPanel.add(new SubPanel(r, "P"));
				} else {
					resultPanel.add(new SubPanel(r, "N"));
				}
			}
		}
		return resultPanel;
	}

	class SubPanel extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private JLabel label = null;

		private JRadioButton radioPos = null;

		private JRadioButton radioNeg = null;

		private ButtonGroup group = null;

		public SubPanel(ExamRow row, String result) {
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			label = new JLabel(row.getDescription());
			this.add(label);

			group = new ButtonGroup();
			radioPos = new JRadioButton(MessageBundle.getMessage("angal.lab.p"));
			radioNeg = new JRadioButton(MessageBundle.getMessage("angal.lab.n"));
			group.add(radioPos);
			group.add(radioNeg);

			this.add(radioPos);
			this.add(radioNeg);
			if (result.equals(MessageBundle.getMessage("angal.lab.p")))
				radioPos.setSelected(true);
			else
				radioNeg.setSelected(true);
		}

		public String getSelectedResult() {
			if (radioPos.isSelected())
				return MessageBundle.getMessage("angal.lab.p");
			else
				return MessageBundle.getMessage("angal.lab.n");
		}

	}

}
