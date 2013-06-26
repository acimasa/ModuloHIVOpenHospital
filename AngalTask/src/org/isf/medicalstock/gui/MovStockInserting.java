package org.isf.medicalstock.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;

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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.medicals.manager.MedicalBrowsingManager;
import org.isf.medicals.model.Medical;
import org.isf.medicalstock.manager.DateTextField;
import org.isf.medicalstock.manager.MovStockInsertingManager;
import org.isf.medicalstock.model.Lot;
import org.isf.medicalstock.model.Movement;
import org.isf.medstockmovtype.manager.MedicaldsrstockmovTypeBrowserManager;
import org.isf.medstockmovtype.model.MovementType;
import org.isf.utils.jobjects.VoLimitedTextField;
import org.isf.ward.manager.WardBrowserManager;
import org.isf.ward.model.Ward;
import org.isf.xmpp.gui.CommunicationFrame;
import org.isf.xmpp.manager.Interaction;

/*-------------------------------------------------------------------
 * modification history
 * ====================
 * 19/08/08 - alex - searchkey textfield
 * 05/01/09	- chiara - lotPrepDate e lotDueDate invertiti nella GUI
 * 06/06/09 - alex - rollback on method getMedicalBox()
 * 					 filtering only on resultset
 *29/12/11  - nicola - add alert to share critical medical stock with an another openhospital's user  
 -------------------------------------------------------------------*/


