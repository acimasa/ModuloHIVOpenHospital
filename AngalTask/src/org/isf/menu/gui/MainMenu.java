package org.isf.menu.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.MDC;
import org.isf.generaldata.GeneralData;
import org.isf.menu.manager.UserBrowsingManager;
import org.isf.menu.model.User;
import org.isf.menu.model.UserMenuItem;
import org.isf.utils.jobjects.ModalJFrame;
import org.isf.xmpp.gui.CommunicationFrame;
import org.isf.xmpp.service.Server;
import org.jivesoftware.smack.XMPPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MainMenu extends JFrame implements ActionListener,
		Login.LoginListener, SubMenu.CommandListener {
	private static final long serialVersionUID = 7620582079916035164L;
	private boolean flag_Xmpp = false;
	
	private final Logger logger = LoggerFactory.getLogger(MainMenu.class);
	
	public void loginInserted(AWTEvent e) {
		if (e.getSource() instanceof User) {
			myUser = (User) e.getSource();
			MDC.put("OHUser", myUser.getUserName());
			MDC.put("OHUserGroup", myUser.getUserGroupName());
			logger.info("Logging: \"" + myUser.getUserName() + "\" user has logged the system.");
		}
	}

	public void commandInserted(AWTEvent e) {
		if (e.getSource() instanceof String) {
			launchApp((String) e.getSource());
		}
	}
	
	public static boolean checkUserGrants(String code) {
		
		Iterator<UserMenuItem> it = myMenu.iterator();
		while (it.hasNext()) {
			UserMenuItem umi = it.next();
			if (umi.getCode().equalsIgnoreCase(code)) {
					return true;
				}
		}
		return false;
	}
	
	public static String getUser() {
		return myUser.getUserName();
	}

	private int minButtonSize=0;
	
	public void setMinButtonSize(int value){
		minButtonSize=value;
	}
	public int getMinButtonSize(){
		return minButtonSize;
	}
	

	private static User myUser=null;
	private static ArrayList<UserMenuItem> myMenu=null;

	
	final int menuXPosition = 10;
	final int menuYDisplacement = 75;

	// singleUser=true : one user
	private boolean singleUser= false;
	// internalPharmacies=false : no internalPharmacies
	private boolean internalPharmacies= false;
	private MainMenu myFrame;
	
	public MainMenu() {	
		myFrame = this;
		
		GeneralData.getGeneralData();
		try{
			singleUser = GeneralData.SINGLEUSER;
			internalPharmacies = GeneralData.INTERNALPHARMACIES;
			flag_Xmpp = GeneralData.XMPPMODULEENABLED;
		}
		catch (Exception e){
			singleUser = true; // default for property not found
			internalPharmacies = false; // default for property not found
		}
		
		
		if (singleUser) {
			logger.info("Logging: Single User mode.");
			myUser = new User("admin", "admin", "admin", "");
			MDC.put("OHUser", myUser.getUserName());
			MDC.put("OHUserGroup", myUser.getUserGroupName());
		} else {
			// get an user
			logger.info("Logging: Multi User mode.");
			new Login(this);
			
			if (myUser == null){
				//Login failed
				actionExit(2);
			}	
		}
		
		// get menu items
		UserBrowsingManager manager = new UserBrowsingManager();
		myMenu = manager.getMenu(myUser);
		
		//start connection with xmpp server if is enabled
		if(flag_Xmpp){
			try {
				Server.getInstance().login(myUser.getUserName(), myUser.getPasswd());
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				new CommunicationFrame();
				/*Interaction communication= new Interaction();
				communication.incomingChat();
				communication.receiveFile();*/
			} catch (XMPPException e) {
				logger.info("No XMPP Server seems to be running: set XMPPMODULEENABLED = false");
				flag_Xmpp = GeneralData.XMPPMODULEENABLED = false;
			}
			
		}
		
		// if in singleUser mode remove "users" and "communication" menu
		if (singleUser) {
			ArrayList<UserMenuItem> junkMenu = new ArrayList<UserMenuItem>();
			Iterator<UserMenuItem> it = myMenu.iterator();
			while (it.hasNext()) {
				UserMenuItem umi = it.next();
				if (umi.getCode().equalsIgnoreCase("USERS")
						|| umi.getMySubmenu().equalsIgnoreCase("USERS"))
					junkMenu.add(umi);
				if (umi.getCode().equalsIgnoreCase("communication")) {
					if (flag_Xmpp) {
						logger.info("Single user mode: set XMPPMODULEENABLED = false");
						flag_Xmpp = GeneralData.XMPPMODULEENABLED = false;
					}
					junkMenu.add(umi);
				}
			}
			Iterator<UserMenuItem> altIt = junkMenu.iterator();
			while (altIt.hasNext()) {
				UserMenuItem umi = altIt.next();
				if (myMenu.contains(umi))
					myMenu.remove(umi);
			}
		} else { //remove only "communication" if flag_Xmpp = false
			if (!flag_Xmpp) {
				ArrayList<UserMenuItem> junkMenu = new ArrayList<UserMenuItem>();
				Iterator<UserMenuItem> it = myMenu.iterator();
				while (it.hasNext()) {
					UserMenuItem umi = it.next();
					if (umi.getCode().equalsIgnoreCase("communication"))
						junkMenu.add(umi);
				}
				Iterator<UserMenuItem> altIt = junkMenu.iterator();
				while (altIt.hasNext()) {
					UserMenuItem umi = altIt.next();
					if (myMenu.contains(umi))
						myMenu.remove(umi);
				}
			}
		}
		
		// if not internalPharmacies mode remove "medicalsward" menu
		if (!internalPharmacies) {
			ArrayList<UserMenuItem> junkMenu = new ArrayList<UserMenuItem>();
			Iterator<UserMenuItem> it = myMenu.iterator();
			while (it.hasNext()) {
				UserMenuItem umi = it.next();
				if (umi.getCode().equalsIgnoreCase("MEDICALSWARD")
						|| umi.getMySubmenu().equalsIgnoreCase("MEDICALSWARD"))
					junkMenu.add(umi);
			}
			Iterator<UserMenuItem> altIt = junkMenu.iterator();
			while (altIt.hasNext()) {
				UserMenuItem umi = altIt.next();
				if (myMenu.contains(umi))
					myMenu.remove(umi);
			}
		}
		
		// remove disabled buttons
		ArrayList<UserMenuItem> junkMenu = new ArrayList<UserMenuItem>();
		Iterator<UserMenuItem> it = myMenu.iterator();
		while (it.hasNext()) {
			UserMenuItem umi = it.next();
			if (!umi.isActive())
				junkMenu.add(umi);
		}
		Iterator<UserMenuItem> altIt = junkMenu.iterator();
		while (altIt.hasNext()) {
			UserMenuItem umi = altIt.next();
			if (myMenu.contains(umi))
				myMenu.remove(umi);
		}

		setTitle("menu");
		// add panel with buttons to frame
		MainPanel panel = new MainPanel(this);
		add(panel);
		pack();
		
		// compute menu position
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenHeight = screenSize.height;

		int frameHeight = getSize().height;

		setLocation(menuXPosition, screenHeight - frameHeight - menuYDisplacement);

		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.setAlwaysOnTop(GeneralData.MAINMENUALWAYSONTOP); 
		myFrame.addWindowListener(new WindowAdapter(){
			
			public void windowClosing(WindowEvent e) {
				actionExit(0);
			}
		});
		
		setResizable(false);
		setVisible(true);
	}
	
	private void actionExit(int status) {
		if (status == 2) logger.info("Login failed.");
		logger.info("\n\n=====================\n OpenHospital closed \n=====================\n");
		System.exit(status);
	}	
	
	/*
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		launchApp(command);
	}

	/**
	 * 
	 * @param itemMenuCode
	 */
	private void launchApp(String itemMenuCode) {

		for (UserMenuItem u : myMenu) {
			if (u.getCode().equals(itemMenuCode)) {
				if (u.getCode().equalsIgnoreCase("EXIT")) {
					actionExit(0);
				} else if (u.isASubMenu()) {
					new SubMenu(this, u.getCode(), myMenu);
					break;
				}
				else {
					String app = u.getMyClass();
					// an empty menu item
					if (app.equalsIgnoreCase("none"))
						return;
					try {												
						Object target =  Class.forName(app).newInstance();
						try {
							((ModalJFrame)target).showAsModal(this);
						} catch (ClassCastException noModalJFrame) {
							try {
								((JFrame)target).setEnabled(true);
							} catch (ClassCastException noJFrame)  {
								((JDialog)target).setEnabled(true);
							}
						}
					} catch (InstantiationException ie) {
						ie.printStackTrace();
					} catch (IllegalAccessException iae) {
						iae.printStackTrace();
					} catch (ClassNotFoundException cnfe) {
						cnfe.printStackTrace();
					}
					break;
				}
			}
		}
	}

	private class MainPanel extends JPanel {
		private static final long serialVersionUID = 4338749100837551874L;

		private JButton button[];
		private MainMenu parentFrame=null;

		public MainPanel(MainMenu parentFrame) {
			this.parentFrame = parentFrame;
			int numItems = 0;

			for (UserMenuItem u : myMenu)
				if (u.getMySubmenu().equals("main"))
					numItems++;

			// System.out.println(numItems);

			button = new JButton[numItems];

			int k = 1;

			for (UserMenuItem u : myMenu)
				if (u.getMySubmenu().equals("main")) {
					button[k - 1] = new JButton(u.getButtonLabel());

					button[k - 1].setMnemonic(KeyEvent.VK_A
							+ (int) (u.getShortcut() - 'A'));

					button[k - 1].addActionListener(parentFrame);
					button[k - 1].setActionCommand(u.getCode());
					k++;
				}

			setButtonsSize(button);
			
			//setBackground(java.awt.Color.WHITE);
			JLabel fig = new JLabel(new ImageIcon("rsc"+File.separator+"images"+File.separator+"LogoMenu.jpg"));
			add(fig,BorderLayout.WEST);
			
			
			JPanel buttons = new JPanel();
			GridBagLayout layout = new GridBagLayout();
			buttons.setLayout(layout);
			
			
			final int insetsValue = 6;			

			for (int i = 0; i < button.length; i++) {
				buttons.add(button[i], new GBC(0, i).setInsets(insetsValue));
			}
			
			add(buttons, BorderLayout.CENTER);
		}

		private void setButtonsSize(JButton button[]) {
			int maxH = 0;
			int maxMax = 0;
			int maxMin = 0;
			int maxPrf = 0;

			for (int i = 0; i < button.length; i++) {
				maxH = Math.max(maxH, button[i].getMaximumSize().height);
				maxMax = Math.max(maxMax, button[i].getMaximumSize().width);
				maxMin = Math.max(maxMin, button[i].getMinimumSize().width);
				maxPrf = Math.max(maxPrf, button[i].getPreferredSize().width);
			}
			for (int i = 0; i < button.length; i++) {
				button[i].setMaximumSize(new Dimension(maxMax, maxH));
				button[i].setMinimumSize(new Dimension(maxMin, maxH));
				button[i].setPreferredSize(new Dimension(maxPrf, maxH));
			}
			parentFrame.setMinButtonSize(maxPrf);
		}
	}// :~MainPanel
}// :~MainMenu
