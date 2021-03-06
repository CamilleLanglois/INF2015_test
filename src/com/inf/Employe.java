package com.inf;
import net.sf.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by davidboutet on 17-09-18.
 */
public class Employe {
    
    public static ArrayList<Employe> finalEmployeList = new ArrayList<Employe>();

    public static final Double FIXED_AMOUNT = 9733.70;

    private static final Double POURC_ANNUITY_PROV = 0.072;
    private static final Double POURC_ANNUITY_FED = 0.125;

    private final Integer NB_DIPLOMA_BASE = 2;
    private final Double BASE_AMOUT_SENIORITY = 5000.0;

    private final String fullname;
    private final Integer departmentType;
    private final Integer nbDiploma;
    private final Integer seniority;
    private final Double workedHours;
    private final Double hourlyRateMin;
    private final Double hourlyRateMax;
    private final String salaryRevisionDate;


   //we decided to put the validation in the constructor because we think it is more convenial
    public Employe(String fullname, Integer departmentType, Double hourlyRateMin, Double hourlyRateMax,
                   Integer nbDiploma, Integer seniority, Double workedHours, String salaryRevisionDate) throws Exception{

        this.fullname = Validation.invalidFullName(fullname, finalEmployeList);
        this.departmentType = Validation.invalidDepartmentType(departmentType);
        this.hourlyRateMin = Validation.invalidAmount(hourlyRateMin);
        this.hourlyRateMax = Validation.invalidAmount(hourlyRateMax);
        this.nbDiploma = Validation.nbDiplomaIsValid(nbDiploma) + NB_DIPLOMA_BASE;
        this.seniority = Validation.seniorityIsValid(seniority);
        this.workedHours = Validation.workedHoursIsValid(workedHours);
        this.salaryRevisionDate = Validation.dateIsValid(salaryRevisionDate);
        addEmployeToList();
    }
    
    public Double calculateTotalSalary(){
        
        switch (this.departmentType) {
            case 0: return calculateNationalSalary() + calculateNationalSeniority() + calculateNationalDiploma();
            case 1: return calculateRegionalSalary() + calculateRegionalSeniority()+ calculateRegionalDiploma();
            case 2: return calculateInternationalSalary()+ calculateInternationalSeniority() + calculateInternationalDiploma();
            default: return 0.0; //the validation is already done in constructor
        }
    }

    private double calculateNationalSalary() {
        return this.workedHours * this.hourlyRateMin;
    }
    
    private double calculateNationalSeniority() {
        return this.seniority * (0.05 * (calculateNationalSalary())) - BASE_AMOUT_SENIORITY;
    }

    //The amount for the national diploma equals to zero 
    private double calculateNationalDiploma() {
        return 0.0;
    }
    
    
    private double calculateRegionalSalary() {
        return this.workedHours * this.calculateAverageRate();
    }
    
    private double calculateRegionalSeniority() {
        return this.seniority * (0.1 * (calculateRegionalSalary())) - BASE_AMOUT_SENIORITY;
    }

    private double calculateRegionalDiploma() {
        if(this.workedHours <= 500)
            return 0;
        else if(this.workedHours > 500 && this.workedHours <= 1000)
            return this.nbDiploma*500;
        else
            return this.nbDiploma*1000;
    }
    
    
    private double calculateInternationalSalary() {
        return this.workedHours * this.hourlyRateMax;
    }
    
    private double calculateInternationalSeniority() {
        return this.seniority * (0.15 * (calculateInternationalSalary())) - BASE_AMOUT_SENIORITY;
    }
    
    private double calculateInternationalDiploma() {
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

    private Double calculateAverageRate() {
         return (this.hourlyRateMin +this.hourlyRateMax)/2;
    }

    //static public method
    static public Double roundToFive(Double n) {
        return (Math.round(n*20.00))/20.00;
    }

    static public String twoDigits(Double n) {
        DecimalFormat df = new DecimalFormat ("0.00");
        return df.format(n).replace(',','.');
    }

    static public Double calculateProvincialTax(Double total){
        return (total * POURC_ANNUITY_PROV);
    }

    static public Double calculateFederalTax(Double total){
        return (total + calculateProvincialTax(total))* POURC_ANNUITY_FED;
    }
    static public Double stringToDouble(String s){
        s = s.replaceAll(",", ".");
        return Double.parseDouble(s.replace(" $", ""));
    }

    //getter
    public String getFullName(){
        return this.fullname;
    }
    public Double getWorkedHours(){
        return this.workedHours;
    }
    public Integer getDepartmentType(){
        return this.departmentType;
    }
    public Double getHourlyRateMin() {
        return this.hourlyRateMin;
    }
    public Double getHourlyRateMax() {
        return this.hourlyRateMax;
    }
    public String getSalaryRevisionDate(){
        return this.salaryRevisionDate;
    }
    @Override
    public String toString() {
        return this.fullname;
    }
    public String toJSONString() {
        JSONObject json = new JSONObject();
        json.accumulate("name", this.fullname);
        json.accumulate("value_per_employee", twoDigits(this.calculateTotalSalary())+" $");
        return json.toString();
    }
}


