package com.example.auotmaticattendance.Attendance_section.View_Attendance;

public class viewobject {

    private String rollno;
    private String name;
    private String attended;
    private String total_classes;
    public  viewobject(String rollno,String name,String attended,String total_classes){

        this.rollno=rollno;
        this.name=name;
        this.attended=attended;
        this.total_classes=total_classes;
    }

    public void setAttended(String attended) {
        this.attended = attended;
    }

    public String getAttended() {
        return attended;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public String getTotal_classes() {
        return total_classes;
    }

    public void setTotal_classes(String total_classes) {
        this.total_classes = total_classes;
    }
}
