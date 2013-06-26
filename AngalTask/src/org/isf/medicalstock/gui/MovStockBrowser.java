
package org.isf.medicalstock.gui;

/*------------------------------------------
 * MovStockBrowser - list medicals movement. let the user search for movements
 * 					  and insert a new movements
 * -----------------------------------------
 * modification history
 * 30/03/2006 - Theo - first beta version
 * 03/11/2006 - ross - changed title, removed delete all button
 *                   - corrected an error in datetextfield class (the month displayed in the filter was -1
 * 			         - version is now  1.0 
 *------------------------------------------*/

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.medicals.manager.MedicalBrowsingManager;
import org.isf.medicals.model.Medical;
import org.isf.medicalstock.manager.DateTextField;
import org.isf.medicalstock.manager.MovBrowserManager;
import org.isf.medicalstock.model.Movement;
import org.isf.medstockmovtype.manager.MedicaldsrstockmovTypeBrowserManager;
import org.isf.medstockmovtype.model.MovementType;
import org.isf.medtype.manager.MedicalTypeBrowserManager;
import org.isf.medtype.model.MedicalType;
import org.isf.utils.excel.ExcelExporter;
import org.isf.utils.jobjects.ModalJFrame;
import org.isf.ward.model.Ward;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MovStockBrowser extends ModalJFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger logger = LoggerFactory.getLogger(MovStockBrowser.class);

	private final JFrame myFrame;
	private JPanel contentPane;
	private JPanel buttonPanel;
	private JPanel tablePanel;
	private JButton closeButton;
	private JButton insertButton;
	private JButton filterButton;
	private JButton exportToExcel;
