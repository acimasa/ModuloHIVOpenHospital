package org.isf.therapy.model;

import java.util.GregorianCalendar;

import org.isf.medicals.model.Medical;

/**
 * 
 * @author Mwithi
 * 
 * Bean to collect data from DB table THERAPIES
 *
 */
public class TherapyRow {
	
	private int patID;
	private int numTherapy;
	private GregorianCalendar startDate;
	private GregorianCalendar endDate;
	private Medical medical;
	private Double qty;
	private int unitID;
	private int freqInDay;
	private int freqInPeriod;
	private String note;
	private boolean notify;
	private boolean sms;
	
	/**
	 * 
	 * @param patID
	 * @param numTherapy
	 * @param startDate
	 * @param endDate
	 * @param medical
	 * @param qty
	 * @param unitID
	 * @param freqInDay
	 * @param freqInPeriod
	 * @param note
	 * @param notify
	 * @param sms
	 */
	public TherapyRow(int patID, int numTherapy,
			GregorianCalendar startDate, GregorianCalendar endDate,
			Medical medical, Double qty, int unitID, int freqInDay,
			int freqInPeriod, String note, boolean notify, boolean sms) {
		super();
		this.patID = patID;
		this.numTherapy = numTherapy;
		this.startDate = startDate;
		this.endDate = endDate;
		this.medical = medical;
		this.qty = qty;
		this.unitID = unitID;
		this.freqInDay = freqInDay;
		this.freqInPeriod = freqInPeriod;
		this.note = note;
		this.notify = notify;
		this.sms = sms;
	}

	public int getPatID() {
		return patID;
	}

	public void setPatID(int patID) {
		this.patID = patID;
	}

	public int getNumTherapy() {
		return numTherapy;
	}

	public void setNumTherapy(int numTherapy) {
		this.numTherapy = numTherapy;
	}

	public GregorianCalendar getStartDate() {
		return startDate;
	}

	public void setStartDate(GregorianCalendar startDate) {
		this.startDate = startDate;
	}

	public GregorianCalendar getEndDate() {
		return endDate;
	}

	public void setEndDate(GregorianCalendar endDate) {
		this.endDate = endDate;
	}

	public Medical getMedical() {
		return medical;
	}

	public void setMedical(Medical medical) {
		this.medical = medical;
	}

	public Double getQty() {
		return qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}

	public int getUnitID() {
		return unitID;
	}

	public void setUnitID(int unitID) {
		this.unitID = unitID;
	}

	public int getFreqInDay() {
		return freqInDay;
	}

	public void setFreqInDay(int freqInDay) {
		this.freqInDay = freqInDay;
	}

	public int getFreqInPeriod() {
		return freqInPeriod;
	}

	public void setFreqInPeriod(int freqInPeriod) {
		this.freqInPeriod = freqInPeriod;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public boolean isNotify() {
		return notify;
	}

	public void setNotify(boolean notify) {
		this.notify = notify;
	}

	public boolean isSms() {
		return sms;
	}

	public void setSms(boolean sms) {
		this.sms = sms;
	}
}
