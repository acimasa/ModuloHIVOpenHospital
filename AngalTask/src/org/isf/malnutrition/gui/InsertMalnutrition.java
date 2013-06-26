package org.isf.malnutrition.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.isf.generaldata.MessageBundle;
import org.isf.malnutrition.manager.MalnutritionManager;
import org.isf.malnutrition.model.Malnutrition;
import org.isf.utils.time.DateTextField;

public class InsertMalnutrition extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane;

	private JPanel timePanel;

	private JPanel fieldPanel;
	
	private JLabel lastMalnutritionLabel;

	private JPanel buttonPanel;

	private DateTextField confDate;

	private DateTextField suppDate;
	
	private JTextField weightField;

	private JTextField heightField;

	private GregorianCalendar gc;

	private Malnutrition maln;
	
	private Malnutrition lastMalnutrition;

	private JButton okButton;

	private JButton cancelButton;

	private boolean inserting;
	
	private boolean isMalnutrition;

	private JDialog myDialog;

	private MalnutritionManager manager = new MalnutritionManager();

	InsertMalnutrition(JDialog owner, Malnutrition malnutrition, boolean insert) {
		super(owner, true);
		myDialog = this;
		maln = malnutrition;
		inserting = insert;
		isMalnutrition = true;
		setTitle(MessageBundle.getMessage("angal.malnutrition.malnutrition"));
		add(getJContentPane());
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public InsertMalnutrition(JDialog owner, Malnutrition malnutrition, boolean insert, boolean isMaln) {
		super(owner, true);
		myDialog = this;
		maln = malnutrition;
		lastMalnutrition = manager.getLastMalnutrition(malnutrition.getPatId());
		inserting = insert;
		isMalnutrition = isMaln;
		setTitle(MessageBundle.getMessage("angal.malnutrition.heightandweight"));
		add(getJContentPane());
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private JPanel getJContentPane() {
		jContentPane = new JPanel();
		jContentPane.setLayout(new BoxLayout(jContentPane, BoxLayout.Y_AXIS));
		jContentPane.add(getTimePanel());
		jContentPane.add(getFieldPanel());
		if (!isMalnutrition && lastMalnutrition != null) jContentPane.add(getLastMalnutritionPanel());
		jContentPane.add(getButtonPanel());
		validate();
		return jContentPane;
	}

	private JLabel getLastMalnutritionPanel() {
		if (lastMalnutritionLabel == null) {
			
			StringBuilder label = new StringBuilder();
			label.append(MessageBundle.getMessage("angal.malnutrition.lastcontrol") + ": ");
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			label.append(sdf.format(lastMalnutrition.getDateSupp().getTime()));
			
			label.append(" - ");
			
			label.append(MessageBundle.getMessage("angal.malnutrition.weight") + ": ");
			label.append(lastMalnutrition.getWeight());
			
			label.append(", ");
			
			label.append(MessageBundle.getMessage("angal.malnutrition.height") + ": ");
			label.append(lastMalnutrition.getHeight());
			
			lastMalnutritionLabel = new JLabel(label.toString());
		}
		return lastMalnutritionLabel;
	}

	private JPanel getTimePanel() {
		timePanel = new JPanel();
		timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.Y_AXIS));
		
		if (inserting) {
			suppDate = new DateTextField(new GregorianCalendar());
			confDate = new DateTextField();
		}else{
			gc = new GregorianCalendar();
			
			gc.set(GregorianCalendar.DAY_OF_MONTH,maln.getDateSupp().get(GregorianCalendar.DAY_OF_MONTH));
			gc.set(GregorianCalendar.MONTH,maln.getDateSupp().get(GregorianCalendar.MONTH));
			gc.set(GregorianCalendar.YEAR,maln.getDateSupp().get(GregorianCalendar.YEAR));
			suppDate = new DateTextField(gc);

			gc.set(GregorianCalendar.DAY_OF_MONTH,maln.getDateConf().get(GregorianCalendar.DAY_OF_MONTH));
			gc.set(GregorianCalendar.MONTH,maln.getDateConf().get(GregorianCalendar.MONTH));
			gc.set(GregorianCalendar.YEAR,maln.getDateConf().get(GregorianCalendar.YEAR));
			confDate = new DateTextField(gc);
			
		}
				
		JLabel suppDateLabel = new JLabel(MessageBundle.getMessage("angal.malnutrition.dateofthiscontrol"));
		suppDateLabel.setAlignmentX(CENTER_ALIGNMENT);
		timePanel.add(suppDateLabel);
		timePanel.add(suppDate);
		if (isMalnutrition) {
			JLabel confDateLabel = new JLabel(MessageBundle.getMessage("angal.malnutrition.dateofthenextcontrol"));
			confDateLabel.setAlignmentX(CENTER_ALIGNMENT);
			timePanel.add(confDateLabel);
			timePanel.add(confDate);
		}
		return timePanel;
	}

	private JPanel getFieldPanel() {
		fieldPanel = new JPanel();
		// fieldPanel.setLayout(new BoxLayout(fieldPanel,BoxLayout.Y_AXIS));
		weightField = new JTextField();
		weightField.setColumns(6);
		heightField = new JTextField();
		heightField.setColumns(6);
		if (!inserting) {
			weightField.setText(String.valueOf(maln.getWeight()));
			heightField.setText(String.valueOf(maln.getHeight()));
		}
		JLabel weightLabel = new JLabel(MessageBundle.getMessage("angal.malnutrition.weight"));
		JLabel heightLabel = new JLabel(MessageBundle.getMessage("angal.malnutrition.height"));
		fieldPanel.add(weightLabel);
		fieldPanel.add(weightField);
		fieldPanel.add(heightLabel);
		fieldPanel.add(heightField);
		return fieldPanel;
	}

	private JPanel getButtonPanel() {
		buttonPanel = new JPanel();
		buttonPanel.add(getOkButton());
		buttonPanel.add(getCancelButton());
		return buttonPanel;
	}

	private JButton getOkButton() {
		okButton = new JButton(MessageBundle.getMessage("angal.malnutrition.ok"));
		okButton.setMnemonic(KeyEvent.VK_O);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (heightField.getText().equals("")) {
					maln.setHeight(0);
				} else
					try {
						maln.setHeight(Float.valueOf(heightField.getText()));
					} catch (NumberFormatException e) {
						maln.setHeight(0);
					}
				if (weightField.getText().equals("")) {
					maln.setWeight(0);
				} else
					try {
						maln.setWeight(Float.valueOf(weightField.getText()));
					} catch (NumberFormatException e) {
						maln.setWeight(0);
					}
				if(suppDate.isDateValid()){
					maln.setDateSupp(suppDate.getCompleteDate());
				}else
					maln.setDateSupp(null);
				if (isMalnutrition) {
					if(confDate.isDateValid()){
						maln.setDateConf(confDate.getCompleteDate());
					}else
						maln.setDateConf(null);
				} else { //No Malnutrizione, conf = supp.
					maln.setDateConf(suppDate.getCompleteDate());
				}
				if (inserting) {
					if (manager.newMalnutrition(maln)) {
						myDialog.dispose();
					}
				} else {
					if (manager.updateMalnutrition(maln)) {
						myDialog.dispose();
					}
				}
			}
		});
		return okButton;
	}

	private JButton getCancelButton() {
		cancelButton = new JButton(MessageBundle.getMessage("angal.malnutrition.cancel"));
		cancelButton.setMnemonic(KeyEvent.VK_C);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				myDialog.dispose();
			}
		});
		return cancelButton;
	}
}
