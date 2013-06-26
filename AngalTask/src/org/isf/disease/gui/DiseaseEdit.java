package org.isf.disease.gui;

/*------------------------------------------
 * LabEdit - Add/edit a Disease
 * -----------------------------------------
 * modification history
 * 25/01/2006 - Rick, Vero, Pupo - first beta version
 * 03/11/2006 - ross - added flags OPD / IPD
 * 			         - changed title, version is now 1.0 
 * 09/06/2007 - ross - when updating, now the user can change the "dis type" also
 *------------------------------------------*/

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.event.*;

import org.isf.disease.manager.DiseaseBrowserManager;
import org.isf.disease.model.*;
import org.isf.distype.manager.DiseaseTypeBrowserManager;
import org.isf.distype.model.DiseaseType;
import org.isf.utils.jobjects.VoLimitedTextField;
import org.isf.generaldata.MessageBundle;

public class DiseaseEdit extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private EventListenerList diseaseListeners = new EventListenerList();
	
	public interface DiseaseListener extends EventListener {
		public void diseaseUpdated(AWTEvent e);
		public void diseaseInserted(AWTEvent e);
	}
	
	public void addDiseaseListener(DiseaseListener l) {
		diseaseListeners.add(DiseaseListener.class, l);
	}
	
	public void removeDiseaseListener(DiseaseListener listener) {
		diseaseListeners.remove(DiseaseListener.class, listener);
	}
	
	private void fireDiseaseInserted() {
		AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};
		
		EventListener[] listeners = diseaseListeners.getListeners(DiseaseListener.class);
		for (int i = 0; i < listeners.length; i++)
			((DiseaseListener)listeners[i]).diseaseInserted(event);
	}
	private void fireDiseaseUpdated() {
		AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};
		
		EventListener[] listeners = diseaseListeners.getListeners(DiseaseListener.class);
		for (int i = 0; i < listeners.length; i++)
			((DiseaseListener)listeners[i]).diseaseUpdated(event);
	}
	
	private static final String VERSION=MessageBundle.getMessage("angal.versione"); 

	private int pfrmBase = 15;
	private int pfrmWidth = 5;
	private int pfrmHeight = 5;
	private int pfrmBordX;
	private int pfrmBordY;
	private JPanel jContentPane = null;
	private JPanel dataPanel = null;
	private JPanel buttonPanel = null;
	private JButton cancelButton = null;
	private JButton okButton = null;
	private JLabel descLabel = null;
	private JLabel codeLabel = null;
	private JTextField descriptionTextField = null;
	private JTextField codeTextField = null;
	private JLabel typeLabel = null;
	private JComboBox typeComboBox = null;
	private Disease disease = null;
	private boolean insert = false;
	private JPanel jNewPatientPanel = null;
