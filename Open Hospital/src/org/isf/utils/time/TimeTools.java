package org.isf.utils.time;

import java.util.GregorianCalendar;

/**
 * 
 * @author Mwithi
 * 
 * Alcune funzioni utili per effettuare calcoli temporali.
 *
 */
public class TimeTools {

	/**
	 * @author Mwithi
	 * 
	 * restituisce la differenza in giorni tra due date
	 * ma il calcolo è inesatto se c'è di mezzo l'ora legale
	 * @param dates
	 * @return
	 */
	public static int getDifferenzaInGiorni(GregorianCalendar from, GregorianCalendar to) {
		long milliseconds1 = from.getTimeInMillis();
		long milliseconds2 = to.getTimeInMillis();
		long diff = milliseconds2 - milliseconds1;
		long diffDays = diff / (24 * 60 * 60 * 1000);
		int diffDaysIntValue = new Long(diffDays).intValue();
		return diffDaysIntValue;
	}
	
	/**
	 * @author Mwithi
	 * 
	 * restituisce la differenza in millisecondi tra due date
	 * @param dates
	 * @return
	 */
	public static Long getDifferenzaInMillisecondi(GregorianCalendar from, GregorianCalendar to) {
		long milliseconds1 = from.getTimeInMillis();
		long milliseconds2 = to.getTimeInMillis();
		long diff = milliseconds2 - milliseconds1;
		return diff;
	}
}
