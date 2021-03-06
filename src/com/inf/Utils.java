package com.inf;

import manage.file.FileManager;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by davidboutet on 17-10-03.
 */
public class Utils {

    public static JSONObject getJsonFromFile(String source, Boolean exception) throws FileNotFoundException {
        JSONObject jsonObject;
        try{
            File file = new File(source);
            if(!file.canRead()){
                new FileOutputStream(file, false);
            }

            String myJSON = FileManager.createStringFromFileContent(source, "");
            if(!myJSON.isEmpty()){
                jsonObject = JSONObject.fromObject(myJSON);
            }else if(!exception){
                jsonObject = new JSONObject();
            } else {
               throw new FileNotFoundException("Input json path could not be found.("+source+")"); 
            }
            
        }catch(Exception e){
            throw new FileNotFoundException("Input json path could not be found.("+source+")");
        }
        return jsonObject;
    }

    public static void createEmployeFromJson(JSONObject jsonObject)throws Exception{
        Validation.objContainsAllProperties(jsonObject);
        Double minRate = Employe.stringToDouble(jsonObject.getString("taux_horaire_min")),
               maxRate = Employe.stringToDouble(jsonObject.getString("taux_horaire_max"));
        JSONArray employeArray = jsonObject.getJSONArray("employes");
        Validation.employeListIsValid(employeArray);
        Validation.arrContainsAllProperties(employeArray);
        for (Object e:employeArray){
            JSONObject employe = (JSONObject)e;
            new Employe(employe.getString("nom"), jsonObject.getInt("type_departement"), minRate, maxRate, employe.getInt("nombre_diplomes"), employe.getInt("nombre_droit_anciennete"), employe.getDouble("charge_travail"), employe.getString("date_revision_salaire"));
        }
    }

    public static void writeJson(String filename, JSONObject json){
        try {
            FileManager.createFileFromStringContent("output", filename, json.toString());
        }catch (Exception e){
            println("The file could not be written.");
        }
    }

    public static void writeErrorJson(String filename, String errorMessage){
        try {
            JSONObject jsonError = new JSONObject();
            jsonError.accumulate("error", errorMessage);
            FileManager.createFileFromStringContent("output", filename, jsonError.toString());
        }catch (Exception e){
            println("The file could not be written.");
        }
    }

    public static JSONObject formatJson(ArrayList<Employe> listEmploye){
        JSONArray salaries = new JSONArray();
        Double totalValue = Employe.FIXED_AMOUNT;
        try{
            for(Employe e:listEmploye){
                totalValue += e.calculateTotalSalary();
                salaries.add(e.toJSONString());
            }
        }catch (Exception e){
            println(e.getMessage());
        }
        Validation.checkResultValues(listEmploye, totalValue);
        JSONArray recomm = formatJsonRecommandation(Recommandation.recommandationList);
        return addToJson(totalValue, Employe.calculateProvincialTax(totalValue), Employe.calculateFederalTax(totalValue), salaries, recomm);
    }
    public static JSONArray formatJsonRecommandation(ArrayList<Recommandation> listRecommandation){
        JSONArray recommandations = new JSONArray();
        try {
            for(Recommandation r:listRecommandation ){
                recommandations.add(r.toString());
            }
        } catch (Exception e){
            println(e.getMessage());
        }
        return recommandations;
    }
    
    
    public static JSONObject addToJson(Double totalValue, Double totalAnnuityProvincial, Double totalAnnuityFederal, JSONArray salaries, JSONArray recomm){
        JSONObject json = new JSONObject();
        json.accumulate("total_value", Employe.twoDigits(Employe.roundToFive(totalValue))+" $");
        json.accumulate("provincial_annuity", Employe.twoDigits(Employe.roundToFive(totalAnnuityProvincial))+" $");
        json.accumulate("federal_annuity", Employe.twoDigits(Employe.roundToFive(totalAnnuityFederal))+" $");
        json.accumulate("salary", salaries);
        if(!recomm.isEmpty()){
            json.accumulate("recommendations",recomm);
        }
        return json;
    }
    public static void writeJsonHistory(String filename, JSONObject jsonObject){
        try {
            FileManager.createFileFromStringContent("output", filename, jsonObject.toString());
        }catch (Exception e){
            println("The file could not be written.");
        }
    }

    public static void println(Object o){
        System.out.println(o);
    }
}