//	private JButton importFromExcel;
	private JPanel filterPanel;
	private JCheckBox jCheckBoxKeepFilter;
	private JComboBox medicalBox;
	private JComboBox medicalTypeBox;
	private JComboBox typeBox;
	private JComboBox wardBox;
	private DateTextField movDateFrom;
	private DateTextField movDateTo;
	private DateTextField lotPrepFrom;
	private DateTextField lotPrepTo;
	private DateTextField lotDueFrom;
	private DateTextField lotDueTo;
	private JTable movTable;
	private MovBrowserModel model;
	private ArrayList<Movement> moves;
	private String[] pColums = { MessageBundle.getMessage("angal.medicalstock.datem"), MessageBundle.getMessage("angal.medicalstock.typem"), MessageBundle.getMessage("angal.medicalstock.wardm"),MessageBundle.getMessage("angal.medicalstock.qtym"), MessageBundle.getMessage("angal.medicalstock.pharmaceuticalm"),
			MessageBundle.getMessage("angal.medicalstock.medtypem"),MessageBundle.getMessage("angal.medicalstock.lotm"),MessageBundle.getMessage("angal.medicalstock.prepdatem"),MessageBundle.getMessage("angal.medicalstock.duedatem"),MessageBundle.getMessage("angal.medicalstock.originm")};

	private int[] pColumwidth = { 80, 45, 130, 35, 150, 70, 70, 80, 65, 50 };
	private static final String DATE_FORMAT_DD_MM_YY = "dd/MM/yy";
	private static final String DATE_FORMAT_DD_MM_YY_HH_MM = "dd/MM/yy HH:mm";
	
	public MovStockBrowser() {
		myFrame = this;
		setTitle(MessageBundle.getMessage("angal.medicalstock.stockmovementbrowser"));
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screensize = kit.getScreenSize();
		final int pfrmBase = 30;
		final int pfrmWidth = 24;
		final int pfrmHeight = 22;
		this.setBounds((screensize.width - screensize.width * pfrmWidth
				/ pfrmBase) / 2, (screensize.height - screensize.height
				* pfrmHeight / pfrmBase) / 2, screensize.width * pfrmWidth
				/ pfrmBase, screensize.height * pfrmHeight / pfrmBase);
		setContentPane(getContentpane());
		
		//setResizable(false);
		pack();
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	private JPanel getContentpane() {
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		JPanel leftPanel = new JPanel();
		leftPanel.add(getFilterPanel());
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.add(getTablePanel());
		rightPanel.add(getButtonPanel());
		contentPane.add(leftPanel, BorderLayout.WEST);
		contentPane.add(rightPanel, BorderLayout.EAST);
		return contentPane;
	}
	/**
	 * this method controls if the automaticlot option is on
	 * 
	 * @return
	 */
	private boolean isAutomaticLot() {
		return GeneralData.AUTOMATICLOT;
	}
	private JPanel getButtonPanel() {
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
		buttonPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(Color.GRAY), MessageBundle.getMessage("angal.medicalstock.buttons")));
		buttonPanel.add(getInsertButton());
		buttonPanel.add(getCloseButton());
		buttonPanel.add(getExportToExcelButton());
		//buttonPanel.add(getImportFromExcelButton());
		//remove this button
		//buttonPanel.add(getdeleteAllButton());
		return buttonPanel;
	}

	private JPanel getTablePanel() {
		tablePanel = new JPanel();
		tablePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(Color.GRAY), MessageBundle.getMessage("angal.medicalstock.table")));
		JScrollPane scrollPane = new JScrollPane(getMovTable());
		int totWidth = 0;
		for (int colWidth : pColumwidth) {
			totWidth += colWidth;
		}
		scrollPane.setPreferredSize(new Dimension(totWidth, 450));
		tablePanel.add(scrollPane);
		return tablePanel;
	}

	private JPanel getFilterPanel() {
		filterPanel = new JPanel();
		filterPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(Color.GRAY), MessageBundle.getMessage("angal.medicalstock.selectionpanel")));
		filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
		filterPanel.add(getMedicalPanel());
		filterPanel.add(getMovementPanel());
		filterPanel.add(getLotPreparationDatePanel());
		filterPanel.add(getLotDueDatePanel());
		JPanel filterButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		filterButtonPanel.add(getFilterButton());
		filterButtonPanel.add(getJCheckBoxKeepFilter());
		filterPanel.add(filterButtonPanel);
		
		return filterPanel;
	}

	private JCheckBox getJCheckBoxKeepFilter() {
		if (jCheckBoxKeepFilter == null) {
			jCheckBoxKeepFilter = new JCheckBox("Keep");
		}
		return jCheckBoxKeepFilter;
	}

	private JPanel getMedicalPanel() {
		JPanel medicalPanel = new JPanel();
		medicalPanel.setLayout(new BoxLayout(medicalPanel, BoxLayout.Y_AXIS));
		medicalPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(Color.GRAY), MessageBundle.getMessage("angal.medicalstock.pharmaceutical")));
		JPanel label1Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		label1Panel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.description")));
		medicalPanel.add(label1Panel);
		JPanel medicalDescPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		medicalDescPanel.add(getMedicalBox());
		medicalPanel.add(medicalDescPanel);
		JPanel label2Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		label2Panel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.type")));
		medicalPanel.add(label2Panel);
		JPanel medicalTypePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		medicalTypePanel.add(getMedicalTypeBox());
		medicalPanel.add(medicalTypePanel);
		return medicalPanel;
	}

	private JPanel getMovementPanel() {
		JPanel movementPanel = new JPanel();
		movementPanel.setLayout(new BoxLayout(movementPanel, BoxLayout.Y_AXIS));
		movementPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(Color.GRAY), MessageBundle.getMessage("angal.medicalstock.movement")));
		JPanel label3Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		label3Panel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.type")));
		movementPanel.add(label3Panel);
		JPanel movementTypePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		movementTypePanel.add(getMovementTypeBox());
		movementPanel.add(movementTypePanel);

		JPanel label2Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		label2Panel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.ward")));
		movementPanel.add(label2Panel);
		JPanel wardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		wardPanel.add(getWardBox());
		movementPanel.add(wardPanel);

		JPanel label4Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		label4Panel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.date")));
		movementPanel.add(label4Panel);

		JPanel moveFromPanel = new JPanel(new BorderLayout());
		moveFromPanel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.from")), BorderLayout.WEST);
		moveFromPanel.add(getMovDateFrom(), BorderLayout.EAST);
		movementPanel.add(moveFromPanel);
		JPanel moveToPanel = new JPanel(new BorderLayout());
		moveToPanel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.to")), BorderLayout.WEST);
		moveToPanel.add(getMovDateTo(), BorderLayout.EAST);
		movementPanel.add(moveToPanel);
		return movementPanel;
	}

	private JPanel getLotPreparationDatePanel() {
		JPanel lotPreparationDatePanel = new JPanel();
		lotPreparationDatePanel.setLayout(new BoxLayout(
				lotPreparationDatePanel, BoxLayout.Y_AXIS));
		lotPreparationDatePanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.GRAY),
				MessageBundle.getMessage("angal.medicalstock.lotpreparationdate")));

		JPanel lotPrepFromPanel = new JPanel(new BorderLayout());
		lotPrepFromPanel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.from")), BorderLayout.WEST);
		lotPrepFromPanel.add(getLotPrepFrom(), BorderLayout.EAST);
		lotPreparationDatePanel.add(lotPrepFromPanel);
		JPanel lotPrepToPanel = new JPanel(new BorderLayout());
		lotPrepToPanel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.to")), BorderLayout.WEST);
		lotPrepToPanel.add(getLotPrepTo(), BorderLayout.EAST);
		lotPreparationDatePanel.add(lotPrepToPanel);

		return lotPreparationDatePanel;
	}

	private JPanel getLotDueDatePanel() {
		JPanel lotDueDatePanel = new JPanel();
		lotDueDatePanel.setLayout(new BoxLayout(lotDueDatePanel,
				BoxLayout.Y_AXIS));
		lotDueDatePanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.GRAY), MessageBundle.getMessage("angal.medicalstock.lotduedate")));

		JPanel lotDueFromPanel = new JPanel(new BorderLayout());
		lotDueFromPanel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.from")), BorderLayout.WEST);
		lotDueFromPanel.add(getLotDueFrom(), BorderLayout.EAST);
		lotDueDatePanel.add(lotDueFromPanel);
		JPanel lotDueToPanel = new JPanel(new BorderLayout());
		lotDueToPanel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.to")), BorderLayout.WEST);
		lotDueToPanel.add(getLotDueTo(), BorderLayout.EAST);
		lotDueDatePanel.add(lotDueToPanel);

		return lotDueDatePanel;
	}

	private JComboBox getWardBox() {
		org.isf.ward.manager.WardBrowserManager wbm = new org.isf.ward.manager.WardBrowserManager();
		wardBox = new JComboBox();
		wardBox.setPreferredSize(new Dimension(130,25));
		wardBox.addItem(MessageBundle.getMessage("angal.medicalstock.all"));
		ArrayList<Ward> wardList = wbm.getWard();
		for (org.isf.ward.model.Ward elem : wardList) {
			wardBox.addItem(elem);
		}
		wardBox.setEnabled(false);
		return wardBox;
	}

	private JComboBox getMedicalBox() {
		medicalBox = new JComboBox();
		medicalBox.setMaximumSize(new Dimension(150, 25));
		medicalBox.setMinimumSize(new Dimension(150, 25));
		medicalBox.setPreferredSize(new Dimension(150, 25));
		MedicalBrowsingManager medicalManager = new MedicalBrowsingManager();
		ArrayList<Medical> medical = medicalManager.getMedicals();
		medicalBox.addItem(MessageBundle.getMessage("angal.medicalstock.all"));
		for (Medical aMedical : medical) {
			medicalBox.addItem(aMedical);
		}
		medicalBox.addMouseListener(new MouseListener() {
			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
				medicalBox.setEnabled(true);
				medicalTypeBox.setSelectedIndex(0);
				medicalTypeBox.setEnabled(false);
			}
		});
		return medicalBox;

	}

	private JComboBox getMedicalTypeBox() {
		medicalTypeBox = new JComboBox();
		medicalTypeBox.setPreferredSize(new Dimension(130,25));
		MedicalTypeBrowserManager medicalManager = new MedicalTypeBrowserManager();
		ArrayList<MedicalType> medical = medicalManager.getMedicalType();
		medicalTypeBox.addItem(MessageBundle.getMessage("angal.medicalstock.all"));
		for (MedicalType aMedicalType : medical) {
			medicalTypeBox.addItem(aMedicalType);
		}
		medicalTypeBox.addMouseListener(new MouseListener() {
			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
				medicalTypeBox.setEnabled(true);
				medicalBox.setSelectedIndex(0);
				medicalBox.setEnabled(false);
			}
		});
		medicalTypeBox.setEnabled(false);
		return medicalTypeBox;
	}

	private JComboBox getMovementTypeBox() {
		typeBox = new JComboBox();
		typeBox.setPreferredSize(new Dimension(130,25));
		MedicaldsrstockmovTypeBrowserManager typeManager = new MedicaldsrstockmovTypeBrowserManager();
		ArrayList<MovementType> type = typeManager.getMedicaldsrstockmovType();
		typeBox.addItem(MessageBundle.getMessage("angal.medicalstock.all"));
		for (MovementType movementType : type) {
			typeBox.addItem(movementType);
		}
		typeBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!(typeBox.getSelectedItem() instanceof String)) {
					MovementType selected = (MovementType) typeBox
							.getSelectedItem();
					if (selected.getType().equalsIgnoreCase("-")) {
						wardBox.setEnabled(true);
					} else {
						wardBox.setSelectedIndex(0);
						wardBox.setEnabled(false);
					}
				} else {
					wardBox.setSelectedIndex(0);
					wardBox.setEnabled(false);
				}
			}
		});
		return typeBox;
	}

	private JTable getMovTable() {
		GregorianCalendar now=new GregorianCalendar();
		GregorianCalendar old=new GregorianCalendar();
		//old.roll(GregorianCalendar.WEEK_OF_YEAR,false);
		old.add(GregorianCalendar.WEEK_OF_YEAR,-1);
		model = new MovBrowserModel(null,null,null,null,old,now,null,null,null,null);
		movTable = new JTable(model);
		if(isAutomaticLot())movTable.removeColumn(movTable.getColumnModel().getColumn(6));
		/*movTable.getColumnModel().getColumn(0).setMaxWidth(pColumwidth[0]);
		movTable.getColumnModel().getColumn(1).setMaxWidth(pColumwidth[1]);
		movTable.getColumnModel().getColumn(2).setMaxWidth(pColumwidth[2]);
		movTable.getColumnModel().getColumn(3).setMaxWidth(pColumwidth[3]);
		movTable.getColumnModel().getColumn(4).setMaxWidth(pColumwidth[4]);
		movTable.getColumnModel().getColumn(5).setMaxWidth(pColumwidth[5]);
		movTable.getColumnModel().getColumn(6).setMaxWidth(pColumwidth[6]);
		movTable.getColumnModel().getColumn(7).setMaxWidth(pColumwidth[7]);
		movTable.getColumnModel().getColumn(0).setMinWidth(pColumwidth[0]);
		movTable.getColumnModel().getColumn(1).setMinWidth(pColumwidth[1]);
		movTable.getColumnModel().getColumn(2).setMinWidth(pColumwidth[2]);
		movTable.getColumnModel().getColumn(3).setMinWidth(pColumwidth[3]);
		movTable.getColumnModel().getColumn(4).setMinWidth(pColumwidth[4]);
		movTable.getColumnModel().getColumn(5).setMinWidth(pColumwidth[5]);
		movTable.getColumnModel().getColumn(6).setMinWidth(pColumwidth[6]);
		movTable.getColumnModel().getColumn(7).setMinWidth(pColumwidth[7]);*/
		movTable.getColumnModel().getColumn(0).setPreferredWidth(pColumwidth[0]);
		movTable.getColumnModel().getColumn(1).setPreferredWidth(pColumwidth[1]);
		movTable.getColumnModel().getColumn(2).setPreferredWidth(pColumwidth[2]);
		movTable.getColumnModel().getColumn(3).setPreferredWidth(pColumwidth[3]);
		movTable.getColumnModel().getColumn(4).setPreferredWidth(pColumwidth[4]);
		movTable.getColumnModel().getColumn(5).setPreferredWidth(pColumwidth[5]);
		if(!isAutomaticLot())movTable.getColumnModel().getColumn(6).setPreferredWidth(pColumwidth[6]);
		movTable.getColumnModel().getColumn(7).setPreferredWidth(pColumwidth[7]);
		movTable.getColumnModel().getColumn(6).setPreferredWidth(pColumwidth[8]);
		movTable.getColumnModel().getColumn(7).setPreferredWidth(pColumwidth[9]);
		return movTable;
	}

	private DateTextField getMovDateFrom() {
		GregorianCalendar time = new GregorianCalendar();
		//time.roll(GregorianCalendar.WEEK_OF_YEAR, false);
		time.add(GregorianCalendar.WEEK_OF_YEAR, -1);
		movDateFrom = new DateTextField(time);
		return movDateFrom;
	}

	private DateTextField getMovDateTo() {
		movDateTo = new DateTextField(new GregorianCalendar());
		return movDateTo;
	}

	private DateTextField getLotPrepFrom() {
		lotPrepFrom = new DateTextField();
		return lotPrepFrom;
	}

	private DateTextField getLotPrepTo() {
		lotPrepTo = new DateTextField();
		return lotPrepTo;
	}

	private DateTextField getLotDueFrom() {
		lotDueFrom = new DateTextField();
		return lotDueFrom;
	}

	private DateTextField getLotDueTo() {
		lotDueTo = new DateTextField();
		return lotDueTo;
	}

	/**
	 * this method creates the button that filters the data
	 * 
	 * @return
	 */

	private JButton getFilterButton() {
		filterButton = new JButton(MessageBundle.getMessage("angal.medicalstock.filter"));
		filterButton.setMnemonic(KeyEvent.VK_F);
		filterButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Integer medicalSelected = null;
				String medicalTypeSelected = null;
				String typeSelected = null;
				String wardSelected = null;
				boolean dateOk = true;
				GregorianCalendar movFrom=movDateFrom.getCompleteDate();
				GregorianCalendar movTo=movDateTo.getCompleteDate();
				GregorianCalendar prepFrom=lotPrepFrom.getCompleteDate();
				GregorianCalendar prepTo=lotPrepTo.getCompleteDate();
				GregorianCalendar dueFrom=lotDueFrom.getCompleteDate();
				GregorianCalendar dueTo=lotDueTo.getCompleteDate();
				
				if((movFrom==null)||(movTo==null)){
					if(!((movFrom==null)&&(movTo==null))){
						JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.medicalstock.chooseavalidmovementdate"));
						dateOk=false;
					}
				}else if(movFrom.compareTo(
						movTo) > 0) {
						JOptionPane
								.showMessageDialog(null,
										MessageBundle.getMessage("angal.medicalstock.movementdatefromcannotbelaterthanmovementdateto"));
						dateOk = false;
					}
					
				if((prepFrom==null)||(prepTo==null)){
					if(!((prepFrom==null)&&(prepTo==null))){
						System.out.println(MessageBundle.getMessage("angal.medicalstock.areo"));
						JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.medicalstock.chooseavalidpreparationdate"));
						dateOk=false;
					}
				}else if (prepFrom.compareTo(
						prepTo) > 0) {
						JOptionPane.showMessageDialog(null,
								MessageBundle.getMessage("angal.medicalstock.preparationdatefromcannotbelaterpreparationdateto"));
						dateOk = false;
				}
				
				
				if((dueFrom==null)||(dueTo==null)){
					if(!((dueFrom==null)&&(dueTo==null))){
						JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.medicalstock.chooseavalidduedate"));
						dateOk=false;
					}
				}else if (dueFrom.compareTo(
						dueTo) > 0) {
						JOptionPane.showMessageDialog(null,
								MessageBundle.getMessage("angal.medicalstock.duedatefromcannotbelaterthanduedateto"));
						dateOk = false;
				}
				if (dateOk) {
					if (medicalBox.isEnabled()) {
						if (!(medicalBox.getSelectedItem() instanceof String)) {
							medicalSelected = ((Medical) medicalBox
									.getSelectedItem()).getCode();
						}
					} else {
						if (!(medicalTypeBox.getSelectedItem() instanceof String)) {
							medicalTypeSelected = ((MedicalType) medicalTypeBox
									.getSelectedItem()).getCode();
						}
					}
					if (!(typeBox.getSelectedItem() instanceof String)) {
						typeSelected = ((MovementType) typeBox
								.getSelectedItem()).getCode();
					}
					if (!(wardBox.getSelectedItem() instanceof String)) {
						wardSelected = ((Ward) wardBox.getSelectedItem())
								.getCode();
					}
					model = new MovBrowserModel(medicalSelected,
							medicalTypeSelected, wardSelected, typeSelected,
							movDateFrom.getCompleteDate(), movDateTo
									.getCompleteDate(), lotPrepFrom
									.getCompleteDate(), lotPrepTo
									.getCompleteDate(), lotDueFrom
									.getCompleteDate(), lotDueTo
									.getCompleteDate());
					if (moves != null) {
						model.fireTableDataChanged();
						movTable.updateUI();
					}
				}
			}

		});
		return filterButton;
	}

	/**
	 * this method creates the button that close the mask
	 * 
	 * @return
	 */
	private JButton getCloseButton() {
		closeButton = new JButton(MessageBundle.getMessage("angal.medicalstock.close"));
		closeButton.setMnemonic(KeyEvent.VK_C);
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}

		});
		return closeButton;
	}

	/**
	 * this method creates the button that deletes all the records in
	 * medicaldsrstockmov and lot it is for training pourposes only to be
	 * deleted in production environment
	 * 
	 * @return
	 */
	/*private JButton getdeleteAllButton() {
		deleteAllButton = new JButton(MessageBundle.getMessage("angal.medicalstock.deleteall"));
		deleteAllButton.setMnemonic(KeyEvent.VK_D);
		deleteAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(null,
						MessageBundle.getMessage("angal.medicalstock.reallywanttodeleteallstockmovementsandlot"),
						MessageBundle.getMessage("angal.hospital"), JOptionPane.YES_NO_OPTION);
				if ((n == JOptionPane.YES_OPTION)) {
					try {
						DbQuery query = new DbQuery();
						String s = "delete from MEDICALDSRSTOCKMOV";
						query.setData(s, true);
						s = "delete from MEDICALDSRLOT;";
						query.setData(s, true);
					} catch (IOException err) {
						JOptionPane.showMessageDialog(null,
								MessageBundle.getMessage("angal.medicalstock.problemsoccurredwithserverconnection"));
						err.printStackTrace();
					} catch (SQLException err) {
						JOptionPane.showMessageDialog(null,
								MessageBundle.getMessage("angal.medicalstock.problemsoccurredwithsqlistruction"));
						err.printStackTrace();
					}
				}
				filterButton.doClick();
			}
		});
		return deleteAllButton;
	}*/

	/**
	 * this method creates the button that load the insert movement mask
	 * 
	 * @return
	 */
	private JButton getInsertButton() {
		insertButton = new JButton(MessageBundle.getMessage("angal.medicalstock.insert"));
		insertButton.setMnemonic(KeyEvent.VK_I);
		insertButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				new MovStockInserting(myFrame);
				model = new MovBrowserModel();
				//model.fireTableDataChanged();
				movTable.updateUI();
				if (jCheckBoxKeepFilter.isSelected()) filterButton.doClick();
			}
		});
		return insertButton;
	}
	
