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
    private static Double hourly_rate_min;
    private static Double hourly_rate_max;

    //Constructor
    public Employe(String fullname, Integer department_type, Double hourly_rate_min, Double hourly_rate_max ){
        this.fullname = fullname;
        this.department_type = department_type;
        this.hourly_rate_min = hourly_rate_min; 
        this.hourly_rate_max = hourly_rate_max;
    }

    public Employe(String fullname, Integer department_type, Integer nbDiploma, Integer seniority, Double hourRate, Double workedHours){
        this(fullname, department_type, hourly_rate_min, hourly_rate_max); 
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
                valeurSalarialeSelonHeureTaux = workedHours + hourly_rate_min;
            break;
            case 1: 
                valeurSalarialeSelonHeureTaux = workedHours * ((hourly_rate_min*hourly_rate_max)/2);
            break;
            case 2 : 
                valeurSalarialeSelonHeureTaux = workedHours * hourly_rate_max;
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
    private static Double getDiplomaAmount(){

        return 20.00;
    }
    //Jade
    private static Double roundToFive(Double n) {
        double n2=0.00;
        n2 = (double)Math.ceil (n*20.00)/20.00; 
        return n2;
    }
    // Simon
    private static Double averageRate(Double minRate, Double maxRate) {
        return 20.00;
    }
    //Camille
    /*public static Double getTotalSalary(){
        return getSalary()+getSeniorityAmount()+getDiplomaAmount();
    }*/
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
