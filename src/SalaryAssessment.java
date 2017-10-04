/**
 * Created by davidboutet on 17-09-18.
 */
import java.io.FileNotFoundException;
import com.inf.Utils;
import net.sf.json.*;

public class SalaryAssessment {
    public static void main(String[] args){
        String filePath = args[0], outputFile = args[1];
        try{
            JSONObject jsonObject = Utils.getJsonFromFile(filePath);
            Utils.createEmployeFromJson(jsonObject);
            Utils.writeJson(outputFile);
        }catch (FileNotFoundException e){
            Utils.writeErrorJson(outputFile, e.getMessage());
        }
    }
}
