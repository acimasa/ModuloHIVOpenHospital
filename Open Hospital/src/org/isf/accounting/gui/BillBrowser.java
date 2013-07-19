package org.isf.accounting.gui;

/**
 * Browsing of table BILLS
 * 
 * @author Mwithi
 * 
 */
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.isf.accounting.gui.PatientBillEdit.PatientBillListener;
import org.isf.accounting.manager.BillBrowserManager;
import org.isf.accounting.model.Bill;
import org.isf.accounting.model.BillPayments;
import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.gui.MainMenu;
import org.isf.stat.manager.GenericReportBill;
import org.isf.stat.manager.GenericReportFromDateToDate;
import org.isf.stat.manager.GenericReportUserInDate;
import org.isf.utils.jobjects.ModalJFrame;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;

public class BillBrowser extends ModalJFrame implements PatientBillListener {

	public void billInserted(AWTEvent event) {
		jTableBills.setModel(new BillTableModel("ALL")); //$NON-NLS-1$
		jTablePending.setModel(new BillTableModel("O")); //$NON-NLS-1$
		jTableClosed.setModel(new BillTableModel("C")); //$NON-NLS-1$
		updateTotals();
		if (event != null) {
			Bill billInserted = (Bill) event.getSource();
			if (billInserted != null) {
				int insertedId = billInserted.getId();
				for (int i = 0; i < jTableBills.getRowCount(); i++) {
					Bill aBill = (Bill) jTableBills.getModel().getValueAt(i, -1);
					if (aBill.getId() == insertedId)
							jTableBills.getSelectionModel().setSelectionInterval(i, i);
				}
			}
		}
	}
	
	public void billFilter() {
		jTableBills.setModel(new BillTableModel("ALL")); //$NON-NLS-1$
		jTablePending.setModel(new BillTableModel("O")); //$NON-NLS-1$
		jTableClosed.setModel(new BillTableModel("C")); //$NON-NLS-1$
		updateTotals();
	}
	
	private static final long serialVersionUID = 1L;
	private JTabbedPane jTabbedPaneBills;
	private JTable jTableBills;
	private JScrollPane jScrollPaneBills;
	private JTable jTablePending;
	private JScrollPane jScrollPanePending;
	private JTable jTableClosed;
	private JScrollPane jScrollPaneClosed;
	private JTable jTableToday;
	private JTable jTablePeriod;
	private JTable jTableUser;
	private JPanel jPanelRange;
	private JPanel jPanelButtons;
	private JPanel jPanelSouth;
	private JPanel jPanelTotals;
	private JButton jButtonNew;
	private JButton jButtonEdit;
	private JButton jButtonPrintReceipt;
	private JButton jButtonDelete;
	private JButton jButtonClose;
	private JButton jButtonReport;
	private JComboBox jComboUsers;
	private JMonthChooser jComboBoxMonths;
	private JYearChooser jComboBoxYears;
	private JLabel jLabelTo;
	private JLabel jLabelFrom;
	private JDateChooser jCalendarTo;
	private JDateChooser jCalendarFrom;
	private GregorianCalendar dateFrom = new GregorianCalendar();
	private GregorianCalendar dateTo = new GregorianCalendar();
	private GregorianCalendar dateToday0 = new GregorianCalendar();
	private GregorianCalendar dateToday24 = new GregorianCalendar();

