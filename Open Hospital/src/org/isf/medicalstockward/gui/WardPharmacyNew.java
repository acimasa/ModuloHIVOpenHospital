package org.isf.medicalstockward.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.GregorianCalendar;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.isf.generaldata.MessageBundle;
import org.isf.medicals.model.Medical;
import org.isf.medicalstockward.manager.MovWardBrowserManager;
import org.isf.medicalstockward.model.MedicalWard;
import org.isf.medicalstockward.model.MovementWard;
import org.isf.patient.gui.SelectPatient;
import org.isf.patient.gui.SelectPatient.SelectionListener;
import org.isf.patient.model.Patient;
import org.isf.utils.jobjects.ModalJFrame;
import org.isf.ward.model.Ward;

public class WardPharmacyNew extends ModalJFrame implements SelectionListener {

//LISTENER INTERFACE --------------------------------------------------------
    private EventListenerList movementWardListeners = new EventListenerList();
	
	public interface MovementWardListeners extends EventListener {
		public void movementUpdated(AWTEvent e);
		public void movementInserted(AWTEvent e);
	}
	
	public void addMovementWardListener(MovementWardListeners l) {
		movementWardListeners.add(MovementWardListeners.class, l);
	}
	
	public void removeMovementWardListener(MovementWardListeners listener) {
		movementWardListeners.remove(MovementWardListeners.class, listener);
	}
	
	private void fireMovementWardInserted() {
		AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};
		
		EventListener[] listeners = movementWardListeners.getListeners(MovementWardListeners.class);
		for (int i = 0; i < listeners.length; i++)
			((MovementWardListeners)listeners[i]).movementInserted(event);
	}
	/*private void fireMovementWardUpdated() {
		AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 1L;};
		
		EventListener[] listeners = movementWardListeners.getListeners(MovementWardListeners.class);
		for (int i = 0; i < listeners.length; i++)
			((MovementWardListeners)listeners[i]).movementUpdated(event);
	}*/
