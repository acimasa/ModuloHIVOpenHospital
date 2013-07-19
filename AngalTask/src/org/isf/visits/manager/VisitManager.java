/**
 * 
 */
package org.isf.visits.manager;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.isf.patient.model.Patient;
import org.isf.utils.exception.OHException;
import org.isf.visits.model.Visit;
import org.isf.visits.model.WorkingDay;
import org.isf.visits.service.IoOperations;
import org.isf.visits.gui.ImpostaScheduler;
import java.util.Date;
/**
 * @author Nanni
 *
 */
public class VisitManager {
	
	private IoOperations ioOperations = new IoOperations();
	
	/**
	 * returns the list of all {@link Visit}s related to a patID
	 * 
	 * @param patID - the {@link Patient} ID. If <code>0</code> return the list of all {@link Visit}s
	 * @return the list of {@link Visit}s
	 */
	public ArrayList<Visit> getVisits(int patID) {
		try {
			return ioOperations.getVisits(patID);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	/**
	 * replace all {@link Visit}s related to a patID
	 * 
	 * @param patID - the {@link Patient} ID
	 * @param visits - the list of {@link Visit}s related to patID. 
	 * @return <code>true</code> if the list has been replaced, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean newVisits(int patID, ArrayList<Visit> visits) {
		try {
			return ioOperations.updateVisits(patID, visits);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
	
	/**
	 * deletes all {@link Visit}s related to a patID
	 * 
	 * @param patID - the {@link Patient} ID
	 * @return <code>true</code> if the list has been deleted, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean deleteAllVisits(int patID) {
		try {
			return ioOperations.updateVisits(patID, null);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
	/* Mostra a schermo la finestra per la configurazione dello
	 * scheduler
	 */
	public void showSchedulerConfig()
	{
		ImpostaScheduler Imp = new ImpostaScheduler();
		Imp.setVisible(true);
	}
	
	public WorkingDay[] giveWorkingDaysfromDB()
	{
		try
		{
			return ioOperations.getWorkingDays();
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}
	
	public Date[] giveNoWorkingDaysfromDB()
	{
		try
		{
			return ioOperations.getNoWorkingDays();
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}
}
