package com.inf;

/**
 * Created by davidboutet on 17-09-18.
 */
public class Employe {

    private String fullname;
    private Long department_type;
    private Long nbDiploma;
    private Long seniority;
    private Double hourRate;
    private Double workedHours;

    //Constructor
    public Employe(String fullname, Long department_type){
        this.fullname = fullname;
        this.department_type = department_type;
    }

    public Employe(String fullname, Long department_type, Long nbDiploma, Long seniority, Double hourRate, Double workedHours){
        this(fullname, department_type);
        this.nbDiploma = nbDiploma;
        this.seniority = seniority;
        this.hourRate = hourRate;
        this.workedHours = workedHours;
    }

    //Methods
    private static Double getSalary(){

        return 20.00;
    }

    private static Double getSeniorityAmount(){

        return 20.00;
    }

    private static Double getDiplomaAmount(){

        return 20.00;
    }

    public static Double getTotalSalary(){
        return getSalary()+getSeniorityAmount()+getDiplomaAmount();
    }

    public static Double calculRenteProvincial(){
        return 100.00;
    }

    public static Double calculRenteFederal(){
        return 100.00;
    }

@Override
    public String toString() {
        return "{\"name\":\""+this.fullname+"\"," +
                "\"hour_rate\":"+this.hourRate+"}";
    }
}
