/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inf;

import static com.inf.Utils.println;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import net.sf.json.*;

/**
 *
 * @author Utilisateur
 */
public class Validation {
    
    //invalidDepartementType ? 
    public static void invalidDepartmentType(Integer departementType) throws IllegalArgumentException {
        
        if ((departementType != 0) || (departementType !=1) || (departementType !=2))  {
            throw new IllegalArgumentException("The type of departement must be 0, 1 or 2.");
        } 
    }
    
    public static void seniorityIsValid(Integer seniority) throws IllegalArgumentException {
        
        if (seniority <= 0)  {
            throw new Exception("The seniority must be greater than or equal to 0.");
        } else if (seniority >= 10) {
            throw new Exception("The seniority must be less than or equal to 10.");
        }
    }
    

    public static void nbDiplomaIsValid(Integer nbDiploma) throws Exception {
        
        if (nbDiploma <= 0)  {
            throw new Exception("The number of diploma must be greater than or equal to 0.");
        } else if (nbDiploma >= 5) {
            throw new Exception("The number of diploma must be less than or equal to 5.");
        }
    }
    
    public static void workedHoursIsValid(Double workedHours) throws Exception {
     
        if ((workedHours < 0) || (workedHours > 1950)) {
            throw new Exception("Employes worked hours must be between 0 and 1950");
        }

    }
    public static void dateIsValid(String date) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date);
        } catch (ParseException e) {
            throw new Exception("Incorrect date format in JSON");
        }
    }
    
    // À regarder si je le mets avec hasEmploye et si le nom est correct.
    public static void sizeListEmployeIsValid(JSONArray employes) throws Exception {

        if (employes.size() > 10) {
            throw new Exception("Employe list should be less than or equal to 10.");
        } 

    }
    
    public static void hasEmploye(JSONArray employes) throws Exception {

        if (employes.isEmpty()) {
            throw new Exception("Employe list is empty");
        } 

    }

    public static void objContainsAllProperties(JSONObject objet) throws Exception {
        String[] depProperties = {"nom_departement", "type_departement", "taux_horaire_min", "taux_horaire_max", "employes"};

        for (int i = 0; i < depProperties.length; i++) {

            if (!objet.containsKey(depProperties[i])) {
                throw new Exception("Departement missing property : " + depProperties[i]);
            }
        }
    }

    public static void arrContainsAllProperties(JSONArray arr) throws Exception {
        String[] empProperties = {"nom", "nombre_droit_anciennete", "nombre_diplomes", "charge_travail", "date_revision_salaire"};

        for (int i = 0; i < arr.size(); i++) {
            for (int j = 0; j < empProperties.length; j++) {
                if (!arr.getJSONObject(i).containsKey(empProperties[j])) {
                    throw new Exception("Employe array missing property : " + empProperties[j]);
                }
            }
        }
    }
}
