package com.inf;

import manage.file.FileManager;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by davidboutet on 17-10-03.
 */
public class Utils {

    public static JSONObject getJsonFromFile(String source) throws FileNotFoundException {
        JSONObject jsonObject;
        try{
            String myJSON = FileManager.createStringFromFileContent(source, "");
            jsonObject = JSONObject.fromObject(myJSON);
            
        }catch(Exception e){
            throw new FileNotFoundException("Input json path could not be found.("+source+")");
        }
        return jsonObject;
    }

    public static JSONArray getJsonArrayFromFile(String source) throws FileNotFoundException {
        JSONArray jsonArray;
        try{
            File historyFile = new File("output/history.json");
            if(!historyFile.canRead()){
                new FileOutputStream(historyFile, false);
            }
            String myJSON = FileManager.createStringFromFileContent(source, "");
            if(!myJSON.isEmpty()){
                jsonArray = JSONArray.fromObject(myJSON);
            }else{
                jsonArray = new JSONArray();
            }


        }catch(Exception e){
            throw new FileNotFoundException("Input json path could not be found.("+source+")");
        }
        return jsonArray;
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

    public static void writeJson(String filename){
        JSONObject json = formatJson(Employe.finalEmployeList);
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
        return addToJson(totalValue, Employe.calculateProvincialTax(totalValue), Employe.calculateFederalTax(totalValue), salaries);
    }

    public static JSONObject addToJson(Double totalValue, Double totalAnnuityProvincial, Double totalAnnuityFederal, JSONArray salaries){
        JSONObject json = new JSONObject();
        json.accumulate("valeur_total", Employe.twoDigits(Employe.roundToFive(totalValue))+" $");
        json.accumulate("rente_provinciale", Employe.twoDigits(Employe.roundToFive(totalAnnuityProvincial))+" $");
        json.accumulate("rente_federal", Employe.twoDigits(Employe.roundToFive(totalAnnuityFederal))+" $");
        json.accumulate("salaires", salaries);
        return json;
    }

    public static void writeJsonHistory(String filename, JSONArray jsonArray){
        try {
            FileManager.createFileFromStringContent("output", filename, jsonArray.toString());
        }catch (Exception e){
            println("The file could not be written.");
        }
    }

    public static void println(Object o){
        System.out.println(o);
    }
}
