package com.zoho.booktickets.model;

public class User{
    
    private long userId;
    private String name;
    private String mobNo;
    private String emailId;
    private String sex;
    
    public User(long userId, String name, String mobNo,String emailId, String sex){
        this.userId = userId;
        this.name = name;
        this.mobNo = mobNo;
        this.emailId = emailId;
        this.sex = sex;
    }
    
    public User(String name, String mobNo,String emailId, String sex){
        this.name = name;
        this.mobNo = mobNo;
        this.emailId = emailId;
        this.sex = sex;
    }
    
    public void setUserId(long userId){
        this.userId = userId;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public void setMobileNo(String mobNo){
        this.mobNo = mobNo;
    }
    
    public void setEmailId(String emailId){
        this.emailId = emailId;
    }
    
    public void setSex(String sex){
        this.sex = sex;
    }
    public long getUserId(){
        return userId;
    }
    
    public String getName(){
        return name;
    }
    
    public String getMobileNo(){
        return mobNo;
    }
    
    public String getEmailId(){
        return emailId;
    }
    
    public String getSex(){
        return sex;
    }
    
     public String toString(){
         return userId+" "+name+" "+mobNo+" "+emailId+" "+sex;  
     } 
}