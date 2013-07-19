
package org.isf.menu.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.isf.menu.manager.*;
import org.isf.menu.model.*;
import org.isf.utils.jobjects.ModalJFrame;
import org.isf.generaldata.MessageBundle;



public class UserBrowsing extends ModalJFrame implements UserEdit.UserListener {
/*
	// x cattura chiusura finestra e lancio evento quit
	public void	windowActivated(WindowEvent e) {}
	public void	windowClosed(WindowEvent e) {
		fireQuitInserted();
	}
	public void	windowClosing(WindowEvent e) {
		fireQuitInserted();
	}
	public void	windowDeactivated(WindowEvent e) {}
	public void	windowDeiconified(WindowEvent e) {}
	public void	windowIconified(WindowEvent e) {}
	public void	windowOpened(WindowEvent e) {}
	
	
	
	
	// gestione evento quit _____________________________________
	
	private EventListenerList quitListeners = new EventListenerList();

    public void addQuitListener(QuitListener listener) {
    	quitListeners.add(QuitListener.class, listener);
    }

    public void removeQuitListener(QuitListener listener) {
    	quitListeners.remove(QuitListener.class, listener);
    }

    private void fireQuitInserted() {
        AWTEvent event = new AWTEvent(this, AWTEvent.RESERVED_ID_MAX + 1) {};

        EventListener[] listeners = quitListeners.getListeners(QuitListener.class);
        for (int i = 0; i < listeners.length; i++)
            ((QuitListener)listeners[i]).quitInserted(event);
    }
	*/
	
	// messaggi raccolti da UserEdit_________________________________________ 
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;




	public void userInserted(AWTEvent e) {				
		User u = (User)e.getSource();		
		pUser.add(0,u);	
		((UserBrowserModel)table.getModel()).fireTableDataChanged();
		table.updateUI();
		if (table.getRowCount() > 0)
			table.setRowSelectionInterval(0, 0);
	}
	public void userUpdated(AWTEvent e) {		
		pUser.set(selectedrow,user);
		((UserBrowserModel)table.getModel()).fireTableDataChanged();
		table.updateUI();
		if ((table.getRowCount() > 0) && selectedrow >-1)
			table.setRowSelectionInterval(selectedrow,selectedrow);	
	}
	
	
	private static final int DEFAULT_WIDTH = 400;
	private static final int DEFAULT_HEIGHT = 200;
	private int pfrmWidth;
	private int pfrmHeight;
	private int selectedrow;
	private JLabel selectlabel;
	private JComboBox pbox;
	private ArrayList<User> pUser;
	private String[] pColums = { MessageBundle.getMessage("angal.menu.userm"), MessageBundle.getMessage("angal.menu.groupm"), MessageBundle.getMessage("angal.menu.passwdm"), MessageBundle.getMessage("angal.menu.descm") };
	private int[] pColumwidth = {70, 70, 70, 150 };
	private User user;
	private DefaultTableModel model ;
	private JTable table;
	private JScrollPane scrollPane;
	private final String MASQ = "xxxxxx";
	
	private String pSelection;
	
