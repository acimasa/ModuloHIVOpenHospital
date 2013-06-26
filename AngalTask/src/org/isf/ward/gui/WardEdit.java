package org.isf.ward.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.EventListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

import org.isf.generaldata.MessageBundle;
import org.isf.utils.jobjects.VoLimitedTextField;
import org.isf.ward.manager.WardBrowserManager;
import org.isf.ward.model.Ward;

/**
 * This class allows wards edits and inserts
 * 
 * @author Rick
 * 
 */
public class WardEdit extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EventListenerList wardListeners = new EventListenerList();
	
	public interface WardListener extends EventListener {
		public void wardUpdated(AWTEvent e);
		public void wardInserted(AWTEvent e);
	}
	
	public void addWardListener(WardListener l) {
		wardListeners.add(WardListener.class, l);
	}
	
	public void removeWardListener(WardListener listener) {
		wardListeners.remove(WardListener.class, listener);
	}
	
	private void fireWardInserted() {
		AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};
		
		EventListener[] listeners = wardListeners.getListeners(WardListener.class);
		for (int i = 0; i < listeners.length; i++)
			((WardListener)listeners[i]).wardInserted(event);
	}
	private void fireWardUpdated() {
		AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};
		
		EventListener[] listeners = wardListeners.getListeners(WardListener.class);
		for (int i = 0; i < listeners.length; i++)
			((WardListener)listeners[i]).wardUpdated(event);
	}
	
	private int pfrmBase = 16;
	private int pfrmWidth = 5;
	private int pfrmHeight = 9;
	private int pfrmBordX;
	private int pfrmBordY;
	private JPanel jContentPane = null;
	private JPanel dataPanel = null;
	private JPanel buttonPanel = null;
	private JButton cancelButton = null;
	private JButton okButton = null;
	private JLabel descLabel = null;
	private JLabel codeLabel = null;
	private JLabel telLabel = null;
	private JLabel faxLabel = null;
	private JLabel emailLabel = null;
	private JLabel bedsLabel = null;
	private JLabel nursLabel = null;
	private JLabel docsLabel = null;
	private JLabel isPharmacyLabel = null;
	private JLabel requiredLabel = null;
//	private JLabel junkLabel = null;
	private JTextField descriptionTextField = null;
	private JTextField codeTextField = null;
	private JTextField telTextField = null;
	private JTextField faxTextField = null;
	private JTextField emailTextField = null;
	private JTextField bedsTextField = null;
	private JTextField nursTextField = null;
	private JTextField docsTextField = null;
	private JCheckBox isPharmacyCheck = null;
