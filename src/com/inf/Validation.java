package com.inf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    
    
    
    
    public static void employeListIsValid(JSONArray employes) throws IndexOutOfBoundsException {

        if (employes.isEmpty()) {
            throw new IndexOutOfBoundsException("Employe list is empty");
        } else if (employes.size() > 10) {
            throw new IndexOutOfBoundsException("Employe list should be less than or equal to 10.");
        } 

    }
    
    // À VÉRIFIER 
    public static void invalidFullName(JSONArray employes) throws IllegalArgumentException {
        
        for (int i = 0; i < employes.size(); i++) {
            JSONArray t[] = (JSONArray[]) employes.get(i);
            for (int j = 0; j < employes.size(); j++) {
              if (t[0]==(employes.getJSONArray(j))) {
                throw new IllegalArgumentException("Two employees should not have the same full name");
              }
            }       
        }
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
    
    
    
}