	private UserBrowsing myFrame;
	
	
	
	
	public UserBrowsing() {
		
		setTitle(MessageBundle.getMessage("angal.menu.usersbrowser"));
		myFrame = this;
		
		
		//addWindowListener(this);
		
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screensize = kit.getScreenSize();
		pfrmWidth = screensize.width / 2 + 100;
		pfrmHeight = screensize.height / 2;
		setBounds(screensize.width / 4, screensize.height / 4, pfrmWidth,
				pfrmHeight);
		
		model = new UserBrowserModel();
		table = new JTable(model);
		table.getColumnModel().getColumn(0).setPreferredWidth(pColumwidth[0]);
		table.getColumnModel().getColumn(1).setPreferredWidth(pColumwidth[1]);
		table.getColumnModel().getColumn(2).setPreferredWidth(pColumwidth[2]);
		table.getColumnModel().getColumn(3).setPreferredWidth(pColumwidth[3]);
				
		scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		
		selectlabel = new JLabel(MessageBundle.getMessage("angal.menu.selectgroup"));
		buttonPanel.add(selectlabel);
		
		UserBrowsingManager manager = new UserBrowsingManager();
		pbox = new JComboBox();
		pbox.addItem(MessageBundle.getMessage("angal.menu.all"));
		ArrayList<UserGroup> group = manager.getUserGroup();
		for (UserGroup elem : group) {
			pbox.addItem(elem);
		}
		pbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				pSelection=pbox.getSelectedItem().toString();
				if (pSelection.compareTo("ALL") == 0)
					model = new UserBrowserModel();
				else
					model = new UserBrowserModel(pSelection);
				model.fireTableDataChanged();
				table.updateUI();
			}
		});
		buttonPanel.add(pbox);

		JButton buttonEdit = new JButton(MessageBundle.getMessage("angal.menu.edit"));
		buttonEdit.setMnemonic(KeyEvent.VK_E);
		buttonEdit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				if (table.getSelectedRow() < 0) {
					JOptionPane.showMessageDialog(				
	                        null,
	                        MessageBundle.getMessage("angal.menu.pleaseselectarow"),
	                        MessageBundle.getMessage("angal.hospital"),
	                        JOptionPane.PLAIN_MESSAGE);				
					return;									
				}else {		
					selectedrow = table.getSelectedRow();
					user = (User)(((UserBrowserModel) model).getValueAt(table.getSelectedRow(), -1));	
					new	UserEdit(myFrame, user,false);
				}	 				
			}
		});
		buttonPanel.add(buttonEdit);
		
		JButton buttonNew = new JButton(MessageBundle.getMessage("angal.menu.new"));
		buttonNew.setMnemonic(KeyEvent.VK_N);
		buttonNew.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				user=new User();
				new UserEdit(myFrame, user,true);
			}
		});
		buttonPanel.add(buttonNew);
		
		JButton buttonDelete = new JButton(MessageBundle.getMessage("angal.menu.delete"));
		buttonDelete.setMnemonic(KeyEvent.VK_D);
		buttonDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (table.getSelectedRow() < 0) {
					JOptionPane.showMessageDialog(				
	                        null,
	                        MessageBundle.getMessage("angal.menu.pleaseselectarow"),
	                        MessageBundle.getMessage("angal.hospital"),
	                        JOptionPane.PLAIN_MESSAGE);				
					return;									
				}else {
				UserBrowsingManager manager = new UserBrowsingManager();
				User m = (User)(((UserBrowserModel) model).getValueAt(table.getSelectedRow(), -1));
				int n = JOptionPane.showConfirmDialog(
                        null,
                        MessageBundle.getMessage("angal.menu.deleteuser")+" \""+m.getUserName()+"\" ?",
                        MessageBundle.getMessage("angal.hospital"),
                        JOptionPane.YES_NO_OPTION);

				if ((n == JOptionPane.YES_OPTION) && (manager.deleteUser(m))){
					pUser.remove(table.getSelectedRow());
					model.fireTableDataChanged();
					table.updateUI();
					}
				}
			}
		});
		buttonPanel.add(buttonDelete);
		
		JButton buttonClose = new JButton(MessageBundle.getMessage("angal.menu.close"));
		buttonClose.setMnemonic(KeyEvent.VK_C);
		buttonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				dispose();
			}
		});
		buttonPanel.add(buttonClose);

		add(buttonPanel, BorderLayout.SOUTH);
		setVisible(true);
	}

		
	class UserBrowserModel extends DefaultTableModel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public UserBrowserModel(String s) {
			UserBrowsingManager manager = new UserBrowsingManager();
			pUser = manager.getUser(s);
		}
		public UserBrowserModel() {
			UserBrowsingManager manager = new UserBrowsingManager();
			pUser = manager.getUser();
		}
		public int getRowCount() {
			if (pUser == null)
				return 0;
			return pUser.size();
		}
		
		public String getColumnName(int c) {
			return pColums[c];
		}

		public int getColumnCount() {
			return pColums.length;
		}

		public Object getValueAt(int r, int c) {
			if (c == 0) {
				return pUser.get(r).getUserName();
			} else if (c == -1) {
				return pUser.get(r);
			} else if (c == 1) {
				return pUser.get(r).getUserGroupName();
			} else if (c == 2) {
				//return pUser.get(r).getPasswd();
				return MASQ;
			} else if (c == 3) {
				return pUser.get(r).getDesc();
			}
			return null;
		}
		
		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			//return super.isCellEditable(arg0, arg1);
			return false;
		}
	}

}
