package com.example.hsc.mdtouch;

public class EventItems {

    String eventLocation;
    String city;
    String state;
    String title;
    String description;
    String pic;
    String dateOfEvent;
    String registered;
    String hospitalId;
    String bloodId;
    String dispensaryId;
    String testId;

    public EventItems(String el,String c,String s,String t,String d,String p,String da,String r,String hid,String bid,String did,String tid){

        this.eventLocation=el;
        this.city=c;
        this.state=s;
        this.title=t;
        this.description=d;
        this.pic=p;
        this.dateOfEvent=da;
        this.registered=r;
        this.hospitalId=hid;
        this.bloodId=bid;
        this.dispensaryId=did;
        this.testId=tid;
    }

    public String getPic(){ return pic; }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public String getDate(){
        return dateOfEvent;
    }

    public String getRegistered(){
        return registered;
    }
}