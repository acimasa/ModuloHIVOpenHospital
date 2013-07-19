package org.isf.menu.model;

public class User {

	String userName;
	String userGroupName;
	String passwd;
	String desc;
	
	
	public User(String aName, String aGroup, String aPasswd, String aDesc){
		this.userName = aName;
		this.userGroupName = aGroup;
		this.passwd = aPasswd;
		this.desc = aDesc;
	}
	
	public User(){
		this("","","","");
	}
	
	
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getUserGroupName() {
		return userGroupName;
	}
	public void setUserGroupName(String userGroupName) {
		this.userGroupName = userGroupName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String toString(){
		return "(User): "+getUserName()+","+getUserGroupName()+";"+getPasswd()+","+getDesc()+"..";		
	}
	
}//class User
