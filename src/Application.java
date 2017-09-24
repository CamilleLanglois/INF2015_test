/**
 * Created by davidboutet on 17-09-18.
 */
import java.io.FileNotFoundException;
import java.util.ArrayList;

import manage.file.*;
import net.sf.json.*;


import com.inf.Employe;


public class Application {
    static ArrayList<Employe> finalEmployeList = new ArrayList<Employe>();
    public static void main(String[] args){
        String filePath = args[0];
        String outputFile = args[1];

        //load external json file(name in argument) and return it as a JSONObject
        JSONObject jsonObject = getJsonFromFile(filePath);
        
        //parse JSON object to required output format
        jsonToEmployeList(jsonObject);

        //write JSONObject to json file pass in argument
        println("JSON writing: "+writeJson(outputFile));
    }

    public static JSONObject getJsonFromFile(String source){
        JSONObject jsonObject = null;
        try{
            String myJSON = FileManager.createStringFromFileContent(source, "");
            jsonObject = JSONObject.fromObject(myJSON);
        }catch(FileNotFoundException fe){
            fe.printStackTrace();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return jsonObject;
    }

    //A voir si on devrait le mettre dans la classe Employe.java
    public static Boolean jsonToEmployeList(JSONObject jsonObject){
        Boolean succeed = false;
        try{
            Integer department_type = jsonObject.getInt("type_departement");
            Double taux_min = Employe.stringToDouble(jsonObject.getString("taux_horaire_min")),
                   taux_max = Employe.stringToDouble(jsonObject.getString("taux_horaire_max"));
            JSONArray employeArray = jsonObject.getJSONArray("employes");
            for (int i = 0; i<employeArray.size(); i++){
                JSONObject employe = (JSONObject)employeArray.get(i);
                Integer nbDiploma = employe.getInt("nombre_diplomes"),
                        seniority = employe.getInt("nombre_droit_anciennete");
//                Double hour_rate = employe.getDouble("charge_travail");
                Double worked_hour = employe.getDouble("charge_travail");
                Employe e = new Employe(employe.getString("nom"), department_type, taux_min, taux_max, nbDiploma, seniority, 0.00, worked_hour);
                finalEmployeList.add(e);
            }
            succeed = true;
        }catch (Exception e){
            println(e.getMessage());
        }
        return succeed;
    }

    public static Boolean writeJson(String filename){
        Boolean succeed = false;
        JSONObject json = formatJson(finalEmployeList);
        try {
            FileManager.createFileFromStringContent("output", filename, json.toString());
            succeed = true;
        }catch (Exception e){
            println(e.getMessage());
        }
        return succeed;
    }

    public static JSONObject formatJson(ArrayList<Employe> listEmploye){
        JSONObject json = new JSONObject();
        Double total_value = 0.00, // ajouter la constante MONTANT_FIXE
               total_rente_provincial = 0.00,
               total_rente_federal = 0.00;
        for(Employe e:listEmploye){
            total_value += e.getTotalSalary();
            total_rente_provincial += e.calculRenteProvincial();
            total_rente_federal += e.calculRenteFederal();
            json.accumulate("salaires", e.toJSONString());
        }
        json.accumulate("valeur_total", Employe.roundToFive(total_value)+" $");
        json.accumulate("rente_provinciale", Employe.roundToFive(total_rente_provincial)+" $");
        json.accumulate("rente_federal", Employe.roundToFive(total_rente_federal)+" $");
        return json;
    }

    public static void println(Object o){
        System.out.println(o);
    }
}
