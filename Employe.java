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
    private static Double getSeniorityAmount(){

        return 20.00;
    }
    //Simon
    private Double getDiplomaAmount(){
        
        double diplomaAmount = 0;
                
        if(null != this.department_type)switch (this.department_type) {
            case 0:
                diplomaAmount = 0;
                break;
            case 1:
                if(this.workedHours <= 500)
                    diplomaAmount = 0;
                else if(this.workedHours > 500 && this.workedHours <= 10000)
                    diplomaAmount = this.nbDiploma*500;
                else
                    diplomaAmount = this.nbDiploma*1000;
                break;
            case 2:
                if(this.workedHours <= 500)
                    diplomaAmount = this.nbDiploma*500;
                else if(this.workedHours > 500)
                    diplomaAmount = this.nbDiploma*1500;
                break;
            default:
                break;
        }
        if(diplomaAmount>5000)
            diplomaAmount=5000;
        
        return diplomaAmount;
    }
    //Jade
    private static Double roundToFive(Double n) {
        return 20.00;
    }
    // Simon
    //averageRate ou medianRate?
    private static Double averageRate(Double minRate, Double maxRate) {
        return (minRate+maxRate)/2;
    }
    //Camille
    public static Double getTotalSalary(){
        return getSalary()+getSeniorityAmount()+getDiplomaAmount();
    }
    //Camille 
    public static Double calculRenteProvincial(){
        return 100.00;
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
