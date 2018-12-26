package com.example.hsc.mdtouch;

public class Appointment {

    private String patient;
    private String hospital;
    private String doctor;
    private String problem;

    public Appointment(){

    }

    public Appointment(String p,String hospital,String doctor,String problem){

        this.patient=p;
        this.hospital=hospital;
        this.doctor=doctor;
        this.problem=problem;

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

}