//---------------------------------------------------------------------------
	
	public void patientSelected(Patient patient) {
		patientSelected = patient;
		jTextFieldPatient.setText(patientSelected.getName());
		jTextFieldPatient.setEditable(false);
		jButtonPickPatient.setText(MessageBundle.getMessage("angal.medicalstockwardedit.changepatient")); //$NON-NLS-1$
		jButtonPickPatient.setToolTipText(MessageBundle.getMessage("angal.medicalstockwardedit.changethepatientassociatedwiththismovement")); //$NON-NLS-1$
		jButtonTrashPatient.setEnabled(true);
		if (patientSelected.getWeight() == 0) {
			JOptionPane.showMessageDialog(WardPharmacyNew.this, MessageBundle.getMessage("angal.medicalstockwardedit.theselectedpatienthasnoweightdefined"));
		}
	}
	
	private static final long serialVersionUID = 1L;
	private JLabel jLabelPatient;
	private JTextField jTextFieldPatient;
	private JButton jButtonPickPatient;
	private JButton jButtonTrashPatient;
	private JPanel jPanelPatient;
	private JPanel jPanelMedicals;
	private JPanel jPanelButtons;
	private JPanel jPanelNorth;
	private JPanel jPanelUse;
	private JButton jButtonOK;
	private JButton jButtonCancel;
	private JRadioButton jRadioPatient;
	private JTable jTableMedicals;
	private JScrollPane jScrollPaneMedicals;
	private JPanel jPanelMedicalsButtons;
	private JButton jButtonAddMedical;
	private JButton jButtonRemoveMedical;
	private static final Dimension PatientDimension = new Dimension(300,20);

	private Patient patientSelected = null;
	private Ward wardSelected;
	private Object[] medClasses = {Medical.class, Integer.class};
	private String[] medColumnNames = {MessageBundle.getMessage("angal.medicalstockward.medical"), 
									   MessageBundle.getMessage("angal.medicalstockward.quantity")};
	private Integer[] medWidth = {200, 150};
	private boolean[] medResizable = {true, false};
	
	//Medicals (ALL)
	//MedicalBrowsingManager medManager = new MedicalBrowsingManager();
	//ArrayList<Medical> medArray = medManager.getMedicals();

	//Medicals (in WARD)
	//ArrayList<MedItem> medItems = new ArrayList<MedItem>();
	private ArrayList<Medical> medArray = new ArrayList<Medical>();
	private ArrayList<Double> qtyArray = new ArrayList<Double>(); 
	private ArrayList<MedicalWard> wardDrugs = null;
	private ArrayList<MedicalWard> medItems = new ArrayList<MedicalWard>();
	private JRadioButton jRadioUse;
	private JTextField jTextFieldUse;
	private JLabel jLabelUse;

	public WardPharmacyNew(Ward ward, ArrayList<MedicalWard> drugs) {
		wardDrugs = drugs;
		for (MedicalWard elem : wardDrugs) {
			medArray.add(elem.getMedical());
			qtyArray.add(elem.getQty());
		}
		wardSelected = ward;
		initComponents();
	}

	private void initComponents() {
		add(getJPanelButtons(), BorderLayout.SOUTH);
		add(getJPanelMedicals(), BorderLayout.CENTER);
		add(getJPanelNorth(), BorderLayout.NORTH);
		setDefaultCloseOperation(WardPharmacyNew.DISPOSE_ON_CLOSE);
		setTitle(MessageBundle.getMessage("angal.medicalstockwardedit.title"));
		pack();
		setLocationRelativeTo(null);
	}

	private JPanel getJPanelNorth() {
		if (jPanelNorth == null) {
			jPanelNorth = new JPanel();
			jPanelNorth.setLayout(new BoxLayout(jPanelNorth, BoxLayout.Y_AXIS));
			jPanelNorth.add(getJPanelPatient());
			jPanelNorth.add(getJPanelUse());
			ButtonGroup group = new ButtonGroup();
			group.add(jRadioPatient);
			group.add(jRadioUse);
		}
		return jPanelNorth;
	}

	private JPanel getJPanelUse() {
		if (jPanelUse == null) {
			jPanelUse = new JPanel(new FlowLayout(FlowLayout.LEFT));
			jPanelUse.add(getJRadioUse());
			jPanelUse.add(getJLabelUse());
			jPanelUse.add(getJTextFieldUse());
		}
		return jPanelUse;
	}

	private JLabel getJLabelUse() {
		if (jLabelUse == null) {
			jLabelUse = new JLabel();
			jLabelUse.setText(MessageBundle.getMessage("angal.medicalstockwardedit.internaluse"));
		}
		return jLabelUse;
	}

	private JTextField getJTextFieldUse() {
		if (jTextFieldUse == null) {
			jTextFieldUse = new JTextField();
			jTextFieldUse.setText(MessageBundle.getMessage("angal.medicalstockwardedit.internaluse").toUpperCase()); //$NON-NLS-1$
			jTextFieldUse.setPreferredSize(PatientDimension);
			jTextFieldUse.setEnabled(false);
		}
		return jTextFieldUse;
	}

	private JRadioButton getJRadioUse() {
		if (jRadioUse == null) {
			jRadioUse = new JRadioButton();
			jRadioUse.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					jTextFieldPatient.setEnabled(false);
					jButtonPickPatient.setEnabled(false);
					jButtonTrashPatient.setEnabled(false);
					jTextFieldUse.setEnabled(true);
				}
			});
		}
		return jRadioUse;
	}

	private JButton getJButtonAddMedical() {
		if (jButtonAddMedical == null) {
			jButtonAddMedical = new JButton();
			jButtonAddMedical.setText(MessageBundle.getMessage("angal.medicalstockwardedit.medical")); //$NON-NLS-1$
			jButtonAddMedical.setMnemonic(KeyEvent.VK_M);
			jButtonAddMedical.setIcon(new ImageIcon("rsc/icons/plus_button.png")); //$NON-NLS-1$
			jButtonAddMedical.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					ArrayList<Medical> currentMeds = new ArrayList<Medical>();
					currentMeds.addAll(medArray);
					ArrayList<Double> currentQties = new ArrayList<Double>();
					currentQties.addAll(qtyArray);

					// remove already inserted items
					for (MedicalWard medItem : medItems) {
						Medical med = medItem.getMedical();
						int index = currentMeds.indexOf(med);
						currentMeds.remove(index);
						currentQties.remove(index);
					}
					
					Icon icon = new ImageIcon("rsc/icons/medical_dialog.png"); //$NON-NLS-1$
					Medical med = (Medical)JOptionPane.showInputDialog(
					                    WardPharmacyNew.this,
					                    MessageBundle.getMessage("angal.medicalstockwardedit.selectamedical"), //$NON-NLS-1$
					                    MessageBundle.getMessage("angal.medicalstockwardedit.medical"), //$NON-NLS-1$
					                    JOptionPane.PLAIN_MESSAGE,
					                    icon,
					                    currentMeds.toArray(),
					                    ""); //$NON-NLS-1$
					if (med != null) {
						Double startQty = 0.;
						Double minQty = 0.;
						Double maxQty = currentQties.get(currentMeds.indexOf(med));
						Double stepQty = 0.5;
						JSpinner jSpinnerQty = new JSpinner(new SpinnerNumberModel(startQty,minQty,null,stepQty));
						
						StringBuilder messageBld = new StringBuilder(med.getDescription()).append("\n");
						messageBld.append(MessageBundle.getMessage("angal.medicalstockwardedit.insertquantitypiecesormls")).append("\n");
						messageBld.append(MessageBundle.getMessage("angal.medicalstockwardedit.instock")).append(": ").append(maxQty);
						
						int r = JOptionPane.showConfirmDialog(WardPharmacyNew.this, 
								new Object[] { messageBld.toString(), jSpinnerQty },
								MessageBundle.getMessage("angal.medicalstockwardedit.quantity"),
				        		JOptionPane.OK_CANCEL_OPTION, 
				        		JOptionPane.PLAIN_MESSAGE);
						
						if (r == JOptionPane.OK_OPTION) {
							try {
								Double qty = (Double) jSpinnerQty.getValue();
								if (qty > maxQty) {
									JOptionPane.showMessageDialog(WardPharmacyNew.this, 
											MessageBundle.getMessage("angal.medicalstockwardedit.invalidquantitypleaseinsertmax") + " " + maxQty, //$NON-NLS-1$
											MessageBundle.getMessage("angal.medicalstockwardedit.invalidquantity"), //$NON-NLS-1$
											JOptionPane.ERROR_MESSAGE);
									return;
								}
								double roundedQty = round(qty, stepQty);
								if (roundedQty >= stepQty)
									addItem(med, roundedQty);
								else
									JOptionPane.showMessageDialog(WardPharmacyNew.this, 
											MessageBundle.getMessage("angal.medicalstockwardedit.invalidquantitypleaseinsertatleast") + " " + stepQty, //$NON-NLS-1$
											MessageBundle.getMessage("angal.medicalstockwardedit.invalidquantity"), //$NON-NLS-1$
											JOptionPane.ERROR_MESSAGE);
							} catch (Exception eee) {
								JOptionPane.showMessageDialog(WardPharmacyNew.this, 
										MessageBundle.getMessage("angal.medicalstockwardedit.invalidquantitypleasetryagain"), //$NON-NLS-1$
										MessageBundle.getMessage("angal.medicalstockwardedit.invalidquantity"), //$NON-NLS-1$
										JOptionPane.ERROR_MESSAGE);
							}
						} else return;
					}
				}
			});
		}
		return jButtonAddMedical;
	}
	
	public double round(double input, double step) {
		return Math.round(input / step) * step;
	}
	
	private JButton getJButtonRemoveMedical() {
		if (jButtonRemoveMedical == null) {
			jButtonRemoveMedical = new JButton();
			jButtonRemoveMedical.setText(MessageBundle.getMessage("angal.medicalstockwardedit.removeitem")); //$NON-NLS-1$
			jButtonRemoveMedical.setIcon(new ImageIcon("rsc/icons/delete_button.png")); //$NON-NLS-1$
			jButtonRemoveMedical.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (jTableMedicals.getSelectedRow() < 0) { 
						JOptionPane.showMessageDialog(WardPharmacyNew.this,
								MessageBundle.getMessage("angal.medicalstockwardedit.pleaseselectanitem"), //$NON-NLS-1$
								"Error", //$NON-NLS-1$
								JOptionPane.WARNING_MESSAGE);
					} else {
						removeItem(jTableMedicals.getSelectedRow());
					}
				}
			});
		}
		return jButtonRemoveMedical;
	}

	private void addItem(Medical med, Double qty) {
		if (med != null) {
			
			MedicalWard item = new MedicalWard(med, qty);
			medItems.add(item);
			jTableMedicals.updateUI();
		}
	}
	
	private void removeItem(int row) {
		if (row != -1) {
			medItems.remove(row);
			jTableMedicals.updateUI();
		}
		
	}
	
	private JPanel getJPanelMedicalsButtons() {
		if (jPanelMedicalsButtons == null) {
			jPanelMedicalsButtons = new JPanel();
			jPanelMedicalsButtons.setLayout(new BoxLayout(jPanelMedicalsButtons, BoxLayout.Y_AXIS));
			jPanelMedicalsButtons.add(getJButtonAddMedical());
			jPanelMedicalsButtons.add(getJButtonRemoveMedical());
		}
		return jPanelMedicalsButtons;
	}

	private JScrollPane getJScrollPaneMedicals() {
		if (jScrollPaneMedicals == null) {
			jScrollPaneMedicals = new JScrollPane();
			jScrollPaneMedicals.setViewportView(getJTableMedicals());
		}
		return jScrollPaneMedicals;
	}

	private JTable getJTableMedicals() {
		if (jTableMedicals == null) {
			jTableMedicals = new JTable();
			jTableMedicals.setModel(new MedicalTableModel());
			for (int i = 0; i < medWidth.length; i++) {
				jTableMedicals.getColumnModel().getColumn(i).setMinWidth(medWidth[i]);
				if (!medResizable[i]) jTableMedicals.getColumnModel().getColumn(i).setMaxWidth(medWidth[i]);
			}
		}
		return jTableMedicals;
	}

	private JButton getJButtonOK() {
		if (jButtonOK == null) {
			jButtonOK = new JButton();
			jButtonOK.setText(MessageBundle.getMessage("angal.medicalstockwardedit.okm")); //$NON-NLS-1$
			jButtonOK.setMnemonic(KeyEvent.VK_O);
			jButtonOK.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					boolean isPatient;
					String description;
					int age = 0;
					float weight = 0;
					
					if (jRadioPatient.isSelected()) {
						if (patientSelected == null) {
							JOptionPane.showMessageDialog(null,	MessageBundle.getMessage("angal.medicalstockwardedit.pleaseselectapatient")); //$NON-NLS-1$
							return;
						}
						description = patientSelected.getName();
						age = patientSelected.getAge();
						weight = patientSelected.getWeight();
						isPatient = true;
					} else {
						if (jTextFieldUse.getText().compareTo("") == 0) {
							JOptionPane.showMessageDialog(null,	MessageBundle.getMessage("angal.medicalstockwardedit.pleaseinsertadescriptionfortheinternaluse")); //$NON-NLS-1$
							jTextFieldUse.requestFocus();
							return;
						}
						description = jTextFieldUse.getText();
						isPatient = false;
					}
					
					if (medItems.size() == 0) {
						JOptionPane.showMessageDialog(null,	MessageBundle.getMessage("angal.medicalstockwardedit.pleaseselectadrug")); //$NON-NLS-1$
						return;
					}
					
					MovWardBrowserManager wardManager = new MovWardBrowserManager();
					GregorianCalendar newDate = new GregorianCalendar();
					
					if (medItems.size() == 1) {
						
						MovementWard oneMovementWard = 
							new MovementWard(wardSelected,
								 			newDate,
								 			isPatient,
								 			patientSelected,
								 			age,
								 			weight,
								 			description,
								 			medItems.get(0).getMedical(),
								 			medItems.get(0).getQty(),
											MessageBundle.getMessage("angal.medicalstockwardedit.pieces"));
						
						boolean result = wardManager.newMovementWard(oneMovementWard);
						if (result) {
							fireMovementWardInserted();
						}
						if (!result)
							JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.medicalstockwardedit.thedatacouldnotbesaved"));
						else
							dispose();
					} else {
						
						ArrayList<MovementWard> manyMovementWard = new ArrayList<MovementWard>();
						for (int i = 0; i < medItems.size(); i++) {
							manyMovementWard.add(new MovementWard(
									wardSelected,
									newDate,
									isPatient,
									patientSelected,
									age,
									weight,
									description,
									medItems.get(i).getMedical(),
									medItems.get(i).getQty(),
									MessageBundle.getMessage("angal.medicalstockwardedit.pieces")));
						}
						
						boolean result = wardManager.newMovementWard(manyMovementWard);
						if (result) {
							fireMovementWardInserted();
						}
						if (!result)
							JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.medicalstockwardedit.thedatacouldnotbesaved"));
						else
							dispose();
					}
				}
			});
		}
		return jButtonOK;
	}
	
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton();
			jButtonCancel.setText(MessageBundle.getMessage("angal.medicalstockwardedit.Cancel")); //$NON-NLS-1$
			jButtonCancel.setMnemonic(KeyEvent.VK_C);
			jButtonCancel.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent event) {
						dispose();
				}
			});
		}
		return jButtonCancel;
	}

	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			jPanelButtons.add(getJButtonOK());
			jPanelButtons.add(getJButtonCancel());
		}
		return jPanelButtons;
	}

	private JPanel getJPanelMedicals() {
		if (jPanelMedicals == null) {
			jPanelMedicals = new JPanel();
			//jPanelMedicals.setLayout(new BoxLayout(jPanelMedicals, BoxLayout.X_AXIS));
			jPanelMedicals.add(getJScrollPaneMedicals());
			jPanelMedicals.add(getJPanelMedicalsButtons());
		}
		return jPanelMedicals;
	}

	private JPanel getJPanelPatient() {
		if (jPanelPatient == null) {
			jPanelPatient = new JPanel(new FlowLayout(FlowLayout.LEFT));
			jPanelPatient.add(getJRadioPatient());
			jPanelPatient.add(getJLabelPatient());
			jPanelPatient.add(getJTextFieldPatient());
			jPanelPatient.add(getJButtonPickPatient());
			jPanelPatient.add(getJButtonTrashPatient());
		}
		return jPanelPatient;
	}

	private JRadioButton getJRadioPatient() {
		if (jRadioPatient == null) {
			jRadioPatient = new JRadioButton();
			jRadioPatient.setSelected(true);
			jRadioPatient.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					System.out.println("in jRadioPatient: " + e.getID());
					jTextFieldUse.setEnabled(false);
					jTextFieldPatient.setEnabled(true);
					jButtonPickPatient.setEnabled(true);
					if (patientSelected != null) jButtonTrashPatient.setEnabled(true);
					
				}
			});
		}
		return jRadioPatient;
	}

	private JButton getJButtonTrashPatient() {
		if (jButtonTrashPatient == null) {
			jButtonTrashPatient = new JButton();
			jButtonTrashPatient.setMnemonic(KeyEvent.VK_R);
			jButtonTrashPatient.setPreferredSize(new Dimension(25,25));
			jButtonTrashPatient.setIcon(new ImageIcon("rsc/icons/remove_patient_button.png")); //$NON-NLS-1$
			jButtonTrashPatient.setToolTipText(MessageBundle.getMessage("angal.medicalstockwardedit.tooltip.removepatientassociationwiththismovement")); //$NON-NLS-1$
			jButtonTrashPatient.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					
					patientSelected = null;
					jTextFieldPatient.setText(""); //$NON-NLS-1$
					jTextFieldPatient.setEditable(true);
					jButtonPickPatient.setText(MessageBundle.getMessage("angal.medicalstockwardedit.pickpatient"));
					jButtonPickPatient.setToolTipText(MessageBundle.getMessage("angal.medicalstockwardedit.tooltip.associateapatientwiththismovement")); //$NON-NLS-1$
					jButtonTrashPatient.setEnabled(false);
				}
			});
			jButtonTrashPatient.setEnabled(false);
		}
		return jButtonTrashPatient;
	}

	private JButton getJButtonPickPatient() {
		if (jButtonPickPatient == null) {
			jButtonPickPatient = new JButton();
			jButtonPickPatient.setText(MessageBundle.getMessage("angal.medicalstockwardedit.pickpatient"));
			jButtonPickPatient.setMnemonic(KeyEvent.VK_P);
			jButtonPickPatient.setIcon(new ImageIcon("rsc/icons/pick_patient_button.png")); //$NON-NLS-1$
			jButtonPickPatient.setToolTipText(MessageBundle.getMessage("angal.medicalstockwardedit.tooltip.associateapatientwiththismovement"));
			jButtonPickPatient.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					SelectPatient sp = new SelectPatient(WardPharmacyNew.this, patientSelected);
					sp.addSelectionListener(WardPharmacyNew.this);
					sp.pack();
					sp.setVisible(true);
				}
			});
		}
		return jButtonPickPatient;
	}

	private JTextField getJTextFieldPatient() {
		if (jTextFieldPatient == null) {
			jTextFieldPatient = new JTextField();
			jTextFieldPatient.setText(""); //$NON-NLS-1$
			jTextFieldPatient.setPreferredSize(PatientDimension);
			//Font patientFont=new Font(jTextFieldPatient.getFont().getName(), Font.BOLD, jTextFieldPatient.getFont().getSize() + 4);
			//jTextFieldPatient.setFont(patientFont);
		}
		return jTextFieldPatient;
	}

	private JLabel getJLabelPatient() {
		if (jLabelPatient == null) {
			jLabelPatient = new JLabel();
			jLabelPatient.setText(MessageBundle.getMessage("angal.medicalstockwardedit.patient"));
		}
		return jLabelPatient;
	}
	
	public class MedicalTableModel implements TableModel {
		
		public MedicalTableModel() {
			
		}
		
		public Class<?> getColumnClass(int i) {
			return medClasses[i].getClass();
		}

		
		public int getColumnCount() {
			return medClasses.length;
		}
		
		public int getRowCount() {
			if (medItems == null)
				return 0;
			return medItems.size();
		}
		
		public Object getValueAt(int r, int c) {
			if (c == -1) {
				return medItems.get(r);
			}
			if (c == 0) {
				return medItems.get(r).getMedical().getDescription();
			}
			if (c == 1) {
				return medItems.get(r).getQty(); 
			}
			return null;
		}
		
		public boolean isCellEditable(int r, int c) {
			if (c == 1) return true;
			return false;
		}
		
		public void setValueAt(Object item, int r, int c) {
			//if (c == 1) billItems.get(r).setItemQuantity((Integer)item);

		}

		public void addTableModelListener(TableModelListener l) {
			
		}

		public String getColumnName(int columnIndex) {
			return medColumnNames[columnIndex];
		}

		public void removeTableModelListener(TableModelListener l) {
		}

	}
}
