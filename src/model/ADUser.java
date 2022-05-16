package com.zoho.booktickets.model;

public class ADUser {

    private String displayName;
    private String userName;
    private String firstName;
    private String lastName;
    private String password;
    private String mail;
    private String description;
    private String mobileNumber;
    private String office;
    private String url;
    private String accountExpires;

    //Add User
    public ADUser(String displayName, String userName, String firstName, String lastName, String password) {
        this.displayName = displayName;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }
    
    public ADUser(String displayName, String userName, String firstName, String lastName, String password, String mail,
            String description, String mobileNumber, String office, String url, String accountExpires) {
        this.displayName = displayName;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.mail = mail;
        this.description = description;
        this.mobileNumber = mobileNumber;
        this.office = office;
        this.url = url;
        this.accountExpires = accountExpires;
    }

    public ADUser(String displayName, String userName, String firstName, String lastName) {
        this.displayName = displayName;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
    }


    // Update User
    public ADUser(String displayName, String userName, String firstName, String lastName, String password, String mail,
            String description, String mobileNumber, String office, String url) {
        this.displayName = displayName;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.mail = mail;
        this.description = description;
        this.mobileNumber = mobileNumber;
        this.office = office;
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMail() {
        return mail;
    }

    public String getAccountExpires() {
        return accountExpires;
    }

    public void setAccountExpires(String accountExpires) {
        this.accountExpires = accountExpires;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}