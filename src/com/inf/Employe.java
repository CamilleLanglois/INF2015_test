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
    private Double hourly_rate_min;
    private Double hourly_rate_max;

    //Constructor
    public Employe(String fullname, Integer department_type, Double hourly_rate_min, Double hourly_rate_max,
                   Integer nbDiploma, Integer seniority, Double hourRate, Double workedHours){
        this.fullname = fullname;
        this.department_type = department_type;
        this.hourly_rate_min = hourly_rate_min;
        this.hourly_rate_max = hourly_rate_max;
        this.nbDiploma = nbDiploma;
        this.seniority = seniority;
        this.hourRate = hourRate;
        this.workedHours = workedHours;
    }

    //Methods
    
    // charge de travail - Jade
    private Double getSalary(){
        Double valeurSalarialeSelonHeureTaux = 0.0;
        switch(this.department_type) {
            case 0:
                valeurSalarialeSelonHeureTaux = this.workedHours + this.hourly_rate_min;
                break;
            case 1:
                valeurSalarialeSelonHeureTaux = this.workedHours * ((this.hourly_rate_min*this.hourly_rate_max)/2);
                break;
            case 2 :
                valeurSalarialeSelonHeureTaux = this.workedHours * this.hourly_rate_max;
                break;
        }
        return valeurSalarialeSelonHeureTaux;
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
    private Double getDiplomaAmount(){

        return 20.00;
    }
    //Jade
    static public Double roundToFive(Double n) {
        Double n2=0.00;
        n2 = Math.ceil (n*20.00)/20.00;
        return n2;
    }
    // Simon
    private Double averageRate(Double minRate, Double maxRate) {
        return 20.00;
    }
    //Camille
    public Double getTotalSalary(){
        return getSalary()+getSeniorityAmount()+getDiplomaAmount();
    }
    //Camille 
    public Double calculRenteProvincial(){
        return 100.0;
    }
    // Simon
    public Double calculRenteFederal(){
        return 100.00;
    }

@Override
    public String toString() {
        return this.fullname;
    }
    public String toJSONString() {
        return "{\"name\":\""+this.fullname+"\"," +
                "\"valeur_par_employe\":"+this.getTotalSalary()+"}";
    }
}
