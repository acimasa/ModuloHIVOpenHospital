package org.isf.utils.time;
/*-------------------------------------------------------------------
 * classe "statica" RememberDates: gestisce un default di data appropriato quando si inseriscono
 *                                 consecutivamente piu' record relativi allo stesso giorno,
 *                                 e quando l'inserimento non avviene lo stesso giorno dell'evento reale
 *                                 la data odierna come default di inserimento viene proposta solo per
 *                                 il primo inserimento, poi si propone l'ultima data inserita
 *                                 viene usato in laboratorio, admission e opd
 * -----------------------------------------
 * modification history
 * =====================
 * 08/11/06 - ross - creazione
 * 09/11/06 - ross - modificata per fornire, la prima volta, la data di sistema (metodi get geregorian)
 * 11/08/10 - claudia - inserita la voce per PATIENTVACCINE
 * 
 *-------------------------------------------------------------------*/



import java.util.Date;
import java.util.GregorianCalendar;

public class RememberDates {

	private static Date lastOpdVisitDate=null;
	private static Date lastAdmInDate=null;
	private static Date lastLabExamDate=null;
	private static Date lastBillDate=null;
	private static Date lastPatientVaccineDate=null;

	//passare da Date a gregorian
	//visitDate.setTime(resultSet.getDate("OPD_DATE_VIS"));
	
	//passare da gregorian a date
	//java.sql.Date visitDate 	= (opd.getVisitDate()==null?null:new java.sql.Date(opd.getVisitDate().getTimeInMillis()));
	//GregorianCalendar time=new GregorianCalendar();//gets the current time

	
	//------------  opd attendance date -----------------------
	public static Date getLastOpdVisitDate() 	{
		return lastOpdVisitDate;
	}
	public static GregorianCalendar getLastOpdVisitDateGregorian() 	{
		GregorianCalendar gc=new GregorianCalendar();
		if (lastOpdVisitDate!=null) {
			gc.setTime(lastOpdVisitDate);
		}
		return gc;
	}
	public static void setLastOpdVisitDate(Date visitDate) 	{
		lastOpdVisitDate=visitDate;
	}
	public static void setLastOpdVisitDate(GregorianCalendar visitDate) 	{
		lastOpdVisitDate=new java.sql.Date(visitDate.getTimeInMillis());
	}

	//------------  laboratory exam -----------------------
	public static Date getLastLabExamDate() 	{
		return lastLabExamDate == null ? new Date() : lastLabExamDate;
	}
	public static GregorianCalendar getLastLabExamDateGregorian() 	{
		GregorianCalendar gc=new GregorianCalendar();
		if (lastLabExamDate!=null) {
			gc.setTime(lastLabExamDate);
		}
		return gc;
	}
	public static void setLastLabExamDate(Date labDate) 	{
		lastLabExamDate=labDate;
	}
	public static void setLastLabExamDate(GregorianCalendar labDate) 	{
		lastLabExamDate=new java.sql.Date(labDate.getTimeInMillis());
	}

	//------------  admission date -----------------------
	public static Date getLastAdmInDate() 	{
		return lastAdmInDate;
	}
	public static GregorianCalendar getLastAdmInDateGregorian() 	{
		GregorianCalendar gc=new GregorianCalendar();
		if (lastAdmInDate!=null) {
			gc.setTime(lastAdmInDate);
		}
		return gc;
	}
	public static void setLastAdmInDate(Date inDate) 	{
		lastAdmInDate=inDate;
	}
	public static void setLastAdmInDate(GregorianCalendar inDate) 	{
		lastAdmInDate=new java.sql.Date(inDate.getTimeInMillis());
	}
	
	//------------ bill date -----------------------
	public static Date getLastBillDate() 	{
		return lastBillDate == null ? new Date() : lastBillDate;
	}
	public static GregorianCalendar getLastBillDateGregorian() 	{
		GregorianCalendar gc=new GregorianCalendar();
		if (lastBillDate!=null) {
			gc.setTime(lastBillDate);
		}
		return gc;
	}
	public static void setLastBillDate(Date inDate) 	{
		lastBillDate=inDate;
	}
	public static void setLastBillDate(GregorianCalendar inDate) 	{
		lastBillDate=new java.sql.Date(inDate.getTimeInMillis());
	}
	
	
	//------------  PAtient vaccine-----------------------
	public static Date getLastPatientVaccineDate() 	{
		return lastPatientVaccineDate == null ? new Date() : lastPatientVaccineDate;
	}
	public static GregorianCalendar getLastPatientVaccineDateGregorian() 	{
		GregorianCalendar gc=new GregorianCalendar();
		if (lastPatientVaccineDate!=null) {
			gc.setTime(lastPatientVaccineDate);
		}
		return gc;
	}
	public static void setLastPatientVaccineDate(Date patVacDate) 	{
		lastPatientVaccineDate=patVacDate;
	}
	public static void setLastPatineVaccineDate(GregorianCalendar labDate) 	{
		lastPatientVaccineDate=new java.sql.Date(labDate.getTimeInMillis());
	}
}
