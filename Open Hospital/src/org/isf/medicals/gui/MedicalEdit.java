package org.isf.medicals.gui;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.isf.generaldata.MessageBundle;
import org.isf.medicals.manager.MedicalBrowsingManager;
import org.isf.medicals.model.Medical;
import org.isf.medtype.manager.MedicalTypeBrowserManager;
import org.isf.medtype.model.MedicalType;
import org.isf.utils.jobjects.VoLimitedTextField;

/**
 * 18-ago-2008
 * added by alex:
 * 	- product code
 *  - pieces per packet
 */


public class MedicalEdit extends JDialog {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JPanel dataPanel = null;

	private JPanel buttonPanel = null;

	private JButton cancelButton = null;

	private JButton okButton = null;

	private JLabel descLabel = null;
	
	private JLabel codeLabel = null;
	
	private JLabel pcsperpckLabel = null;
	
	private JLabel criticLabel = null;
	
	private JTextField pcsperpckField = null;

	private VoLimitedTextField descriptionTextField = null;
	
	private VoLimitedTextField codeTextField = null;

	private JTextField minQtiField = null;

	private JLabel typeLabel = null;

	private JComboBox typeComboBox = null;

	private Medical medical = null;

	private boolean insert = false;

	/**
	 * 
	 * This is the default constructor; we pass the arraylist and the
	 * selectedrow because we need to update them
	 */
	public MedicalEdit(Medical old, boolean inserting,JFrame owner) {
		super(owner,true);
		insert = inserting;
		medical = old; // medical will be used for every operation
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {

		//this.setBounds(300, 300, 350, 240);
		this.setContentPane(getJContentPane());
		if (insert) {
			this.setTitle(MessageBundle.getMessage("angal.medicals.newmedicalrecord"));
		} else {
			this.setTitle(MessageBundle.getMessage("angal.medicals.editingmedicalrecord"));
		}
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getDataPanel(), java.awt.BorderLayout.CENTER); // Generated
			jContentPane.add(getButtonPanel(), java.awt.BorderLayout.SOUTH); // Generated
		}
		return jContentPane;
	}

	/**
	 * This method initializes dataPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getDataPanel() {
		if (dataPanel == null) {
			dataPanel = new JPanel();
			dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS)); // Generated
			
			typeLabel = new JLabel();
			typeLabel.setText(MessageBundle.getMessage("angal.medicals.type")); // Generated
			typeLabel.setAlignmentX(CENTER_ALIGNMENT);
			codeLabel = new JLabel();
			codeLabel.setText(MessageBundle.getMessage("angal.medicals.code")); // Generated
			codeLabel.setAlignmentX(CENTER_ALIGNMENT);
			descLabel = new JLabel();
			descLabel.setText(MessageBundle.getMessage("angal.medicals.description")); // Generated
			descLabel.setAlignmentX(CENTER_ALIGNMENT);
			pcsperpckLabel = new JLabel();
			pcsperpckLabel.setText(MessageBundle.getMessage("angal.medicals.pcsperpckExt")); // Generated
			pcsperpckLabel.setAlignmentX(CENTER_ALIGNMENT);
			criticLabel = new JLabel();
			criticLabel.setText(MessageBundle.getMessage("angal.medicals.criticallevel")); // Generated
			criticLabel.setAlignmentX(CENTER_ALIGNMENT);
			
			dataPanel.add(typeLabel, null); // Generated
			dataPanel.add(getTypeComboBox(), null); // Generated
			dataPanel.add(codeLabel, null); // Generated
			dataPanel.add(getCodeTextField(), null); // Generated
			dataPanel.add(descLabel, null); // Generated
			dataPanel.add(getDescriptionTextField(), null); // Generated
			dataPanel.add(pcsperpckLabel, null);
			dataPanel.add(getPcsperpckField());
			dataPanel.add(criticLabel, null);
			dataPanel.add(getMinQtiField());
		}
		return dataPanel;
	}

	/**
	 * This method initializes buttonPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.add(getOkButton(), null); // Generated
			buttonPanel.add(getCancelButton(), null); // Generated
		}
		return buttonPanel;
	}

	/**
	 * This method initializes cancelButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText(MessageBundle.getMessage("angal.medicals.cancel")); // Generated
			cancelButton.setMnemonic(KeyEvent.VK_C);
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return cancelButton;
	}

	/**
	 * This method initializes okButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setText(MessageBundle.getMessage("angal.medicals.ok")); // Generated
			okButton.setMnemonic(KeyEvent.VK_O);
			okButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
						MedicalBrowsingManager manager = new MedicalBrowsingManager();
						medical.setType((MedicalType) typeComboBox
								.getSelectedItem());
						medical.setDescription(descriptionTextField.getText());
						medical.setProd_code(codeTextField.getText());
						try {
							medical.setPcsperpck(Integer.valueOf(pcsperpckField.getText()));
							medical.setMinqty(Double.valueOf(minQtiField.getText()));
							boolean result = false;
							if (insert) { // inserting
								result = manager.newMedical(medical);
								if (result) {
									dispose();
								}
							} else { // updating
								result = manager.updateMedical(medical);
								if (result) {
									dispose();
								}
							}
							if (!result)
								JOptionPane.showMessageDialog(null,
										MessageBundle.getMessage("angal.medicals.thedatacouldnotbesaved"));

						} catch (NumberFormatException ex) {
							JOptionPane.showMessageDialog(null,
									MessageBundle.getMessage("angal.medicals.insertavalidvalue"));
					}
				}
			});
		}
		return okButton;
	}

	/**
	 * This method initializes descriptionTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private VoLimitedTextField getDescriptionTextField() {
		if (descriptionTextField == null) {
			if (insert) {
				descriptionTextField = new VoLimitedTextField(100,50);
			} else {
				descriptionTextField = new VoLimitedTextField(100,50);
				descriptionTextField.setText(medical.getDescription());
			}
		}
		return descriptionTextField;
	}
	/**
	 * This method initializes codeTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private VoLimitedTextField getCodeTextField() {
		if (codeTextField == null) {
			if (insert) {
				codeTextField = new VoLimitedTextField(5);
			} else {
				codeTextField = new VoLimitedTextField(5);
				codeTextField.setText(medical.getProd_code());
			}
		}
		return codeTextField;
	}

	private JTextField getMinQtiField() {
		if (minQtiField == null) {
			if (insert)
				minQtiField = new JTextField(3);
			else
				minQtiField = new JTextField(String.valueOf(medical.getMinqty()));
		}
		return minQtiField;
	}
	
	private JTextField getPcsperpckField() {
		if (pcsperpckField == null) {
			if (insert)
				pcsperpckField = new JTextField(3);
			else
				pcsperpckField = new JTextField(String.valueOf(medical.getPcsperpck()));
		}
		return pcsperpckField;
	}

	/**
	 * This method initializes typeComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getTypeComboBox() {
		if (typeComboBox == null) {
			typeComboBox = new JComboBox();
			if (insert) {
				MedicalTypeBrowserManager manager = new MedicalTypeBrowserManager();
				ArrayList<MedicalType> types = manager.getMedicalType();
				for (MedicalType elem : types) {
					typeComboBox.addItem(elem);
				}
			} else {
				typeComboBox.addItem(medical.getType());
				typeComboBox.setEnabled(false);
			}

		}
		return typeComboBox;
	}

} // @jve:decl-index=0:visual-constraint="82,7"
