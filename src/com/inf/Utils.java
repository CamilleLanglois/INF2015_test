package com.inf;

import manage.file.FileManager;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.io.FileNotFoundException;
import java.util.ArrayList;

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

    public static void createEmployeFromJson(JSONObject jsonObject)throws Exception{
        Validation.objContainsAllProperties(jsonObject);
        Double minRate = Employe.stringToDouble(jsonObject.getString("taux_horaire_min")),
               maxRate = Employe.stringToDouble(jsonObject.getString("taux_horaire_max"));
        JSONArray employeArray = jsonObject.getJSONArray("employes");
        Validation.hasEmploye(employeArray);
        Validation.arrContainsAllProperties(employeArray);
        for (Object e:employeArray){
            JSONObject employe = (JSONObject)e;
            // rassembler validation des propriétés dans classe Validation
          //erreur je vais y penser et le changer demain-Jade  Validation.nbDiplomaIsValid(employe.getInteger("nombre_diplomes"));
            Validation.dateIsValid(employe.getString("date_revision_salaire"));
            Validation.workedHoursIsValid(employe.getDouble("charge_travail"));
            //
            new Employe(employe.getString("nom"), jsonObject.getInt("type_departement"), minRate, maxRate, employe.getInt("nombre_diplomes"), employe.getInt("nombre_droit_anciennete"), employe.getDouble("charge_travail"));
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
                totalValue += e.getTotalSalary();
                salaries.add(e.toJSONString());
            }
        }catch (Exception e){
            println(e.getMessage());
        }
        return addToJson(totalValue, Employe.calculRenteProvincial(totalValue), Employe.calculRenteFederal(totalValue), salaries);
    }

    public static JSONObject addToJson(Double totalValue, Double totalAnnuityProvincial, Double totalAnnuityFederal, JSONArray salaries){
        JSONObject json = new JSONObject();
        json.accumulate("valeur_total", Employe.twoDigits(Employe.roundToFive(totalValue))+" $");
        json.accumulate("rente_provinciale", Employe.twoDigits(Employe.roundToFive(totalAnnuityProvincial))+" $");
        json.accumulate("rente_federal", Employe.twoDigits(Employe.roundToFive(totalAnnuityFederal))+" $");
        json.accumulate("salaires", salaries);
        return json;
    }

    public static void println(Object o){
        System.out.println(o);
    }
}
