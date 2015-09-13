package com.verizontelematics.indrivemobile.models.data;

import java.util.List;

/**
 * Created by z688522 on 3/6/15.
 */
public class UserAccountInfoData {

    private String FirstName;
    private String LastName;
    private String AccountID;
    private String MobileUserID;
    private String Email;
    private String Phone;

    private String AccountType;
    private String Organization;

    private String AccountStatus;
    private AddressType Address;
    private List<SubscriptionInfoType> SubscriptionInfo;

    public UserAccountInfoData() {
        super();
    }

    public AddressType getAddress() {
        return Address;
    }

    public void setAddress(AddressType address) {
        Address = address;
    }

    public String getAccountStatus() {
        return AccountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        AccountStatus = accountStatus;
    }

    public List<SubscriptionInfoType> getSubscriptionInfo() {
        return SubscriptionInfo;
    }

    public void setSubscriptionInfo(List<SubscriptionInfoType> subscriptionInfo) {
        SubscriptionInfo = subscriptionInfo;
    }

    @Override
    public String toString() {
        return "Account Status "+AccountStatus;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getAccountID() {
        return AccountID;
    }

    public void setAccountID(String accountID) {
        AccountID = accountID;
    }

    public String getMobileUserID() {
        return MobileUserID;
    }

    public void setMobileUserID(String mobileUserID) {
        MobileUserID = mobileUserID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAccountType() {
        return AccountType;
    }

    public void setAccountType(String accountType) {
        AccountType = accountType;
    }

    public String getOrganization() {
        return Organization;
    }

    public void setOrganization(String organization) {
        Organization = organization;
    }
}
