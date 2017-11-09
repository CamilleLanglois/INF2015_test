package com.inf;
import net.sf.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import com.inf.Validation;



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

    private String fullname;
    private Integer departmentType;
    private Integer nbDiploma;
    private Integer seniority;
    private Double workedHours;
    private Double hourlyRateMin;
    private Double hourlyRateMax;
    private String salaryRevisionDate;


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
        Double totalSalary = 0.0;
        switch (this.departmentType) {
            case 0: totalSalary = calculateNationalSalary() + calculateNationalSeniority() + calculateNationalDiploma();
            break;
            case 1: totalSalary = calculateRegionalSalary() + calculateRegionalSeniority()+ calculateRegionalDiploma();
            break;
            case 2: totalSalary = calculateInternationalSalary()+ calculateInternationalSeniority() + calculateInternationalDiploma();
            break;
            
        }
        return Validation.invalidAmount(totalSalary);
    }

    private double calculateNationalSalary() {
        return this.workedHours + this.hourlyRateMin;
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
        Double temp=0.00;
        
        temp = (Math.round(n*20.00))/20.00;

        return temp;
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
        json.accumulate("valeur_par_employe", twoDigits(this.calculateTotalSalary())+" $");
        return json.toString();
    }
}


