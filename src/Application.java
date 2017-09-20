/**
 * Created by davidboutet on 17-09-18.
 */
import java.io.FileNotFoundException;
import java.io.FileWriter;
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
        println("JSON writing: "+writeJson(jsonObject, outputFile));
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
            JSONArray employeArray = jsonObject.getJSONArray("employes");
            for (int i = 0; i<employeArray.size(); i++){
                JSONObject employe = (JSONObject)employeArray.get(i);
                Employe e = new Employe(employe.getString("nom"), department_type);

                finalEmployeList.add(e);
            }
            succeed = true;
        }catch (Exception e){
            println(e);
        }

        return succeed;
    }

    public static Boolean writeJson(JSONObject json, String filename){
        Boolean succeed = false;
        try {
            FileManager.createFileFromStringContent("output", filename, json.toString());
            succeed = true;
        }catch (Exception e){
            println(e);
        }
        return succeed;
    }

    public static void println(Object o){
        System.out.println(o);
    }
}
