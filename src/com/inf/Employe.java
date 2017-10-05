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
    
    public Double getTotalSalary(){
        
        switch (this.departmentType) {
            case 0: return getNationalSalary() + getNationalSeniority() + getNationalDiploma();
            case 1: return getRegionalSalary() + getRegionalSeniority()+ getRegionalDiploma();
            case 2: return getInternationalSalary()+ getInternationalSeniority() + getInternationalDiploma();
            default: return 0.0; //TODO: lancer une exception
        }
    }
    
    private double getNationalSalary() {
        return this.workedHours + this.hourlyRateMin;
    }
    
    private double getNationalSeniority() {
        return this.seniority * (0.05 * (getNationalSalary())) - BASE_AMOUT_SENIORITY;
    }

    private double getNationalDiploma() {
        return 0.0;
    }
    
    private double getRegionalSalary() {
        return this.workedHours * this.averageRate();
    }
    
    private double getRegionalSeniority() {
        return this.seniority * (0.1 * (getRegionalSalary())) - BASE_AMOUT_SENIORITY;
    }

    private double getRegionalDiploma() {
        if(this.workedHours <= 500)
            return 0;
        else if(this.workedHours > 500 && this.workedHours <= 1000)
            return this.nbDiploma*500;
        else
            return this.nbDiploma*1000;
    }
    
    
    private double getInternationalSalary() {
        return this.workedHours * this.hourlYRateMax;
    }
    
    private double getInternationalSeniority() {
        return this.seniority * (0.15 * (getInternationalSalary())) - BASE_AMOUT_SENIORITY;
    }
    
    private double getInternationalDiploma() {
        double amount;
        if(this.workedHours <= 500)
            amount = this.nbDiploma*500;
        else
            amount = this.nbDiploma*1500;
        if(amount>5000)
            amount=5000;
        return amount;
    }
    
    private void addEmployeToList(){
        finalEmployeList.add(this);
    }

    private Double averageRate() {
         return (this.hourlyRateMin +this.hourlYRateMax)/2;
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
        s = s.replaceAll(",", ".");
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


