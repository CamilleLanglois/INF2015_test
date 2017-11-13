/**
 * Created by davidboutet on 17-09-18.
 */

import com.inf.Employe;
import com.inf.History;
import com.inf.Utils;
import net.sf.json.*;

public class SalaryAssessment {
    public static void main(String[] args){
        String filePath = args[0], outputFile = args[1], historyAction = args.length>2?args[2]:"";
        try{
            JSONObject jsonObject = Utils.getJsonFromFile(filePath);
            Utils.createEmployeFromJson(jsonObject);
            Utils.writeJson(outputFile, Utils.formatJson(Employe.finalEmployeList));
            new History(Employe.finalEmployeList, historyAction);
        }catch (Exception e){
            Utils.writeErrorJson(outputFile, e.getMessage());
        }
    }
}