public class MovStockInserting extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private MovStockInsertingManager manager;

	private JPanel selectionPanel = null;

	private JPanel lotPanel = null;

	private JPanel buttonPanel = null;

	private JComboBox medicalBox = null;

	private JTextField quantityText = null;

	private JPanel jPanelUnits;

	private JRadioButton jRadioPackets;

	private JRadioButton jRadioPieces;

	private JComboBox typeBox = null;

	private JComboBox wardBox = null;

	private JTextField lotIdText = null;

	private DateTextField lotPrepDate = null;

	private DateTextField lotDueDate = null;

	private JButton okButton = null;

	private JButton closeButton = null;

	private JButton resetButton = null;

	private JCheckBox lotCheck = null;

	private ArrayList<Lot> lotList = null;

	private JTable table;

	private Medical medicalSelected = null;

	private JPanel medicalPanel = null;

	//ADDED
	private JTextField searchString = null;
	private String lastKey = "";
	private MedicalBrowsingManager medicalManager = new MedicalBrowsingManager();
	private ArrayList<Medical> medicalList = medicalManager.getMedicals();
	//ADDED

	private MovementType typeSelected = null;
	private JTextField currentQuantity = null;
	private VoLimitedTextField movFrom = null;

	private boolean isCharging;

	private JComboBox shareWith=null;
	private Interaction share;

	/**
	 * This is the default constructor
	 */
	public MovStockInserting(JFrame owner) {
		super(owner, true);
		manager = new MovStockInsertingManager();
		initialize();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}

	/**
	 * This method initializes this Frame, sets the correct Dimensions
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setContentPane(getJContentPane());
		this.setTitle(MessageBundle.getMessage("angal.medicalstock.stockmovementinserting"));
		pack();
		setLocationRelativeTo(null);
	}

	/**
	 * this method controls if the automaticlot option is on
	 * 
	 * @return
	 */
	private boolean isAutomaticLot() {
		return GeneralData.AUTOMATICLOT;
	}

	/**
	 * This method initializes jContentPane, adds the main parts of the frame
	 * 
	 * @return jContentPanel (JPanel)
	 */
	private JPanel getJContentPane() {
		jContentPane = new JPanel();
		jContentPane.setLayout(new BoxLayout(jContentPane, BoxLayout.Y_AXIS));
		jContentPane.add(getSelectionPanel());
		lotPanel = new JPanel();
		lotPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(Color.GRAY), MessageBundle.getMessage("angal.medicalstock.lotpanel")));
		if(!isAutomaticLot()){
			lotPanel.setPreferredSize(new Dimension(550, 350));
			lotPanel.setMinimumSize(new Dimension(550, 350));
			lotPanel.setMaximumSize(new Dimension(550, 350));
		}
		jContentPane.add(lotPanel);
		jContentPane.add(getButtonPanel());
		validate();
		return jContentPane;
	}

	private JPanel getSelectionPanel() {
		selectionPanel = new JPanel();
		selectionPanel
		.setLayout(new BoxLayout(selectionPanel, BoxLayout.Y_AXIS));
		selectionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(Color.GRAY), MessageBundle.getMessage("angal.medicalstock.selectionpanel")));
		JPanel label2Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		label2Panel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.movementtypechargedischarge")));
		selectionPanel.add(label2Panel);
		JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		typePanel.add(getTypeBox());
		selectionPanel.add(typePanel);
		JPanel label1Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		label1Panel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.medical")));
		selectionPanel.add(label1Panel);

		//ADDED
		searchString = new JTextField();
		searchString.setColumns(15);
		searchString.addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e) {
				lastKey = "";
				String s = "" + e.getKeyChar();
				if (Character.isLetterOrDigit(e.getKeyChar())) {
					lastKey = s;
				}
				s = searchString.getText() + lastKey;
				s.trim();

				//medicalBox.removeAllItems();
				filterMedicalBox(s);
			}

			public void keyPressed(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
			}

		});
		JPanel label1bisPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel searchLabel = new JLabel(MessageBundle.getMessage("angal.medicalstock.searchcode"));
		label1bisPanel.add(searchLabel);
		label1bisPanel.add(searchString);
		selectionPanel.add(label1bisPanel);
		//ADDED

		medicalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		//medicalPanel.add(getMedicalBox(s));
		medicalPanel.add(getMedicalBox());
		selectionPanel.add(medicalPanel);
		JPanel label3Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		label3Panel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.quantity")));
		selectionPanel.add(label3Panel);
		JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		quantityPanel.add(getQuantityText());
		selectionPanel.add(quantityPanel);
		selectionPanel.add(getUnitsPanel());


		return selectionPanel;
	}

	private JPanel getUnitsPanel() {
		if (jPanelUnits == null) {
			jPanelUnits = new JPanel();
			ButtonGroup groupUnits = new ButtonGroup();
			groupUnits.add(getJRadioPackets());
			groupUnits.add(getJRadioPieces());
			jPanelUnits.add(getJRadioPieces());
			jPanelUnits.add(getJRadioPackets());
		}
		return jPanelUnits;
	}

	private JRadioButton getJRadioPackets() {
		if (jRadioPackets == null) {
			jRadioPackets = new JRadioButton(MessageBundle.getMessage("angal.medicalstock.bottlesorboxes"));
		}
		return jRadioPackets;
	}

	private JRadioButton getJRadioPieces() {
		if (jRadioPieces == null) {
			jRadioPieces = new JRadioButton(MessageBundle.getMessage("angal.medicalstock.piecesorml"));
			jRadioPieces.setSelected(true);
		}
		return jRadioPieces;
	}

	private JPanel getLotLoadPanel() {
		lotPanel.removeAll();
		if (!isAutomaticLot()) {
			lotPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
			JPanel newLotPanel = new JPanel();
			newLotPanel.setLayout(new BoxLayout(newLotPanel, BoxLayout.Y_AXIS));
			JPanel lotCheckPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			lotCheckPanel.add(getLotCheck());
			newLotPanel.add(lotCheckPanel);
			JPanel label1Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			label1Panel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.lotid")));
			newLotPanel.add(label1Panel);
			JPanel lotIdPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			lotIdPanel.add(getLotIdText());
			newLotPanel.add(lotIdPanel);
			JPanel label2Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			label2Panel.add(new JLabel("Insert the Preparation Date"));
			newLotPanel.add(label2Panel);
			newLotPanel.add(getLotPrepDate());
			JPanel label3Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			label3Panel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.inserttheexpirydate")));
			newLotPanel.add(label3Panel);
			newLotPanel.add(getLotDueDate());
			lotIdText.setEnabled(false);
			lotPrepDate.setEnabled(false);
			lotDueDate.setEnabled(false);
			lotPanel.add(newLotPanel);
			JPanel tablePanel = new JPanel();
			tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
			JPanel label4Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			label4Panel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.chooseanalreadyexistentlot")));
			tablePanel.add(label4Panel);
			JPanel lotTablePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			lotTablePanel.add(getTable());
			tablePanel.add(lotTablePanel);
			lotPanel.add(tablePanel);
		} else{
			lotPanel.setLayout(new BoxLayout(lotPanel,BoxLayout.Y_AXIS));
			JPanel label2Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			label2Panel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.insertthepreparationdate")));
			lotPanel.add(label2Panel);
			lotPanel.add(getLotPrepDate());			
			JPanel label4Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			label4Panel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.inserttheexpirydate")));
			lotPanel.add(label4Panel);
			JPanel expiryDatePanel = new JPanel(new FlowLayout(
					FlowLayout.CENTER));
			expiryDatePanel.add(getLotDueDate());
			lotPanel.add(expiryDatePanel);

		}
		JPanel originPanel=new JPanel();
		originPanel.setLayout(new BoxLayout(originPanel,BoxLayout.Y_AXIS));
		JPanel label2Panel=new JPanel(new FlowLayout(FlowLayout.CENTER));
		label2Panel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.inserttheoriginofthepharmaceutical")));
		originPanel.add(label2Panel);
		JPanel movFromPanel=new JPanel(new FlowLayout(FlowLayout.CENTER));
		movFromPanel.add(getMovFromField());
		originPanel.add(movFromPanel);
		lotPanel.add(originPanel);
		repaint();
		pack();
		setLocationRelativeTo(null);
		return lotPanel;
	}

	private JComboBox getShareUser(){

		share= new Interaction();
		Collection<String> contacts = share.getContactOnline();
		contacts.add("-- Share alert with: Nobody --");
		shareWith= new JComboBox(contacts.toArray());
		shareWith.setSelectedItem("-- Share alert with: Nobody --");

		return shareWith;
	}
	private JPanel getLotUnLoadPanel() {
		boolean xmpp;
		xmpp=GeneralData.XMPPMODULEENABLED;

		lotPanel.removeAll();
		lotPanel.setLayout(new BoxLayout(lotPanel, BoxLayout.Y_AXIS));
		JPanel label1Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		label1Panel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.chooseaward")));
		lotPanel.add(label1Panel);
		JPanel wardBoxPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		wardBoxPanel.add(getWardBox());
		lotPanel.add(wardBoxPanel);
		JPanel label2Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		label2Panel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.chooseanalreadyexistentlot")));
		lotPanel.add(label2Panel);
		JPanel lotTablePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		lotTablePanel.add(getTable());
		lotPanel.add(lotTablePanel);
		JPanel currentQuantityPanel=new JPanel(new FlowLayout(FlowLayout.RIGHT));
		currentQuantityPanel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.currentquantitylyinginstock")));
		currentQuantityPanel.add(getCurrentQuantityField());

		lotPanel.add(currentQuantityPanel);

		if(xmpp==true)
		{
			shareWith=getShareUser();
			shareWith.setEnabled(false);
			lotPanel.add(shareWith);
		}
		repaint();
		pack();
		setLocationRelativeTo(null);
		return lotPanel;
	}

	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.setBorder(BorderFactory.createTitledBorder(
					BorderFactory.createLineBorder(Color.GRAY), MessageBundle.getMessage("angal.medicalstock.buttons")));
			buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 5));
			buttonPanel.add(getOkButton());
			buttonPanel.add(getResetButton());
			buttonPanel.add(getCloseButton());
		}
		return buttonPanel;
	}

	private JComboBox getWardBox() {
		WardBrowserManager wbm = new WardBrowserManager();
		wardBox = new JComboBox();
		wardBox.addItem(MessageBundle.getMessage("angal.medicalstock.notspecified"));
		ArrayList<Ward> wardList = wbm.getWard();
		for (Ward elem : wardList) {
			if (GeneralData.INTERNALPHARMACIES) {
				if (elem.isPharmacy())
					wardBox.addItem(elem);
			} else {
				wardBox.addItem(elem);
			}
		}
		return wardBox;
	}

	//REPLACE BY getMedicalBox(String s)
	//ROLLBACK: Filtering in the ComboBox not in the query
	private JComboBox getMedicalBox() {
		if (medicalBox == null) {
			medicalBox = new JComboBox();
			medicalBox.setPreferredSize(new Dimension(500,20));
			medicalBox.setMinimumSize(new Dimension(500,20));
			medicalBox.setMaximumSize(new Dimension(500,20));
			medicalBox.addItem(MessageBundle.getMessage("angal.medicalstock.chooseapharmaceutical"));
			for (Medical medical : medicalList) {
				medicalBox.addItem(medical);
			}
			medicalBox.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (!(medicalBox.getSelectedItem() instanceof String) &&
							(medicalBox.getSelectedItem() != null)) {
						medicalSelected = (Medical) medicalBox.getSelectedItem();
						if (medicalSelected.getPcsperpck() == 0) 
							jRadioPackets.setEnabled(false);
						else 
							jRadioPackets.setEnabled(true);
						if (currentQuantity != null) {
							Medical medical = medicalManager.getMedical(medicalSelected.getCode());
							currentQuantity.setText(String.valueOf(medical.getTotalQuantity()));
						}
					} else
						medicalSelected = null;
					if (!(typeBox.getSelectedItem() instanceof String))
						if (!isCharging) {
							lotPanel = getLotUnLoadPanel();
						} else {
							lotPanel = getLotLoadPanel();
						}

					validate();
					repaint();
				}
			});
		}
		medicalSelected = null;
		return medicalBox;
	}

	private void filterMedicalBox(String s) {

		medicalBox.removeAllItems();

		if (s == null || s.compareTo("") == 0) {
			medicalBox.addItem(MessageBundle.getMessage("angal.medicalstock.chooseapharmaceutical"));
		}

		s.trim();
		String[] s1 = s.split(" ");

		for (Medical elem : medicalList) {
			if(s != null)
			{
				//Search key extended to description and code
				StringBuilder sbName = new StringBuilder();
				sbName.append(elem.getDescription().toUpperCase());
				sbName.append(elem.getProd_code().toUpperCase());
				String name = sbName.toString();

				int a = 0;
				for (int j = 0; j < s1.length ; j++) {
					if (name.contains(s1[j].toUpperCase())) {
						a++;
					}
				}
				if (a == s1.length) medicalBox.addItem(elem);
				/*if(name.toLowerCase().contains(s.toLowerCase()) == true)
					medicalBox.addItem(elem);*/
			} else
				medicalBox.addItem(elem);
		}

		if (medicalBox.getItemCount() == 1) {
			medicalSelected = (Medical) medicalBox.getSelectedItem();
		}
	}

	private JTextField getCurrentQuantityField(){
		if (currentQuantity == null) {
			currentQuantity = new JTextField();
			currentQuantity.setPreferredSize(new Dimension(50,20));
			currentQuantity.setEditable(false);
			//currentQuantity.setOpaque(true);
			//if(medicalSelected!=null)currentQuantity.setText(String.valueOf(manager.getCurrentQuantity(medicalSelected)));
		}
		return currentQuantity;
	}
	private VoLimitedTextField getMovFromField(){
		movFrom=new VoLimitedTextField(30,30);
		movFrom.setText("JMS");
		return movFrom;
	}
	private JComboBox getTypeBox() {
		manager = new MovStockInsertingManager();
		MedicaldsrstockmovTypeBrowserManager typeBrowserManager = new MedicaldsrstockmovTypeBrowserManager();
		ArrayList<MovementType> typeList = typeBrowserManager.getMedicaldsrstockmovType();

		typeBox = new JComboBox();
		typeBox.addItem("Choose a Type");
		for (MovementType type : typeList) {
			typeBox.addItem(type);
		}
		typeBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (!(typeBox.getSelectedItem() instanceof String)) {
					typeSelected = (MovementType) typeBox.getSelectedItem();
					if (typeSelected.getType().equalsIgnoreCase("+")) {
						lotPanel = getLotLoadPanel();
						isCharging = true;

					} else if (typeSelected.getType().equalsIgnoreCase("-")) {
						lotPanel = getLotUnLoadPanel();
						isCharging = false;
					}
					validate();
					repaint();
				} else
					typeSelected = null;
				// medicalBox.setSelectedIndex(0);
				// medicalSelected=null;
				// quantityText.setText("0");
			}
		});
		typeSelected = null;
		return typeBox;
	}

	private JTextField getQuantityText() {
		quantityText = new JTextField(5);
		quantityText.setText("0");
		quantityText.addFocusListener(new FocusListener() {

			public void focusLost(FocusEvent e) {
				String written = quantityText.getText();
				if (written.length() == 0)
					quantityText.setText("0");
				else
					for (int i = 0; i < written.length(); i++) {
						if (written.charAt(i) < '0' || written.charAt(i) > '9') {
							quantityText.setText("0");
							break;
						}
					}
			}

			public void focusGained(FocusEvent e) {
				quantityText.setSelectionStart(0);
				quantityText.setSelectionEnd(quantityText.getText().length());
			}
		});

		quantityText.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				if(typeSelected != null && typeSelected.getType().equalsIgnoreCase("-")){
					boolean isCritic = manager.alertCriticalQuantity(medicalSelected, Integer.parseInt(quantityText.getText()));


					if(isCritic){
						if(GeneralData.XMPPMODULEENABLED)
							shareWith.setEnabled(true);
					}
				} 
			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		return quantityText;
	}

	private JCheckBox getLotCheck() {
		lotCheck = new JCheckBox(MessageBundle.getMessage("angal.medicalstock.insertanewlot"));
		lotCheck.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				if (lotCheck.isSelected()) {
					lotIdText.setEnabled(true);
					lotPrepDate.setEnabled(true);
					lotDueDate.setEnabled(true);
				} else {
					lotIdText.setEnabled(false);
					lotPrepDate.setEnabled(false);
					lotDueDate.setEnabled(false);
				}
			}

		});
		return lotCheck;
	}

	private JTextField getLotIdText() {
		lotIdText = new JTextField(10);
		return lotIdText;
	}

	private DateTextField getLotPrepDate() {
		lotPrepDate = new DateTextField(new GregorianCalendar());
		return lotPrepDate;
	}

	private DateTextField getLotDueDate() {
		lotDueDate = new DateTextField(new GregorianCalendar());
		return lotDueDate;
	}

	private JScrollPane getTable() {
		table = new JTable(new StockMovModel(medicalSelected));
		table.addMouseListener(new MouseListener() {
			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
				if (isCharging)
					lotCheck.setSelected(false);
			}
		});
		table.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		table.getColumnModel().getColumn(0).setMaxWidth(100);
		table.getColumnModel().getColumn(1).setMaxWidth(80);
		table.getColumnModel().getColumn(2).setMaxWidth(80);
		table.getColumnModel().getColumn(3).setMaxWidth(100);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(330, 130));
		return scrollPane;

	}

	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton(MessageBundle.getMessage("angal.medicalstock.ok"));
			okButton.setMnemonic(KeyEvent.VK_O);
			okButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					Lot lot = null;
					if (isCharging) {
						if (typeSelected == null) {
							JOptionPane.showMessageDialog(
									null, 
									MessageBundle.getMessage("angal.medicalstock.chooseatype"));
							return;
						} else {
							if (!isAutomaticLot()) {
								if (lotCheck.isSelected()) {
									lot = new Lot(lotIdText.getText(),
											lotPrepDate.getCompleteDate(),
											lotDueDate.getCompleteDate());
								} else if (table.getSelectedRow() != -1) {
									lot = lotList.get(table.getSelectedRow());
								} else {
									JOptionPane.showMessageDialog(
											null, 
											MessageBundle.getMessage("angal.medicalstock.pleaseselectalotorcreateanewone"));
									return;
								}
							} else
								lot = new Lot("0",
										lotPrepDate.getCompleteDate(),
										lotDueDate.getCompleteDate());
							Movement movement = new Movement(medicalSelected,
									typeSelected, null, lot,
									new GregorianCalendar(), 
									jRadioPackets.isSelected() ? 
											medicalSelected.getPcsperpck()*Integer.valueOf(quantityText.getText()) :
												Integer.valueOf(quantityText.getText()),
												movFrom.getText());
							manager = new MovStockInsertingManager();
							if (manager.newMovement(movement))
								dispose();
						}
					} else {
						if (typeSelected == null) {
							JOptionPane.showMessageDialog(
									null, 
									MessageBundle.getMessage("angal.medicalstock.chooseatype"));
							return;
						} else {
							Ward wardSelected = null;
							if (!(wardBox.getSelectedItem() instanceof String)) {
								wardSelected = (Ward) wardBox.getSelectedItem();
							}
							if (table.getSelectedRow() != -1) {
								lot = lotList.get(table.getSelectedRow());
							} else {
								JOptionPane.showMessageDialog(
										null, 
										MessageBundle.getMessage("angal.medicalstock.pleaseselectalot"));
								return;
							}
							Movement movement = new Movement(medicalSelected,
									typeSelected, wardSelected, lot,
									new GregorianCalendar(), 
									jRadioPackets.isSelected() ? 
											medicalSelected.getPcsperpck()*Integer.valueOf(quantityText.getText()) :
												Integer.valueOf(quantityText.getText()),
												null);
							manager = new MovStockInsertingManager();
							if (manager.newMovement(movement))
								dispose();
							if(GeneralData.XMPPMODULEENABLED){
								if(shareWith.isEnabled()&& (!(((String)shareWith.getSelectedItem())=="-- Share alert with: Nobody --"))){
									CommunicationFrame frame= (CommunicationFrame)CommunicationFrame.getFrame();
									frame.sendMessage("ALERT: "+medicalSelected.getDescription()+" is about to end", (String)shareWith.getSelectedItem(), false);
								}
							}

						}
					}

				}

			});
		}
		return okButton;
	}

	private JButton getCloseButton() {

		if (closeButton == null) {
			closeButton = new JButton(MessageBundle.getMessage("angal.medicalstock.close"));
			closeButton.setMnemonic(KeyEvent.VK_C);
			closeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return closeButton;
	}

	private JButton getResetButton() {
		if (resetButton == null) {
			resetButton = new JButton(MessageBundle.getMessage("angal.medicalstock.reset"));
			resetButton.setMnemonic(KeyEvent.VK_R);
			resetButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					initialize();
				}
			});
		}
		return resetButton;
	}

	private String getConvertedString(GregorianCalendar time) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		return sdf.format(time.getTime());
	}

	class StockMovModel extends DefaultTableModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private MovStockInsertingManager manager = new MovStockInsertingManager();

		public StockMovModel(Medical medical) {
			lotList = manager.getLotByMedical(medical);
		}

		public int getRowCount() {
			if (lotList == null)
				return 0;
			return lotList.size();
		}

		public String getColumnName(int c) {
			if (c == 0) {
				return MessageBundle.getMessage("angal.medicalstock.lotid");
			}
			if (c == 1) {
				return MessageBundle.getMessage("angal.medicalstock.prepdate");
			}
			if (c == 2) {
				return MessageBundle.getMessage("angal.medicalstock.duedate");
			}
			if (c == 3) {
				return MessageBundle.getMessage("angal.medicalstock.quantity");
			}
			return "";
		}

		public int getColumnCount() {
			return 4;
		}

		public Object getValueAt(int r, int c) {
			if (c == -1) {
				return lotList.get(r);
			} else if (c == 0) {
				return lotList.get(r).getCode();
			} else if (c == 1) {
				return getConvertedString(lotList.get(r).getPreparationDate());
			} else if (c == 2) {
				return getConvertedString(lotList.get(r).getDueDate());
			} else if (c == 3) {
				return lotList.get(r).getQuantity();
			}
			return null;
		}

		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			// return super.isCellEditable(arg0, arg1);
			return false;
		}

	}
}