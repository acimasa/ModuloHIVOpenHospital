package org.isf.therapy.manager;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.JOptionPane;

import org.isf.medicals.model.Medical;
import org.isf.therapy.model.Therapy;
import org.isf.therapy.model.TherapyRow;
import org.isf.therapy.service.IoOperations;
import org.isf.utils.exception.OHException;

public class TherapyManager {
	
	private IoOperations ioOperations = new IoOperations();
	
	public Therapy createTherapy(TherapyRow th) {
		return createTherapy(th.getPatID(), th.getNumTherapy(), th.getMedical(), th.getQty(),
				th.getStartDate(), th.getEndDate(), th.getFreqInPeriod(), th.getFreqInDay(), 
				th.getNote(), th.isNotify(), th.isSms());
	}
	
	public Therapy createTherapy(int patID, int numTherapy, Medical med, Double qty,
			GregorianCalendar startDate, GregorianCalendar endDate, int freqInPeriod,
			int freqInDay, String note, boolean notify, boolean sms) {
		
		ArrayList<GregorianCalendar> datesArray = new ArrayList<GregorianCalendar>();
		
		GregorianCalendar stepDate = new GregorianCalendar();
		stepDate.setTime(startDate.getTime());
		datesArray.add(new GregorianCalendar(
				startDate.get(GregorianCalendar.YEAR),
				startDate.get(GregorianCalendar.MONTH),
				startDate.get(GregorianCalendar.DAY_OF_MONTH)));
		
		while (stepDate.before(endDate)) {
			
			stepDate.add(GregorianCalendar.DAY_OF_MONTH, freqInPeriod);
			datesArray.add(new GregorianCalendar(
					stepDate.get(GregorianCalendar.YEAR),
					stepDate.get(GregorianCalendar.MONTH),
					stepDate.get(GregorianCalendar.DAY_OF_MONTH))
			);
		}
		
		GregorianCalendar[] dates = new GregorianCalendar[datesArray.size()];
		
		for (int i = 0; i < datesArray.size(); i++) {
			//dates[i] = new GregorianCalendar();
			dates[i] = datesArray.get(i);
			//System.out.println(formatDate(dates[i]));
		}
		
		Therapy th = new Therapy(numTherapy,
				patID,
				dates,
				med,
				qty,
				"",
				freqInDay,
				note,
				notify,
				sms);
		
		datesArray.clear();
		dates = null;
		
		return th;
	}

	public ArrayList<Therapy> getTherapies(ArrayList<TherapyRow> thRows) {
		
		if (thRows != null) {
			ArrayList<Therapy> therapies = new ArrayList<Therapy>();
			
			for (TherapyRow thRow : thRows) {
				
				therapies.add(createTherapy(thRow));
			}
			return therapies;
		} else {
			return null;
		}
	}

	/**
	 * return the list of {@link TherapyRow}s (therapies) for specified Patient ID
	 * or
	 * return all {@link TherapyRow}s (therapies) if <code>0</code> is passed
	 * 
	 * @param patID - the Patient ID
	 * @return the list of {@link TherapyRow}s (therapies)
	 */
	public ArrayList<TherapyRow> getTherapyRows(int code) {
		try {
			return ioOperations.getTherapyRows(code);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}
	
	/**
	 * replace all {@link TherapyRow}s (therapies) for related Patient
	 * 
	 * @param thRows - the list of {@link TherapyRow}s (therapies)
	 * @param notify - mark as important
	 * @param sms - mark for sms reminder
	 * @return <code>true</code> if the row has been inserted, <code>false</code> otherwise
	 */
	public boolean newTherapies(ArrayList<TherapyRow> thRows, boolean notify, boolean sms) {
		if (!thRows.isEmpty()) {
			try {
				ioOperations.deleteAllTherapies(thRows.get(0).getPatID());

				int numTherapy = 1;
				for (TherapyRow thRow : thRows) {

					thRow.setNotify(notify);
					thRow.setSms(sms);
					ioOperations.newTherapy(thRow, numTherapy);
					numTherapy++;
				}
			} catch (OHException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
				return false;
			}
		}
		return true;
	}

	/**
	 * delete all {@link TherapyRow}s (therapies) for specified Patient ID
	 * 
	 * @param patID - the Patient ID
	 * @return <code>true</code> if the therapies have been deleted, <code>false</code> otherwise
	 */
	public boolean deleteAllTherapies(Integer code) {
		try {
			return ioOperations.deleteAllTherapies(code);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

}
