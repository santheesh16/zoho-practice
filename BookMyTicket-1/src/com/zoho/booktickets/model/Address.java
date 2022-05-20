package com.zoho.booktickets.model;

public class Address{
    private long addressId;
    private String streetNo;
    private String city;
    private String state;
    private String pinCode;
    private String landmark;
    
    public Address(long addressId,String streetNo, String city, String state, String pinCode,String landmark){
        this.addressId = addressId;
        this.streetNo = streetNo;
        this.city = city;
        this.state = state;
        this.pinCode = pinCode;
        this.landmark = landmark;
    }
    
    public Address(String streetNo, String city, String state, String pinCode,String landmark){
        this.streetNo = streetNo;
        this.city = city;
        this.state = state;
        this.pinCode = pinCode;
        this.landmark = landmark;
    }
    
    public void setAddressId(long addressId){
        this.addressId = addressId;
    }
    
    public long getAddressId(){
        return addressId;
    }
    
    public String getStreet(){
        return streetNo;
    }
    
    public String getCity(){
        return city;
    }
    
    public String getState(){
        return state;
    }
    public String getPincode(){
        return pinCode;
    }
    public String getLandmark(){
        return landmark;
    }
    
    public String toString(){
         return new StringBuilder().append(streetNo).append(",").append(city)
				.append(",").append(state).append(",").append(pinCode).append(",").append(landmark).toString();
     } 
}