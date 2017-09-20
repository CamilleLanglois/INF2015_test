/**
 * Created by davidboutet on 17-09-18.
 */
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.inf.Employe;


public class Application {
    public static void main(String[] args){
        String filePath = args[0];
        String outputFile = args[1];

        //load external json file(name in argument) and return it as a JSONObject
        JSONArray jsonArray = getJsonFromFile(filePath);

        JSONObject jsonObject = outputJson(jsonArray);
        //write JSONObject to json file pass in argument
        println("JSON writing: "+writeJson(jsonObject, outputFile));
    }

    public static JSONArray getJsonFromFile(String source){
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = null;
        try{
            Object object = parser.parse(new FileReader(source));

            jsonArray = (JSONArray)object;

        }catch(FileNotFoundException fe){
            fe.printStackTrace();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return jsonArray;
    }

    //A voir si on devrait le mettre dans la classe Employe.java
    public static JSONObject outputJson(JSONArray jsonArray){
        Map<Long, ArrayList<Employe>> completeEmployeMap = new HashMap<Long, ArrayList<Employe>>();
        JSONObject returnJson = null;
        for (Object json:jsonArray){
            ArrayList<Employe> list = new ArrayList<Employe>();
            JSONObject jObject = (JSONObject)json;
            Long department_type = (Long)jObject.get("type_departement");

            completeEmployeMap.put(department_type, new ArrayList<Employe>());

            ArrayList listEmploye = (ArrayList)jObject.get("employes");

            for (int i = 0; i<listEmploye.size(); i++){
                JSONObject employe = (JSONObject)listEmploye.get(i);
                Employe e = new Employe((String)employe.get("nom"), department_type);
                list.add(e);
            }
            completeEmployeMap.put(department_type, list);
        }
        try{
            returnJson = new JSONObject(completeEmployeMap);
        }catch (Exception e){
            println(e);
        }
        return returnJson;

    }

    public static Boolean writeJson(JSONObject json, String filename){
        Boolean succeed = false;
        try {
            FileWriter file = new FileWriter("output/"+filename);
            file.write(json.toJSONString());
            file.flush();
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
