package org.isf.patient.model;

import java.awt.Image;
import java.util.Date;

import org.isf.opd.model.Opd;

/*------------------------------------------
 * Patient - model for the patient entity
 * -----------------------------------------
 * modification history
 * 05/05/2005 - giacomo  - first beta version 
 * 03/11/2006 - ross - added toString method
 * 11/08/2008 - Alessandro - added mother and father names textfield
 * 						   - added birthdate and age check
 * 19/08/2008 - Mex        - substitute EduLevel with BloodType
 * 22/08/2008 - Claudio    - added birth date field
 * 						   - modified age field
 * 01/01/2009 - Fabrizio   - modified age field type back to int
 *                         - removed unuseful super() call in constructor
 *                         - removed unuseful todo comment
 *                         - removed assignment to attribute hasInsurance
 *                           since it had no effect
 * 16/09/2009 - Alessandro - added equals override to support comparing
 * 							 and filtering
 * 17/10/2011 - Alessandro - added height and weight (from malnutritionalcontrol)
 * 
 *------------------------------------------*/

public class Patient {
	/*
	 * PAT_ID int NOT NULL AUTO_INCREMENT , PAT_FNAME varchar (50) NOT NULL ,
	 * --first name (nome) PAT_SNAME varchar (50) NOT NULL , --second name
	 * (cognome) PAT_AGE int NOT NULL , --age PAT_SEX char (1) NOT NULL , --sex :
	 * M or F PAT_ADDR varchar (50) NULL , --address (via , n.) PAT_CITY varchar
	 * (50) NOT NULL , --city PAT_NEXT_KIN varchar (50) NULL , --next kin
	 * (parente prossimo, figlio di..) PAT_TELE varchar (50) NULL , --telephone
	 * number PAT_MOTH char (1) NULL , --mother: D=dead, A=alive PAT_FATH char
	 * (1) NULL , --father: D=dead, A=alive PAT_LEDU char (1) NULL , --level of
	 * education: 1 or 2 or 3 or 4 PAT_ESTA char (1) NULL , --economic status:
	 * R=rich, P=poor PAT_PTOGE char (1) NULL , --parents together: Y or N
	 * PAT_LOCK int NOT NULL default 0, PRIMARY KEY ( PAT_ID )
	 */

	private Integer code;
	private String firstName;
	private String secondName;
	private Date birthDate;
	private int age;
	private String agetype;
	private char sex;
	private String address;
	private String city;
	private String nextKin;
	private String telephone;
	private String note;
	private String mother_name; // mother's name
	private char mother; // D=dead, A=alive
	private String father_name; // father's name
	private char father; // D=dead, A=alive
	//private char levelEdu; // 1 or 2 or 3 or 4
	private String bloodType; // (0-/+, A-/+ , B-/+, AB-/+)
	private char hasInsurance; // Y=Yes, N=no
	private char parentTogether; // parents together: Y or N
	private String taxCode;
	private float height;
	private float weight;
	private int lock;
	private Image photo;
	
	private volatile int hashCode = 0;
	

	public Patient() {
		
		this.firstName = "";
		this.secondName = ""; 
		 this.birthDate = null;
		 this.age = 0;
		 this.agetype = "";
		 this.sex = ' ';
		 this.address = "";
		 this.city = "";
		 this.nextKin = ""; 
		 this.telephone = "";
		 this.mother_name = "";
		 this.mother = ' ';
		 this.father_name = "";
		 this.father = ' ';
		 this.bloodType = "";
		 this.hasInsurance = ' ';
		 this.parentTogether = ' ';
		 this.taxCode = "";
		 this.height = 0;
		 this.weight = 0;
		 this.lock = 0;
	}
	
	public Patient(Opd opd) {
		
		 this.firstName = opd.getfirstName();
		 this.secondName = opd.getsecondName(); 
		 this.birthDate = null;
		 this.age = opd.getAge();
		 this.agetype = "";
		 this.sex = opd.getSex();
		 this.address = opd.getaddress();
		 this.city = opd.getcity();
		 this.nextKin = opd.getnextKin(); 
		 this.telephone = "";
		 this.mother_name = "";
		 this.mother = ' ';
		 this.father_name = "";
		 this.father = ' ';
		 this.bloodType = "";
		 this.hasInsurance = ' ';
		 this.parentTogether = ' ';
		 this.lock = 0;
	}
	
