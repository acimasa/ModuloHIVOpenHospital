package org.isf.accounting.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EventListener;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.isf.accounting.manager.BillBrowserManager;
import org.isf.accounting.model.Bill;
import org.isf.accounting.model.BillItems;
import org.isf.accounting.model.BillPayments;
import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.generaldata.TxtPrinter;
import org.isf.menu.gui.MainMenu;
import org.isf.patient.gui.SelectPatient;
import org.isf.patient.gui.SelectPatient.SelectionListener;
import org.isf.patient.manager.PatientBrowserManager;
import org.isf.patient.model.Patient;
import org.isf.priceslist.manager.PriceListManager;
import org.isf.priceslist.model.List;
import org.isf.priceslist.model.Price;
import org.isf.pricesothers.manager.PricesOthersManager;
import org.isf.pricesothers.model.PricesOthers;
import org.isf.stat.manager.GenericReportBill;
import org.isf.utils.time.RememberDates;

import com.toedter.calendar.JDateChooser;
/**
 * Create a single Patient Bill
 * it affects tables BILLS, BILLITEMS and BILLPAYMENTS
 * 
 * @author Mwithi
 * 
 */
public class PatientBillEdit extends JDialog implements SelectionListener {

//LISTENER INTERFACE --------------------------------------------------------
	private EventListenerList patientBillListener = new EventListenerList();
	
	public interface PatientBillListener extends EventListener {
		public void billInserted(AWTEvent aEvent);
	}
	
	public void addPatientBillListener(PatientBillListener l) {
		patientBillListener.add(PatientBillListener.class, l);
		
	}
	
	private void fireBillInserted(Bill aBill) {
		AWTEvent event = new AWTEvent(aBill, AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};
		
		EventListener[] listeners = patientBillListener.getListeners(PatientBillListener.class);
		for (int i = 0; i < listeners.length; i++)
			((PatientBillListener)listeners[i]).billInserted(event);
	}
//---------------------------------------------------------------------------
	
