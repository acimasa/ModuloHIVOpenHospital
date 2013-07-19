package org.isf.medicalstock.model;

import java.util.GregorianCalendar;

import org.isf.generaldata.MessageBundle;

public class Lot implements Comparable<Lot> {
	private String code;
	private GregorianCalendar preparationDate;
	private GregorianCalendar dueDate;
	private int quantity;
	
	public Lot(String aCode,GregorianCalendar aPreparationDate,GregorianCalendar aDueDate){
		code=aCode;
		preparationDate=aPreparationDate;
		dueDate=aDueDate;
	}
	public Lot(String aCode,GregorianCalendar aPreparationDate,GregorianCalendar aDueDate,int aQuantity){
		code=aCode;
		preparationDate=aPreparationDate;
		dueDate=aDueDate;
		quantity=aQuantity;
	}
	public String getCode(){
		return code;
	}
	public int getQuantity(){
		return quantity;
	}
	public GregorianCalendar getPreparationDate(){
		return preparationDate;
	}
	public GregorianCalendar getDueDate(){
		return dueDate;
	}
	public void setCode(String aCode){
		code=aCode;
	}
	public void setPreparationDate(GregorianCalendar aPreparationDate){
		preparationDate=aPreparationDate;
	}
	public void setQuantity(int aQuantity){
		quantity=aQuantity;
	}
	public void setDueDate(GregorianCalendar aDueDate){
		dueDate=aDueDate;
	}
	public String toString(){
		if(code==null)return MessageBundle.getMessage("angal.medicalstock.nolot");
		return getCode();
	}

	public boolean isValidLot(){
		return getCode().length()<=50;
	}
	@Override
	public int compareTo(Lot o) {
		Lot aLot = (Lot) o;
		Integer d1 = this.getQuantity();
		Integer d2 = aLot.getQuantity();
		return d1.compareTo(d2) * -1;
	}

}