	public Patient(String firstName, String secondName, Date birthDate, int age, String agetype, char sex,
			String address, String city, String nextKin, String telephone,
			String mother_name, char mother, String father_name, char father,
			String bloodType, char economicStatut, char parentTogether, int lock, String personalCode) { //Changed EduLev with bloodType
		this.firstName = firstName;
		this.secondName = secondName;
		this.birthDate = birthDate;
		this.age = age;
		this.agetype = agetype;
		this.sex = sex;
		this.address = address;
		this.city = city;
		this.nextKin = nextKin;
		this.telephone = telephone;
		this.mother_name = mother_name;
		this.mother = mother;
		this.father_name = father_name;
		this.father = father;
		this.hasInsurance = economicStatut;
		this.bloodType = bloodType;
		this.parentTogether = parentTogether;
		this.taxCode = personalCode;
		this.height = 0;
		this.weight = 0;
		this.lock = lock;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	/**
	 * @return the birthDate
	 */
	public Date getBirthDate() {
		return birthDate;
	}

	/**
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setAgetype(String agetype) {
		this.agetype = agetype;
	}

	public String getAgetype() {
		return agetype;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public int getLock() {
		return lock;
	}

	public void setLock(int lock) {
		this.lock = lock;
	}

	public String getNextKin() {
		return nextKin;
	}

	public void setNextKin(String nextKin) {
		this.nextKin = nextKin;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public char getSex() {
		return sex;
	}

	public void setSex(char sex) {
		this.sex = sex;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/*public char getLevelEdu() {
		return levelEdu;
	}*/

	public String getBloodType() {
	    return bloodType;
	}
	
	/*public void setLevelEdu(char levelEdu) {
		this.levelEdu = levelk;
	}*/

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}
	
	public String getName() {
		return getSecondName() + " " + getFirstName();
	}

	public char getHasInsurance() {
		return hasInsurance;
	}

	public void setHasInsurance(char hasInsurance) {
		this.hasInsurance = hasInsurance;
	}

	public char getFather() {
		return father;
	}

	public void setFather(char father) {
		this.father = father;
	}

	public char getMother() {
		return mother;
	}

	public void setMother(char mother) {
		this.mother = mother;
	}

	public char getParentTogether() {
		return parentTogether;
	}

	public void setParentTogether(char parentTogether) {
		this.parentTogether = parentTogether;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String toString() {
		return getName();
	}

	// added
	public String getFather_name() {
		return father_name;
	}

	public void setFather_name(String father_name) {
		this.father_name = father_name;
	}

	public String getMother_name() {
		return mother_name;
	}

	public void setMother_name(String mother_name) {
		this.mother_name = mother_name;
	}

	public Image getPhoto() {
		return photo;
	}
	
	public void setPhoto(Image photo) {
		this.photo = photo;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}
	
	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!(obj instanceof Patient)) {
			return false;
		}
		
		Patient patient = (Patient)obj;
		return (this.getCode().equals(patient.getCode()));
	}
	
	@Override
	public int hashCode() {
	    if (this.hashCode == 0) {
	        final int m = 23;
	        int c = 133;
	        
	        c = m * c + ((code == null) ? 0 : code.intValue());
	        
	        this.hashCode = c;
	    }
	  
	    return this.hashCode;
	}
	
	public String getSearchString() {
		StringBuffer sbName = new StringBuffer();
		sbName.append(getCode());
		sbName.append(" ");
		sbName.append(getFirstName().toLowerCase());
		sbName.append(" ");
		sbName.append(getSecondName().toLowerCase());
		sbName.append(" ");
		sbName.append(getCity().toLowerCase());
		sbName.append(" ");
		sbName.append(getAddress().toLowerCase());
		sbName.append(" ");
		sbName.append(getTelephone());
		sbName.append(" ");
		sbName.append(getNote().toLowerCase());
		sbName.append(" ");
		sbName.append(getTaxCode().toLowerCase());
		sbName.append(" ");
		return sbName.toString();
	}
	
	public String getInformations() {
		int i = 0;
		StringBuffer infoBfr = new StringBuffer();
		if (!city.equals("")) {
			infoBfr.append(i > 0 ? " - " : "");
			infoBfr.append(city);
			i++;
		}
		if (!address.equals("")) {
			infoBfr.append(i > 0 ? " - " : "");
			infoBfr.append(address);
			i++;
		}
		if (!telephone.equals("")) {
			infoBfr.append(i > 0 ? " - " : "");
			infoBfr.append(telephone);
			i++;
		}
		if (!note.equals("")) {
			infoBfr.append(i > 0 ? " - " : "");
			infoBfr.append(note);
			i++;
		}
		if (!taxCode.equals("")) {
			infoBfr.append(i > 0 ? " - " : "");
			infoBfr.append(taxCode);
			i++;
		}
		return infoBfr.toString();
	}
}

