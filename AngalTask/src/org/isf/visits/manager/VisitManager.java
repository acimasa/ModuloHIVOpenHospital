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
import org.isf.visits.gui.InsertWorkingDay;

import java.util.Date;
/**
 * @author Nanni
 * @Updated by Alfonso
 *
 */
public class VisitManager {
	
	private IoOperations 			ioOperations;
	private ImpostaScheduler 		Imp;
	private InsertWorkingDay		InsertWD;
	private ArrayList<WorkingDay>	GiorniLavorativi;
	private static VisitManager 	instance;
	
	private VisitManager()
	{
		ioOperations	= 	new IoOperations();
		GiorniLavorativi= 	new ArrayList<WorkingDay>();
		instance		= 	this;
	}
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
		Imp = new ImpostaScheduler();
		Imp.setVisible(true);
	}
	
	public void showInsertWorkingDay()
	{
		InsertWD = new InsertWorkingDay(GiorniLavorativi);
		InsertWD.setVisible(true);
	}
	
	/*public ArrayList<WorkingDay> giveWorkingDaysfromDB()
	{
		try
		{
			return ioOperations.getWorkingDays();
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}*/
	
	/*public Date[] giveNoWorkingDaysfromDB()
	{
		try
		{
			return ioOperations.getNoWorkingDays();
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}*/
	
	public ImpostaScheduler getViewImpostaScheduler()
	{
		return this.Imp;
	}
	
	public static VisitManager getInstance()
	{
		if(instance==null)
			return new VisitManager();
		else
			return instance;
	}
	
	public ArrayList<WorkingDay> getModelWorkingDay()
	{
		return this.GiorniLavorativi;
	}
}
