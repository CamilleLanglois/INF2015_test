/**
 * Created by davidboutet on 17-09-18.
 */
import java.io.FileNotFoundException;
import java.util.ArrayList;
import manage.file.*;
import net.sf.json.*;
import com.inf.Employe;

public class SalaryAssessment {
    public static void main(String[] args){
        String filePath = args[0], outputFile = args[1];
        try{
            JSONObject jsonObject = getJsonFromFile(filePath);
            createEmployeFromJson(jsonObject);
            writeJson(outputFile);
        }catch (FileNotFoundException e){
            writeErrorJson(outputFile, e.getMessage());
        }
    }

    public static JSONObject getJsonFromFile(String source) throws FileNotFoundException{
        JSONObject jsonObject;
        try{
            String myJSON = FileManager.createStringFromFileContent(source, "");
            jsonObject = JSONObject.fromObject(myJSON);
        }catch(Exception e){
            throw new FileNotFoundException("Input json path could not be found.("+source+")");
        }
        return jsonObject;
    }

    //A voir si on devrait le mettre dans la classe Employe.java
    public static void createEmployeFromJson(JSONObject jsonObject){
        Double taux_min = Employe.stringToDouble(jsonObject.getString("taux_horaire_min")),
               taux_max = Employe.stringToDouble(jsonObject.getString("taux_horaire_max"));
        JSONArray employeArray = jsonObject.getJSONArray("employes");
        for (int i = 0; i<employeArray.size(); i++){
            JSONObject employe = (JSONObject)employeArray.get(i);
            new Employe(employe.getString("nom"), jsonObject.getInt("type_departement"), taux_min, taux_max, employe.getInt("nombre_diplomes"), employe.getInt("nombre_droit_anciennete"), employe.getDouble("charge_travail"));
        }
    }

    public static void writeJson(String filename) {
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

    public static JSONObject formatJson(ArrayList<Employe> listEmploye) {
        JSONObject json = new JSONObject();
        JSONArray salaires = new JSONArray();
        Double total_value = Employe.FIXED_AMOUNT,
               total_rente_provincial = 0.00,
               total_rente_federal = 0.00;
        for(Employe e:listEmploye){
            total_value += e.getTotalSalary();
            salaires.add(e.toJSONString());
        }
        total_rente_provincial = Employe.calculRenteProvincial(total_value);
        total_rente_federal = Employe.calculRenteFederal(total_value);
        json.accumulate("valeur_total", Employe.twoDigits(Employe.roundToFive(total_value))+" $");
        json.accumulate("rente_provinciale", Employe.twoDigits(Employe.roundToFive(total_rente_provincial))+" $");
        json.accumulate("rente_federal", Employe.twoDigits(Employe.roundToFive(total_rente_federal))+" $");
        json.accumulate("salaires", salaires);
        return json;
    }

    public static void println(Object o){
        System.out.println(o);
    }
}
