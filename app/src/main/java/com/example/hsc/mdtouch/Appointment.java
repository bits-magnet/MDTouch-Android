package com.example.hsc.mdtouch;

import android.widget.ArrayAdapter;

public class Appointment {

    private String patient;
    private String hospital;
    private String doctor;
    private String problem;
    private String date;
    private String status;

    public Appointment(){

    }

    public Appointment(String p,String hospital,String doctor,String problem,String date,String status){

        this.patient=p;
        this.hospital=hospital;
        this.doctor=doctor;
        this.problem=problem;
        this.date=date;
        this.status=status;
    }

    public void setPatient(String patient){
        this.patient=patient;
    }

    public String  getPatient(){
        return this.patient;
    }

    public void setHospital(String hospital){
        this.hospital=hospital;
    }

    public String  getHospital(){
        return this.hospital;
    }

    public void setDoctor(String doctor){
        this.doctor=doctor;
    }

    public String  getDoctor(){
        return this.doctor;
    }

    public void setProblem(String problem){
        this.problem=problem;
    }

    public String  getProblem(){
        return this.problem;
    }

    public void setDate(String date){
        this.date=date;
    }

    public String  getDate(){
        return this.date;
    }

    public void setStatus(String status){
        this.status=status;
    }

    public String  getStatus(){
        return this.status;
    }



}