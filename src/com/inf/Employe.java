package com.inf;
import net.sf.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by davidboutet on 17-09-18.
 */
public class Employe {
    //public static variables
    public static ArrayList<Employe> finalEmployeList = new ArrayList<Employe>();

    //public static constant
    public static final Double FIXED_AMOUNT = 9733.70;

    //private static constant
    private static final Double POURC_ANNUITY_PROV = 0.072;
    private static final Double POURC_ANNUITY_FED = 0.125;

    //private constant
    private final Integer NB_DIPLOMA_BASE = 2;
    private final Double BASE_AMOUT_SENIORITY = 5000.0;

    //private variable
    private String fullname;
    private Integer departmentType;
    private Integer nbDiploma;
    private Integer seniority;
    private Double workedHours;
    private Double hourlyRateMin;
    private Double hourlYRateMax;

    //Constructor
    public Employe(String fullname, Integer departmentType, Double hourlyRateMin, Double hourlYRateMax,
                   Integer nbDiploma, Integer seniority, Double workedHours){
        this.fullname = fullname;
        this.departmentType = departmentType;
        this.hourlyRateMin = hourlyRateMin;
        this.hourlYRateMax = hourlYRateMax;
        this.nbDiploma = nbDiploma + NB_DIPLOMA_BASE;
        this.seniority = seniority;
        this.workedHours = workedHours;
        addEmployeToList();
    }

    //private methods
    private Double getSalary(){
        Double valeurSalarialeSelonHeureTaux = 0.0;
        switch(this.departmentType) {
            case 0:
                valeurSalarialeSelonHeureTaux = this.workedHours + this.hourlyRateMin;
                break;
            case 1:
                valeurSalarialeSelonHeureTaux = this.workedHours * this.averageRate();
                break;
            case 2 :
                valeurSalarialeSelonHeureTaux = this.workedHours * this.hourlYRateMax;
                break;
        }
        return valeurSalarialeSelonHeureTaux;
    }

    private Double getSeniorityAmount(){
        Double pourcentageValeurSalariale = 0.0;
        
        switch(this.departmentType) {
            case 0: pourcentageValeurSalariale = 0.05;
            break;
            case 1: pourcentageValeurSalariale = 0.1;
            break;
            case 2 : pourcentageValeurSalariale = 0.15;
            break;
        }
        return (this.seniority * (pourcentageValeurSalariale * this.getSalary()) - BASE_AMOUT_SENIORITY);
    }

    private Double getDiplomaAmount(){

        double diplomaAmount = 0;
                
        if(null != this.departmentType)switch (this.departmentType) {
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
    
    private Double averageRate() {
        return (this.hourlyRateMin +this.hourlYRateMax)/2;
    }

    public Double getTotalSalary(){
        return getSalary()+getSeniorityAmount()+getDiplomaAmount();
    }

    private void addEmployeToList(){
        finalEmployeList.add(this);
    }


    //static public method
    static public Double roundToFive(Double n) {
        Double n2=0.00;
        n2 = Math.ceil (n*20.00)/20.00;
        return n2;
    }

    static public String twoDigits(Double n) {
        DecimalFormat df = new DecimalFormat ("0.00");
        return df.format(n).replace(',','.');
    }

    static public Double calculRenteProvincial(Double total){
        return (total * POURC_ANNUITY_PROV);
    }

    static public Double calculRenteFederal(Double total){
        return (total + calculRenteProvincial(total))* POURC_ANNUITY_FED;
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
