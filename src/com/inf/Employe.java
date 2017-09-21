package com.inf;

/**
 * Created by davidboutet on 17-09-18.
 */
public class Employe {
    
    private final Integer NB_DIPLOMES_BASE = 2;
    private final Double MONTANT_FIXE = 9733.70;
    private final Double POURC_RENTE_PROV = 0.072;
    private final Double POURC_RENTE_FED = 0.125;
    
    private String fullname;
    private Integer department_type;
    private Integer nbDiploma;
    private Integer seniority;
    private Double hourRate;
    private Double workedHours;

    //Constructor
    public Employe(String fullname, Integer department_type){
        this.fullname = fullname;
        this.department_type = department_type;
    }

    public Employe(String fullname, Integer department_type, Integer nbDiploma, Integer seniority, Double hourRate, Double workedHours){
        this(fullname, department_type);
        this.nbDiploma = nbDiploma;
        this.seniority = seniority;
        this.hourRate = hourRate;
        this.workedHours = workedHours;
    }

    //Methods
    
    // charge de travail - JAde
    private static Double getSalary(){

        return 20.00;
    }
    //Camille
    private Double getSeniorityAmount(){
  
        Double pourcentageValeurSalariale = 0.0;
        
        switch(this.department_type) {
            
            case 0: pourcentageValeurSalariale = 0.05;
            break;
            case 1: pourcentageValeurSalariale = 0.1;
            break;
            case 2 : pourcentageValeurSalariale = 0.15;
            break;
        }
        return (this.seniority * (pourcentageValeurSalariale * this.getSalary()) - 5000);
    }
    //Simon
    private static Double getDiplomaAmount(){

        return 20.00;
    }
    //Jade
    private static Double roundToFive(Double n) {
        return 20.00;
    }
    // Simon
    private static Double averageRate(Double minRate, Double maxRate) {
        return 20.00;
    }
    //Camille
    public static Double getTotalSalary(){
        return getSalary()+getSeniorityAmount()+getDiplomaAmount();
    }
    //Camille 
    public static Double calculRenteProvincial(){
        return 100.0;
    }
    // Simon
    public static Double calculRenteFederal(){
        return 100.00;
    }

@Override
    public String toString() {
        return "{\"name\":\""+this.fullname+"\"," +
                "\"hour_rate\":"+this.hourRate+"}";
    }
}