//	private JButton getImportFromExcelButton()
//	{
//		importFromExcel = new JButton(MessageBundle.getMessage("angal.medicalstock.import"));
//		importFromExcel.setMnemonic(KeyEvent.VK_I);
//		importFromExcel.addActionListener(new ActionListener()
//		{
//			public void actionPerformed(ActionEvent e) {
//				
//				JFileChooser fcExcel = new JFileChooser();
//				FileNameExtensionFilter excelFilter = new FileNameExtensionFilter("Excel (*.xls)","xls");
//				fcExcel.setFileFilter(excelFilter);
//				
//				int iRetVal = fcExcel.showOpenDialog(MovStockBrowser.this);
//				if(iRetVal == JFileChooser.APPROVE_OPTION)
//				{
//					ExcelImporter xlsImport = new ExcelImporter();
//					try
//					{
//						xlsImport.OpenInTable(movTable, fcExcel.getSelectedFile());
//						movTable.updateUI();
//					} catch(IOException exc)
//					{
//						System.out.println("Import to excel error : "+exc.getMessage());
//					}
//				}
//			}
//		});
//		return importFromExcel;
//	}
	
	private JButton getExportToExcelButton()
	{
		exportToExcel = new JButton(MessageBundle.getMessage("angal.medicalstock.export"));
		exportToExcel.setMnemonic(KeyEvent.VK_E);
		exportToExcel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fcExcel = new JFileChooser();
				FileNameExtensionFilter excelFilter = new FileNameExtensionFilter("Excel (*.xls)","xls");
				fcExcel.setFileFilter(excelFilter);
				fcExcel.setFileSelectionMode(JFileChooser.FILES_ONLY);  
				
				int iRetVal = fcExcel.showSaveDialog(MovStockBrowser.this);
				if(iRetVal == JFileChooser.APPROVE_OPTION)
				{
					File exportFile = fcExcel.getSelectedFile();
					if (!exportFile.getName().endsWith("xls")) exportFile = new File(exportFile.getAbsoluteFile() + ".xls");
					
					ExcelExporter xlsExport = new ExcelExporter();
					try
					{
						xlsExport.ExportTable(movTable, exportFile);
					} catch(IOException exc)
					{
						logger.info("Export to excel error : "+exc.getMessage());
					}
				}
			}
		});
		return exportToExcel;
	}


	/**
	 * This is the table model
	 * 
	 */
	class MovBrowserModel extends DefaultTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public MovBrowserModel() {
			GregorianCalendar now=new GregorianCalendar();
			GregorianCalendar old=new GregorianCalendar();
			old.add(GregorianCalendar.WEEK_OF_YEAR,-1);
			
			new MovBrowserModel(null,null,null,null,old,now,null,null,null,null);
		}

		public MovBrowserModel(Integer medicalCode, String medicalType,
				String ward, String movType, GregorianCalendar movFrom,
				GregorianCalendar movTo, GregorianCalendar lotPrepFrom,
				GregorianCalendar lotPrepTo, GregorianCalendar lotDueFrom,
				GregorianCalendar lotDueTo) {
			MovBrowserManager manager = new MovBrowserManager();
			moves = manager.getMovements(medicalCode, medicalType, ward,
					movType, movFrom, movTo, lotPrepFrom, lotPrepTo,
					lotDueFrom, lotDueTo);
		}

		public int getRowCount() {
			if (moves == null)
				return 0;
			return moves.size();
		}

		public String getColumnName(int c) {
			return pColums[c];
		}

		public int getColumnCount() {
			return pColums.length;
		}

		/**
		 * Note: We must get the objects in a reversed way because of the query
		 * 
		 * @see org.isf.lab.service.IoOperations
		 */
		public Object getValueAt(int r, int c) {
			Movement movement = moves.get(r);
			if (c == -1) {
				return movement;
			} else if (c == 0) {
				return formatDateTime(movement.getDate());
			} else if (c == 1) {
				return movement.getType().toString();
			} else if (c == 4) {
				return movement.getMedical()
						.getDescription();
			} else if (c == 5) {
				return movement.getMedical().getType().getDescription();
			} else if (c == 6) {
				if(isAutomaticLot())
					return MessageBundle.getMessage("angal.medicalstock.generated");
				else return movement.getLot();
			} else if (c == 7) {
				return formatDate(movement.getLot().getPreparationDate());
			} else if (c == 8) {
				return formatDate(movement.getLot().getDueDate());
			} else if (c == 3) {
				return movement.getQuantity();
			} else if (c == 2) {
				Ward ward = movement.getWard();
				if (ward.getDescription() != null)
					return ward;
				else
					//return MessageBundle.getMessage("angal.medicalstock.noward");
					return "";
			}else if(c==9){
				return movement.getOrigin();
			}
			return null;
		}

		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			return false;
		}
	}

	private String formatDate(GregorianCalendar time) {
		if (time == null)
			return MessageBundle.getMessage("angal.medicalstock.nodate");
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DD_MM_YY);
		return sdf.format(time.getTime());
	}
	
	private String formatDateTime(GregorianCalendar time) {
		if (time == null)
			return MessageBundle.getMessage("angal.medicalstock.nodate");
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DD_MM_YY_HH_MM);
		return sdf.format(time.getTime());
	}
}