	private JButton jButtonToday;
	
//	private String status;
	private String[] columsNames = {MessageBundle.getMessage("angal.billbrowser.id"),
			MessageBundle.getMessage("angal.billbrowser.date"), 
			MessageBundle.getMessage("angal.billbrowser.patientID"),
			MessageBundle.getMessage("angal.billbrowser.patient"), 
			MessageBundle.getMessage("angal.billbrowser.amount"), 
			MessageBundle.getMessage("angal.billbrowser.lastpayment"), 
			MessageBundle.getMessage("angal.billbrowser.status"), 
			MessageBundle.getMessage("angal.billbrowser.balance")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
	private int[] columsWidth = {50, 150, 50, 50, 100, 150, 50, 100};
	private int[] maxWidth = {150, 150, 150, 200, 100, 150, 50, 100};
	private boolean[] columsResizable = {false, false, false, true, false, false, false, false};
	private Class<?>[] columsClasses = {Integer.class, String.class, String.class, String.class, Double.class, String.class, String.class, Double.class};
	private boolean[] alignCenter = {true, true, true, false, false, true, true, false};
	private boolean[] boldCenter = {true, false, false, false, false, false, false, false};
	
//	private final int TabbedWidth = 800;
//	private final int TabbedHeight = 400;
//	private final int TotalWidth = TabbedWidth / 2;
//	private final int TotalHeight = 20;
	
	private Double totalToday;
	private Double balanceToday;
	private Double totalPeriod;
	private Double balancePeriod;
	private Double userToday;
	private Double userPeriod;
	private int month;
	private int year;
	
	//Bills & Payments
	private BillBrowserManager billManager = new BillBrowserManager();
	private ArrayList<Bill> billPeriod;
	private HashMap<Integer, Bill> mapBill = new HashMap<Integer, Bill>();
	private ArrayList<BillPayments> paymentsPeriod;
	
	//Users
	private String user = MainMenu.getUser();
	private ArrayList<String> users = billManager.getUsers();
	
	public BillBrowser() {
		initComponents();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void initComponents() {
		add(getJPanelRange(), BorderLayout.NORTH);
		add(getJTabbedPaneBills(), BorderLayout.CENTER);
		add(getJPanelSouth(), BorderLayout.SOUTH);
		setTitle(MessageBundle.getMessage("angal.billbrowser.title")); //$NON-NLS-1$
		setMinimumSize(new Dimension(800,500));
		addWindowListener(new WindowAdapter(){
			
			public void windowClosing(WindowEvent e) {
				//to free memory
				billPeriod.clear();
				mapBill.clear();
				users.clear();
				dispose();
			}			
		});
		pack();
	}

	private JPanel getJPanelSouth() {
		if (jPanelSouth == null) {
			jPanelSouth = new JPanel();
			jPanelSouth.setLayout(new BoxLayout(jPanelSouth, BoxLayout.X_AXIS));
			jPanelSouth.add(getJPanelTotals());
			jPanelSouth.add(getJPanelButtons());
		}
		return jPanelSouth;
	}

	private JPanel getJPanelTotals() {
		if (jPanelTotals == null) {
			jPanelTotals = new JPanel();
			jPanelTotals.setLayout(new BoxLayout(jPanelTotals, BoxLayout.Y_AXIS));
			jPanelTotals.add(getJTableToday());
			jPanelTotals.add(getJTablePeriod());
			if (!GeneralData.SINGLEUSER) jPanelTotals.add(getJTableUser());
			updateTotals();
		}
		return jPanelTotals;
	}

	private JLabel getJLabelTo() {
		if (jLabelTo == null) {
			jLabelTo = new JLabel();
			jLabelTo.setText(MessageBundle.getMessage("angal.billbrowser.to")); //$NON-NLS-1$
		}
		return jLabelTo;
	}

	private JDateChooser getJCalendarFrom() {
		if (jCalendarFrom == null) {
			dateFrom.set(GregorianCalendar.HOUR_OF_DAY, 0);
			dateFrom.set(GregorianCalendar.MINUTE, 0);
			dateFrom.set(GregorianCalendar.SECOND, 0);
			dateToday0.setTime(dateFrom.getTime());
			jCalendarFrom = new JDateChooser(dateFrom.getTime()); // Calendar
			jCalendarFrom.setLocale(new Locale(GeneralData.LANGUAGE));
			jCalendarFrom.setDateFormatString("dd/MM/yy"); //$NON-NLS-1$
			jCalendarFrom.addPropertyChangeListener("date", new PropertyChangeListener() { //$NON-NLS-1$

				public void propertyChange(PropertyChangeEvent evt) {
					jCalendarFrom.setDate((Date) evt.getNewValue());
					dateFrom.setTime((Date) evt.getNewValue());
					dateFrom.set(GregorianCalendar.HOUR_OF_DAY, 0);
					dateFrom.set(GregorianCalendar.MINUTE, 0);
					dateFrom.set(GregorianCalendar.SECOND, 0);
					dateToday0.setTime(dateFrom.getTime());
					jButtonToday.setEnabled(true);
					//billFilter();
					billInserted(null);
				}
			});
		}			
		return jCalendarFrom;
	}

	private JDateChooser getJCalendarTo() {
		if (jCalendarTo == null) {
			dateTo.set(GregorianCalendar.HOUR_OF_DAY, 23);
			dateTo.set(GregorianCalendar.MINUTE, 59);
			dateTo.set(GregorianCalendar.SECOND, 59);
			dateToday24.setTime(dateTo.getTime());
			jCalendarTo = new JDateChooser(dateTo.getTime()); // Calendar
			jCalendarTo.setLocale(new Locale(GeneralData.LANGUAGE));
			jCalendarTo.setDateFormatString("dd/MM/yy"); //$NON-NLS-1$
			jCalendarTo.addPropertyChangeListener("date", new PropertyChangeListener() { //$NON-NLS-1$
				
				public void propertyChange(PropertyChangeEvent evt) {
					jCalendarTo.setDate((Date) evt.getNewValue());
					dateTo.setTime((Date) evt.getNewValue());
					dateTo.set(GregorianCalendar.HOUR_OF_DAY, 23);
					dateTo.set(GregorianCalendar.MINUTE, 59);
					dateTo.set(GregorianCalendar.SECOND, 59);
					dateToday24.setTime(dateTo.getTime());
					jButtonToday.setEnabled(true);
					billInserted(null);
				}
			});
		}
		return jCalendarTo;
	}
	
	private JLabel getJLabelFrom() {
		if (jLabelFrom == null) {
			jLabelFrom = new JLabel();
			jLabelFrom.setText(MessageBundle.getMessage("angal.billbrowser.from")); //$NON-NLS-1$
		}
		return jLabelFrom;
	}
	
	private JButton getJButtonReport() {
		if (jButtonReport == null) {
			jButtonReport = new JButton();
			jButtonReport.setText(MessageBundle.getMessage("angal.billbrowser.report")); //$NON-NLS-1$
			jButtonReport.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					
					ArrayList<String> options = new ArrayList<String>();
					options.add(MessageBundle.getMessage("angal.billbrowser.todayclosure"));
					options.add(MessageBundle.getMessage("angal.billbrowser.today"));
					options.add(MessageBundle.getMessage("angal.billbrowser.period"));
					options.add(MessageBundle.getMessage("angal.billbrowser.thismonth"));
					options.add(MessageBundle.getMessage("angal.billbrowser.othermonth"));
					
					Icon icon = new ImageIcon("rsc/icons/calendar_dialog.png"); //$NON-NLS-1$
					String option = (String) JOptionPane.showInputDialog(BillBrowser.this, 
							MessageBundle.getMessage("angal.billbrowser.pleaseselectareport"), 
							MessageBundle.getMessage("angal.billbrowser.report"), 
							JOptionPane.INFORMATION_MESSAGE, 
							icon, 
							options.toArray(), 
							options.get(0));
					
					if (option == null) return;
					
					String from = null;
					String to = null;
					
					int i = 0;
					
					if (options.indexOf(option) == i) {
							
							from = formatDateTimeReport(dateToday0);
							to = formatDateTimeReport(dateToday24);
							String user;
							if (GeneralData.SINGLEUSER) {
								user = "admin";
							} else {
								user = MainMenu.getUser();
							}
							new GenericReportUserInDate(from, to, user, "BillsReportUserInDate");
							return;
						}
					if (options.indexOf(option) == ++i) {
						
						from = formatDateTimeReport(dateToday0);
						to = formatDateTimeReport(dateToday24);
					}
					if (options.indexOf(option) == ++i) {
						
						from = formatDateTimeReport(dateFrom);
						to = formatDateTimeReport(dateTo);
					}
					if (options.indexOf(option) == ++i) {
						
						month = jComboBoxMonths.getMonth();
						GregorianCalendar thisMonthFrom = dateFrom;
						GregorianCalendar thisMonthTo = dateTo;
						thisMonthFrom.set(GregorianCalendar.MONTH, month);
						thisMonthFrom.set(GregorianCalendar.DAY_OF_MONTH, 1);
						thisMonthTo.set(GregorianCalendar.MONTH, month);
						thisMonthTo.set(GregorianCalendar.DAY_OF_MONTH, dateFrom.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
						from = formatDateTimeReport(thisMonthFrom);
						to = formatDateTimeReport(thisMonthTo);
					}
					if (options.indexOf(option) == ++i) {
						
						icon = new ImageIcon("rsc/icons/calendar_dialog.png"); //$NON-NLS-1$
						
						int month;
						JMonthChooser monthChooser = new JMonthChooser();
						monthChooser.setLocale(new Locale(GeneralData.LANGUAGE));
						
				        int r = JOptionPane.showConfirmDialog(BillBrowser.this, 
				        		monthChooser, 
				        		MessageBundle.getMessage("angal.billbrowser.month"), 
				        		JOptionPane.OK_CANCEL_OPTION, 
				        		JOptionPane.PLAIN_MESSAGE,
				        		icon);

				        if (r == JOptionPane.OK_OPTION) {
				        	month = monthChooser.getMonth();
				        } else {
				            return;
				        }
				        
						GregorianCalendar thisMonthFrom = dateFrom;
						GregorianCalendar thisMonthTo = dateTo;
						thisMonthFrom.set(GregorianCalendar.MONTH, month);
						thisMonthFrom.set(GregorianCalendar.DAY_OF_MONTH, 1);
						thisMonthTo.set(GregorianCalendar.MONTH, month);
						thisMonthTo.set(GregorianCalendar.DAY_OF_MONTH, dateFrom.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
						from = formatDateTimeReport(thisMonthFrom);
						to = formatDateTimeReport(thisMonthTo);
					}

					options = new ArrayList<String>();
					options.add(MessageBundle.getMessage("angal.billbrowser.shortreportonlybaddebts"));
					options.add(MessageBundle.getMessage("angal.billbrowser.fullreportallbills"));
										
					icon = new ImageIcon("rsc/icons/list_dialog.png"); //$NON-NLS-1$
					option = (String) JOptionPane.showInputDialog(BillBrowser.this, 
							MessageBundle.getMessage("angal.billbrowser.pleaseselectareport"), 
							MessageBundle.getMessage("angal.billbrowser.report"), 
							JOptionPane.INFORMATION_MESSAGE, 
							icon, 
							options.toArray(), 
							options.get(0));
					
					if (option == null) return;
					
					if (options.indexOf(option) == 0) {
						new GenericReportFromDateToDate(from, to,  GeneralData.BILLSREPORTMONTH);
					}
					if (options.indexOf(option) == 1) {
						new GenericReportFromDateToDate(from, to,  GeneralData.BILLSREPORT);
					}
				}
			});
		}
		return jButtonReport;
	}
	
	private JButton getJButtonClose() {
		if (jButtonClose == null) {
			jButtonClose = new JButton();
			jButtonClose.setText(MessageBundle.getMessage("angal.billbrowser.close")); //$NON-NLS-1$
			jButtonClose.setMnemonic(KeyEvent.VK_C);
			jButtonClose.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					//to free memory
					billPeriod.clear();
					mapBill.clear();
					users.clear();
					dispose();
				}
			});
		}
		return jButtonClose;
	}

	private JButton getJButtonEdit() {
		if (jButtonEdit == null) {
			jButtonEdit = new JButton();
			jButtonEdit.setText(MessageBundle.getMessage("angal.billbrowser.editbill")); //$NON-NLS-1$
			jButtonEdit.setMnemonic(KeyEvent.VK_E);
			jButtonEdit.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					try {
						if (jScrollPaneBills.isShowing()) {
							int rowSelected = jTableBills.getSelectedRow();
							Bill editBill = (Bill)jTableBills.getValueAt(rowSelected, -1);
							if (!editBill.getStatus().equals("C")) { //$NON-NLS-1$
								PatientBillEdit pbe = new PatientBillEdit(BillBrowser.this, editBill, false);
								pbe.addPatientBillListener(BillBrowser.this);
								pbe.setVisible(true);
							} else {
								/*JOptionPane.showMessageDialog(BillBrowser.this,
										MessageBundle.getMessage("angal.billbrowser.billalreadyclosed"),  //$NON-NLS-1$
										MessageBundle.getMessage("angal.hospital"),  //$NON-NLS-1$
										JOptionPane.CANCEL_OPTION);*/
								new GenericReportBill(editBill.getId(), GeneralData.PATIENTBILL);
							}
						}
						if (jScrollPanePending.isShowing()) {
							int rowSelected = jTablePending.getSelectedRow();
							Bill editBill = (Bill)jTablePending.getValueAt(rowSelected, -1);
							PatientBillEdit pbe = new PatientBillEdit(BillBrowser.this, editBill, false);
							pbe.addPatientBillListener(BillBrowser.this);
							pbe.setVisible(true);
						}
						if (jScrollPaneClosed.isShowing()) {
							int rowSelected = jTableClosed.getSelectedRow();
							Bill editBill = (Bill)jTableClosed.getValueAt(rowSelected, -1);
							new GenericReportBill(editBill.getId(), GeneralData.PATIENTBILL);
						}
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null,
								MessageBundle.getMessage("angal.billbrowser.pleaseselectabillfirst"), //$NON-NLS-1$
								MessageBundle.getMessage("angal.billbrowser.title"), //$NON-NLS-1$
								JOptionPane.PLAIN_MESSAGE);
					}
				}
			});
		}
		return jButtonEdit;
	}
	
	private JButton getJButtonPrintReceipt() {
		if (jButtonPrintReceipt == null) {
			jButtonPrintReceipt = new JButton();
			jButtonPrintReceipt.setText(MessageBundle.getMessage("angal.billbrowser.receipt")); //$NON-NLS-1$
			jButtonPrintReceipt.setMnemonic(KeyEvent.VK_R);
			jButtonPrintReceipt.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					try {
						if (jScrollPaneBills.isShowing()) {
							int rowSelected = jTableBills.getSelectedRow();
							Bill editBill = (Bill)jTableBills.getValueAt(rowSelected, -1);
							if (editBill.getStatus().equals("C")) { //$NON-NLS-1$
								new GenericReportBill(editBill.getId(), GeneralData.PATIENTBILL, false, true);
							} else {
								JOptionPane.showMessageDialog(BillBrowser.this,
										MessageBundle.getMessage("angal.billbrowser.billnotyetclosed"),  //$NON-NLS-1$
										MessageBundle.getMessage("angal.hospital"),  //$NON-NLS-1$
										JOptionPane.CANCEL_OPTION);
							}
						}
						if (jScrollPanePending.isShowing()) {
							int rowSelected = jTablePending.getSelectedRow();
							Bill editBill = (Bill)jTablePending.getValueAt(rowSelected, -1);
							PatientBillEdit pbe = new PatientBillEdit(BillBrowser.this, editBill, false);
							pbe.addPatientBillListener(BillBrowser.this);
							pbe.setVisible(true);
						}
						if (jScrollPaneClosed.isShowing()) {
							int rowSelected = jTableClosed.getSelectedRow();
							Bill editBill = (Bill)jTableClosed.getValueAt(rowSelected, -1);
							new GenericReportBill(editBill.getId(), GeneralData.PATIENTBILL);
						}
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null,
								MessageBundle.getMessage("angal.billbrowser.pleaseselectabillfirst"), //$NON-NLS-1$
								MessageBundle.getMessage("angal.billbrowser.title"), //$NON-NLS-1$
								JOptionPane.PLAIN_MESSAGE);
					}
				}
			});
		}
		return jButtonPrintReceipt;
	}

	private JButton getJButtonNew() {
		if (jButtonNew == null) {
			jButtonNew = new JButton();
			jButtonNew.setText(MessageBundle.getMessage("angal.billbrowser.newbill")); //$NON-NLS-1$
			jButtonNew.setMnemonic(KeyEvent.VK_N);
			jButtonNew.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					PatientBillEdit newBill = new PatientBillEdit(BillBrowser.this, new Bill(), true);
					newBill.addPatientBillListener(BillBrowser.this);
					newBill.setVisible(true);
				}
				
			});
		}
		return jButtonNew;
	}
	
	private JButton getJButtonDelete() {
		if (jButtonDelete == null) {
			jButtonDelete = new JButton();
			jButtonDelete.setText(MessageBundle.getMessage("angal.billbrowser.deletebill")); //$NON-NLS-1$
			jButtonDelete.setMnemonic(KeyEvent.VK_D);
			jButtonDelete.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					try {
						if (jScrollPaneBills.isShowing()) {
							int rowSelected = jTableBills.getSelectedRow();
							Bill deleteBill = (Bill)jTableBills.getValueAt(rowSelected, -1);
							int ok = JOptionPane.showConfirmDialog(null, 
									MessageBundle.getMessage("angal.billbrowser.doyoureallywanttodeletetheselectedbill"),  //$NON-NLS-1$
									MessageBundle.getMessage("angal.billbrowser.delete"), //$NON-NLS-1$
									JOptionPane.YES_NO_OPTION);
							if (ok == JOptionPane.YES_OPTION) billManager.deleteBill(deleteBill);
						}
						if (jScrollPanePending.isShowing()) {
							int rowSelected = jTablePending.getSelectedRow();
							Bill deleteBill = (Bill)jTablePending.getValueAt(rowSelected, -1);
							int ok = JOptionPane.showConfirmDialog(null, 
									MessageBundle.getMessage("angal.billbrowser.doyoureallywanttodeletetheselectedbill"),  //$NON-NLS-1$
									MessageBundle.getMessage("angal.billbrowser.delete"), //$NON-NLS-1$
									JOptionPane.YES_NO_OPTION);
							if (ok == JOptionPane.YES_OPTION) billManager.deleteBill(deleteBill);
						}
						if (jScrollPaneClosed.isShowing()) {
							int rowSelected = jTableClosed.getSelectedRow();
							Bill deleteBill = (Bill)jTableClosed.getValueAt(rowSelected, -1);
							int ok = JOptionPane.showConfirmDialog(null, 
									MessageBundle.getMessage("angal.billbrowser.doyoureallywanttodeletetheselectedbill"),  //$NON-NLS-1$
									MessageBundle.getMessage("angal.billbrowser.delete"), //$NON-NLS-1$
									JOptionPane.YES_NO_OPTION);
							if (ok == JOptionPane.YES_OPTION) billManager.deleteBill(deleteBill);
						}
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null,
								MessageBundle.getMessage("angal.billbrowser.pleaseselectabillfirst"), //$NON-NLS-1$
								MessageBundle.getMessage("angal.hospital"), //$NON-NLS-1$
								JOptionPane.PLAIN_MESSAGE);
					}
					billInserted(null);
				}
			});
		}
		return jButtonDelete;
	}

	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			if (MainMenu.checkUserGrants("btnbillnew")) jPanelButtons.add(getJButtonNew());
			if (MainMenu.checkUserGrants("btnbilledit")) jPanelButtons.add(getJButtonEdit());
			if (MainMenu.checkUserGrants("btnbilldelete")) jPanelButtons.add(getJButtonDelete());
			if (MainMenu.checkUserGrants("btnbillreceipt") && GeneralData.RECEIPTPRINTER) jPanelButtons.add(getJButtonPrintReceipt());
			if (MainMenu.checkUserGrants("btnbillreport")) jPanelButtons.add(getJButtonReport());
			jPanelButtons.add(getJButtonClose());
		}
		return jPanelButtons;
	}

	private JPanel getJPanelRange() {
		if (jPanelRange == null) {
			jPanelRange = new JPanel();
			if (!GeneralData.SINGLEUSER && user.equals("admin")) jPanelRange.add(getJComboUsers());
			jPanelRange.add(getJButtonToday());
			jPanelRange.add(getJLabelFrom());
			jPanelRange.add(getJCalendarFrom());
			jPanelRange.add(getJLabelTo());
			jPanelRange.add(getJCalendarTo());
			jPanelRange.add(getJComboMonths());
			jPanelRange.add(getJComboYears());
						
		}
		return jPanelRange;
	}

	private JComboBox getJComboUsers() {
		if (jComboUsers == null) {
			jComboUsers = new JComboBox();
			for (String user : users) 
				jComboUsers.addItem(user);
			
			jComboUsers.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					user = (String) jComboUsers.getSelectedItem();
					jTableUser.setValueAt("<html><b>"+user+"</b></html>", 0, 0);
					updateTotals();
				}
			});
		}
		return jComboUsers;
	}
	
	private JButton getJButtonToday() {
		if (jButtonToday == null) {
			jButtonToday = new JButton();
			jButtonToday.setText(MessageBundle.getMessage("angal.billbrowser.today")); //$NON-NLS-1$
			jButtonToday.setMnemonic(KeyEvent.VK_T);
			jButtonToday.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					dateFrom.setTime(dateToday0.getTime());
					dateTo.setTime(dateToday24.getTime());
					
					jCalendarFrom.setDate(dateFrom.getTime());
					jCalendarTo.setDate(dateTo.getTime());
					
					jButtonToday.setEnabled(false);
				}
			});
			jButtonToday.setEnabled(false);
		}
		return jButtonToday;
	}

	private JMonthChooser getJComboMonths() {
		if (jComboBoxMonths == null) {
			jComboBoxMonths = new JMonthChooser();
			jComboBoxMonths.setLocale(new Locale(GeneralData.LANGUAGE));
			jComboBoxMonths.addPropertyChangeListener("month", new PropertyChangeListener() { //$NON-NLS-1$

				public void propertyChange(PropertyChangeEvent evt) {
					month = jComboBoxMonths.getMonth();
					dateFrom.set(GregorianCalendar.MONTH, month);
					dateFrom.set(GregorianCalendar.DAY_OF_MONTH, 1);
					dateTo.set(GregorianCalendar.MONTH, month);
					dateTo.set(GregorianCalendar.DAY_OF_MONTH, dateFrom.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
					
					jCalendarFrom.setDate(dateFrom.getTime());
					jCalendarTo.setDate(dateTo.getTime());
				}
			});
		}
		return jComboBoxMonths;
	}

	private JYearChooser getJComboYears() {
		if (jComboBoxYears == null) {
			jComboBoxYears = new JYearChooser();
			jComboBoxYears.setLocale(new Locale(GeneralData.LANGUAGE));
			jComboBoxYears.addPropertyChangeListener("year", new PropertyChangeListener() { //$NON-NLS-1$

				public void propertyChange(PropertyChangeEvent evt) {
					year = jComboBoxYears.getYear();
					dateFrom.set(GregorianCalendar.YEAR, year);
					dateFrom.set(GregorianCalendar.MONTH, 1);
					dateFrom.set(GregorianCalendar.DAY_OF_YEAR, 1);
					dateTo.set(GregorianCalendar.YEAR, year);
					dateTo.set(GregorianCalendar.MONTH, 12);
					dateTo.set(GregorianCalendar.DAY_OF_YEAR, dateFrom.getActualMaximum(GregorianCalendar.DAY_OF_YEAR));
					jCalendarFrom.setDate(dateFrom.getTime());
					jCalendarTo.setDate(dateTo.getTime());
				}
			});
		}
		return jComboBoxYears;
	}

	private JScrollPane getJScrollPaneClosed() {
		if (jScrollPaneClosed == null) {
			jScrollPaneClosed = new JScrollPane();
			jScrollPaneClosed.setViewportView(getJTableClosed());
		}
		return jScrollPaneClosed;
	}

	private JTable getJTableClosed() {
		if (jTableClosed == null) {
			jTableClosed = new JTable();
			jTableClosed.setModel(new BillTableModel("C")); //$NON-NLS-1$
			for (int i=0;i<columsWidth.length; i++){
				jTableClosed.getColumnModel().getColumn(i).setMinWidth(columsWidth[i]);
				if (!columsResizable[i]) jTableClosed.getColumnModel().getColumn(i).setMaxWidth(maxWidth[i]);
				if (alignCenter[i]) {
					jTableClosed.getColumnModel().getColumn(i).setCellRenderer(new StringCenterTableCellRenderer());
					if (boldCenter[i]) {
						jTableClosed.getColumnModel().getColumn(i).setCellRenderer(new CenterBoldTableCellRenderer());
					}
				}
			}
			jTableClosed.setAutoCreateColumnsFromModel(false);
			jTableClosed.setDefaultRenderer(String.class, new StringTableCellRenderer());
			jTableClosed.setDefaultRenderer(Integer.class, new IntegerTableCellRenderer());
			jTableClosed.setDefaultRenderer(Double.class, new DoubleTableCellRenderer());
		}
		return jTableClosed;
	}

	private JScrollPane getJScrollPanePending() {
		if (jScrollPanePending == null) {
			jScrollPanePending = new JScrollPane();
			jScrollPanePending.setViewportView(getJTablePending());
		}
		return jScrollPanePending;
	}

	private JTable getJTablePending() {
		if (jTablePending == null) {
			jTablePending = new JTable();
			jTablePending.setModel(new BillTableModel("O")); //$NON-NLS-1$
			for (int i=0;i<columsWidth.length; i++){
				jTablePending.getColumnModel().getColumn(i).setMinWidth(columsWidth[i]);
				if (!columsResizable[i]) jTablePending.getColumnModel().getColumn(i).setMaxWidth(maxWidth[i]);
				if (alignCenter[i]) {
					jTablePending.getColumnModel().getColumn(i).setCellRenderer(new StringCenterTableCellRenderer());
					if (boldCenter[i]) {
						jTablePending.getColumnModel().getColumn(i).setCellRenderer(new CenterBoldTableCellRenderer());
					}
				}
			}
			jTablePending.setAutoCreateColumnsFromModel(false);
			jTablePending.setDefaultRenderer(String.class, new StringTableCellRenderer());
			jTablePending.setDefaultRenderer(Integer.class, new IntegerTableCellRenderer());
			jTablePending.setDefaultRenderer(Double.class, new DoubleTableCellRenderer());
		}
		return jTablePending;
	}

	private JScrollPane getJScrollPaneBills() {
		if (jScrollPaneBills == null) {
			jScrollPaneBills = new JScrollPane();
			jScrollPaneBills.setViewportView(getJTableBills());
		}
		return jScrollPaneBills;
	}

	private JTable getJTableBills() {
		if (jTableBills == null) {
			jTableBills = new JTable();
			jTableBills.setModel(new BillTableModel("ALL")); //$NON-NLS-1$
			for (int i=0;i<columsWidth.length; i++){
				jTableBills.getColumnModel().getColumn(i).setMinWidth(columsWidth[i]);
				if (!columsResizable[i]) jTableBills.getColumnModel().getColumn(i).setMaxWidth(maxWidth[i]);
				if (alignCenter[i]) {
					jTableBills.getColumnModel().getColumn(i).setCellRenderer(new StringCenterTableCellRenderer());
					if (boldCenter[i]) {
						jTableBills.getColumnModel().getColumn(i).setCellRenderer(new CenterBoldTableCellRenderer());
					}
				}
			}
			jTableBills.setAutoCreateColumnsFromModel(false);
			jTableBills.setDefaultRenderer(String.class, new StringTableCellRenderer());
			jTableBills.setDefaultRenderer(Integer.class, new IntegerTableCellRenderer());
			jTableBills.setDefaultRenderer(Double.class, new DoubleTableCellRenderer());
		}
		return jTableBills;
	}

	private JTabbedPane getJTabbedPaneBills() {
		if (jTabbedPaneBills == null) {
			jTabbedPaneBills = new JTabbedPane();
			jTabbedPaneBills.addTab(MessageBundle.getMessage("angal.billbrowser.bills"), getJScrollPaneBills()); //$NON-NLS-1$
			jTabbedPaneBills.addTab(MessageBundle.getMessage("angal.billbrowser.pending"), getJScrollPanePending()); //$NON-NLS-1$
			jTabbedPaneBills.addTab(MessageBundle.getMessage("angal.billbrowser.closed"), getJScrollPaneClosed()); //$NON-NLS-1$
		}
		return jTabbedPaneBills;
	}

	private JTable getJTableToday() {
		if (jTableToday == null) {
			jTableToday = new JTable();
			jTableToday.setModel(new DefaultTableModel(new Object[][] {{"<html><b>"+MessageBundle.getMessage("angal.billbrowser.todaym")+"</b></html>", totalToday, "<html><b>"+MessageBundle.getMessage("angal.billbrowser.notpaid")+"</b></html>", balanceToday}}, new String[] {"","","",""}) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				private static final long serialVersionUID = 1L;
				Class<?>[] types = new Class<?>[] { JLabel.class, Double.class, JLabel.class, Double.class};
	
				public Class<?> getColumnClass(int columnIndex) {
					return types[columnIndex];
				}

				public boolean isCellEditable(int row, int column) {
					return false;
				}
			});
			jTableToday.setRowSelectionAllowed(false);
			jTableToday.setGridColor(Color.WHITE);

		}
		return jTableToday;
	}
	
	private JTable getJTablePeriod() {
		if (jTablePeriod == null) {
			jTablePeriod = new JTable();
			jTablePeriod.setModel(new DefaultTableModel(new Object[][] {{"<html><b>"+MessageBundle.getMessage("angal.billbrowser.periodm")+"</b></html>", totalPeriod, "<html><b>"+MessageBundle.getMessage("angal.billbrowser.notpaid")+"</b></html>", balancePeriod}}, new String[] {"","","",""}) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				private static final long serialVersionUID = 1L;
				Class<?>[] types = new Class<?>[] { JLabel.class, Double.class, JLabel.class, Double.class};
	
				public Class<?> getColumnClass(int columnIndex) {
					return types[columnIndex];
				}

				public boolean isCellEditable(int row, int column) {
					return false;
				}
			});
			jTablePeriod.setRowSelectionAllowed(false);
			jTablePeriod.setGridColor(Color.WHITE);

		}
		return jTablePeriod;
	}
	
	private JTable getJTableUser() {
		if (jTableUser == null) {
			jTableUser = new JTable();
			jTableUser.setModel(new DefaultTableModel(new Object[][] {{"<html><b>"+user+"</b></html>", userToday, "<html><b>"+MessageBundle.getMessage("angal.billbrowser.period")+"</b></html>", userPeriod}}, new String[] {"","","",""}) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				private static final long serialVersionUID = 1L;
				Class<?>[] types = new Class<?>[] { JLabel.class, Double.class, JLabel.class, Double.class};
	
				public Class<?> getColumnClass(int columnIndex) {
					return types[columnIndex];
				}

				public boolean isCellEditable(int row, int column) {
					return false;
				}
			});
			jTableUser.setRowSelectionAllowed(false);
			jTableUser.setGridColor(Color.WHITE);

		}
		return jTableUser;
	}
	
	private void updateTotals() {
		
		ArrayList<Bill> billToday = billManager.getBills(dateToday0, dateToday24);
		ArrayList<BillPayments> paymentsToday = billManager.getPayments(dateToday0, dateToday24);
//		billPeriod = billManager.getBills(dateFrom, dateTo);
//		paymentsPeriod = billManager.getPayments(dateFrom, dateTo);
		
		totalPeriod = 0.;
		balancePeriod = 0.;
		totalToday = 0.;
		balanceToday = 0.;
		userToday = 0.;
		userPeriod = 0.;
				
		//Bills in range contribute for Not Paid (balance)
		for (Bill bill : billPeriod) {
			balancePeriod+=bill.getBalance();
		}
		
		//Payments in range contribute for Paid Period (total)
		for (BillPayments payment : paymentsPeriod) {
			
			double payAmount = payment.getAmount();
			String payUser = payment.getUser();
			
			totalPeriod+=payAmount;
				
			if (!GeneralData.SINGLEUSER && payUser.equals(user))
				userPeriod+=payAmount;
		}
		
		//Bills in today contribute for Not Paid Today (balance)
		for (Bill bill : billToday) {
			balanceToday+=bill.getBalance();
		}
		
		//Payments in today contribute for Paid Today (total)
		for (BillPayments payment : paymentsToday) {
			double payAmount = payment.getAmount();
			String payUser = payment.getUser();
			totalToday+=payAmount;
			if (!GeneralData.SINGLEUSER && payUser.equals(user))
				userToday+=payAmount;
		}
		
		jTableToday.setValueAt(totalToday, 0, 1);
		jTableToday.setValueAt(balanceToday, 0, 3);
		jTablePeriod.setValueAt(totalPeriod, 0, 1);
		jTablePeriod.setValueAt(balancePeriod, 0, 3);
		if (jTableUser != null) {
			jTableUser.setValueAt(userToday, 0, 1);
			jTableUser.setValueAt(userPeriod, 0, 3);
		}
	}
	
	public class BillTableModel extends DefaultTableModel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		ArrayList<Bill> tableArray = new ArrayList<Bill>();

		public BillTableModel(String status) {
			
			tableArray.clear();
			mapBill.clear();
			
			/*
			 * Bills in the period
			 */
			billPeriod = billManager.getBills(dateFrom, dateTo);
			
			/*
			 * Payments in the period
			 */
			paymentsPeriod = billManager.getPayments(dateFrom, dateTo);
			
			/*
			 * Bills not in the period but with payments in the period
			 */
			ArrayList<Bill> billFromPayments = billManager.getBills(paymentsPeriod);
			
			/*
			 * All Bills
			 */
			ArrayList<Bill> billAll = new ArrayList<Bill>();

			/*
			 * Mappings Bills in the period 
			 */
			for (Bill bill : billPeriod) {
				//mapBill.clear();
				mapBill.put(bill.getId(), bill);
			}
			
			/*
			 * Merging the two bills lists
			 */
			billAll.addAll(billPeriod);
			for (Bill bill : billFromPayments) {
				if (mapBill.get(bill.getId()) == null)
					billAll.add(bill);
			}
			
			if (status.equals("O")) {
				tableArray = billManager.getPendingBills(0);
				
			} else if (status.equals("ALL")) {
				
				Collections.sort(billAll);
				tableArray = billAll;

			} else if (status.equals("C")) {
				for (Bill bill : billPeriod) {
					
					if (bill.getStatus().equals(status)) 
						tableArray.add(bill);
				}	
			}
			
			Collections.sort(tableArray, Collections.reverseOrder());
		}
		
		public Class<?> getColumnClass(int columnIndex) {
			return columsClasses[columnIndex];
		}

		public int getColumnCount() {
			return columsNames.length;
		}

		public String getColumnName(int columnIndex) {
			return columsNames[columnIndex];
		}

		public int getRowCount() {
			if (tableArray == null)
				return 0;
			return tableArray.size();
		}
		
		//["Date", "Patient", "Balance", "Update", "Status", "Amount"};

		public Object getValueAt(int r, int c) {
			int index = -1;
			Bill thisBill = tableArray.get(r);
			if (c == index) {
				return thisBill;
			}
			if (c == ++index) {
				return thisBill.getId();
			}
			if (c == ++index) {
				return formatDateTime(thisBill.getDate());
			}
			if (c == ++index) {
				int patID = thisBill.getPatID();
				return patID == 0 ? "" : String.valueOf(patID);
			}
			if (c == ++index) {
				return thisBill.getPatName();
			}
			if (c == ++index) {
				return thisBill.getAmount();
			}
			if (c == ++index) {
				return formatDateTime(thisBill.getUpdate());
			}
			if (c == ++index) {
				return thisBill.getStatus();
			}
			if (c == ++index) {
				return thisBill.getBalance();
			}
			return null;
		}

		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

	}
	
	public String formatDate(GregorianCalendar time) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");  //$NON-NLS-1$
		return format.format(time.getTime());
	}
	
	public String formatDateTime(GregorianCalendar time) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy - HH:mm:ss");  //$NON-NLS-1$
		return format.format(time.getTime());
	}
	
	public String formatDateTimeReport(GregorianCalendar time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //$NON-NLS-1$
		return format.format(time.getTime());
	}
	
	public boolean isSameDay(GregorianCalendar aDate, GregorianCalendar today) {
		return (aDate.get(Calendar.YEAR) == today.get(Calendar.YEAR)) &&
			   (aDate.get(Calendar.MONTH) == today.get(Calendar.MONTH)) &&
			   (aDate.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH));
	}
	
	class StringTableCellRenderer extends DefaultTableCellRenderer {  
	   
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
				boolean hasFocus, int row, int column) {  
		   
			Component cell=super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
			cell.setForeground(Color.BLACK);
			if (((String)table.getValueAt(row, 6)).equals("C")) { //$NON-NLS-1$
				cell.setForeground(Color.GRAY);
			}		   
			return cell;
	   }
	}
	
	class StringCenterTableCellRenderer extends DefaultTableCellRenderer {  
		   
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
				boolean hasFocus, int row, int column) {  
		   
			Component cell=super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
			cell.setForeground(Color.BLACK);
			setHorizontalAlignment(CENTER);
			if (((String)table.getValueAt(row, 6)).equals("C")) { //$NON-NLS-1$
				cell.setForeground(Color.GRAY);
			}
			return cell;
	   }
	}
	
	class IntegerTableCellRenderer extends DefaultTableCellRenderer {  
		   
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
				boolean hasFocus, int row, int column) {  
		   
			Component cell=super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
			cell.setForeground(Color.BLACK);
			cell.setFont(new Font(null, Font.BOLD, 12));
			setHorizontalAlignment(CENTER);
			if (((String)table.getValueAt(row, 6)).equals("C")) { //$NON-NLS-1$
				cell.setForeground(Color.GRAY);
			}
			return cell;
	   }
	}
	
	class DoubleTableCellRenderer extends DefaultTableCellRenderer {  
		   
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
				boolean hasFocus, int row, int column) {  
		   
			Component cell=super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
			cell.setForeground(Color.BLACK);
			setHorizontalAlignment(RIGHT);
			if (((String)table.getValueAt(row, 6)).equals("C")) { //$NON-NLS-1$
				cell.setForeground(Color.GRAY);
			}
			return cell;
	   }
	}
	
	class CenterBoldTableCellRenderer extends DefaultTableCellRenderer {  
		   
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
				boolean hasFocus, int row, int column) {  
		   
			Component cell=super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
			cell.setForeground(Color.BLACK);
			setHorizontalAlignment(CENTER);
			cell.setFont(new Font(null, Font.BOLD, 12));
			return cell;
	   }
	}
}