//	private String lastdescription;
	private Ward ward = null;
	private boolean insert = false;
	private int beds;
	private int nurs;
	private int docs;
	
	/**
	 * 
	 * This is the default constructor; we pass the parent frame
	 * (because it is a jdialog), the arraylist and the selected
	 * row because we need to update them
	 */
	public WardEdit(JFrame parent,Ward old,boolean inserting) {
		super(parent, true);
		insert = inserting;
		ward = old;		//operation will be used for every operation
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
			this.setTitle(MessageBundle.getMessage("angal.ward.newwardrecord"));
		} else {
			this.setTitle(MessageBundle.getMessage("angal.ward.editingwardrecord"));
		}
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
			descLabel = new JLabel();
			descLabel.setText(MessageBundle.getMessage("angal.ward.nameedit"));
			codeLabel = new JLabel();
			codeLabel.setText(MessageBundle.getMessage("angal.ward.codeedit"));
			telLabel = new JLabel();
			telLabel.setText(MessageBundle.getMessage("angal.ward.telephoneedit"));
			faxLabel = new JLabel();
			faxLabel.setText(MessageBundle.getMessage("angal.ward.faxedit"));
			emailLabel = new JLabel();
			emailLabel.setText(MessageBundle.getMessage("angal.ward.emailedit"));
			bedsLabel = new JLabel();
			bedsLabel.setText(MessageBundle.getMessage("angal.ward.bedsedit"));
			nursLabel = new JLabel();
			nursLabel.setText(MessageBundle.getMessage("angal.ward.nursesedit"));
			docsLabel = new JLabel();
			docsLabel.setText(MessageBundle.getMessage("angal.ward.doctorsedit"));
			isPharmacyLabel = new JLabel();
			isPharmacyLabel.setText(MessageBundle.getMessage("angal.ward.wardwithpharmacy"));
			requiredLabel= new JLabel();
			requiredLabel.setText(MessageBundle.getMessage("angal.ward.requiredfields"));
			dataPanel = new JPanel();
			dataPanel.setLayout(new BoxLayout(getDataPanel(), BoxLayout.Y_AXIS));  // Generated
			dataPanel.add(codeLabel, null);
			dataPanel.add(getCodeTextField(), null);
			dataPanel.add(descLabel, null);
			dataPanel.add(getDescriptionTextField(),null);
			dataPanel.add(telLabel, null);
			dataPanel.add(getTelTextField(),null);
			dataPanel.add(faxLabel, null);
			dataPanel.add(getFaxTextField(),null);
			dataPanel.add(emailLabel, null);
			dataPanel.add(getEmailTextField(),null);
			dataPanel.add(bedsLabel, null);
			dataPanel.add(getBedsTextField(),null);
			dataPanel.add(nursLabel, null);
			dataPanel.add(getNursTextField(),null);
			dataPanel.add(docsLabel, null);
			dataPanel.add(getDocsTextField(),null);
			dataPanel.add(isPharmacyLabel, null);
			dataPanel.add(getIsPharmacyCheck(),null);
			dataPanel.add(requiredLabel, null);
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
			cancelButton.setText(MessageBundle.getMessage("angal.ward.cancel"));  // Generated
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
			okButton.setText(MessageBundle.getMessage("angal.ward.ok"));  // Generated
			okButton.setMnemonic(KeyEvent.VK_O);
			
			okButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (insert) {
						String key = codeTextField.getText().trim();
						if (key.equals("")) {
							JOptionPane.showMessageDialog(				
									null,
									MessageBundle.getMessage("angal.ward.pleaseinsertacode"),
									MessageBundle.getMessage("angal.ward.stlukehospital"),
									JOptionPane.PLAIN_MESSAGE);
							return;
						}	
						if (key.length()>1) {
							JOptionPane.showMessageDialog(				
									null,
									MessageBundle.getMessage("angal.ward.codetoolong"),
									MessageBundle.getMessage("angal.ward.stlukehospital"),
									JOptionPane.PLAIN_MESSAGE);
							
							return;
						}
						WardBrowserManager manager = new WardBrowserManager();
						
						if (manager.codeControl(key)) {
							JOptionPane.showMessageDialog(				
									null,
									MessageBundle.getMessage("angal.ward.codealreadyinuse"),
									MessageBundle.getMessage("angal.ward.stlukehospital"),
									JOptionPane.PLAIN_MESSAGE);
							
							return;
						}
						
					}
					if (descriptionTextField.getText().trim().equals("")) {
						JOptionPane.showMessageDialog(				
								null,
								MessageBundle.getMessage("angal.ward.pleaseinsertaname"),
								MessageBundle.getMessage("angal.ward.stlukehospital"),
								JOptionPane.PLAIN_MESSAGE);
						return;
					}
					try {
						beds = Integer.parseInt(bedsTextField.getText());
					} catch (NumberFormatException f) {
						JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.ward.insertavalidbedsnumber"));
						return;
					}
					try {
						nurs = Integer.parseInt(nursTextField.getText());
					} catch (NumberFormatException f) {
						JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.ward.insertavalidnursesnumber"));
						return;
					}
					try {
						docs = Integer.parseInt(docsTextField.getText());
					} catch (NumberFormatException f) {
						JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.ward.insertavaliddoctorsnumber"));
						return;
					}
					if (beds<0) {
						JOptionPane.showMessageDialog(				
								null,
								MessageBundle.getMessage("angal.ward.bedsnumbermustbepositive"),
								MessageBundle.getMessage("angal.ward.stlukehospital"),
								JOptionPane.PLAIN_MESSAGE);
						return;
					}
					if (nurs<0) {
						JOptionPane.showMessageDialog(				
								null,
								MessageBundle.getMessage("angal.ward.nursesnumbermustbepositive"),
								MessageBundle.getMessage("angal.ward.stlukehospital"),
								JOptionPane.PLAIN_MESSAGE);
						return;
					}
					if (docs<0) {
						JOptionPane.showMessageDialog(				
								null,
								MessageBundle.getMessage("angal.ward.doctorsnumbermustbepositive"),
								MessageBundle.getMessage("angal.ward.stlukehospital"),
								JOptionPane.PLAIN_MESSAGE);
						return;
					}
					
					WardBrowserManager manager = new WardBrowserManager();
					
					ward.setDescription(descriptionTextField.getText());
					ward.setCode(codeTextField.getText().toUpperCase().trim());
					ward.setTelephone(telTextField.getText());
					ward.setFax(faxTextField.getText());
					ward.setEmail(emailTextField.getText());
					ward.setBeds(beds);
					ward.setNurs(nurs);
					ward.setDocs(docs);
					ward.setPharmacy(isPharmacyCheck.isSelected());
					
					boolean result = false;
					if (insert) {      // inserting
						result = manager.newWard(ward);
						if (result) {
							fireWardInserted();
						}
					}
					else {                          // updating
						result = manager.updateWard(ward);
						if (result) {
							fireWardUpdated();
						}
					}
					if (!result) JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.ward.thedatacouldnotbesaved"));
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
			descriptionTextField = new VoLimitedTextField(50);
			if (!insert) {				
				descriptionTextField.setText(ward.getDescription());
//				lastdescription=ward.getDescription();
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
			codeTextField = new VoLimitedTextField(1);			
			if (!insert) {
				codeTextField.setText(ward.getCode());
				codeTextField.setEnabled(false);
			}
		}
		return codeTextField;
	}
	
	/**
	 * This method initializes telTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTelTextField() {
		if (telTextField == null) {
			telTextField = new VoLimitedTextField(50);
			if (!insert) {
				telTextField.setText(ward.getTelephone());
			}
		}
		return telTextField;
	}
	
	/**
	 * This method initializes faxTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getFaxTextField() {
		if (faxTextField == null) {
			faxTextField = new VoLimitedTextField(50);
			if (!insert) {
				faxTextField.setText(ward.getFax());
			}
		}
		return faxTextField;
	}
	
	/**
	 * This method initializes emailTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getEmailTextField() {
		if (emailTextField == null) {
			emailTextField = new VoLimitedTextField(50);
			if (!insert) {
				emailTextField.setText(ward.getEmail());
			}
		}
		return emailTextField;
	}
	
	/**
	 * This method initializes bedsTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getBedsTextField() {	
		if (bedsTextField == null) {
			bedsTextField = new VoLimitedTextField(4);
			if (!insert) {
				bedsTextField.setText(Integer.toString(ward.getBeds()));
			}
		}
		return bedsTextField;
	}
	
	/**
	 * This method initializes nursTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getNursTextField() {
		if (nursTextField == null) {
			nursTextField = new VoLimitedTextField(4);
			if (!insert) {
				nursTextField.setText(Integer.toString(ward.getNurs()));
			}
		}
		return nursTextField;
	}
	
	/**
	 * This method initializes docsTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getDocsTextField() {
		if (docsTextField == null) {
			docsTextField = new VoLimitedTextField(4);
			if (!insert) {
				docsTextField.setText(Integer.toString(ward.getDocs()));
			}
		}
		return docsTextField;
	}
	
	/**
	 * This method initializes isPharmacyCheck
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getIsPharmacyCheck() {
		if (isPharmacyCheck==null) {
			isPharmacyCheck = new JCheckBox();
			if (!insert) {
				isPharmacyCheck.setSelected(ward.isPharmacy());
			}
		}
		return isPharmacyCheck;
	}
} 