	public void patientSelected(Patient patient) {
		patientSelected = patient;
		ArrayList<Bill> patientPendingBills = billManager.getPendingBills(patient.getCode());
		if (patientPendingBills.isEmpty()) {
			//BILL
			thisBill.setPatID(patientSelected.getCode());
			thisBill.setPatient(true);
			thisBill.setPatName(patientSelected.getName());
		} else {
			if (patientPendingBills.size() == 1) {
				JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.admission.thispatienthasapendingbill"),
						MessageBundle.getMessage("angal.admission.bill"), JOptionPane.PLAIN_MESSAGE);
				setBill(patientPendingBills.get(0));
				insert = false;
			} else {
				JOptionPane.showConfirmDialog(null, MessageBundle.getMessage("angal.admission.thereismorethanonependingbillforthispatientcontinue"),
						MessageBundle.getMessage("angal.admission.bill"), JOptionPane.WARNING_MESSAGE);
				return;
			}
		} 
		updateUI();
	}
	
	private static final long serialVersionUID = 1L;
	private JTable jTableBill;
	private JScrollPane jScrollPaneBill;
	private JButton jButtonAddMedical;
	private JButton jButtonAddOperation;
	private JButton jButtonAddExam;
	private JButton jButtonAddOther;
	private JButton jButtonAddPayment;
	private JPanel jPanelButtons;
	private JPanel jPanelDate;
	private JPanel jPanelPatient;
	private JTable jTablePayment;
	private JScrollPane jScrollPanePayment;
	private JTextField jTextFieldPatient;
	private JComboBox jComboBoxPriceList;
	private JPanel jPanelData;
	private JTable jTableTotal;
	private JScrollPane jScrollPaneTotal;
	private JTable jTableBalance;
	private JScrollPane jScrollPaneBalance;
	private JPanel jPanelTop;
	private JDateChooser jCalendarDate;
	private JLabel jLabelDate;
	private JLabel jLabelPatient;
	private JButton jButtonRemoveItem;
	private JLabel jLabelPriceList;
	private JButton jButtonRemovePayment;
	private JButton jButtonAddRefund;
	private JPanel jPanelButtonsPayment;
	private JPanel jPanelButtonsBill;
	private JPanel jPanelButtonsActions;
	private JButton jButtonClose;
	private JButton jButtonPaid;
	private JButton jButtonSave;
	private JButton jButtonBalance;
	private JButton jButtonCustom;
	private JButton jButtonPickPatient;
	private JButton jButtonTrashPatient;
	
	private static final Dimension PatientDimension = new Dimension(300,20);
	private static final Dimension LabelsDimension = new Dimension(60,20);
	private static final int PanelWidth = 450;
	private static final int ButtonWidth = 160;
	private static final int ButtonWidthBill = 160;
	private static final int ButtonWidthPayment = 160;
	private static final int PriceWidth = 150;
	private static final int QuantityWidth = 40;
	private static final int BillHeight = 300;
	private static final int TotalHeight = 20;
	private static final int PaymentHeight = 200;
	private static final int BalanceHeight = 20;
	//private static final int ActionHeight = 100;
	private static final int ButtonHeight = 25;
	
	private Double total;
	private Double balance;
	private int billID;
	private List listSelected;
	private boolean insert;
	private boolean modified = false;
	private boolean keepDate = true;
	private boolean paid = false;
	private Bill thisBill;
	private Patient patientSelected;
	private boolean foundList;
	private GregorianCalendar billDate = new GregorianCalendar();
	private GregorianCalendar today = new GregorianCalendar();
	
	private Object[] billClasses = {Price.class, Integer.class, Double.class};
	private String[] billColumnNames = {MessageBundle.getMessage("angal.newbill.item"), MessageBundle.getMessage("angal.newbill.qty"), MessageBundle.getMessage("angal.newbill.amount")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	private Object[] paymentClasses = {Date.class, Double.class};
	
	//Prices and Lists (ALL)
	private PriceListManager prcManager = new PriceListManager();
	private ArrayList<Price> prcArray = prcManager.getPrices();
	private ArrayList<List> lstArray = prcManager.getLists();
	
	//PricesOthers (ALL)
	private PricesOthersManager othManager = new PricesOthersManager();
	private ArrayList<PricesOthers> othPrices = othManager.getOthers();

	//Items and Payments (ALL)
	private BillBrowserManager billManager = new BillBrowserManager();
	private PatientBrowserManager patManager = new PatientBrowserManager();
	
	//Prices, Items and Payments for the tables
	private ArrayList<BillItems> billItems = new ArrayList<BillItems>();
	private ArrayList<BillPayments> payItems = new ArrayList<BillPayments>();
	private ArrayList<Price> prcListArray = new ArrayList<Price>();
	private int billItemsSaved;
	private int payItemsSaved;
	
	
	//User
	private String user = MainMenu.getUser();
	
	public PatientBillEdit() {
		PatientBillEdit newBill = new PatientBillEdit(null, new Bill(), true);
		newBill.setVisible(true);
	}
	
	public PatientBillEdit(JFrame owner, Patient patient) {
		Bill bill = new Bill();
		bill.setPatient(true);
		bill.setPatID(patient.getCode());
		bill.setPatName(patient.getName());
		PatientBillEdit newBill = new PatientBillEdit(owner, bill, true);
		newBill.setPatientSelected(patient);
		newBill.setVisible(true);
	}
	
	public PatientBillEdit(JFrame owner, Bill bill, boolean inserting) {
		super(owner, true);
		this.insert = inserting;
		setBill(bill);
		initComponents();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
	}
	
	private void setBill(Bill bill) {
		this.thisBill = bill;
		billDate = bill.getDate();
		billItems = billManager.getItems(thisBill.getId());
		payItems = billManager.getPayments(thisBill.getId());
		billItemsSaved = billItems.size();
		payItemsSaved = payItems.size();
		if (!insert) {
			checkBill();
		}
	}
	
	private void initComponents() {
		add(getJPanelTop(), BorderLayout.NORTH);
		add(getJPanelData(), BorderLayout.CENTER);
		add(getJPanelButtons(), BorderLayout.EAST);
		if (insert) {
			setTitle(MessageBundle.getMessage("angal.newbill.title"));  //$NON-NLS-1$
		} else {
			setTitle(MessageBundle.getMessage("angal.newbill.title") + " " + thisBill.getId());  //$NON-NLS-1$
		}
		pack();
	}

	//check if PriceList and Patient still exist
	private void checkBill() {
		
		foundList = false;
		if (thisBill.isList()) {
			for (List list : lstArray) {
				
				if (list.getId() == thisBill.getListID()) {
					listSelected = list;
					foundList = true;
					break;
				}
			}
			if (!foundList) { //PriceList not found
				Icon icon = new ImageIcon("rsc/icons/list_dialog.png"); //$NON-NLS-1$
				List list = (List)JOptionPane.showInputDialog(
				                    PatientBillEdit.this,
				                    MessageBundle.getMessage("angal.newbill.pricelistassociatedwiththisbillnolongerexists") + //$NON-NLS-1$
				                    "no longer exists", //$NON-NLS-1$
				                    MessageBundle.getMessage("angal.newbill.selectapricelist"), //$NON-NLS-1$
				                    JOptionPane.OK_OPTION,
				                    icon,
				                    lstArray.toArray(),
				                    ""); //$NON-NLS-1$
				if (list == null) {
					
					JOptionPane.showMessageDialog(PatientBillEdit.this,
							MessageBundle.getMessage("angal.newbill.nopricelistselected.part1") + //$NON-NLS-1$
							lstArray.get(0).getName() + MessageBundle.getMessage("angal.newbill.nopricelistselected.part2"), //$NON-NLS-1$
							"Error", //$NON-NLS-1$
							JOptionPane.WARNING_MESSAGE);
					list = lstArray.get(0);
				}
				thisBill.setListID(list.getId());
				thisBill.setListName(list.getName());
				
			}
		}
				
		if (thisBill.isPatient()) {
			
			Patient patient = patManager.getPatient(thisBill.getPatID());
			if (patient != null) {
				patientSelected = patient;
			} else {  //Patient not found
				Icon icon = new ImageIcon("rsc/icons/patient_dialog.png"); //$NON-NLS-1$
				JOptionPane.showMessageDialog(PatientBillEdit.this,
						MessageBundle.getMessage("angal.newbill.patientassociatedwiththisbillnolongerexists") + //$NON-NLS-1$
	                    "no longer exists", //$NON-NLS-1$
	                    "Warning", //$NON-NLS-1$
						JOptionPane.WARNING_MESSAGE,
						icon);
				
				thisBill.setPatient(false);
				thisBill.setPatID(0);
			}
		}
	}
	
	private JPanel getJPanelData() {
		if (jPanelData == null) {
			jPanelData = new JPanel();
			jPanelData.setLayout(new BoxLayout(jPanelData, BoxLayout.Y_AXIS));
			jPanelData.add(getJScrollPaneBill());
			jPanelData.add(getJScrollPaneTotal());
			jPanelData.add(getJScrollPanePayment());
			jPanelData.add(getJScrollPaneBalance());
		}
		return jPanelData;
	}
	
	private JPanel getJPanelPatient() {
		if (jPanelPatient == null) {
			jPanelPatient = new JPanel();
			jPanelPatient.setLayout(new FlowLayout(FlowLayout.LEFT));
			jPanelPatient.add(getJLabelPatient());
			jPanelPatient.add(getJTextFieldPatient());
			jPanelPatient.add(getJLabelPriceList());
			jPanelPatient.add(getJComboBoxPriceList());
		}
		return jPanelPatient;
	}

	private JLabel getJLabelPatient() {
		if (jLabelPatient == null) {
			jLabelPatient = new JLabel();
			jLabelPatient.setText(MessageBundle.getMessage("angal.newbill.patient")); //$NON-NLS-1$
			jLabelPatient.setPreferredSize(LabelsDimension);
		}
		return jLabelPatient;
	}

	
	private JTextField getJTextFieldPatient() {
		if (jTextFieldPatient == null) {
			jTextFieldPatient = new JTextField();
			jTextFieldPatient.setText(""); //$NON-NLS-1$
			jTextFieldPatient.setPreferredSize(PatientDimension);
			//Font patientFont=new Font(jTextFieldPatient.getFont().getName(), Font.BOLD, jTextFieldPatient.getFont().getSize() + 4);
			//jTextFieldPatient.setFont(patientFont);
			//if (!insert) jTextFieldPatient.setText(thisBill.getPatName());
			if (thisBill.isPatient()) {
				jTextFieldPatient.setText(thisBill.getPatName());
			}
			jTextFieldPatient.setEditable(false);
		}
		return jTextFieldPatient;
	}
	
	private JLabel getJLabelPriceList() {
		if (jLabelPriceList == null) {
			jLabelPriceList = new JLabel();
			jLabelPriceList.setText(MessageBundle.getMessage("angal.newbill.list")); //$NON-NLS-1$
		}
		return jLabelPriceList;
	}
	
	private JComboBox getJComboBoxPriceList() {
		if (jComboBoxPriceList == null) {
			jComboBoxPriceList = new JComboBox();
			List list = null;
			for (List lst : lstArray) {
				
				jComboBoxPriceList.addItem(lst);
				if (!insert)
					if (lst.getId() == thisBill.getListID())
						list = lst;
			}
			if (list != null) 
				jComboBoxPriceList.setSelectedItem(list);
			
			jComboBoxPriceList.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					
					listSelected = (List)jComboBoxPriceList.getSelectedItem();
					jTableBill.setModel(new BillTableModel());
					updateTotal();
					updateBalance();
					jTableTotal.getModel().setValueAt(total, 0, 1);
					jTableBalance.getModel().setValueAt(balance, 0, 1);
					
				}
			});
		}
		return jComboBoxPriceList;
	}
	
	private JDateChooser getJCalendarDate() {
		if (jCalendarDate == null) {
			
			if (insert) {
				//To remind last used
				billDate.set(Calendar.YEAR, RememberDates.getLastBillDateGregorian().get(Calendar.YEAR));
				billDate.set(Calendar.MONTH, RememberDates.getLastBillDateGregorian().get(Calendar.MONTH));
				billDate.set(Calendar.DAY_OF_MONTH, RememberDates.getLastBillDateGregorian().get(Calendar.DAY_OF_MONTH));
				jCalendarDate = new JDateChooser(billDate.getTime()); 
			} else { 
				//get BillDate
				jCalendarDate = new JDateChooser(thisBill.getDate().getTime());
				billDate.setTime(jCalendarDate.getDate());
			}
			jCalendarDate.setLocale(new Locale(GeneralData.LANGUAGE));
			jCalendarDate.setDateFormatString("dd/MM/yy - HH:mm:ss"); //$NON-NLS-1$
			jCalendarDate.getJCalendar().addPropertyChangeListener("calendar", new PropertyChangeListener() { //$NON-NLS-1$

				public void propertyChange(PropertyChangeEvent evt) {

					if (!insert) {
						if (keepDate && evt.getNewValue().toString().compareTo(
								evt.getOldValue().toString()) != 0) {
							
							Icon icon = new ImageIcon("rsc/icons/clock_dialog.png"); //$NON-NLS-1$
							int ok = JOptionPane.showConfirmDialog(PatientBillEdit.this,
									MessageBundle.getMessage("angal.newbill.doyoureallywanttochangetheoriginaldate"), //$NON-NLS-1$
									"Warning",  //$NON-NLS-1$
									JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE,
									icon);
							if (ok == JOptionPane.YES_OPTION) {
								keepDate = false;
								modified = true;
								jCalendarDate.setDate(((Calendar)evt.getNewValue()).getTime());
							} else {
								jCalendarDate.setDate(((Calendar)evt.getOldValue()).getTime());
							}
						} else {
							jCalendarDate.setDate(((Calendar)evt.getNewValue()).getTime());
						}
						billDate.setTime(jCalendarDate.getDate());
					} else {
						jCalendarDate.setDate(((Calendar)evt.getNewValue()).getTime());
						billDate.setTime(jCalendarDate.getDate());
					}
				}	
			});
		}
		return jCalendarDate;
	}
	
	private JLabel getJLabelDate() {
		if (jLabelDate == null) {
			jLabelDate = new JLabel();
			jLabelDate.setText(MessageBundle.getMessage("angal.newbill.Date")); //$NON-NLS-1$
			jLabelDate.setPreferredSize(LabelsDimension);
		}
		return jLabelDate;
	}

	private JPanel getJPanelDate() {
		if (jPanelDate == null) {
			jPanelDate = new JPanel();
			jPanelDate.setLayout(new FlowLayout(FlowLayout.LEFT));
			jPanelDate.add(getJLabelDate());
			jPanelDate.add(getJCalendarDate());
			jPanelDate.add(getJButtonPickPatient());
			jPanelDate.add(getJButtonTrashPatient());
		}
		return jPanelDate;
	}

	private JButton getJButtonTrashPatient() {
		if (jButtonTrashPatient == null) {
			jButtonTrashPatient = new JButton();
			jButtonTrashPatient.setMnemonic(KeyEvent.VK_R);
			jButtonTrashPatient.setPreferredSize(new Dimension(25,25));
			jButtonTrashPatient.setIcon(new ImageIcon("rsc/icons/remove_patient_button.png")); //$NON-NLS-1$
			jButtonTrashPatient.setToolTipText(MessageBundle.getMessage("angal.newbill.tooltip.removepatientassociationwiththisbill")); //$NON-NLS-1$
			jButtonTrashPatient.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					
					patientSelected = null;
					//BILL
					thisBill.setPatient(false);
					thisBill.setPatID(0);
					thisBill.setPatName(""); //$NON-NLS-1$
					//INTERFACE
					jTextFieldPatient.setText(""); //$NON-NLS-1$
					jTextFieldPatient.setEditable(false);
					jButtonPickPatient.setText(MessageBundle.getMessage("angal.newbill.pickpatient"));
					jButtonPickPatient.setToolTipText(MessageBundle.getMessage("angal.newbill.tooltip.associateapatientwiththisbill")); //$NON-NLS-1$
					jButtonTrashPatient.setEnabled(false);
				}
			});
			if (!thisBill.isPatient()) {
				jButtonTrashPatient.setEnabled(false);
			}
		}
		return jButtonTrashPatient;
	}

	private JButton getJButtonPickPatient() {
		if (jButtonPickPatient == null) {
			jButtonPickPatient = new JButton();
			jButtonPickPatient.setText(MessageBundle.getMessage("angal.newbill.pickpatient")); //$NON-NLS-1$
			jButtonPickPatient.setMnemonic(KeyEvent.VK_P);
			jButtonPickPatient.setIcon(new ImageIcon("rsc/icons/pick_patient_button.png")); //$NON-NLS-1$
			jButtonPickPatient.setToolTipText(MessageBundle.getMessage("angal.newbill.tooltip.associateapatientwiththisbill")); //$NON-NLS-1$
			jButtonPickPatient.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					SelectPatient sp = new SelectPatient(PatientBillEdit.this, patientSelected);
					sp.addSelectionListener(PatientBillEdit.this);
					sp.pack();
					sp.setVisible(true);
					
				}
			});
			if (thisBill.isPatient()) {
				jButtonPickPatient.setText(MessageBundle.getMessage("angal.newbill.changepatient")); //$NON-NLS-1$
				jButtonPickPatient.setToolTipText(MessageBundle.getMessage("angal.newbill.tooltip.changethepatientassociatedwiththisbill")); //$NON-NLS-1$
			}
		}
		return jButtonPickPatient;
	}

	public void setPatientSelected(Patient patientSelected) {
		this.patientSelected = patientSelected;
	}

	private JPanel getJPanelTop() {
		if (jPanelTop == null) {
			jPanelTop = new JPanel();
			jPanelTop.setLayout(new BoxLayout(jPanelTop, BoxLayout.Y_AXIS));
			jPanelTop.add(getJPanelDate());
			jPanelTop.add(getJPanelPatient());
		}
		return jPanelTop;
	}

	private JScrollPane getJScrollPaneBill() {
		if (jScrollPaneBill == null) {
			jScrollPaneBill = new JScrollPane();
			jScrollPaneBill.setBorder(null);
			jScrollPaneBill.setViewportView(getJTableBill());
			jScrollPaneBill.setMaximumSize(new Dimension(PanelWidth, BillHeight));
			jScrollPaneBill.setMinimumSize(new Dimension(PanelWidth, BillHeight));
			jScrollPaneBill.setPreferredSize(new Dimension(PanelWidth, BillHeight));

		}
		return jScrollPaneBill;
	}

	private JTable getJTableBill() {
		if (jTableBill == null) {
			jTableBill = new JTable();
			jTableBill.setModel(new BillTableModel());
			jTableBill.getColumnModel().getColumn(1).setMinWidth(QuantityWidth);
			jTableBill.getColumnModel().getColumn(1).setMaxWidth(QuantityWidth);
			jTableBill.getColumnModel().getColumn(2).setMinWidth(PriceWidth);
			jTableBill.getColumnModel().getColumn(2).setMaxWidth(PriceWidth);
			jTableBill.setAutoCreateColumnsFromModel(false);
		}
		return jTableBill;
	}
	
	private JScrollPane getJScrollPaneTotal() {
		if (jScrollPaneTotal == null) {
			jScrollPaneTotal = new JScrollPane();
			jScrollPaneTotal.setViewportView(getJTableTotal());
			jScrollPaneTotal.setMaximumSize(new Dimension(PanelWidth, TotalHeight));
			jScrollPaneTotal.setMinimumSize(new Dimension(PanelWidth, TotalHeight));
			jScrollPaneTotal.setPreferredSize(new Dimension(PanelWidth, TotalHeight));
		}
		return jScrollPaneTotal;
	}

	private JTable getJTableTotal() {
		if (jTableTotal == null) {
			jTableTotal = new JTable();
			jTableTotal.setModel(new DefaultTableModel(new Object[][] {{"<html><b>"+MessageBundle.getMessage("angal.newbill.totalm")+"</b></html>", total}}, new String[] {"",""}) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				private static final long serialVersionUID = 1L;
				Class<?>[] types = new Class<?>[] { JLabel.class, Double.class, };
	
				public Class<?> getColumnClass(int columnIndex) {
					return types[columnIndex];
				}

				public boolean isCellEditable(int row, int column) {
					return false;
				}
			});
			jTableTotal.getColumnModel().getColumn(1).setMinWidth(PriceWidth);
			jTableTotal.getColumnModel().getColumn(1).setMaxWidth(PriceWidth);
			jTableTotal.setMaximumSize(new Dimension(PanelWidth, TotalHeight));
			jTableTotal.setMinimumSize(new Dimension(PanelWidth, TotalHeight));
			jTableTotal.setPreferredSize(new Dimension(PanelWidth, TotalHeight));
		}
		return jTableTotal;
	}

	private JScrollPane getJScrollPanePayment() {
		if (jScrollPanePayment == null) {
			jScrollPanePayment = new JScrollPane();
			jScrollPanePayment.setBorder(null);
			jScrollPanePayment.setViewportView(getJTablePayment());
			jScrollPanePayment.setMaximumSize(new Dimension(PanelWidth, PaymentHeight));
			jScrollPanePayment.setMinimumSize(new Dimension(PanelWidth, PaymentHeight));
			jScrollPanePayment.setPreferredSize(new Dimension(PanelWidth, PaymentHeight));
		}
		return jScrollPanePayment;
	}

	private JTable getJTablePayment() {
		if (jTablePayment == null) {
			jTablePayment = new JTable();
			jTablePayment.setModel(new PaymentTableModel());
			jTablePayment.getColumnModel().getColumn(1).setMinWidth(PriceWidth);
			jTablePayment.getColumnModel().getColumn(1).setMaxWidth(PriceWidth);
		}
		return jTablePayment;
	}
	
	private JScrollPane getJScrollPaneBalance() {
		if (jScrollPaneBalance == null) {
			jScrollPaneBalance = new JScrollPane();
			jScrollPaneBalance.setViewportView(getJTableBalance());
			jScrollPaneBalance.setMaximumSize(new Dimension(PanelWidth, BalanceHeight));
			jScrollPaneBalance.setMinimumSize(new Dimension(PanelWidth, BalanceHeight));
			jScrollPaneBalance.setPreferredSize(new Dimension(PanelWidth, BalanceHeight));
		}
		return jScrollPaneBalance;
	}

	private JTable getJTableBalance() {
		if (jTableBalance == null) {
			jTableBalance = new JTable();
			jTableBalance.setModel(new DefaultTableModel(new Object[][] {{"<html><b>"+MessageBundle.getMessage("angal.newbill.balancem")+"</b></html>", balance}}, new String[] {"",""}) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				private static final long serialVersionUID = 1L;
				Class<?>[] types = new Class<?>[] { Object.class, Double.class, };
	
				public Class<?> getColumnClass(int columnIndex) {
					return types[columnIndex];
				}
				
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			});
			jTableBalance.getColumnModel().getColumn(1).setMinWidth(PriceWidth);
			jTableBalance.getColumnModel().getColumn(1).setMaxWidth(PriceWidth);
			jTableBalance.setMaximumSize(new Dimension(PanelWidth, BalanceHeight));
			jTableBalance.setMinimumSize(new Dimension(PanelWidth, BalanceHeight));
			jTableBalance.setPreferredSize(new Dimension(PanelWidth, BalanceHeight));
		}
		return jTableBalance;
	}
	
	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			jPanelButtons.setLayout(new BoxLayout(jPanelButtons, BoxLayout.Y_AXIS));
			jPanelButtons.add(getJPanelButtonsBill());
			jPanelButtons.add(getJPanelButtonsPayment());
			jPanelButtons.add(getJPanelButtonsActions());
		}
		return jPanelButtons;
	}
	
	private JPanel getJPanelButtonsBill() {
		if (jPanelButtonsBill == null) {
			jPanelButtonsBill = new JPanel();
			jPanelButtonsBill.setLayout(new BoxLayout(jPanelButtonsBill, BoxLayout.Y_AXIS));
			jPanelButtonsBill.add(getJButtonAddMedical());
			jPanelButtonsBill.add(getJButtonAddOperation());
			jPanelButtonsBill.add(getJButtonAddExam());
			jPanelButtonsBill.add(getJButtonAddOther());
			jPanelButtonsBill.add(getJButtonAddCustom());
			jPanelButtonsBill.add(getJButtonRemoveItem());
			jPanelButtonsBill.setMinimumSize(new Dimension(ButtonWidth, BillHeight+TotalHeight));
			jPanelButtonsBill.setMaximumSize(new Dimension(ButtonWidth, BillHeight+TotalHeight));
			jPanelButtonsBill.setPreferredSize(new Dimension(ButtonWidth, BillHeight+TotalHeight));

		}
		return jPanelButtonsBill;
	}

	private JPanel getJPanelButtonsPayment() {
		if (jPanelButtonsPayment == null) {
			jPanelButtonsPayment = new JPanel();
			jPanelButtonsPayment.setLayout(new BoxLayout(jPanelButtonsPayment, BoxLayout.Y_AXIS));
			jPanelButtonsPayment.add(getJButtonAddPayment());
			jPanelButtonsPayment.add(getJButtonAddRefund());
			jPanelButtonsPayment.add(getJButtonRemovePayment());
			jPanelButtonsPayment.setMinimumSize(new Dimension(ButtonWidth, PaymentHeight));
			jPanelButtonsPayment.setMaximumSize(new Dimension(ButtonWidth, PaymentHeight));
			//jPanelButtonsPayment.setPreferredSize(new Dimension(ButtonWidth, PaymentHeight));

		}
		return jPanelButtonsPayment;
	}
	
	private JPanel getJPanelButtonsActions() {
		if (jPanelButtonsActions == null) {
			jPanelButtonsActions = new JPanel();
			jPanelButtonsActions.setLayout(new BoxLayout(jPanelButtonsActions, BoxLayout.Y_AXIS));
			jPanelButtonsActions.add(getJButtonBalance());
			jPanelButtonsActions.add(getJButtonSave());
			jPanelButtonsActions.add(getJButtonPaid());
			jPanelButtonsActions.add(getJButtonClose());
			//jPanelButtonsActions.setMinimumSize(new Dimension(ButtonWidth, ActionHeight));
			//jPanelButtonsActions.setMaximumSize(new Dimension(ButtonWidth, ActionHeight));
	}
	return jPanelButtonsActions;
	}

	private JButton getJButtonBalance() {
		if (jButtonBalance == null) {
			jButtonBalance = new JButton();
			jButtonBalance.setText(MessageBundle.getMessage("angal.newbill.givechange") + "..."); //$NON-NLS-1$
			jButtonBalance.setMnemonic(KeyEvent.VK_B);
			jButtonBalance.setMaximumSize(new Dimension(ButtonWidth, ButtonHeight));
			jButtonBalance.setIcon(new ImageIcon("rsc/icons/money_button.png")); //$NON-NLS-1$
			if(insert) jButtonBalance.setEnabled(false);
			jButtonBalance.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					
					Icon icon = new ImageIcon("rsc/icons/money_dialog.png"); //$NON-NLS-1$
					Double amount = 0.;
					
					String quantity = (String) JOptionPane.showInputDialog(PatientBillEdit.this, 
							MessageBundle.getMessage("angal.newbill.entercustomercash"), 
							MessageBundle.getMessage("angal.newbill.givechange"), 
							JOptionPane.OK_CANCEL_OPTION, 
							icon, 
							null, 
							amount);
					
					if (quantity != null) {
						try {
							amount = Double.valueOf(quantity);
							if (amount == 0. || amount < balance) return;
							StringBuffer balanceBfr = new StringBuffer(MessageBundle.getMessage("angal.newbill.givechange"));
							balanceBfr.append(": ").append(amount.doubleValue() - balance.doubleValue());
							JOptionPane.showMessageDialog(PatientBillEdit.this, 
									balanceBfr.toString(),
									MessageBundle.getMessage("angal.newbill.givechange"), 
									JOptionPane.OK_OPTION,
									icon);
						} catch (Exception eee) {
							JOptionPane.showMessageDialog(PatientBillEdit.this, 
									MessageBundle.getMessage("angal.newbill.invalidquantitypleasetryagain"), //$NON-NLS-1$
									MessageBundle.getMessage("angal.newbill.invalidquantity"), //$NON-NLS-1$
									JOptionPane.ERROR_MESSAGE);
							return;
						}
					} else return;
				}
			});
		}
		return jButtonBalance;
	}

	private JButton getJButtonSave() {
		if (jButtonSave == null) {
			jButtonSave = new JButton();
			jButtonSave.setText(MessageBundle.getMessage("angal.newbill.save")); //$NON-NLS-1$
			jButtonSave.setMnemonic(KeyEvent.VK_S);
			jButtonSave.setMaximumSize(new Dimension(ButtonWidth, ButtonHeight));
			jButtonSave.setIcon(new ImageIcon("rsc/icons/save_button.png")); //$NON-NLS-1$
			jButtonSave.addActionListener(new ActionListener() {
			
				public void actionPerformed(ActionEvent e) {
					
					GregorianCalendar upDate = new GregorianCalendar();
					GregorianCalendar firstPay = new GregorianCalendar();
					
					if (payItems.size() > 0) {
						firstPay = payItems.get(0).getDate();
						upDate = payItems.get(payItems.size()-1).getDate();
					} else {
						upDate = billDate;
					}
					
					if (billDate.after(today)) {
						
						JOptionPane.showMessageDialog(PatientBillEdit.this,
								MessageBundle.getMessage("angal.newbill.billsinfuturenotallowed"), //$NON-NLS-1$
								MessageBundle.getMessage("angal.newbill.title"), //$NON-NLS-1$
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					if (billDate.after(firstPay)) {
						
						JOptionPane.showMessageDialog(PatientBillEdit.this,
								MessageBundle.getMessage("angal.newbill.billdateafterfirstpayment"), //$NON-NLS-1$
								MessageBundle.getMessage("angal.newbill.title"), //$NON-NLS-1$
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					if (jTextFieldPatient.getText().equals("")) { //$NON-NLS-1$
						
						JOptionPane.showMessageDialog(PatientBillEdit.this,
								MessageBundle.getMessage("angal.newbill.pleaseinsertanameforthepatient"), //$NON-NLS-1$
								MessageBundle.getMessage("angal.newbill.title"), //$NON-NLS-1$
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					if (listSelected == null) {
						listSelected = lstArray.get(0); 
					}
					
					if (insert) {
						RememberDates.setLastBillDate(billDate);				//to remember for next INSERT
						Bill newBill = new Bill(0,							//Bill ID
								billDate,			 						//from calendar
								upDate,										//most recent payment 
								true,										//is a List?
								listSelected.getId(),						//List
								listSelected.getName(),						//List name
								thisBill.isPatient(),						//is a Patient?
								thisBill.isPatient() ? 
										thisBill.getPatID() : 0,			//Patient ID
								thisBill.isPatient() ? 
										patientSelected.getName() : 
										jTextFieldPatient.getText(),		//Patient Name
								paid ? "C" : "O",							//CLOSED or OPEN
								total,										//Total
								balance);									//Balance
						
						billID = billManager.newBill(newBill);
						if (billID == 0) {
							JOptionPane.showMessageDialog(PatientBillEdit.this,
									MessageBundle.getMessage("angal.newbill.failedtosavebill"), //$NON-NLS-1$
									MessageBundle.getMessage("angal.newbill.title"), //$NON-NLS-1$
									JOptionPane.ERROR_MESSAGE);
							return;
						} else {
							newBill.setId(billID);
							billManager.newBillItems(billID, billItems);
							billManager.newBillPayments(billID, payItems);
							fireBillInserted(newBill);
						}
					} else {
						billID = thisBill.getId();
						Bill updateBill = new Bill(billID,			//Bill ID
								billDate,							//from calendar
								upDate,								//most recent payment
								true,								//is a List?
								listSelected.getId(),				//List
								listSelected.getName(),				//List name
								thisBill.isPatient(),				//is a Patient?
								thisBill.isPatient() ?
										thisBill.getPatID() : 0,	//Patient ID
								thisBill.isPatient() ?
										thisBill.getPatName() :
										jTextFieldPatient.getText(),//Patient Name
								paid ? "C" : "O",					//CLOSED or OPEN
								total,								//Total
								balance);							//Balance
						
						billManager.updateBill(updateBill);
						billManager.newBillItems(billID, billItems);
						billManager.newBillPayments(billID, payItems);
						fireBillInserted(updateBill);
						
					}
					if (paid && GeneralData.RECEIPTPRINTER) {
						
						TxtPrinter.getTxtPrinter();
						if (TxtPrinter.PRINT_AS_PAID)
							new GenericReportBill(billID, GeneralData.PATIENTBILL, false, !TxtPrinter.PRINT_WITHOUT_ASK);
					}
					dispose();
				}
			});
		}
		return jButtonSave;
	}
	
	private JButton getJButtonPaid() {
		if (jButtonPaid == null) {
			jButtonPaid = new JButton();
			jButtonPaid.setText(MessageBundle.getMessage("angal.newbill.paid")); //$NON-NLS-1$
			jButtonPaid.setMnemonic(KeyEvent.VK_A);
			jButtonPaid.setMaximumSize(new Dimension(ButtonWidth, ButtonHeight));
			jButtonPaid.setIcon(new ImageIcon("rsc/icons/ok_button.png")); //$NON-NLS-1$
			if(insert) jButtonPaid.setEnabled(false);
			jButtonPaid.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					
					GregorianCalendar datePay = new GregorianCalendar();
					GregorianCalendar lastPay = new GregorianCalendar();
					
					if (jTextFieldPatient.getText().equals("")) { //$NON-NLS-1$
						
						JOptionPane.showMessageDialog(PatientBillEdit.this,
								MessageBundle.getMessage("angal.newbill.pleaseinsertanameforthepatient"), //$NON-NLS-1$
								MessageBundle.getMessage("angal.newbill.title"), //$NON-NLS-1$
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					Icon icon = new ImageIcon("rsc/icons/money_dialog.png"); //$NON-NLS-1$
					int ok = JOptionPane.showConfirmDialog(PatientBillEdit.this,
							MessageBundle.getMessage("angal.newbill.doyouwanttosetaspaidcurrentbill"), //$NON-NLS-1$
							MessageBundle.getMessage("angal.newbill.paid"),  //$NON-NLS-1$
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE,
							icon);
					if (ok == JOptionPane.NO_OPTION) return;
					
					if (balance > 0.) {
						if (billDate.before(today)) { //if Bill is in the past the user will be asked for PAID date
							
							icon = new ImageIcon("rsc/icons/calendar_dialog.png"); //$NON-NLS-1$
	
							JDateChooser datePayChooser = new JDateChooser(new Date());
							datePayChooser.setLocale(new Locale(GeneralData.LANGUAGE));
							datePayChooser.setDateFormatString("dd/MM/yy - HH:mm:ss"); //$NON-NLS-1$
							
					        int r = JOptionPane.showConfirmDialog(PatientBillEdit.this, 
					        		datePayChooser, 
					        		MessageBundle.getMessage("angal.newbill.dateofpayment"), 
					        		JOptionPane.OK_CANCEL_OPTION, 
					        		JOptionPane.PLAIN_MESSAGE,
					        		icon);
	
					        if (r == JOptionPane.OK_OPTION) {
					        	datePay.setTime(datePayChooser.getDate());
					        } else {
					            return;
					        }
					        
						    GregorianCalendar now = new GregorianCalendar();
						    
						    if (payItems.size() > 0) { 
						    	lastPay = payItems.get(payItems.size()-1).getDate();
							} else {
								lastPay = billDate;
							}
						    
						    if (datePay.before(lastPay)) {
						    	JOptionPane.showMessageDialog(PatientBillEdit.this, 
										MessageBundle.getMessage("angal.newbill.datebeforelastpayment"), //$NON-NLS-1$
										MessageBundle.getMessage("angal.newbill.invaliddate"), //$NON-NLS-1$
										JOptionPane.ERROR_MESSAGE);
						    	return;
						    } else if (datePay.after(now)) {
						    	JOptionPane.showMessageDialog(PatientBillEdit.this, 
										MessageBundle.getMessage("angal.newbill.payementinthefuturenotallowed"), //$NON-NLS-1$
										MessageBundle.getMessage("angal.newbill.invaliddate"), //$NON-NLS-1$
										JOptionPane.ERROR_MESSAGE);
						    	return;
						    } else {
						    	addPayment(datePay, balance);
						    }
							
						} else {
							datePay = new GregorianCalendar();
							addPayment(datePay, balance);
						}
					}
					paid = true;
					updateBalance();
					jButtonSave.doClick();
				}
			});
		}
		return jButtonPaid;
	}
	
	private JButton getJButtonClose() {
		if (jButtonClose == null) {
			jButtonClose = new JButton();
			jButtonClose.setText(MessageBundle.getMessage("angal.newbill.close")); //$NON-NLS-1$
			jButtonClose.setMnemonic(KeyEvent.VK_C);
			jButtonClose.setMaximumSize(new Dimension(ButtonWidth, ButtonHeight));
			jButtonClose.setIcon(new ImageIcon("rsc/icons/close_button.png")); //$NON-NLS-1$
			jButtonClose.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (modified) {
						
						Icon icon = new ImageIcon("rsc/icons/save_dialog.png"); //$NON-NLS-1$
						int ok = JOptionPane.showConfirmDialog(PatientBillEdit.this, 
								MessageBundle.getMessage("angal.newbill.billhasbeenchangedwouldyouliketosavechanges"), 
								MessageBundle.getMessage("angal.newbill.save"), 
								JOptionPane.YES_NO_CANCEL_OPTION, 
								JOptionPane.WARNING_MESSAGE, 
								icon);
						if (ok == JOptionPane.YES_OPTION) {
							jButtonSave.doClick();
						} else if (ok == JOptionPane.NO_OPTION) {
							//fireBillInserted(null);
							dispose();
						} else return;
					} else {
						
						//fireBillInserted(null);
						dispose();
					}
				}
			});
		}
		return jButtonClose;
	}

	private JButton getJButtonAddRefund() {
		if (jButtonAddRefund == null) {
			jButtonAddRefund = new JButton();
			jButtonAddRefund.setText(MessageBundle.getMessage("angal.newbill.refund")); //$NON-NLS-1$
			jButtonAddRefund.setMaximumSize(new Dimension(ButtonWidthPayment, ButtonHeight));
			jButtonAddRefund.setHorizontalAlignment(SwingConstants.LEFT);
			jButtonAddRefund.setIcon(new ImageIcon("rsc/icons/plus_button.png")); //$NON-NLS-1$
			jButtonAddRefund.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					
					Icon icon = new ImageIcon("rsc/icons/money_dialog.png"); //$NON-NLS-1$
					Double amount = 0.;
					
					GregorianCalendar datePay = new GregorianCalendar();
					
					String quantity = (String) JOptionPane.showInputDialog(
		                    PatientBillEdit.this,
		                    MessageBundle.getMessage("angal.newbill.insertquantity"), //$NON-NLS-1$
		                    MessageBundle.getMessage("angal.newbill.quantity"), //$NON-NLS-1$
		                    JOptionPane.PLAIN_MESSAGE,
		                    icon,
		                    null,
		                    amount);
					if (quantity != null) {
						try {
							amount = -Double.valueOf(quantity);
							if (amount == 0.) return;
						} catch (Exception eee) {
							JOptionPane.showMessageDialog(PatientBillEdit.this, 
									MessageBundle.getMessage("angal.newbill.invalidquantitypleasetryagain"), //$NON-NLS-1$
									MessageBundle.getMessage("angal.newbill.invalidquantity"), //$NON-NLS-1$
									JOptionPane.ERROR_MESSAGE);
							return;
						}
					} else return;
					
					if (billDate.before(today)) { //if is a bill in the past the user will be asked for date of payment
						
						icon = new ImageIcon("rsc/icons/calendar_dialog.png"); //$NON-NLS-1$
						
						JDateChooser datePayChooser = new JDateChooser(new Date());
						datePayChooser.setLocale(new Locale(GeneralData.LANGUAGE));
						datePayChooser.setDateFormatString("dd/MM/yy - HH:mm:ss"); //$NON-NLS-1$
						
				        int r = JOptionPane.showConfirmDialog(PatientBillEdit.this, 
				        		datePayChooser, 
				        		MessageBundle.getMessage("angal.newbill.dateofpayment"), 
				        		JOptionPane.OK_CANCEL_OPTION, 
				        		JOptionPane.PLAIN_MESSAGE);

				        if (r == JOptionPane.OK_OPTION) {
				        	datePay.setTime(datePayChooser.getDate());
				        } else {
				            return;
				        }

					    GregorianCalendar now = new GregorianCalendar();
					    
					    if (datePay.before(billDate)) {
					    	JOptionPane.showMessageDialog(PatientBillEdit.this, 
									MessageBundle.getMessage("angal.newbill.paymentbeforebilldate"), //$NON-NLS-1$
									MessageBundle.getMessage("angal.newbill.invaliddate"), //$NON-NLS-1$
									JOptionPane.ERROR_MESSAGE);
					    } else if (datePay.after(now)) {
					    	JOptionPane.showMessageDialog(PatientBillEdit.this, 
									MessageBundle.getMessage("angal.newbill.payementinthefuturenotallowed"), //$NON-NLS-1$
									MessageBundle.getMessage("angal.newbill.invaliddate"), //$NON-NLS-1$
									JOptionPane.ERROR_MESSAGE);
					    } else {
					    	addPayment(datePay, amount);
					    }
					} else {
						datePay = new GregorianCalendar();
						addPayment(datePay, amount);
					}
				}
			});
		}
		return jButtonAddRefund;
	}
	
	private JButton getJButtonAddPayment() {
		if (jButtonAddPayment == null) {
			jButtonAddPayment = new JButton();
			jButtonAddPayment.setText(MessageBundle.getMessage("angal.newbill.payment")); //$NON-NLS-1$
			jButtonAddPayment.setMnemonic(KeyEvent.VK_Y);
			jButtonAddPayment.setMaximumSize(new Dimension(ButtonWidthPayment, ButtonHeight));
			jButtonAddPayment.setHorizontalAlignment(SwingConstants.LEFT);
			jButtonAddPayment.setIcon(new ImageIcon("rsc/icons/plus_button.png")); //$NON-NLS-1$
			jButtonAddPayment.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					
					Icon icon = new ImageIcon("rsc/icons/money_dialog.png"); //$NON-NLS-1$
					Double amount = 0.;
					
					GregorianCalendar datePay = new GregorianCalendar();
					
					String quantity = (String) JOptionPane.showInputDialog(
		                    PatientBillEdit.this,
		                    MessageBundle.getMessage("angal.newbill.insertquantity"), //$NON-NLS-1$
		                    MessageBundle.getMessage("angal.newbill.quantity"), //$NON-NLS-1$
		                    JOptionPane.PLAIN_MESSAGE,
		                    icon,
		                    null,
		                    amount);
					if (quantity != null) {
						try {
							amount = Double.valueOf(quantity);
							if (amount == 0.) return;
						} catch (Exception eee) {
							JOptionPane.showMessageDialog(PatientBillEdit.this, 
									MessageBundle.getMessage("angal.newbill.invalidquantitypleasetryagain"), //$NON-NLS-1$
									MessageBundle.getMessage("angal.newbill.invalidquantity"), //$NON-NLS-1$
									JOptionPane.ERROR_MESSAGE);
							return;
						}
					} else return;
					
					if (billDate.before(today)) { //if is a bill in the past the user will be asked for date of payment
						
						icon = new ImageIcon("rsc/icons/calendar_dialog.png"); //$NON-NLS-1$
						
						JDateChooser datePayChooser = new JDateChooser(new Date());
						datePayChooser.setLocale(new Locale(GeneralData.LANGUAGE));
						datePayChooser.setDateFormatString("dd/MM/yy - HH:mm:ss"); //$NON-NLS-1$
						
				        int r = JOptionPane.showConfirmDialog(PatientBillEdit.this, 
				        		datePayChooser, 
				        		MessageBundle.getMessage("angal.newbill.dateofpayment"), 
				        		JOptionPane.OK_CANCEL_OPTION, 
				        		JOptionPane.PLAIN_MESSAGE);

				        if (r == JOptionPane.OK_OPTION) {
				        	datePay.setTime(datePayChooser.getDate());
				        } else {
				            return;
				        }

					    GregorianCalendar now = new GregorianCalendar();
					    
					    if (datePay.before(billDate)) {
					    	JOptionPane.showMessageDialog(PatientBillEdit.this, 
									MessageBundle.getMessage("angal.newbill.paymentbeforebilldate"), //$NON-NLS-1$
									MessageBundle.getMessage("angal.newbill.invaliddate"), //$NON-NLS-1$
									JOptionPane.ERROR_MESSAGE);
					    } else if (datePay.after(now)) {
					    	JOptionPane.showMessageDialog(PatientBillEdit.this, 
									MessageBundle.getMessage("angal.newbill.payementinthefuturenotallowed"), //$NON-NLS-1$
									MessageBundle.getMessage("angal.newbill.invaliddate"), //$NON-NLS-1$
									JOptionPane.ERROR_MESSAGE);
					    } else {
					    	addPayment(datePay, amount);
					    }
					} else {
						datePay = new GregorianCalendar();
						addPayment(datePay, amount);
					}
				}
			});
		}
		return jButtonAddPayment;
	}

	private JButton getJButtonRemovePayment() {
		if (jButtonRemovePayment == null) {
			jButtonRemovePayment = new JButton();
			jButtonRemovePayment.setText(MessageBundle.getMessage("angal.newbill.removepayment")); //$NON-NLS-1$
			//jButtonRemovePayment.setMnemonic(KeyEvent.VK_Y);
			jButtonRemovePayment.setMaximumSize(new Dimension(ButtonWidthPayment, ButtonHeight));
			jButtonRemovePayment.setHorizontalAlignment(SwingConstants.LEFT);
			jButtonRemovePayment.setIcon(new ImageIcon("rsc/icons/delete_button.png")); //$NON-NLS-1$
			jButtonRemovePayment.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					int row = jTablePayment.getSelectedRow();
					if (row > -1) {
						removePayment(row);
					}
				}
			});
		}
		return jButtonRemovePayment;
	}
	
	private JButton getJButtonAddOther() {
		if (jButtonAddOther == null) {
			jButtonAddOther = new JButton();
			jButtonAddOther.setText(MessageBundle.getMessage("angal.newbill.other")); //$NON-NLS-1$
			jButtonAddOther.setMnemonic(KeyEvent.VK_T);
			jButtonAddOther.setMaximumSize(new Dimension(ButtonWidthBill, ButtonHeight));
			jButtonAddOther.setHorizontalAlignment(SwingConstants.LEFT);
			jButtonAddOther.setIcon(new ImageIcon("rsc/icons/plus_button.png")); //$NON-NLS-1$
			jButtonAddOther.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					
					HashMap<Integer,PricesOthers> othersHashMap = new HashMap<Integer,PricesOthers>();
					for (PricesOthers other : othPrices) {
				    	othersHashMap.put(other.getId(), other);
				    }
					
					ArrayList<Price> othArray = new ArrayList<Price>();
					for (Price price : prcListArray) {
						
						if (price.getGroup().equals("OTH")) //$NON-NLS-1$
							othArray.add(price);
					}
					
					Icon icon = new ImageIcon("rsc/icons/plus_dialog.png"); //$NON-NLS-1$
					Price oth = (Price)JOptionPane.showInputDialog(
					                    PatientBillEdit.this,
					                    MessageBundle.getMessage("angal.newbill.pleaseselectanitem"), //$NON-NLS-1$
					                    MessageBundle.getMessage("angal.newbill.item"), //$NON-NLS-1$
					                    JOptionPane.PLAIN_MESSAGE,
					                    icon,
					                    othArray.toArray(),
					                    ""); //$NON-NLS-1$
					
					if (oth != null) {
						if (othersHashMap.get(Integer.valueOf(oth.getItem())).isUndefined()) {
							icon = new ImageIcon("rsc/icons/money_dialog.png"); //$NON-NLS-1$
							String price = (String)JOptionPane.showInputDialog(
				                    PatientBillEdit.this,
				                    MessageBundle.getMessage("angal.newbill.howmuchisit"), //$NON-NLS-1$
				                    MessageBundle.getMessage("angal.newbill.undefined"), //$NON-NLS-1$
				                    JOptionPane.PLAIN_MESSAGE,
				                    icon,
				                    null,
									"0"); //$NON-NLS-1$
							try {
								if (price == null) return;
								double amount = Double.valueOf(price);
								oth.setPrice(amount);
							} catch (Exception eee) {
								JOptionPane.showMessageDialog(PatientBillEdit.this, 
										MessageBundle.getMessage("angal.newbill.invalidpricepleasetryagain"), //$NON-NLS-1$
										MessageBundle.getMessage("angal.newbill.invalidprice"), //$NON-NLS-1$
										JOptionPane.ERROR_MESSAGE);
								return;
							}
						}
						if (othersHashMap.get(Integer.valueOf(oth.getItem())).isDischarge()) {
							double amount = oth.getPrice();
							oth.setPrice(-amount);
						}
						if (othersHashMap.get(Integer.valueOf(oth.getItem())).isDaily()) {
							int qty = 1;
							icon = new ImageIcon("rsc/icons/calendar_dialog.png"); //$NON-NLS-1$
							String quantity = (String) JOptionPane.showInputDialog(
				                    PatientBillEdit.this,
				                    MessageBundle.getMessage("angal.newbill.howmanydays"), //$NON-NLS-1$
				                    MessageBundle.getMessage("angal.newbill.days"), //$NON-NLS-1$
				                    JOptionPane.PLAIN_MESSAGE,
				                    icon,
				                    null,
				                    qty);
							try {
								if (quantity == null || quantity.equals("")) return;
								qty = Integer.valueOf(quantity);
								addItem(oth, qty);
							} catch (Exception eee) {
								JOptionPane.showMessageDialog(PatientBillEdit.this, 
										MessageBundle.getMessage("angal.newbill.invalidquantitypleasetryagain"), //$NON-NLS-1$
										MessageBundle.getMessage("angal.newbill.invalidquantity"), //$NON-NLS-1$
										JOptionPane.ERROR_MESSAGE);
							}
						} else {
							addItem(oth, 1);
						}
					}
				}
			});
		}
		return jButtonAddOther;
	}

	private JButton getJButtonAddExam() {
		if (jButtonAddExam == null) {
			jButtonAddExam = new JButton();
			jButtonAddExam.setText(MessageBundle.getMessage("angal.newbill.exam")); //$NON-NLS-1$
			jButtonAddExam.setMnemonic(KeyEvent.VK_E);
			jButtonAddExam.setMaximumSize(new Dimension(ButtonWidthBill, ButtonHeight));
			jButtonAddExam.setHorizontalAlignment(SwingConstants.LEFT);
			jButtonAddExam.setIcon(new ImageIcon("rsc/icons/plus_button.png")); //$NON-NLS-1$
			jButtonAddExam.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					
					ArrayList<Price> exaArray = new ArrayList<Price>();
					for (Price price : prcListArray) {
						
						if (price.getGroup().equals("EXA")) //$NON-NLS-1$
							exaArray.add(price);
					}
					
					Icon icon = new ImageIcon("rsc/icons/exam_dialog.png"); //$NON-NLS-1$
					Price exa = (Price)JOptionPane.showInputDialog(
					                    PatientBillEdit.this,
					                    MessageBundle.getMessage("angal.newbill.selectanexam"), //$NON-NLS-1$
					                    MessageBundle.getMessage("angal.newbill.exam"), //$NON-NLS-1$
					                    JOptionPane.PLAIN_MESSAGE,
					                    icon,
					                    exaArray.toArray(),
					                    ""); //$NON-NLS-1$
					addItem(exa, 1);
				}
			});
		}
		return jButtonAddExam;
	}

	private JButton getJButtonAddOperation() {
		if (jButtonAddOperation == null) {
			jButtonAddOperation = new JButton();
			jButtonAddOperation.setText(MessageBundle.getMessage("angal.newbill.operation")); //$NON-NLS-1$
			jButtonAddOperation.setMnemonic(KeyEvent.VK_O);
			jButtonAddOperation.setMaximumSize(new Dimension(ButtonWidthBill, ButtonHeight));
			jButtonAddOperation.setHorizontalAlignment(SwingConstants.LEFT);
			jButtonAddOperation.setIcon(new ImageIcon("rsc/icons/plus_button.png")); //$NON-NLS-1$
			jButtonAddOperation.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					
					ArrayList<Price> opeArray = new ArrayList<Price>();
					for (Price price : prcListArray) {
						
						if (price.getGroup().equals("OPE")) //$NON-NLS-1$
							opeArray.add(price);
					}
					
					Icon icon = new ImageIcon("rsc/icons/operation_dialog.png"); //$NON-NLS-1$
					Price ope = (Price)JOptionPane.showInputDialog(
					                    PatientBillEdit.this,
					                    MessageBundle.getMessage("angal.newbill.selectanoperation"), //$NON-NLS-1$
					                    MessageBundle.getMessage("angal.newbill.operation"), //$NON-NLS-1$
					                    JOptionPane.PLAIN_MESSAGE,
					                    icon,
					                    opeArray.toArray(),
					                    ""); //$NON-NLS-1$
					addItem(ope, 1);
				}
			});
		}
		return jButtonAddOperation;
	}

	private JButton getJButtonAddMedical() {
		if (jButtonAddMedical == null) {
			jButtonAddMedical = new JButton();
			jButtonAddMedical.setText(MessageBundle.getMessage("angal.newbill.medical")); //$NON-NLS-1$
			jButtonAddMedical.setMnemonic(KeyEvent.VK_M);
			jButtonAddMedical.setMaximumSize(new Dimension(ButtonWidthBill, ButtonHeight));
			jButtonAddMedical.setHorizontalAlignment(SwingConstants.LEFT);
			jButtonAddMedical.setIcon(new ImageIcon("rsc/icons/plus_button.png")); //$NON-NLS-1$
			jButtonAddMedical.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					
					ArrayList<Price> medArray = new ArrayList<Price>();
					for (Price price : prcListArray) {
						
						if (price.getGroup().equals("MED")) //$NON-NLS-1$
							medArray.add(price);
					}
					
					Icon icon = new ImageIcon("rsc/icons/medical_dialog.png"); //$NON-NLS-1$
					Price med = (Price)JOptionPane.showInputDialog(
					                    PatientBillEdit.this,
					                    MessageBundle.getMessage("angal.newbill.selectamedical"), //$NON-NLS-1$
					                    MessageBundle.getMessage("angal.newbill.medical"), //$NON-NLS-1$
					                    JOptionPane.PLAIN_MESSAGE,
					                    icon,
					                    medArray.toArray(),
					                    ""); //$NON-NLS-1$
					if (med != null) {
						int qty = 1;
						String quantity = (String) JOptionPane.showInputDialog(
			                    PatientBillEdit.this,
			                    MessageBundle.getMessage("angal.newbill.insertquantity"), //$NON-NLS-1$
			                    MessageBundle.getMessage("angal.newbill.quantity"), //$NON-NLS-1$
			                    JOptionPane.PLAIN_MESSAGE,
			                    icon,
			                    null,
			                    qty);
						try {
							if (quantity == null || quantity.equals("")) return;
							qty = Integer.valueOf(quantity);
							addItem(med, qty);
						} catch (Exception eee) {
							JOptionPane.showMessageDialog(PatientBillEdit.this, 
									MessageBundle.getMessage("angal.newbill.invalidquantitypleasetryagain"), //$NON-NLS-1$
									MessageBundle.getMessage("angal.newbill.invalidquantity"), //$NON-NLS-1$
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			});
		}
		return jButtonAddMedical;
	}
	
	private JButton getJButtonAddCustom() {
		if (jButtonCustom == null) {
			jButtonCustom = new JButton();
			jButtonCustom.setText(MessageBundle.getMessage("angal.newbill.custom")); //$NON-NLS-1$
			jButtonCustom.setMnemonic(KeyEvent.VK_U);
			jButtonCustom.setMaximumSize(new Dimension(ButtonWidthBill, ButtonHeight));
			jButtonCustom.setHorizontalAlignment(SwingConstants.LEFT);
			jButtonCustom.setIcon(new ImageIcon("rsc/icons/plus_button.png")); //$NON-NLS-1$
			jButtonCustom.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					double amount;
					Icon icon = new ImageIcon("rsc/icons/custom_dialog.png"); //$NON-NLS-1$
					String desc = (String)JOptionPane.showInputDialog(
					                    PatientBillEdit.this,
					                    MessageBundle.getMessage("angal.newbill.chooseadescription"), //$NON-NLS-1$
					                    MessageBundle.getMessage("angal.newbill.customitem"), //$NON-NLS-1$
					                    JOptionPane.PLAIN_MESSAGE,
					                    icon,
					                    null,
										MessageBundle.getMessage("angal.newbill.newdescription")); //$NON-NLS-1$
					if (desc == null || desc.equals("")) { //$NON-NLS-1$
						return;
					} else {
						icon = new ImageIcon("rsc/icons/money_dialog.png"); //$NON-NLS-1$
						String price = (String)JOptionPane.showInputDialog(
			                    PatientBillEdit.this,
			                    MessageBundle.getMessage("angal.newbill.howmuchisit"), //$NON-NLS-1$
			                    MessageBundle.getMessage("angal.newbill.customitem"), //$NON-NLS-1$
			                    JOptionPane.PLAIN_MESSAGE,
			                    icon,
			                    null,
								"0"); //$NON-NLS-1$
						try {
							amount = Double.valueOf(price);
						} catch (Exception eee) {
							JOptionPane.showMessageDialog(PatientBillEdit.this, 
									MessageBundle.getMessage("angal.newbill.invalidpricepleasetryagain"), //$NON-NLS-1$
									MessageBundle.getMessage("angal.newbill.invalidprice"), //$NON-NLS-1$
									JOptionPane.ERROR_MESSAGE);
							return;
						}
						
					}
					
					BillItems newItem = new BillItems(0,
							billID,
							false,
							"", //$NON-NLS-1$
							desc,
							amount,
							1);
					addItem(newItem);
				}
			});
		}
		return jButtonCustom;
	}
	
	private JButton getJButtonRemoveItem() {
		if (jButtonRemoveItem == null) {
			jButtonRemoveItem = new JButton();
			jButtonRemoveItem.setText(MessageBundle.getMessage("angal.newbill.removeitem")); //$NON-NLS-1$
			//jButtonRemoveItem.setMnemonic(KeyEvent.VK_R);
			jButtonRemoveItem.setMaximumSize(new Dimension(ButtonWidthBill, ButtonHeight));
			jButtonRemoveItem.setHorizontalAlignment(SwingConstants.LEFT);
			jButtonRemoveItem.setIcon(new ImageIcon("rsc/icons/delete_button.png")); //$NON-NLS-1$
			jButtonRemoveItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					int row = jTableBill.getSelectedRow();
					if (row > -1) {
						removeItem(row);
					}
				}
			});
		}
		return jButtonRemoveItem;
	}
	
	private void updateTotal() {
		total = 0.;
		for (BillItems item : billItems) {
			
			total+=item.getItemAmount()*item.getItemQuantity(); 
		}
	}
	
	private void updateBalance() {
		balance = 0.;
		double payments = 0.;
		for (BillPayments pay : payItems) {
			
			payments+=pay.getAmount(); 
		}
		balance = total - payments;
		if (jButtonPaid != null) jButtonPaid.setEnabled(balance >= 0.);
		if (jButtonBalance != null) jButtonBalance.setEnabled(balance > 0.);
	}

	private void addItem(Price prc, int qty) {
		if (prc != null) {
			double amount = prc.getPrice();
			BillItems item = new BillItems(0, 
					billID, 
					true, 
					prc.getGroup()+prc.getItem(),
					prc.getDesc(),
					amount,
					qty);
			billItems.add(item);
			modified = true;
			jTableBill.updateUI();
			updateTotal();
			updateBalance();
			jTableTotal.getModel().setValueAt(total, 0, 1);
			jTableBalance.getModel().setValueAt(balance, 0, 1);
		}
	}
	
	private void updateUI() {
		
		jCalendarDate.setDate(thisBill.getDate().getTime());
		jTextFieldPatient.setText(patientSelected.getName());
		jTextFieldPatient.setEditable(false);
		jButtonPickPatient.setText(MessageBundle.getMessage("angal.newbill.changepatient")); //$NON-NLS-1$
		jButtonPickPatient.setToolTipText(MessageBundle.getMessage("angal.newbill.changethepatientassociatedwiththisbill")); //$NON-NLS-1$
		jButtonTrashPatient.setEnabled(true);
		jTableBill.updateUI();
		jTablePayment.updateUI();
		updateTotal();
		updateBalance();
		jTableTotal.getModel().setValueAt(total, 0, 1);
		jTableBalance.getModel().setValueAt(balance, 0, 1);
		
	}
	
	private void addItem(BillItems item) {
		if (item != null) {
			billItems.add(item);
			modified = true;
			jTableBill.updateUI();
			updateTotal();
			updateBalance();
			jTableTotal.getModel().setValueAt(total, 0, 1);
			jTableBalance.getModel().setValueAt(balance, 0, 1);
		}
	}
	
	private void addPayment(GregorianCalendar datePay, Double qty) {
		if (qty != 0.) {
			BillPayments pay = new BillPayments(0,
					billID,
					datePay,
					qty,
					user);
			payItems.add(pay);
			modified = true;
			Collections.sort(payItems);
			jTablePayment.updateUI();
			updateBalance();
			jTableBalance.getModel().setValueAt(balance, 0, 1);
		}
	}
	
	private void removeItem(int row) {
		if (row != -1 && row >= billItemsSaved) {
			billItems.remove(row);
			jTableBill.updateUI();
			jTableBill.clearSelection();
			updateTotal();
			updateBalance();
			jTableTotal.getModel().setValueAt(total, 0, 1);
			jTableBalance.getModel().setValueAt(balance, 0, 1);
		} else {
			JOptionPane.showMessageDialog(null,
					MessageBundle.getMessage("angal.newbill.youcannotdeletealreadysaveditems"), //$NON-NLS-1$
					MessageBundle.getMessage("angal.newbill.title"), //$NON-NLS-1$
					JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	private void removePayment(int row) {
		if (row != -1 && row >= payItemsSaved) {
			payItems.remove(row);
			jTablePayment.updateUI();
			jTablePayment.clearSelection();
			updateBalance();
			jTableBalance.getModel().setValueAt(balance, 0, 1);
		} else {
			JOptionPane.showMessageDialog(null,
					MessageBundle.getMessage("angal.newbill.youcannotdeletepastpayments"), //$NON-NLS-1$
					MessageBundle.getMessage("angal.newbill.title"), //$NON-NLS-1$
					JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	public class BillTableModel implements TableModel {
		
		public BillTableModel() {
			
//			HashMap<String,Price> priceHashTable = new HashMap<String,Price>();
			prcListArray = new ArrayList<Price>();
			//billItems = new ArrayList<BillItems>();
			
			/*
			 * seleziona i prezzi del listino selezionato.
			 * se nessun listino e' selezionato (new bill) si prende il primo.
			 */
			for (Price price : prcArray) {
				if (listSelected == null) listSelected = lstArray.get(0);
		    	if (price.getList() == listSelected.getId()) 
		    		prcListArray.add(price);
		    }
			
			/*
			 * crea una hastTable con i prezzi selezionati.
			 */
//			for (Price price : prcListArray) {
//				priceHashTable.put(price.getList()+
//  					  price.getGroup()+
//  					  price.getItem(), price);
//		    }
			
			/*
			 * aggiorna gli item presenti nel bill.
			 */
//		    for (BillItems item : billItems) {
//				
//					if (item.isPrice()) {
//						Price p = priceHashTable.get(listSelected.getId()+item.getPriceID());
//						item.setItemDescription(p.getDesc());
//						item.setItemAmount(p.getPrice());
//					}
//			}
			
		    /*
		     * aggiorna i totali.
		     */
		    updateTotal();
			updateBalance();
		}
		
		public Class<?> getColumnClass(int i) {
			return billClasses[i].getClass();
		}

		
		public int getColumnCount() {
			return billClasses.length;
		}
		
		public int getRowCount() {
			if (billItems == null)
				return 0;
			return billItems.size();
		}
		
		public Object getValueAt(int r, int c) {
			BillItems item = billItems.get(r);
			if (c == -1) {
				return item;
			}
			if (c == 0) {
				return item.getItemDescription();
			}
			if (c == 1) {
				return item.getItemQuantity(); 
			}
			if (c == 2) {
				int qty = item.getItemQuantity();
				return item.getItemAmount()*qty; 
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
			return billColumnNames[columnIndex];
		}

		public void removeTableModelListener(TableModelListener l) {
		}

	}

	public class PaymentTableModel implements TableModel {
		
		public PaymentTableModel() {
			updateBalance();
		}
		
		public void addTableModelListener(TableModelListener l) {

		}
		
		public Class<?> getColumnClass(int columnIndex) {
			return paymentClasses[columnIndex].getClass();
		}
		
		public int getColumnCount() {
			return paymentClasses.length;
		}
		
		public String getColumnName(int columnIndex) {
			return null;
		}
		
		public int getRowCount() {
			return payItems.size();
		}
		
		public Object getValueAt(int r, int c) {
			if (c == -1) {
				return payItems.get(r);
			}
			if (c == 0) {
				return formatDateTime(payItems.get(r).getDate());
			}
			if (c == 1) {
				return payItems.get(r).getAmount(); 
			}
			return null;
		}
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return true;
		}
		
		public void removeTableModelListener(TableModelListener l) {
		}
		
		public void setValueAt(Object value, int rowIndex, int columnIndex) {
		}
	}

	public String formatDate(GregorianCalendar time) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");  //$NON-NLS-1$
		return format.format(time.getTime());
	}
	
	public String formatDateTime(GregorianCalendar time) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");  //$NON-NLS-1$
		return format.format(time.getTime());
	}
	
	public boolean isSameDay(GregorianCalendar billDate, GregorianCalendar today) {
		return (billDate.get(Calendar.YEAR) == today.get(Calendar.YEAR)) &&
			   (billDate.get(Calendar.MONTH) == today.get(Calendar.MONTH)) &&
			   (billDate.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH));
	}
}