//	private JLabel inludeOpdLabel = null;
//	private JLabel inludeIpdLabel = null;
	private JCheckBox includeOpdCheckBox  = null;
	private JCheckBox includeIpdCheckBox  = null;

	private String lastDescription;

	/**
	 * 
	 * This is the default constructor; we pass the arraylist and the selectedrow
	 * because we need to update them
	 */
	public DiseaseEdit(JFrame parent,Disease old,boolean inserting) {
		super(parent,true);
		insert = inserting;
		disease = old;		//disease will be used for every operation
		initialize();
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screensize = kit.getScreenSize();
		pfrmBordX = (screensize.width - (screensize.width / pfrmBase * pfrmWidth)) / 2;
		pfrmBordY = (screensize.height - (screensize.height / pfrmBase * pfrmHeight)) / 2;
		this.setBounds(pfrmBordX,pfrmBordY,screensize.width / pfrmBase * pfrmWidth,screensize.height / pfrmBase * pfrmHeight);
		this.setContentPane(getJContentPane());
		if (insert) {
			this.setTitle(MessageBundle.getMessage("angal.disease.newdisease")+VERSION+")");
		} else {
			this.setTitle(MessageBundle.getMessage("angal.disease.editdisease")+VERSION+")");
		}
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
			jContentPane.add(getDataPanel(), java.awt.BorderLayout.NORTH);  // Generated
			jContentPane.add(getButtonPanel(), java.awt.BorderLayout.SOUTH);  // Generated
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
			typeLabel = new JLabel();
			typeLabel.setText(MessageBundle.getMessage("angal.disease.type"));  // Generated
			descLabel = new JLabel();
			descLabel.setText(MessageBundle.getMessage("angal.disease.description"));  // Generated
			codeLabel = new JLabel();
			codeLabel.setText(MessageBundle.getMessage("angal.disease.code"));			
			dataPanel = new JPanel();
			dataPanel.setLayout(new BoxLayout(getDataPanel(), BoxLayout.Y_AXIS));  // Generated
			dataPanel.add(typeLabel, null);  // Generated
			dataPanel.add(getTypeComboBox(), null);  // Generated
			dataPanel.add(codeLabel, null);  // Generated
			dataPanel.add(getCodeTextField(), null);  // Generated
			dataPanel.add(descLabel, null);  // Generated
			dataPanel.add(getDescriptionTextField(), null);  // Generated
			dataPanel.add(getJFlagsPanel(), null);
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
			buttonPanel.add(getOkButton(), null);  // Generated
			buttonPanel.add(getCancelButton(), null);  // Generated
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
			cancelButton.setText(MessageBundle.getMessage("angal.disease.cancel"));  // Generated
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
			okButton.setText(MessageBundle.getMessage("angal.disease.ok"));  // Generated
			okButton.setMnemonic(KeyEvent.VK_O);
			okButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					DiseaseBrowserManager manager = new DiseaseBrowserManager();
					String diseaseDesc = descriptionTextField.getText();
					if (insert){
						String key = codeTextField.getText().trim();
						if (key.equals("")){
							JOptionPane.showMessageDialog(				
									null,
									MessageBundle.getMessage("angal.disease.pleaseinsertacode"),
									MessageBundle.getMessage("angal.hospital"),
									JOptionPane.PLAIN_MESSAGE);
							return;
						}	
						if (key.length()>10){
							JOptionPane.showMessageDialog(				
									null,
									MessageBundle.getMessage("angal.disease.codetoolongmaxchars"),
									MessageBundle.getMessage("angal.hospital"),
									JOptionPane.PLAIN_MESSAGE);
							
							return;	
						}
						
						if (manager.codeControl(key)){
							JOptionPane.showMessageDialog(				
									null,
									MessageBundle.getMessage("angal.disease.codealreadyinuse"),
									MessageBundle.getMessage("angal.hospital"),
									JOptionPane.PLAIN_MESSAGE);
							
							return;	
						}
					}
					if (diseaseDesc.equals("")){
						JOptionPane.showMessageDialog(				
								null,
								MessageBundle.getMessage("angal.disease.pleaseinsertavaliddescription"),
								MessageBundle.getMessage("angal.hospital"),
								JOptionPane.PLAIN_MESSAGE);
						return;	
					}

					//if inserting or description has changed on updating
					if (lastDescription == null || !lastDescription.equals(diseaseDesc)) {
						if (manager.descriptionControl(descriptionTextField.getText(),
								((DiseaseType)typeComboBox.getSelectedItem()).getCode())){
							JOptionPane.showMessageDialog(				
									null,
									MessageBundle.getMessage("angal.disease.diseasealreadypresent"),
									MessageBundle.getMessage("angal.hospital"),
									JOptionPane.PLAIN_MESSAGE);
							
							return;	
						}
					}
					
					disease.setType((DiseaseType)typeComboBox.getSelectedItem());
					disease.setDescription(descriptionTextField.getText());
					disease.setCode(codeTextField.getText().trim().toUpperCase());
					disease.setOpdInclude(includeOpdCheckBox.isSelected());
					disease.setIpdInclude(includeIpdCheckBox.isSelected());
					
					boolean result = false;
					if (insert) {      // inserting
						result = manager.newDisease(disease);
						if (result) {
							fireDiseaseInserted();
						}
					} else {                          // updating
						result = manager.updateDisease(disease);
						if (result) {
							fireDiseaseUpdated();
						}
					}
					if (!result) JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.disease.thedatacouldnotbesaved"));
					else  dispose();
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
	private JTextField getDescriptionTextField() {
		if (descriptionTextField == null) {
			if (insert) {
				descriptionTextField = new JTextField();
			} else {
				lastDescription = disease.getDescription();
				descriptionTextField = new JTextField(lastDescription);
			}
		}
		return descriptionTextField;
	}
	
	/**
	 * This method initializes codeTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getCodeTextField() {
		if (codeTextField == null) {
			codeTextField = new VoLimitedTextField(10);
			if (!insert) {
				codeTextField.setText(disease.getCode());
				codeTextField.setEnabled(false);
			}
		}
		return codeTextField;
	}

	
	//private JLabel inludeOpdLabel = null;
	//private JCheckBox includeOpdCheckBox  = null;

	
	public JPanel getJFlagsPanel() {
		if (jNewPatientPanel == null){
			jNewPatientPanel = new JPanel();
			includeOpdCheckBox = new JCheckBox(MessageBundle.getMessage("angal.disease.opd"));
			includeIpdCheckBox = new JCheckBox(MessageBundle.getMessage("angal.disease.ipd"));
			jNewPatientPanel.add(includeOpdCheckBox);
			jNewPatientPanel.add(includeIpdCheckBox);
			if(!insert){
				if (disease.getOpdInclude()) includeOpdCheckBox.setSelected(true);
				if (disease.getIpdInclude()) includeIpdCheckBox.setSelected(true);
			}
		}
		return jNewPatientPanel;
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
				DiseaseTypeBrowserManager manager = new DiseaseTypeBrowserManager();
				ArrayList<DiseaseType> types = manager.getDiseaseType();
				for (DiseaseType elem : types) {
					typeComboBox.addItem(elem);
				}
			} else {
				DiseaseType selectedDiseaseType=null;
				DiseaseTypeBrowserManager manager = new DiseaseTypeBrowserManager();
				ArrayList<DiseaseType> types = manager.getDiseaseType();
				for (DiseaseType elem : types) {
					typeComboBox.addItem(elem);
					if (disease.getType().equals(elem)) {
						selectedDiseaseType = elem;
					}
				}
				if (selectedDiseaseType!=null)
					typeComboBox.setSelectedItem(selectedDiseaseType);
				//typeComboBox.setEnabled(false);
			}
			
		}
		return typeComboBox;
	}
	
	
}  //  @jve:decl-index=0:visual-constraint="82,7"