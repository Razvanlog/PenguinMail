package me;

public class Contact implements personSetable{
    private String name,familyName;
    private String email;
    private String phoneNumber;
    private String Business;
    private String Description;
    private int priorityValue;
    public Contact(String Email,String Name,String FamilyName,String PhoneN,String Bus,String des,int p){
        email=Email;
        name=Name;
        familyName=FamilyName;
        phoneNumber=PhoneN;
        Business=Bus;
        Description=des;
        priorityValue=p;
    }
    public String getName(){
        return name;
    }
    public String getFamilyName(){
        return familyName;
    }
    public void setName(String x){
        name=x;
    }
    public void setFamilyName(String x){
        familyName=x;
    }
    public String getPhoneNumber(){
        return phoneNumber;
    }
    public void setPhoneNumber(String x){
        phoneNumber=x;
    }
    public String getBusiness(){
        return Business;
    }
    public void setBusiness(String x){
        Business=x;
    }
    public String getDescription(){
        return Description;
    }
    public void setDescription(String x){
        Description=x;
    }
    @Override
    public String getEmail(){
        return email;
    }
    @Override
    public void setEmail(String x){
        email=x;
    }
    @Override
    public int getPriority(){
        return priorityValue;
    }
    @Override
    public void setPriority(int x){
        priorityValue=x;
    }
}
