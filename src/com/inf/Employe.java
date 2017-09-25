package com.inf;
import net.sf.json.JSONObject;

import java.text.DecimalFormat;

/**
 * Created by davidboutet on 17-09-18.
 */
public class Employe {
    
    private final Integer NB_DIPLOMES_BASE = 2;
    public static final Double MONTANT_FIXE = 9733.70;
    private final Double POURC_RENTE_PROV = 0.072;
    private final Double POURC_RENTE_FED = 0.125;
    private final Double MONTANT_BASE_ANCIENNETE = 5000.0;
    
    private String fullname;
    private Integer department_type;
    private Integer nbDiploma;
    private Integer seniority;
    private Double workedHours;
    private Double hourly_rate_min;
    private Double hourly_rate_max;

    //Constructor
    public Employe(String fullname, Integer department_type, Double hourly_rate_min, Double hourly_rate_max,
                   Integer nbDiploma, Integer seniority, Double workedHours){
        this.fullname = fullname;
        this.department_type = department_type;
        this.hourly_rate_min = hourly_rate_min;
        this.hourly_rate_max = hourly_rate_max;
        this.nbDiploma = nbDiploma + NB_DIPLOMES_BASE;
        this.seniority = seniority;
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
        return (this.seniority * (pourcentageValeurSalariale * this.getSalary()) - MONTANT_BASE_ANCIENNETE);
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
    static public Double roundToFive(Double n) {
        Double n2=0.00;
        n2 = Math.ceil (n*20.00)/20.00;
        return n2;
    }
    
     static public String twoDigits(Double n) {
        DecimalFormat df = new DecimalFormat ("0.00");
        return df.format(n);
    }
    
    private Double averageRate(Double minRate, Double maxRate) {
        return (minRate+maxRate)/2;
    }
    //Camille
    public Double getTotalSalary(){
        return getSalary()+getSeniorityAmount()+getDiplomaAmount();
    }
    //Camille 
    public Double calculRenteProvincial(){
        return (getTotalSalary()* POURC_RENTE_PROV);
    }
    // Simon
    public Double calculRenteFederal(){
        return (getTotalSalary()+ calculRenteProvincial())* POURC_RENTE_FED;
    }
    static public Double stringToDouble(String s){
        return Double.parseDouble(s.replace(" $", ""));
    }

@Override
    public String toString() {
        return this.fullname;
    }
    public String toJSONString() {
        JSONObject json = new JSONObject();
        json.accumulate("name", this.fullname);
        json.accumulate("valeur_par_employe", twoDigits(this.getTotalSalary())+" $");
        return json.toString();
    }
}
