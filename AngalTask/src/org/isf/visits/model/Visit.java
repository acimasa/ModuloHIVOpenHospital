package org.isf.visits.model;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class Visit extends GregorianCalendar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String note;
	
	public Visit() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Visit(int year, int month, int dayOfMonth, int hourOfDay,
			int minute, int second) {
		super(year, month, dayOfMonth, hourOfDay, minute, second);
		// TODO Auto-generated constructor stub
	}

	public Visit(int year, int month, int dayOfMonth, int hourOfDay,
			int minute) {
		super(year, month, dayOfMonth, hourOfDay, minute);
		// TODO Auto-generated constructor stub
	}

	public Visit(int year, int month, int dayOfMonth) {
		super(year, month, dayOfMonth);
		// TODO Auto-generated constructor stub
	}

	public Visit(Locale locale) {
		super(locale);
		// TODO Auto-generated constructor stub
	}

	public Visit(TimeZone zone, Locale locale) {
		super(zone, locale);
		// TODO Auto-generated constructor stub
	}

	public Visit(TimeZone zone) {
		super(zone);
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		
		String visitString = "Visit: " + formatDateTime(this);
		return visitString;
	}
	
	public String formatDateTime(GregorianCalendar time) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy - HH:mm:ss");  //$NON-NLS-1$
		return format.format(time.getTime());
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
