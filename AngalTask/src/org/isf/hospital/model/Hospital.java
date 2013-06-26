/**
 * @(#) Hospital.java
 * 21-jan-2006
 */
package org.isf.hospital.model;


/**
 * Pure Model Hospital : represents the Hospital 
 * @author bob
 *
 */
public class Hospital {

    private String code;
    private String description;
    private String address;
    private String city;
    private String telephone;
    private String fax;
    private String email;
    private Integer lock;
    /**
     * @param aCode
     * @param aDescription
     * @param aAddress
     * @param aCity
     * @param aTelephone
     * @param aFax
     * @param aEmail
     * @param aLock
     */
    public Hospital(){
    	super();
        // TODO Auto-generated constructor stub
        this.code = null;
        this.description = null;
        this.address = null;
        this.city = null;
        this.telephone = null;
        this.fax = null;
        this.email = null;
        this.lock = null;
    }
    public Hospital(String aCode, String aDescription, String aAddress, String aCity, String aTelephone, String aFax, String aEmail, Integer aLock) {
        super();
        // TODO Auto-generated constructor stub
        this.code = aCode;
        this.description = aDescription;
        this.address = aAddress;
        this.city = aCity;
        this.telephone = aTelephone;
        this.fax = aFax;
        this.email = aEmail;
        this.lock = aLock;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String aAddress) {
        this.address = aAddress;
    }
    public String getCity() {
        return this.city;
    }
    public void setCity(String aCity) {
        this.city = aCity;
    }
    public String getCode() {
        return this.code;
    }
    public void setCode(String aCode) {
        this.code = aCode;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String aDescription) {
        this.description = aDescription;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String aEmail) {
        this.email = aEmail;
    }
    public String getFax() {
        return this.fax;
    }
    public void setFax(String aFax) {
        this.fax = aFax;
    }
    public Integer getLock() {
        return this.lock;
    }
    public void setLock(Integer aLock) {
        this.lock = aLock;
    }
    public String getTelephone() {
        return this.telephone;
    }
    public void setTelephone(String aTelephone) {
        this.telephone = aTelephone;
    }
    
    public boolean equals(Object anObject) {
        return (anObject == null) || !(anObject instanceof Hospital) ? false
                : (getCode().equals(((Hospital) anObject).getCode())
                        && getDescription().equalsIgnoreCase(((Hospital) anObject).getDescription())
                        && getTelephone().equalsIgnoreCase(((Hospital) anObject).getTelephone()) && (getFax()
                        .equalsIgnoreCase(((Hospital) anObject).getFax()) 
                        && (getAddress().equalsIgnoreCase(((Hospital) anObject).getAddress()) 
                        && (getCity().equalsIgnoreCase(((Hospital) anObject).getCity())
                        && (getEmail().equalsIgnoreCase(((Hospital) anObject).getEmail())
                        && (getLock()
                        .equals(((Hospital) anObject).getLock())))))));
    }

}
