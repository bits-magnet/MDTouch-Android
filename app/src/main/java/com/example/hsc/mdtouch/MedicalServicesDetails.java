package com.example.hsc.mdtouch;

public class MedicalServicesDetails {

    String name;
    String address;
    String city;
    String contact;

    public MedicalServicesDetails(String a,String b,String c,String d){

        this.name=a;
        this.address=b;
        this.city=c;
        this.contact=d;

    }

    public String getName(){
        return name;
    }

    public String getAddress(){
        return address;
    }

    public String getCity(){
        return city;
    }

    public String getContact(){
        return contact;
    }
}