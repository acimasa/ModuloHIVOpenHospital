package org.isf.visits.model;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import org.isf.patient.model.*;
import org.isf.visits.manager.*;

public class Visit extends GregorianCalendar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String note;
	private Patient Pat; //Paziente al quale � legata la visita
	private int VisitId; //Id univoco della visita
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
			int minute,Patient pat) {
		super(year, month, dayOfMonth, hourOfDay, minute);
		// TODO Auto-generated constructor stub
		//Prima di creare la visita un certo giorno richiamo la funzione che verifica
		//la possibilit� di inserire una visita quel giorno
		if(verifyVisit(year,month,dayOfMonth,hourOfDay,minute)==true)
		{
			
			this.setPat(pat);
		}
		else //genera visita automaticamente
		{
		}
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
	
	public void setPat(Patient pat)
	{
		this.Pat=pat;
	}
	
	public Patient getPat()
	{
		return this.Pat;
	}
	/*Questo metodo verifica la possiblit� di inserire una visita nel
	 * giorno selezionato dall'operatore
	 */
	
	public boolean verifyVisit (int year,int month,int dayOfMonth,int hourOfDay,int minute)
	{
		/*VisitManager Manager=Manager.getInstance();
		//prelevo i giorni lavorativi
		//prelevo i giorni non lavorativi
		//verifico che dayOfMonth non sia un giorno non lavorativo
		//verifico che dayOfmoth faccia parte dei giorni lavorativi
		//prelevo il numero di appuntamento fissati quel giorno
		//verifico se si e' già raggiunto il numero di appuntamenti max
		//se non si e' raggiunto restituisco true altrimenti false*/
		return true;
	}
}
