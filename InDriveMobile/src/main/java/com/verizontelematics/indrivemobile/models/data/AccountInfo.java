package com.verizontelematics.indrivemobile.models.data;


public class AccountInfo extends BaseData{

private String mobileUserID;
private String accountID;
private String accountType;
private String firstName;
private String lastName;
private String email;
private String phone;
private String accountStatus;
private String address1;
private String address2;
private String city;

private String state;
private String country;
private String zipCode;
private String organization;
private String subscriptionPackageName;
private String subscriptionPartNumber;


public  String  getMobileUserID(){
	return mobileUserID;
}
public void setMobileUserID( String  mobileUserID){
	this.mobileUserID = mobileUserID;
}

public  String  getAccountID(){
	return accountID;
}
public void setAccountID( String  accountID){
	this.accountID = accountID;
}

public  String  getAccountType(){
	return accountType;
}
public void setAccountType( String  accountType){
	this.accountType = accountType;
}

public  String  getFirstName(){
	return firstName;
}
public void setFirstName( String  firstName){
	this.firstName = firstName;
}

public  String  getLastName(){
	return lastName;
}
public void setLastName( String  lastName){
	this.lastName = lastName;
}

public  String  getEmail(){
	return email;
}
public void setEmail( String  email){
	this.email = email;
}

public  String  getPhone(){
	return phone;
}
public void setPhone( String  phone){
	this.phone = phone;
}

public  String  getAccountStatus(){
	return accountStatus;
}
public void setAccountStatus( String  accountStatus){
	this.accountStatus = accountStatus;
}

public  String  getAddress1(){
	return address1;
}
public void setAddress1( String  address1){
	this.address1 = address1;
}

public  String  getAddress2(){
	return address2;
}
public void setAddress2( String  address2){
	this.address2 = address2;
}

public  String  getCity(){
	return city;
}
public void setCity( String  city){
	this.city = city;
}

public  String  getState(){
	return state;
}
public void setState( String  state){
	this.state = state;
}

public  String  getCountry(){
	return country;
}
public void setCountry( String  country){
	this.country = country;
}


public  String  getZipCode(){
	return zipCode;
}
public void setZipCode( String  zipCode){
	this.zipCode = zipCode;
}

public  String  getOrganization(){
	return organization;
}
public void setOrganization( String  organization){
	this.organization = organization;
}

public  String  getSubscriptionPackageName(){
	return subscriptionPackageName;
}
public void setSubscriptionPackageName( String  subscriptionPackageName){
	this.subscriptionPackageName = subscriptionPackageName;
}

public  String  getSubscriptionPartNumber(){
	return subscriptionPartNumber;
}
public void setSubscriptionPartNumber( String  subscriptionPartNumber){
	this.subscriptionPartNumber = subscriptionPartNumber;
}

}