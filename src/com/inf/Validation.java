package com.inf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.InputMismatchException;
import net.sf.json.*;

public class Validation {
    public static Integer invalidDepartmentType(Integer departementType) throws IllegalArgumentException {
        if ((departementType != 0) && (departementType !=1) && (departementType !=2))  {
            throw new IllegalArgumentException("The type of departement must be 0, 1 or 2.");
        }
        return departementType;
    }
    
    public static Integer seniorityIsValid(Integer seniority) throws IllegalArgumentException {
        if (seniority < 0)  {
            throw new IllegalArgumentException("The seniority must be greater than or equal to 0.");
        } else if (seniority > 10) {
            throw new IllegalArgumentException("The seniority must be less than or equal to 10.");
        }
        return seniority;
    }
    

    public static Integer nbDiplomaIsValid(Integer nbDiploma) throws IllegalArgumentException {
        if (nbDiploma < 0)  {
            throw new IllegalArgumentException("The number of diploma must be greater than or equal to 0.");
        } else if (nbDiploma > 5) {
            throw new IllegalArgumentException("The number of diploma must be less than or equal to 5.");
        }
        return nbDiploma;
    }
    
    public static Double workedHoursIsValid(Double workedHours) throws IllegalArgumentException {
        if ((workedHours < 0) || (workedHours > 1950)) {
            throw new IllegalArgumentException("Employes worked hours must be between 0 and 1950");
        }
        return workedHours;
    }
    
    
    public static Double invalidAmount(Double amount) throws IllegalArgumentException {
        if ( amount < 0)  {
            throw new IllegalArgumentException("The amount must be greater than or equal to 0.");
        }
        return amount;
    }
    
    public static String dateIsValid(String date) throws InputMismatchException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date);
        } catch (ParseException e) {
            throw new InputMismatchException("Incorrect date format in JSON");
        }
        return date;
    }
    public static void checkResultValues(ArrayList<Employe> e, Double totalValue){
        
        checkEmployeValue(e);
        checkWorkedHours(e);
        checkHourlyRates(e);
        checkTotalValue(totalValue);
        checkAnnuities(totalValue);
        checkRevisionDates(e);
    }
    public static void checkTotalValue(Double totalValue){
        if (totalValue > 500000.00){
            new Recommandation("Total departement value should be lower than 500 000.00$");
        }
    }
    public static void checkEmployeValue(ArrayList<Employe> employes){
        for(Employe e: employes){
            if(e.calculateTotalSalary() > 150000.00){
                new Recommandation("La valeur par employé de " + e.getFullName() + " est trop dispendieuse.");
            } 
        }
    }
    public static void checkWorkedHours(ArrayList<Employe> employes){
        for(Employe e: employes){
            if(e.getWorkedHours() < 500){
                new Recommandation("La charge de travail de " + e.getFullName()+ " est inférieure à 500 heures");
            }
        }
    }
    public static void checkHourlyRates(ArrayList<Employe> employes){
        Double minRate = employes.get(0).getHourlyRateMin();
        Double maxRate = employes.get(0).getHourlyRateMax();
        
        if(maxRate > (2*minRate)) {
            new Recommandation ("Le taux horaire maximal ne doit pas être supérieur à deux fois le taux horaire minimal");
        }
    }
    public static void checkAnnuities(Double totalValue){
        
        Double fedAnnuity = Employe.calculateFederalTax(totalValue);
        Double provAnnuity = Employe.calculateProvincialTax(totalValue);
        
        if(fedAnnuity > 150000){
            new Recommandation ("La rente fédérale à payer nécessite deux versements");
        }
        if(provAnnuity > 75000){
            new Recommandation ("La rente provinciale à payer nécessite deux versements");
        }
    }
    public static void checkRevisionDates(ArrayList<Employe> employes){
        Boolean errDate = false;
        int i = 1;
        while(i < employes.size() && !errDate){
            int monthsDifference = monthsDifference(employes.get(i-1).getSalaryRevisionDate(), employes.get(i).getSalaryRevisionDate());
            if(Math.abs(monthsDifference) > 6) {
                new Recommandation("L'écart maximal entre les dates de révision de salaire des employés d'un même département devrait être de moins de 6 mois");
                errDate = true;
            }
            i++;
        }
    }
    public static void employeListIsValid(JSONArray employes) throws IndexOutOfBoundsException {

        if (employes.isEmpty()) {
            throw new IndexOutOfBoundsException("Employe list is empty");
        } else if (employes.size() > 10) {
            throw new IndexOutOfBoundsException("Employe list should be less than or equal to 10.");
        } 

    }

    public static String invalidFullName(String name, ArrayList <Employe> list) throws IllegalArgumentException {
        for(Employe e:list){
            if (e.getFullName().equals(name)){
                throw new IllegalArgumentException("Two employees should not have the same full name");
            }
        }
        return name;
    }
    
    public static void objContainsAllProperties(JSONObject objet) throws NoSuchFieldException {
        String[] depProperties = {"nom_departement", "type_departement", "taux_horaire_min", "taux_horaire_max", "employes"};

        for (int i = 0; i < depProperties.length; i++) {

            if (!objet.containsKey(depProperties[i])) {
                throw new NoSuchFieldException("Departement missing property : " + depProperties[i]);
            }
        }
    }

    public static void arrContainsAllProperties(JSONArray arr) throws NoSuchFieldException {
        String[] empProperties = {"nom", "nombre_droit_anciennete", "nombre_diplomes", "charge_travail", "date_revision_salaire"};

        for (int i = 0; i < arr.size(); i++) {
            for (int j = 0; j < empProperties.length; j++) {
                if (!arr.getJSONObject(i).containsKey(empProperties[j])) {
                    throw new NoSuchFieldException("Employe array missing property : " + empProperties[j]);
                }
            }
        }
    }
    private static Date stringToDate(String date){
       Date newDate = null;
       try{
            newDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
       
       } catch (ParseException e) {
            e.printStackTrace();
       }
       return newDate;
    }
    private static Integer monthsDifference(String startDate, String endDate){
        
        Date date1 = stringToDate(startDate);
        Date date2 = stringToDate(endDate);
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
            
        return monthsDifference(cal1, cal2);
          
    }
    private static Integer monthsDifference(Calendar startDate, Calendar endDate){
        
        int m1 = startDate.get(Calendar.YEAR) * 12 + startDate.get(Calendar.MONTH);
        int m2 = endDate.get(Calendar.YEAR) * 12 + endDate.get(Calendar.MONTH);
        return m2 - m1;
    }
}
