package org.isf.medicalstock.model;

import java.util.GregorianCalendar;

import org.isf.medicals.model.Medical;
import org.isf.medstockmovtype.model.MovementType;
import org.isf.ward.model.Ward;
import org.isf.generaldata.MessageBundle;

public class Movement {

	private int code;
	private Medical medical;
	private MovementType type;
	private Ward ward;
	private Lot lot;
	private GregorianCalendar date;
	private int quantity;
	private String origin;
	
	public Movement(Medical aMedical,MovementType aType,Ward aWard,Lot aLot,GregorianCalendar aDate,int aQuantity,String aOrigin){
		medical=aMedical;
		type=aType;
		ward=aWard;
		lot=aLot;
		date=aDate;
		quantity=aQuantity;
		origin=aOrigin;
	}
	
	public int getCode(){
		return code;
	}
	public Medical getMedical(){
		return medical;
	}
	public MovementType getType(){
		return type;
	}
	public Ward getWard(){
		return ward;
	}
	public Lot getLot(){
		return lot;
	}
	public GregorianCalendar getDate(){
		return date;
	}
	public int getQuantity(){
		return quantity;
	}
	public String getOrigin(){
		return origin;
	}
	public void setCode(int aCode){
		code=aCode;
	}
	public void setMedical(Medical aMedical){
		medical=aMedical;
	}
	public void setType(MovementType aType){
		type=aType;
	}
	public void setLot(Lot aLot){
		lot=aLot;
	}
	public String toString(){
		return MessageBundle.getMessage("angal.medicalstock.medical")+":"+medical.toString()+MessageBundle.getMessage("angal.medicalstock.type")+":"+type.toString()+MessageBundle.getMessage("angal.medicalstock.quantity")+":"+quantity;
	}
}
